package com.app.dashboard.dashboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComercioNoEncontradoException extends RuntimeException {
    public ComercioNoEncontradoException(String email) {
        super("Comercio con email '" + email + "' no encontrado");
    }
}
