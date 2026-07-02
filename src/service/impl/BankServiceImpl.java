package service.impl;

import domain.account;
import domain.transaction;
import domain.type;
import repository.TransactionRepository;
import repository.accountRepository;
import service.BankService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
private final accountRepository accRe= new accountRepository();
private final TransactionRepository transactionRepository= new TransactionRepository();

    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId= UUID.randomUUID().toString();

        //CHANGE LATER -->10 +1 = AC11
      //  String accountNumber= UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        account Account= new account(accountNumber, accountType, (double) 0, customerId);
        accRe.save(Account);
       //SAVE
       return accountNumber;

    }

    @Override
    public List<account> listAccounts() {
        return accRe.findAll().stream()
                .sorted(Comparator.comparing(account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        account Account= accRe.findByNumber(accountNumber)
                .orElseThrow( ()-> new RuntimeException("Account not Found: " +accountNumber));
        Account.setBalance(Account.getBalance() + amount);
        transaction Transaction= new transaction(UUID.randomUUID().toString(),
                type.DEPOSIT,Account.getAccountNumber(),amount,LocalDateTime.now(), note );
        transactionRepository.add(Transaction);
    }

    private String getAccountNumber() {
        int size= accRe.findAll().size() +1;
        return String.format("AC%06d", size);
    }
}
