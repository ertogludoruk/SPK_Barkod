package com.spk.spkbarkoduygulamas.helpers;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {

    @SuppressLint("NewApi")
    public static Connection CONN_MSSql_DB(String _DB,String _user,String _pass,String _server) throws SQLException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String ConnURL = "jdbc:jtds:sqlserver://" + _server + ";databaseName=" + _DB + ";user=" + _user + ";password=" + _pass + ";";
        return DriverManager.getConnection(ConnURL);

    }

}
