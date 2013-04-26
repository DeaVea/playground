package com.cdietz.gitprofiles.profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class Event {

    public static final String ARG_JSON_TYPE = "type";
    public static final String ARG_JSON_PUBLIC = "public";
    public static final String ARG_JSON_PAYLOAD = "payload";
    public static final String ARG_JSON_REPO = "repo";
    public static final String ARG_JSON_ACTOR = "actor";
    public static final String ARG_JSON_ORG = "org";
    public static final String ARG_JSON_CREATED_AT = "created_at";
    public static final String ARG_JSON_ID = "id";
    
    /**
     * The objects held by this event are backed by this event and are public merely for easy access.
     */
    public final String mType;
    public final String mPublic;
    public final String mCreatedAt;
    public final String mId;
    public final Payload mPayload;
    public final Repo mRepo;
    public final Actor mActor;
    public final Org mOrg;
    
    public Event(JSONObject object) throws JSONException {
        // Even though the documentation says all Events have the same format, that is not the case.
        // Events of "CreateEvent" for example do not have "org" objects
        
        mType = ParseTools.getStringOrDefault(object, ARG_JSON_TYPE, "NA");
        mPublic = ParseTools.getStringOrDefault(object, ARG_JSON_PUBLIC, "NA");
        mCreatedAt = ParseTools.getStringOrDefault(object, ARG_JSON_CREATED_AT, "NA");
        mId = ParseTools.getStringOrDefault(object, ARG_JSON_ID, "NA");
        mPayload = object.has(ARG_JSON_PAYLOAD) ? new Payload(object.getJSONObject(ARG_JSON_PAYLOAD)) : null;
        mRepo = object.has(ARG_JSON_REPO) ? new Repo(object.getJSONObject(ARG_JSON_REPO)) : null;
        mActor = object.has(ARG_JSON_ACTOR) ? new Actor(object.getJSONObject(ARG_JSON_ACTOR)) : null;
        mOrg = object.has(ARG_JSON_ORG) ? new Org(object.getJSONObject(ARG_JSON_ORG)) : null;
    }
    
    public Event(Bundle object) {
        mType = ParseTools.getStringOrDefault(object, ARG_JSON_TYPE, "NA");
        mPublic = ParseTools.getStringOrDefault(object, ARG_JSON_PUBLIC, "NA");
        mCreatedAt = ParseTools.getStringOrDefault(object, ARG_JSON_CREATED_AT, "NA");
        mId = ParseTools.getStringOrDefault(object, ARG_JSON_ID, "NA");
        mPayload = new Payload(object.getBundle(ARG_JSON_PAYLOAD));
        mRepo = new Repo(object.getBundle(ARG_JSON_REPO));
        mActor = new Actor(object.getBundle(ARG_JSON_ACTOR));
        mOrg = new Org(object.getBundle(ARG_JSON_ORG));
    }
    
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putString(ARG_JSON_TYPE, mType);
        bundle.putString(ARG_JSON_PUBLIC, mPublic);
        bundle.putString(ARG_JSON_CREATED_AT, mCreatedAt);
        bundle.putString(ARG_JSON_ID, mId);
        if(mPayload != null) {
            bundle.putBundle(ARG_JSON_PAYLOAD, mPayload.toBundle());
        }
        if(mRepo != null) {
        bundle.putBundle(ARG_JSON_REPO, mRepo.toBundle());
        }
        if(mActor != null) {
            bundle.putBundle(ARG_JSON_ACTOR, mActor.toBundle());
        }
        if(mOrg != null) {
            bundle.putBundle(ARG_JSON_ORG, mOrg.toBundle());
        }
        return bundle;
    }
    
    /**
     * A payload is unique to the type of event, so a standard event has nothing at its base.
     */
    public static class Payload {
        public static final String FULL_JSON_DATA = "fulldata";
        
        public final JSONObject mFullJSONData;
        
        public Payload(JSONObject jobject) throws JSONException {
            mFullJSONData = jobject;
        }
        
        public Payload(Bundle bundle) {
            JSONObject test;
            try {
                test = new JSONObject(bundle.getString(FULL_JSON_DATA));
            } catch (JSONException e) {
                test = null;
            }
           mFullJSONData = test;
        }
        
        public Bundle toBundle() {
            final Bundle bundle = new Bundle();
            bundle.putString(FULL_JSON_DATA, mFullJSONData.toString());
            return bundle;
        }
    }
    
    public static class Repo {
        public static final String ARG_JSON_ID = "id";
        public static final String ARG_JSON_NAME = "name";
        public static final String ARG_JSON_URL = "url";
        
        public final String mId;
        public final String mName;
        public final String mUrl;
        
        public Repo(JSONObject repoObject) throws JSONException {
            mId = ParseTools.getStringOrDefault(repoObject, ARG_JSON_ID, "NA");
            mName = ParseTools.getStringOrDefault(repoObject, ARG_JSON_NAME, "NA");
            mUrl = ParseTools.getStringOrDefault(repoObject, ARG_JSON_URL, "NA");
        }
        
        public Repo(Bundle repoObject) {
            mId = ParseTools.getStringOrDefault(repoObject, ARG_JSON_ID, "NA");
            mName = ParseTools.getStringOrDefault(repoObject, ARG_JSON_NAME, "NA");
            mUrl = ParseTools.getStringOrDefault(repoObject, ARG_JSON_URL, "NA");
        }
        
        public Bundle toBundle() {
            final Bundle bundle = new Bundle();
            if(mId != null) {
                bundle.putString(ARG_JSON_ID, mId);
            }
            bundle.putString(ARG_JSON_NAME, mName);
            bundle.putString(ARG_JSON_URL, mUrl);
            return bundle;
        }
    }
    
    public static class Actor {
        public static final String ARG_JSON_LOGIN = "login";
        public static final String ARG_JSON_ID = "id";
        public static final String ARG_JSON_AVATAR_URL = "avatar_url";
        public static final String ARG_JSON_GRAVATAR_ID = "gravatar_id";
        public static final String ARG_JSON_URL = "url";
        
        public final String mLogin;
        public final String mId;
        public final String mAvatarUrl;
        public final String mGravatarId;
        public final String mUrl;
        
        public Actor(JSONObject actorObject) throws JSONException {
            mLogin = ParseTools.getStringOrDefault(actorObject, ARG_JSON_LOGIN, "NA");
            mId = ParseTools.getStringOrDefault(actorObject, ARG_JSON_ID, "NA");
            mAvatarUrl = ParseTools.getStringOrDefault(actorObject, ARG_JSON_AVATAR_URL, "NA");
            mGravatarId = ParseTools.getStringOrDefault(actorObject, ARG_JSON_GRAVATAR_ID, "NA");
            mUrl = ParseTools.getStringOrDefault(actorObject, ARG_JSON_URL, "NA");
        }
        
        public Actor(Bundle actorObject) {
            mLogin = ParseTools.getStringOrDefault(actorObject, ARG_JSON_LOGIN, "NA");
            mId = ParseTools.getStringOrDefault(actorObject, ARG_JSON_ID, "NA");
            mAvatarUrl = ParseTools.getStringOrDefault(actorObject, ARG_JSON_AVATAR_URL, "NA");
            mGravatarId = ParseTools.getStringOrDefault(actorObject, ARG_JSON_GRAVATAR_ID, "NA");
            mUrl = ParseTools.getStringOrDefault(actorObject, ARG_JSON_URL, "NA");
        }
        
        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_JSON_LOGIN, mLogin);
            bundle.putString(ARG_JSON_ID, mId);
            bundle.putString(ARG_JSON_AVATAR_URL, mAvatarUrl);
            bundle.putString(ARG_JSON_GRAVATAR_ID, mGravatarId);
            bundle.putString(ARG_JSON_URL, mUrl);
            return bundle;
        }
    }
    
    public static class Org {
        public static final String ARG_JSON_LOGIN = "login";
        public static final String ARG_JSON_ID = "id";
        public static final String ARG_JSON_AVATAR_URL = "avatar_url";
        public static final String ARG_JSON_GRAVATAR_ID = "gravatar_id";
        public static final String ARG_JSON_URL = "url";
        
        public final String mLogin;
        public final String mId;
        public final String mAvatarUrl;
        public final String mGravatarId;
        public final String mUrl;
        
        public Org(JSONObject orgObject) throws JSONException {
            mLogin = ParseTools.getStringOrDefault(orgObject, ARG_JSON_LOGIN, "NA");
            mId = ParseTools.getStringOrDefault(orgObject, ARG_JSON_ID, "NA");
            mAvatarUrl = ParseTools.getStringOrDefault(orgObject, ARG_JSON_AVATAR_URL, "NA");
            mGravatarId = ParseTools.getStringOrDefault(orgObject, ARG_JSON_GRAVATAR_ID, "NA");
            mUrl = ParseTools.getStringOrDefault(orgObject, ARG_JSON_URL, "NA");
        }
        
        public Org(Bundle orgObject) {
            mLogin = ParseTools.getStringOrDefault(orgObject, ARG_JSON_LOGIN, "NA");
            mId = ParseTools.getStringOrDefault(orgObject, ARG_JSON_ID, "NA");
            mAvatarUrl = ParseTools.getStringOrDefault(orgObject, ARG_JSON_AVATAR_URL, "NA");
            mGravatarId = ParseTools.getStringOrDefault(orgObject, ARG_JSON_GRAVATAR_ID, "NA");
            mUrl = ParseTools.getStringOrDefault(orgObject, ARG_JSON_URL, "NA");
        }
        
        public Bundle toBundle() {
            final Bundle bundle = new Bundle();
            bundle.putString(ARG_JSON_LOGIN, mLogin);
            bundle.putString(ARG_JSON_ID, mId);
            bundle.putString(ARG_JSON_AVATAR_URL, mAvatarUrl);
            bundle.putString(ARG_JSON_GRAVATAR_ID, mGravatarId);
            bundle.putString(ARG_JSON_URL, mUrl);
            return bundle;
        }
    }
}
