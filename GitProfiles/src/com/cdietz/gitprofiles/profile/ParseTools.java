package com.cdietz.gitprofiles.profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class ParseTools {

    public static String getStringOrDefault(JSONObject obj, String key, String defaultString) {
        String returnString;
        try {
            returnString = (obj != null && obj.has(key)) ? obj.getString(key) : defaultString;
        } catch (JSONException e) {
            // Shouldn't get here, but in case we do, something went wrong and use the default string.
            returnString = defaultString;
        }
        return returnString;
    }
    
    public static String getStringOrDefault(Bundle obj, String key, String defaultString) {
        String returnString = (obj != null && obj.containsKey(key)) ? obj.getString(key) : defaultString;
        return returnString;
    }
}
