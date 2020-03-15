package com.vrcorp.myblog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.myblog.db.DBHelper;
import com.vrcorp.myblog.layout.RekomendasiFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class DetailActivity extends AppCompatActivity {
    LinearLayout desKategori, desKonten;
    CardView cFav, cShare, cBack;
    ImageView bgDes, desFav;
    TextView des_judul, des_penerbit, des_waktu;
    WebView des_isi;
    ShimmerLayout sh_des;
    DBHelper helper;
    String urlnya, Nama, gambara, urlPosting, waktu, penerbit, isi;
    int success=0, favoritStatus=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        desKategori = findViewById(R.id.des_kategori);
        cFav = findViewById(R.id.des_fav);
        cShare = findViewById(R.id.des_share);
        cBack = findViewById(R.id.des_back);
        desKonten = findViewById(R.id.des_content);
        bgDes = findViewById(R.id.des_bg_art);
        des_judul = findViewById(R.id.des_art_judul);
        des_penerbit = findViewById(R.id.des_penerbit);
        des_waktu = findViewById(R.id.des_waktu);
        des_isi = findViewById(R.id.des_isi);
        sh_des = findViewById(R.id.sh_des);
        desFav = findViewById(R.id.img_des_fav);
        helper = new DBHelper(this);
        Intent intent = getIntent();
        urlnya = intent.getStringExtra("url");
        success = helper.cekFav(urlnya);
        if(success>0){
            Glide.with(desFav)
                    .load(getResources()
                            .getIdentifier("fav", "drawable", this.getPackageName()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            desFav.setImageDrawable(resource);
                        }
                    });
            favoritStatus=1;
        }
        cFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritStatus>0){
                    Glide.with(desFav)
                            .load(getResources()
                                    .getIdentifier("nofav", "drawable", getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    desFav.setImageDrawable(resource);
                                }
                            });
                    helper.deletDB(urlnya);
                    favoritStatus=0;
                }else{
                    Glide.with(desFav)
                            .load(getResources()
                                    .getIdentifier("fav", "drawable", getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    desFav.setImageDrawable(resource);
                                }
                            });
                    helper.insertIntoDB(1,Nama,gambara,urlnya,"","","1","","");
                    favoritStatus=1;
                }
            }
        });
        cBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent;
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Baca artikel "+Nama+", Download aplikasi Resep Memasak secara gratis " + "https://play.google.com/store/apps/details?id=" +getPackageName());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent,"Share with"));
            }
        });
        new CardGet().execute();
        //dialog.show();
        sh_des.startShimmerAnimation();
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = urlnya;
            Document mBlogPagination = null;
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            //Log.d(TAG, "doInBackground: "+mBlogPagination);
            System.out.println(mBlogPagination);
            // Using Elements to get the Meta data
            // -------------- RECENTT ---------------
            //----------------
            Elements mElementDataSize = mBlogPagination.select("div[class=blog-posts hfeed container] article[class=post-outer-container]");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();
            int max = 0;
            if(mElementSize>10){
                max=10;
            }else{
                max=mElementSize;
            }
            //System.out.println("jumlah data"+mElementSize);
            for (int i = 0; i < max; i++) {
                //Judul
                Elements ElemenJudul = mElementDataSize.select("h3[class=post-title entry-title]").eq(i);
                Nama= ElemenJudul.text();
                //gambar
                Elements elGambar = mElementDataSize.select("div[class=post-body entry-content float-container] div").eq(i);
                gambara = elGambar.select("img").eq(0).attr("src");
                urlPosting = ElemenJudul.select("a").eq(0).attr("href");
                Elements elWaktu = mElementDataSize.select("div[class=post-header-line-1]").eq(i);
                waktu = elWaktu.text().trim();
                Elements elIsi = mElementDataSize.select("div[class=page-generator__output js-generator-output]").eq(i);
                isi = elIsi.text().trim();
                penerbit = "MyBlog";
                Elements kategoriSize = mBlogPagination.select("div[class=blog-posts hfeed container] article[class=post-outer-container] div[class=post-bottom] a");
                System.out.println("jumlah data"+kategoriSize.size());
                for(int s=0;s<2;s++){
                    Elements div = kategoriSize.eq(s);
                    String bahan = div.text().trim();
                    final TextView label = new TextView(DetailActivity.this);
                    LinearLayout.LayoutParams lyParam =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int marginz = getResources().getDimensionPixelSize(R.dimen._5sdp);
                    lyParam.setMargins(marginz,marginz,marginz,marginz);
                    label.setLayoutParams(lyParam);
                    int margin = getResources().getDimensionPixelSize(R.dimen._9sdp);
                    label.setPadding(margin, margin, margin, margin);//left,top,right,bottom
                    label.setTextColor(getResources().getColor(R.color.purple_400));
                    label.setBackgroundColor(getResources().getColor(R.color.purple_100));
                    label.setText(bahan);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            desKategori.addView(label);
                        }
                    });

                }
                //STATUS
            }
            //---------------------------
            //--------------------------

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            Log.d("EAEA", "onPostExecute: "+result);
            //--------------RECENT -
            //----------------------
            desKonten.setVisibility(View.VISIBLE);
            des_judul.setText(Nama);
            des_waktu.setText(waktu);
            des_isi.loadDataWithBaseURL(null, isi, "text/html", "utf-8", null);
            Glide.with(bgDes)
                    .load(Uri.parse(gambara))
                    .apply(RequestOptions.centerCropTransform())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            bgDes.setImageDrawable(resource);
                        }
                    });
            System.out.println("HTML   "+isi);
            cFav.setVisibility(View.VISIBLE);
            cShare.setVisibility(View.VISIBLE);
            sh_des.stopShimmerAnimation();
            sh_des.setVisibility(View.GONE);
            //--------------------------
            //dialog.dismiss();
        }

    }
    public void onResume() {
        sh_des.startShimmerAnimation();
        super.onResume();
    }

    @Override
    public void onPause() {
        sh_des.stopShimmerAnimation();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
