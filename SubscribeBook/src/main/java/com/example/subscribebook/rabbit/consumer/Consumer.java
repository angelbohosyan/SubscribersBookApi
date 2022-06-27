package com.example.subscribebook.rabbit.consumer;

import com.example.subscribebook.exceptions.GoogleApiExceptions.GoogleApiNotAuthorizedException;
import com.example.subscribebook.exceptions.GoogleApiExceptions.GoogleApiRequestError;
import com.example.subscribebook.models.UrlDAO;
import com.example.subscribebook.rabbit.config.MessagingConfig;
import com.example.subscribebook.repositories.UrlErrorRepository;
import com.example.subscribebook.repositories.UrlRepository;
import com.example.subscribebook.repositories.UrlUrlResultsRepository;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.GoogleApiService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Consumer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GoogleApiService googleApiService;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlUrlResultsRepository urlUrlResultsRepository;

    @Autowired
    private UrlErrorRepository urlErrorRepository;

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(UrlDAO urlDAO) throws IOException {
        work(urlDAO);
    }
    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue2(UrlDAO urlDAO) throws IOException {
        work(urlDAO);
    }
    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue3(UrlDAO urlDAO) throws IOException {
        work(urlDAO);
    }
    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue4(UrlDAO urlDAO) throws IOException {
        work(urlDAO);
    }
    private void work(UrlDAO urlDAO) throws IOException {
        try {
            System.out.println("Message received");
            HashSet<String> result = googleApiService.getGoogleLinks(urlDAO.getUrl());
            System.out.println(result);
            urlUrlResultsRepository.createMultipleUrlUrlResults(result.stream().toList(), urlDAO.getId());
            System.out.println("Message finished");
            System.out.println();
        } catch (GoogleApiNotAuthorizedException e) {
            rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY,urlDAO);
            System.out.println("I need token");
        } catch (GoogleApiRequestError e) {
            urlRepository.deleteUrlById(urlDAO.getId());
            urlErrorRepository.createUrlError(urlDAO.getUrl());
            System.out.println("Wrong Url To The DataBase");
        } catch (IOException e) {
            System.out.println("Something went wrong");
            urlRepository.deleteUrlById(urlDAO.getId());
        }
    }
}
