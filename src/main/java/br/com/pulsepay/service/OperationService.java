package br.com.pulsepay.service;

import br.com.pulsepay.entity.Operation;
import br.com.pulsepay.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    // Registrar uma operação
    public Operation register(
            String type,
            Long accountId,
            Long relatedAccountId,
            BigDecimal amount,
            String description) {

        Operation operation = new Operation();

        operation.setType(type);
        operation.setAccountId(accountId);
        operation.setRelatedAccountId(relatedAccountId);
        operation.setAmount(amount);
        operation.setDescription(description);

        return operationRepository.save(operation);
    }

    // Consultar todas as operações
    public List<Operation> findAll() {
        return operationRepository.findAll();
    }

    // Consultar operações de uma conta específica
    public List<Operation> findByAccountId(Long accountId) {
        return operationRepository.findByAccountId(accountId);
    }
}
