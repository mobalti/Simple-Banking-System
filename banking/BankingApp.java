package banking;

import java.util.Scanner;

public class BankingApp {
    static Scanner scanner = new Scanner(System.in);
    static String[] info = new String[2];
    static int balance = 0;
    Database db;

    public BankingApp(Database db) {
        this.db = db;
    }

    public void mainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            int option = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (option) {
                case 1:
                    createAnAccount();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                case 0:
                    System.out.println("Bye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid operation try again!");
                    break;
            }
        }
    }

    private void createAnAccount() {
        String cardNumber = Card.generateCardNumber();
        String codePIN = Card.generatePin();
        info[0] = cardNumber;
        info[1] = codePIN;
        db.insertCard(cardNumber, codePIN);
        System.out.printf("Your card has been created\n" +
                "Your card number:\n" +
                "%s\n" +
                "Your card PIN:\n" +
                "%S%n%n", cardNumber, codePIN);

    }
    private void logIntoAccount() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String codePIN = scanner.nextLine();
        if (db.isValid(cardNumber, codePIN)) {
            System.out.println("You have successfully logged in!\n");
            info[0] = cardNumber;
            info[1] = codePIN;
            bankMenu();
        } else {
            System.out.println("Wrong card number or PIN!");
        }

    }

    private void bankMenu() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
        int option = scanner.nextInt();
        scanner.nextLine();
        System.out.println();

        switch (option) {
            case 1:
                printBalance();
                break;
            case 2:
                addIncome();
                break;
            case 3:
                doTransfer();
                break;
            case 4:
                closeAccount();
                break;
            case 5:
                logout();
                break;
            case 0:
                System.out.println("Bye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid operation try again!");
                break;
        }
        bankMenu();

    }

    private void printBalance() {
        System.out.printf("Balance: %d%n",
                db.getBalance(info[0],info[1]));
    }

    private void closeAccount() {
        db.closeAccount(info[0]);
        System.out.println("Your account has been closed");


    }

    private void doTransfer() {
        System.out.println("Enter card number:");
        String cardNumber = scanner.nextLine();
        if (Card.isValidCardNumber(cardNumber)) {
            if (db.isCardExist(cardNumber)) {
                System.out.println("Enter how much money you want to transfer:");
                int amount = scanner.nextInt();
                scanner.nextLine();
                if (amount > db.getBalance(info[0],info[1])) {
                    System.out.println("Not enough money!");
                } else {
                    db.doTransfer(info[0],cardNumber,amount);
                    System.out.println("Success!");

                }

            } else {
                System.out.println("Such a card does not exist.");
            }

        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        }

    }

    private void addIncome() {
        System.out.println("Enter income:");
        int income  = scanner.nextInt();
        scanner.nextLine();
        db.addIncome(info[0],income);
        System.out.println("Income was added!");
    }

    private void logout() {
        info = new String[2];
        System.out.println("You have successfully logged out!\n");
        mainMenu();

    }
}
