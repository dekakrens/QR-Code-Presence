package com.example.user.myschool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import static com.example.user.myschool.SiswaLogin.TEXT;

public class Guru extends AppCompatActivity {
    ListView listViewSiswa;
    DatabaseReference databaseSiswa;
    DatabaseReference database;
    List<Siswa> daftarSiswa;
    public String nama1,nip1,mapel1;
    public static final  String SHARED_PREFS = "shared";
    public String Username;
    private TextView tampilnamaguru, tampilnip, tampilmapel;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru);
        databaseSiswa = FirebaseDatabase.getInstance().getReference("siswa");
        database = FirebaseDatabase.getInstance().getReference("dataguru");
        listViewSiswa = (ListView) findViewById(R.id.listViewSiswa);
        daftarSiswa = new ArrayList<>();

        tampilnamaguru = findViewById(R.id.tampilNamaGuru);
        tampilnip = findViewById(R.id.tampilNip);
        tampilmapel = findViewById(R.id.tampilMapel);
        btnLogout = findViewById(R.id.btnLogout);

    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseSiswa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    daftarSiswa.clear();
                    for (DataSnapshot siswaSnapshot : dataSnapshot.getChildren()) {
                        Siswa siswa = siswaSnapshot.getValue(Siswa.class);

                        daftarSiswa.add(siswa);
                    }
                    DaftarSiswa adapter = new DaftarSiswa(Guru.this, daftarSiswa);
                    listViewSiswa.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                Username = sharedPreferences.getString(TEXT, "");

                String nama = String.valueOf(dataSnapshot.child(Username).child("nama").getValue());
                String nisn = String.valueOf(dataSnapshot.child(Username).child("nomor").getValue());
                String uname = String.valueOf(dataSnapshot.child(Username).child("mapel").getValue());
                tampilnamaguru.setText(nama);
                tampilnip.setText(nisn);
                tampilmapel.setText(uname);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Guru.this, GuruLogin.class));
            }
        });

    }


}