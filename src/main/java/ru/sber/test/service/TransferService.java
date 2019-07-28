package ru.sber.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.test.entity.Payment;
import ru.sber.test.repository.AccountMutexRepository;
import ru.sber.test.repository.AccountRepository;
import ru.sber.test.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMutexRepository accountMutexRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public Optional<Payment> transfer(Long from, Long to, BigDecimal sum) {

        return
                Optional
                        .ofNullable(from)
                        .filter(f -> Objects.nonNull(to))
                        .filter(f -> Objects.nonNull(sum))
                        .filter(f -> sum.compareTo(BigDecimal.ZERO) > 0)
                        .filter(f -> new TreeSet<Long>(Arrays.asList(from, to))
                                .stream()
                                .map(accountMutexRepository::findById) // <-- PESSIMISTIC_WRITE
                                .allMatch(Optional::isPresent))
                        .flatMap(f -> accountRepository.findById(from))
                        .filter(f -> Objects.nonNull(f.getAmount()))
                        .filter(f -> f.getAmount().compareTo(sum) >= 0)
                        .map(f -> Payment.builder().fromAccount(f))
                        .flatMap(p -> accountRepository.findById(to)
                                .filter(t -> Objects.nonNull(t.getAmount()))
                                .map(t -> p.toAccount(t))
                        )
                        .map(p -> p.date(LocalDateTime.now()))
                        .map(p -> p.amount(sum))
                        .map(Payment.PaymentBuilder::build)
                        .map(p -> {
                            p.getFromAccount().setAmount(p.getFromAccount().getAmount().subtract(sum));
                            p.getToAccount().setAmount(p.getToAccount().getAmount().add(sum));

                            return paymentRepository.save(p);
                        });

    }

}
