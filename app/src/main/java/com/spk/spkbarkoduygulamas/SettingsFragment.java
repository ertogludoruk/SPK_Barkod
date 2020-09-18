package com.spk.spkbarkoduygulamas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.omdb.DepoYeri;
import com.spk.spkbarkoduygulamas.omdb.Hesap;
import com.spk.spkbarkoduygulamas.omdb.Stok;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    Button buttonStokAl;
    Button buttonVeriSıfırla;
    Button buttonHaraketVerileriniKaydet;



    public SettingsFragment() {
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonStokAl = view.findViewById(R.id.button_StoklariAl);
        buttonStokAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomething();
            }
        });

        buttonVeriSıfırla = view.findViewById(R.id.button_Sifirla);
        buttonVeriSıfırla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbMain.userDao().deleteDepoHaraketleri();
            }
        });

        buttonHaraketVerileriniKaydet = view.findViewById(R.id.buttonVeriYedekle);

        buttonHaraketVerileriniKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Connection connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
                    String queryStmt =
                            String.format("INSERT TO\n" +
                                    "  FROM [MikroDB_V16_V01].[dbo].[Tugkan1]");
                }
                catch (Exception e){

                }
            }
        });

        return view;
    }

    private void doSomething(){
        try{
            MainActivity.dbMain.userDao().deleteStokData();
            Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01","mikros","mikro","192.168.1.249");
            String queryStmt =
                    String.format("SELECT [sto_kod],[bar_kodu],[sto_isim],[sto_yabanci_isim],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi]\n" +
                            "  FROM [MikroDB_V16_V01].[dbo].[Tugkan1]");
            PreparedStatement ps = connect.prepareStatement(queryStmt);
            ResultSet rs = ps.executeQuery();
            List<Stok> stokList = new ArrayList<>();
            while (rs.next()){
                Stok stok = new Stok();
                stok.stokKodu = rs.getString("sto_kod");
                stok.barkod = rs.getString("bar_kodu");
                stok.stokIsim = rs.getString("sto_isim");
                stok.stokYabanciIsim = rs.getString("sto_yabanci_isim");
                stok.birim1 = rs.getString("sto_birim1_ad");
                stok.birim2 = rs.getString("sto_birim2_ad");
                stok.adet = Integer.toString((rs.getInt("sto_birim2_katsayi") * -1)) ;
                stokList.add(stok);
            }
            MainActivity.dbMain.userDao().insertStokAll(stokList);

            MainActivity.dbMain.userDao().deleteDepoYeriData();
            connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
            queryStmt =
                    "SELECT * FROM [DepoYerleri]";
            ps = connect.prepareStatement(queryStmt);
            rs = ps.executeQuery();
            List<DepoYeri> yerler = new ArrayList<>();
            while (rs.next()) {
                DepoYeri yer = new DepoYeri();
                yer.guid = rs.getInt("id");
                yer.adresAdi =  rs.getString("adres_adi");
                yerler.add(yer);
            }
            MainActivity.dbMain.userDao().insertDepoAll(yerler);

            MainActivity.dbMain.userDao().deleteHesapsData();
            connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
            queryStmt =
                    "SELECT * FROM [Depo_Personelleri]";
            ps = connect.prepareStatement(queryStmt);
            rs = ps.executeQuery();
            List<Hesap> hesaps = new ArrayList<>();
            while (rs.next()) {
                Hesap hesap = new Hesap();
                hesap.uid =  rs.getInt("id");
                hesap.kullaniciAdi = rs.getString("kullaniciadi");
                hesap.isim = rs.getString("isim");
                hesap.soyisim = rs.getString("soyisim");
                hesap.sifre = rs.getString("sifre");

                hesaps.add(hesap);
            }
            MainActivity.dbMain.userDao().instertHesapAll(hesaps);

        }
        catch (Exception e){
            String za = e.toString();
        }

    }
}