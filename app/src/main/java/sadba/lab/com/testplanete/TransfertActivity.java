package sadba.lab.com.testplanete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sadba.lab.com.testplanete.Adapter.CommSpinnerAdapter;
import sadba.lab.com.testplanete.Adapter.CycleSpinnerAdapter;
import sadba.lab.com.testplanete.Adapter.EtabSPinnerAdapter;
import sadba.lab.com.testplanete.Adapter.IaSpinnerAdapter;
import sadba.lab.com.testplanete.Adapter.IefSpinnerAdapter;
import sadba.lab.com.testplanete.Model.Commune;
import sadba.lab.com.testplanete.Model.Departement;
import sadba.lab.com.testplanete.Model.EtabTransfert;
import sadba.lab.com.testplanete.Model.IaResponse;
import sadba.lab.com.testplanete.Model.Ias;
import sadba.lab.com.testplanete.Model.IefResponse;
import sadba.lab.com.testplanete.Model.Iefs;
import sadba.lab.com.testplanete.Model.Region;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;
import sadba.lab.com.testplanete.Remote.RetrofitInstance;

public class TransfertActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spinner;
    private String code_region;
    private String code_departement;
    private String code_cycle;
    private String code_comm;
    private String code_comm2;
    private String code_comm3;
    private String code_str;
    private String code_str2;
    private String school1;
    private String school2;
    private String type;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert);

        toolbar =  findViewById(R.id.toolbarAgents);
        toolbar.setTitle("Demande de transfert");

        editText = findViewById(R.id.edt_email);

        getCycles();

        //getRegions();


       // spinner = findViewById(R.id.spinnerIa);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });

        Button btnTransfert = findViewById(R.id.btn_transfert);
        btnTransfert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTransfert();
            }
        });



    }

    private void postTransfert() {

    }

    private void getCycles() {
        IMyAPI service = RetrofitInstance.getRetrofitInstance().create(IMyAPI.class);
        Call<IaResponse> call = service.getCycle();

        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<IaResponse>() {
            @Override
            public void onResponse(Call<IaResponse> call, Response<IaResponse> response) {
                generateCycles(response.body().getIasList());
            }

            @Override
            public void onFailure(Call<IaResponse> call, Throwable t) {

            }
        });
    }

    private void generateCycles(ArrayList<Ias> iasList) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerCycle);
        CycleSpinnerAdapter cycleSpinnerAdapter = new CycleSpinnerAdapter(getBaseContext(), R.layout.ias_item, iasList);
        spinner.setAdapter(cycleSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir un cycle"))
                {
                    //do nothing
                } else {
                    Ias ias = (Ias) adapterView.getItemAtPosition(i);
                    code_cycle = ias.getCode_cycle();
                    if (code_cycle != null){
                        final Spinner spinnerComm = findViewById(R.id.spinnerIa);
                        spinnerComm.setVisibility(View.VISIBLE);
                        getRegions();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getIefs(final String code_region) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getDepart(code_region)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Departement>>() {
                    @Override
                    public void onNext(List<Departement> departements) {

                        generateIef(departements);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void generateIef(List<Departement> departements) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerIef);
        IefSpinnerAdapter iefSpinnerAdapter = new IefSpinnerAdapter(getBaseContext(), R.layout.ias_item, departements);
        spinner.setAdapter(iefSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir un département"))
                {
                    //do nothing
                } else {
                    Departement dept = (Departement) adapterView.getItemAtPosition(i);
                    code_departement = dept.getCode_departement();
                    if (code_departement != null){
                        final Spinner spinnerComm = findViewById(R.id.spinnerCommune);
                        spinnerComm.setVisibility(View.VISIBLE);
                        getCommunes(code_departement);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCommunes(String code_departement) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getComm(code_departement)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Commune>>() {
                    @Override
                    public void onNext(List<Commune> communes) {

                        generateComm(communes);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void generateComm(List<Commune> communes) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerCommune);
        CommSpinnerAdapter commSpinnerAdapter = new CommSpinnerAdapter(getBaseContext(), R.layout.ias_item, communes);
        spinner.setAdapter(commSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir une commune")) {
                    //do nothing
                } else {
                    Commune comm = (Commune) adapterView.getItemAtPosition(i);
                    code_comm = comm.getCode_commune();
                    if (code_comm != null) {
                        final Spinner spinnerComm = findViewById(R.id.spinnerEtab);
                        spinnerComm.setVisibility(View.VISIBLE);
                        getEtab(code_cycle, code_comm);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getEtab(String code_cycle, String code_comm) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getEtab(code_cycle, code_comm)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<EtabTransfert>>() {
                    @Override
                    public void onNext(List<EtabTransfert> etabTransferts) {

                        generateEtab(etabTransferts);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void generateEtab(List<EtabTransfert> etabTransferts) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerEtab);
        EtabSPinnerAdapter etabSPinnerAdapter = new EtabSPinnerAdapter(getBaseContext(), R.layout.ias_item, etabTransferts);
        spinner.setAdapter(etabSPinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir un etablissement")) {
                    //do nothing
                } else {
                    EtabTransfert etab = (EtabTransfert) adapterView.getItemAtPosition(i);
                    code_str = etab.getCode_str();
                    if (code_region != null){
                        //final Spinner spinnerIef = findViewById(R.id.spinnerIef);
                        final Spinner spinnerCycle = findViewById(R.id.spinnerCycle);
                        final Spinner spinnerRegion = findViewById(R.id.spinnerIa);
                        final Spinner spinnerDepartement = findViewById(R.id.spinnerIef);
                        final Spinner spinnerComm2 = findViewById(R.id.spinnerComm2);
                        spinnerCycle.setVisibility(View.GONE);
                        //spinnerIef.setVisibility(View.INVISIBLE);
                        spinnerRegion.setVisibility(View.GONE);
                        spinnerDepartement.setVisibility(View.GONE);
                        spinnerComm2.setVisibility(View.VISIBLE);
                        getCommunes2(code_departement);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCommunes2(String code_departement) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getComm(code_departement)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Commune>>() {
                    @Override
                    public void onNext(List<Commune> communes) {

                        generateComm2(communes);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void generateComm2(List<Commune> communes) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerComm2);
        CommSpinnerAdapter commSpinnerAdapter = new CommSpinnerAdapter(getBaseContext(), R.layout.ias_item, communes);
        spinner.setAdapter(commSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir une commune")) {
                    //do nothing
                } else {
                    Commune comm = (Commune) adapterView.getItemAtPosition(i);
                    code_comm2 = comm.getCode_commune();
                    if (code_comm2 != null) {
                        final Spinner spinnerComm = findViewById(R.id.spinnerEtab2);
                        spinnerComm.setVisibility(View.VISIBLE);
                        getEtab2(code_cycle, code_comm2);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getEtab2(String code_cycle, String code_comm2) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getEtab(code_cycle, code_comm2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<EtabTransfert>>() {
                    @Override
                    public void onNext(List<EtabTransfert> etabTransferts) {

                        generateEtab2(etabTransferts);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void generateEtab2(List<EtabTransfert> etabTransferts) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerEtab2);
        EtabSPinnerAdapter etabSPinnerAdapter = new EtabSPinnerAdapter(getBaseContext(), R.layout.ias_item, etabTransferts);
        spinner.setAdapter(etabSPinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir un deuxieme etablissement")) {
                    //do nothing
                } else {
                    EtabTransfert etab = (EtabTransfert) adapterView.getItemAtPosition(i);
                    code_str2 = etab.getCode_str();
                    if (code_str2 != null){
                        //final Spinner spinnerIef = findViewById(R.id.spinnerIef);
                        final Spinner spinnerComm3 = findViewById(R.id.spinnerComm3);
                        spinnerComm3.setVisibility(View.VISIBLE);

                        getCommunes3(code_departement);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCommunes3(String code_departement) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getComm(code_departement)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Commune>>() {
                    @Override
                    public void onNext(List<Commune> communes) {

                        generateComm3(communes);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void generateComm3(List<Commune> communes) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerComm3);
        CommSpinnerAdapter commSpinnerAdapter = new CommSpinnerAdapter(getBaseContext(), R.layout.ias_item, communes);
        spinner.setAdapter(commSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir une commune")) {
                    //do nothing
                } else {
                    Commune comm = (Commune) adapterView.getItemAtPosition(i);
                    code_comm3 = comm.getCode_commune();
                    if (code_comm3 != null) {
                        final Spinner spinnerComm = findViewById(R.id.spinnerEtab3);
                        spinnerComm.setVisibility(View.VISIBLE);
                        getEtab3(code_cycle, code_comm3);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getEtab3(String code_cycle, String code_comm3) {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getEtab(code_cycle, code_comm3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<EtabTransfert>>() {
                    @Override
                    public void onNext(List<EtabTransfert> etabTransferts) {

                        generateEtab3(etabTransferts);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void generateEtab3(List<EtabTransfert> etabTransferts) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerEtab3);
        EtabSPinnerAdapter etabSPinnerAdapter = new EtabSPinnerAdapter(getBaseContext(), R.layout.ias_item, etabTransferts);
        spinner.setAdapter(etabSPinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir un troisieme etablissement")) {
                    //do nothing
                } else {


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void getRegions() {
        IMyAPI service = ApiClient.getRetrofit1().create(IMyAPI.class);

        service.getRegions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Region>>() {
                    @Override
                    public void onNext(List<Region> regions) {

                        generateIa(regions);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransfertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void generateIa(List<Region> regions) {
        spinner = (MaterialSpinner) findViewById(R.id.spinnerIa);
        IaSpinnerAdapter iaSpinnerAdapter = new IaSpinnerAdapter(getBaseContext(), R.layout.ias_item, regions);
        spinner.setAdapter(iaSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Veuillez choisir une région"))
                {
                    //do nothing
                } else {
                    Region reg = (Region) adapterView.getItemAtPosition(i);
                    code_region = reg.getCode_region();
                    if (code_region != null){
                        final Spinner spinnerIef = findViewById(R.id.spinnerIef);
                        spinnerIef.setVisibility(View.VISIBLE);
                        getIefs(code_region);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
