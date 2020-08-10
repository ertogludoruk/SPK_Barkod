package com.spk.spkbarkoduygulamas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {

    ImageView okuImage;
    ImageView depoGirisImage;
    ImageView depoAra;


    public MainMenuFragment() {
        // Required empty public constructor
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
                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_depoGirisFragment);
            }
        });
        depoAra = view.findViewById(R.id.imageView_DepoAra);
        depoAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_mainMenuFragment_to_urunAraFragment);
            }
        });

        return view;
    }
}