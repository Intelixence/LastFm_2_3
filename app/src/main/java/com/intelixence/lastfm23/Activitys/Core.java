package com.intelixence.lastfm23.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.intelixence.lastfm23.CustomUi.ViewPager.LockableViewPager;
import com.intelixence.lastfm23.Fragments.TopArtists;
import com.intelixence.lastfm23.Fragments.TopTracks;
import com.intelixence.lastfm23.R;
import com.intelixence.peticiones.EasyReq;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Core extends AppCompatActivity {

    //components
    TextView tv_url;
    LockableViewPager lvp_fragment;
    TabLayout tl_fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        //starts the view components
        tv_url = findViewById(R.id.ac_tv_url);
        lvp_fragment = findViewById(R.id.ac_lvp_fragment);
        tl_fragments = findViewById(R.id.ac_tl_fragments);

        //process
        EasyReq.enabledHistoryRequests(true);
        tv_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.last.fm/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        Random r = new Random();
        int a = r.nextInt((100-10)+1)+10;
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), a);
        adapter.addFragment(new TopTracks(),"TOP TRACKS");
        adapter.addFragment(new TopArtists(),"TOP ARTISTS");
        lvp_fragment.setAdapter(adapter);
        tl_fragments.setupWithViewPager(lvp_fragment);
        lvp_fragment.setSwipeable(false);
        tl_fragments.getTabAt(tl_fragments.getSelectedTabPosition()).view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        tl_fragments.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tl_fragments.getTabAt(0).view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    tl_fragments.getTabAt(1).view.setBackgroundDrawable(null);
                }else{
                    tl_fragments.getTabAt(1).view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    tl_fragments.getTabAt(0).view.setBackgroundDrawable(null);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public static class TabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public TabAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
