//package com.interswitch.Unsolorockets.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        prePostEnabled = true
//)
//public class AppConfig {
//
//    private final UnAuthorizedEntryPoint unAuthorizedEntryPoint;
//
//    public AppConfig(UnAuthorizedEntryPoint unAuthorizedEntryPoint) {
//        this.unAuthorizedEntryPoint = unAuthorizedEntryPoint;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .authorizeHttpRequests(authorize -> {
//                    try{
//                        authorize.antMatchers("/**/auth/**").permitAll()
//                                .antMatchers("/customError").permitAll()
//                                .antMatchers("/access-denied").permitAll()
//
//                                .anyRequest().authenticated()
//                                .and()
//                                .exceptionHandling().authenticationEntryPoint(unAuthorizedEntryPoint)
//                                .accessDeniedHandler(accessDeniedHandler())
//                                .and()
//                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                });
////        http.addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);
////        http.addFilterAfter(exceptionHandlerFilterBean(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
