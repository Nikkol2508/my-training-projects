import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Bank {
    private HashMap<String, Account> accounts;
    private final Random random = new Random();
    private volatile long sum;

    public Bank(HashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account accountFrom = accounts.get(fromAccountNum);
        if (accountFrom.getMoney().longValue() >= amount) {
            Account accountTo = accounts.get(toAccountNum);
            if (accountFrom.isBlocked() | accountTo.isBlocked()) System.out.println("Один из счетов заблокирован");
            else {

//                synchronized (accountFrom){
//                    synchronized (accountTo){
//                        accountFrom.withdraw(amount);
//                        accountTo.replenish(amount);
//                    }
//                }

                synchronized (accountFrom.getAccNumber().compareTo(accountTo.getAccNumber()) > 0 ? accountFrom : accountTo){
                    synchronized (accountFrom.getAccNumber().compareTo(accountTo.getAccNumber()) < 0 ? accountFrom : accountTo){
                        accountFrom.withdraw(amount);
                        accountTo.replenish(amount);
                    }
                }

                if (amount > 50000) {
                    System.out.println("сумма перевода больше 50000");
                    accountFrom.setBlocked(true);
                    accountTo.setBlocked(true);

                    if (isFraud(fromAccountNum, toAccountNum, amount)) {
                        System.out.println("блокировка счёта !!!!!!!!!!!!!!!!!!!!!!!");
                    } else {
                        accountFrom.setBlocked(false);
                        accountTo.setBlocked(false);
                    }
                }

            }
        } else System.out.println("Не достаточно средств");
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        long balance = accounts.get(accountNum).getMoney().longValue();
        return balance;
    }
}
