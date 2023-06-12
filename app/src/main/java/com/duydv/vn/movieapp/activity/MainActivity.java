package com.duydv.vn.movieapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.duydv.vn.movieapp.R;
import com.duydv.vn.movieapp.adapter.MyViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 mViewPager2;
    private TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        mViewPager2.setAdapter(myViewPagerAdapter);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home){
                    mViewPager2.setCurrentItem(0);
                    txt_title.setText(getString(R.string.nav_home));
                }else if(id == R.id.nav_favorite){
                    mViewPager2.setCurrentItem(1);
                    txt_title.setText(getString(R.string.nav_favorite));
                }else if(id == R.id.nav_history){
                    mViewPager2.setCurrentItem(2);
                    txt_title.setText(getString(R.string.nav_history));
                }
                return true;
            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        txt_title.setText(getString(R.string.nav_home));
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                        txt_title.setText(getString(R.string.nav_favorite));
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.nav_history).setChecked(true);
                        txt_title.setText(getString(R.string.nav_history));
                        break;
                }
            }
        });
    }

    private void initUI() {
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mViewPager2 = findViewById(R.id.view_pager_2);
        txt_title = findViewById(R.id.txt_title);
    }
}