package com.cdietz.gitprofiles;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class PromptFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = PromptFragment.class.getSimpleName();
    
    private PromptListener mListener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PromptListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement a PromptListener interface.");
        }
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View viewPrompt = inflater.inflate(R.layout.profile_prompt, container, false);
        
        final View button = viewPrompt.findViewById(R.id.prompt_button);
        button.setOnClickListener(this);
        
        // By Android design guidlines, the touch area of a button should be no less than 48 dp.  The refresh is less than that.
        viewPrompt.post(new Runnable() {
            @Override
            public void run() {
                final Rect area = new Rect();
                button.getHitRect(area);
                
                final int buttonArea = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getActivity().getResources().getDisplayMetrics());
                
                final int halfDiffWidth = (buttonArea - area.width()) / 2;
                final int halfDiffHeight = (buttonArea - area.height()) / 2;
                
                if(halfDiffWidth > 0) {
                    area.left -= halfDiffWidth;
                    area.right += halfDiffWidth;
                }
                
                if(halfDiffHeight > 0) {
                    area.top -= halfDiffHeight;
                    area.bottom += halfDiffHeight;
                }
                
                final TouchDelegate touchArea = new TouchDelegate(area, button);
                ((View) button.getParent()).setTouchDelegate(touchArea);
            }
        });
        
        return viewPrompt;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.prompt_button) {
            final EditText et = (EditText) getView().findViewById(R.id.prompt_text);
            final String searchString = et.getText().toString();
            mListener.onSearch(searchString);
        }
    }    
    
    public void setLoading(boolean isLoading) {
        final View myView = getView();
        
        if(myView != null) {
            final int progressVisibility = (isLoading) ? View.VISIBLE : View.GONE;
            myView.findViewById(R.id.prompt_progress_circle).setVisibility(progressVisibility);
            
            final int buttonVisibility = (isLoading) ? View.GONE : View.VISIBLE;
            myView.findViewById(R.id.prompt_button).setVisibility(buttonVisibility);
        }
    }
    
    public interface PromptListener {
        public void onSearch(String name);
    }
}
