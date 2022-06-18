package com.example.subscribebook.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

public class GoogleApiService {

    public HashSet<String> getGoogleLinks(String urlRequested) throws IOException {
        URL url = new URL(String.format("https://serpapi.com/search.json?q=%s&location=Austin,+Texas,+United+States&hl=en&gl=us&google_domain=google.com&api_key=fac3d3b58cb39fc068f74c4d978cd3d66db5c74e6b03beb9c08a15d92248df1a", urlRequested));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();


        JSONObject json = new JSONObject(content.toString().trim());
        JSONArray statistics = json.getJSONArray("organic_results");

        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < statistics.length(); i++) {
            set.add(statistics.getJSONObject(i).getString("link"));
        }
        return set;
    }

}
