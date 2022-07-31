package banking;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class BankingHelper {

    public static final String BIN = "400000";

    private Connection connection;

    private BankingHelper(){

    }


    /**
     * creates an instance of this class
     * @param conn - needs a functioning database connection
     **/
    public BankingHelper(Connection conn) {
        this.connection = conn;
    }

    /**
     * displays main menu options.
     **/
    public int displayMainMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    /**
     * displays and handles operations related to an account.
     * @param cardNumber - account being managed.
     **/
    public boolean accessAccountManagement(String cardNumber) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            Scanner internal = new Scanner(System.in);
            int access = internal.nextInt();
            System.out.println("");
            switch (access) {
                default:
                case 0:
                    return true;
                case 1://1. get balance
                    displayBalance(cardNumber);
                    break;
                case 2://2. Add income
                    addIncome(cardNumber);
                    break;
                case 3://3. Do transfer
                    transferBalance(cardNumber);
                    break;
                case 4://4. Close account
                    exit= true;
                    deleteAccount(cardNumber);
                    break;
                case 5://5. logout
                    exit = true;
                    System.out.println("You have successfully logged out!");
                    break;
            }
            System.out.println();
        }
        return false;
    }

    /**
     * Displays balance of a certain account.
     * @param cardNumber - card number to be processed.
     **/
    private void displayBalance(String cardNumber){
        System.out.println("Balance: "+getBalance(cardNumber) );
    }

    /**
     * retrieves account balance.
     * @param cardNumber - account being managed.
     **/
    private int getBalance(String cardNumber){
        int balance=0;
        String sql = "select balance from card where number=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            ResultSet set=preparedStatement.executeQuery();

            balance= set.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * performs balance deposit for an account.
     * @param cardNumber - account being managed.
     **/
    private void addIncome(String cardNumber){
        System.out.println("Enter income:");
        Scanner scanner=new Scanner(System.in);
        int income = scanner.nextInt();
        updateIncome(cardNumber, income);
        System.out.println("Income was added!");
    }

    /**
     * updates the balance of an account.
     * @param cardNumber - account being managed.
     * @param amount - amount to be updated.
     **/
    private void updateIncome(String cardNumber,Integer amount){

        String sql = "update card set balance=balance+? where number=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(2, cardNumber);
            preparedStatement.setInt(1, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * performs balance transfer from an account.
     * @param cardNumber - account being managed.
     **/
    private void transferBalance(String cardNumber){

        System.out.println("Transfer");
        System.out.println("Enter card number:");
        Scanner scanner=new Scanner(System.in);

        String beneficiary = scanner.nextLine();

        if (generateChecksum(beneficiary.substring(0,beneficiary.length()-1))
                != Character.getNumericValue(beneficiary.charAt(beneficiary.length()-1))){
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }

        String sql = "select number from card where number=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, beneficiary);
            ResultSet set=preparedStatement.executeQuery();
            beneficiary= set.getString(1);
        } catch (SQLException e) {
            System.out.println("Such a card does not exist.");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");

        int transferAmount=scanner.nextInt();
        int currentBalance=getBalance(cardNumber);

        if (transferAmount>currentBalance){
            System.out.println("Not enough money!");
            return;
        }

        updateIncome(cardNumber,-transferAmount);

        updateIncome(beneficiary,transferAmount);

        System.out.println("Success!");
    }

    /**
     * performs account deletion.
     * @param cardNumber - account being managed.
     **/
    private void deleteAccount(String cardNumber){

        String sql = "delete from card where number=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.executeUpdate();
            System.out.println("The account has been closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a random account id with 9 digits long
     * @param random - random generator.
     * @return  - a random account ID.
     **/
    private static String randomAccountId(Random random) {
        int upper = 1000000000;
        int lower = 100000000;
        return String.valueOf(random.nextInt(upper - lower) + lower);//
    }

    /**
     * Generates a checksum for the card number.
     * @param input - card number to be processed.
     * @return  - [checksum] last digit to be added to the card number.
     **/
    private static int generateChecksum(String input) {
        //drop last digit
        char[] chars = input.toCharArray();
        int sum = 0;
        for (int x = 1; x < chars.length + 1; x++) {
            int digit = Character.getNumericValue(chars[x - 1]);
            //multiply odd digits by 2
            if (x % 2 == 1)
                digit = digit * 2;
            //subtract 9 to numbers over 9
            if (digit > 9)
                digit -= 9;
            //add all numbers
            sum += digit;
        }

        int rem = sum % 10;
        if (rem!=0){
            int rebase = sum - rem + 10;
            return rebase - sum;
        }
        return 0;
    }

    //todo:by using random , we should check for existence first

    /**
     * Creates a new user account.
     * @param random - random generator.
     **/
    public void createRandomAccount(Random random) {
        /*
         * 400000 : 6 digits
         * id : 9 digits
         * checksum: 9 (for now)
         */
        String accountId = randomAccountId(random);

        int upper = 10000;
        int lower = 1000;
        String cardPin =String.valueOf(random.nextInt(upper - lower) + lower);//

        String cardNumber = BIN.concat(accountId)
                .concat(String.valueOf(generateChecksum(BIN.concat(accountId))));

        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, cardPin);
            preparedStatement.executeUpdate();

            System.out.println("Your card has been created");
            System.out.println("Your card number:");// 4000004938320895
            System.out.println(cardNumber);
            System.out.println("Your card PIN:");
            System.out.println(cardPin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * performs sign in and checks for credentials
     * @param cardNumber - account card number.
     * @param cardPin - account card PIN.
     * @return  - true if login was successful.
     **/
    public boolean login(String cardNumber, String cardPin) {

        String sql = "SELECT number,pin FROM card WHERE number = ? and pin = ?";
        try (PreparedStatement stmt =
                     connection.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            stmt.setString(2, cardPin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
