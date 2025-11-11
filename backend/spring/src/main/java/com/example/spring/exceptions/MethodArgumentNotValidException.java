package com.example.spring.exceptions;

public class MethodArgumentNotValidException extends RuntimeException {

   public MethodArgumentNotValidException( String message){
     super(message);
   }
}
