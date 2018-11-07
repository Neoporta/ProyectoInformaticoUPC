package com.acyspro.beacons.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acyspro.beacons.NewsApp;
import com.acyspro.beacons.R;
import com.acyspro.beacons.activities.DetailAnuncioActivity;
import com.acyspro.beacons.models.Anuncio;
import com.acyspro.beacons.services.SQLiteHelper;
import com.androidnetworking.widget.ANImageView;

import java.util.List;

import static java.security.AccessController.getContext;

public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.ViewHolder> {
    private List<Anuncio> anuncios;

    public void setAnuncios(List<Anuncio> anuncios) { this.anuncios = anuncios; }

    @NonNull
    @Override
    public AnuncioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_anuncio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioAdapter.ViewHolder holder, final int position) {

        holder.logoAnuncioANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.logoAnuncioANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.logoAnuncioANImageView.setImageUrl(anuncios.get(position).getImage_pre_url());
        holder.titleTextView.setText(anuncios.get(position).getTitle());
        holder.subTitleTextView.setText(anuncios.get(position).getDescription().substring(0,100) + "...");

        holder.anuncioCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsApp.getInstance().getService().setCurrentAnuncio(anuncios.get(position));
                Intent intent = new Intent(v.getContext(), DetailAnuncioActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView anuncioCardView;
        ANImageView logoAnuncioANImageView;
        TextView titleTextView;
        TextView subTitleTextView;
        ViewHolder(View itemView) {
            super(itemView);
            anuncioCardView = itemView.findViewById(R.id.anuncioCardView);
            logoAnuncioANImageView = itemView.findViewById(R.id.logoAnuncioANImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            subTitleTextView = itemView.findViewById(R.id.subTitleTextView);

        }
    }
}
