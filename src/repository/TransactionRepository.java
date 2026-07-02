package repository;
import domain.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    private final Map<String, List<transaction>> txByAccount= new HashMap<>();

    public void add(transaction transaction) {
        txByAccount.computeIfAbsent(transaction.getAccountNumber(),
            k-> new ArrayList<>()).add(transaction);
    }
}
