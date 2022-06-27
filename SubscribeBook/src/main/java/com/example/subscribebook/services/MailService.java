package com.example.subscribebook.services;


import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MailService {
    private static String DOMAIN = "sandboxd990e8b8d7ff4a86946680956a92c5bc.mailgun.org";

    public void sendMail(String subject,String body,String recipients) {
        try {
            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("from", "angelbohosyan@gmail.com"));
            form.add(new BasicNameValuePair("to", recipients));
            form.add(new BasicNameValuePair("subject", subject));
            form.add(new BasicNameValuePair("text", body));
            postMessage(String.format("https://api.mailgun.net/v3/%s/messages", DOMAIN), form);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String postMessage(String URL,List<NameValuePair> form) throws Exception {
        if(form==null||URL==null||URL.trim().length()==0) return "";
        StringBuilder stringBuilder = new StringBuilder();

        org.apache.http.client.HttpClient client = buildHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        HttpResponse response = null;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            response = client.execute(httpPost);
            stringBuilder.append(getBody(response.getEntity().getContent()));
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                System.out.println("post mailgun messages ok:"+URL);
            } else {
                System.out.println("Failed to post mailgun,status=" + statusCode);
                throw new RuntimeException("post failed:"+URL);
            }
        } catch (Exception e) {
            throw e;
        }finally {
            if(response!=null&&response.getEntity()!=null) {
                response.getEntity().consumeContent();
            }
        }
        return stringBuilder.toString();
    }

    private String getBody(InputStream inputStream) {
        String result = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    inputStream));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
                result += "\n";
            }
            in.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return result;
    }

    private HttpClient buildHttpClient() {
        return HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(getAuthProvider())
                .build();
    }

    private CredentialsProvider getAuthProvider() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("api", "c0fa20fc35766cb732d388aaaefd4752-50f43e91-e8a7c2eb");
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }
}
