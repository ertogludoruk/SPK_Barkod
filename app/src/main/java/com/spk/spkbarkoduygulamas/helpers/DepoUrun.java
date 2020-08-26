package com.spk.spkbarkoduygulamas.helpers;

import java.sql.Timestamp;

public class DepoUrun{
    private String StokKodu;
    private Integer Miktar;
    private Integer Lot;
    private String Adres;
    private Timestamp Tarih;
    private String BarkodNo;
    private String UrunAdi;

    public DepoUrun(){
    }

    public boolean isReady(){
        if(StokKodu != null && Miktar != null && Adres != null  && BarkodNo != null && UrunAdi != null) return true;
        else return false;
    }

    public String printUrun(){
        String adr = Adres.replaceAll("\\s+","");
        String str = "Miktar: " + Miktar + " Adres: " + adr + " Tarih: " + Tarih;
        return str;
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
}
