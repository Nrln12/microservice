package com.bankofbaku.common.entities;

import com.bankofbaku.common.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_account")
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;
    private Long accountCode;
    private LocalDateTime openDate;
    private Double balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
//    @ManyToOne(optional = false,cascade = CascadeType.ALL)
//    @JoinColumn(name = "client_id")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy="senderAccount", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Transaction> senders;

    @OneToMany(mappedBy="receiverAccount", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Transaction> receivers;
}