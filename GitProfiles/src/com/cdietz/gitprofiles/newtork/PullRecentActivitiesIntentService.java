package com.cdietz.gitprofiles.newtork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;

import com.cdietz.gitprofiles.profile.Event;

import android.content.Intent;
import android.os.Bundle;

public class PullRecentActivitiesIntentService extends PullIntentService {
    public static final String TAG = PullRecentActivitiesIntentService.class
            .getSimpleName();

    public static final String BROADCAST_NEW_ACTIVITY_LIST = "com.cdietz.gitprofiles.newactivitylist";

    public static final String BROADCAST_BUNDLE_ARG_EVENT_LIST = "eventlist";
    
    public static final String BUNDLE_STRING_ARG_USER_NAME = "username";
    
    private static final String ACTIVITY_LIST_URL = "https://api.github.com/users/UNAME/events/public";

    public PullRecentActivitiesIntentService() {
        super(TAG);
    }

    private Bundle downloadPage(String baseUrl, int page) throws MalformedURLException, IOException, JSONException {
        final String urlString = baseUrl + "?page=" + page;
        HttpsURLConnection connection;
        connection = openInputConnection(urlString, "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));

        final StringBuilder builder = new StringBuilder();
        String line;
         while ((line = in.readLine()) != null) {
             builder.append(line);
         }

         final String result = builder.toString();
         final JSONArray activities = new JSONArray(result);
         final int count = activities.length();
         final Bundle eventList = new Bundle();
         Event newEvent;
         for(int i = 0; i < count; i++) {
             newEvent = new Event(activities.getJSONObject(i));
             eventList.putBundle(String.valueOf(i), newEvent.toBundle());
         }
         return eventList;
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        final String userName;
        if (extras == null
                || (userName = extras
                        .getString(BUNDLE_STRING_ARG_USER_NAME)) == null) {
            return;
        }

        final String urlString = ACTIVITY_LIST_URL.replace("UNAME", userName);
        
        // The Github API does not all more than 10 pages worth of activities.
        // Once you run out of pages, you just get empty JSON arrays.
        int page = 1;
        Bundle eventList;
        try {
            do {
                eventList = downloadPage(urlString, page);
                broadcastNewEvents(eventList);
            } while (eventList.size() > 0 && page++ <= 10);
        } catch (MalformedURLException e) {
            // TODO url was bad
        } catch (IOException e) {
            broadcastConnectionError();
        } catch (JSONException e) {
            broadcastReadError();
            e.printStackTrace();
        }
    }
    
    public void broadcastNewEvents(Bundle eventList) {
        final Intent broadcastIntent = new Intent(BROADCAST_NEW_ACTIVITY_LIST);
        broadcastIntent.putExtra(BROADCAST_BUNDLE_ARG_EVENT_LIST, eventList);
        sendBroadcast(broadcastIntent);
    }
}
