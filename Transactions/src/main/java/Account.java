import java.util.concurrent.atomic.AtomicLong;

public class Account
{
    private AtomicLong money = new AtomicLong();
    private String accNumber;
    private volatile boolean isBlocked;

    public Account(AtomicLong money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
        this.isBlocked = false;
    }

    public AtomicLong getMoney() {
        return money;
    }

    public void replenish(long money) {
        this.money.addAndGet(money);
    }

    public void withdraw(long money){
        this.money.addAndGet(-money);
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
