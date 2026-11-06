package br.com.pulsepay.repository;

import br.com.pulsepay.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository
        extends JpaRepository<Operation, Long> {

    // Buscar operações pelo ID da conta
    List<Operation> findByAccountId(Long accountId);
}