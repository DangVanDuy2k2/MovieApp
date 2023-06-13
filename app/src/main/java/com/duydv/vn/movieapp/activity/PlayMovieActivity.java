package com.duydv.vn.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.duydv.vn.movieapp.R;
import com.duydv.vn.movieapp.model.Movie;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PlayMovieActivity extends AppCompatActivity {
    private WebView mWebView;
    private ImageView img_back;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_movie);

        getDataIntent();
        mWebView = findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();
        //enable JavaScript cho WebView
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl(mMovie.getUrl());

        img_back = findViewById(R.id.img_back);
        //exit current activity
        img_back.setOnClickListener(view -> onBackPressed());

        setHistory();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        mMovie = (Movie) bundle.get("object_movie");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setHistory(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("movie");

        Map<String,Object> map = new HashMap<>();
        map.put("history",true);

        myRef.child(String.valueOf(mMovie.getId())).updateChildren(map);
    }
}