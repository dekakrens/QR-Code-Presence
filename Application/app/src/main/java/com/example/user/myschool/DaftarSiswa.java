package com.example.user.myschool;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DaftarSiswa extends ArrayAdapter<Siswa> {
    private Activity context;
    private List<Siswa> daftarSiswa;

    public DaftarSiswa(Activity context, List<Siswa> daftarSiswa){
        super(context, R.layout.daftar_layout, daftarSiswa);
        this.context = context;
        this.daftarSiswa = daftarSiswa;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.daftar_layout, null, true);
        TextView textViewNama = (TextView) listViewItem.findViewById(R.id.textViewNama);
        TextView textViewNISN = (TextView) listViewItem.findViewById(R.id.textViewNISN);
        TextView textViewKehadiran = (TextView) listViewItem.findViewById(R.id.textViewKehadiran);
        TextView textViewWaktu = (TextView) listViewItem.findViewById(R.id.textViewWaktu);

        Siswa siswa = daftarSiswa.get(position);
        textViewNama.setText(siswa.getNamaSiswa());
        textViewNISN.setText(siswa.getNomorInduk());
        textViewKehadiran.setText(siswa.getKehadiranSiswa());
        textViewWaktu.setText(siswa.getWaktu());
        return listViewItem;
    }
}
