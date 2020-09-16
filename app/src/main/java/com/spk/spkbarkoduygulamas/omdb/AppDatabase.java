package com.spk.spkbarkoduygulamas.omdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Stok.class,DepoYeri.class,Hesap.class,DepoHaraketi.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}