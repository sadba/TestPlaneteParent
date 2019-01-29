package sadba.lab.com.testplanete.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import sadba.lab.com.testplanete.Model.Enseignant;
import sadba.lab.com.testplanete.R;

public class EnseignantAdapter extends RecyclerView.Adapter<EnseignantAdapter.EnseignantViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<Enseignant> enseignants;

    public EnseignantAdapter(Context context, List<Enseignant> enseignants){
        this.context = context;
        this.enseignants = enseignants;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public EnseignantAdapter.EnseignantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.enseignants_layout, parent, false);
        return new EnseignantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EnseignantAdapter.EnseignantViewHolder holder, int position) {
        Enseignant enseignant = enseignants.get(position);
        holder.setItemContent(enseignant);
    }

    @Override
    public int getItemCount(){
        return enseignants.size();
    }

    class EnseignantViewHolder extends RecyclerView.ViewHolder {
        TextView nom_complet, specialite;

        public EnseignantViewHolder(View itemView){
            super(itemView);
            nom_complet = itemView.findViewById(R.id.prenom_enseignant);
            specialite = itemView.findViewById(R.id.matiere_enseignant);
        }

        void setItemContent(Enseignant enseignant){
            nom_complet.setText(enseignant.getNom_complet());
            specialite.setText(enseignant.getSpecialite());
        }
    }


}
