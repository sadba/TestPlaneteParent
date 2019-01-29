package sadba.lab.com.testplanete.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import sadba.lab.com.testplanete.Adapter.TempsAdapter;
import sadba.lab.com.testplanete.EvalActivity;
import sadba.lab.com.testplanete.Model.Temps;
import sadba.lab.com.testplanete.R;
import sadba.lab.com.testplanete.Remote.ApiClient;
import sadba.lab.com.testplanete.Remote.IMyAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class LundiFragment extends Fragment {

    private RecyclerView recycler_lundi;
    private String value;
    private List<Temps> temps = new ArrayList<>();
    private Realm realm;
    private RealmResults<Temps> results;
    private TextView visible;
    ProgressDialog progressDoalog;

    View view;

    public LundiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //realm = Realm.getDefaultInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        value = sharedPreferences.getString("ien_enfant", "");
        Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();


        //InfosEleves infosEleves = realm.where(InfosEleves.class).equalTo("ien" ,value).findFirst();
        //assert infosEleves != null;
        //String code_classe = infosEleves.getCode_classe();
       // realm.close();

        realm = Realm.getDefaultInstance();
        results = realm.where(Temps.class)
                .equalTo("num_jour", "1")
                .equalTo("ien_eleve", value)
                .findAllAsync();


        temps = realm.copyFromRealm(results);

        realm.close();







    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lundi, container, false);
        recycler_lundi = view.findViewById(R.id.recycler_temps);

        TempsAdapter adapter = new TempsAdapter(getContext(), temps);
        recycler_lundi.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_lundi.setHasFixedSize(true);
        //recycler_lundi.setItemAnimator(getContext());
        recycler_lundi.setAdapter(adapter);
        return view;

    }



}
