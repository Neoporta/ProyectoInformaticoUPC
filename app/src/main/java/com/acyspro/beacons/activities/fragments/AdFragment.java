package com.acyspro.beacons.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acyspro.beacons.R;
import com.acyspro.beacons.adapters.AnuncioAdapter;
import com.acyspro.beacons.models.Anuncio;
import com.acyspro.beacons.services.SQLiteHelper;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class AdFragment extends Fragment {

    RecyclerView anuncioRecyclerView;
    GridLayoutManager anuncioLayoutManager;
    AnuncioAdapter anuncioAdapter;
    List<Anuncio> anuncios = new ArrayList<>();
    Anuncio anuncio;
    SQLiteDatabase db;
    SQLiteHelper usdbh;

    private ProximityObserver proximityObserver;

    public AdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ad, container, false);

        usdbh = new SQLiteHelper(getContext(), "DBAnuncios", null, 6);
        db = usdbh.getWritableDatabase();


        String sql = "DELETE FROM anuncios";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.execute();
        stmt.close();


        anuncioRecyclerView = view.findViewById(R.id.recycler_view);
        anuncioAdapter = new AnuncioAdapter();
        anuncioLayoutManager = new GridLayoutManager(getContext(), 1);

        anuncios = obtenerAnunciosBD();
        anuncioAdapter.setAnuncios(anuncios);
        anuncioRecyclerView.setAdapter(anuncioAdapter);
        anuncioRecyclerView.setLayoutManager(anuncioLayoutManager);

        EstimoteCloudCredentials cloudCredentials =
                new EstimoteCloudCredentials(getResources().getString(R.string.appId),  getResources().getString(R.string.appToken));

        final ProximityObserver proximityObserver = new ProximityObserverBuilder(getContext(), cloudCredentials)
                .withEstimoteSecureMonitoringDisabled()
                .withTelemetryReportingDisabled()
                .onError(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Toast.makeText(getContext(), "proximity observer error: " + throwable, Toast.LENGTH_SHORT).show();
                        return null;
                    }
                })
                .build();

        final ProximityZone zone1 =
                new ProximityZoneBuilder()
                        .forTag("floor")
                        .inCustomRange(3.5)
                        //.inNearRange()
                        .onEnter(new Function1<ProximityZoneContext, Unit>() {
                            @Override public Unit invoke(ProximityZoneContext proximityContext) {
                                /* Do something here */
                                return null;
                            }
                        })
                        .onExit(new Function1<ProximityZoneContext, Unit>() {
                            @Override
                            public Unit invoke(ProximityZoneContext proximityContext) {
                                /* Do something here */
                                return null;
                            }
                        })
                        .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                            @Override
                            public Unit invoke(Set<? extends ProximityZoneContext> proximityZoneContexts) {
                                for (ProximityZoneContext context : proximityZoneContexts) {
                                    //deskOwners.add(context.getAttachment().get("desk-owner"));
                                    updateAnuncios(context.getDeviceId());
                                }
                                return null;
                            }

                        })
                        .build();

        ProximityObserver.Handler observationHandler = proximityObserver.startObserving(zone1);

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(getActivity(),
                        // onRequirementsFulfilled
                        new Function0<Unit>() {
                            @Override public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityObserver.startObserving(zone1);
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });

        return view;
    }

    private void updateAnuncios(String idBeacon) {
        AndroidNetworking.get("http://157.230.11.57:8080/api/getad/" + idBeacon)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Calendar c = Calendar.getInstance();
                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            dateFormat1.setTimeZone(c.getTimeZone());
                            String fechaHoy = dateFormat1.format(c.getTime());

                            String sql = "INSERT INTO anuncios (id, title, description," +
                                    " image_full_name, image_pre_name, image_full_url, image_pre_url," +
                                    " video_url, link_url, created_at, content, favorite," +
                                    " client_id, campaign_id, ad_id, beacon_id, user_id)" +
                                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                            SQLiteStatement stmt = db.compileStatement(sql);

                            String client_id = response.getJSONObject("data").getString("client_id");
                            String campaign_id = response.getJSONObject("data").getJSONObject("ad").getString("campaign_id");
                            String ad_id = response.getJSONObject("data").getJSONObject("ad").getString("id");
                            String beacon_id = response.getJSONObject("data").getString("beacon_id");
                            String action = "VISTO";
                            String user_id = "";

                            stmt.bindString(1, response.getJSONObject("data").getJSONObject("ad").getString("id"));
                            stmt.bindString(2, response.getJSONObject("data").getJSONObject("ad").getString("title"));
                            stmt.bindString(3, response.getJSONObject("data").getJSONObject("ad").getString("description"));
                            stmt.bindString(4, response.getJSONObject("data").getJSONObject("ad").getString("image_full_name"));
                            stmt.bindString(5, response.getJSONObject("data").getJSONObject("ad").getString("image_pre_name"));
                            stmt.bindString(6, response.getJSONObject("data").getJSONObject("ad").getString("image_full_url"));
                            stmt.bindString(7, response.getJSONObject("data").getJSONObject("ad").getString("image_pre_url"));
                            stmt.bindString(8, response.getJSONObject("data").getJSONObject("ad").getString("video_url"));
                            stmt.bindString(9, response.getJSONObject("data").getJSONObject("ad").getString("link_url"));
                            stmt.bindString(10, fechaHoy);
                            stmt.bindString(11, response.getJSONObject("data").getJSONObject("ad").getString("body"));
                            stmt.bindString(12, "0");
                            stmt.bindString(13, client_id);
                            stmt.bindString(14, campaign_id);
                            stmt.bindString(15, ad_id);
                            stmt.bindString(16, beacon_id);
                            stmt.bindString(17, "");
                            stmt.execute();
                            stmt.close();

                            sendStatics(client_id, campaign_id, ad_id, action, beacon_id, user_id);

                            anuncios = obtenerAnunciosBD();
                            anuncioAdapter.setAnuncios(anuncios);
                            anuncioAdapter.notifyDataSetChanged();
                        } catch (JSONException | SQLiteException e) {
                            System.out.println();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public List<Anuncio> obtenerAnunciosBD() {
        List<Anuncio> anuncios = new ArrayList<>();
        try {

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

                    anuncios.add(new Anuncio(cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                            cursor.getString(10),
                            "0",
                            cursor.getString(11),
                            cursor.getString(12),
                            cursor.getString(13),
                            cursor.getString(14),
                            null));

                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return anuncios;
    }

    private void sendStatics(String client_id,
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

}
