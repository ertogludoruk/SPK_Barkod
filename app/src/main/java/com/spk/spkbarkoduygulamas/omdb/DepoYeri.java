package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DepoYeri {
    @PrimaryKey(autoGenerate = true)
    public int guid;

    @ColumnInfo(name = "adres_adi")
    public String adresAdi;
}
