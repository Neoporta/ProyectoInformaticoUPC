package com.acyspro.beacons.adapters;

import android.content.Intent;
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
import com.acyspro.beacons.models.FavoriteAd;
import com.androidnetworking.widget.ANImageView;

import java.util.List;

public class FavoriteAdAdapter extends RecyclerView.Adapter<FavoriteAdAdapter.ViewHolder> {
    private List<FavoriteAd> favoriteAds;

    public void setfavoriteAds(List<FavoriteAd> favoriteAds) { this.favoriteAds = favoriteAds; }

    @NonNull
    @Override
    public FavoriteAdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_anuncio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdAdapter.ViewHolder holder, final int position) {

        holder.logoAnuncioANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.logoAnuncioANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.logoAnuncioANImageView.setImageUrl(favoriteAds.get(position).getImage_pre_url());
        holder.titleTextView.setText(favoriteAds.get(position).getTitle());
        holder.subTitleTextView.setText(favoriteAds.get(position).getDescription());

        holder.anuncioCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsApp.getInstance().getService().setCurrentFavoriteAd(favoriteAds.get(position));
                Intent intent = new Intent(v.getContext(), DetailAnuncioActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favoriteAds.size();
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
