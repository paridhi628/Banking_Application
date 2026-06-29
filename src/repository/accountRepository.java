package repository;

import domain.account;

import java.util.HashMap;
import java.util.Map;

public class accountRepository {
    private final Map<String, account> accountsByNumber= new HashMap<>();
    public void save(account Account){
        // accountsByNumber.put(account.getAccountNumber(), Account );
    }
}
