package com.example.subscribebook.beans;

import com.example.subscribebook.repositories.*;
import com.example.subscribebook.services.*;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class ServiceBeans {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.subscribebook")).build();
    }

    @Bean
    public GoogleApiService googleApiService() {
        return new GoogleApiService();
    }

    @Bean
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    @Bean
    public MailService mailService() {
        return new MailService();
    }

    @Bean
    public MyUserDetailsService myUserDetailsService(UserRepository userRepository) {
        return new MyUserDetailsService(userRepository);
    }

    @Bean
    public DecodeService decodeService() {
        return new DecodeService();
    }

    @Bean
    public NotificationService notificationService(SubscribeRepository subscribeRepository,
                                                   UrlRepository urlRepository,
                                                   UrlScopeRepository urlScopeRepository,
                                                   FriendsRepository friendsRepository,
                                                   NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository,
                                                   SeenUrlsRepository seenUrlsRepository) {
        return new NotificationService(subscribeRepository, urlRepository,
                urlScopeRepository,friendsRepository,
                newResultsForUrlWithUrlRepository, seenUrlsRepository);
    }

    @Bean
    public AccountService accountService(MailService mailService,UserSaltRepository userSaltRepository, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, UserRepository userRepository,NotificationService notificationService,UrlUrlResultsRepository urlUrlResultsRepository,UrlRepository urlRepository,ProfilePictureRepository profilePictureRepository) {
        return new AccountService(mailService, userSaltRepository,  authenticationManager,  jwtTokenUtil,  userDetailsService,  userRepository, notificationService,profilePictureRepository, urlUrlResultsRepository,urlRepository);
    }

    @Bean
    public GoogleUrlService googleUrlService(NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository, UrlRepository urlRepository,NotificationService notificationService) {
        return new GoogleUrlService(newResultsForUrlWithUrlRepository,  urlRepository, notificationService);
    }

    @Bean
    public SubscribeService subscribeService(UserRepository userRepository, SubscribeRepository subscribeRepository) {
        return new SubscribeService(userRepository,subscribeRepository);
    }

    @Bean
    public FriendsService friendsService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, FriendRequestRepository friendRequestRepository, FriendsRepository friendsRepository) {
        return new FriendsService( userRepository,  jwtTokenUtil,  friendRequestRepository,  friendsRepository);
    }

}
