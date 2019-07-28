package ru.sber.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import ru.sber.test.entity.AccountMutex;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountMutexRepository extends JpaRepository<AccountMutex, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    Optional<AccountMutex> findById(Long aLong);
}