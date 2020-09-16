package com.spk.spkbarkoduygulamas.omdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Stok {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sto_kod")
    public String stokKodu;

    @ColumnInfo(name = "bar_kodu")
    public String barkod;

    @ColumnInfo(name = "sto_isim")
    public String stokIsim;

    @ColumnInfo(name = "sto_yabanci_isim")
    public String stokYabanciIsim;

    @ColumnInfo(name = "sto_birim1_ad")
    public String birim1;

    @ColumnInfo(name = "sto_birim2_ad")
    public String birim2;

    @ColumnInfo(name = "sto_birim2_katsayi")
    public String adet;

}
