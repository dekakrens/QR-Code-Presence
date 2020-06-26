package com.example.user.myschool;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SiswaSignup extends AppCompatActivity {

    EditText editNamaSis, editNISSis, editUnameSis, editEmailSis, editPasswordSis;
    private Button btnRegisSis;

    ListView listViewSiswa;
    DatabaseReference databaseSiswa;

    List<Siswa> daftarSiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswa_signup);

        databaseSiswa = FirebaseDatabase.getInstance().getReference("siswa");

        listViewSiswa = (ListView) findViewById(R.id.listViewSiswa);
        daftarSiswa = new ArrayList<Siswa>();
        ////////////////////////////////////////////

        editNamaSis = findViewById(R.id.editNamaSis);
        editNISSis = findViewById(R.id.editNISSis);
        editUnameSis = findViewById(R.id.editUnameSis);
        editEmailSis = findViewById(R.id.editEmailSis);
        editPasswordSis = findViewById(R.id.editPasswordSis);
        btnRegisSis = findViewById(R.id.btnRegisSis);


        btnRegisSis.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                final String nama = editNamaSis.getText().toString();
                final String nis = editNISSis.getText().toString();
                final String uname = editUnameSis.getText().toString();
                final String email = editEmailSis.getText().toString();
                final String pwd = editPasswordSis.getText().toString();
                if (nama.isEmpty() || nis.isEmpty() || uname.isEmpty() || email.isEmpty()|| pwd.isEmpty()){
                    showMessage("Lengkapi data registrasi");
                } else {
                    login();
                    showMessage("Siswa berhasil didaftarkan");
                }
            }
        });
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


    public void login(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("siswa");

        String kehadiran = "Tidak Hadir";
        String nama =  editNamaSis.getText().toString();
        String nis = editNISSis.getText().toString();
        String uname =  editUnameSis.getText().toString();
        String pass =  editPasswordSis.getText().toString();

        String id = (uname);
        myRef.child(id).setValue(uname);
        myRef.child(id).child("namaSiswa").setValue(nama);
        myRef.child(id).child("nomorInduk").setValue(nis);
        myRef.child(id).child("kehadiranSiswa").setValue(kehadiran);
        myRef.child(id).child("username").setValue(uname);
        myRef.child(id).child("pass").setValue(pass);

        Intent intent = new Intent(this, SiswaLogin.class);
        startActivity(intent);
    }
}
