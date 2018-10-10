package ch.hsr.winescore.api;

import android.util.Log;
import ch.hsr.winescore.WineScoreApplication;

import java.io.File;
import java.io.IOException;

import ch.hsr.winescore.WineScoreConstants;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GWSClient {

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        File cacheDirectory = WineScoreApplication.getApplicationInstance().getCacheDir();
        File responsesCache = new File(cacheDirectory, "wsApiResponses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(responsesCache, cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)
                .addInterceptor(AUTHORIZATION_HEADER_INTERCEPTOR)
                .cache(cache)
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

    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 5000)
                    .build();
        } else {
            return originalResponse;
        }
    };

    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = chain -> {
        Request request = chain.request();
        if (!WineScoreApplication.hasNetwork()) {
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached")
                    .build();
        }
        return chain.proceed(request);
    };
}
