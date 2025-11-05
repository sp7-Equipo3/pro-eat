package com.example.spring.dtos;

import org.springframework.stereotype.Indexed;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class UserRequestDto {   

    
    private String name;
    private String surname;



}
// This class is currently empty, but it can be used to define user request data transfer objects in the future.
