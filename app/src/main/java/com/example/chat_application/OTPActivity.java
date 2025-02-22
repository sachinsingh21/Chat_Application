package com.example.chat_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chat_application.databinding.ActivityOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private ActivityOtpactivityBinding binding;
    private FirebaseAuth auth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        String phone = getIntent().getStringExtra("number");
        binding.textView.setText("Verify: " + phone);
        getSupportActionBar().hide();
        //binding.otpView.requestFocus();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)

                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        super.onCodeSent(verifyId, forceResendingToken);
                        verificationId = verifyId;
                    }
                }).build();

            PhoneAuthProvider.verifyPhoneNumber(options);

            binding.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,binding.otpView.getText().toString());
                    auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //Toast.makeText(OTPActivity.this, "Logged In", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(OTPActivity.this, SetupProfileActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                            else
                            {
                                Toast.makeText(OTPActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

    }
}