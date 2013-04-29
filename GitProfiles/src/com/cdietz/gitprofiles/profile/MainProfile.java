package com.cdietz.gitprofiles.profile;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

/**
 * A Main profile in this case is the top-level user profile
 * for a github site.
 * All methods in this class assume valid parsable JSON
 * elements so it must be validated prior to using.
 * @author Christopher Dietz
 *
 */
public class MainProfile implements ProfileElement {
    
    /**
     * All these elements correspond with a JSON string involving the main profile element
     */
    public static final String ARG_JSON_USER_NAME = "login";
    public static final String ARG_JSON_USER_ID = "id";
    public static final String ARG_JSON_AVATAR_URL = "avatar_url";
    public static final String ARG_JSON_DATA_URL = "url";
    public static final String ARG_JSON_HTML_URL = "html_url";
    public static final String ARG_JSON_REAL_NAME = "name";
    public static final String ARG_JSON_BIO = "bio";
    
    public final String mUserName;
    public final String mUserId;
    public final String mAvatarUrl;
    public final String mDataUrl;
    public final String mHtmlUrl;
    public final String mRealName;
    public final String mBio;
    public final ArrayList<Event> mEvents;
    
    /**
     * This is the parameter mapped to the events maintained by this profile.
     * It will be a JSONArray if taken from a JSONObject or a Bundle if taken
     * from a Bundle.  The keys of each will be number values from "0" - "n - 1" where
     * "n" is the number of events.
     */
    public static final String EVENTS_CLUSTER = "events_array";
    
    public MainProfile(JSONObject object) throws JSONException {
        mUserName = ParseTools.getStringOrDefault(object, ARG_JSON_USER_NAME, "NA");
        mUserId = ParseTools.getStringOrDefault(object, ARG_JSON_USER_ID, "NA");
        mAvatarUrl = ParseTools.getStringOrDefault(object, ARG_JSON_AVATAR_URL, "NA");
        mDataUrl = ParseTools.getStringOrDefault(object, ARG_JSON_DATA_URL, "NA");
        mHtmlUrl = ParseTools.getStringOrDefault(object, ARG_JSON_HTML_URL, "NA");
        mRealName = ParseTools.getStringOrDefault(object, ARG_JSON_REAL_NAME, "NA");
        mBio = ParseTools.getStringOrDefault(object, ARG_JSON_BIO, "NA");
        mEvents = new ArrayList<Event>();
    }
    
    public MainProfile(Bundle object) {
        mUserName = ParseTools.getStringOrDefault(object, ARG_JSON_USER_NAME, "NA");
        mUserId = ParseTools.getStringOrDefault(object, ARG_JSON_USER_ID, "NA");
        mAvatarUrl = ParseTools.getStringOrDefault(object, ARG_JSON_AVATAR_URL, "NA");
        mDataUrl = ParseTools.getStringOrDefault(object, ARG_JSON_DATA_URL, "NA");
        mHtmlUrl = ParseTools.getStringOrDefault(object, ARG_JSON_HTML_URL, "NA");
        mRealName = ParseTools.getStringOrDefault(object, ARG_JSON_REAL_NAME, "NA");
        mBio = ParseTools.getStringOrDefault(object, ARG_JSON_BIO, "NA");
        mEvents = new ArrayList<Event>();
    }
    
    @Override
    public JSONObject toJson() throws JSONException {
        final JSONArray events = new JSONArray();
        for(Event ev : mEvents) {
            events.put(ev.toJson());
        }
        
        final JSONObject root = new JSONObject();
        root.put(ARG_JSON_USER_NAME, mUserName)
        .put(ARG_JSON_USER_ID, mUserId)
        .put(ARG_JSON_AVATAR_URL, mAvatarUrl)
        .put(ARG_JSON_DATA_URL, mDataUrl)
        .put(ARG_JSON_HTML_URL, mHtmlUrl)
        .put(ARG_JSON_REAL_NAME, mRealName)
        .put(ARG_JSON_BIO, mBio)
        .put(EVENTS_CLUSTER, events);
        
        return root;
    }
    
    @Override
    public Bundle toBundle() {
        final Bundle events = new Bundle();
        final int count = mEvents.size();
        for(int i = 0; i < count; i++) {
            events.putBundle(String.valueOf(i), mEvents.get(i).toBundle());
        }
        
        final Bundle retBundle = new Bundle();
        retBundle.putString(ARG_JSON_USER_NAME, mUserName);
        retBundle.putString(ARG_JSON_USER_ID, mUserId);
        retBundle.putString(ARG_JSON_AVATAR_URL, mAvatarUrl);
        retBundle.putString(ARG_JSON_DATA_URL, mDataUrl);
        retBundle.putString(ARG_JSON_HTML_URL, mHtmlUrl);
        retBundle.putString(ARG_JSON_REAL_NAME, mRealName);
        retBundle.putString(ARG_JSON_BIO, mBio);
        retBundle.putBundle(EVENTS_CLUSTER, events);
        
        return retBundle;
    }
    
    /**
     * Add an event to the profile. If the event is null, it will be ignored.
     * @param ev
     *      Event to add.
     */
    public void addEvent(Event ev) {
        if(ev != null) {
            mEvents.add(ev);
        }
    }
    
    /**
     * This returns a parsed JObject to the root node
     * of a JSON string.  Returns NULL if the string
     * can not be parsed in to JSON or is incomplete.
     * @param candidate
     *      The json to parse
     * @return
     *      The root JObject of the string or NULL if
     *      it can not be parsed or there are missing elements.
     */
    public static JSONObject validate(String candidate) {
        JSONObject object;
        try{
            object = new JSONObject(candidate);
        } catch (JSONException e) {
            object = null;
        }
        return object;
    }
}
