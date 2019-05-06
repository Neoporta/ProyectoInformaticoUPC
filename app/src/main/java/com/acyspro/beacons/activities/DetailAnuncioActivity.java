package com.acyspro.beacons.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acyspro.beacons.NewsApp;
import com.acyspro.beacons.R;
import com.acyspro.beacons.models.Anuncio;
import com.acyspro.beacons.services.Utils;
import com.androidnetworking.widget.ANImageView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static com.acyspro.beacons.services.Utils.sendStatics;
import static com.acyspro.beacons.services.Utils.updateStatus;


public class DetailAnuncioActivity extends AppCompatActivity {

    Anuncio anuncio;
    ANImageView imageANImageView;
    TextView lblTituloAnuncio;
    TextView lblSubTituloAnuncio;
    Button btnEnlaceLink;
    Button btnEnlaceVideo;
    Menu menu;
    ShareButton shareButton;
    RatingBar adRatingBar;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anuncio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imageANImageView = findViewById(R.id.imageANImageView);
        lblTituloAnuncio = findViewById(R.id.lblTituloAnuncio);
        lblSubTituloAnuncio = findViewById(R.id.lblSubTituloAnuncio);
        adRatingBar = findViewById(R.id.rating_bar_ads);

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
        //lblSubTituloAnuncio.setText(anuncio.getDescription());
        lblSubTituloAnuncio.setText(Html.fromHtml(anuncio.getContent()));

        Float valorRating = Float.parseFloat("0");;

        if (anuncio.getAtributo1() != null) {
            valorRating = Float.parseFloat(anuncio.getAtributo1().toString());
        } else {

        }

        adRatingBar.setRating(valorRating);

        adRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String valorRating = Float.toString(adRatingBar.getRating());
                updateStatus(getApplicationContext(), anuncio.getId().toString(), valorRating);
                anuncio.setAtributo1(valorRating);
            }
        });

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

        //Envio para ver estadisticas

        String client_id = anuncio.getClient_id();
        String campaign_id = anuncio.getCampaign_id();
        String ad_id = anuncio.getAd_id();
        String beacon_id = anuncio.getBeacon_id();
        String action = "NOTIFICADO";
        String user_id = "";

        if (!(anuncio.getAtributo2().toString().equals("NOTIFICADO"))) {
            sendStatics(client_id, campaign_id, ad_id, action, beacon_id, user_id);
            anuncio.setAtributo2("NOTIFICADO");
        }

        //Facebook Link

        shareButton = findViewById(R.id.fb_share_button);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bitmap image = null;
            try {
                URL url = new URL(anuncio.getImage_full_url());
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(image)
                .build();

        ShareHashtag shareHashtag = new ShareHashtag.Builder()
                .setHashtag("#Digicupon")
                .build();

        SharePhotoContent photoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .setShareHashtag(shareHashtag)
                .build();

        shareButton.setShareContent(photoContent);

        TextView shareFB = findViewById(R.id.fb_share_button_custom);

        if(AccessToken.getCurrentAccessToken()!=null) {
            shareFB.setVisibility(View.VISIBLE);
        } else {
            shareFB.setVisibility(View.GONE);
        }
        shareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareButton.performClick();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;

            case R.id.action_favorite:
                //shareOnWall();
                //getImage();
                if (anuncio.getFavorite().equals("0")) {
                    anuncio.setFavorite("1");
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_24dp));
                    Utils.markStatistics(anuncio.getAd_id(), "1", getApplicationContext());
                } else {
                    anuncio.setFavorite("0");
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24dp));
                    Utils.markStatistics(anuncio.getAd_id(), "0", getApplicationContext());
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ad_detail, menu);

        if (anuncio.getFavorite().equals("0")) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24dp));
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_24dp));
        }

        return super.onCreateOptionsMenu(menu);
    }

    void shareOnWall(Bitmap bm) {
        ShareDialog shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new

                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        //Log.d(TAG, "onSuccess: ");
                        Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        //Log.d(TAG, "onCancel: ");
                        Toast.makeText(getApplicationContext(), "onCancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //Log.d(TAG, "onError: ");
                        Toast.makeText(getApplicationContext(), "onError" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        if (ShareDialog.canShow(ShareLinkContent.class)) {

            /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);*/

            /*Bitmap image = null;
            try {
                URL url = new URL(anuncio.getImage_full_url());
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }*/

            ShareHashtag shareHashtag = new ShareHashtag.Builder()
                                            .setHashtag("#Digicupon")
                                            .build();

            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bm)
                    .build();

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Y esto es??")
                    .setImageUrl(Uri.parse(anuncio.getImage_full_url()))
                    .setQuote(anuncio.getTitle())
                    .setContentUrl(Uri.parse(anuncio.getImage_full_url()))
                    .setShareHashtag(shareHashtag)
                    .build();

            /*SharePhotoContent photoContent = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .setShareHashtag(shareHashtag)
                    .build();*/

            /*ShareContent shareContent = new ShareMediaContent.Builder()
                    .addMedium(sharePhoto)
                    .setShareHashtag(shareHashtag)
                    .build();*/

            shareDialog.show(linkContent);
            //ShareApi.share(linkContent, null);
        }
    }

    private void getImage(){
        new DownloadImgTask().execute(anuncio.getImage_full_url());
    }

    private class DownloadImgTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bm = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bm = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bm;
        }

        protected void onPostExecute(Bitmap result) {
            //postFB(result);
            //shareOnWall(result);
            //shareButton.performClick();
        }
    }

}
