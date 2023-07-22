package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR(5100, "Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4000, "Parametre Hatası", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4300, "Bu kullanıcı zaten kayıtlı", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_CREATED(4100, "Kullanıcı oluşturulamadı", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4400, "Böyle bir kullanıcı bulunamadı", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(4600,"Token hatası" ,  HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    HttpStatus httpStatus;
}
