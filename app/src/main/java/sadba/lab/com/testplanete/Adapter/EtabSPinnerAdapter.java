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

import sadba.lab.com.testplanete.Model.EtabTransfert;
import sadba.lab.com.testplanete.R;

public class EtabSPinnerAdapter extends ArrayAdapter<String> {

    private final List<EtabTransfert> etabTransferts;
    private final Context context;
    private LayoutInflater inflater;
    private int mResource;

    public EtabSPinnerAdapter(Context context, int mResource, List etabTransferts) {
        super(context, mResource, 0, etabTransferts);
        this.etabTransferts = etabTransferts;
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
        EtabTransfert etab = etabTransferts.get(position);

        TextView label = row.findViewById(R.id.ias);

        // String mot = regi.getLibelle_region().substring(3, ia.getLibelle_structure().length());


        label.setText(etab.getLibelle_structure());


        return row;
    }
}

