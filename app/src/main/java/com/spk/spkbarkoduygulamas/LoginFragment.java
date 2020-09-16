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
import com.spk.spkbarkoduygulamas.omdb.Hesap;

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
                        Hesap hesap = MainActivity.dbMain.userDao().findUserByIdAndPass(kullaniciAdi,sifre);

                        MainActivity mainActivity = (MainActivity)getActivity();
                        if(hesap !=null){
                            mainActivity.girisYap(hesap);
                            Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_loginFragment_to_mainMenuFragment11);
                        }
                        else{
                            Toast.makeText(context, "Kullanıcı adı Veya Şifre Hatalı", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(context, "Geçerli Bir Personel Giriniz", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            editTextKullaniciAdi.setText(MainActivity.giriliHesap.kullaniciAdi);
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