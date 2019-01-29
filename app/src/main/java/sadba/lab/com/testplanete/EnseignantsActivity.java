package sadba.lab.com.testplanete;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import sadba.lab.com.testplanete.Adapter.EnseignantAdapter;
import sadba.lab.com.testplanete.Adapter.EvalAdapter;
import sadba.lab.com.testplanete.Model.Enseignant;
import sadba.lab.com.testplanete.Model.Evaluation;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;

public class EnseignantsActivity extends AppCompatActivity {

    private String ien;
    private Realm realm;
    private List<Enseignant> enseignants = new ArrayList<>();
    View view;
    private RecyclerView recycler_enseignants;

    private RealmResults<Enseignant> results;
    android.support.v7.widget.Toolbar toolbar;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enseignants);

        progressDoalog = new ProgressDialog(EnseignantsActivity.this);
        progressDoalog.setMessage("Chargement des donnees...");
        progressDoalog.show();

        toolbar =  findViewById(R.id.toolbar_emploi);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setTitle("Liste des Enseignants");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ien = sharedPreferences.getString("ien_enfant", "");
        recycler_enseignants = findViewById(R.id.recycler_enseignants);
        getEnseignants(ien);

        realm = Realm.getDefaultInstance();
        results = realm.where(Enseignant.class).findAll();

        enseignants = realm.copyFromRealm(results);

        EnseignantAdapter adapter = new EnseignantAdapter(getApplicationContext(), enseignants);
        recycler_enseignants.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler_enseignants.setHasFixedSize(true);
        recycler_enseignants.setAdapter(adapter);
        realm.close();

        assert ien != null;
        if (isNetworkAvailable(this)) {
            getEnseignants(ien);
        }else {
            progressDoalog.dismiss();
            realm = Realm.getDefaultInstance();
            results = realm.where(Enseignant.class)
                    .equalTo("ien_eleve", ien)
                    .findAll();

            enseignants = realm.copyFromRealm(results);

            EnseignantAdapter adapter1 = new EnseignantAdapter(getApplicationContext(), enseignants);
            recycler_enseignants.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recycler_enseignants.setHasFixedSize(true);
            recycler_enseignants.setAdapter(adapter1);
            realm.close();

        }

    }

    private void getEnseignants(String ien) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);
        service.getEnseignants(ien)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Enseignant>>() {
                    @Override
                    public void onNext(List<Enseignant> enseignants) {
                        // realm = Realm.getDefaultInstance();
                        //Toast.makeText(HomeActivity.this, String.valueOf(bulletins.size()), Toast.LENGTH_SHORT).show();

                        try{
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                for (Enseignant enseignant: enseignants){
                                    Enseignant enseignant1 = new Enseignant();
                                    enseignant1.setId_enseignant(enseignant.getId_enseignant());
                                    enseignant1.setIen_ens(enseignant.getIen_ens());
                                    enseignant1.setIen_eleve(enseignant.getIen_eleve());
                                    enseignant1.setNom_complet(enseignant.getNom_complet());
                                    enseignant1.setSpecialite(enseignant.getSpecialite());
                                    enseignant1.setId_enseignant(enseignant.getId_enseignant());


                                    realm.copyToRealmOrUpdate(enseignant1);

                                    progressDoalog.dismiss();
                                }
                            });
                        } catch (Exception e){
                            realm.close();
                        } if (enseignants.isEmpty()){
                            progressDoalog.dismiss();
                            Toast.makeText(EnseignantsActivity.this, "Pas d'enseignants actuellement", Toast.LENGTH_LONG).show();
                        } else{

                            EnseignantAdapter adapter = new EnseignantAdapter(EnseignantsActivity.this, enseignants);
                            recycler_enseignants.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recycler_enseignants.setItemAnimator(new DefaultItemAnimator());
                            recycler_enseignants.setAdapter(adapter);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connection internet1", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onStop() {
        // realm.close();
        super.onStop();
    }

}
