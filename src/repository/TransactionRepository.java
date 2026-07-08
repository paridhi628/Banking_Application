package repository;
import domain.account;
import domain.transaction;

import java.util.*;

public class TransactionRepository {
    private final Map<String, List<transaction>> txByAccount= new HashMap<>();

    public void add(transaction transaction) {
      List<transaction> list=  txByAccount.computeIfAbsent(transaction.getAccountNumber(),
            k-> new ArrayList<>());
            list.add(transaction);
    }

    public List<transaction> findByAccount(String account) {
        return new ArrayList<>(txByAccount.getOrDefault(account, Collections.emptyList()));
    }
}
