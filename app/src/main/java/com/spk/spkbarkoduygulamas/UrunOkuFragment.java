package com.spk.spkbarkoduygulamas;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spk.spkbarkoduygulamas.omdb.Stok;

import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;


public class UrunOkuFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
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



        final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String barcode = Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0).coerceToText(context).toString();
                if(barcode.equals(" ")){return;}
                Stok stok = MainActivity.dbMain.userDao().findStokByBarcode(barcode);
                HashMap<String,String> veriler = new HashMap<String,String>();
                veriler.put("Stok Adı",stok.stokIsim);
                veriler.put("Stok Kodu",stok.stokKodu);
                veriler.put("Barkod",stok.barkod);
                veriler.put("Birim",stok.birim1);
                veriler.put("Ambalaj",stok.birim2);
                veriler.put("Adet",stok.adet);
                MyOkumaAdapter okumaAdapter = new MyOkumaAdapter(veriler);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(okumaAdapter);
            }
        });

        return view;
    }
}