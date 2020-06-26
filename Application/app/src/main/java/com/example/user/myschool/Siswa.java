package com.example.user.myschool;

import com.google.firebase.auth.FirebaseUser;

public class Siswa {
    String namaSiswa;
    String nomorInduk;
    String kehadiranSiswa;
    String waktu;

    public Siswa(){

    }

    public Siswa(String namaSiswa, String nomorInduk, String kehadiranSiswa, String waktu) {
        this.namaSiswa = namaSiswa;
        this.nomorInduk = nomorInduk;
        this.kehadiranSiswa = kehadiranSiswa;
        this.waktu = waktu;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public void setNomorInduk(String nomorInduk) {
        this.nomorInduk = nomorInduk;
    }

    public void setKehadiran(String kehadiranSiswa) {
        this.kehadiranSiswa = kehadiranSiswa;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public String getNomorInduk() {
        return nomorInduk;
    }

    public String getKehadiranSiswa() {
        return kehadiranSiswa;
    }

    public String getWaktu() {
        return waktu;
     }

}
