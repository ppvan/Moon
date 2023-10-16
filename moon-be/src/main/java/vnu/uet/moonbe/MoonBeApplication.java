package vnu.uet.moonbe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vnu.uet.moonbe.repositories.SongRepository;

@SpringBootApplication
public class MoonBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoonBeApplication.class, args);
    }

    @Bean
    public CommandLineRunner initValues(SongRepository repository) {
        return (args) -> {

        };
    }

}
