package com.ktu.taim;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ktu.taim.task.TaskFragment;
import com.ktu.taim.task.TaskPresenter;

/**
 * This class represent the main window of the application
 */
public class MainActivity extends AppCompatActivity {

    private static String APP_VERSION_SIG = "1610_2310";

    private boolean isContainerEmpty = true;
    private MainPresenter mainPresenter;
    private int taskCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);

        // create the main presenter who will control the business logic of this app
        mainPresenter = new MainPresenter(this);

        // register swipe listener for the fragment container frame layout,
        // since this layout span over the whole screen
        final SwipeListener swipeListener = new SwipeListener();
        FrameLayout frLayout = (FrameLayout) findViewById(R.id.activityFrameContainer);
        frLayout.setOnTouchListener(new SwipeListener() {
            @Override
            public void onLeftToRightSwipe() {
                // left to right swipe: --->
                // means swipe the current task to right
                // also means bring the left task in
                mainPresenter.switchToLeftTask();
            }

            @Override
            public void onRightToLeftSwipe() {
                // right to left swipe: <---
                // means swipe the current task to left
                // also means bring the right task in
                mainPresenter.switchToRightTask();
            }

            @Override
            public void onBottomToTopSwipe() {
                // only for testing
                taskCount++;
                mainPresenter.createNewTask("test task " + taskCount);
            }
        });

        // initiate data loading
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mainPresenter.initialize(MainActivity.this);
                return null;
            }
        }.execute();
    }



    /**
     * Present current task content to the screen
     *
     * @param taskPresenter the presenter of the task
     */
    public void presentTask(TaskPresenter taskPresenter) {
        if (isContainerEmpty) {
            // Add the fragment to the 'fragment container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activityFrameContainer, taskPresenter.getFragment()).commit();
            isContainerEmpty = false;
        }
        else {
            // Replace whatever is in the fragment_container view with new fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activityFrameContainer, taskPresenter.getFragment());
            // Commit the transaction
            transaction.commit();
        }
    }

    /**
     * Tell user his/her task queue is already full
     */
    public void notifyTaskQueueFull() {
        Toast.makeText(this, "You task queue is already full!", Toast.LENGTH_SHORT).show();
    }
}
