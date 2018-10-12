package sadba.lab.com.testplanete.Common;


import sadba.lab.com.testplanete.Remote.IMyAPI;
import sadba.lab.com.testplanete.Remote.RetrofitClient;

public class Common {

    public static final String BASE_URL = "https://api.education.sn/mobi-ien/";

    public static IMyAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }
}
