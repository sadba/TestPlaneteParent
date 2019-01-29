package sadba.lab.com.testplanete.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Enseignant extends RealmObject{

    @PrimaryKey
    @SerializedName("id_enseignant")
    @Expose
    private String id_enseignant;
    @SerializedName("ien_ens")
    @Expose
    private String ien_ens;
    @SerializedName("ien_eleve")
    @Expose
    private String ien_eleve;
    @SerializedName("nom_complet")
    @Expose
    private String nom_complet;
    @SerializedName("specialite")
    @Expose
    private String specialite;


    public Enseignant() {
    }

    public String getId_enseignant() {
        return id_enseignant;
    }

    public void setId_enseignant(String id_enseignant) {
        this.id_enseignant = id_enseignant;
    }

    public String getIen_ens() {
        return ien_ens;
    }

    public void setIen_ens(String ien_ens) {
        this.ien_ens = ien_ens;
    }

    public String getIen_eleve() {
        return ien_eleve;
    }

    public void setIen_eleve(String ien_eleve) {
        this.ien_eleve = ien_eleve;
    }

    public String getNom_complet() {
        return nom_complet;
    }

    public void setNom_complet(String nom_complet) {
        this.nom_complet = nom_complet;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}
