package com.acyspro.beacons.services;

import com.acyspro.beacons.models.Anuncio;
import com.acyspro.beacons.models.FavoriteAd;

public class NewsService {

    private Anuncio currentAnuncio;
    private FavoriteAd currentFavoriteAd;

    public Anuncio getCurrentAnuncio() {
        return currentAnuncio;
    }

    public void setCurrentAnuncio(Anuncio currentAnuncio) {
        this.currentAnuncio = currentAnuncio;
    }


    public FavoriteAd getCurrentFavoriteAd() {
        return currentFavoriteAd;
    }

    public void setCurrentFavoriteAd(FavoriteAd currentFavoriteAd) {
        this.currentFavoriteAd = currentFavoriteAd;
    }
}
