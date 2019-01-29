package sadba.lab.com.testplanete.Ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;



import io.realm.Realm;
import sadba.lab.com.testplanete.Model.Bulletin;
import sadba.lab.com.testplanete.R;

public class Webview extends AppCompatActivity {

    private Realm realm;
    private String ien;
   // ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        realm = Realm.getDefaultInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ien = sharedPreferences.getString("ien_enfant", "");

        String id_semestre = getIntent().getStringExtra("id_semestre");

        Bulletin bulletin = realm.where(Bulletin.class).equalTo("id_semestre", id_semestre).findFirst();

        String myPdfUrl = bulletin.getChemin_bulletin();
        //Toast.makeText(this, myPdfUrl, Toast.LENGTH_SHORT).show();
        String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        final WebView webView;
        webView = findViewById(R.id.webView_pdf);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);


    }
}
