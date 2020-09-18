package com.spk.spkbarkoduygulamas;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.spk.spkbarkoduygulamas.omdb.Stok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;


public class UrunOkuFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    Button depoAra;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public UrunOkuFragment() {
    }


    public static UrunOkuFragment newInstance() {
        UrunOkuFragment fragment = new UrunOkuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_urun_oku, container, false);
        recyclerView = view.findViewById(R.id.RecyclerViewOku);
        depoAra = view.findViewById(R.id.buttonDepoAra);
        depoAra.setVisibility(View.GONE);



        final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String barcode = Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0).coerceToText(context).toString();
                if(barcode.equals(" ")){return;}
                final Stok stok = MainActivity.dbMain.userDao().findStokByBarcode(barcode);
                if(stok != null) {
                    List<String[]> veriler = new ArrayList<>();
                    veriler.add(new String[]{"Stok Adı", stok.stokIsim});
                    veriler.add(new String[]{"Stok Kodu", stok.stokKodu});
                    veriler.add(new String[]{"Barkod", stok.barkod});
                    veriler.add(new String[]{"Birim", stok.birim1});
                    veriler.add(new String[]{"Ambalaj", stok.birim2});
                    veriler.add(new String[]{"Adet", stok.adet});

                    MyOkumaAdapter okumaAdapter = new MyOkumaAdapter(veriler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(okumaAdapter);
                    depoAra.setVisibility(View.VISIBLE);
                    depoAra.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bd = new Bundle();
                            bd.putString("keyData", stok.stokKodu);
                            bd.putString("keySpinnerChoice", "spinner_stokkodu");
                            Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_urunOkuFragment_to_urunGosterFragment, bd);
                        }
                    });
                }
                else{
                    Toast.makeText(context, "ÜRÜN STOKTA KAYITLI DEĞİL", Toast.LENGTH_SHORT).show();
                    depoAra.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
}