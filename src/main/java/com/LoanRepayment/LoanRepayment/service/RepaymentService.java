package com.LoanRepayment.LoanRepayment.service;

import com.LoanRepayment.LoanRepayment.model.Repayment;
import com.LoanRepayment.LoanRepayment.repository.RepaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RepaymentService {
    @Autowired
    private RepaymentRepository repaymentRepository;

    public List<Repayment> getRepaymentSchedule(Long applicationId) {
        return repaymentRepository.findAllByApplicationId(applicationId);
    }

    public Repayment makePayment(Long repaymentId, LocalDate paymentDate) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElse(null);
        if (repayment != null && repayment.getPaymentStatus() == Repayment.PaymentStatus.PENDING) {
            repayment.setPaymentDate(paymentDate);
            repayment.setPaymentStatus(Repayment.PaymentStatus.COMPLETED);
            return repaymentRepository.save(repayment);
        }
        return null;
    }

    public double getOutstandingBalance(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);
        double outstandingBalance = 0;
        for (Repayment repayment : repayments) {
            if (repayment.getPaymentStatus() == Repayment.PaymentStatus.PENDING) {
                outstandingBalance += repayment.getAmountDue();
            }
        }
        return outstandingBalance;
    }

    public List<Repayment> generateRepaymentSchedule(Long applicationId, double loanAmount, int numberOfInstallments, int daysBetweenInstallments) {

        // 1. Delete existing repayments for the application ID
        repaymentRepository.deleteByApplicationId(applicationId);

        // 2. Generate and save the new schedule
        List<Repayment> schedule = new java.util.ArrayList<>();
        double amountPerInstallment = loanAmount / numberOfInstallments;
        LocalDate dueDate = LocalDate.now().plusDays(daysBetweenInstallments);

        for (int i = 0; i < numberOfInstallments; i++) {
            Repayment repayment = new Repayment();
            repayment.setApplicationId(applicationId);
            repayment.setDueDate(dueDate);
            repayment.setAmountDue(amountPerInstallment);
            repayment.setPaymentStatus(Repayment.PaymentStatus.PENDING);
            schedule.add(repayment);
            dueDate = dueDate.plusDays(daysBetweenInstallments);
        }
        return repaymentRepository.saveAll(schedule);
    }
}