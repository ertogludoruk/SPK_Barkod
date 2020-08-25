package com.spk.spkbarkoduygulamas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.spk.spkbarkoduygulamas.helpers.DBManager;

/* CREATED BY Doruk Ertoğlu and
 Tuğkan Söğüt 2020 */

public class MainActivity extends AppCompatActivity {
    static Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(activeFragment != null){
           //((DepoGirisFragment)activeFragment).myOnKeyDown(keyCode);
       }

        return super.onKeyDown(keyCode, event);
    }
}