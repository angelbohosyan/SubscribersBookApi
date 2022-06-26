package com.example.subscribebook.beans;

import com.example.subscribebook.repositories.*;
import com.example.subscribebook.repositories.mysql.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Properties;

@Configuration
public class RepositoryBeans {

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        return new MySQLUserRepository(jdbcTemplate,transactionTemplate);
    }

    @Bean
    public UrlErrorRepository urlErrorRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLUrlErrorRepository(jdbcTemplate);
    }


    @Bean
    public ProfilePictureRepository profilePictureRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLProfilePictureRepository(jdbcTemplate);
    }

    @Bean
    public UserSaltRepository userSaltRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLUserSaltRepository(jdbcTemplate);
    }

    @Bean
    public UrlScopeRepository urlScopeRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLUrlScopeRepository(jdbcTemplate);
    }

    @Bean
    public SubscribeRepository subscribeRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLSubscribeRepository(jdbcTemplate);
    }

    @Bean
    public FriendRequestRepository friendRequestRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLFriendRequestRepository(jdbcTemplate);
    }

    @Bean
    public FriendsRepository friendsRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLFriendsRepository(jdbcTemplate);
    }


    @Bean
    public UrlRepository urlRepository(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate,UrlUrlResultsRepository urlUrlResultsRepository) {
        return new MySQLUrlRepository(jdbcTemplate,transactionTemplate,urlUrlResultsRepository);
    }


    @Bean
    public SeenUrlsRepository seenUrlsRepository(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        return new MySQLSeenUrlsRepository(jdbcTemplate,transactionTemplate);
    }

    @Bean
    public UrlUrlResultsRepository urlUrlResultsRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLUrlUrlResultsRepository(jdbcTemplate);
    }

    @Bean
    public NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLNewResultsForUrlWithUrlRepository(jdbcTemplate);
    }


}
