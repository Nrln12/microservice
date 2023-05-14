package com.bankofbaku.common.models;

import com.bankofbaku.common.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class ClientDto {
    private String username;
    private String password;
    private Gender gender;
    private Date birthdate;
    private String name;
    private String surname;
    private String middlename;
}
