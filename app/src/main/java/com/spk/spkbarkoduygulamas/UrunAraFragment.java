package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.spk.spkbarkoduygulamas.helpers.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UrunAraFragment extends Fragment {
    AutoCompleteTextView edit_text;
    Context context;
    Button buttonAra;
    Spinner spinner;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public UrunAraFragment(){
    }

    public void myOnKeyDown(int key_code){
        if(key_code == KeyEvent.KEYCODE_3) {
            String text = edit_text.getText().toString();
            List<String> list;
            if(spinner.getSelectedItem().toString().equals("ADRES")){
                list = refreshListAdres(text, "Yer_Adi");
            }
            else if(spinner.getSelectedItem().toString().equals("STOK KODU")){
                list = refreshList(text, "bar_stokkodu");
            }
            else{
                list = refreshList(text, "bar_kodu");
            }
            ArrayAdapter<String> adapter =new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list);
            edit_text.setAdapter(adapter);
        }
    }

    public static UrunAraFragment newInstance(){
        return new UrunAraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MainActivity.activeFragment = this;
        super.onCreate(savedInstanceState); }

    public List<String> refreshList(String text, String column){
        List<String> list = new LinkedList<String>();
        try{
            Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01","mikros","mikro","192.168.1.249");
            String queryStmt =
                    "SELECT * FROM [BARKOD_TANIMLARI] WHERE ["+ column +"] LIKE '"+ text + "%'";
            PreparedStatement ps = connect.prepareStatement(queryStmt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String str = rs.getString(column);
                list.add(str);
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
        return list;
    }
    public List<String> refreshListAdres(String text, String column){
        List<String> list = new LinkedList<String>();
        try{
            Connection connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
            String queryStmt =
                    "SELECT * FROM [DepoYerleri] WHERE ["+ column +"] LIKE '"+ text + "%'";
            PreparedStatement ps = connect.prepareStatement(queryStmt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String str = rs.getString(column);
                list.add(str);
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
        return list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_urun_ara, container, false);
        edit_text = view.findViewById(R.id.editTextBarkod);
        buttonAra = view.findViewById(R.id.buttonAra);

        spinner = view.findViewById(R.id.spinner);
        String[] items = new String[]{"ADRES", "STOK KODU", "BARKOD"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adp);

        buttonAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sp;
                if(spinner.getSelectedItem().toString().equals("BARKOD")){sp = "bar_kodu";}
                else if (spinner.getSelectedItem().toString().equals("STOK KODU")){sp = "bar_stokkodu";}
                else{sp = "Yer_Adi";}

                Bundle bd = new Bundle();
                bd.putString("key1", edit_text.getText().toString());
                bd.putString("key2", sp);

                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_urunAraFragment_to_urunGosterFragment, bd);
            }
        });

        return view;
    }
}
