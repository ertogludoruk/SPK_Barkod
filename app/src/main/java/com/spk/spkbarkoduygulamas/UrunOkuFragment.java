package com.spk.spkbarkoduygulamas;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class UrunOkuFragment extends Fragment {

    TextView tvStokBarkod;
    TextView tvStokKodu;
    TextView tvStokAdi;
    TextView tvStokCinsi;
    TextView tvStokBirimi;
    TextView tvAmbAdet;

    Context context;

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

        tvStokBarkod = view.findViewById(R.id.textViewBarkod);
        tvStokKodu = view.findViewById(R.id.textViewStokKodu);
        tvStokAdi = view.findViewById(R.id.textViewStokAdi);
        tvStokCinsi = view.findViewById(R.id.textViewCins);
        tvStokBirimi = view.findViewById(R.id.textViewBirim);
        tvAmbAdet = view.findViewById(R.id.textViewAdet);

        final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String barcode = Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0).coerceToText(context).toString();
                if(barcode.equals(" ")){return;}
                tvStokBarkod.setText(barcode);
                ReadBarcode readBarcode = new ReadBarcode();
                readBarcode.execute(barcode);
            }
        });

        return view;
    }

    public class ReadBarcode extends AsyncTask<String,String, Urun> {

        boolean isSuccess;

        @Override
        protected void onProgressUpdate(String... strings) {
            Toast.makeText(context, strings[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            isSuccess = false;
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
                        String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [MikroDB_V16_V01].[dbo].[STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);

                ps = connect.prepareStatement(queryStmt);

                rs = ps.executeQuery();
                rs.next();
                String urunadi = rs.getString("sto_isim");
                String birim = rs.getString("sto_birim1_ad");
                Integer cins = rs.getInt("sto_cins");
                Integer ambalajIciAdeti = Math.abs(rs.getInt("sto_birim2_katsayi")) ;

                ps.close();
                queryStmt =
                        String.format("SELECT [sto_isim],[sto_cins],[sto_birim1_ad],[sto_birim2_ad],[sto_birim2_katsayi] FROM [MikroDB_V16_V01].[dbo].[STOKLAR] WHERE [sto_kod]='%s'", stok_kodu);
                ps = connect.prepareStatement(queryStmt);

                rs = ps.executeQuery();
                rs.next();
                ps.close()
                ;
                Urun okunanUrun = new Urun(barcode,urunadi,cins,birim,stok_kodu,ambalajIciAdeti);
                isSuccess = true;
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
                e.printStackTrace();
                Throwable za = e.getCause();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Urun s) {

            if(isSuccess){
                Toast.makeText(context, "STOK OKUNDU", Toast.LENGTH_SHORT).show();
                tvStokBarkod.setText(s.getBarkod());
                tvStokKodu.setText(s.getStokKodu());
                tvStokAdi.setText(s.getIsim());
                tvStokCinsi.setText(s.getCins().toString() );
                tvStokBirimi.setText(s.getBirim1());
                tvAmbAdet.setText(s.getAmbalajAdeti().toString() );
            }
        }
    }
}