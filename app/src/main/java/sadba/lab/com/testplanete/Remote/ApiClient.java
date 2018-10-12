package sadba.lab.com.testplanete.Remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // private static final String BASE_URL = "http://api.simendev.com/"; https://planeteapi.education.sn/
    private static final String BASE_URL = "https://api.education.sn/mobi-ien/";
    private static final String BASE_URL1 = "https://planeteapi.education.sn/";

    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;

    public static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofit1() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit1;
    }


}
