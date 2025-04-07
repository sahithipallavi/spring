package com.LoanRepayment.LoanRepayment.controller;

import com.LoanRepayment.LoanRepayment.model.Repayment;
import com.LoanRepayment.LoanRepayment.service.RepaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/repayments")
public class RepaymentController {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentController.class);

    @Autowired
    private RepaymentService repaymentService;


    @GetMapping("/schedule/{applicationId}")
    public ResponseEntity<List<Repayment>> getRepaymentSchedule(@PathVariable Long applicationId) {
        logger.info("Getting repayment schedule for applicationId: {}", applicationId);
        List<Repayment> schedule = repaymentService.getRepaymentSchedule(applicationId);
        if (schedule==null || schedule.isEmpty()) {
            logger.warn("No repayment schedule found for applicationId: {}", applicationId);
            return ResponseEntity.notFound().build();
        }
        logger.info("Repayment schedule retrieved successfully for applicationId: {}", applicationId);
        return ResponseEntity.ok(schedule);
    }

    @PostMapping("/payment/{repaymentId}")
    public ResponseEntity<Repayment> makePayment(@PathVariable Long repaymentId,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate) {
        logger.info("Making payment for repaymentId: {}, paymentDate: {}", repaymentId, paymentDate);
        Repayment payment = repaymentService.makePayment(repaymentId, paymentDate);
        if (payment != null) {
            logger.info("Payment processed successfully for repaymentId: {}", repaymentId);
            return ResponseEntity.ok(payment);
        } else {
            logger.warn("Repayment not found or already completed for repaymentId: {}", repaymentId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/balance/{applicationId}")
    public ResponseEntity<Double> getOutstandingBalance(@PathVariable Long applicationId) {
        logger.info("Getting outstanding balance for applicationId: {}", applicationId);
        double balance = repaymentService.getOutstandingBalance(applicationId);
        logger.info("Outstanding balance retrieved successfully for applicationId: {}", applicationId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/generate/{applicationId}")
    public ResponseEntity<List<Repayment>> generateRepaymentSchedule(@PathVariable Long applicationId,
                                                                     @RequestParam double loanAmount,
                                                                     @RequestParam int numberOfInstallments,
                                                                     @RequestParam int daysBetweenInstallments) {
        logger.info("Generating repayment schedule for applicationId: {}, loanAmount: {}, installments: {}, days: {}",
         9       applicationId, loanAmount, numberOfInstallments, daysBetweenInstallments);

        if (loanAmount <= 0 || numberOfInstallments <= 0 || daysBetweenInstallments <= 0){
            logger.warn("Invalid input parameters for generating repayment schedule. Loan amount, number of installments, and days between installments must be positive.");
            return ResponseEntity.badRequest().body(null);
        }

        List<Repayment> schedule = repaymentService.generateRepaymentSchedule(applicationId, loanAmount, numberOfInstallments, daysBetweenInstallments);
        logger.info("Repayment schedule generated successfully for applicationId: {}", applicationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

}