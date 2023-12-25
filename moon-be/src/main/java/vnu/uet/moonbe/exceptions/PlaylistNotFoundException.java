package vnu.uet.moonbe.exceptions;

public class PlaylistNotFoundException extends RuntimeException{

  private static final long serialVersionUID = 3;

  public PlaylistNotFoundException(String message) {
    super(message);
  }
}
