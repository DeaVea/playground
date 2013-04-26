package com.cdietz.gitprofiles;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdietz.gitprofiles.profile.MainProfile;

public class MainProfileFragment extends Fragment {
    public static final String TAG = MainProfileFragment.class.getSimpleName();
    private static final String BUNDLE_SAVED_MAIN_PROFILE = "main_profile";

    /**
     * Profile handled by this fragment. Can be null
     */
    private MainProfile mProfile;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View viewPrompt = inflater.inflate(R.layout.main_profile, container, false);
        
        if(savedInstanceState != null) {
            setProfile(new MainProfile(savedInstanceState));
        }
        
        return viewPrompt;
    }    
    
    @Override
    public void onSaveInstanceState(Bundle out) {
        if(mProfile != null) {
            out.putBundle(BUNDLE_SAVED_MAIN_PROFILE, mProfile.toBundle());
        }
        
        super.onSaveInstanceState(out);
    }
    
    /**
     * Returns the profile backed by this fragment
     * @return
     *      The profile being displayed.
     */
    public MainProfile getProfile() {
        return mProfile;
    }
    
    /**
     * Sets a new profile for this fragment.
     * @param mp
     *      The new profile to show
     */
    public void setProfile(MainProfile mp) {
        mProfile = mp;
        final View myView = getView();
        
        // May be detached
        if(myView == null) {
            return;
        }
        
        ((TextView) myView.findViewById(R.id.mainp_user_name)).setText(mp.mUserName);
        ((TextView) myView.findViewById(R.id.mainp_real_name)).setText(mp.mRealName);
        ((TextView) myView.findViewById(R.id.mainp_html_url)).setText(mp.mHtmlUrl);
        ((TextView) myView.findViewById(R.id.mainp_bio)).setText(mp.mBio);
    }
    
    /**
     * Sets the profile pic of the profile
     * @param bm
     *      Bitmap of the profile
     */
    public void setProfilePic(Bitmap bm) {
        final View myView = getView();
        
        Log.d(TAG, "Setting profile");
        // May be detached
        if(myView == null) {
            return;
        }
        
        ((ImageView) myView.findViewById(R.id.mainp_profile_pic)).setImageBitmap(bm);
    }
}
