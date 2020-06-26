package com.example.user.myschool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;
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

public class GuruLogin extends AppCompatActivity {


    private Button btnMasuk;
    EditText loginUnameGuru, loginPasswordGuru;
    TextView tvBuat;
    public static final  String SHARED_PREFS = "shared";
    public static final  String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_login);

        btnMasuk = findViewById(R.id.btnMasuk);

        tvBuat = findViewById(R.id.tvBuatAkun);
        loginPasswordGuru = findViewById(R.id.loginPasswordGuru);
        loginUnameGuru = findViewById(R.id.loginUnameGuru);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("dataguru");

        String text = "Belum Mendaftar? Daftar di sini";
        SpannableString sini = new SpannableString(text);
        ClickableSpan klik = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                daftar();
            }
        };

        sini.setSpan(klik, 27, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvBuat.setText(sini);
        tvBuat.setMovementMethod(LinkMovementMethod.getInstance());

        /////////////////////////////////////////////
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uname = loginUnameGuru.getText().toString();
                final String pwd = loginPasswordGuru.getText().toString();
                if (uname.isEmpty() || pwd.isEmpty()){
                    showMessage("Lengkapi data login");
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(TEXT, loginUnameGuru.getText().toString());

                    editor.apply();
                    masukProfilGuru();
                }
            }
        });


    }
    public void daftar(){
        Intent intent = new Intent(this, GuruSignup.class);
        startActivity(intent);
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void masukProfilGuru(){
        Intent intent = new Intent(this, Guru.class);
        startActivity(intent);
    }
}