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
    POST_NOT_FOUND(4400, "Kullanıcıya ait böyle bir post bulunamadı.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(4600,"Token hatası" ,  HttpStatus.BAD_REQUEST),
    FOLLOW_ALREADY_EXIST(4700, "Böyle bir takip isteği daha önce oluşturulmuştur.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOLLOW(4800, "Kullanıcı kendisini takip edemez.", HttpStatus.BAD_REQUEST),
    PASSWORD_ERROR(4900, "Girdiğiniz şifre ile eski şifreniz uyuşmamaktadır.", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    HttpStatus httpStatus;
}
