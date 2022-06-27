package com.example.subscribebook.controller.GoogleUrlController;

import com.example.subscribebook.repositories.*;
import com.example.subscribebook.services.GoogleApiService;
import com.example.subscribebook.services.GoogleUrlService;
import com.example.subscribebook.services.NotificationService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private  GoogleUrlService googleUrlService;

    @Autowired
    private  NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository;

    @Autowired
    private  UrlRepository urlRepository;

    @Autowired
    private  UrlUrlResultsRepository urlUrlResultsRepository;

    @Autowired
    private  UrlScopeRepository urlScopeRepository;

    @Autowired
    private  SeenUrlsRepository seenUrlsRepository;

    @Autowired
    private  NotificationService notificationService;

    @Autowired
    private GoogleApiService googleApiService;

    @Test
    public void createFriend_pass_than_removeFriend_pass() throws JSONException, IOException, InterruptedException {
        String url = "facebook";
        JSONObject image = new JSONObject();
        image.put("url",url);
        String testMapping = "url";
        HttpHeaders headers = new HttpHeaders();
        String token =jwtTokenUtil.generateToken(new User("angelcho3","parola za angelcho3",new ArrayList<>()));
        headers.set("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(image.toString(),headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/" + testMapping,
                HttpMethod.POST,
                httpEntity,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        TimeUnit.SECONDS.sleep(3);

        JSONObject urlWithUrl = new JSONObject();
        urlWithUrl.put("resultUrl",url);
        urlWithUrl.put("url","https://www.speedtest.net/");
        System.out.println(urlWithUrl.toString());

        httpEntity = new HttpEntity<>(urlWithUrl.toString(),headers);
        response = restTemplate.exchange(
                "http://localhost:" + port + "/" + "userNotFoundUrl",
                HttpMethod.POST,
                httpEntity,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        com.example.subscribebook.models.User user = userRepository.getUserWithName("angelcho3");
        int urlId = urlRepository.getIdByUserIdAndUrl(user.getId(),"facebook");
        List<Integer> list = new ArrayList<>();
        list.add(urlId);
        urlUrlResultsRepository.deleteUrlUrlResult(new ArrayList<>(list));
        urlRepository.deleteUrlById(urlId);

    }

    @Test
    public void userFoundUrl_pass() throws JSONException, InterruptedException, IOException {
        String url = "facebook";
        JSONObject image = new JSONObject();
        image.put("url",url);
        String testMapping = "url";
        HttpHeaders headers = new HttpHeaders();
        String token =jwtTokenUtil.generateToken(new User("angelcho3","parola za angelcho3",new ArrayList<>()));
        headers.set("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(image.toString(),headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/" + testMapping,
                HttpMethod.POST,
                httpEntity,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        TimeUnit.SECONDS.sleep(3);


        JSONObject urlWithUrl = new JSONObject();
        urlWithUrl.put("resultUrl",url);
        urlWithUrl.put("url","https://www.speedtest.net/");
//        urlWithUrl.put("url","https://www.facebook.com/");
        httpEntity = new HttpEntity<>(urlWithUrl.toString(),headers);
        response = restTemplate.exchange(
                "http://localhost:" + port + "/" + "userFoundUrl",
                HttpMethod.POST,
                httpEntity,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

//        com.example.subscribebook.models.User user = userRepository.getUserWithName("angelcho3");
//        System.out.println(user.getId());
//        int urlId = urlRepository.getIdByUserIdAndUrl(user.getId(),"facebook");
//        urlUrlResultsRepository.deleteUrlUrlResult(new ArrayList<>(urlId));
//        urlRepository.deleteUrlById(urlId);
    }
}
