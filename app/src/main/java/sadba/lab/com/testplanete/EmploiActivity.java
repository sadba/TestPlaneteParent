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
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import sadba.lab.com.testplanete.Adapter.ViewPagerAdapter;
import sadba.lab.com.testplanete.Fragments.JeudiFragment;
import sadba.lab.com.testplanete.Fragments.LundiFragment;
import sadba.lab.com.testplanete.Fragments.MardiFragment;
import sadba.lab.com.testplanete.Fragments.MercrediFragment;
import sadba.lab.com.testplanete.Fragments.SamediFragment;
import sadba.lab.com.testplanete.Fragments.VendrediFragment;
import sadba.lab.com.testplanete.Model.Temps;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;

public class EmploiActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String ien_bis;
    private Realm realm;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploi);

        progressDoalog = new ProgressDialog(EmploiActivity.this);
        progressDoalog.setMessage("Chargement des donnees...");
        progressDoalog.show();

        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);

        toolbar =  findViewById(R.id.toolbar_emploi);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setTitle("Emploi du temps");

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

        if (isNetworkAvailable(this)){
            getEmploi(ien_bis);
        } else {
            progressDoalog.dismiss();
        }



    }

    private void getEmploi(String ien) {
        //realm = Realm.getDefaultInstance();
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);
        service.getTemps(ien_bis)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Temps>>() {
                    @Override
                    public void onNext(List<Temps> temps) {
                        // realm = Realm.getDefaultInstance();
                        //Toast.makeText(EmploiActivity.this, String.valueOf(temps.size()), Toast.LENGTH_SHORT).show();

                        try{
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                for (Temps temp: temps){
                                    Temps temps1 = new Temps();
                                    temps1.setLibelle_discipline(temp.getLibelle_discipline());
                                    temps1.setHeure_debut(temp.getHeure_debut());
                                    temps1.setHeure_fin(temp.getHeure_fin());
                                    temps1.setLibelle_classe(temp.getLibelle_classe());
                                    temps1.setNum_jour(temp.getNum_jour());
                                    temps1.setId_planing_horaire(temp.getId_planing_horaire());
                                    temps1.setCode_classe(temp.getCode_classe());
                                    temps1.setCouleur_discipline(temp.getCouleur_discipline());
                                    temps1.setId_classe_physique(temp.getId_classe_physique());
                                    temps1.setIen_eleve(temp.getIen_eleve());
                                    realm.copyToRealmOrUpdate(temps1);

                                    progressDoalog.dismiss();
                                }


                            });
                        } catch (Exception e){
                            realm.close();
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

    private void setUpViewPager(final ViewPager viewPager){
        // Enfant enfant = new Enfant();

        ien_bis = getIntent().getStringExtra("ien_bis");
        //Toast.makeText(this, ien_bis, Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("ien_enfant", ien_bis);

        LundiFragment lundiFragment = new LundiFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), bundle, this);
        adapter.AddFragment(lundiFragment, "LUN");
        adapter.AddFragment(new MardiFragment(), "MAR");
        adapter.AddFragment(new MercrediFragment(), "MER");
        adapter.AddFragment(new JeudiFragment(), "JEU");
        adapter.AddFragment(new VendrediFragment(), "VEN");
        adapter.AddFragment(new SamediFragment(), "SAM");
        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        lundiFragment.setArguments(bundle);

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
