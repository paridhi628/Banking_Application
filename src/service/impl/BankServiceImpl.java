package service.impl;

import domain.account;
import service.BankService;

import java.util.UUID;

public class BankServiceImpl implements BankService {


    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId= UUID.randomUUID().toString();

        //CHANGE LATER -->10 +1 = AC11
        String accountNumber= UUID.randomUUID().toString();
       account a= new account(accountNumber, accountType, (double) 0, customerId);
       //SAVE

        return "";
    }
}
