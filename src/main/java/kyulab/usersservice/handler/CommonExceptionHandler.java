package kyulab.usersservice.handler;

import kyulab.usersservice.handler.exception.BadRequestException;
import kyulab.usersservice.handler.exception.ServiceUnabailabeExcpetion;
import kyulab.usersservice.handler.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(ServiceUnabailabeExcpetion.class)
	public ResponseEntity<String> handleBadRequestException(ServiceUnabailabeExcpetion e) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
	}

}
