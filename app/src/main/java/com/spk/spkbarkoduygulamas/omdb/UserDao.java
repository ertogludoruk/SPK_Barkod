package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM stok WHERE bar_kodu = :barkod")
    public Stok findStokByBarcode(String barkod);

    @Query("SELECT * FROM hesap WHERE kullaniciAdi = :kullaniciAdi AND sifre = :password")
    public Hesap findUserByIdAndPass(String kullaniciAdi,String password);

    @Insert
    public void insertStokAll(List<Stok> stokList);

    @Insert
    public void instertHesapAll(List<Hesap> hesapList);

    @Query("DELETE FROM stok")
    public void deleteStokData();

    @Query("SELECT * FROM stok WHERE sto_kod LIKE :typedChars")
    public Stok[] getStokFromChars(String typedChars);

    @Insert
    public void insertDepoAll(List<DepoYeri> depoYeriList);

    @Query("SELECT adres_adi FROM depoyeri")
    public DepoYeri[] getButunDepoYerleri();

    @Query("DELETE FROM depoyeri")
    public void deleteDepoYeriData();

    @Query("DELETE FROM hesap")
    public void deleteHesapsData();
}
