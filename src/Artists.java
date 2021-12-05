import secret.Secret;

import java.sql.*;
import java.util.Scanner;


public class Artists {
    private Connection connection;
    private final Scanner scanner = new Scanner(System.in);
    private final String databaseName = "laboration3";     //todo: keep global variable or write inline?

    public static void main(String[] args)  {
        Artists artists = new Artists();
        artists.run();

    }

    private void run() {
        start();
        menu();
    }

    private void menu() {
        int selection = 0;
        do {
            Menu.printMenuOptions();
            selection = Menu.handleSelection(scanner, 7);
            executeSelection(selection);
        } while (selection != 0);
    }

    private void executeSelection(int selection) {
        switch (selection) {
            case 0 -> System.out.println("Closing the program...");
            case 1 -> System.out.println("add artist");
            default -> System.out.println("Invalid selection");
        }
    }


    private void start()  {
        try {
            databaseConnection();
            createDatabase();
            createArtistTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createArtistTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Artist (" +
                "id int PRIMARY KEY AUTO_INCREMENT," +
                "first_name varchar(50) NOT NULL," +
                "last_name varchar(50) NOT NULL," +
                "age smallint NOT NULL" +
                ")"
        );
        statement.execute();
    }

    private void createDatabase() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS " + databaseName);
        statement.execute();

        statement = connection.prepareStatement("USE " + databaseName);
        statement.execute();
    }

    private  void databaseConnection() throws SQLException {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    Secret.user,
                    Secret.password
            );
    }
}
