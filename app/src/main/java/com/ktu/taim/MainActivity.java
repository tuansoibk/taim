package com.ktu.taim;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);

        // Create a new Fragment to be placed in the activity layout
        final ActivityFragment firstFragment = new ActivityFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());
        Bundle args = new Bundle();
        args.putString(ActivityFragment.ACTIVITY_NAME_ARG, "test activity");
        firstFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activityFrameContainer, firstFragment).commit();

        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                System.out.println("I'm tapped in main activity");
                return true;
            }
        });

        final SwipeListener swipeListener = new SwipeListener();
        FrameLayout frLayout = (FrameLayout) findViewById(R.id.activityFrameContainer);
//        frLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("I'm touched in main activity -- " + event.getAction());
//                gestureDetector.onTouchEvent(event);
//                return true;
//            }
//        });

        frLayout.setOnTouchListener(new SwipeListener() {
            @Override
            public void onLeftToRightSwipe() {
                // Create a new Fragment to be placed in the activity layout
                ActivityFragment secondFragment = new ActivityFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                secondFragment.setArguments(getIntent().getExtras());
                Bundle args = new Bundle();
                args.putString(ActivityFragment.ACTIVITY_NAME_ARG, "another activity");
                secondFragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.activityFrameContainer, secondFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
    }
}
