package br.com.pulsepay.repository;

import br.com.pulsepay.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Optional<Account> findByCpf(String cpf);
}
