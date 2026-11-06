package br.com.pulsepay.service;

import br.com.pulsepay.entity.Account;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Notificar recebimento de transferência
    public void notifyTransferReceived(
            Account sender,
            Account receiver,
            BigDecimal amount) {

        Map<String, Object> notification = Map.of(
                "message", "Você recebeu uma transferência!",
                "senderAccountId", sender.getId(),
                "senderName", sender.getName(),
                "receiverAccountId", receiver.getId(),
                "amount", amount,
                "dateTime", LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/accounts/" + receiver.getId(),
                (Object) notification
        );
    }

    // Notificar realização de depósito
    public void notifyDepositReceived(
            Account account,
            BigDecimal amount) {

        Map<String, Object> notification = Map.of(
                "message", "Depósito realizado com sucesso!",
                "accountId", account.getId(),
                "accountName", account.getName(),
                "amount", amount,
                "dateTime", LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/accounts/" + account.getId(),
                (Object) notification
        );
    }
}
