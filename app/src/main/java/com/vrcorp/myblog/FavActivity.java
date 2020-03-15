package com.vrcorp.myblog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vrcorp.myblog.adapter.ArtikelAdapter;
import com.vrcorp.myblog.db.DBHelper;
import com.vrcorp.myblog.db.DBModel;

import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class FavActivity extends AppCompatActivity {
    String Nama, gambara, urlPosting, waktu, penerbit, cariVal;
    private ArrayList<String> judulList= new ArrayList<>();
    private ArrayList<String> gambarList= new ArrayList<String>();
    private ArrayList<String> penerbitList = new ArrayList<>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> kategoriList = new ArrayList<>();
    private ArrayList<Integer> favList = new ArrayList<Integer>();
    RecyclerView rc_fav;
    int totalData=0, mentok=0;
    DBHelper helper;
    LinearLayout no_result;
    ShimmerLayout sh_fav;
    TextView favJudul;
    List<DBModel> dbList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        rc_fav = findViewById(R.id.rc_fav);
        no_result = findViewById(R.id.no_result);
        favJudul = findViewById(R.id.fav_judul);
        sh_fav = findViewById(R.id.shimmer_fav);
        sh_fav.startShimmerAnimation();
        //Cursor cursor = db.fetchMahasiswa(tnim);
        helper = new DBHelper(getApplicationContext());
        totalData = helper.getTotalFav();
        dbList= new ArrayList<DBModel>();
        dbList = helper.getFromDB();
        System.out.println("List"+totalData);
        if(totalData<1){
            favJudul.setVisibility(View.GONE);
            sh_fav.stopShimmerAnimation();
            sh_fav.setVisibility(View.GONE);
            rc_fav.setVisibility(View.GONE);
            no_result.setVisibility(View.VISIBLE);
        }else{
            for (int i = 0; i < totalData; i++) {
                judulList.add(dbList.get(i).getJudul());
                gambarList.add(dbList.get(i).getGambar());
                penerbitList.add(dbList.get(i).getPenerbit());
                waktuList.add(dbList.get(i).getWaktu());;
                urlList.add(dbList.get(i).getUrl());
                kategoriList.add(dbList.get(i).getKategori());
                favList.add(Integer.parseInt(dbList.get(i).getFavorit()));
                System.out.println("Masuk while "+judulList);
                if(totalData - i == 1){
                    mentok=1;
                }
            }
            if(mentok>0){
                ArtikelAdapter mDataAdapter = new ArtikelAdapter( FavActivity.this, judulList, kategoriList,
                        gambarList, urlList,penerbitList,waktuList,favList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
                rc_fav.setLayoutManager(mLayoutManager);
                rc_fav.setAdapter(mDataAdapter);
                sh_fav.stopShimmerAnimation();
                sh_fav.setVisibility(View.GONE);
                System.out.println("Mentok"+judulList);

            }

        }
    }
}
