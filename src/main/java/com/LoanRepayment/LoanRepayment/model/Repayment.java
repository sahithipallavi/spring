package com.LoanRepayment.LoanRepayment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Repayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repaymentId;

    private Long applicationId;

    private LocalDate dueDate;

    private double amountDue;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public enum PaymentStatus {
        PENDING,
        COMPLETED
    }
}