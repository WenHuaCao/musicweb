package buaa.cs.caowenhua.musicweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CaoWenHua
 * @create 2021-07-01  12:41
 */
@Configuration
public class UpLoadConFig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/upload/**").addResourceLocations("file:C:/Users/cc/IdeaProjects/musicweb/src/main/resources/static/upload/");
        }

}
