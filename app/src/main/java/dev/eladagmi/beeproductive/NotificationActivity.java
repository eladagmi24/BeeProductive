package dev.eladagmi.beeproductive;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        TextView textView = findViewById(R.id.message);

        String message = getIntent().getStringExtra("message");
        textView.setText(message);

    }


}