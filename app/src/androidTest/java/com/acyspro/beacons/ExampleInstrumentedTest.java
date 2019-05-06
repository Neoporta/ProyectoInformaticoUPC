package com.acyspro.beacons;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.acyspro.beacons.services.SQLiteHelper;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    SQLiteDatabase db;
    SQLiteHelper usdbh;

    @Test
    public void obtenerAnunciosBD() {
        usdbh = new SQLiteHelper(InstrumentationRegistry.getTargetContext(), "DBAnuncios", null, 6);
        db = usdbh.getWritableDatabase();

        String sql = "SELECT id, title, description," +
                "            image_full_name, image_pre_name, image_full_url, image_pre_url," +
                "            video_url, link_url, created_at, content, client_id, campaign_id," +
                "            ad_id, beacon_id" +
                "  FROM [anuncios]" +
                "  WHERE 1 = 1" +
                "  AND favorite = '0'" +
                "  ORDER BY created_at desc";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                String client_id = cursor.getString(11);
                String campaign_id = cursor.getString(12);
                String ad_id = cursor.getString(0);
                String beacon_id = cursor.getString(14);;
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String video_url = cursor.getString(7);
                String link_url = cursor.getString(8);;

                Log.d("obtenerAnunciosBD", "client_id: " + client_id);
                Log.d("obtenerAnunciosBD", "campaign_id: " + campaign_id);
                Log.d("obtenerAnunciosBD", "ad_id: " + ad_id);
                Log.d("obtenerAnunciosBD", "beacon_id: " + beacon_id);
                Log.d("obtenerAnunciosBD", "title: " + title);
                Log.d("obtenerAnunciosBD", "description: " + description);
                Log.d("obtenerAnunciosBD", "video_url: " + video_url);
                Log.d("obtenerAnunciosBD", "link_url: " + link_url);

            } while (cursor.moveToNext());
        }

        cursor.close();

    }

    @Test
    public void llamadaAnuncio() {
        String idBeacon = "8115ab3f8f4ff322f9ece172bd73ce28";
        AndroidNetworking.get("http://157.230.11.57:8080/api/getad/" + idBeacon)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String client_id = response.getJSONObject("data").getString("client_id");
                            String campaign_id = response.getJSONObject("data").getJSONObject("ad").getString("campaign_id");
                            String ad_id = response.getJSONObject("data").getJSONObject("ad").getString("id");
                            String beacon_id = response.getJSONObject("data").getString("beacon_id");
                            String title = response.getJSONObject("data").getJSONObject("ad").getString("title");
                            String description = response.getJSONObject("data").getJSONObject("ad").getString("description");
                            String video_url = response.getJSONObject("data").getJSONObject("ad").getString("video_url");
                            String link_url = response.getJSONObject("data").getJSONObject("ad").getString("link_url");

                            Log.d("llamadaAnuncioTest", "client_id: " + client_id);
                            Log.d("llamadaAnuncioTest", "campaign_id: " + campaign_id);
                            Log.d("llamadaAnuncioTest", "ad_id: " + ad_id);
                            Log.d("llamadaAnuncioTest", "beacon_id: " + beacon_id);
                            Log.d("llamadaAnuncioTest", "title: " + title);
                            Log.d("llamadaAnuncioTest", "description: " + description);
                            Log.d("llamadaAnuncioTest", "video_url: " + video_url);
                            Log.d("llamadaAnuncioTest", "link_url: " + link_url);


                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("Ocurrió un error ...");
                        System.out.println(anError.getErrorDetail());
                    }

                });
    }


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.acyspro.beacons", appContext.getPackageName());
    }

    @Test
    public void obtenerAnunciosFavoritosBD() {
        usdbh = new SQLiteHelper(InstrumentationRegistry.getTargetContext(), "DBAnuncios", null, 6);
        db = usdbh.getWritableDatabase();

        String sql = "SELECT id, title, description," +
                "            image_full_name, image_pre_name, image_full_url, image_pre_url," +
                "            video_url, link_url, created_at, content, client_id, campaign_id," +
                "            ad_id, beacon_id" +
                "  FROM [anuncios]" +
                "  WHERE 1 = 1" +
                "  AND favorite = '1'" +
                "  ORDER BY created_at desc";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                String client_id = cursor.getString(11);
                String campaign_id = cursor.getString(12);
                String ad_id = cursor.getString(0);
                String beacon_id = cursor.getString(14);;
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String video_url = cursor.getString(7);
                String link_url = cursor.getString(8);;

                Log.d("obtenerAnunciosFavoritosBD", "client_id: " + client_id);
                Log.d("obtenerAnunciosFavoritosBD", "campaign_id: " + campaign_id);
                Log.d("obtenerAnunciosFavoritosBD", "ad_id: " + ad_id);
                Log.d("obtenerAnunciosFavoritosBD", "beacon_id: " + beacon_id);
                Log.d("obtenerAnunciosFavoritosBD", "title: " + title);
                Log.d("obtenerAnunciosFavoritosBD", "description: " + description);
                Log.d("obtenerAnunciosFavoritosBD", "video_url: " + video_url);
                Log.d("obtenerAnunciosFavoritosBD", "link_url: " + link_url);

            } while (cursor.moveToNext());
        }

        cursor.close();

    }


}
