package com.cdietz.gitprofiles;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.cdietz.gitprofiles.PromptFragment.PromptListener;
import com.cdietz.gitprofiles.newtork.PullIntentService;
import com.cdietz.gitprofiles.newtork.PullProfileIntentService;
import com.cdietz.gitprofiles.newtork.PullProfilePictureIntentService;
import com.cdietz.gitprofiles.newtork.PullRecentActivitiesIntentService;
import com.cdietz.gitprofiles.profile.Event;
import com.cdietz.gitprofiles.profile.MainProfile;

public class GitProfilesActivity extends FragmentActivity implements PromptListener {
    
    private EventAdapter mEA;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        registerReceiver(UIUpdater, new IntentFilter(PullProfileIntentService.BROADCAST_NEW_MAIN_PROFILE));
        registerReceiver(UIUpdater, new IntentFilter(PullProfilePictureIntentService.BROADCAST_NEW_PROFILE_PIC));
        registerReceiver(UIUpdater, new IntentFilter(PullRecentActivitiesIntentService.BROADCAST_NEW_ACTIVITY_LIST));
        registerReceiver(ErrorHandler, new IntentFilter(PullProfileIntentService.BROADCAST_NO_PROFILE_FOUND));
        registerReceiver(ErrorHandler, new IntentFilter(PullIntentService.BROADCAST_CONNECTION_ERROR));
        registerReceiver(ErrorHandler, new IntentFilter(PullIntentService.BROADCAST_READ_ERROR));
        
        mEA = (EventAdapter) ((EventsListFragment) getSupportFragmentManager().findFragmentByTag(EventsListFragment.TAG)).getListAdapter();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        unregisterReceiver(UIUpdater);
        unregisterReceiver(ErrorHandler);
    }
    
    private void updateNewProfile(MainProfile mp) {
        ((MainProfileFragment) getSupportFragmentManager().findFragmentByTag(MainProfileFragment.TAG)).setProfile(mp);
        mEA.clear();
    }
    
    private void downloadPicture(MainProfile mp) {
        final Intent getPic = new Intent(getApplicationContext(), PullProfilePictureIntentService.class);
        getPic.putExtra(PullProfilePictureIntentService.BUNDLE_STRING_ARG_FILE_URL, mp.mAvatarUrl);
        startService(getPic);
    }
    
    private void downloadEvents(MainProfile mp) {
        final Intent getEvents = new Intent(getApplicationContext(), PullRecentActivitiesIntentService.class);
        getEvents.putExtra(PullRecentActivitiesIntentService.BUNDLE_STRING_ARG_USER_NAME, mp.mUserName);
        startService(getEvents);
    }
    
    private void updateProfilePic() {
        final String pathName = getFilesDir().getAbsolutePath() + File.separator + PullProfilePictureIntentService.PROFILE_PIC_FILE_NAME;
        final Bitmap bm = BitmapFactory.decodeFile(pathName);
        ((MainProfileFragment) getSupportFragmentManager().findFragmentByTag(MainProfileFragment.TAG)).setProfilePic(bm);    
    }
    
    private void addEvent(Event ev) {
        mEA.add(ev.mType);
    }
    
    public BroadcastReceiver ErrorHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String broadcast = intent.getAction();
            if(broadcast.equals(PullIntentService.BROADCAST_CONNECTION_ERROR)) {
                Toast.makeText(context, "Error connecting to host.", Toast.LENGTH_LONG).show();
            } else if (broadcast.equals(PullIntentService.BROADCAST_READ_ERROR)) {
                Toast.makeText(context, "Error parsing data.", Toast.LENGTH_LONG).show();
            } else if(broadcast.equals(PullProfileIntentService.BROADCAST_NO_PROFILE_FOUND)) {
                Toast.makeText(context, "Profile was not found.", Toast.LENGTH_LONG).show();
            } 
            
            ((PromptFragment) getSupportFragmentManager().findFragmentByTag(PromptFragment.TAG)).setLoading(false);
        }
    };
    
    public BroadcastReceiver UIUpdater = new BroadcastReceiver() {
        private MainProfile getProfile(Intent intent) {
            final Bundle newProfile = intent.getBundleExtra(PullProfileIntentService.BROADCAST_BUNDLE_ARG_NEW_PROFILE);
            return new MainProfile(newProfile);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String broadcast = intent.getAction();
            if(broadcast.equals(PullProfileIntentService.BROADCAST_NEW_MAIN_PROFILE)) {
                final MainProfile mp = getProfile(intent);
                
                updateNewProfile(mp);
                downloadPicture(mp);
                downloadEvents(mp);
                
            } else if (broadcast.equals(PullProfilePictureIntentService.BROADCAST_NEW_PROFILE_PIC)) {

                updateProfilePic();
                
            } else if (broadcast.equals(PullRecentActivitiesIntentService.BROADCAST_NEW_ACTIVITY_LIST)) {
                
                final Bundle list = intent.getBundleExtra(PullRecentActivitiesIntentService.BROADCAST_BUNDLE_ARG_EVENT_LIST);
                final int count = list.size();
                for(int i = 0; i < count; i++) {
                    addEvent(new Event(list.getBundle(String.valueOf(i))));
                }
                
                ((PromptFragment) getSupportFragmentManager().findFragmentByTag(PromptFragment.TAG)).setLoading(false);
            }
            
        }
    };

    @Override
    public void onSearch(String name) {
        final Intent testIntent = new Intent(getApplicationContext(), PullProfileIntentService.class);
        testIntent.putExtra(PullProfileIntentService.BUNDLE_STRING_ARG_PROFILE_NAME, name);
        startService(testIntent);
        
        ((PromptFragment) getSupportFragmentManager().findFragmentByTag(PromptFragment.TAG)).setLoading(true);
    }
}
