package vnu.uet.moonbe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vnu.uet.moonbe.dto.SongDTO;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.repositories.SongRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class SongService {


    private final SongRepository repository;

    public SongService(@Autowired SongRepository repository) {
        this.repository = repository;
    }

    public List<Song> findAllSongs() {
        return Collections.emptyList();
    }

    public void addSong(SongDTO song) {

    }

    public void updateSong(SongDTO song) {

    }

    public void deleteSong(long id) {

    }


}
