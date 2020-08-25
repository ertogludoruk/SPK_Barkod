package com.spk.spkbarkoduygulamas;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.helpers.DepoUrun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;


public class DepoGirisFragment extends Fragment {
    TextView tvStokBarkod;
    TextView tvStokKodu;
    TextView tvStokAdi;

    DepoUrun okunmusUrun;

    TextView tvAdet;
    TextView tvAdres;
    ImageView ivTemizle;
    ImageView ivOnayla;
    TextView tvEvet;
    TextView tvHayir;
    LinearLayout popup;
    Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public DepoGirisFragment() {
    }

    public static DepoGirisFragment newInstance() {
        DepoGirisFragment fragment = new DepoGirisFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        okunmusUrun = new DepoUrun();
        MainActivity.activeFragment = this;
    }

    public void myOnKeyDown(int key_code){
        if(7 < key_code && key_code < 16 ) {
            tvAdet.setText(tvAdet.getText().toString() + (key_code - 7));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_depo_giris, container, false);

        tvStokBarkod = view.findViewById(R.id.textViewBarkod);
        tvStokKodu = view.findViewById(R.id.textViewStokKodu);
        tvStokAdi = view.findViewById(R.id.textViewStokAdi);
        ivTemizle = view.findViewById(R.id.imageViewTemizle);
        ivOnayla = view.findViewById(R.id.imageViewOnayla);
        popup = view.findViewById(R.id.popUpWindow);
        tvEvet = view.findViewById(R.id.textViewEvet);
        tvHayir = view.findViewById(R.id.textViewHayir);
        tvAdet = view.findViewById(R.id.textViewAdet);
        tvAdres = view.findViewById(R.id.textViewAdres);





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
                    if(miktar > 0){
                        okunmusUrun.setMiktar(miktar);
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
                                Connection connect = DBManager.CONN_MSSql_DB("DEPO_DB","depo_us","depo2020","192.168.1.249");
                                String queryStmt = String.format("INSERT INTO [DEPO_DB].[dbo].[GirisHareketleri] (Urun_Kodu, Girilen_Miktar, Giris_Tarihi, Girilen_Adres) VALUES ('%s','%d',CURRENT_TIMESTAMP,'%s')",
                                        okunmusUrun.getStokKodu(),okunmusUrun.getMiktar(),okunmusUrun.getAdres() );
                                PreparedStatement ps = connect.prepareStatement(queryStmt);

                                ps.executeUpdate();
                                Toast.makeText(context, "STOK EKLENDİ", Toast.LENGTH_SHORT).show();
                                ivTemizle.setEnabled(true);
                                ivOnayla.setEnabled(true);
                                popup.setVisibility(View.INVISIBLE);
                                ClearInputsUI();
                            } catch (SQLException | ClassNotFoundException e) {
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
                if(barcode.contains("DEPO_")){
                    String depoAdi = barcode.substring(5);
                    tvAdres.setText(depoAdi);
                    okunmusUrun.setAdres(depoAdi);
                }
                else{
                    DepoGirisFragment.ReadBarcode1 readBarcode = new DepoGirisFragment.ReadBarcode1();
                    readBarcode.execute(barcode);
                }
            }
        });

        return view;
    }
    private class ReadBarcode1 extends AsyncTask<String, String, DepoUrun>{
        @Override
        protected void onPreExecute() {
        }

        @Override
            protected DepoUrun doInBackground(String... strings) {

            try {
                Connection connect = DBManager.CONN_MSSql_DB("MikroDB_V16_V01","mikros","mikro","192.168.1.249");
                String barcode = strings[0];
                String queryStmt =
                        String.format("SELECT [bar_stokkodu] FROM [BARKOD_TANIMLARI] WHERE [bar_kodu]='%s'", barcode);
                PreparedStatement ps = connect.prepareStatement(queryStmt);
                ResultSet rs = ps.executeQuery();
                rs.next();

                String stok_kodu= rs.getString("bar_stokkodu");
                ps.close();
                queryStmt =
                        String.format("SELECT [sto_isim] FROM [STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                ps = connect.prepareStatement(queryStmt);
                rs = ps.executeQuery();
                rs.next();

                String urunadi = rs.getString("sto_isim");
                urunadi = urunadi.trim().replaceAll(" +", " ");
                urunadi = urunadi.substring(1, urunadi.length()-1);
                ps.close();

                DepoUrun okunanUrun = new DepoUrun();
                okunanUrun.setBarkodNo(barcode);
                okunanUrun.setStokKodu(stok_kodu);
                okunanUrun.setUrunAdi(urunadi);

                publishProgress("STOK OKUNDU");

                return okunanUrun;
            } catch (SQLException e) {
                String hata = e.getSQLState();
                if((hata.equals("24000")) & (strings[0].contains("DEPO_"))){
                    publishProgress("Stokta Ürün Kayıtlı Değil");
                }
                else if(hata.equals("08S01")){
                    publishProgress("Cihazın Bağlantısını Kontrol Edin");
                }
                else{
                    publishProgress("Veritabanı Hatası, Uygulama Güncellenmeli");
                }

                return null;
            } catch (Exception e) {
                publishProgress("Hata Kodu: "+e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(DepoUrun s) {
            if(s != null ){
                tvStokBarkod.setText(s.getBarkodNo());
                tvStokKodu.setText(s.getStokKodu());
                tvStokAdi.setText(s.getUrunAdi());
                okunmusUrun.setBarkodNo(s.getBarkodNo());
                okunmusUrun.setStokKodu(s.getStokKodu());
                okunmusUrun.setUrunAdi(s.getUrunAdi());
            }
        }
    }
    private void ClearInputsUI(){
        tvStokBarkod.setText("");
        tvStokKodu.setText("");
        tvStokAdi.setText("");
        tvAdet.setText("");
        tvAdres.setText("");
        okunmusUrun = new DepoUrun();
    }
}