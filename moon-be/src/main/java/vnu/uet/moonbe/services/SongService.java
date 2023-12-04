package vnu.uet.moonbe.services;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vnu.uet.moonbe.dto.ResponseDto;
import vnu.uet.moonbe.dto.SongDTO;
import vnu.uet.moonbe.exceptions.SongNotFoundException;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.models.User;
import vnu.uet.moonbe.repositories.SongRepository;
import vnu.uet.moonbe.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SongService {

    private final UserRepository userRepository;

    private final SongRepository songRepository;

    public List<Song> findAllSongs() {
        return Collections.emptyList();
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public List<Song> getSongsByTitle(String keyword) {
        return songRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Song> getSongsByArtist(String keyword) {
        return songRepository.findByArtistContainingIgnoreCase(keyword);
    }

    public List<Song> getSongsByGenre(String keyword) {
        return songRepository.findByGenreContainingIgnoreCase(keyword);
    }

    public Song getSongById(int id) {
        Song song = songRepository.findById(id)
            .orElseThrow(() -> new SongNotFoundException("Song could not be found"));
        return song;
    }

    public ResponseEntity<?> addSong(SongDTO songDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();

        if (songDTO == null) {
            throw new SongNotFoundException("No song details found");
        }

        if (StringUtils.isBlank(songDTO.getTitle()) ||
            StringUtils.isBlank(songDTO.getArtist()) ||
            StringUtils.isBlank(songDTO.getGenre()) ||
            StringUtils.isBlank(songDTO.getFilePath())
        ) {
            throw new SongNotFoundException("No song details found");
        }

        Song newSong = new Song(
            songDTO.getId(),
            songDTO.getTitle(),
            songDTO.getArtist(),
            songDTO.getGenre(),
            songDTO.getFilePath()
        );

//        newSong.addUser(user, "UPLOAD");

        songRepository.save(newSong);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.CREATED.value());
        responseDto.setMessage("Song added success");

        return ResponseEntity.ok(responseDto);
    }

    public ResponseEntity<?> updateSong(int id, SongDTO songDTO) {
        Song song = songRepository.findById(id)
            .orElseThrow(() -> new SongNotFoundException("Song could not be updated"));

        song.setTitle(songDTO.getTitle());
        song.setArtist(songDTO.getArtist());
        song.setGenre(songDTO.getArtist());
        song.setFilePath(songDTO.getFilePath());

        songRepository.save(song);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.OK.value());
        responseDto.setMessage("Song updated success");

        return ResponseEntity.ok(responseDto);
    }

    public ResponseEntity<?> deleteSong(int id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song could not be deleted"));
        songRepository.delete(song);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.NO_CONTENT.value());
        responseDto.setMessage("Song deleted success");

        return ResponseEntity.ok(responseDto);
    }
}