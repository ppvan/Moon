package vnu.uet.moonbe.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = 1;

  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
