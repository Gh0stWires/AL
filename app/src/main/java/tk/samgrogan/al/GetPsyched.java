package tk.samgrogan.al;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class GetPsyched extends AppCompatActivity {

    WebView wolf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_psyched);

        wolf = findViewById(R.id.wolf);
        WebSettings webSettings = wolf.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        //webSettings.setSupportMultipleWindows(true);
        wolf.loadUrl("http://samgrogan.tk/ghost/");

    }
}
