package com.ktu.taim.task;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktu.taim.R;


/**
 * This fragment class responsible for visualizing a task & its info.
 *
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    public static String TASK_NAME_ARG = "task_name";
    public static String BACKGROUND_COLOR_ARG = "bg_color";

    private String taskName;
    private int bgColor;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment.
     *
     * @param taskName
     * @param color
     * @return
     */
    public static TaskFragment newInstance(String taskName, int color) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(TASK_NAME_ARG, taskName);
        args.putInt(BACKGROUND_COLOR_ARG, color);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskName = getArguments().getString(TASK_NAME_ARG);
            bgColor = getArguments().getInt(BACKGROUND_COLOR_ARG);
        }
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
        bgLayout.setBackgroundColor(bgColor);
        ((TextView) (getView().findViewById(R.id.activityNameTxt))).setText(taskName);
    }
}