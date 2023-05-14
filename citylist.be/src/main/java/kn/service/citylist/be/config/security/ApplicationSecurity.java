package kn.service.citylist.be.config.security;

import kn.service.citylist.be.config.CorsConfig;
import kn.service.citylist.be.properties.AppSecurityCredProperties;
import kn.service.citylist.be.properties.AppSecurityRoleProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class ApplicationSecurity {


    private final AppSecurityCredProperties appSecurityCredProperties;
    private final AppSecurityRoleProperties appSecurityRoleProperties;

    public ApplicationSecurity(AppSecurityCredProperties appSecurityCredProperties, AppSecurityRoleProperties appSecurityRoleProperties) {
        this.appSecurityCredProperties = appSecurityCredProperties;
        this.appSecurityRoleProperties = appSecurityRoleProperties;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        List<UserDetails> user = appSecurityCredProperties.getUser().keySet().stream()
                .map(userName -> User.builder()
                        .username(userName)
                        .password(getEncodePassword(appSecurityCredProperties.getUser().get(userName)))
                        .roles(appSecurityRoleProperties.getUser())
                        .build())
                .toList();
        List<UserDetails> admin = appSecurityCredProperties.getAdmin().keySet().stream()
                .map(userName -> User.builder()
                        .username(userName)
                        .password(getEncodePassword(appSecurityCredProperties.getAdmin().get(userName)))
                        .roles(appSecurityRoleProperties.getUser(), appSecurityRoleProperties.getAdmin())
                        .build())
                .toList();
        List<UserDetails> allUserDetails = new ArrayList<>(user);
        allUserDetails.addAll(admin);

        return new InMemoryUserDetailsManager(allUserDetails);
    }

    @Bean
    public SecurityFilterChain applicationBasicAuthFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(requestMatcherForAdmin()).hasRole(appSecurityRoleProperties.getAdmin())
                .requestMatchers(requestMatcherForUser()).hasRole(appSecurityRoleProperties.getUser())
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChainExcludeOauth(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .requestMatcher(requestMatcherForExcludeAuth())
                .authorizeRequests()
                .anyRequest()
                .permitAll();

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", Objects.requireNonNull(new CorsConfig().getCorsConfiguration(null)));
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private String getEncodePassword(String password) {
        return passwordEncoder().encode(password);
    }

    private OrRequestMatcher requestMatcherForExcludeAuth() {
        String[] WHITELIST_URI_PREFIXES = {
                "/img/**"
        };
        List<RequestMatcher> matcherList = Arrays.stream(WHITELIST_URI_PREFIXES)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        return new OrRequestMatcher(matcherList);
    }

    private OrRequestMatcher requestMatcherForUser() {
        String[] WHITELIST_URI_PREFIXES = {
                "/v1/**"
        };
        List<RequestMatcher> matcherList = Arrays.stream(WHITELIST_URI_PREFIXES)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        return new OrRequestMatcher(matcherList);
    }

    private OrRequestMatcher requestMatcherForAdmin() {
        String[] WHITELIST_URI_PREFIXES = {
                "/v1/admin/city-list"
        };
        List<RequestMatcher> matcherList = Arrays.stream(WHITELIST_URI_PREFIXES)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        return new OrRequestMatcher(matcherList);
    }

}
