package repository;

import domain.account;
import domain.customer;

import java.util.*;

public class accountRepository {
    private static final Map<String, account> accountsByNumber= new HashMap<>();

    public static List <account> findByCustomerId(String CustomerId) {
        List<account> result= new ArrayList<>();
        for(account a: accountsByNumber.values()){
            if(a.getCustomerId().equals(CustomerId))
                result.add(a);
        }
        return result;
    }

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
