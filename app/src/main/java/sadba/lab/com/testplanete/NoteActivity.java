package sadba.lab.com.testplanete;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import sadba.lab.com.testplanete.Adapter.ViewPagerAdapter;
import sadba.lab.com.testplanete.Fragments.BulletinFragment;
import sadba.lab.com.testplanete.Fragments.CompoFragment;
import sadba.lab.com.testplanete.Fragments.DevoirFragment;
import sadba.lab.com.testplanete.Model.Note;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;

public class NoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Note> notes = new ArrayList<>();
    private Realm realm;
    private String ien_note;
    private RecyclerView recycler_devoir;
    private String ien;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        progressDoalog = new ProgressDialog(NoteActivity.this);
        progressDoalog.setMessage("Chargement des donnees...");
        progressDoalog.show();


        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);

        toolbar = findViewById(R.id.toolbar_emploi);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setTitle("Notes et Bulletins");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });

        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         ien = sharedPreferences1.getString("ien_enfant", "");
        //Toast.makeText(this, ien, Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), bundle, this);
        adapter.AddFragment(new DevoirFragment(), "Devoirs");
        adapter.AddFragment(new CompoFragment(), "Compositions");
        adapter.AddFragment(new BulletinFragment(), "Bulletins");

        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setUpViewPager(viewPager);

        if (isNetworkAvailable(this)){
            getNotes(ien);
        } else {
            progressDoalog.dismiss();
        }
    }

    private void setUpViewPager(final ViewPager viewPager) {

        Bundle bundle = new Bundle();

        DevoirFragment devoirFragment = new DevoirFragment();

        //Bundle bundle = new Bundle();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), bundle, this);
        adapter.AddFragment(new DevoirFragment(), "Devoirs");
        adapter.AddFragment(new CompoFragment(), "Compositions");
        adapter.AddFragment(new BulletinFragment(), "Bulletins");

        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        devoirFragment.setArguments(bundle);
    }

    private void getNotes(String ien) {
        // realm = Realm.getDefaultInstance();
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);
        service.getNotes(ien)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Note>>() {
                    @Override
                    public void onNext(List<Note> notes) {
                        //realm = Realm.getDefaultInstance();
                        //Toast.makeText(HomeActivity.this, String.valueOf(notes.size()), Toast.LENGTH_SHORT).show();

                        try{
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                for (Note note: notes){
                                    Note note1 = new Note();
                                    note1.setId_note(note.getId_note());
                                    note1.setDate_eval(note.getDate_eval());
                                    note1.setDevoir(note.getDevoir());
                                    note1.setLibelle_discipline(note.getLibelle_discipline());
                                    note1.setNote(note.getNote());
                                    note1.setIen(note.getIen());
                                    realm.copyToRealmOrUpdate(note1);

                                    progressDoalog.dismiss();
                                }
                            });
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDoalog.dismiss();
                        Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connection internet", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }


}
