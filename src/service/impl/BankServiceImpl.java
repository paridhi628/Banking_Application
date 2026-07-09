package service.impl;

import domain.account;
import domain.customer;
import domain.transaction;
import domain.type;
import repository.CustomerRepository;
import repository.TransactionRepository;
import repository.accountRepository;
import service.BankService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
private final accountRepository accRe= new accountRepository();
private final TransactionRepository transactionRepository= new TransactionRepository();
private final CustomerRepository customerRepository= new CustomerRepository();

    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId= UUID.randomUUID().toString();

        //create cutomer
        customer c= new customer(email, customerId,name);
        customerRepository.save(c);

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

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        account Account= accRe.findByNumber(accountNumber)
                .orElseThrow( ()-> new RuntimeException("Account not Found: " +accountNumber));
        if(Account.getBalance().compareTo(amount)<0)
            throw new RuntimeException("Insufficient Balance");
        Account.setBalance(Account.getBalance() + amount);
        transaction Transaction= new transaction(UUID.randomUUID().toString(),
                type.WITHDRAW,Account.getAccountNumber(),amount,LocalDateTime.now(), note );
        transactionRepository.add(Transaction);
    }

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note) {
        if(fromAcc.equals(toAcc))
            throw new RuntimeException("Cannot Transfer to your own account");
        account from= accRe.findByNumber(fromAcc)
                .orElseThrow( ()-> new RuntimeException("Account not Found: " +fromAcc));
        account to= accRe.findByNumber(toAcc)
                .orElseThrow( ()-> new RuntimeException("Account not Found: " +toAcc));
        if(from.getBalance().compareTo(amount)<0)
            throw new RuntimeException("Insufficient Balance");
        from.setBalance(from.getBalance() + amount);
        to.setBalance(to.getBalance() + amount);
        transaction FromTransaction= new transaction(UUID.randomUUID().toString(),
                type.TRANSFER_OUT,from.getAccountNumber(),amount,LocalDateTime.now(), note );
        transactionRepository.add(FromTransaction);
        transaction ToTransaction= new transaction(UUID.randomUUID().toString(),
                type.TRANSFER_IN,to.getAccountNumber(),amount,LocalDateTime.now(), note );
        transactionRepository.add(ToTransaction);
    }

    @Override
    public List<transaction> getStatement(String account) {
        return transactionRepository.findByAccount(account).stream()
                .sorted(Comparator.comparing(transaction:: getTimeStamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<account> searchAccountByCustomer(String q) {
        String query= (q==null) ? "" : q.toLowerCase();
  //      List<account> result= new ArrayList<>();
//        for(customer c: customerRepository.findAll()){
//           if(c.getName().toLowerCase().contains(query))
//               result.addAll(accountRepository.findByCustomerId(c.getId()));
//        }
//        result.sort(Comparator.comparing(account::getAccountNumber));
        return customerRepository.findAll().stream()
                .filter(c-> c.getName().toLowerCase().contains(query))
                .flatMap(c->accountRepository.findByCustomerId(c.getId()).stream())
                .sorted(Comparator.comparing(account::getAccountNumber))
                .collect(Collectors.toList());
        //return result;
    }

    private String getAccountNumber() {
        int size= accRe.findAll().size() +1;
        return String.format("AC%06d", size);
    }
}
