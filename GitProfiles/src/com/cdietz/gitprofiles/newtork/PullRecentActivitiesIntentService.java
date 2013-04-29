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

/**
 * This will download all events associated with the given profile.
 * Based on Githup API documentation, it is only possible to download
 * one page of 30 events for up to a total of 10 pages.  Each found list
 * can have 0 to 30 events.
 * @author Christopher Dietz
 *
 */
public class PullRecentActivitiesIntentService extends PullIntentService {
    public static final String TAG = PullRecentActivitiesIntentService.class
            .getSimpleName();

    /**
     * A new list of activities downloaded.
     */
    public static final String BROADCAST_NEW_ACTIVITY_LIST = "com.cdietz.gitprofiles.newactivitylist";

    /**
     * The key for the Extra Bundle in the broadcast Intent.
     */
    public static final String BROADCAST_BUNDLE_ARG_EVENT_LIST = "eventlist";
    
    /**
     * The key for the username parameter for the user to search for.
     */
    public static final String BUNDLE_STRING_ARG_USER_NAME = "username";
    
    /**
     * This is the base API for the public activities of a given user.
     * Replace UNAME with the username provided to start the downloaded.
     * In order to specify the page, you must append "?page=PAGE_NUM" where
     * PAGE_NUM is the number to download.
     * 
     */
    private static final String ACTIVITY_LIST_URL = "https://api.github.com/users/UNAME/events/public";

    public PullRecentActivitiesIntentService() {
        super(TAG);
    }

    /**
     * Download the events of a specific page.
     * @param baseUrl
     *          The URL to download from.  Must include the username inside.
     * @param page
     *          The page to download
     * @return
     *          A Bundle containing all the events.  Their key will be a number from 
     *          "0" to "n - 1" where "n" is the total number of events.
     *          If no events were found, then the Bundle will be empty.
     * @throws MalformedURLException
     *          The URL provided was bad.
     * @throws IOException
     *          An error connecting or reading the data.
     * @throws JSONException
     *          The data found was not valid JSON data.
     */
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
            // Only reason this should happen is the username was bad.
            broadcastBadName();
        } catch (IOException e) {
            broadcastConnectionError();
        } catch (JSONException e) {
            broadcastReadError();
            e.printStackTrace();
        }
    }
    
    /**
     * Send the broadcast of new events list.  The list will be a Bundle
     * containing all the events.  Their keys will be numbers from "0" to 
     * "n - 1" where "n" is the number of events in the list.
     * @param eventList
     *          The key to the Extra Bundle within the intent.
     */
    protected void broadcastNewEvents(Bundle eventList) {
        final Intent broadcastIntent = new Intent(BROADCAST_NEW_ACTIVITY_LIST);
        broadcastIntent.putExtra(BROADCAST_BUNDLE_ARG_EVENT_LIST, eventList);
        sendBroadcast(broadcastIntent);
    }
}
