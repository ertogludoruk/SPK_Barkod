package com.spk.spkbarkoduygulamas.helpers;

import com.spk.spkbarkoduygulamas.MainActivity;
import com.spk.spkbarkoduygulamas.omdb.DepoHaraketi;
import com.spk.spkbarkoduygulamas.omdb.DepoYeri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TugkanHelper {

    static public Map<String,Integer> GetEachDepo(DepoYeri[] yerler, List<DepoHaraketi> haraketler){
        Map<String,Integer> map = new HashMap<>();
        for(DepoYeri yer:yerler){
            map.put(yer.adresAdi,0);
        }
        for(DepoHaraketi haraket:haraketler){
            String depoyeri = haraket.adres;
            Integer aktifSayi = map.get(depoyeri);

            if(haraket.haraket == 0){
                map.put(depoyeri,aktifSayi+haraket.adet);
            }
            else{
                map.put(depoyeri,aktifSayi-haraket.adet);
            }
        }
        return map;
    }

    static public Integer GetDepoAdet(List<DepoHaraketi> haraketler){
        Integer toplam = 0;
        for(DepoHaraketi haraket:haraketler){
            if(haraket.haraket == 0){
                toplam += haraket.adet;
            }
            else{
                toplam -= haraket.adet;
            }
        }
        return toplam;
    }
}
