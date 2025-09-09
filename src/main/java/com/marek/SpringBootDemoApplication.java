package com.marek;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@RestController
public class SpringBootDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootDemoApplication.class, args);
  }

  //    @Bean
  //    public WebMvcConfigurer corsConfigurer() {
  //        return new WebMvcConfigurer() {
  //            @Override
  //            public void addCorsMappings(@NonNull CorsRegistry registry) {
  //                registry.addMapping("/**")
  //                        .allowedOrigins("http://localhost:3000") // Your React app's URL
  //                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
  //                        .allowedHeaders("*")
  //                        .exposedHeaders("Authorization")
  //                        .allowCredentials(true);
  //            }
  //        };
  //    }

}
