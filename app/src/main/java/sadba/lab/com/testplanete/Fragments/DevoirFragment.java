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
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import sadba.lab.com.testplanete.Adapter.NoteAdapter;
import sadba.lab.com.testplanete.Model.Note;
import sadba.lab.com.testplanete.R;
import sadba.lab.com.testplanete.Remote.IMyAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevoirFragment extends Fragment {


    private RecyclerView recycler_devoir;
    private String value;
    private Realm realm;
    private List<Note> notes = new ArrayList<>();
    private RealmResults<Note> results;
    private TextView visible;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyAPI iMyAPI;

    View view;

    public DevoirFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        value = sharedPreferences.getString("ien_enfant", "");
        //Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();




        realm = Realm.getDefaultInstance();
        results = realm.where(Note.class)
                .equalTo("devoir", "Devoir")
                .equalTo("ien", value)
                .findAllAsync();


        notes = realm.copyFromRealm(results);
        Toast.makeText(getContext(), notes.toString(), Toast.LENGTH_SHORT).show();

        //realm.close();




    }

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_devoir, container, false);
        recycler_devoir = view.findViewById(R.id.recycler_devoir);

        //visible = view.findViewById(R.id.visibility_devoir);
        //if (results.isEmpty()){
          //  visible.setVisibility(View.VISIBLE);
       // }


        NoteAdapter adapter = new NoteAdapter(getContext(), notes);
        recycler_devoir.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_devoir.setHasFixedSize(true);
        //recycler_lundi.setItemAnimator(getContext());
        recycler_devoir.setAdapter(adapter);
       // getNotes();
        return view;

    }

    /*private void getNotes() {
        compositeDisposable.add(iMyAPI.getNotes("934PXXQE")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Note>>() {
                       @Override
                       public void accept(List<Note> notes) throws Exception {
                            displayData(notes);
                       }
                   }, new Consumer<Throwable>() {
                       @Override
                       public void accept(Throwable throwable) throws Exception {

                       }
                   }
        ));
    }

    private void displayData(List<Note> notes) {
        NoteAdapter noteAdapter = new NoteAdapter(getContext(), notes);
        recycler_devoir.setAdapter(noteAdapter);
    } */

}
