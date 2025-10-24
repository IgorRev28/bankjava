import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BankGUI {
    private static menu m = new menu();
    private JTextArea output;
    public static void main(String[] args) {
        new BankGUI().sozdatOkno();
    }


    private void sozdatOkno() {
        JFrame okno = new JFrame("Банк");
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setSize(700, 500);

        JPanel fonPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image image = new ImageIcon("phon.jpg").getImage();
                    double panelRatio = (double)getWidth() / getHeight();
                    double imgRatio = (double)image.getWidth(null) / image.getHeight(null);

                    int width, height, x, y;

                    if (panelRatio > imgRatio) {
                        height = getHeight();
                        width = (int)(height * imgRatio);
                        x = (getWidth() - width) / 2;
                        y = 0;
                    } else {
                        width = getWidth();
                        height = (int)(width / imgRatio);
                        x = 0;
                        y = (getHeight() - height) / 2; // расчет размеров для сохранения пропорций изображения (все равно не получилось реализовать фон как хотел)
                    }

                    g.drawImage(image, x, y, width, height, this);
                } catch (Exception e) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight()); // серый фон если картинка не загрузилась
                }
            }
        };

        JLabel zagolovok = new JLabel("БАНК", JLabel.CENTER);
        zagolovok.setFont(new Font("Arial", Font.BOLD, 20));
        zagolovok.setForeground(Color.BLUE);
        zagolovok.setOpaque(false);
        fonPanel.add(zagolovok, BorderLayout.NORTH); // заголовок окна

        JPanel knopkiPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        knopkiPanel.setOpaque(false);
        sozdatKnopki(knopkiPanel);
        fonPanel.add(knopkiPanel, BorderLayout.CENTER); //панель с кнопками

        output = new JTextArea(8, 50);
        output.setBackground(new Color(255, 255, 255, 220));
        JScrollPane scroll = new JScrollPane(output);
        fonPanel.add(scroll, BorderLayout.SOUTH); // область для вывода информации

        okno.setContentPane(fonPanel);
        okno.setVisible(true); // отображение окна
    }

    private void sozdatKnopki(JPanel p) {
        JButton b1 = new JButton("Открыть счет");
        b1.addActionListener(e -> otkrytSchet());
        p.add(b1);

        JButton b2 = new JButton("Пополнить");
        b2.addActionListener(e -> popolnit());
        p.add(b2);

        JButton b3 = new JButton("Снять");
        b3.addActionListener(e -> snyat());
        p.add(b3);

        JButton b4 = new JButton("Перевод");
        b4.addActionListener(e -> perevod());
        p.add(b4);

        JButton b5 = new JButton("Баланс");
        b5.addActionListener(e -> balans());
        p.add(b5);

        JButton b6 = new JButton("История");
        b6.addActionListener(e -> istoriya());
        p.add(b6);

        JButton b7 = new JButton("Поиск");
        b7.addActionListener(e -> poisk());
        p.add(b7);

        JButton b8 = new JButton("Смешная кнопка");
        b8.addActionListener(e -> smeshnayaKnopka(b8));
        p.add(b8); //кнопки создание
    }

    private void otkrytSchet() {
        String a = JOptionPane.showInputDialog("Номер счета:");
        String b = JOptionPane.showInputDialog("БИК:");
        String k = JOptionPane.showInputDialog("КПП:");
        if (a != null && b != null && k != null) {
            m.openacc(a, b, k);
            output.append("Счет " + a + " открыт\n"); //новый счет
        }
    }

    private void popolnit() {
        String a = JOptionPane.showInputDialog("Номер счета:");
        String s = JOptionPane.showInputDialog("Сумма:");
        if (a != null && s != null) {
            try {
                double sum = Double.parseDouble(s);
                m.dep(a, sum);
                output.append("Пополнение: +" + sum + " на счет " + a + "\n"); //пополнение
            } catch (Exception e) {
                output.append("Ошибка: " + e.getMessage() + "\n");
            }
        }
    }


    private void snyat() {
        String a = JOptionPane.showInputDialog("Номер счета:");
        String s = JOptionPane.showInputDialog("Сумма:");
        if (a != null && s != null) {
            try {
                double sum = Double.parseDouble(s);
                m.snyat(a, sum);
                output.append("Снятие: -" + sum + " со счета " + a + "\n"); //снятие
            } catch (Exception e) {
                output.append("Ошибка: " + e.getMessage() + "\n");
            }
        }
    }

    private void perevod() {
        String ot = JOptionPane.showInputDialog("Счет отправителя:");
        String k = JOptionPane.showInputDialog("Счет получателя:");
        String s = JOptionPane.showInputDialog("Сумма:");
        if (ot != null && k != null && s != null) {
            try {
                double sum = Double.parseDouble(s);
                m.perevod(ot, k, sum);
                output.append("Перевод: " + sum + " с " + ot + " на " + k + "\n"); //перевод
            } catch (Exception e) {
                output.append("Ошибка перевода: " + e.getMessage() + "\n");
            }
        }
    }



    private void balans() {
        String a = JOptionPane.showInputDialog("Номер счета:");
        if (a != null) {
            output.append("Баланс счета " + a + ": [функция в разработке]\n"); // баланс не получилось реализовать поэтому сообщение что функция в разработке =0
        }
    }

    private void istoriya() {
        String a = JOptionPane.showInputDialog("Номер счета:");
        if (a != null) {
            output.append("История операций счета " + a + ": [функция в разработке]\n"); // не получилось реализовать поиск тут поэтому просто сообщение что функция в разработке
        }
    }


    private void poisk() {
        JOptionPane.showMessageDialog(null,
                "Поиск временно недоступен в графической версии",
                "В разработке",
                JOptionPane.INFORMATION_MESSAGE); // поиск недоступен
    }

    private void smeshnayaKnopka(JButton knopka) {
        Random r = new Random();
        Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        knopka.setBackground(c); //изменение цвета кнопки на случайный
    }
}