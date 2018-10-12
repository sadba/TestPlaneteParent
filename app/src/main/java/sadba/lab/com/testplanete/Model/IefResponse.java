package sadba.lab.com.testplanete.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IefResponse {

    @SerializedName("code")
    private String code;
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("Structures")
    private ArrayList<Iefs> IefList;

    public IefResponse() {
    }

    public IefResponse(String code, String status, String message, ArrayList<Iefs> IefList) {
        this.code = code;
        this.status = status;
        this.message = message;
        IefList = IefList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Iefs> getIefList() {
        return IefList;
    }

    public void setIefList(ArrayList<Iefs> iefList) {
        IefList = iefList;
    }
}
