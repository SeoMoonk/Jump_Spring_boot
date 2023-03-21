package com.mysite.sbb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//404 오류(Not Found Error) 가 뜬 상태가 주어진다면.
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DataNotFoundException(String message)
    {
        super(message);
    }

}
