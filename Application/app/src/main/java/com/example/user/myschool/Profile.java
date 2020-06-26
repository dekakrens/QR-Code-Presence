package com.example.user.myschool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.user.myschool.SiswaLogin.SHARED_PREFS;
import static com.example.user.myschool.SiswaLogin.TEXT;

public class Profile extends AppCompatActivity {

    private Button btnCreate;
    private Button btnLogout;

    private static final String TAG = "Profile";

    DatabaseReference myRef;
    private  TextView tampilnama,tampilnis;
    public String Username;
    public static final  String SHARED_PREFS = "shared";

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

        //    btnCreate = findViewById(R.id.btnCreate);
            btnLogout = findViewById(R.id.btnLogout);

            myRef = FirebaseDatabase.getInstance().getReference("siswa");


            tampilnama = findViewById(R.id.tampilNama);
            tampilnis = findViewById(R.id.tampilNis);
            final ImageView imageView = findViewById(R.id.imageView);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                Username = sharedPreferences.getString(TEXT, "");

                    String nama = String.valueOf(dataSnapshot.child(Username).child("namaSiswa").getValue());
                    String nisn = String.valueOf(dataSnapshot.child(Username).child("nomorInduk").getValue());
                    String uname = String.valueOf(dataSnapshot.child(Username).child("username").getValue());
                        tampilnama.setText(nama);
                        tampilnis.setText(nisn);



                if (nama != "null" || nisn != "null" || uname != "null") {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(uname + "#" + nama + "#" +nisn + "#", BarcodeFormat.QR_CODE,
                                500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageView.setImageBitmap(bitmap);
                    } catch (WriterException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Pastikan anda sudah melakukan registrasi dan melakukan Login dengan data yang sesuai.",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(),"Gagal menampilkan profil",Toast.LENGTH_LONG).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Profile.this, SiswaLogin.class));
            }
        });
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
