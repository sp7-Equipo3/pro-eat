package com.example.spring.dtos;

import org.springframework.stereotype.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequestDto {   

    private Long id;
    private String name;
    private String surname;
    private String email;




}
// This class is currently empty, but it can be used to define user request data transfer objects in the future.
