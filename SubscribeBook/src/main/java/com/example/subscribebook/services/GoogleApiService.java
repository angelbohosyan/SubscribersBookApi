package com.example.subscribebook.services;

import com.example.subscribebook.Properties;
import com.example.subscribebook.exceptions.GoogleApiExceptions.GoogleApiRequestError;
import com.example.subscribebook.exceptions.GoogleApiExceptions.GoogleApiNotAuthorizedException;
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
        String key= Properties.key;
        HttpURLConnection con = null;
        try {
            URL url = new URL(String.format("https://serpapi.com/search.json?q=%s&location=Austin,+Texas,+United+States&hl=en&gl=us&google_domain=google.com&api_key=%s", urlRequested,key));
            System.out.println(url);
            con = (HttpURLConnection) url.openConnection();
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
            JSONArray statistics;
            try {
                statistics = json.getJSONArray("organic_results");
            } catch (Exception e) {
                statistics = new JSONArray();
            }

            HashSet<String> set = new HashSet<>();
            for (int i = 0; i < statistics.length(); i++) {
                set.add(statistics.getJSONObject(i).getString("link"));
            }
            return set;
        } catch (IOException e) {
            if (con!=null&&con.getResponseCode() == 400) {
                throw new GoogleApiRequestError();
            }
            if(con!=null&&con.getResponseCode() == 401) {
                throw new GoogleApiNotAuthorizedException();
            }
            throw new IOException();
        }
    }

}
