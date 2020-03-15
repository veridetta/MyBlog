package com.vrcorp.myblog.layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.vrcorp.myblog.R;
import com.vrcorp.myblog.adapter.ArtikelAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class HobiFragment extends Fragment {
    ShimmerLayout sh_art;
    View view;
    RecyclerView rc_art;
    NestedScrollView scrollView;
    LinearLayout kosong;
    Document mBlogDocument  = null, cardDoc = null;
    String Nama, gambara, urlPosting, waktu, penerbit;
    private ArrayList<String> judulList= new ArrayList<>();
    private ArrayList<String> gambarList= new ArrayList<String>();
    private ArrayList<String> penerbitList = new ArrayList<>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> kategoriList = new ArrayList<>();
    private ArrayList<Integer> favList = new ArrayList<Integer>();
    int success=0;
    private static final boolean GRID_LAYOUT = false;
    public HobiFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HobiFragment newInstance(String param1, String param2) {
        HobiFragment fragment = new HobiFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getActivity(),"Pertamaa",Toast.LENGTH_LONG).show();
        //dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rekomendasi, container, false);
        rc_art = view.findViewById(R.id.rc_art);
        sh_art = view.findViewById(R.id.sh_art);
        kosong = view.findViewById(R.id.kosong);
        new CardGet().execute();
        sh_art.startShimmerAnimation();
        return view;
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = "https://mydemoblog19.blogspot.com/search/label/hobi";
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
            ArtikelAdapter mDataAdapter = new ArtikelAdapter( getActivity(), judulList, kategoriList,
                    gambarList, urlList,penerbitList,waktuList,favList);
            //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
            //attachToRecyclerView

            if(judulList.size()>0){
                rc_art.setHasFixedSize(true);

                //Use this now
                //rc_art.setLayoutManager(mLayoutManager);
                if (GRID_LAYOUT) {
                    rc_art.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                } else {
                    rc_art.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                rc_art.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                rc_art.setAdapter(mDataAdapter);
                //rc_art.setAdapter(mDataAdapter);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
            }else{
                kosong.setVisibility(View.VISIBLE);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
            }

            //--------------------------
            //-------------------------
            //dialog.dismiss();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        sh_art.startShimmerAnimation();

    }

    @Override
    public void onPause() {
        sh_art.stopShimmerAnimation();
        super.onPause();
    }
}