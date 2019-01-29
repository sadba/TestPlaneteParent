package sadba.lab.com.testplanete.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IaResponse {
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;
    @SerializedName("records")
    private ArrayList<Ias> IasList;

    public IaResponse() {
    }

    public IaResponse(String code, String status, String message, ArrayList<Ias> iasList) {
        this.code = code;
        this.message = message;
        IasList = iasList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Ias> getIasList() {
        return IasList;
    }

    public void setIasList(ArrayList<Ias> iasList) {
        IasList = iasList;
    }
}
