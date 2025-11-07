package com.example.spring.dtos.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
