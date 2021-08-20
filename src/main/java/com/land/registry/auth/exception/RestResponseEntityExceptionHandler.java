package com.land.registry.auth.exception;

import java.io.IOException;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import org.apache.log4j.Logger;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = Logger.getLogger(ResponseEntityExceptionHandler.class);

  /**
   * In case of {@link IllegalArgumentException} is being thrown from codebase, the API will return
   * {@link ResponseEntity}
   *
   * @param e {@link IllegalStateException} caught exception
   * @return {@link ResponseEntity} with HTTP Status UNPROCESSABLE_ENTITY -  422
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<VndErrors> handleIllegalArgumentException(
      final IllegalArgumentException e) {

    return error(e, HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
  }

  /**
   * In case of {@link EntityExistsException} is being thrown from codebase, the API will return
   * {@link ResponseEntity}
   *
   * @param e {@link EntityExistsException} caught exception
   * @return {@link ResponseEntity} with HTTP Status CONFLICT -  409
   */
  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<VndErrors> handleEntityExistsException(final EntityExistsException e) {
    return error(e, HttpStatus.CONFLICT, e.getMessage());
  }

  /**
   * In case of {@link IOException} is being thrown from codebase, the API will return {@link
   * ResponseEntity}
   *
   * @param e {@link IOException} caught exception
   * @return {@link ResponseEntity} with HTTP Status INTERNAL_SERVER_ERROR -  500
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<VndErrors> handleIoException(final IOException e) {
    return error(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

  }

  /**
   * In case of {@link NullPointerException} is being thrown from codebase, the API will return
   * {@link ResponseEntity}
   *
   * @param e {@link NullPointerException} caught exception
   * @return {@link ResponseEntity} with HTTP Status BAD_REQUEST -  400
   */
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<VndErrors> handleNullPointerException(final NullPointerException e) {
    return error(e, HttpStatus.BAD_REQUEST, e.getMessage()); // 400
  }

  private ResponseEntity<VndErrors> error(final Exception exception, final HttpStatus httpStatus,
      final String logRef) {

    final String message = Optional.of(exception.getMessage())
        .orElse(exception.getClass().getSimpleName());

    LOGGER.error(exception.getMessage());

    return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
  }

}
