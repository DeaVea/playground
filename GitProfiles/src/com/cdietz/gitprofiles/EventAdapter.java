package com.cdietz.gitprofiles;

import android.content.Context;
import android.widget.ArrayAdapter;

public class EventAdapter extends ArrayAdapter<String> {

    public EventAdapter(Context context) {
        super(context, R.layout.event_row);
    }

}
