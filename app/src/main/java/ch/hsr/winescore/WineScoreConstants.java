package ch.hsr.winescore;

public class WineScoreConstants {

    private static String rootUrl = "https://api.globalwinescore.com/globalwinescores/";
    private static String apiKey = "03a6a975ed86c26d1a3d791571ef9c8df080c5c6";

    private WineScoreConstants() {
        throw new IllegalStateException("Static class");
    }

    public static String getRootUrl() {
        return rootUrl;
    }

    public static void setRootUrl(String rootUrl) {
        WineScoreConstants.rootUrl = rootUrl;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String apiKey) {
        WineScoreConstants.apiKey = apiKey;
    }
}
