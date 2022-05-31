package com.bmveiga.projects.gestaovendas.excecao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GestaoVendasExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String LENGTH = "Length";
	private static final String NOT_BLANK = "NotBlank";
	private static final String NOT_NULL = "NotNull";

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Error> erros = gerarListaErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request){
		String msgUser = "Recurso não encontrado";
		String msgDev = ex.toString();
		List<Error> errors = Arrays.asList(new Error(msgUser, msgDev));
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(JpaObjectRetrievalFailureException.class)
	public ResponseEntity<Object> handleJpaObjectRetrievalFailureException(JpaObjectRetrievalFailureException ex, WebRequest request){
		String msgUser = "Recurso não encontrado";
		String msgDev = ex.toString();
		List<Error> errors = Arrays.asList(new Error(msgUser, msgDev));
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(RegraNegocioException.class)
	public ResponseEntity<Object> handlerRegraNegocioException(RegraNegocioException ex,WebRequest request){
		String msgUser = ex.getMessage();
		String msgDev = ex.getMessage();
		List<Error> errors = Arrays.asList(new Error(msgUser, msgDev));
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	private List<Error> gerarListaErros(BindingResult bindingResult) {
		List<Error> errors = new ArrayList<>();
		bindingResult.getFieldErrors().forEach(fieldError -> {
			String msgUser = tratarMsgErroUser(fieldError);
			String msgDev = fieldError.toString();
			errors.add(new Error(msgUser, msgDev));
		});
		return errors;
	}

	private String tratarMsgErroUser(FieldError fieldError) {
		if (fieldError.getCode().equals(NOT_BLANK)) {
			return fieldError.getDefaultMessage().concat(" é obrigatório");
		}
		if (fieldError.getCode().equals(NOT_NULL)) {
			return fieldError.getDefaultMessage().concat(" é obrigatório");
		}
		if (fieldError.getCode().equals(LENGTH)) {
			return fieldError.getDefaultMessage().concat(String.format(" deve ter entre %s e %s caracteres.",
					fieldError.getArguments()[2], fieldError.getArguments()[1]));
		}
		return fieldError.toString();
	}
}
