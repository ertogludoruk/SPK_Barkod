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
import com.spk.spkbarkoduygulamas.helpers.DataHaraket;
import com.spk.spkbarkoduygulamas.helpers.Haraket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class UrunGosterFragment extends Fragment {

    TextView tvAdi,tvStokkodu, tvBarkod;
    LinearLayout popUp;
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
        tvBarkod = view.findViewById(R.id.textViewBarkod);
        popUp = view.findViewById(R.id.popUp);
        listview = view.findViewById(R.id.listview);

        if (spinnerValue.equals("bar_kodu")) {
            String text = data;
        }

        String stokKodu = "C12100M5-020020";
        ArrayList<DataHaraket> haraketler = new ArrayList<>();

        try{
            Connection connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
            String queryStmt =
                    String.format("SELECT        TOP (100) PERCENT dbo.Depo_Haraketleri.*, dbo.Depo_Haraketleri.islem_tarihi AS Expr1, dbo.DepoYerleri.adres_adi, dbo.Depo_Haraketleri.sto_kod AS Expr2\n" +
                            "FROM            dbo.Depo_Haraketleri INNER JOIN\n" +
                            "                         dbo.DepoYerleri ON dbo.Depo_Haraketleri.adres = dbo.DepoYerleri.id\n" +
                            "WHERE        (dbo.Depo_Haraketleri.[sto_kod] = '%s')\n" +
                            "ORDER BY Expr1 DESC",stokKodu);
            PreparedStatement ps = connect.prepareStatement(queryStmt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int lot = rs.getInt("lot_kod");
                String musteri = rs.getString("musteri");
                int adet = rs.getInt("adet");
                Date tarih = rs.getTimestamp("islem_tarihi");
                String adres = rs.getString("adres_adi");
                int islem = rs.getInt("islem");
                DataHaraket dataHaraket = new DataHaraket();
                Haraket haraket;
                if(islem == 0){
                    haraket = Haraket.GIRIS;
                }
                else if(islem == 1){
                    haraket =  Haraket.CIKIS;
                }
                else{
                    haraket = Haraket.GIRIS;
                }
                dataHaraket.setHareket(haraket);
                dataHaraket.setAdres(adres);
                dataHaraket.setFirma(musteri);
                dataHaraket.setZaman(tarih);
                dataHaraket.setAdet(adet);
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

        return view;
    }
}
