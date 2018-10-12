package sadba.lab.com.testplanete.Model;

import com.google.gson.annotations.SerializedName;

public class Ias {


    @SerializedName("code_cycle")
    private String code_cycle;
    @SerializedName("nom_cycle")
    private String nom_cycle;
    @SerializedName("statut_cycle")
    private String statut_cycle;
    @SerializedName("description")
    private String description;
    @SerializedName("bg")
    private String bg;
    @SerializedName("image_cycle")
    private String image_cycle;

    public Ias() {
    }

    public String getCode_cycle() {
        return code_cycle;
    }

    public void setCode_cycle(String code_cycle) {
        this.code_cycle = code_cycle;
    }

    public String getNom_cycle() {
        return nom_cycle;
    }

    public void setNom_cycle(String nom_cycle) {
        this.nom_cycle = nom_cycle;
    }

    public String getStatut_cycle() {
        return statut_cycle;
    }

    public void setStatut_cycle(String statut_cycle) {
        this.statut_cycle = statut_cycle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getImage_cycle() {
        return image_cycle;
    }

    public void setImage_cycle(String image_cycle) {
        this.image_cycle = image_cycle;
    }
}
