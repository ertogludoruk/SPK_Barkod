package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM stok WHERE bar_kodu = :barkod")
    public Stok findStokByBarcode(String barkod);

    @Query("SELECT * FROM stok WHERE sto_kod = :stokKodu")
    public Stok findStokByStokKodu(String stokKodu);

    @Insert
    public void insertStokAll(List<Stok> stokList);

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

    @Query("DELETE FROM depoyeri")
    public void deleteDepoYeriData();

    //**************************
    @Insert
    public void insertDepoHaraketi(DepoHaraketi depoHaraketi);

    @Query("SELECT * FROM depoharaketi WHERE stokKodu LIKE :stokKodu")
    public DepoHaraketi[] getDepoHaraketsFromStoKod(String stokKodu);

    @Query("SELECT * FROM depoharaketi WHERE stokKodu LIKE :stokKodu AND haraket = 0")
    public DepoHaraketi[] getDepoHaraketsFromStoKod0(String stokKodu);

    @Query("SELECT * FROM depoharaketi WHERE stokKodu LIKE :stokKodu AND haraket = 1")
    public DepoHaraketi[] getDepoHaraketsFromStoKod1(String stokKodu);

    @Query("SELECT * FROM depoharaketi WHERE adres LIKE :adresi")
    public DepoHaraketi[] getDepoHaraketsFromAdres(String adresi);

    @Query("SELECT * FROM depoharaketi WHERE adres LIKE :adresi AND haraket = 0")
    public DepoHaraketi[] getDepoHaraketsFromAdres0(String adresi);

    @Query("SELECT * FROM depoharaketi WHERE adres LIKE :adresi AND haraket = 1")
    public DepoHaraketi[] getDepoHaraketsFromAdres1(String adresi);

    @Query("DELETE FROM depoharaketi")
    public void deleteDepoHaraketleri();
}
