package com.acyspro.beacons.activities.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acyspro.beacons.R;
import com.acyspro.beacons.adapters.AnuncioAdapter;
import com.acyspro.beacons.adapters.FavoriteAdAdapter;
import com.acyspro.beacons.models.Anuncio;
import com.acyspro.beacons.models.FavoriteAd;
import com.acyspro.beacons.services.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdFragment extends Fragment {

    RecyclerView anuncioRecyclerView;
    GridLayoutManager anuncioLayoutManager;
    FavoriteAdAdapter anuncioAdapter;
    List<FavoriteAd> favoriteAds = new ArrayList<>();
    FavoriteAd favoriteAd;
    SQLiteDatabase db;
    SQLiteHelper usdbh;

    public FavoriteAdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_ad, container, false);

        usdbh = new SQLiteHelper(getContext(), "DBAnuncios", null, 5);
        db = usdbh.getWritableDatabase();

        anuncioRecyclerView = view.findViewById(R.id.recycler_view_favorite);
        anuncioAdapter = new FavoriteAdAdapter();
        anuncioLayoutManager = new GridLayoutManager(getContext(), 1);

        favoriteAds = obtenerAnunciosBD();
        anuncioAdapter.setfavoriteAds(favoriteAds);
        anuncioRecyclerView.setAdapter(anuncioAdapter);
        anuncioRecyclerView.setLayoutManager(anuncioLayoutManager);

        anuncioAdapter.notifyDataSetChanged();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        favoriteAds = obtenerAnunciosBD();
        anuncioAdapter.setfavoriteAds(favoriteAds);

        anuncioAdapter.notifyDataSetChanged();
    }

    public List<FavoriteAd> obtenerAnunciosBD() {
        List<FavoriteAd> favoriteAds = new ArrayList<>();
        try {

            String sql = "SELECT id, title, description," +
                    "            image_full_name, image_pre_name, image_full_url, image_pre_url," +
                    "            video_url, link_url, created_at, content" +
                    "  FROM [anuncios]" +
                    "  WHERE 1 = 1" +
                    "  AND favorite = '1'" +
                    "  ORDER BY created_at desc";

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {

                    favoriteAds.add(new FavoriteAd(cursor.getString(0),
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
                            "1"));

                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return favoriteAds;
    }


}
