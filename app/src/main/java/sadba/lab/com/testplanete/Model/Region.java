package sadba.lab.com.testplanete.Model;

import com.google.gson.annotations.SerializedName;

public class Region {


    private String code_region;
    private String libelle_region;

    public Region() {
    }

    public String getCode_region() {
        return code_region;
    }

    public void setCode_region(String code_region) {
        this.code_region = code_region;
    }

    public String getLibelle_region() {
        return libelle_region;
    }

    public void setLibelle_region(String libelle_region) {
        this.libelle_region = libelle_region;
    }
}
