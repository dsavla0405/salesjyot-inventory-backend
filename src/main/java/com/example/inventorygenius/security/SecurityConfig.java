package com.example.inventorygenius.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
//		http.csrf(AbstractHttpConfigurer::disable)
//		.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/auth/google").permitAll() // allow your token endpoint
//                .anyRequest().authenticated()
//            );
////		.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
////		.oauth2Login(Customizer.withDefaults());
////		.oauth2Login(oauth2->oauth2.defaultSuccessUrl("http://localhost:3000"));
//		return http.build();
		System.out.println("---------------------heree in Security Config---------------");
		
		 http
		 .cors(withDefaults()) // this will use the above bean		    
	        .csrf(AbstractHttpConfigurer::disable)
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/api/auth/google", 
	                "/login/oauth2/**", 
	                "/oauth2/**", 
	                "/error", 
	                "/api/auth/logout",
	                "/public/**" // if needed
	                
	            ).permitAll()
	            .anyRequest().authenticated()
	        )
	        .oauth2Login(oauth2 -> oauth2
//	        		.successHandler(customOAuth2SuccessHandler(sessionRegistry()))
	            .defaultSuccessUrl("https://techjyot.up.railway.app/home", true) // redirect after Google login
	        )
	        .sessionManagement(session -> session
		            .maximumSessions(1)
		            .maxSessionsPreventsLogin(false) // OR false to kick the previous user out
		            .sessionRegistry(sessionRegistry())
//		            .expiredSessionStrategy(event -> {
//		                System.out.println("Session expired for user: " + event.getSessionInformation().getPrincipal());
//		            })
		        );
//	        .logout(logout -> logout
//	            .logoutSuccessUrl("http://localhost:3000") // go back to login page
//	            .invalidateHttpSession(true)
//	            .deleteCookies("JSESSIONID")
//	        )
	        
		 
		 
		 
	    return http.build();
		
		
	}
	
	
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}
	
//	@Bean
//	public AuthenticationSuccessHandler customOAuth2SuccessHandler(SessionRegistry sessionRegistry) {
//	    SessionAuthenticationStrategy sessionStrategy = sessionAuthenticationStrategy(sessionRegistry);
//	    System.out.println("sessionStrategy::"+sessionStrategy.toString());
//	    return (request, response, authentication) -> {
//	        HttpSession session = request.getSession(false);
//	        if (session != null) {
//	            sessionRegistry.registerNewSession(session.getId(), authentication.getPrincipal());
//	            sessionStrategy.onAuthentication(authentication, request, response); // 👈 Enforce max sessions
//	            System.out.println("✅ Registered and enforced session: " + session.getId());
//	            
//	        }
////	        response.sendRedirect("http://localhost:3000/home");
//	    };
//	}
//
//	@Bean
//	public SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
//	    return new RegisterSessionAuthenticationStrategy(sessionRegistry);
//	}



//	@Bean
//	public static HttpSessionEventPublisher httpSessionEventPublisher() {
//	    return new HttpSessionEventPublisher();
//	}
//    
	
	
	

}
