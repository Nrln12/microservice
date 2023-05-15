package com.bankofbaku.common.entities;

import com.bankofbaku.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="t_client")
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;
    private String username;
    private String password;
    private Gender gender;
    @Column(name = "birthdate", columnDefinition = "DATE")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthdate;
    private String name;
    private String surname;
    private String middlename;
    @OneToMany(mappedBy = "client")
    private List<Account> accounts;
}
