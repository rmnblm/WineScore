package ch.hsr.winescore.data.api;

import java.io.File;
import java.util.UUID;

import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.WineScoreConstants;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GWSClient {

    private GWSClient() {
        throw new IllegalStateException("Static class");
    }

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(AUTHORIZATION_HEADER_INTERCEPTOR)
                .build();

        return new Retrofit.Builder()
                .baseUrl(WineScoreConstants.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * Get API Endpoint
     *
     * @return API Endpoint
     */
    public static GWSService getService() {
        return getRetrofitInstance().create(GWSService.class);
    }

    private static final Interceptor AUTHORIZATION_HEADER_INTERCEPTOR = chain -> {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest
                .newBuilder()
                .header("Authorization", "Token " + WineScoreConstants.API_KEY);
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    };
}
