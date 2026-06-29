package domain;

import java.time.LocalDateTime;

public class transaction {
    private String id;
    private type Type;
    private String accountNumber;
    private Double amount;
    private LocalDateTime timeStamp;
    private String note;

    public transaction(String id, type type, String accountNumber, Double amount, LocalDateTime timeStamp, String note) {
        this.id = id;
        Type = type;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.note = note;
    }
}
