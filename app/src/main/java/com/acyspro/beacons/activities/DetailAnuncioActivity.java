package com.acyspro.beacons.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acyspro.beacons.NewsApp;
import com.acyspro.beacons.R;
import com.acyspro.beacons.models.Anuncio;
import com.androidnetworking.widget.ANImageView;

public class DetailAnuncioActivity extends AppCompatActivity {

    Anuncio anuncio;
    ANImageView imageANImageView;
    TextView lblTituloAnuncio;
    TextView lblSubTituloAnuncio;
    Button btnEnlaceLink;
    Button btnEnlaceVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anuncio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageANImageView = findViewById(R.id.imageANImageView);
        lblTituloAnuncio = findViewById(R.id.lblTituloAnuncio);
        lblSubTituloAnuncio = findViewById(R.id.lblSubTituloAnuncio);

        btnEnlaceLink = findViewById(R.id.btnEnlaceLink);
        btnEnlaceVideo = findViewById(R.id.btnEnlaceVideo);

        anuncio = NewsApp.getInstance().getService().getCurrentAnuncio();

        if (!anuncio.getLink_url().equals("null")) {
            btnEnlaceLink.setVisibility(View.VISIBLE);
        }

        if (!anuncio.getVideo_url().equals("null")) {
            btnEnlaceVideo.setVisibility(View.VISIBLE);
        }

        setTitle(anuncio.getTitle());

        imageANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        imageANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        imageANImageView.setImageUrl(anuncio.getImage_full_url());
        lblTituloAnuncio.setText(anuncio.getTitle());
        lblSubTituloAnuncio.setText(anuncio.getDescription());

        btnEnlaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(anuncio.getLink_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnEnlaceVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(anuncio.getVideo_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
