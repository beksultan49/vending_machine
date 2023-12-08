import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {
    Scanner sc = new Scanner(System.in);
    String m;
    int number;
    int password;

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private Acceptor coinAcceptor;
    private Acceptor cardAcceptor;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new Acceptor(100) {
            @Override
            public void payBy() {
                print("Монет на сумму: " + coinAcceptor.getAmount());
            }
        };
        cardAcceptor = new Acceptor(100) {
            @Override
            public void payBy() {
                print("На балансе карты: " + cardAcceptor.getAmount());
            }
        };

    }

    public static void run() {
        AppRunner app = new AppRunner();
        app.qwerty();
        while (!isExit) {
            app.startSimulation();
        }
    }
    private void qwerty(){
        print("Выберите способ оплаты:");
        System.out.println("x - опралить наличными\n" + "y - оплатить картой" );
        m = sc.nextLine();
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);
        if (m.equals("x")) {
            coinAcceptor.payBy();
        } else if (m.equals("y")) {

            System.out.println("Введите номер карты:");
            number = sc.nextInt();
            System.out.println("Введите пароль:");
            password = sc.nextInt();
            cardAcceptor.payBy();
        }
        else System.out.println("Не правильный ввод данных!");
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        if (m.equals("x")){
            for (int i = 0; i < products.size(); i++) {
                if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            }
        } else if (m.equals("y")) {
            for (int i = 0; i < products.size(); i++) {
                if (cardAcceptor.getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            }

        }

        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase())) && m.equals("x")) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase())) && m.equals("y")) {
                    cardAcceptor.setAmount(cardAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
            chooseAction(products);
        }
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
