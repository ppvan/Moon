package vnu.uet.moonbe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorObject> handleEmailAlreadyExistsException(
      EmailAlreadyExistsException e,
      WebRequest request
  ) {
    ErrorObject errorObject = new ErrorObject();
    errorObject.setStatusCode(HttpStatus.CONFLICT.value());
    errorObject.setMessage(e.getMessage());

    return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(SongNotFoundException.class)
  public ResponseEntity<ErrorObject> handleSongNotFoundException(
      SongNotFoundException e,
      WebRequest request
  ) {
    ErrorObject errorObject = new ErrorObject();
    errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
    errorObject.setMessage(e.getMessage());

    return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
  }

	@ExceptionHandler(PlaylistNotFoundException.class)
	public ResponseEntity<ErrorObject> handlePlaylistNotFoundException(
			PlaylistNotFoundException e,
			WebRequest request
	) {
		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(e.getMessage());

		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PlaylistItemNotFoundException.class)
	public ResponseEntity<ErrorObject> handlePlaylistItemNotFoundException(
			PlaylistItemNotFoundException e,
			WebRequest request
	) {
		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(e.getMessage());

		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	}
}