package banking;

import java.io.File;
import java.sql.*;
import java.util.*;

public class App {


    public static void main(String[] args) {

        BankingHelper bankingHelper = new BankingHelper(connect(args[1]));

        //flag to detect exit option
        boolean exit = false;

        while (!exit) {
            int decision = bankingHelper.displayMainMenu();
            System.out.println();
            switch (decision) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    bankingHelper.createRandomAccount(new Random());
                    System.out.println();
                    break;
                case 2:
                    Scanner loginScanner = new Scanner(System.in);
                    System.out.println("Enter your card number:");
                    String cardNumber = loginScanner.nextLine();
                    System.out.println("Enter your PIN:");// 4000004938320895
                    String cardPin = loginScanner.nextLine();
                    if (!bankingHelper.login(cardNumber, cardPin)) {
                        System.out.println("\nWrong card number or PIN!\n");
                    } else {
                        System.out.println("\nYou have successfully logged in!\n");
                        exit = bankingHelper.accessAccountManagement(cardNumber);
                    }
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
        System.out.println("Bye!");
    }

    public static Connection connect(String fileName) {

        String file = new File(fileName).getAbsolutePath();
        boolean newDatabase = !new File(file).exists();

        Connection conn = null;
        // db parameters
        String url = "jdbc:sqlite:" + file;
        // create a connection to the database
        try {
            conn = DriverManager.getConnection(url);
            if (newDatabase) {
                try (Statement statement = conn.createStatement()) {
                    // Statement execution
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "number TEXT NOT NULL," +
                            "pin TEXT NOT NULL," +
                            "balance INTEGER DEFAULT 0)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public String getGreeting() {
        return "Hello Intellij Geeks!";
    }

}