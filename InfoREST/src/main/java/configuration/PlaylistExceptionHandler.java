package configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class PlaylistExceptionHandler
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<String> handleConflict(Exception request) {
        return new ResponseEntity<String>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
