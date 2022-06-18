package com.example.subscribebook.beans;

import com.example.subscribebook.repositories.*;
import com.example.subscribebook.services.*;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class ServiceBeans {

    @Bean
    public GoogleApiService googleApiService() {
        return new GoogleApiService();
    }

    @Bean
    public MyUserDetailsService myUserDetailsService(UserRepository userRepository) {
        return new MyUserDetailsService(userRepository);
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
    public AccountService accountService(UserSaltRepository userSaltRepository, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, UserRepository userRepository,NotificationService notificationService,UrlRepository urlRepository,ProfilePictureRepository profilePictureRepository) {
        return new AccountService( userSaltRepository,  authenticationManager,  jwtTokenUtil,  userDetailsService,  userRepository, notificationService, urlRepository, profilePictureRepository);
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
