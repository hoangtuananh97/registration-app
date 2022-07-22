package com.example.registrationapp.web.util;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class GenericResponse<T> {
    private String message;
    private String error;

    private T item;
    private List<T> items;
    public GenericResponse(String message, String error) {
        super();
        this.message = message;
        this.error = error;
    }

    public GenericResponse(String message) {
        super();
        this.message = message;
    }

    public GenericResponse(String message, T item, String error) {
        super();
        this.error = error;
        this.message = message;
        this.item = item;
    }
    public GenericResponse(String message, List<T> items, String error){
        super();
        this.error = error;
        this.message = message;
        this.items = items;
    }

    public GenericResponse(List<ObjectError> allErrors, String error) {
        this.error = error;
        String temp = allErrors.stream().map(e -> {
            if (e instanceof FieldError) {
                return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            } else {
                return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            }
        }).collect(Collectors.joining(","));
        this.message = "[" + temp + "]";
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
