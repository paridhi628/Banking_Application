package domain;

public class account {
    private String accountNumber;
    private String customerId;
    private Double balance;
    private String accountType;

    public account(String accountNumber,String customerId, Double balance, String accountType ) {
        this.accountNumber = accountNumber;
        this.customerId=customerId;
        this.balance=balance;
        this.accountType=accountType;
    }
}
