package kyulab.usersservice.handler;

import kyulab.usersservice.common.BasicResponse;
import kyulab.usersservice.handler.exception.BadRequestException;
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
	public ResponseEntity<BasicResponse<Object>> handleNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BasicResponse<>(e.getMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<BasicResponse<Object>> handleBadRequestException(BadRequestException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BasicResponse<>(e.getMessage()));
	}

}
