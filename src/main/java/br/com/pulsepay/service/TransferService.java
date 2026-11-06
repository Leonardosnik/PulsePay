
package br.com.pulsepay.service;

import br.com.pulsepay.entity.Account;
import br.com.pulsepay.entity.Transfer;
import br.com.pulsepay.repository.AccountRepository;
import br.com.pulsepay.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private static final Logger logger =
            LoggerFactory.getLogger(TransferService.class);

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final OperationService operationService;
    private final NotificationService notificationService;

    public TransferService(
            AccountRepository accountRepository,
            TransferRepository transferRepository,
            OperationService operationService,
            NotificationService notificationService) {

        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.operationService = operationService;
        this.notificationService = notificationService;
    }

    // Realizar transferência
    @Transactional
    public Transfer makeTransfer(
            Long senderId,
            Long receiverId,
            BigDecimal amount) {

        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException(
                    "As contas de origem e destino são obrigatórias.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor da transferência deve ser maior que zero.");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException(
                    "Não é possível transferir para a própria conta.");
        }

        Account sender = accountRepository.findById(senderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conta de origem não encontrada."));

        Account receiver = accountRepository.findById(receiverId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conta de destino não encontrada."));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente para realizar a transferência.");
        }

        // Atualizar os saldos
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Registrar a transferência
        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setReceiver(receiver);
        transfer.setAmount(amount);

        Transfer savedTransfer = transferRepository.save(transfer);

        // Registrar saída na conta de origem
        operationService.register(
                "TRANSFERENCIA",
                senderId,
                receiverId,
                amount,
                "Transferência realizada"
        );

        // Registrar entrada na conta de destino
        operationService.register(
                "TRANSFERENCIA_RECEBIDA",
                receiverId,
                senderId,
                amount,
                "Transferência recebida de " + sender.getName()
        );

        // Notificar a conta que recebeu o dinheiro
        notificationService.notifyTransferReceived(
                sender,
                receiver,
                amount
        );

        // Registrar no log
        logger.info(
                "TRANSFERÊNCIA REALIZADA | ID: {} | Origem: {} | Destino: {} | Valor: {}",
                savedTransfer.getId(),
                senderId,
                receiverId,
                amount
        );

        return savedTransfer;
    }

    // Consultar somente transferências das quais a conta participou
    public List<Transfer> findByAccountId(Long accountId) {

        return transferRepository.findBySender_IdOrReceiver_Id(
                accountId,
                accountId
        );
    }
}
