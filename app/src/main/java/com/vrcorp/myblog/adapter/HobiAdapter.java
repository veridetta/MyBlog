package com.vrcorp.myblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.myblog.DetailActivity;
import com.vrcorp.myblog.R;
import com.vrcorp.myblog.db.DBHelper;

import java.util.ArrayList;

public class HobiAdapter extends RecyclerView.Adapter<HobiAdapter.MyViewHolder> {
    private ArrayList<String> judulList = new ArrayList<>();
    private ArrayList<String> kategoriList = new ArrayList<>();
    private ArrayList<String> photoList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> penerbitList = new ArrayList<>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<Integer> favList = new ArrayList<>();
    private Context context;
    DBHelper helper;
    int success=0, favoritStatus=0;


    public HobiAdapter(Context context, ArrayList<String> judulList,
                          ArrayList<String> kategoriList,
                          ArrayList<String> photoList,
                          ArrayList<String> urlList,
                          ArrayList<String> penerbitList,
                          ArrayList<String> waktuList,
                          ArrayList<Integer> favList) {
        this.context = context;
        this.judulList = judulList;
        this.kategoriList = kategoriList;
        this.photoList = photoList;
        this.urlList = urlList;
        this.penerbitList = penerbitList;
        this.waktuList = waktuList;
        this.favList=favList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tJudul, tPenerbit, tWaktu;
        int tFav;
        ImageView gArtikel, gFav;
        CardView cArtikel;
        public MyViewHolder(View view) {
            super(view);
            tJudul = view.findViewById(R.id.art_judul);
            tPenerbit= view.findViewById(R.id.art_penerbit);
            tWaktu= view.findViewById(R.id.art_tanggal);
            gArtikel= view.findViewById(R.id.bg_artikel);
            cArtikel = view.findViewById(R.id.card_artikel);
            gFav = view.findViewById(R.id.art_fav);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artikel_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tJudul.setText(judulList.get(position));
        holder.tPenerbit.setText(penerbitList.get(position));
        holder.tWaktu.setText(waktuList.get(position));
        Glide.with(holder.gArtikel.getContext())
                .load(Uri.parse(photoList.get(position)))
                .apply(RequestOptions.centerCropTransform())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.gArtikel.setImageDrawable(resource);
                    }
                });
        holder.cArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("url",urlList.get(position));
                context.startActivity(intent);
            }
        });
        helper = new DBHelper(context);
        success = helper.cekFav(urlList.get(position));
        if(success>0){
            Glide.with(holder.gFav)
                    .load(context.getResources()
                            .getIdentifier("fav", "drawable", context.getPackageName()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.gFav.setImageDrawable(resource);
                        }
                    });
            favoritStatus=1;
        }
        holder.gFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritStatus>0){
                    Glide.with(holder.gFav)
                            .load(context.getResources()
                                    .getIdentifier("nofav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.gFav.setImageDrawable(resource);
                                }
                            });
                    favoritStatus=0;
                }else{
                    Glide.with(holder.gFav)
                            .load(context.getResources()
                                    .getIdentifier("fav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.gFav.setImageDrawable(resource);
                                }
                            });
                    helper.insertIntoDB(1,judulList.get(position),photoList.get(position),urlList.get(position),"","","1",waktuList.get(position),penerbitList.get(position));
                    favoritStatus=1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return judulList.size();
    }
}

