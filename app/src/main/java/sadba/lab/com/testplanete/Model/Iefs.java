package sadba.lab.com.testplanete.Model;

import com.google.gson.annotations.SerializedName;

public class Iefs {

    @SerializedName("id")
    private String id;
    @SerializedName("libelle_structure")
    private String libelle_structure;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("date_creation")
    private String date_creation;
    @SerializedName("tel_str")
    private String tel_str;
    @SerializedName("email_str")
    private String email_str;
    @SerializedName("sigle")
    private String sigle;

    public Iefs() {
    }

    public Iefs(String id, String libelle_structure, String longitude, String latitude, String date_creation, String tel_str, String email_str, String sigle) {
        this.id = id;
        this.libelle_structure = libelle_structure;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date_creation = date_creation;
        this.tel_str = tel_str;
        this.email_str = email_str;
        this.sigle = sigle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle_structure() {
        return libelle_structure;
    }

    public void setLibelle_structure(String libelle_structure) {
        this.libelle_structure = libelle_structure;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getTel_str() {
        return tel_str;
    }

    public void setTel_str(String tel_str) {
        this.tel_str = tel_str;
    }

    public String getEmail_str() {
        return email_str;
    }

    public void setEmail_str(String email_str) {
        this.email_str = email_str;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }
}
