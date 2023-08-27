package com.apkvisionewsapp.apkvisionnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Details extends AppCompatActivity {

    TextView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = findViewById(R.id.txt);

        Intent data = getIntent();
        String title = data.getStringExtra("title");
        String content = data.getStringExtra("content");


        toolbar.setText(title);

        WebView webview = findViewById(R.id.contentView);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";


        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setJavaScriptEnabled(true);



        webview.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}iframe{display: inline;height: 180;max-width: 100%;}</style>"+content,
                mimeType, encoding, null);


        //contentView.loadDataWithBaseURL("", content, mimeType, encoding, "");
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}