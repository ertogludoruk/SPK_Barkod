package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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

public class UrunAraFragment extends Fragment {
    AutoCompleteTextView edit_text_StokBarkod;
    AutoCompleteTextView edit_text_StokKodu;
    Context context;
    Button buttonAra;
    TextView tvStokBarkod, tvStokKodu, tvStokAdi;
    LinearLayout popUp;
    ListView listview;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public UrunAraFragment(){
    }

    public static UrunAraFragment newInstance(){
        return new UrunAraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public List<String> refreshList(String text, String column){
        List<String> list_barkod = new LinkedList<String>();
        try{
            Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01","mikros","mikro","192.168.1.249");
            String queryStmt =
                    "SELECT * FROM [BARKOD_TANIMLARI] WHERE ["+ column +"] LIKE '"+ text + "%'";
            PreparedStatement ps = connect.prepareStatement(queryStmt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String str = rs.getString(column);
                list_barkod.add(str);
            }
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
        return list_barkod;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_urun_ara, container, false);
        edit_text_StokBarkod = view.findViewById(R.id.editTextBarkod);
        edit_text_StokKodu = view.findViewById(R.id.editTextKodu);
        buttonAra = view.findViewById(R.id.buttonAra);

        tvStokBarkod = view.findViewById(R.id.textViewBarkod);
        tvStokKodu = view.findViewById(R.id.textViewStokKodu);
        tvStokAdi = view.findViewById(R.id.textViewStokAdi);

        popUp = view.findViewById(R.id.popUp);

        listview = view.findViewById(R.id.listview);

        edit_text_StokBarkod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text_barkod = edit_text_StokBarkod.getText().toString();
                List<String> list_barkod = refreshList(text_barkod, "bar_kodu");
                ArrayAdapter<String> adapter =new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_barkod);
                edit_text_StokBarkod.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edit_text_StokKodu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text_stok = edit_text_StokKodu.getText().toString();
                List<String> list_stok = refreshList(text_stok, "bar_stokkodu");
                ArrayAdapter<String> adapter =new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_stok);
                edit_text_StokKodu.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buttonAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_text_StokKodu.getText().toString().equals("") & !edit_text_StokBarkod.getText().toString().equals("")){
                    edit_text_StokBarkod.setText("");
                    edit_text_StokKodu.setText("");
                    popUp.setVisibility(View.GONE);
                    Toast.makeText(context, "İKİ SEÇENEK AYNI ANDA ARANAMAZ", Toast.LENGTH_SHORT).show();
                }
                if (!edit_text_StokBarkod.getText().toString().equals("")){
                    String text = edit_text_StokBarkod.getText().toString();
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
                        String birim = rs.getString("sto_birim1_ad");
                        Integer cins = rs.getInt("sto_cins");
                        Integer ambalajIciAdeti = Math.abs(rs.getInt("sto_birim2_katsayi")) ;
                        ps.close();

                        queryStmt =
                                String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [MikroDB_V16_V01].[dbo].[STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                        ps = connect.prepareStatement(queryStmt);
                        rs = ps.executeQuery();
                        rs.next();
                        ps.close();

                        connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
                        queryStmt = String.format("SELECT [Girilen_Miktar], [Giris_Tarihi], [Girilen_Adres] FROM [DEPO_DB].[dbo].[GirisHareketleri] WHERE [Urun_Kodu]='%s'", stok_kodu);
                        ps = connect.prepareStatement(queryStmt);
                        rs = ps.executeQuery();

                        List<String> tempList = new LinkedList<String>();

                        while(rs.next()){
                            DepoUrun depoUrun = new DepoUrun(stok_kodu, rs.getInt("Girilen_Miktar"),
                                    rs.getString("Girilen_Adres"), rs.getDate("Giris_Tarihi"));
                            tempList.add(depoUrun.printUrun());
                        }
                        ps.close();

                        Urun okunanUrun = new Urun(text,urunadi,cins,birim,stok_kodu,ambalajIciAdeti);
                        Toast.makeText(context, "ÜRÜN BULUNDU", Toast.LENGTH_SHORT).show();

                        tvStokBarkod.setText(okunanUrun.getBarkod());
                        tvStokKodu.setText(okunanUrun.getStokKodu());
                        tvStokAdi.setText(okunanUrun.getIsim());

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
                if(!edit_text_StokKodu.getText().toString().equals("")){
                    String text = edit_text_StokKodu.getText().toString();
                    edit_text_StokKodu.requestFocus();
                    edit_text_StokKodu.setFocusableInTouchMode(true);
                    try {
                        Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01","mikros","mikro","192.168.1.249");
                        String queryStmt =
                                String.format("SELECT [bar_kodu] FROM [BARKOD_TANIMLARI] WHERE [bar_stokkodu]='%s'", text);
                        PreparedStatement ps = connect.prepareStatement(queryStmt);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        String bar_kodu= rs.getString("bar_kodu");
                        ps.close();

                        queryStmt =
                                String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [MikroDB_V16_V01].[dbo].[STOKLAR] WHERE [sto_kod]='%s'", text);
                        ps = connect.prepareStatement(queryStmt);
                        rs = ps.executeQuery();
                        rs.next();
                        String urunadi = rs.getString("sto_isim");
                        String birim = rs.getString("sto_birim1_ad");
                        Integer cins = rs.getInt("sto_cins");
                        Integer ambalajIciAdeti = Math.abs(rs.getInt("sto_birim2_katsayi")) ;
                        ps.close();

                        queryStmt =
                                String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [MikroDB_V16_V01].[dbo].[STOKLAR] WHERE [sto_kod]='%s'", text);
                        ps = connect.prepareStatement(queryStmt);
                        rs = ps.executeQuery();
                        rs.next();
                        ps.close();

                        connect = DBManager.CONN_MSSql_DB("DEPO_DB", "depo_us", "depo2020", "192.168.1.249");
                        queryStmt = String.format("SELECT [Girilen_Miktar], [Giris_Tarihi], [Girilen_Adres] FROM [DEPO_DB].[dbo].[GirisHareketleri] WHERE [Urun_Kodu]='%s'", text);
                        ps = connect.prepareStatement(queryStmt);
                        rs = ps.executeQuery();

                        List<String> tempList = new LinkedList<String>();

                        while(rs.next()){
                            DepoUrun depoUrun = new DepoUrun(text, rs.getInt("Girilen_Miktar"),
                                    rs.getString("Girilen_Adres"), rs.getDate("Giris_Tarihi"));
                            tempList.add(depoUrun.printUrun());
                        }
                        ps.close();


                        Urun okunanUrun = new Urun(bar_kodu,urunadi,cins,birim,text,ambalajIciAdeti);
                        Toast.makeText(context, "ÜRÜN BULUNDU", Toast.LENGTH_SHORT).show();
                        tvStokBarkod.setText(okunanUrun.getBarkod());
                        tvStokKodu.setText(okunanUrun.getStokKodu());
                        tvStokAdi.setText(okunanUrun.getIsim());

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
            }
        });

        return view;
    }
}
