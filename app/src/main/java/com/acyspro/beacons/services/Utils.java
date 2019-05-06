package com.acyspro.beacons.services;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.acyspro.beacons.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static void sendStatics(String client_id,
                             String campaign_id,
                             String ad_id,
                             String action,
                             String beacon_id,
                             String user_id) {

        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(c.getTimeZone());
        String fechaHoy = dateFormat.format(c.getTime());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client_id", client_id);
            jsonObject.put("campaign_id", campaign_id);
            jsonObject.put("ad_id", ad_id);
            jsonObject.put("action", action);
            jsonObject.put("fecha_hora", fechaHoy);
            jsonObject.put("beacon_id", beacon_id);
            jsonObject.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://157.230.11.57:8080/api/statistics")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    public static void markStatistics(String ad_id, String status, Context context) {

        SQLiteDatabase db;
        SQLiteHelper usdbh;

        usdbh = new SQLiteHelper(context, "DBAnuncios", null, 6);
        db = usdbh.getWritableDatabase();

        String sql = "UPDATE anuncios SET favorite = '"+ status +"' WHERE id = " + ad_id;
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.execute();
        stmt.close();

    }

    public static void updateStatus(Context context, String id, String rating) {

        Resources res = context.getResources();

        SQLiteHelper usdbh = new SQLiteHelper(context, "DBAnuncios", null, 6);
        SQLiteDatabase db = usdbh.getWritableDatabase();

        String sql = "UPDATE anuncios SET atributo1 = ?" +
                " WHERE id = ?";

        SQLiteStatement stmt = db.compileStatement(sql);

        stmt.bindString(1, rating);
        stmt.bindString(2, id);

        stmt.execute();
        stmt.close();
        db.close();
    }

}
