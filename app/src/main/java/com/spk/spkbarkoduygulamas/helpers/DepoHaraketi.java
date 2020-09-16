package com.spk.spkbarkoduygulamas.helpers;

import java.sql.Timestamp;

public class DepoHaraketi {
    private String StokKodu;
    private Integer Miktar;
    private Integer Lot;
    private String Musteri;
    private String Adres;
    private Timestamp Tarih;
    private String BarkodNo;
    private String UrunAdi;
    private Haraket haraket;

    public DepoHaraketi(){
    }

    public boolean isReady(){
        if(StokKodu != null && Miktar != null && Adres != null  && BarkodNo != null && UrunAdi != null) return true;
        else return false;
    }

    public Integer getMiktar() {
        return Miktar;
    }

    public void setMiktar(Integer miktar) {
        Miktar = miktar;
    }

    public String getStokKodu() {
        return StokKodu;
    }

    public void setStokKodu(String stokKodu) {
        StokKodu = stokKodu;
    }

    public String getAdres() {
        return Adres;
    }

    public void setAdres(String adres) {
        Adres = adres;
    }

    public Timestamp getTarih() {
        return Tarih;
    }

    public void setTarih(Timestamp tarih) {
        Tarih = tarih;
    }

    public String getBarkodNo() {
        return BarkodNo;
    }

    public void setBarkodNo(String barkodNo) {
        BarkodNo = barkodNo;
    }

    public String getUrunAdi() {
        return UrunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        UrunAdi = urunAdi;
    }

    public Integer getLot() {
        return Lot;
    }

    public void setLot(Integer lot) {
        Lot = lot;
    }

    public Haraket getHaraket() {
        return haraket;
    }

    public void setHaraket(Haraket haraket) {
        this.haraket = haraket;
    }

    public String getMusteri() {
        return Musteri;
    }

    public void setMusteri(String musteri) {
        Musteri = musteri;
    }
}
