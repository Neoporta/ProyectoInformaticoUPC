package com.acyspro.beacons.services;

import com.acyspro.beacons.models.Anuncio;

public class NewsService {

    private Anuncio currentAnuncio;

    public Anuncio getCurrentAnuncio() {
        return currentAnuncio;
    }

    public void setCurrentAnuncio(Anuncio currentAnuncio) {
        this.currentAnuncio = currentAnuncio;
    }
}
