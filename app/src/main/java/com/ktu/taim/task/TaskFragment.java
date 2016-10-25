package com.ktu.taim.task;


import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.InputType;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.ktu.taim.R;


/**
 * This fragment class responsible for visualizing a task & its info.
 *
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    public static long MILIS_PER_HOUR = 3600 * 1000;
    public static long MILIS_PER_MIN = 60 * 1000;
    public static long MILIS_PER_SEC = 1000;
    public static long MILIS_PER_STAR = 10;

    private TaskPresenter presenter;
    private Handler handler;

    public TaskFragment() {
        // emty constructor
    }

    public void assignPresenter(TaskPresenter presenter) {
        this.presenter = presenter;
    }

    public Handler getHandler() { return handler; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateTimeCounterDisplay();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.taskfragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout bgLayout = (LinearLayout) (getView().findViewById(R.id.backgroundLayout));
        bgLayout.setBackgroundColor(presenter.getTask().getColor());
        TextView taskTxt = (TextView) (getView().findViewById(R.id.activityNameTxt));
        taskTxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Call_me_maybe.ttf"));
        taskTxt.setText(presenter.getTask().getName());

        final TextView nameTxt = (TextView) (getView().findViewById(R.id.activityNameTxt));

        nameTxt.setText(presenter.getTask().getName());
        //Rename task on name text clicked
        nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                renameTask();
            }
        });

        nameTxt.setLongClickable(true);
        nameTxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

        // start task timer after ui is shown
        System.out.println("start task timer");
        presenter.startTimeTracking();
    }

    /**
     * Update the visualization of task time counter on the screen.
     */
    private void updateTimeCounterDisplay() {
        long counter = presenter.getTask().getTimeCounter();
        int hours = (int)(counter / MILIS_PER_HOUR);
        counter %= MILIS_PER_HOUR;
        int mins = (int)(counter / MILIS_PER_MIN);
        counter %= MILIS_PER_MIN;
        int secs = (int)(counter / MILIS_PER_SEC);
        counter %= MILIS_PER_SEC;
        int stars = (int)(counter / MILIS_PER_STAR);
        String timeCounterString = String.format("%02d:%02d:%02d.%02d", hours, mins, secs, stars);
        View view = getView();
        if (view != null) {
            ((TextView) view.findViewById(R.id.timerValueTxt)).setText(timeCounterString);
        }
        else {
            // just to ensure the program will not break if a call is made
            // to this method after this fragment has been closed
            System.out.println(presenter.getTask().getName() + " is cancelled");
        }
    }

    //Rename the task
    public void renameTask()
    {
        final TextView nameTxt = (TextView) (getView().findViewById(R.id.activityNameTxt));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.task_name_popup);

// Set up the input
        final EditText input = new EditText(getContext());
        input.setText(nameTxt.getText());
// Specify the type of input expected: text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                presenter.getTask().setName(newName);
                nameTxt.setText(newName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    //Delete task pop up, if yes, delete
    private void deleteTaskOnFragment(){
        final TextView nameTxt = (TextView) (getView().findViewById(R.id.activityNameTxt));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.delete_task_popup);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.selfKill();
                removeFragment();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void removeFragment(){
        //// TODO: 10/20/2016 check this: cannot apply remove(fragment) to TaskFragment
        //getActivity().getFragmentManager().beginTransaction().remove(this);
    }
}