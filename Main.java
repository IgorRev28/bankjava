import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class menu {
    private List<String> accountNumbers; //номера счетов
    private List<acc> accounts; // список объектов счетов
    private static final String f = "data.dat"; //файл для сохранения данных

    public menu() {
        this.accountNumbers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        load(); //загрузка данных при создании меню
    }

    public void openacc(String accountNumber, String bic, String kpp){
        for (int i = 0; i < accountNumbers.size(); i++) {
            if (accountNumbers.get(i).equals(accountNumber)) {
                System.out.println("Счёт есть");
                return; //проверка существует ли счет
            }
        }

        acc account = new acc(accountNumber, bic, kpp);
        accountNumbers.add(accountNumber);
        accounts.add(account);
        System.out.println("Счёт открыт: " + account);
        save(); //сохранение после изменений
    }

    public acc findacc(String accountNumber) {
        for (int i = 0; i < accountNumbers.size(); i++) {
            if (accountNumbers.get(i).equals(accountNumber)) {
                return accounts.get(i); // поиск по номеру
            }
        }
        return null;
    }


    public List<acc> bicfind(String bic) {
        List<acc> result = new ArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getBic().equals(bic)) {
                result.add(accounts.get(i)); // поиск по бику
            }
        }
        return result;
    }

    public List<acc> kppfind(String kpp) {
        List<acc> result = new ArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getKpp().equals(kpp)) {
                result.add(accounts.get(i)); // поиск по кпп
            }
        }
        return result;
    }

    public void dep(String accountNumber, double amount) {
        acc account = findacc(accountNumber);
        if (account == null) {
            System.out.println("Нет счёта");
            return;
        }
        account.dep(amount);
        System.out.println("Успешно. Баланс: " + account.getBalance());
        save();
    }

    public void snyat(String accountNumber, double amount) {
        acc account = findacc(accountNumber);
        if (account == null) {
            System.out.println("Нет счёта");
            return;
        }
        account.snyat(amount);
        System.out.println("Успешно. Баланс: " + account.getBalance());
        save();
    }

    public void perevod(String fromAcc, String toAcc, double amount) {
        acc from = findacc(fromAcc);
        acc to = findacc(toAcc);
        if (from == null) {
            System.out.println("Счёт отправителя не найден");
            return;
        }
        if (to == null) {
            System.out.println("Счёт получателя не найден");
            return;
        }

        from.snyat(amount);
        to.dep(amount);
        // Замена транзакций снятия или пополнения на транзакции перевода
        if (from.getTransactions().size() > 0) {
            from.getTransactions().remove(from.getTransactions().size() - 1);
        }
        if (to.getTransactions().size() > 0) {
            to.getTransactions().remove(to.getTransactions().size() - 1);
        }

        from.getTransactions().add(new Transaction("PER", amount, "Перевод на " + toAcc));
        to.getTransactions().add(new Transaction("PER", amount, "Перевод от " + fromAcc));
        System.out.println("Перевод успешен");
        save();
    }

    public void showbalance(String accountNumber) {
        acc account = findacc(accountNumber);
        if (account == null) {
            System.out.println("Нет счёта");
            return;
        }
        System.out.println("Баланс: " + account.getBalance());
    }


    public void showTransactions(String accountNumber) {
        acc account = findacc(accountNumber);
        if (account == null) {
            System.out.println("Нет счёта");
            return;
        }

        System.out.println("Операции:");
        List<Transaction> transactions = account.getTransactions();
        for (int i = 0; i < transactions.size(); i++){
            Transaction transaction = transactions.get(i);
            System.out.println("  " + transaction); // вывод операций
        }


        if (transactions.size() > 0) {
            System.out.println("    Статистика     ");
            double totalDep = 0;
            double totalSnyat = 0;
            double totalPer = 0;

            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                if (t.getType().equals("DEP")) totalDep += t.getAmount();
                else if (t.getType().equals("SNYAT")) totalSnyat += t.getAmount();
                else if (t.getType().equals("PER")) totalPer += t.getAmount(); // подсчет сумм по типам операций
            }

            System.out.println("Всего пополнений: " + totalDep);
            System.out.println("Всего снятий: " + totalSnyat);
            System.out.println("Всего переводов: " + totalPer);
            System.out.println("Кол-во операций: " + transactions.size()); // вывод статистики
        }
    }

    private void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(accountNumbers);
            out.writeObject(accounts);
            out.close(); //сохранение данных в файл
        } catch (IOException e) {
            System.out.println("Ошибка сохранения");
        }
    }


    @SuppressWarnings("unchecked")
    private void load() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            accountNumbers = (List<String>) in.readObject();
            accounts = (List<acc>) in.readObject();
            in.close(); // загрузка данных из файла
        } catch (Exception e) {
            // игнор ошибок на первом запуске
        }
    }
}

class bank {
    private static Scanner scanner = new Scanner(System.in);
    private static menu m = new menu();



    public static void main(String[] args) {
        System.out.println("       Банк       ");
        System.out.println("Все данные сохраняются");

        while (true) {
            menu();
            int choice = getIntInput("Выбор: ");

            switch (choice) {
                case 1: open(); break;
                case 2: depmoney(); break;
                case 3: snyatmoney(); break;
                case 4: balance(); break;
                case 5: showTransactions(); break;
                case 6: poisk(); break;
                case 7: perevod(); break;
                case 0:
                    System.out.println("Выход");
                    return; // выход из программы
                default:
                    System.out.println("Неверно");
            }
        }
    }

    private static void menu() {
        System.out.println("\n1. Открыть счёт");
        System.out.println("2. Положить деньги");
        System.out.println("3. Снять деньги");
        System.out.println("4. Баланс");
        System.out.println("5. Транзакции + статистика");
        System.out.println("6. Поиск счетов");
        System.out.println("7. Перевод между счетами");
        System.out.println("0. Выход");
    }

    private static void open() {
        System.out.print("Номер счёта: ");
        String accountNumber = scanner.nextLine();
        System.out.print("БИК: ");
        String bic = scanner.nextLine();
        System.out.print("КПП: ");
        String kpp = scanner.nextLine();
        m.openacc(accountNumber, bic, kpp); // открытие счета
    }

    private static void depmoney() {
        System.out.print("Номер счёта: ");
        String accountNumber = scanner.nextLine();
        double amount = getDoubleInput("Сумма: ");
        m.dep(accountNumber, amount); // пополнение
    }

    private static void snyatmoney() {
        System.out.print("Номер счёта: ");
        String accountNumber = scanner.nextLine();
        double amount = getDoubleInput("Сумма: ");
        m.snyat(accountNumber, amount); // снятие
    }

    private static void perevod() {
        System.out.print("Счёт отправителя: ");
        String fromAcc = scanner.nextLine();
        System.out.print("Счёт получателя: ");
        String toAcc = scanner.nextLine();
        double amount = getDoubleInput("Сумма перевода: ");
        m.perevod(fromAcc, toAcc, amount); // перевод между счетами
    }

    private static void balance() {
        System.out.print("Номер счёта: ");
        String accountNumber = scanner.nextLine();
        m.showbalance(accountNumber); // проверка баланса
    }


    private static void showTransactions() {
        System.out.print("Номер счёта: ");
        String accountNumber = scanner.nextLine();
        m.showTransactions(accountNumber); // история операций
    }

    private static void poisk() {
        System.out.println("Поиск по: 1-счёт, 2-БИК, 3-КПП");
        int type = getIntInput("Тип: ");

        switch (type) {
            case 1:
                System.out.print("Номер счёта: ");
                String accountNumber = scanner.nextLine();
                acc account = m.findacc(accountNumber);
                if (account != null) System.out.println("Найден: " + account);
                else System.out.println("Нет"); // поиск по номеру
                break;
            case 2:
                System.out.print("БИК: ");
                String bic = scanner.nextLine();
                List<acc> byBic = m.bicfind(bic);
                System.out.println("Найдено: " + byBic.size());
                for (int i = 0; i < byBic.size(); i++) System.out.println(byBic.get(i)); // поиск по бику
                break;
            case 3:
                System.out.print("КПП: ");
                String kpp = scanner.nextLine();
                List<acc> byKpp = m.kppfind(kpp);
                System.out.println("Найдено: " + byKpp.size());
                for (int i = 0; i < byKpp.size(); i++) System.out.println(byKpp.get(i)); // поиск по кпп
                break;
            default:
                System.out.println("Неверно");
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Число!");
            scanner.next();
            System.out.print(prompt); // проверка что введено целое число
        }
        int result = scanner.nextInt();
        scanner.nextLine();
        return result;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Число!");
            scanner.next();
            System.out.print(prompt); // проверка что введено вообще число
        }
        double result = scanner.nextDouble();
        scanner.nextLine();
        return result;
    }
}