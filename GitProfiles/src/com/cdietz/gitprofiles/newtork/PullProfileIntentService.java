package com.cdietz.gitprofiles.newtork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.cdietz.gitprofiles.profile.MainProfile;

/**
 * This Service is intended to search for the main profile of a particular user.
 * The data of the profile will be placed in to a Bundle and put in to the Extras
 * of the broadcasted Intent upon a successful completion.
 * @author Christopher Dietz
 *
 */
public class PullProfileIntentService extends PullIntentService {
    private static final String TAG = PullProfileIntentService.class.getSimpleName();
    
    /**
     * Sent when a full new profile has been loaded
     */
    public static final String BROADCAST_NEW_MAIN_PROFILE = "com.cdietz.gitprofiles.newprofile";
    /**
     * Sent when the profile was not found
     */
    public static final String BROADCAST_NO_PROFILE_FOUND = "com.cdietz.gitprofiles.notfound";
    
    /**
     * The Bundle key containing information for the new MainProfile object.
     * The Bundle will be in the extras of the broadcasted intent. 
     */
    public static final String BROADCAST_BUNDLE_ARG_NEW_PROFILE = "newprofile";
    
    /**
     * Bundle key for the profile name to search for.  Put this in the extras
     * of the Intent that starts the Service.
     * Intent intent = new Intent(PullProfileIntentService.class);
     */
    public static final String BUNDLE_STRING_ARG_PROFILE_NAME = "pname";
    
    /**
     * Base URL for the public profiles of the users.
     */
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
            final InputStream is = connection.getInputStream();
            final BufferedReader in = new BufferedReader(new InputStreamReader(is));

            // TODO: Check response codes.
            final StringBuilder builder = new StringBuilder();
            String line;
            while((line = in.readLine()) != null) {
                builder.append(line);
            }
            
            final String result = builder.toString();
            
            final JSONObject root = MainProfile.validate(result);
            if(root != null) {
                final MainProfile mp = new MainProfile(root);
                broadcastNewMainProfile(mp);
            } else {
                broadcastNoProfileFound();
            }
            
        } catch (MalformedURLException e) {
           broadcastBadName();
        } catch (IOException e) {
            broadcastConnectionError();
        } catch (JSONException e) {
            broadcastReadError();
        }
    }
    
    /**
     * Send the broadcast for a successfully retrieved profile.
     * A Bundle containing the profile's information will be packaged in the 
     * Intent's Extras.
     * @param mp
     *          The MainProfile containing the information.
     */
    protected void broadcastNewMainProfile(MainProfile mp) {
        final Intent broadcastIntent = new Intent(BROADCAST_NEW_MAIN_PROFILE);
        broadcastIntent.putExtra(BROADCAST_BUNDLE_ARG_NEW_PROFILE, mp.toBundle());
        sendBroadcast(broadcastIntent);
    }
    
    /**
     * Send the broadcast that the profile was not found.
     */
    protected void broadcastNoProfileFound() {
        final Intent broadcastIntent = new Intent(BROADCAST_NO_PROFILE_FOUND);
        sendBroadcast(broadcastIntent);
    }
}
