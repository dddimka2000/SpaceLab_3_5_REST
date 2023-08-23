package org.example.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class UserDTO {

    @Size(min = 6, max = 15, message = "Логин должен быть от 6 до 15 символов.")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Логин должен содержать только буквы a-z A-Z, цифры 0-9 и символ подчеркивания \"_\".")
    public String login;

//    @Size(min = 8, max = 30, message = "Пароль должен быть от 8 до 30 символов")
    private String pass;

    @Size(min = 2, max = 30, message = "Имя должен быть от 2 до 30 символов")
    private String name;

    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов")
    private String surname;

    @Size(min = 10, max = 15, message ="Номер должен быть от 10 до 15 символов.")
    @Pattern(regexp = "\\+?[0-9]+", message = "Номер телефона должен начинаться с '+' и содержать только цифры.")
    private String telephone;

    @Email(message = "E-mail не валиден. Пример почты \"example@pre.com\".")
    @Size(min = 5, max = 30,message = "E-mail  должен быть 5-30 символов")
    private String email;

    private String path;

}


