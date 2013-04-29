package com.cdietz.gitprofiles.profile;

import org.json.JSONObject;

import android.os.Bundle;

/**
 * Convenience methods for parsing data.
 * @author Christopher Dietz
 *
 */
public class ParseTools {

    public static String getStringOrDefault(JSONObject obj, String key, String defaultString) {
        return obj.optString(key, defaultString);
    }
    
    public static String getStringOrDefault(Bundle obj, String key, String defaultString) {
        String returnString = (obj != null && obj.containsKey(key)) ? obj.getString(key) : defaultString;
        return returnString;
    }
}
