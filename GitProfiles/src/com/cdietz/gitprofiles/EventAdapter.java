package com.cdietz.gitprofiles;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * This adapter is in charge of keeping track of Events to be displayed to the user.
 * @author Christopher Dietz
 *
 */
public class EventAdapter extends ArrayAdapter<String> {
    public EventAdapter(Context context) {
        super(context, R.layout.event_row);
    }
}
