package Account;

import javax.swing.event.InternalFrameEvent;
import java.math.BigDecimal;

public class Account {
    private Integer userId;
    private String username;
    private BigDecimal balance;
    private  Integer account_id;
    private Integer user_id;


    public Account(Integer userId, String username, Double balance  ) {
        this.userId = userId;
        this.username = username;
        this.balance = BigDecimal.valueOf(balance);
    }

    public Account() {

    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountId(int accountId) {
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
