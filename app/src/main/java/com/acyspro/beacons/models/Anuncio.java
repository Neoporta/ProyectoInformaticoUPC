package com.acyspro.beacons.models;

import android.database.sqlite.SQLiteDatabase;

import com.acyspro.beacons.activities.MainActivity;
import com.acyspro.beacons.services.SQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Anuncio {

    private String id;
    private String type;
    private String title;
    private String description;
    private String image_full_name;
    private String image_full_url;
    private String image_pre_name;
    private String image_pre_url;
    private String video_url;
    private String link_url;
    private String created_at;

    public Anuncio() {
    }

    public Anuncio(String id,
                   String type,
                   String title,
                   String description,
                   String image_full_name,
                   String image_pre_name,
                   String image_full_url,
                   String image_pre_url,
                   String video_url,
                   String link_url,
                   String created_at) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.image_full_name = image_full_name;
        this.image_pre_name = image_pre_name;
        this.image_full_url = image_full_url;
        this.image_pre_url = image_pre_url;
        this.video_url = video_url;
        this.link_url = link_url;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_full_name() {
        return image_full_name;
    }

    public void setImage_full_name(String image_full_name) {
        this.image_full_name = image_full_name;
    }

    public String getImage_pre_name() {
        return image_pre_name;
    }

    public void setImage_pre_name(String image_pre_name) {
        this.image_pre_name = image_pre_name;
    }

    public String getImage_full_url() {
        return image_full_url;
    }

    public void setImage_full_url(String image_full_url) {
        this.image_full_url = image_full_url;
    }

    public String getImage_pre_url() {
        return image_pre_url;
    }

    public void setImage_pre_url(String image_pre_url) {
        this.image_pre_url = image_pre_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static Anuncio build(JSONObject jsonTraining) {

        Anuncio anuncio = new Anuncio();
        try {
            anuncio.setId(jsonTraining.getString("id"));
            anuncio.setType(jsonTraining.getString("type"));
            anuncio.setTitle(jsonTraining.getString("title"));
            anuncio.setDescription(jsonTraining.getString("description"));

            anuncio.setImage_full_name(jsonTraining.getString("image_full_name"));
            anuncio.setImage_pre_name(jsonTraining.getString("image_pre_name"));
            anuncio.setImage_full_url(jsonTraining.getString("image_full_url"));
            anuncio.setImage_pre_url(jsonTraining.getString("image_pre_url"));

            anuncio.setVideo_url(jsonTraining.getString("video_url"));
            anuncio.setLink_url(jsonTraining.getString("link_url"));

            return anuncio;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Anuncio> build(JSONArray jsonTrainings) {
        int anunciosCount = jsonTrainings.length();
        List<Anuncio> anuncios = new ArrayList<>();
        for(int i = 0; i < anunciosCount; i++) {
            try {
                JSONObject jsonTraining = (JSONObject) jsonTrainings.get(i);
                Anuncio anuncio = Anuncio.build(jsonTraining);
                anuncios.add(anuncio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return anuncios;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
