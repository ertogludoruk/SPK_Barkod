package com.spk.spkbarkoduygulamas;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.spk.spkbarkoduygulamas.omdb.DepoHaraketi;
import com.spk.spkbarkoduygulamas.omdb.Stok;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;


public class DepoGirisCikisFragment extends Fragment {
    Context context;
    int alanSecili;
    Integer haraket;

    DepoHaraketi depoHaraketi;

    TextView tvStokBarkod;
    TextView tvStokKodu;
    TextView tvStokAdi;
    TextView tvLot;
    TextView tvTitle;
    TextView tvAdet;
    TextView tvAdres;
    ImageView ivTemizle;
    ImageView ivOnayla;
    TextView tvEvet;
    TextView tvHayir;
    LinearLayout popup;
    EditText editTextGidenFirma;
    TextView tvLotAdi;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public DepoGirisCikisFragment() {
    }

    public static DepoGirisCikisFragment newInstance() {
        DepoGirisCikisFragment fragment = new DepoGirisCikisFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        depoHaraketi = new DepoHaraketi();
        haraket = (getArguments().getInt("argGirisCikis"));
    }

    public void myOnKeyDown(int key_code){
        if(7 <= key_code && key_code <= 16 ) {
            if(alanSecili ==0){
                tvAdet.setText(tvAdet.getText().toString() + (key_code - 7));
            }
            else if(alanSecili ==1){
                tvLot.setText(tvLot.getText().toString() + (key_code - 7));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_depo_giriscikis, container, false);

        tvStokBarkod = view.findViewById(R.id.tvOkuValue);
        tvStokKodu = view.findViewById(R.id.textViewStokKodu);
        tvStokAdi = view.findViewById(R.id.textViewStokAdi);
        ivTemizle = view.findViewById(R.id.imageViewTemizle);
        ivOnayla = view.findViewById(R.id.imageViewOnayla);
        popup = view.findViewById(R.id.popUpWindow);
        tvEvet = view.findViewById(R.id.textViewEvet);
        tvHayir = view.findViewById(R.id.textViewHayir);
        tvAdet = view.findViewById(R.id.textViewAdet);
        tvLot = view.findViewById(R.id.textViewLot);
        tvAdres = view.findViewById(R.id.textViewAdres);
        tvTitle = view.findViewById(R.id.tvDepoTitle);
        editTextGidenFirma = view.findViewById(R.id.editTextGidenFirma);
        tvLotAdi = view.findViewById(R.id.tvLotAdi);

        popup.setVisibility(View.GONE);

        if(haraket == 0){
            tvTitle.setText(R.string.depo_title_giris);
            editTextGidenFirma.setVisibility(View.GONE);
        }
        else if(haraket == 1){
            tvTitle.setText(R.string.depo_title_cikis);
            editTextGidenFirma.setVisibility(View.VISIBLE);
        }

        ivTemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearInputsUI();
            }
        });
        ivOnayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Integer miktar = Integer.parseInt(tvAdet.getText().toString());
                    Integer lot = Integer.parseInt(tvLot.getText().toString());
                    if(miktar > 0 && lot > 0){
                        depoHaraketi.adet = miktar;
                        depoHaraketi.lotKodu = lot;
                    }
                }
                catch (Exception e){

                }
                depoHaraketi.haraket = haraket;
                depoHaraketi.tarih = new Date();
                depoHaraketi.musteri = editTextGidenFirma.getText().toString();

                if(depoHaraketi.isReady()){
                    ivTemizle.setEnabled(false);
                    ivOnayla.setEnabled(false);
                    popup.setVisibility(View.VISIBLE);
                    tvHayir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ivTemizle.setEnabled(true);
                            ivOnayla.setEnabled(true);
                            popup.setVisibility(View.INVISIBLE);
                        }
                    });
                    tvEvet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.dbMain.userDao().insertDepoHaraketi(depoHaraketi);
                            ivTemizle.setEnabled(true);
                            ivOnayla.setEnabled(true);
                            popup.setVisibility(View.INVISIBLE);
                            ClearInputsUI();
                        }
                    });
                }
                else{
                    Toast.makeText(context, "LÜTFEN ALANLARI DOLDURUNUZ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String barcode = Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0).coerceToText(context).toString();
                if(barcode.equals(" ")){return;}
                if(barcode.contains("DEPO_")){
                    String depoAdi = barcode.substring(5);
                    depoHaraketi.adres = depoAdi;
                    tvAdres.setText(depoHaraketi.adres);
                }
                else{
                    Stok stok = MainActivity.dbMain.userDao().findStokByBarcode(barcode);
                    if(stok!=null){
                        depoHaraketi.stokKodu = stok.stokKodu;
                        tvStokKodu.setText(depoHaraketi.stokKodu);
                        tvStokAdi.setText(stok.stokIsim);
                        tvStokBarkod.setText(stok.barkod);
                    }
                    else{
                        Toast.makeText(context, "ÜRÜN STOKTA KAYITLI DEĞİL", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvAdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alanSec(0);
            }
        });

        tvLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alanSec(1);
            }
        });

        tvLotAdi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Calendar calendar = Calendar.getInstance();
                Integer day = calendar.get(Calendar.DAY_OF_WEEK);
                day = day - 1;
                if(day==0){
                    day = 6;
                }
                Integer week = calendar.get(Calendar.WEEK_OF_YEAR);
                Integer year = calendar.get(Calendar.YEAR);
                year=year-2000;
                String lotnum=String.format("%d%d%d",day,week,year);

                tvLot.setText(lotnum);

                return false;
            }
        });

        alanSec(0);
        return view;
    }
    private void ClearInputsUI(){
        tvStokBarkod.setText("");
        tvStokKodu.setText("");
        tvStokAdi.setText("");
        tvAdet.setText("");
        tvAdres.setText("");
        tvLot.setText("");
        editTextGidenFirma.setText("");
        depoHaraketi = new DepoHaraketi();
    }
    private void alanSec(int alan){
        alanSecili = alan;
        if(alan == 0){
            tvAdet.setBackgroundColor(Color.parseColor("#00000000"));
            tvLot.setBackground(getResources().getDrawable(R.color.black_overlay));
        }
        else if(alan ==1){
            tvAdet.setBackground(getResources().getDrawable(R.color.black_overlay));
            tvLot.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }
}