package br.com.pulsepay.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "sender_account_id",
            nullable = false,
            updatable = false
    )
    private Account sender;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "receiver_account_id",
            nullable = false,
            updatable = false
    )
    private Account receiver;

    @Column(
            nullable = false,
            precision = 15,
            scale = 2,
            updatable = false
    )
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Transfer() {
    }

    public Long getId() {
        return id;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
