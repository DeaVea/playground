package com.cdietz.gitprofiles.newtork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.cdietz.gitprofiles.profile.MainProfile;

public class PullProfileIntentService extends PullIntentService {
    private static final String TAG = PullProfileIntentService.class.getSimpleName();
    
    public static final String BROADCAST_NEW_MAIN_PROFILE = "com.cdietz.gitprofiles.newprofile";
    public static final String BROADCAST_BAD_PROFILE_NAME = "com.cdietz.gitprofiles.badname";
    public static final String BROADCAST_NO_PROFILE_FOUND = "com.cdietz.gitprofiles.notfound";
    
    public static final String BROADCAST_BUNDLE_ARG_NEW_PROFILE = "newprofile";
    
    public static final String BUNDLE_STRING_ARG_PROFILE_NAME = "pname";
    
    private static final String BASE_URL = "https://api.github.com/users/";
    
    
    public PullProfileIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        final String profileName;
        if(extras == null || (profileName = extras.getString(BUNDLE_STRING_ARG_PROFILE_NAME)) == null) {
            return;
        }
        
        final String fullUrlString = BASE_URL + profileName.trim();

        try {
            final HttpsURLConnection connection = openInputConnection(fullUrlString, "application/json");
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // TODO: Check response codes.
            final StringBuilder builder = new StringBuilder();
            String line;
            while((line = in.readLine()) != null) {
                builder.append(line);
            }
            
            final String result = builder.toString();
            
            final JSONObject root = MainProfile.validate(result);
//            if(root == null) {
                final MainProfile mp = new MainProfile(root);
                broadcastNewMainProfile(mp);
//            } else {
//                broadcastNoProfileFound();
//            }
            
        } catch (MalformedURLException e) {
            // TODO: URL was bad meaning profile was bad.
        } catch (IOException e) {
            broadcastConnectionError();
        } catch (JSONException e) {
            broadcastReadError();
        }
    }
    
    protected void broadcastNewMainProfile(MainProfile mp) {
        final Intent broadcastIntent = new Intent(BROADCAST_NEW_MAIN_PROFILE);
        broadcastIntent.putExtra(BROADCAST_BUNDLE_ARG_NEW_PROFILE, mp.toBundle());
        sendBroadcast(broadcastIntent);
    }
    
    protected void broadcastNoProfileFound() {
        final Intent broadcastIntent = new Intent(BROADCAST_NO_PROFILE_FOUND);
        sendBroadcast(broadcastIntent);
    }
    
    /**
     * Opens a working connection to the given URL.
     * @return
     *      Working connection or null if exceptions are thrown.
     *          
     */
    protected HttpsURLConnection openInputConnection(String urlString, String acceptType) throws MalformedURLException, IOException {
        final String acceptFormat = (acceptType != null) ? acceptType : "text/plain";
        final URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("Accept", acceptFormat);
        connection.setDoInput(true);
        connection.setDoOutput(false);
        return connection;
    }
}
