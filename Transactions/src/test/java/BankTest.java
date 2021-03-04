
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class BankTest {

    HashMap<String, Account> accounts = new HashMap<>();
    Bank bank = new Bank(accounts);
    Random random = new Random();
    volatile long sum;

    @Before
    public void setUp() throws Exception {

        for (int i = 0; i < 1000; i++){
            String accNum = String.valueOf(i);
            AtomicLong ms = new AtomicLong();
            ms.set(100000);
            accounts.put(accNum, new Account(ms, accNum));
        }
    }

    public long getBalance(){
        long balance = accounts.entrySet().stream().mapToLong(v -> v.getValue().getMoney().longValue()).sum();
        return balance;
    }

    @Test
    public void testTransfer(){
        System.out.println(getBalance());
        ExecutorService pool = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 50; i++){
            Runnable t = () -> {
                try {
                    bank.transfer(String.valueOf(random.nextInt(3)), String.valueOf(random.nextInt(3)), random.nextInt(60000));
                    System.out.println("проведён перевод " );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            pool.execute(t);
        }

        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getBalance());

    }

    @Test
    public void testTransfer2(){
        System.out.println(getBalance());
        ExecutorService pool = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 50; i++){
            Runnable t = () -> {
                try {
                    String accFrom = String.valueOf(random.nextInt(4));
                    String accTo = String.valueOf(random.nextInt(4));
                    bank.transfer(accFrom, accTo, random.nextInt(60000));
                    bank.transfer(accTo, accFrom, random.nextInt(60000));
                    System.out.println("проведён перевод " );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            pool.execute(t);
        }

        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getBalance());

    }
}
