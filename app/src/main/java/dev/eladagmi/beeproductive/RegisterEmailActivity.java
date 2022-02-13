package dev.eladagmi.beeproductive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterEmailActivity extends AppCompatActivity {

    private EditText etRegEmail;
    private EditText etRegPassword;
    private TextView tvLoginHere;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeremail);
        findViews();

        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(view -> {
            createUser();
        });
        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterEmailActivity.this, LogInEmailActivity.class));
        });
    }

    private void createUser() {
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            etRegEmail.setError("Email can't be empty");
            etRegEmail.requestFocus();
        } else if(TextUtils.isEmpty(password)){
            etRegEmail.setError("Password can't be empty");
            etRegEmail.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterEmailActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterEmailActivity.this, LogInEmailActivity.class));
                    } else {
                        Toast.makeText(RegisterEmailActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
    private void findViews() {
        etRegEmail = findViewById(R.id.editTextTextEmailAddress);
        etRegPassword = findViewById(R.id.editTextTextPassword);
        tvLoginHere = findViewById(R.id.textView3);
        btnRegister = findViewById(R.id.button2);
    }

}