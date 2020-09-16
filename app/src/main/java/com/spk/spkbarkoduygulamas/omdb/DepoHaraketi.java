package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
@Entity
public class DepoHaraketi {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String StokKodu;
    public Integer Miktar;
    public Integer Lot;
    public String Musteri;
    public String Adres;
    public Timestamp Tarih;
    public String BarkodNo;
    public String UrunAdi;
    public Integer Haraket;

}