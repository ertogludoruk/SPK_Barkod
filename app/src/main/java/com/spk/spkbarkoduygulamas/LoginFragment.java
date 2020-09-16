package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.helpers.Hesap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFragment extends Fragment {
    Context context;
    EditText editTextKullaniciAdi;
    EditText editTextSifre;
    Button buttonGiris;


    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        editTextKullaniciAdi = view.findViewById(R.id.hesapEditTextTextKullaniciAdi);
        editTextSifre = view.findViewById(R.id.hesapEditTextTextPassword);
        buttonGiris = view.findViewById(R.id.hesapButtonGiris);
        if(MainActivity.giriliHesap == null){
            buttonGiris.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String kullaniciAdi = editTextKullaniciAdi.getText().toString();
                    String sifre = editTextSifre.getText().toString();
                    if(kullaniciAdi.length()>4 && sifre.length()>4 ){
                        try{
                            Connection connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
                            String queryStmt = "SELECT *\n" +
                                    "FROM DEPO_DB.dbo.Depo_Personelleri WHERE kullaniciadi = ? AND sifre = ?";
                            PreparedStatement ps = connect.prepareStatement(queryStmt);
                            ps.setString(1,kullaniciAdi);
                            ps.setString(2,sifre);
                            ResultSet rs = ps.executeQuery();
                            if(rs.next()){
                                Hesap hesap = new Hesap();
                                hesap.setId(rs.getInt("id"));
                                hesap.setIsim(rs.getString("isim"));
                                hesap.setSoyisim(rs.getString("soyisim"));
                                hesap.setKullaniciAdi(rs.getString("kullaniciadi"));
                                MainActivity mainActivity = (MainActivity)getActivity();
                                mainActivity.girisYap(hesap);
                                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_loginFragment_to_mainMenuFragment11);
                            }
                            else{
                                Toast.makeText(context, "Yanlış Giriş Yapıldı", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){

                        }

                    }
                    else {
                        Toast.makeText(context, "Geçerli Bir Personel Giriniz", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            editTextKullaniciAdi.setText(MainActivity.giriliHesap.getKullaniciAdi());
            editTextSifre.setEnabled(false);
            buttonGiris.setText("Çıkış Yap");
            buttonGiris.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.cikisYap();
                    Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_loginFragment_self);
                }
            });
        }

        return view;
    }
}