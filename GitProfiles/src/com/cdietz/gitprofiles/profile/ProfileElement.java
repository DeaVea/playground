package com.cdietz.gitprofiles.profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

/**
 * An object that belongs to the user's profile.
 * @author Christopher Dietz
 *
 */
public interface ProfileElement {
    /**
     * Convert all data to a Bundle object
     * @return
     *      All data in the Bundle that can be used for Android APIs
     */
    public Bundle toBundle();
    /**
     * Convert all data to JSONOjbects
     * @return
     *      All data converted to a usable JSONObject
     * @throws JSONException
     *      An error in converting the data.
     */
    public JSONObject toJson() throws JSONException;
}
