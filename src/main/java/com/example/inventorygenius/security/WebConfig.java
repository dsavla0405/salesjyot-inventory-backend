package com.example.inventorygenius.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;





@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000") //http://localhost:3000
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
        
        System.out.println("---------------------heree in Web Config 111---------------");
        
        
    }
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
		System.out.println("---------------------heree in web Config 22222---------------");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);  // boxed Boolean

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
// ------------Use to log request coming from front end ---------------	
//	@Bean
//	public CommonsRequestLoggingFilter requestLoggingFilter() {
//		System.out.println("---------------------heree in web Config 33333---------------");
//		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
//		loggingFilter.setIncludeHeaders(true);
//	    loggingFilter.setIncludePayload(true);
//	    loggingFilter.setMaxPayloadLength(10000);
//	    loggingFilter.setAfterMessagePrefix("REQUEST DATA : ");
//	    return loggingFilter;
//	}
	
	
    
    
}