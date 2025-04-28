package kyulab.usersservice.handler;

import kyulab.usersservice.handler.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(ServiceUnavailabeExcpetion.class)
	public ResponseEntity<String> handleBadRequestException(ServiceUnavailabeExcpetion e) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException u) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(u.getMessage());
	}

	@ExceptionHandler(ConflictRequestException.class)
	public ResponseEntity<String> handleConflictRequestException(ConflictRequestException c) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(c.getMessage());
	}

	@ExceptionHandler(ServerErrorExcpetion.class)
	public ResponseEntity<String> handleServerErrorExcpetion(ServerErrorExcpetion s) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(s.getMessage());
	}

}
