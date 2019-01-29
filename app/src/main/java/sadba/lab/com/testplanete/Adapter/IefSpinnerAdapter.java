package sadba.lab.com.testplanete.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sadba.lab.com.testplanete.Model.Departement;
import sadba.lab.com.testplanete.Model.Iefs;
import sadba.lab.com.testplanete.R;

public class IefSpinnerAdapter extends ArrayAdapter<String> {

    private final List<Departement> departements;
    private final Context context;
    private LayoutInflater inflater;
    private int mResource;

    public IefSpinnerAdapter(Context context, int mResource, List departements) {
        super(context, mResource, 0, departements);
        this.departements = departements;
        this.context = context;
        this.mResource = mResource;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @SuppressLint("SetTextI18n")
    private View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.ias_item, parent, false);
        Departement depart = departements.get(position);

        TextView label = row.findViewById(R.id.ias);

        String mot = depart.getLibelle_departement().substring(4, depart.getLibelle_departement().length());


        label.setText(mot);


        return row;
    }
}
