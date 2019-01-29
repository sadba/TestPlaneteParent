package sadba.lab.com.testplanete.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import sadba.lab.com.testplanete.AbscenceActivity;
import sadba.lab.com.testplanete.Adapter.EnfantSpinnerAdapter;
import sadba.lab.com.testplanete.EmploiActivity;
import sadba.lab.com.testplanete.EnseignantsActivity;
import sadba.lab.com.testplanete.EvalActivity;
import sadba.lab.com.testplanete.InfoEtabActivity;
import sadba.lab.com.testplanete.Model.Bulletin;
import sadba.lab.com.testplanete.Model.Enfant;
import sadba.lab.com.testplanete.Model.Note;
import sadba.lab.com.testplanete.Model.Temps;
import sadba.lab.com.testplanete.NoteActivity;
import sadba.lab.com.testplanete.R;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;
import sadba.lab.com.testplanete.TransfertActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private String ien;
    android.support.v7.widget.Toolbar toolbar;
     Realm realm;
    private CardView tempsCard, notesCard, evalCard, infosCard, abscenceCard;
    private List<Enfant>enfantsSpinner = new ArrayList<>();
    private RealmResults<Enfant> resultsEnfants;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        //getEnfants();

        tempsCard = findViewById(R.id.temps_card);
        notesCard = findViewById(R.id.notes_card);
        evalCard = findViewById(R.id.eval_card);
        infosCard = findViewById(R.id.infos_card);
        abscenceCard = findViewById(R.id.absc_retard);

        toolbar =  findViewById(R.id.toolbarHome);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        collapsingToolbar.setTitle("DASHBOARD");



        //Drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);

        //Add Click Listener to the cards
        tempsCard.setOnClickListener(this);
        notesCard.setOnClickListener(this);
        evalCard.setOnClickListener(this);
        infosCard.setOnClickListener(this);
        abscenceCard.setOnClickListener(this);


        //spinner
        spinner = findViewById(R.id.spinner_enfants);
        realm = Realm.getDefaultInstance();
        resultsEnfants = realm.where(Enfant.class).findAll();
        enfantsSpinner = realm.copyFromRealm(resultsEnfants);
        Toast.makeText(this, enfantsSpinner.toString(), Toast.LENGTH_SHORT).show();
        //realm.close();
       // Toast.makeText(this, String.valueOf(enfantsSpinner.size()), Toast.LENGTH_LONG).show();
        EnfantSpinnerAdapter enfantSpinnerAdapter = new EnfantSpinnerAdapter(getApplicationContext(), R.layout.enfants_item, enfantsSpinner);
        spinner.setAdapter(enfantSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Enfant enfantsClick = (Enfant) adapterView.getItemAtPosition(i);
                ien = enfantsClick.getIen_eleve();
                //Toast.makeText(HomeActivity.this, ien, Toast.LENGTH_SHORT).show();
                String code_classe = enfantsClick.getId_etablissement();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ien_enfant", ien);
                editor.putString("code_classe", code_classe);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        realm.close();

        getBulletins();


    }

    private void getBulletins() {
        //realm = Realm.getDefaultInstance();
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);
        service.getBulletins(ien)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Bulletin>>() {
                    @Override
                    public void onNext(List<Bulletin> bulletins) {
                        // realm = Realm.getDefaultInstance();
                        //Toast.makeText(HomeActivity.this, String.valueOf(bulletins.size()), Toast.LENGTH_SHORT).show();

                        try{
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                for (Bulletin bulletin: bulletins){
                                    Bulletin bulletin1 = new Bulletin();
                                    bulletin1.setId_semestre(bulletin.getId_semestre());
                                    bulletin1.setChemin_bulletin(bulletin.getChemin_bulletin());
                                    bulletin1.setLibelle_semestre(bulletin.getLibelle_semestre());


                                    realm.copyToRealmOrUpdate(bulletin1);
                                }
                            });
                        } catch (Exception e){
                            //Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            realm.close();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connection internet3", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dossier_eleve) {
            // Handle the camera action
           // Intent intent = new Intent(this, DossierActivity.class);
            //startActivity(intent);
        } else if (id == R.id.transfert) {

             Intent intent = new Intent(this, TransfertActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            // Handle the camera action
            Intent intent = new Intent(this, EnseignantsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.profil_parent) {
            //Intent intent = new Intent(this, ProfilParentActivity.class);
            //startActivity(intent);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.temps_card :
                i = new Intent(this, EmploiActivity.class);
                i.putExtra("ien_bis", ien);
                startActivity(i);
                break;

            case R.id.notes_card : i =
                    new Intent(this, NoteActivity.class);
                i.putExtra("ien_bis", ien);
                startActivity(i);
                break;

            case R.id.eval_card : i =
                    new Intent(this, EvalActivity.class);
                i.putExtra("ien_bis", ien);
                startActivity(i);
                break;
            case R.id.infos_card : i =
                    new Intent(this, InfoEtabActivity.class);
                i.putExtra("ien_bis", ien);
                startActivity(i);
                break;
            case R.id.absc_retard : i =
                    new Intent(this, AbscenceActivity.class);
                i.putExtra("ien_bis", ien);
                startActivity(i);
                break;
            default: break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
