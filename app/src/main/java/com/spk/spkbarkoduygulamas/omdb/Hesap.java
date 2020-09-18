package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Hesap {
    @PrimaryKey(autoGenerate = true)
    public Integer uid;
    public String isim;
    public String soyisim;
    public String sifre;
    public String kullaniciAdi;

}