package com.acyspro.beacons.activities.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.suitebuilder.annotation.MediumTest;

import com.acyspro.beacons.services.SQLiteHelper;

import org.junit.Before;
import org.junit.Test;

@MediumTest
public class AdFragmentTest {

    SQLiteDatabase db;
    SQLiteHelper usdbh;

    private Context instrumentationCtx;

    @Test
    public void obtenerAnunciosBD() {

        usdbh = new SQLiteHelper(instrumentationCtx, "DBAnuncios", null, 6);
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

                System.out.println("client_id: " + client_id);
                System.out.println("campaign_id: " + campaign_id);
                System.out.println("ad_id: " + ad_id);
                System.out.println("beacon_id: " + beacon_id);
                System.out.println("title: " + title);
                System.out.println("description: " + description);
                System.out.println("video_url: " + video_url);
                System.out.println("link_url: " + link_url);

            } while (cursor.moveToNext());
        }

        cursor.close();

    }

/*    @Test
    public void llamadaAnuncio() {
        System.out.println("Entre al TEST");
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

                            System.out.println("client_id: " + client_id);
                            System.out.println("campaign_id: " + campaign_id);
                            System.out.println("ad_id: " + ad_id);
                            System.out.println("beacon_id: " + beacon_id);
                            System.out.println("title: " + title);
                            System.out.println("description: " + description);
                            System.out.println("video_url: " + video_url);
                            System.out.println("link_url: " + link_url);


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
*/
}