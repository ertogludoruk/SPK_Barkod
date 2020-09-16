package com.spk.spkbarkoduygulamas;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.helpers.DepoHaraketi;
import com.spk.spkbarkoduygulamas.helpers.Haraket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;


public class DepoGirisCikisFragment extends Fragment {
    Context context;
    int alanSecili;
    Haraket haraket;

    DepoHaraketi okunmusUrun;

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
        okunmusUrun = new DepoHaraketi();
        haraket =(Haraket) (getArguments().getSerializable("argGirisCikis"));
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

        tvStokBarkod = view.findViewById(R.id.textViewBarkod);
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

        if(haraket == Haraket.GIRIS){
            tvTitle.setText(R.string.depo_title_giris);
        }
        else if(haraket == Haraket.CIKIS){
            tvTitle.setText(R.string.depo_title_cikis);
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
                        okunmusUrun.setMiktar(miktar);
                        okunmusUrun.setLot(lot);
                    }
                }
                catch (Exception e){

                }

                if(okunmusUrun.isReady()){
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
                            try {
                                Integer secim = 0;
                                if(haraket == Haraket.GIRIS){
                                    secim = 0;
                                }
                                else if(haraket == Haraket.CIKIS){
                                    secim = 1;
                                }

                                Connection connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
                                String queryStmt = String.format("INSERT INTO [dbo].[Depo_Haraketleri] (sto_kod,lot_kod,islem_tarihi,adet,adres,islem,operator)" +
                                                "VALUES ('%s','%d',CURRENT_TIMESTAMP,'%d',(SELECT id from [DEPO_DB].[dbo].[DepoYerleri] WHERE adres_barkodu = '%s'),%d,%d )",
                                        okunmusUrun.getStokKodu(),okunmusUrun.getLot(),okunmusUrun.getMiktar(),okunmusUrun.getAdres(), secim,MainActivity.giriliHesap.getId());
                                PreparedStatement ps = connect.prepareStatement(queryStmt);

                                ps.executeUpdate();
                                Toast.makeText(context, "İşlem Yapıldı", Toast.LENGTH_SHORT).show();
                                ivTemizle.setEnabled(true);
                                ivOnayla.setEnabled(true);
                                popup.setVisibility(View.INVISIBLE);
                                ClearInputsUI();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "HATA", Toast.LENGTH_SHORT).show();
                            }
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
                if (barcode.contains("DEPO_")) {
                    String depoAdi = barcode.substring(5);
                    tvAdres.setText(depoAdi);
                    okunmusUrun.setAdres(barcode);
                } else {
                    try {
                        Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01", "mikros", "mikro", "192.168.1.249");
                        String queryStmt =
                                String.format("SELECT [bar_stokkodu] FROM [BARKOD_TANIMLARI] WHERE [bar_kodu]='%s'", barcode);
                        PreparedStatement ps = connect.prepareStatement(queryStmt);
                        ResultSet rs = ps.executeQuery();
                        rs.next();

                        String stok_kodu = rs.getString("bar_stokkodu");
                        ps.close();
                        queryStmt =
                                String.format("SELECT [sto_isim] FROM [STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                        ps = connect.prepareStatement(queryStmt);
                        rs = ps.executeQuery();
                        rs.next();

                        String urunadi = rs.getString("sto_isim");
                        urunadi = urunadi.trim().replaceAll(" +", " ");
                        urunadi = urunadi.substring(1, urunadi.length() - 1);
                        ps.close();

                        tvStokBarkod.setText(barcode);
                        tvStokKodu.setText(stok_kodu);
                        tvStokAdi.setText(urunadi);
                        okunmusUrun.setBarkodNo(barcode);
                        okunmusUrun.setStokKodu(stok_kodu);
                        okunmusUrun.setUrunAdi(urunadi);
                    }

                    catch (SQLException e) {
                        String hata = e.getSQLState();
                        if (hata.equals("24000")) {
                            Toast.makeText(context, "Stokta Ürün Kayıtlı Değil", Toast.LENGTH_SHORT).show();
                        } else if (hata.equals("08S01")) {
                            Toast.makeText(context, "Cihazın Bağlantısını Kontrol Edin", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "Veritabanı Hatası: "+hata, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (ClassNotFoundException e){
                        Toast.makeText(context, "Hata Kodu: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        okunmusUrun = new DepoHaraketi();
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