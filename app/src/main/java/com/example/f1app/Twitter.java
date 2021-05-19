package com.example.f1app;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebView;

public class Twitter extends AppCompatActivity {

    public static final String TAG = "TimeLineActivity";
    private static final String baseURl = "http://twitter.com";
    private static final String widgetInfo = "<a class=\"twitter-timeline\" href=\"https://twitter.com/KamilVivern/lists/f1-news-71142?ref_src=twsrc%5Etfw\">A Twitter List by KamilVivern</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script> ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView webView = (WebView) findViewById(R.id.feedWebView);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(baseURl, widgetInfo, "text/html", "UTF-8", null);
    }



}
