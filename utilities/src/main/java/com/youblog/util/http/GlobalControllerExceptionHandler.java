package com.youblog.util.http;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.youblog.util.exceptions.InternalApplicationException;
import com.youblog.util.exceptions.InvalidInputException;
import com.youblog.util.exceptions.NotFoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

	@ResponseStatus(NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public @ResponseBody HttpErrorInfo handleNotFoundExceptions(ServerHttpRequest request, Exception ex) {
		return createHttpErrorInfo(NOT_FOUND, request, ex);
	}

	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(InvalidInputException.class)
	public @ResponseBody HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, Exception ex) {
		return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, ex);
	}
	
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(InternalApplicationException.class)
	public @ResponseBody HttpErrorInfo handleInternalApplicationException(ServerHttpRequest request, Exception ex) {
		return createHttpErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, request, ex);
	}


	private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
		final String path = request.getPath().pathWithinApplication().value();
		final String message = ex.getMessage();
		LOG.debug("Returning Http status: {} from path: {}, message: {}", httpStatus, path, message);
		return new HttpErrorInfo(httpStatus, path, message);
	}
}
