package com.spk.spkbarkoduygulamas.helpers;

import java.util.Date;

public class DepoUrun{
    private String StokKodu;
    private int Miktar;
    private String Adres;
    private Date Tarih;

    public DepoUrun(String stokKodu, int miktar, String adres, Date tarih){
        StokKodu = stokKodu;
        Miktar = miktar;
        Adres = adres;
        Tarih = tarih;
    }

    public String printUrun(){
        String adr = Adres.replaceAll("\\s+","");
        String str = "Miktar: " + Miktar + " Adres: " + adr + " Tarih: " + Tarih;
        return str;
    }
}
