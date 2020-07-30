package com.spk.spkbarkoduygulamas;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.helpers.Urun;

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
    TextView tvStokCinsi;
    TextView tvStokBirimi;
    TextView tvAmbAdet;
    EditText editText_girilen_miktar;
    TextView textView_adres;
    ImageView ivTemizle;
    ImageView ivOnayla;
    TextView tvEvet;
    TextView tvHayir;
    LinearLayout popup;
    Context context;
    View layoutdummy;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_depo_giris, container, false);

        tvStokBarkod = view.findViewById(R.id.textViewBarkod);
        tvStokKodu = view.findViewById(R.id.textViewStokKodu);
        tvStokAdi = view.findViewById(R.id.textViewStokAdi);
        tvStokCinsi = view.findViewById(R.id.textViewCins);
        tvStokBirimi = view.findViewById(R.id.textViewBirim);
        tvAmbAdet = view.findViewById(R.id.textViewAdet);
        ivTemizle = view.findViewById(R.id.imageViewTemizle);
        ivOnayla = view.findViewById(R.id.imageViewOnayla);
        popup = view.findViewById(R.id.popUpWindow);
        tvEvet = view.findViewById(R.id.textViewEvet);
        tvHayir = view.findViewById(R.id.textViewHayir);
        editText_girilen_miktar = view.findViewById(R.id.editTextGirelenAdet);
        textView_adres = view.findViewById(R.id.textViewAdres);
        layoutdummy = view.findViewById(R.id.layoutdepogiris);

        editText_girilen_miktar.requestFocus();

        ivTemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearInputsUI();
            }
        });

        ivOnayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvStokKodu.getText().equals("") & !editText_girilen_miktar.getText().toString().equals("") & !textView_adres.getText().equals("")){
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
                            Toast.makeText(context, "STOK EKLENDİ", Toast.LENGTH_SHORT).show();
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
                editText_girilen_miktar.setEnabled(false);
                if(barcode.contains("DEPO_")){
                    String depoAdi = barcode.substring(5);
                    textView_adres.setText(depoAdi);
                }
                else{
                    DepoGirisFragment.ReadBarcode1 readBarcode = new DepoGirisFragment.ReadBarcode1();
                    readBarcode.execute(barcode);
                }


            }
        });
        return view;
    }
    private class ReadBarcode1 extends AsyncTask<String,String, Urun>{
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Urun doInBackground(String... strings) {

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
                        String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                ps = connect.prepareStatement(queryStmt);

                rs = ps.executeQuery();
                rs.next();
                String urunadi = rs.getString("sto_isim");
                String birim = rs.getString("sto_birim1_ad");
                Integer cins = rs.getInt("sto_cins");
                Integer ambalajIciAdeti = Math.abs(rs.getInt("sto_birim2_katsayi")) ;

                ps.close();
                queryStmt =
                        String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                ps = connect.prepareStatement(queryStmt);

                rs = ps.executeQuery();
                rs.next();
                ps.close();
                Urun okunanUrun = new Urun(barcode,urunadi,cins,birim,stok_kodu,ambalajIciAdeti);
                publishProgress("STOK OKUNDU");

                return okunanUrun;
            } catch (SQLException e) {
                String hata = e.getSQLState();
                if(hata.equals("24000")){
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
        protected void onPostExecute(Urun s) {
            if(s != null ){
                tvStokBarkod.setText(s.getBarkod());
                tvStokKodu.setText(s.getStokKodu());
                tvStokAdi.setText(s.getIsim());
                tvStokCinsi.setText(s.getCins().toString() );
                tvStokBirimi.setText(s.getBirim1());
                tvAmbAdet.setText(s.getAmbalajAdeti().toString() );
            }
            editText_girilen_miktar.requestFocus();
            editText_girilen_miktar.setEnabled(true);
        }
    }
    private void ClearInputsUI(){
        tvStokBarkod.setText("");
        tvStokKodu.setText("");
        tvStokAdi.setText("");
        tvStokCinsi.setText("");
        tvStokBirimi.setText("");
        tvAmbAdet.setText("");
        editText_girilen_miktar.setText("");
        textView_adres.setText("");
    }
}