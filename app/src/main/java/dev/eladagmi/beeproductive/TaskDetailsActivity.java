package dev.eladagmi.beeproductive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskDetailsActivity extends AppCompatActivity implements Serializable, OnMapReadyCallback {


    TextView exit, edit, taskName, creatorName, members, dateAndHour;
    ImageButton delete, done;
    DatabaseReference reference;
    DatabaseReference scoreRef;
    CallBack_Data callBackData;

    private MapFragment mapFragment;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetails);
        hideSystemUI();
        findView();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) width, (int) height);


        mapFragment = new MapFragment();
        mapFragment.setCallBackMap(callBack_map);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, mapFragment).commit();
        String task = getIntent().getStringExtra("task");

        exit.setOnClickListener(view ->{
           finish();
        });

        edit.setOnClickListener(view ->{
            String key = getIntent().getStringExtra("key");
            editData(key, task);
        });

        delete.setOnClickListener(view ->{
            String key = getIntent().getStringExtra("key");
            deleteTask(key);
        });

        done.setOnClickListener(view ->{
            String key = getIntent().getStringExtra("key");
            doneTask(task, key);
        });

        fillData(task);


    }

    private void fillData(String task) {
        String[] parts, smallParts;
        String fullHour, fullImportance, fullDate, fullMembers, fullCreatorName, fullTaskName, fullLat, fullLon;
        String hour, importance, date, members1, creatorName1, taskName1, lat, lon;

        parts = task.split(",");
        fullHour = parts[0];
        fullImportance = parts[1];
        fullDate = parts[2];
        fullMembers = parts[3];
        fullCreatorName = parts[4];
        fullLon = parts[5];
        fullTaskName = parts[6];
        fullLat = parts[7];

        smallParts = fullHour.split("=");
        hour = smallParts[1];

        smallParts = fullImportance.split("=");
        importance = smallParts[1];

        smallParts = fullDate.split("=");
        date = smallParts[1];

        smallParts = fullMembers.split("=");
        members1 = smallParts[1];

        smallParts = fullCreatorName.split("=");
        creatorName1 = smallParts[1];

        smallParts = fullTaskName.split("=");
        taskName1 = smallParts[1];

        smallParts = fullLon.split("=");
        lon = smallParts[1];

        smallParts = fullLat.split("=");
        lat = smallParts[1];

        taskName1 = stringWithoutLastWord(taskName1);
        lat = stringWithoutLastWord(lat);
        taskName.setText(taskName1);
        creatorName.setText("By: " + creatorName1);
        members.setText(members1);
        dateAndHour.setText(date + " | " + hour);

        String la = getIntent().getStringExtra("lat");
        String lo = getIntent().getStringExtra("lon");
        //callBack_map.locationSelected(la, lo);

    }



    private void editData(String key, String task) {
        reference = FirebaseDatabase.getInstance().getReference("tasks");
        reference.child(key).removeValue();
        Intent intent = new Intent(TaskDetailsActivity.this, TaskEditActivity.class);
        intent.putExtra("task", task);
        startActivity(intent);
        finish();
    }

    private void deleteTask(String key) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(TaskDetailsActivity.this);
        deleteDialog.setMessage("Are you sure you want to delete this task?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference = FirebaseDatabase.getInstance().getReference("tasks");
                        reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(TaskDetailsActivity.this, "Task Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(TaskDetailsActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.cancel();
                    }
                });
        AlertDialog alert = deleteDialog.create();
        alert.setTitle("Delete");
        alert.show();
    }

    private void doneTask(String strTask, String key) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(TaskDetailsActivity.this);
        deleteDialog.setMessage("Are you sure you finished this task?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference = FirebaseDatabase.getInstance().getReference("tasks");
                        reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(TaskDetailsActivity.this, "Great! you earned points! ", Toast.LENGTH_SHORT).show();
                                    int points = calculatePoints(strTask);
                                    Intent intent = new Intent(TaskDetailsActivity.this, MainActivity.class);
                                    intent.putExtra("points", String.valueOf(points));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(TaskDetailsActivity.this, "Done Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = deleteDialog.create();
        alert.setTitle("Done");
        alert.show();
    }

    private int calculatePoints(String task) {
        int calc = 0;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        String[] parts, partsOfTasks, partsDate;
        parts = formattedDate.split("-");
        int curDay = Integer.parseInt(parts[2]);
        int curMonth = Integer.parseInt(parts[1]);
        int curYear = Integer.parseInt(parts[0]);

        partsOfTasks = task.split(",");
        String tempDate = partsOfTasks[2];
        partsDate = tempDate.split("=");
        String date = partsDate[1];
        String[] dayMonthYear;
        dayMonthYear = date.split("/");
        int day = Integer.parseInt(dayMonthYear[0]);
        int month = Integer.parseInt(dayMonthYear[1]);
        int year = Integer.parseInt(dayMonthYear[2]);

        if(month - curMonth == 0) {
            if(day - curDay < 7)
                calc = 10;
        } else if(month - curMonth > 0) {
            if(curDay - day <= 0)
                calc = 100;
            else
                calc = 10;
        } else {
            calc = 5;
        }
        return calc;
    }


    private void findView() {
        exit = findViewById(R.id.exit);
        edit = findViewById(R.id.edit);
        taskName = findViewById(R.id.taskName);
        creatorName = findViewById(R.id.creatorName);
        members = findViewById(R.id.members);
        dateAndHour = findViewById(R.id.dateAndHour);
        delete = findViewById(R.id.delete);
        done = findViewById(R.id.done);
    }

    public String stringWithoutLastWord(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '}') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void hideSystemUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void mapClicked(double lat, double lon) {
        }

        @Override
        public void locationSelected(String lat, String lon) {
            mapFragment.onClicked(lat, lon);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng mark = new LatLng(32.104236455127015, 34.87987851707526);
        map.addMarker(new MarkerOptions().position(mark).title("Task created here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(mark));
    }
}