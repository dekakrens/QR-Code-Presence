package com.example.user.myschool;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


    public class GuruSignup extends AppCompatActivity {

        EditText editNamaGuru, editNIP, editUnameGuru, editEmailGuru, editPasswordGuru, editMapel;
        private Button btnRegisGuru;
        private ProgressBar loadingProgress;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_guru_signup);

            editNamaGuru = findViewById(R.id.editNamaGuru);
            editNIP = findViewById(R.id.editNIP);
            editUnameGuru = findViewById(R.id.editUnameGuru);
            editEmailGuru = findViewById(R.id.editEmailGuru);
            editPasswordGuru = findViewById(R.id.editPasswordGuru);
            editMapel = findViewById(R.id.editMapel);
            loadingProgress = findViewById(R.id.progressBar);
            btnRegisGuru = findViewById(R.id.btnRegisGuru);
            loadingProgress.setVisibility(View.INVISIBLE);

            btnRegisGuru.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onClick(View v) {
                    btnRegisGuru.setVisibility(View.INVISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                    final String nama = editNamaGuru.getText().toString();
                    final String nip = editNIP.getText().toString();
                    final String uname = editUnameGuru.getText().toString();
                    final String email = editEmailGuru.getText().toString();
                    final String pwd = editPasswordGuru.getText().toString();
                    final String mapel = editMapel.getText().toString();


                    if (nama.isEmpty() || nip.isEmpty() || uname.isEmpty() || email.isEmpty() || pwd.isEmpty() || mapel.isEmpty() ) {

                        showMessage("Mohon lengkapi data registrasi");
                        btnRegisGuru.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.VISIBLE);
                    } else {
                        showMessage("Guru berhasil didaftarkan");
                        login();
                    }

                }
            });
        }

        private void showMessage(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        private void updateUserInfo(String nama, String nip, String uname, FirebaseUser currentUser) {

        }
        public void login() {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("dataguru");

            String nama =  editNamaGuru.getText().toString();
            String nis = editNIP.getText().toString();
            String mapel = editMapel.getText().toString();
            String uname =  editUnameGuru.getText().toString();

            String id = (uname);
            myRef.child(id).setValue(uname);
            myRef.child(id).child("nama").setValue(nama);
            myRef.child(id).child("nomor").setValue(nis);
            myRef.child(id).child("mapel").setValue(mapel);
            myRef.child(id).child("username").setValue(uname);

            Intent intent = new Intent(this, GuruLogin.class);
            startActivity(intent);
        }

    }
