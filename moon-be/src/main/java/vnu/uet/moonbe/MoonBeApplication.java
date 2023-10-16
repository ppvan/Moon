package vnu.uet.moonbe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vnu.uet.moonbe.models.Playlist;
import vnu.uet.moonbe.repositories.PlaylistRepository;

@SpringBootApplication
public class MoonBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoonBeApplication.class, args);
    }

    @Bean
    public CommandLineRunner initValues(PlaylistRepository repository) {
        return (args) -> {

            Playlist demo1 = new Playlist(1L, "Playlist 1");
            Playlist demo2 = new Playlist(2L, "Playlist 2");
            Playlist demo3 = new Playlist(3L, "Playlist 3");
            Playlist demo4 = new Playlist(4L, "Playlist 4");
            repository.save(demo1);
            repository.save(demo2);
            repository.save(demo3);
            repository.save(demo4);
        };
    }

}
