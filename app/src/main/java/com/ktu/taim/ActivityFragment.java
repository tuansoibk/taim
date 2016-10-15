package com.ktu.taim;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    public static String ACTIVITY_NAME_ARG = "activity_name";

    private String activityName;

    private GestureDetectorCompat gestureDetector;

    public ActivityFragment() {
        // Required empty public constructor
    }

    public static ActivityFragment newInstance(String param1) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putString(ACTIVITY_NAME_ARG, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activityName = getArguments().getString(ACTIVITY_NAME_ARG);
        }
        gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                System.out.println("I'm tapped");
                LinearLayout bgLayout = (LinearLayout) (getView().findViewById(R.id.backgroundLayout));
                bgLayout.setBackgroundColor(ColorLot.getInstance().getColorToUse());
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activityfragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout bgLayout = (LinearLayout) (getView().findViewById(R.id.backgroundLayout));
//        bgLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("I'm touched -- " + event.getAction());
//                gestureDetector.onTouchEvent(event);
//                return false;
//            }
//        });
        bgLayout.setBackgroundColor(ColorLot.getInstance().getColorToUse());
        ((TextView) (getView().findViewById(R.id.activityNameTxt))).setText(activityName);
    }
}