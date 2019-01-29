package sadba.lab.com.testplanete.Fragments;


import android.annotation.SuppressLint;
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



import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import io.realm.Realm;
import io.realm.RealmResults;
import sadba.lab.com.testplanete.Adapter.TempsAdapter;
import sadba.lab.com.testplanete.Model.Temps;
import sadba.lab.com.testplanete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MercrediFragment extends Fragment {


    private RecyclerView recycler_mercredi;
    private String value;
    private List<Temps> temps = new ArrayList<>();
    private Realm realm;
    private RealmResults<Temps> results;
    private TextView visible;

    View view;

    public MercrediFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //realm = Realm.getDefaultInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        value = sharedPreferences.getString("ien_enfant", "");

        //InfosEleves infosEleves = realm.where(InfosEleves.class).equalTo("ien" ,value).findFirst();
       // String code_classe = infosEleves.getCode_classe();

        realm = Realm.getDefaultInstance();
        results = realm.where(Temps.class)
                .equalTo("num_jour", "3")
                .equalTo("ien_eleve", value)
                .findAllAsync();


        temps = realm.copyFromRealm(results);

        realm.close();


    }

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mercredi, container, false);
        recycler_mercredi = view.findViewById(R.id.recycler_temps2);

      //  visible = view.findViewById(R.id.visibility_mercredi);
        //if (results.isEmpty()){
          //  visible.setVisibility(View.VISIBLE);
       // }


        TempsAdapter adapter = new TempsAdapter(getContext(), temps);
        recycler_mercredi.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_mercredi.setHasFixedSize(true);
        //recycler_lundi.setItemAnimator();
        recycler_mercredi.setAdapter(adapter);
        return view;

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
