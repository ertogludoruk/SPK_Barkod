package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class DepoHaraketi {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public String stokKodu;
    public Integer adet;
    public Integer lotKodu;
    public String musteri;
    public String adres;
    public Date tarih;
    public Integer haraket;

    public boolean isReady(){
        if(stokKodu != null && lotKodu != null && adet != null && adres != null && tarih != null && haraket !=null ){
            return true;
        }
        else{
            return false;
        }
    }
}