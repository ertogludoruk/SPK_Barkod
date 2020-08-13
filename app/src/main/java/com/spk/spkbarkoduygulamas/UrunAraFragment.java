package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.content.Intent;
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
import androidx.navigation.Navigation;

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
                Bundle bd = new Bundle();
                bd.putString("key1", edit_text_StokBarkod.getText().toString());
                bd.putString("key2", edit_text_StokKodu.getText().toString());

                if(!edit_text_StokKodu.getText().toString().equals("") & !edit_text_StokBarkod.getText().toString().equals("")){
                    edit_text_StokBarkod.setText("");
                    edit_text_StokKodu.setText("");
                    Toast.makeText(context, "İKİ SEÇENEK AYNI ANDA ARANAMAZ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_urunAraFragment_to_urunGosterFragment, bd);
                }
            }
        });

        return view;
    }
}
