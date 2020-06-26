package com.example.user.myschool;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class LihatDaftar extends AppCompatActivity {
    ListView listViewSiswa;
    DatabaseReference databaseSiswa;
    List<Siswa> daftarSiswa;
    Spinner updateKehadiran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_daftar);
        databaseSiswa = FirebaseDatabase.getInstance().getReference("siswa");
        listViewSiswa = (ListView) findViewById(R.id.listViewSiswa);
        daftarSiswa = new ArrayList<>();

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
                DaftarSiswa adapter = new DaftarSiswa(LihatDaftar.this, daftarSiswa);
                listViewSiswa.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
