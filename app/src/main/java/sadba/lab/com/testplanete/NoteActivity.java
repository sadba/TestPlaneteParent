package sadba.lab.com.testplanete;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        getToast();
    }

    private void getToast() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         value = sharedPreferences.getString("ien_enfant", "");

        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }
}
