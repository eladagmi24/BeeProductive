package dev.eladagmi.beeproductive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private CallBack_List callBackList;
    private MaterialTextView[] tasks = new MaterialTextView[10];
    private ArrayList<String> keys = new ArrayList<>();
    int counter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }

    public void tasksForCurDate(ArrayList<String> allTasks, ArrayList<String> allKeys, String date, String currentDate) {
        for(int i = 0; i < tasks.length; i++)
            tasks[i].setText("");
        counter = 0;
        String[] parts, tn, h, dt, smallParts;
        String taskName, hour, curDate, fullImportance, importance;
        for(int i = 0; i < allTasks.size(); i++) {
            parts = allTasks.get(i).split(",");
            taskName = parts[6];
            hour = parts[0];
            fullImportance = parts[1];
            tn = taskName.split("=");
            taskName = tn[1];
            h = hour.split("=");
            hour = h[1];

            curDate = parts[2];
            dt = curDate.split("=");
            curDate = dt[1];

            smallParts = fullImportance.split("=");
            importance = smallParts[1];

            taskName = stringWithoutLastWord(taskName);
            if(curDate.equals(date)) {
                tasks[counter].setText(taskName + "                            " + hour);
                if(importance.equals("Important & Urgent"))
                    tasks[counter].setTextColor(getResources().getColor(R.color.red));
                else if(importance.equals("Important & Not Urgent"))
                    tasks[counter].setTextColor(getResources().getColor(R.color.orange));
                else if(importance.equals("Not Important & Urgent"))
                    tasks[counter].setTextColor(getResources().getColor(R.color.orange));
                else
                    tasks[counter].setTextColor(getResources().getColor(R.color.green));

                keys.add(counter, allKeys.get(i));
                counter++;
            }
        }
    }

    private void initViews() {
        for (int i = 0; i < tasks.length; i++) {
            final int finI = i;
            tasks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(tasks[finI].getText().equals(""))) {
                        callBackList.taskSelected(keys.get(finI));
                    }

                }
            });
        }
    }

    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }

    private void findViews(View view) {
        tasks[0] = view.findViewById(R.id.list_LBL_task1);
        tasks[1] = view.findViewById(R.id.list_LBL_task2);
        tasks[2] = view.findViewById(R.id.list_LBL_task3);
        tasks[3] = view.findViewById(R.id.list_LBL_task4);
        tasks[4] = view.findViewById(R.id.list_LBL_task5);
        tasks[5] = view.findViewById(R.id.list_LBL_task6);
        tasks[6] = view.findViewById(R.id.list_LBL_task7);
        tasks[7] = view.findViewById(R.id.list_LBL_task8);
        tasks[8] = view.findViewById(R.id.list_LBL_task9);
        tasks[9] = view.findViewById(R.id.list_LBL_task10);
    }

    public String stringWithoutLastWord(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '}') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }



}