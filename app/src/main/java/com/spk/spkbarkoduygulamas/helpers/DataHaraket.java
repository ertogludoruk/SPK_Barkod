package com.spk.spkbarkoduygulamas.helpers;


import java.util.Date;

public class DataHaraket {
    private String adres;
    private Haraket hareket;
    private Date zaman;
    private String firma;
    private Integer lot;
    private Integer adet;

    public DataHaraket(){
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Haraket getHareket() {
        return hareket;
    }

    public void setHareket(Haraket hareket) {
        this.hareket = hareket;
    }

    public Date getZaman() {
        return zaman;
    }

    public void setZaman(Date zaman) {
        this.zaman = zaman;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public Integer getLot() {
        return lot;
    }

    public void setLot(Integer lot) {
        this.lot = lot;
    }

    public Integer getAdet() {
        return adet;
    }

    public void setAdet(Integer adet) {
        this.adet = adet;
    }
}
