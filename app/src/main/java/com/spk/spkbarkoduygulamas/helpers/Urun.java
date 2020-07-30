package com.spk.spkbarkoduygulamas.helpers;

public class Urun {
    private String Isim;
    private Integer Cins;
    private String Birim1;
    private String StokKodu;
    private String Barkod;
    private Integer AmbalajAdeti;

    public Urun(String barkod, String isim, Integer cins, String birim1,String stokKodu,Integer ambalajAdeti){
        Isim = isim;
        Cins = cins;
        Birim1 = birim1;
        StokKodu = stokKodu;
        Barkod = barkod;
        AmbalajAdeti = ambalajAdeti;
    }

    public String getIsim() {
        return Isim;
    }

    public Integer getCins() {
        return Cins;
    }

    public String getBirim1() {
        return Birim1;
    }

    public String getStokKodu() {
        return StokKodu;
    }

    public String getBarkod() {
        return Barkod;
    }

    public Integer getAmbalajAdeti() {
        return AmbalajAdeti;
    }
}
