package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {
    Context context;

    ImageView okuImage;
    ImageView depoGirisImage;
    ImageView depoAraImage;
    ImageView depoCikisImage;
    ImageView hesapGirisImage;
    ImageView ayarlarImage;


    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        okuImage = view.findViewById(R.id.imageView_Oku);
        okuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_urunOkuFragment);
            }
        });
        depoGirisImage = view.findViewById(R.id.imageView_Giris);
        depoGirisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.giriliHesap!=null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("argGirisCikis",0);
                    Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_depoGirisCikisFragment, bundle);
                }
                else{
                    Toast.makeText(context, "LÜTFEN GİRİŞ YAPINIZ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        depoAraImage = view.findViewById(R.id.imageView_DepoAra);
        depoAraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_urunAraFragment);
            }
        });
        depoCikisImage = view.findViewById(R.id.imageView_Cikis);
        depoCikisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.giriliHesap!=null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("argGirisCikis",1);
                    Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_depoGirisCikisFragment, bundle);
                }
                else{
                    Toast.makeText(context, "LÜTFEN GİRİŞ YAPINIZ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hesapGirisImage = view.findViewById(R.id.imageView_Hesap);
        hesapGirisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_loginFragment);
            }
        });
        ayarlarImage = view.findViewById(R.id.imageView_Settings);
        ayarlarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_settingsFragment);
            }
        });

        return view;
    }
}