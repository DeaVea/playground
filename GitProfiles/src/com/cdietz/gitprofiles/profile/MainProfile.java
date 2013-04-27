package com.cdietz.gitprofiles.profile;

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
public class MainProfile {
    
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
    
    /**
     * TODO: Keep this up to date for easy traversal of the arguments
     */
    private static String[] JSON_ELEMENT_ARRAY = { 
        ARG_JSON_USER_NAME, 
        ARG_JSON_USER_ID, 
        ARG_JSON_AVATAR_URL,
        ARG_JSON_DATA_URL,
        ARG_JSON_HTML_URL,
        ARG_JSON_REAL_NAME,
        ARG_JSON_BIO
    };
    
    public final String mUserName;
    public final String mUserId;
    public final String mAvatarUrl;
    public final String mDataUrl;
    public final String mHtmlUrl;
    public final String mRealName;
    public final String mBio;
    
    public MainProfile(JSONObject object) throws JSONException {
        mUserName = ParseTools.getStringOrDefault(object, ARG_JSON_USER_NAME, "NA");
        mUserId = ParseTools.getStringOrDefault(object, ARG_JSON_USER_ID, "NA");
        mAvatarUrl = ParseTools.getStringOrDefault(object, ARG_JSON_AVATAR_URL, "NA");
        mDataUrl = ParseTools.getStringOrDefault(object, ARG_JSON_DATA_URL, "NA");
        mHtmlUrl = ParseTools.getStringOrDefault(object, ARG_JSON_HTML_URL, "NA");
        mRealName = ParseTools.getStringOrDefault(object, ARG_JSON_REAL_NAME, "NA");
        mBio = ParseTools.getStringOrDefault(object, ARG_JSON_BIO, "NA");
    }
    
    public MainProfile(Bundle object) {
        mUserName = ParseTools.getStringOrDefault(object, ARG_JSON_USER_NAME, "NA");
        mUserId = ParseTools.getStringOrDefault(object, ARG_JSON_USER_ID, "NA");
        mAvatarUrl = ParseTools.getStringOrDefault(object, ARG_JSON_AVATAR_URL, "NA");
        mDataUrl = ParseTools.getStringOrDefault(object, ARG_JSON_DATA_URL, "NA");
        mHtmlUrl = ParseTools.getStringOrDefault(object, ARG_JSON_HTML_URL, "NA");
        mRealName = ParseTools.getStringOrDefault(object, ARG_JSON_REAL_NAME, "NA");
        mBio = ParseTools.getStringOrDefault(object, ARG_JSON_BIO, "NA");
    }
    
    public Bundle toBundle() {
        final Bundle retBundle = new Bundle();
        
        retBundle.putString(ARG_JSON_USER_NAME, mUserName);
        retBundle.putString(ARG_JSON_USER_ID, mUserId);
        retBundle.putString(ARG_JSON_AVATAR_URL, mAvatarUrl);
        retBundle.putString(ARG_JSON_DATA_URL, mDataUrl);
        retBundle.putString(ARG_JSON_HTML_URL, mHtmlUrl);
        retBundle.putString(ARG_JSON_REAL_NAME, mRealName);
        retBundle.putString(ARG_JSON_BIO, mBio);
        
        return retBundle;
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
