import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class acc implements Serializable {
    private static final long serialVersionUID = 1L; //контроль версий
    private String accountNumber;
    private String bic;
    private String kpp;
    private double balance;
    private List<Transaction> transactions;


    public acc(String accountNumber, String bic, String kpp) {
        this.accountNumber = accountNumber;
        this.bic = bic;
        this.kpp = kpp;
        this.balance = 0.0;
        this.transactions = new ArrayList<>(); //создание списка с операциями
    }

    public String getAccountNumber() { return accountNumber; }
    public String getBic() { return bic; }
    public String getKpp() { return kpp; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; } //геттеры для доступа к приватным данным счета

    public void dep(double amount) {
        if (amount <= 0) {
            System.out.println("Ошибка суммы");
        } else {
            balance += amount;
            transactions.add(new Transaction("DEP", amount, "Пополнение")); //добавление операции
        }
    }

    public void snyat(double amount){
        if (amount <= 0) {
            System.out.println("Ошибка суммы");
        } else if (amount > balance) {
            System.out.println("Недостаточно средств");
        } else {
            balance -= amount;
            transactions.add(new Transaction("SNYAT", amount, "Снятие"));
        }
    }

    public void perevodSnyat(double amount, String toAcc) {
        if (amount <= 0) {
            System.out.println("Ошибка суммы");
        } else if (amount > balance) {
            System.out.println("Недостаточно средств");
        } else {
            balance -= amount;
            if (transactions.size() > 0) {
                transactions.remove(transactions.size() - 1); //удаление операции снятия т.к. будет операция перевода только
            }
            transactions.add(new Transaction("PER", amount, "Перевод на " + toAcc)); //операция перевода -
        }
    }


    public void perevodDep(double amount, String fromAcc) {
        if (amount <= 0) {
            System.out.println("Ошибка суммы");
        } else {
            balance += amount;
            if (transactions.size() > 0) {
                transactions.remove(transactions.size() - 1); // удаление операции пополнения т.к. будет только операция перевода
            }
            transactions.add(new Transaction("PER", amount, "Перевод от " + fromAcc)); //операция перевода +
        }
    }

    public String stroka() {
        return "Счёт: " + accountNumber + ", БИК: " + bic + ", КПП: " + kpp + ", Баланс: " + balance; //строка счета
    }
}