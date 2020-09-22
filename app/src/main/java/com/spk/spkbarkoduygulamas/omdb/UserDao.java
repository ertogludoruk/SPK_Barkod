package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    public void insertStokAll(List<Stok> stokList);

    @Query("SELECT * FROM stok WHERE bar_kodu = :barkod")
    public Stok findStokByBarcode(String barkod);

    @Query("SELECT * FROM stok WHERE sto_kod = :stokKodu")
    public Stok findStokByStokKodu(String stokKodu);

    @Query("DELETE FROM stok")
    public void deleteStokData();

    @Query("SELECT * FROM stok WHERE sto_kod LIKE :typedChars")
    public Stok[] getStokFromChars(String typedChars);

    //**************************

    @Insert
    public void instertHesapAll(List<Hesap> hesapList);

    @Query("DELETE FROM hesap")
    public void deleteHesapsData();

    @Query("SELECT * FROM hesap WHERE kullaniciAdi = :kullaniciAdi AND sifre = :password")
    public Hesap findUserByIdAndPass(String kullaniciAdi,String password);

    //**************************

    @Insert
    public void insertDepoAll(List<DepoYeri> depoYeriList);

    @Query("SELECT adres_adi FROM depoyeri")
    public DepoYeri[] getButunDepoYerleri();

    @Query("SELECT adres_adi FROM depoyeri WHERE adres_adi LIKE :depoYeri")
    public DepoYeri getDepoYeri(String depoYeri);

    @Query("DELETE FROM depoyeri")
    public void deleteDepoYeriData();

    //**************************
    @Insert
    public void insertDepoHaraketi(DepoHaraketi depoHaraketi);

    @Query("SELECT * FROM depoharaketi WHERE stokKodu LIKE :stokKodu")
    public DepoHaraketi[] getDepoHaraketsFromStoKod(String stokKodu);

    @Query("SELECT * FROM depoharaketi WHERE adres == :depoAdi")
    public DepoHaraketi[] getDepoHaraketsFromDepoAdi(String depoAdi);

    @Query("DELETE FROM depoharaketi")
    public void deleteDepoHaraketleri();
}
