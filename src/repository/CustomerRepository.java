package repository;

import domain.customer;
import domain.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerRepository {
    private final Map<String, customer> customersById= new HashMap<>();

    public List<customer> findAll() {
        return new ArrayList<>(customersById.values());
    }

    public void save(customer c) {
        customersById.put(c.getId(), c);
    }
}
