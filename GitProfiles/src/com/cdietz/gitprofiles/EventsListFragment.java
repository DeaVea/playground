package com.cdietz.gitprofiles;

import com.cdietz.gitprofiles.profile.Event;

import android.app.Activity;
import android.support.v4.app.ListFragment;

/**
 * List fragment displaying all events found.
 * A default list adapter is provided though one can be replaced if needed.
 * @author Christopher Dietz
 *
 */
public class EventsListFragment extends ListFragment {
    public static final String TAG = EventsListFragment.class.getSimpleName();
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setListAdapter(new EventAdapter(activity));
    }
    
    /**
     * Adds an event to the list.
     * @param ev
     *          The event to add
     */
    public void addEvent(Event ev) {
        ((EventAdapter) getListAdapter()).add(ev.mType);
    }
}
