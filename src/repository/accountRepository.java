package repository;

import domain.account;

import java.util.*;

public class accountRepository {
    private final Map<String, account> accountsByNumber= new HashMap<>();
    public void save(account Account){
         accountsByNumber.put(Account.getAccountNumber(), Account );
    }

    public List<account> findAll() {
        return new ArrayList<>(accountsByNumber.values());
    }

    public Optional<account> findByNumber(String accountNumber) {
        return Optional.ofNullable(accountsByNumber.get(accountNumber));
    }
}
