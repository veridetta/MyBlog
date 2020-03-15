package com.vrcorp.myblog;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.vrcorp.myblog.adapter.ArtikelAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class CariActivity extends AppCompatActivity {
    String urlnya, txtJudul;
    private ActionBar toolbar;
    TextView jdul;
    LinearLayout no_result;
    SearchView cari;
    String Nama, gambara, urlPosting, waktu, penerbit, cariVal;
    private ArrayList<String> judulList= new ArrayList<>();
    private ArrayList<String> gambarList= new ArrayList<String>();
    private ArrayList<String> penerbitList = new ArrayList<>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> kategoriList = new ArrayList<>();
    private ArrayList<Integer> favList = new ArrayList<Integer>();
    RecyclerView rc_cari;
    int data=0;
    ShimmerLayout sh_cari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        Intent intent = getIntent();
        urlnya = intent.getStringExtra("url");
        txtJudul =  intent.getStringExtra("judul");
        rc_cari = findViewById(R.id.rc_cari);
        sh_cari = findViewById(R.id.shimmer_cari);
        jdul = findViewById(R.id.cari_judul);
        cari = findViewById(R.id.cari_input);
        jdul.setText(txtJudul);
        no_result = findViewById(R.id.no_result);
        cari.setQueryHint("Masukan Kata Kunci");
        cari.onActionViewExpanded();
        cari.setIconified(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cari.clearFocus();
            }
        }, 300);
        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 cariVal = cari.getQuery().toString();
                if(cariVal.length()<3){
                    Toast.makeText(CariActivity.this,"Minimal kata minimal 3 huruf",Toast.LENGTH_LONG).show();
                }else if(cariVal.length()>10){
                    Toast.makeText(CariActivity.this,"Maksimal 10 huruf",Toast.LENGTH_LONG).show();
                }else{
                    no_result.setVisibility(View.GONE);
                    new CardGet().execute();
                    sh_cari.setVisibility(View.VISIBLE);
                    sh_cari.startShimmerAnimation();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = "https://mydemoblog19.blogspot.com/search?q="+cariVal;
            Document mBlogPagination = null;

            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            //Log.d(TAG, "doInBackground: "+mBlogPagination);
            //System.out.println(mBlogPagination);
            // Using Elements to get the Meta data
            // -------------- RECENTT ---------------
            //----------------
            Elements mElementDataSize = mBlogPagination.select("div[class=post] article[class=post-outer-container]");
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
                Elements elGambar = mElementDataSize.select("div[class=container post-body entry-content] div[class=snippet-thumbnail]").eq(i);
                gambara = elGambar.select("img").eq(0).attr("src");
                urlPosting = ElemenJudul.select("a").eq(0).attr("href");
                Elements elWaktu = mElementDataSize.select("div[class=post-header]").eq(i);
                waktu = elWaktu.text().trim();
                penerbit = "MyBlog";
                //STATUS
                judulList.add(Nama);
                urlList.add(urlPosting);
                penerbitList.add(penerbit);
                gambarList.add(gambara);
                waktuList.add(waktu);
                kategoriList.add("");
                favList.add(1);
            }
            //---------------------------
            //--------------------------
            //--------------------------
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            //Log.d(TAG, "onPostExecute: "+result);
            //--------------RECENT -
            //--------------------
            System.out.println(gambarList);
            ArtikelAdapter mDataAdapter = new ArtikelAdapter( CariActivity.this, judulList, kategoriList,
                    gambarList, urlList,penerbitList,waktuList,favList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
            //attachToRecyclerView

            if(judulList.size()>0){
                //Use this now
                rc_cari.setLayoutManager(mLayoutManager);
                rc_cari.setAdapter(mDataAdapter);
                //rc_art.setAdapter(mDataAdapter);
                no_result.setVisibility(View.GONE);
                sh_cari.stopShimmerAnimation();
                sh_cari.setVisibility(View.GONE);
            }else{
                jdul.setVisibility(View.GONE);
                sh_cari.stopShimmerAnimation();
                sh_cari.setVisibility(View.GONE);
                rc_cari.setVisibility(View.GONE);
                no_result.setVisibility(View.VISIBLE);
            }
            //--------------------------
            //-------------------------
            //dialog.dismiss();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        sh_cari.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        sh_cari.stopShimmerAnimation();
        super.onPause();
    }
}
