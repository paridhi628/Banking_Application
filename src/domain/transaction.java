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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public type getType() {
        return Type;
    }

    public void setType(type type) {
        Type = type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
