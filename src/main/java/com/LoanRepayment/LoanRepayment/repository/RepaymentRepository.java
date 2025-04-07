package com.LoanRepayment.LoanRepayment.repository;


import com.LoanRepayment.LoanRepayment.model.Repayment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long> {
    List<Repayment> findAllByApplicationId(Long applicationId);
    @Transactional
    void deleteByApplicationId(Long applicationId);
}
