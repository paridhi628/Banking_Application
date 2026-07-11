package service.impl;

import domain.account;
import domain.customer;
import domain.transaction;
import domain.type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ValidationException;
import repository.CustomerRepository;
import repository.TransactionRepository;
import repository.accountRepository;
import service.BankService;
import util.Validation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
private final accountRepository accRe= new accountRepository();
private final TransactionRepository transactionRepository= new TransactionRepository();
private final CustomerRepository customerRepository= new CustomerRepository();

private final Validation<String> validateName= name -> {
    if(name==null || name.isBlank()) throw new ValidationException("Name is required");
};

private final Validation<String> validateEmail=(email) -> {
    if(email==null || !email.contains("@")) throw new ValidationException("Email is required");
};

    private final Validation<String> validateType=(type) -> {
        if(type==null || !(type.equalsIgnoreCase("SAVINGS") || type.contains("CURRENT")))
            throw new ValidationException("Type must be SAVINGS or CURRENT");
    };

    private final Validation<Double> validateAmountPositive=(amount) -> {
        if(amount==null || amount<0)
            throw new ValidationException("Please enter valid amount");
    };

    @Override
    public String openAccount(String name, String email, String accountType) {
        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);
        String customerId= UUID.randomUUID().toString();

        //create customer
        customer c= new customer(email, customerId,name);
        customerRepository.save(c);

        //CHANGE LATER -->10 +1 = AC11
      //  String accountNumber= UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        account Account= new account(accountNumber, customerId, (double) 0, accountType);
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
        validateAmountPositive.validate(amount);
        account Account= accRe.findByNumber(accountNumber)
                .orElseThrow( ()-> new AccountNotFoundException("Account not Found: " +accountNumber));
        Account.setBalance(Account.getBalance() + amount);
        transaction Transaction= new transaction(UUID.randomUUID().toString(),
                type.DEPOSIT,Account.getAccountNumber(),amount,LocalDateTime.now(), note );
        transactionRepository.add(Transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        validateAmountPositive.validate(amount);
        account Account= accRe.findByNumber(accountNumber)
                .orElseThrow( ()-> new AccountNotFoundException("Account not Found: " +accountNumber));
        if(Account.getBalance().compareTo(amount)<0)
            throw new InsufficientFundsException("Insufficient Balance");
        Account.setBalance(Account.getBalance() - amount);
        transaction Transaction= new transaction(UUID.randomUUID().toString(),
                type.WITHDRAW,Account.getAccountNumber(),amount,LocalDateTime.now(), note );
        transactionRepository.add(Transaction);
    }

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note) {
        validateAmountPositive.validate(amount);
        if(fromAcc.equals(toAcc))
            throw new ValidationException("Cannot Transfer to your own account");
        account from= accRe.findByNumber(fromAcc)
                .orElseThrow( ()-> new AccountNotFoundException("Account not Found: " +fromAcc));
        account to= accRe.findByNumber(toAcc)
                .orElseThrow( ()-> new AccountNotFoundException("Account not Found: " +toAcc));
        if(from.getBalance().compareTo(amount)<0)
            throw new InsufficientFundsException("Insufficient Balance");
        from.setBalance(from.getBalance() - amount);
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
