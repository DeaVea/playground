package com.cdietz.gitprofiles;

import com.cdietz.gitprofiles.profile.Event;

import android.app.Activity;
import android.support.v4.app.ListFragment;

public class EventsListFragment extends ListFragment {
    public static final String TAG = EventsListFragment.class.getSimpleName();
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setListAdapter(new EventAdapter(activity));
    }
    
    public void addEvent(Event ev) {
        ((EventAdapter) getListAdapter()).add(ev.mType);
    }
}
