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
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import sadba.lab.com.testplanete.Adapter.ViewPagerAdapter;
import sadba.lab.com.testplanete.Fragments.AbscenceFragment;
import sadba.lab.com.testplanete.Fragments.RetardFragment;
import sadba.lab.com.testplanete.Model.Abscence;
import sadba.lab.com.testplanete.Model.Retard;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;

public class AbscenceActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Realm realm;
    private String ien_bis;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abscence);

        progressDoalog = new ProgressDialog(AbscenceActivity.this);
        progressDoalog.setMessage("Chargement des donnees...");
        progressDoalog.show();

        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);

        toolbar =  findViewById(R.id.toolbar_emploi);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setTitle("Abscences et Retards");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });

        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ien_bis = sharedPreferences1.getString("ien_enfant", "");

        setUpViewPager(viewPager);

        getAbscences(ien_bis);
        getRetards(ien_bis);

        if (isNetworkAvailable(this)){
            getAbscences(ien_bis);
            getRetards(ien_bis);
        } else {
            progressDoalog.dismiss();
        }


    }

    private void getRetards(String ien) {
        //realm = Realm.getDefaultInstance();
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);
        service.getRetards(ien)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Retard>>() {
                    @Override
                    public void onNext(List<Retard> retards) {
                        // realm = Realm.getDefaultInstance();
                        //Toast.makeText(HomeActivity.this, String.valueOf(bulletins.size()), Toast.LENGTH_SHORT).show();

                        try{
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                for (Retard retard: retards){
                                    Retard retard1 = new Retard();
                                    retard1.setId_retard(retard.getId_retard());
                                    retard1.setDate_absence(retard.getDate_absence());
                                    retard1.setDiscipline(retard.getDiscipline());
                                    retard1.setDuree(retard.getDuree());
                                    retard1.setHeure_debut_cours(retard.getHeure_debut_cours());
                                    retard1.setJour(retard.getJour());
                                    retard1.setMotif(retard.getMotif());


                                    realm.copyToRealmOrUpdate(retard1);

                                    progressDoalog.dismiss();
                                }
                            });
                        } catch (Exception e){
                            //Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            realm.close();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDoalog.dismiss();
                        // Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connection internet1", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getAbscences(String ien) {
        //realm = Realm.getDefaultInstance();
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);
        service.getAbscences(ien_bis)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Abscence>>() {
                    @Override
                    public void onNext(List<Abscence> abscences) {
                        // realm = Realm.getDefaultInstance();
                        //Toast.makeText(HomeActivity.this, String.valueOf(bulletins.size()), Toast.LENGTH_SHORT).show();

                        try{
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                for (Abscence abscence: abscences){
                                    Abscence abscence1 = new Abscence();
                                    abscence1.setId_absence(abscence.getId_absence());
                                    abscence1.setDate_absence(abscence.getDate_absence());
                                    abscence1.setDiscipline(abscence.getDiscipline());
                                    abscence1.setHeure_d(abscence.getHeure_d());
                                    abscence1.setHeure_f(abscence.getHeure_f());
                                    abscence1.setJour(abscence.getJour());
                                    abscence1.setMotif(abscence.getMotif());


                                    realm.copyToRealmOrUpdate(abscence1);

                                    progressDoalog.dismiss();
                                }
                            });
                        } catch (Exception e){
                            //Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            realm.close();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDoalog.dismiss();
                        //Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connection internet2", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setUpViewPager(final ViewPager viewPager){


        Bundle bundle = new Bundle();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), bundle, this);

        //ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), bundle, this);
        adapter.AddFragment(new AbscenceFragment(), "ABSCENCES");
        adapter.AddFragment(new RetardFragment(), "RETARDS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
