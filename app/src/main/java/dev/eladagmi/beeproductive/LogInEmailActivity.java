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

public class LogInEmailActivity extends AppCompatActivity {

    EditText etRegEmail;
    EditText etRegPassword;
    TextView tvRegisterHere;
    Button btnLogin;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginemail);

        etRegEmail = findViewById(R.id.editTextTextEmailAddress);
        etRegPassword = findViewById(R.id.editTextTextPassword);
        tvRegisterHere = findViewById(R.id.textView3);
        btnLogin = findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view ->{
            loginUser();
        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LogInEmailActivity.this, RegisterEmailActivity.class));
        });
    }

    private void loginUser() {
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            etRegEmail.setError("Email can't be empty");
            etRegEmail.requestFocus();
        } else if(TextUtils.isEmpty(password)){
            etRegEmail.setError("Password can't be empty");
            etRegEmail.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LogInEmailActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInEmailActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LogInEmailActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }


}