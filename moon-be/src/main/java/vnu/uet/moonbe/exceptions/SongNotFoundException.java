package vnu.uet.moonbe.exceptions;

public class SongNotFoundException extends RuntimeException{

  private static final long serialVersionUID = 2;

  public SongNotFoundException(String message) {
    super(message);
  }
}
