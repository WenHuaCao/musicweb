package buaa.cs.caowenhua.musicweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MusicwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicwebApplication.class, args);
    }

}
