package service;

import domain.account;
import domain.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BankService {
    String openAccount(String name, String email, String accountType);
    List<account> listAccounts();

    void deposit(String accountNumber, Double amount, String note);
    void withdraw(String accountNumber, Double amount, String note);

    void transfer(String from, String to, Double amount, String transfer);

     List<transaction> getStatement(String account);

    List<account> searchAccountByCustomer(String q);
}
