package ch.hsr.winescore.api;

import ch.hsr.winescore.model.WineList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;

public class ApiClient {

    private static final String ROOT_URL = "https://api.globalwinescore.com/globalwinescores/";
    private static final String API_KEY = "03a6a975ed86c26d1a3d791571ef9c8df080c5c6";

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(getHeaderInterceptor());
        OkHttpClient customClient = builder.build();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(customClient)
                .build();
    }

    /**
     * Get Authentication Interceptor
     */
    private static Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Token " + API_KEY)
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }

    /**
     * Get API Endpoint
     *
     * @return API Endpoint
     */
    public static GWSEndpointInterface getEndpointInterface() {
        return getRetrofitInstance().create(GWSEndpointInterface.class);
    }

    public interface GWSEndpointInterface {

        @GET("latest")
        Call<WineList> getLatest();

        @GET("latest")
        Call<WineList> getByCountry(@Query("country") String country);

    }
}
