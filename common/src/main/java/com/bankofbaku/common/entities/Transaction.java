package com.bankofbaku.common.entities;

import com.bankofbaku.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_transaction")
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private LocalDateTime date;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name="sender_account_id")
    @JsonIgnore
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name="receiver_account_id")
    @JsonIgnore
    private Account receiverAccount;
}