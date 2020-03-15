package com.vrcorp.myblog;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.vrcorp.myblog.adapter.TabAdapter;
import com.vrcorp.myblog.layout.GayaHidupFragment;
import com.vrcorp.myblog.layout.MakananFragment;
import com.vrcorp.myblog.layout.RekomendasiFragment;
import com.vrcorp.myblog.layout.HobiFragment;
import com.vrcorp.myblog.layout.SportFragment;

public class MainActivity extends AppCompatActivity {private TabAdapter adapter;
    //private TabLayout tabLayout;
    private MaterialViewPager viewPager;
    Toolbar toolbar;
    ActionBar actionBar;
    NestedScrollView scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager =  findViewById(R.id.viewPager);
        //scroll = findViewById(R.id.scRoll);
        ViewPager viewpager = viewPager.getViewPager();
        toolbar = viewPager.getToolbar();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        //actionBar.hide();
        //tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new RekomendasiFragment(), "Rekomendasi");
        adapter.addFragment(new HobiFragment(), "Hobi");
        adapter.addFragment(new GayaHidupFragment(), "Gaya Hidup");
        adapter.addFragment(new MakananFragment(), "Makanan");
        adapter.addFragment(new SportFragment(), "Olahraga");
        //MaterialViewPagerHelper.registerScrollView(this, scroll);
        viewpager.setAdapter(adapter);
        viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());
        viewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.orange_400,
                                "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue_400,
                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red_400,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green_400,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    case 4:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.yellow_400,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        //tabLayout.setupWithViewPager(viewpager);
        if (toolbar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_fav) {
            Intent intent = new Intent(getApplicationContext(), FavActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_cari) {
            Intent intent = new Intent(getApplicationContext(), CariActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}