package com.physiquiz.student;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRemoteConfig {
    public interface Callback { void onResult(JSONObject json); }
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final String endpoint;

    public AppRemoteConfig(String baseUrl) {
        endpoint = baseUrl.replaceAll("/+$", "") + "/wp-json/physiquiz-app/v1/config";
    }

    public void load(Callback callback) {
        io.execute(() -> {
            JSONObject result = new JSONObject();
            try {
                HttpURLConnection c = (HttpURLConnection)new URL(endpoint).openConnection();
                c.setConnectTimeout(10000);
                c.setReadTimeout(10000);
                c.setRequestProperty("Accept", "application/json");
                BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder b = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) b.append(line);
                result = new JSONObject(b.toString());
                c.disconnect();
            } catch(Exception ignored) {}
            callback.onResult(result);
        });
    }
}
