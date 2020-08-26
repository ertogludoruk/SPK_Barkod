package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.helpers.DepoUrun;
import com.spk.spkbarkoduygulamas.helpers.Urun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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

        if (spinnerValue.equals("bar_kodu")){
            String text = data;
            try {
                Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01","mikros","mikro","192.168.1.249");
                String queryStmt =
                        String.format("SELECT [bar_stokkodu] FROM [BARKOD_TANIMLARI] WHERE [bar_kodu]='%s'", text);
                PreparedStatement ps = connect.prepareStatement(queryStmt);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String stok_kodu= rs.getString("bar_stokkodu");
                ps.close();

                queryStmt =
                        String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [MikroDB_V16_V01].[dbo].[STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                ps = connect.prepareStatement(queryStmt);
                rs = ps.executeQuery();
                rs.next();
                String urunadi = rs.getString("sto_isim");
                urunadi = urunadi.trim().replaceAll(" +", " ");
                urunadi = urunadi.substring(1, urunadi.length()-1);
                String birim = rs.getString("sto_birim1_ad");
                Integer cins = rs.getInt("sto_cins");
                Integer ambalajIciAdeti = Math.abs(rs.getInt("sto_birim2_katsayi")) ;
                ps.close();

                connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
                queryStmt = String.format("SELECT [Girilen_Miktar], [Giris_Tarihi], [Girilen_Adres] FROM [DEPO_DB].[dbo].[GirisHareketleri] WHERE [Urun_Kodu]='%s'", stok_kodu);
                ps = connect.prepareStatement(queryStmt);
                rs = ps.executeQuery();

                List<String> tempList = new LinkedList<String>();

                while(rs.next()){
                    DepoUrun depoUrun = new DepoUrun();
                    depoUrun.setStokKodu(stok_kodu);
                    depoUrun.setMiktar(rs.getInt("Girilen_Miktar"));
                    depoUrun.setAdres("Girilen_Adres");
                    depoUrun.setTarih(rs.getTimestamp("Giris_Tarihi"));

                    tempList.add(depoUrun.printUrun());
                }
                ps.close();

                Urun okunanUrun = new Urun(text,urunadi,cins,birim,stok_kodu,ambalajIciAdeti);
                Toast.makeText(context, "ÜRÜN BULUNDU", Toast.LENGTH_SHORT).show();

                tvBarkod.setText(okunanUrun.getBarkod());
                tvStokkodu.setText(okunanUrun.getStokKodu());
                tvAdi.setText(okunanUrun.getIsim());

                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, tempList);
                listview.setAdapter(listAdapter);

                popUp.setVisibility(View.VISIBLE);

            } catch (SQLException e) {
                String hata = e.getSQLState();
                if(hata.equals("24000")){
                    Toast.makeText(context, "Stokta Ürün Kayıtlı Değil", Toast.LENGTH_SHORT).show();
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
        }
        else if(spinnerValue.equals("bar_stokkodu")){

        }
        else{

        }

        return view;
    }
}
