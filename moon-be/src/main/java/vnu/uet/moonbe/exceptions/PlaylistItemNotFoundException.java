package vnu.uet.moonbe.exceptions;

public class PlaylistItemNotFoundException extends RuntimeException{

  private static final long serialVersionUID = 4;

  public PlaylistItemNotFoundException(String message) {
    super(message);
  }
}
