package com.example.user.myschool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SiswaLogin extends AppCompatActivity {

    private Button btnMasuk;
    EditText loginUnameSis, loginPasswordSis;
    TextView tvBuat;
    String nama,pwdf;
    private static final String TAG = "SiswaLogin";
    public static final  String SHARED_PREFS = "shared";
    public static final  String TEXT = "text";
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswa_login);

            btnMasuk = findViewById(R.id.btnMasuk);


            tvBuat = findViewById(R.id.tvBuatAkun);
            loginPasswordSis = findViewById(R.id.loginPasswordSis);
            loginUnameSis = findViewById(R.id.loginUnameSis);

        database = FirebaseDatabase.getInstance().getReference("siswa");


        String text = "Belum Mendaftar? Daftar di sini";
        SpannableString sini = new SpannableString(text);
        ClickableSpan klik = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                daftar();
            }
        };

        sini.setSpan(klik, 27, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvBuat.setText(sini);
        tvBuat.setMovementMethod(LinkMovementMethod.getInstance());

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameSiswa = loginUnameSis.getText().toString();
                final String uname = loginUnameSis.getText().toString();
                final String pwd = loginPasswordSis.getText().toString();
                if (uname.isEmpty() || pwd.isEmpty()){
                    showMessage("Lengkapi data login");
                } else {
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String uname = loginUnameSis.getText().toString();
                            final String pwd = loginPasswordSis.getText().toString();
                            nama = String.valueOf(dataSnapshot.child(uname).child("namaSiswa").getValue());
                            pwdf = String.valueOf(dataSnapshot.child(uname).child("pass").getValue());

                            if (nama == "null") {
                                showMessage("Anda belum melakukan registrasi");
                            }
                            else if (pwd.equals(pwdf)) {
                                showMessage("Berhasil masuk sebagai " + nama);
                                masukProfilSiswa();
                            }
                            else if (pwdf != pwd){
                                showMessage("Password anda salah");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
    public void daftar(){
        Intent intent = new Intent(this, SiswaSignup.class);
        startActivity(intent);
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void masukProfilSiswa() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, loginUnameSis.getText().toString());
        editor.apply();

        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

}
