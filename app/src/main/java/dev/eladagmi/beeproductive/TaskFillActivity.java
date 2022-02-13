package dev.eladagmi.beeproductive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Calendar;

public class TaskFillActivity extends AppCompatActivity implements Serializable {

    //Contacts
    private String contacts = "";
    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;
    private String[] importance = {"Important & Urgent", "Important & Not Urgent", "Not Important & Urgent", "Not Important & Not Urgent"};
    private Spinner importanceSpinner;
    private TextInputEditText taskName, creatorName, addMember;
    private TextInputEditText dueDate, hourToPerform;
    private Button submitTask;
    private TaskActivity taskActivity = new TaskActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskfill);
        hideSystemUI();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        importanceSpinner = findViewById(R.id.importance);
        taskName = findViewById(R.id.login_ET_task_name);
        creatorName = findViewById(R.id.login_ET_creatorName);
        dueDate = findViewById(R.id.login_ET_date);
        submitTask = findViewById(R.id.submit);
        addMember = findViewById(R.id.login_ET_members);
        hourToPerform = findViewById(R.id.login_ET_hour);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, importance);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter1);
        importanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskActivity.setImportance(importanceSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((width), (height));
        addMember = findViewById(R.id.login_ET_members);
        addMember.setOnClickListener(view -> {
            if (checkContactPermission()) {
                addMember.setText("");
                pickContactIntent();
            } else {
                requestContactPermission();
            }

        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks");
        submitTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;
                if (taskName.getText().toString().length() == 0) {
                    taskName.setError("Enter Task Name");
                    counter++;
                }
                if (creatorName.getText().toString().length() == 0) {
                    creatorName.setError("Enter Creator Name");
                    counter++;
                }
                if (dueDate.getText().toString().length() == 0) {
                    dueDate.setError("Enter Due Date");
                    counter++;
                }
                if (hourToPerform.getText().toString().length() == 0) {
                    hourToPerform.setError("Enter Hour To Perform");
                    counter++;
                }
                if (counter == 0) {
                    taskActivity.setTaskName(taskName.getText().toString());
                    taskActivity.setCreatorName(creatorName.getText().toString());
                    if (addMember.getText().toString().equals(""))
                        taskActivity.setMembers("No Members");
                    else
                        taskActivity.setMembers(addMember.getText().toString());


                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    try {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions(TaskFillActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    taskActivity.setDueDate(dueDate.getText().toString());
                    taskActivity.setHourToPerform(hourToPerform.getText().toString());
                    taskActivity.setLat(location.getLatitude());
                    taskActivity.setLon(location.getLongitude());
                    tasksRef.child(String.valueOf(Calendar.getInstance().getTime())).setValue(taskActivity);
                    Toast.makeText(TaskFillActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        dueDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String curDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dueDate.setText(curDate);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        hourToPerform.setOnClickListener(view ->{
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String timeString;
                    if(hourOfDay - 10 < 0 && minute - 10 < 0)
                        timeString = "0" + hourOfDay + ":" + "0" + minute;
                    else if(hourOfDay - 10 < 0)
                        timeString = "0" + hourOfDay + ":" + minute;
                    else if(minute - 10 < 0)
                        timeString = hourOfDay + ":" + "0" + minute;
                    else
                        timeString = hourOfDay + ":" + minute;
                    hourToPerform.setText(timeString);
                }

            } ,hour, minute, true);
            timePickerDialog.show();
        });



    }
    private boolean checkContactPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestContactPermission() {
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE);
    }

    private void pickContactIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContactIntent();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String date = data.getStringExtra("Date");
            dueDate.setText(date);

            //Contacts
            if(requestCode == CONTACT_PICK_CODE) {
                //addMember.setText("");
                Cursor cursor1, cursor2;
                Uri uri = data.getData();

                cursor1 = getContentResolver().query(uri, null, null, null, null);
                if(cursor1.moveToFirst()) {
                    @SuppressLint("Range") String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    @SuppressLint("Range") String idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);
                    contacts += contactName + " | ";
                    addMember.setText(contacts);

                    if(idResultHold == 1) {
                        cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                        while(cursor2.moveToNext()) {
                            @SuppressLint("Range") String contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }
                        cursor2.close();
                    }
                    cursor1.close();

                }
            }
        }

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