package buaa.cs.caowenhua.musicweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author CaoWenHua
 * @create 2021-06-25  10:11
 */
@Configuration
public class AuthWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/register","/css/**","/bootstrap/**","/fontawesome/**","/fonts/**",
                        "/images/**","/jquery/**","/js/**");

    }
}
