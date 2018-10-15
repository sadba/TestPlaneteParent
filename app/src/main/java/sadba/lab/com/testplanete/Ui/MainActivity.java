package sadba.lab.com.testplanete.Ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sadba.lab.com.testplanete.Common.Common;
import sadba.lab.com.testplanete.Model.Enfant;
import sadba.lab.com.testplanete.Model.PostRegisterUser;
import sadba.lab.com.testplanete.Model.PostUser;
import sadba.lab.com.testplanete.Model.PostVerifUser;
import sadba.lab.com.testplanete.Model.RegisterUser;
import sadba.lab.com.testplanete.Model.User;
import sadba.lab.com.testplanete.Model.VerifUser;
import sadba.lab.com.testplanete.R;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;

public class MainActivity extends AppCompatActivity {



    TextView txt_register, txt_verif;
    EditText edt_ien, edt_password;
    Button btn_login;
    SharedPreferences sp;
    SharedPreferences sp1;
    IMyAPI mService;
    Realm realm;
    private VerifUser verifUsers;
    //private String ien;
    //final String ien = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Service
        mService = Common.getAPI();



        //getEnfants();

        //Init View
        txt_verif = findViewById(R.id.txt_verif);
        edt_ien = findViewById(R.id.edt_ien);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        sp = getSharedPreferences("btn_login", MODE_PRIVATE);
        sp1 = getSharedPreferences("ien_parent", MODE_PRIVATE);

        if (sp.getBoolean("logged", false)){
            gotToHomeActivity();
        }

        //Event
        txt_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, VerifActivity.class));
                showVerifDialog();
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  ien = edt_ien.getText().toString();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ien_parent", ien);
                editor.apply();
                String password = edt_password.getText().toString();
                if (TextUtils.isEmpty(password)){
                    edt_password.setError("Mot de passe ne pas etre vide");
                } else if (TextUtils.isEmpty(ien)){
                    edt_ien.setError("Ien ne doit pas etre vide");
                } else {
                    authenticateUser(ien, password);



                    //edt_ien.getText().clear();
                    //edt_password.getText().clear();
                }

            }
        });




    }







    private void showVerifDialog() {
        View view = getLayoutInflater().inflate(R.layout.verif_layout, null);

        final MaterialEditText edt_ien = (MaterialEditText) view.findViewById(R.id.edt_ienChild);
        final MaterialEditText edt_cni = (MaterialEditText) view.findViewById(R.id.edt_id_card);

        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(view)
                .setTitle("Verification des données")
                .setPositiveButton("VERIFIER", null)
                .setNegativeButton("ANNULER", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //alertDialog.dismiss();
                        String ien = edt_ien.getText().toString();
                        String cni = edt_cni.getText().toString();

                        if (TextUtils.isEmpty(ien)){
                            edt_ien.setError("L'IEN ne doit pas etre vide");
                            // Toast.makeText(MainActivity.this, "yup", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(cni)){
                            edt_cni.setError("Le CNI ne doit pas etre vide");
                            return;
                        } else {
                            PostVerifUser postVerifUser = new PostVerifUser();
                            postVerifUser.setIen(ien);
                            postVerifUser.setCni(cni);

                            final android.app.AlertDialog watingDialog = new SpotsDialog(MainActivity.this);
                            watingDialog.show();
                            watingDialog.setTitle("En cours...");
                            mService.verifUser(postVerifUser)
                                    .enqueue(new Callback<VerifUser>() {
                                        @Override
                                        public void onResponse(Call<VerifUser> call, Response<VerifUser> response) {
                                            VerifUser result = response.body();
                                            if (result.getCode().equals("1")){
                                                //progressDoalog.dismiss();
                                                Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                                                watingDialog.dismiss();
                                            } else {

                                                alertDialog.dismiss();
                                                watingDialog.dismiss();

                                                Realm mRealm = null;
                                                try{
                                                    mRealm = Realm.getDefaultInstance();
                                                    final Realm finalMRealm = mRealm;
                                                    mRealm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            try{
                                                                VerifUser verifUser = new VerifUser();
                                                                verifUser.setCode(result.getCode());
                                                                verifUser.setMessage(result.getMessage());
                                                                verifUser.setIen_parent(result.getIen_parent());
                                                                verifUser.setNom_parent(result.getNom_parent());
                                                                verifUser.setNombre_enfants(result.getNombre_enfants());
                                                                verifUser.setPrenom_parent(result.getPrenom_parent());
                                                                verifUser.setType_affiliation(result.getType_affiliation());

                                                                finalMRealm.copyToRealm(verifUser);

                                                                showRegisterDialog();

                                                                getEnfants();

                                                            } catch (RealmPrimaryKeyConstraintException e){
                                                                Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } finally {
                                                    if (mRealm != null) {
                                                        mRealm.close();
                                                    }
                                                }

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<VerifUser> call, Throwable t) {
                                            // progressDoalog.dismiss();
                                            watingDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Veuillez vérifier votre connection", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });

                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void getEnfants() {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //String value = sharedPreferences.getString("ien_parent", "");
        realm = Realm.getDefaultInstance();
        verifUsers = realm.where(VerifUser.class).findFirst();
        String value = verifUsers.getIen_parent();
        //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        realm.close();

        IMyAPI service = ApiClient.getRetrofit().create(IMyAPI.class);
        service.getEnfants(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Enfant>>() {
                    @Override
                    public void onNext(List<Enfant> enfants) {
                        // realm = Realm.getDefaultInstance();
                        //Toast.makeText(HomeActivity.this, String.valueOf(bulletins.size()), Toast.LENGTH_SHORT).show();


                        try{

                            realm.executeTransaction(realm1 -> {
                                for (Enfant enfant: enfants){
                                    Enfant enfant1 = new Enfant();
                                    enfant1.setCode(enfant.getCode());
                                    enfant1.setMessage(enfant.getMessage());
                                    enfant1.setType_affiliation(enfant.getType_affiliation());
                                    enfant1.setDate_naiss_eleve(enfant.getDate_naiss_eleve());
                                    enfant1.setSexe_eleve(enfant.getSexe_eleve());
                                    enfant1.setPrenom_eleve(enfant.getPrenom_eleve());
                                    enfant1.setNom_eleve(enfant.getNom_eleve());
                                    enfant1.setLieu_naiss_eleve(enfant.getLieu_naiss_eleve());
                                    enfant1.setLibelle_etablissement(enfant.getLibelle_etablissement());
                                    enfant1.setLibelle_cycle(enfant.getLibelle_cycle());
                                    enfant1.setIen_eleve(enfant.getIen_eleve());
                                    enfant1.setId_parent(enfant.getId_parent());
                                    enfant1.setId_niveau(enfant.getId_niveau());
                                    enfant1.setId_etablissement(enfant.getId_etablissement());
                                    enfant1.setId_cycle(enfant.getId_cycle());
                                    enfant1.setLibelle_niveau(enfant.getLibelle_niveau());


                                    realm.copyToRealmOrUpdate(enfant1);
                                    //realm.close();
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
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showRegisterDialog() {

        View view = getLayoutInflater().inflate(R.layout.register_layout, null);

        final AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                .setView(view)
                .setPositiveButton("VERIFIER", null)
                .setNegativeButton("ANNULER", null)
                .create();

        final MaterialEditText edt_ien = (MaterialEditText) view.findViewById(R.id.edt_ien_parent);
        final MaterialEditText edt_code = (MaterialEditText) view.findViewById(R.id.edt_sms_code);
        final MaterialEditText edt_password = (MaterialEditText) view.findViewById(R.id.edt_password_register);
        final MaterialEditText edt_confirm = (MaterialEditText) view.findViewById(R.id.edt_confirm_register);

        alertDialog1.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //alertDialog1.dismiss();
                        String ien = edt_ien.getText().toString();
                        String code_verif = edt_code.getText().toString();
                        String password = edt_password.getText().toString();
                        String confirm_password = edt_confirm.getText().toString();
                        if (TextUtils.isEmpty(ien)){
                            edt_ien.setError("L'IEN ne doit pas etre vide");
                            return;
                        } else if (TextUtils.isEmpty(code_verif)){
                            edt_code.setError("Le CNI ne doit pas etre vide");
                            return;
                        } else if (TextUtils.isEmpty(password) && !isPasswordValid(password)){
                            edt_password.setError("Mot de passe invalide");
                            return;
                        } else if (!password.equals(confirm_password)) {
                            edt_confirm.setError("Les mots de passe ne sont pas identiques");
                            return;
                        } else {
                            PostRegisterUser postRegisterUser = new PostRegisterUser();
                            postRegisterUser.setIen(ien);
                            postRegisterUser.setCode_verif(code_verif);
                            postRegisterUser.setPassword(password);

                            final android.app.AlertDialog watingDialog = new SpotsDialog(MainActivity.this);
                            watingDialog.show();
                            watingDialog.setTitle("En cours...");

                            mService.registerUser(postRegisterUser)
                                    .enqueue(new Callback<RegisterUser>() {
                                        @Override
                                        public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {

                                            RegisterUser result = response.body();
                                            if (result.getCode().equals("1")) {
                                                //progressDoalog.dismiss();
                                                Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                                            } else {

                                                watingDialog.dismiss();
                                                //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                                //show.dismiss();
                                                alertDialog1.dismiss();
                                                Toast.makeText(MainActivity.this, "Compte crée avec Succes", Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<RegisterUser> call, Throwable t) {
                                            //progressDoalog.dismiss();
                                            watingDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Veuillez vérifier votre connection", Toast.LENGTH_LONG).show();

                                        }
                                    });

                        }
                    }
                });

                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.cancel();
                    }
                });
            }
        });
        alertDialog1.show();




    }

    private void gotToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }



    private void authenticateUser(String ien, String password) {
        PostUser postUser = new PostUser();
        postUser.setPassword(password);
        postUser.setIen(ien);
        mService.loginUser(postUser)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User result = response.body();
                        if (result.getCode().equals("1")) {
                            Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        } else {

                            gotToHomeActivity();
                            sp.edit().putBoolean("logged", true).apply();
                            Realm mRealm = null;
                            try{
                                mRealm = Realm.getDefaultInstance();
                                final Realm finalMRealm = mRealm;
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        try{
                                            User user = new User();
                                           user.setCode(result.getCode());
                                           user.setIen(result.getIen());
                                           user.setMessage(result.getMessage());
                                            finalMRealm.copyToRealm(user);

                                            showRegisterDialog();

                                            getEnfants();

                                        } catch (RealmPrimaryKeyConstraintException e){
                                            Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } finally {
                                if (mRealm != null) {
                                    mRealm.close();
                                }
                            }

                        }


                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 6;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
