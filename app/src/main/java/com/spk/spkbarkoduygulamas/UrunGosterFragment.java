package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.helpers.DepoHaraketi;
import com.spk.spkbarkoduygulamas.helpers.Haraket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class UrunGosterFragment extends Fragment {

    TextView tvAdi,tvStokkodu, tvBarkod;
    ListView listview;
    Context context;
    String data;
    String spinnerValue;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public UrunGosterFragment(){
    }

    public static UrunGosterFragment newInstance(){
        return new UrunGosterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_urun_goster, container, false);

        Bundle bd = this.getArguments();
        assert bd != null;
        data = bd.getString("key1");
        spinnerValue = bd.getString("key2");

        tvAdi = view.findViewById(R.id.textViewStokAdi);
        tvStokkodu = view.findViewById(R.id.textViewStokKodu);
        listview = view.findViewById(R.id.listview);



        if (spinnerValue.equals("spinner_stokkodu")) {
            String stok_kodu = data;
            tvStokkodu.setText(stok_kodu);
            ArrayList<DepoHaraketi> haraketler = new ArrayList<>();

            try{
                Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01","mikros","mikro","192.168.1.249");
                String queryStmt =
                        String.format("SELECT [sto_isim] FROM [MikroDB_V16_V01].[dbo].[STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                PreparedStatement ps = connect.prepareStatement(queryStmt);
                ResultSet rs = ps.executeQuery();
                rs.next();

                String urunadi = rs.getString("sto_isim");
                urunadi = urunadi.trim().replaceAll(" +", " ");
                urunadi = urunadi.substring(1, urunadi.length()-1);
                tvAdi.setText(urunadi);


                connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
                queryStmt =
                        String.format("SELECT * FROM dbo.Haraket_Islem WHERE [sto_kod] = '%s'", stok_kodu);

                ps = connect.prepareStatement(queryStmt);
                rs = ps.executeQuery();

                while (rs.next()) {
                    int lot = rs.getInt("lot_kod");
                    String musteri = rs.getString("musteri");
                    int adet = rs.getInt("adet");
                    Timestamp tarih = rs.getTimestamp("islem_tarihi");
                    String adres = rs.getString("adres_adi");
                    int islem = rs.getInt("islem");
                    DepoHaraketi dataHaraket = new DepoHaraketi();
                    Haraket haraket = Haraket.GIRIS;
                    if(islem == 0){
                        haraket = Haraket.GIRIS;
                    }
                    else if(islem == 1){
                        haraket =  Haraket.CIKIS;
                    }


                    dataHaraket.setHaraket(haraket);
                    dataHaraket.setAdres(adres);
                    dataHaraket.setMusteri(musteri);
                    dataHaraket.setTarih(tarih);
                    dataHaraket.setMiktar(adet);
                    dataHaraket.setLot(lot);
                    haraketler.add(dataHaraket);
                }

            } catch (SQLException e) {
                String hata = e.getSQLState();
                if(hata.equals("24000")){
                    Toast.makeText(context, "Kayıt Bulunamadı", Toast.LENGTH_SHORT).show();
                }
                else if(hata.equals("08S01")){
                    Toast.makeText(context, "Cihazın Bağlantısını Kontrol Edin", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Veritabanı Hatası, Uygulama Güncellenmeli", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Throwable za = e.getCause();
            }
            CustomHaraketAdapter adapter = new CustomHaraketAdapter(haraketler,context);
            listview.setAdapter(adapter);
        }
        else if(spinnerValue.equals("spinner_adres")){

        }


        return view;
    }
}
