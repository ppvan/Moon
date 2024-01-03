package vnu.uet.moonbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_songs")
public class UserAction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "song_id", referencedColumnName = "id")
  private Song song;

  @Enumerated(EnumType.STRING)
  @JoinColumn(name = "action_type")
  private ActionType actionType;

  public UserAction(User user, Song song, ActionType actionType) {
    this.user = user;
    this.song = song;
    this.actionType = actionType;
  }
}