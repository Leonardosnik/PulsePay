
package br.com.pulsepay.controller;

import br.com.pulsepay.entity.Transfer;
import br.com.pulsepay.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    // Realizar transferência usando a conta autenticada como origem
    @PostMapping
    public TransferResponse makeTransfer(
            @RequestBody TransferRequest request,
            Authentication authentication) {

        Long senderId = getAuthenticatedAccountId(authentication);

        Transfer transfer = transferService.makeTransfer(
                senderId,
                request.receiverId(),
                request.amount()
        );

        return convertToResponse(transfer);
    }

    // Consultar somente transferências das quais o usuário participou
    @GetMapping
    public List<TransferResponse> findMyTransfers(
            Authentication authentication) {

        Long accountId = getAuthenticatedAccountId(authentication);

        return transferService.findByAccountId(accountId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Converter entidade em resposta sem expor dados pessoais
    private TransferResponse convertToResponse(Transfer transfer) {

        return new TransferResponse(
                transfer.getId(),
                transfer.getSender().getId(),
                transfer.getSender().getName(),
                transfer.getReceiver().getId(),
                transfer.getReceiver().getName(),
                transfer.getAmount(),
                transfer.getCreatedAt()
        );
    }

    // Recuperar o ID da conta autenticada
    private Long getAuthenticatedAccountId(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário não autenticado."
            );
        }

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Identificação do usuário inválida."
            );
        }
    }

    // Dados recebidos na transferência
    public record TransferRequest(
            Long receiverId,
            BigDecimal amount
    ) {
    }

    // Dados devolvidos ao usuário
    public record TransferResponse(
            Long id,
            Long senderId,
            String senderName,
            Long receiverId,
            String receiverName,
            BigDecimal amount,
            LocalDateTime createdAt
    ) {
    }
}
