package vnu.uet.moonbe.models;

//import com.alibou.security.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

//  @Builder.Default
//  @OneToMany(mappedBy = "user")
//  private List<UserSongMapping> userSongMappings = new ArrayList<>();

//  @Builder.Default
//  @ManyToMany
//  @JoinTable(
//      name = "user_songs",
//      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//      inverseJoinColumns = {
//          @JoinColumn(name = "song_id", referencedColumnName = "id"),
//          @JoinColumn(name = "type_service", referencedColumnName = "type_service")
//      }
//  )
//  private List<Song> songs = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

//  public void addSong(Song song, String typeService) {
//    UserSongMapping userSongMapping = new UserSongMapping();
//    userSongMapping.setSong(song);
//    userSongMapping.setTypeService(typeService);
//
//    song.getUserSongMappings().add(userSongMapping);
//    userSongMapping.setUser(this);
//
////    songs.add(song);
////    song.getUsers().add(this);
//  }
}
