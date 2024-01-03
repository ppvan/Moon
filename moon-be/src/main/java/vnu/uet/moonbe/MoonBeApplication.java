package vnu.uet.moonbe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.repositories.SongRepository;

@SpringBootApplication
@EnableTransactionManagement
public class MoonBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoonBeApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initValues(SongRepository repository) {
//        return (args) -> {
//            Song song1 = new Song("Alone", "xhxbsvsgwbss");
//            Song song2 = new Song("Played", "xhxbsvsgwbss");
//            Song song3 = new Song("Ignite", "xhxbsvsgwbss");
//            Song song4 = new Song("Faded", "xhxbsvsgwbss");
//
//            repository.save(song1);
//            repository.save(song2);
//            repository.save(song3);
//            repository.save(song4);
//        };
//    }

}
