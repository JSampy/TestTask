package ru.sber.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.test.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
