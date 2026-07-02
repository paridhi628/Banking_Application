package service;

import domain.account;

import java.util.List;

public interface BankService {
    String openAccount(String name, String email, String accountType);
    List<account> listAccounts();

    void deposit(String accountNumber, Double amount, String note);
}
