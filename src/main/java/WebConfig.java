import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")  // Cho phép frontend truy cập
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")  // Cho phép OPTIONS request
                        .allowedHeaders("*")  // Cho phép tất cả headers, bao gồm Authorization
                        .allowCredentials(true);  // Cho phép gửi cookies/token
            }
        };
    }
}
