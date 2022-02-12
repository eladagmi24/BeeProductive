package dev.eladagmi.beeproductive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addTask;
    CalendarView calendar;
    ListFragment listFragment;
    ArrayList<String> allTasks = new ArrayList<>();
    ArrayList<String> allKeys = new ArrayList<>();
    TextView txtScore;
    int score;
    boolean flag;
    private MapFragment mapFragment;
    private GoogleMap map;
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        DatabaseReference scoreRef = database.getReference("score");



        ListFragment listFragment = new ListFragment();
        listFragment.setCallBackList(callBack_List);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, listFragment).commit();

        addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this, TaskFillActivity.class));
        });

        logout = findViewById(R.id.signout);
        logout.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            intent.putExtra("signout", true);
            startActivity(intent);
        });
//        mapFragment = new MapFragment();
//        mapFragment.setCallBackMap(callBack_map);
        //getSupportFragmentManager().beginTransaction().add(R.id.frame2, mapFragment).commit();
        calendar = findViewById(R.id.calendar);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("tasks");
        DatabaseReference referenceScore = FirebaseDatabase.getInstance().getReference().child("score");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allTasks.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    allTasks.add(snapshot1.getValue().toString());
                    allKeys.add(snapshot1.getKey());
                }

                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        String  curDate = String.valueOf(dayOfMonth);
                        String  Year = String.valueOf(year);
                        String  Month = String.valueOf(month + 1);
                        String date = curDate + "/" + Month + "/" + Year;
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                        String currentDate = df.format(c);
                        listFragment.tasksForCurDate(allTasks, allKeys, date, currentDate);

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        flag = true;
        referenceScore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flag == true) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        score = Integer.parseInt(snapshot1.getValue().toString());
                    }
                    txtScore = findViewById(R.id.score);
                    String points = getIntent().getStringExtra("points");
                    if(points != null) {
                        score = score + Integer.parseInt(points);
                        txtScore.setText("Score: " + score);
                    }
                    DatabaseReference scoreRef = database.getReference("score");
                    scoreRef.child("score").setValue(String.valueOf(score));
                }
                flag = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        hideSystemUI();
    }



    CallBack_List callBack_List = new CallBack_List() {
        @Override
        public void taskSelected(String key) {
            for(int i = 0; i < allKeys.size(); i++) {
                if(key.equals(allKeys.get(i))) {
                    String[] parts, smallParts;
                    String fullLon, fullLat, lon, lat;
                    parts = allTasks.get(i).split(",");
                    fullLon = parts[5];
                    fullLat = parts[7];

                    smallParts = fullLon.split("=");
                    lon = smallParts[1];

                    smallParts = fullLat.split("=");
                    lat = smallParts[1];

                    lat = stringWithoutLastWord(lat);

                    Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                    intent.putExtra("task", allTasks.get(i));
                    intent.putExtra("key", key);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    startActivity(intent);


                }
            }

        }
        public void locationSelected(String key) {
            for(int i = 0; i < allKeys.size(); i++) {
                if(key.equals(allKeys.get(i))) {

                }
            }

        }


    };
//    CallBack_Map callBack_map = new CallBack_Map() {
//        @Override
//        public void mapClicked(double lat, double lon) {
//        }
//
//        @Override
//        public void locationSelected(String lat, String lon) {
//            mapFragment.onClicked(lat, lon);
//        }
//
//    };
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        LatLng mark = new LatLng(32.104236455127015, 34.87987851707526);
//        map.addMarker(new MarkerOptions().position(mark).title("Task created here"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(mark));
//    }
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
}
