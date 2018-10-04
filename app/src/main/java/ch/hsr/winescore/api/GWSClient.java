package ch.hsr.winescore.api;

import android.util.Log;
import ch.hsr.winescore.WineScoreApplication;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GWSClient {

    private static final String ROOT_URL = "https://api.globalwinescore.com/globalwinescores/";
    private static final String API_KEY = "03a6a975ed86c26d1a3d791571ef9c8df080c5c6";

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new AuthorizationHeaderInterceptor())
                .addInterceptor(new OfflineCacheInterceptor())
                .cache(new Cache(new File(
                        WineScoreApplication.getApplicationInstance().getCacheDir(),
                        "wsApiResponses"),
                        5 * 1024 * 1024) // 5 MB
                )
                .build();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
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

    private static class AuthorizationHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest
                    .newBuilder()
                    .header("Authorization", "Token " + API_KEY);
            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }

    private static class ResponseCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-size=" + 60)
                    .build();
        }
    }

    private static class OfflineCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!WineScoreApplication.hasNetwork()) {
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + (60 * 60 * 24 * 7)) // 1 Week
                        .build();
                Log.d("API", "New offline cache stored");
            }
            return chain.proceed(request);
        }
    }
}
