package configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException;

@ControllerAdvice
public class PlaylistExceptionHandler
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<String> handleConflict(Exception exception) {
        return new ResponseEntity<String>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { UnauthorizedException.class })
    protected ResponseEntity<String> unauthorizedException(UnauthorizedException exception) {
        return new ResponseEntity<String>("Spotify session timed out!", HttpStatus.UNAUTHORIZED);
    }
}
