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
import sadba.lab.com.testplanete.Adapter.NoteAdapter;
import sadba.lab.com.testplanete.Model.Note;
import sadba.lab.com.testplanete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompoFragment extends Fragment {


    private RecyclerView recycler_compo;
    private String value;
    private Realm realm;
    private RealmResults<Note> results;
    private List<Note> notes = new ArrayList<>();
    private TextView visible;

    View view;

    public CompoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        value = sharedPreferences.getString("ien_enfant", "");



        realm = Realm.getDefaultInstance();
         results = realm.where(Note.class)
                .equalTo("devoir", "Composition")
                 .equalTo("ien", value)
                .findAllAsync();


        notes = realm.copyFromRealm(results);

        realm.close();


    }

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_compo, container, false);
        recycler_compo = view.findViewById(R.id.recycler_compo);

       // visible = view.findViewById(R.id.visibility_compo);
     //   if (results.isEmpty()){
          //  visible.setVisibility(View.VISIBLE);
       // }


        NoteAdapter adapter = new NoteAdapter(getContext(), notes);
        recycler_compo.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_compo.setHasFixedSize(true);
        //recycler_lundi.setItemAnimator(getContext());
        recycler_compo.setAdapter(adapter);
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
