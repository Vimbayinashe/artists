import secret.Secret;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Artists {
    private Connection connection;
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)  {
        Artists artists = new Artists();
        artists.run();

    }

    public void run() {
        start();
        //menu();

        try {
            List<String> newArtist = add("Yvonne", "Chaka Chaka", 75);
            printOne(newArtist);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void printArtistNames(List<String> artists) {
//        if(artists.isEmpty())
//            System.out.println("No artists found");
//        else {
//            System.out.println("ID  Firstname   Lastname   Age");
//            artists.forEach(System.out::println);
//        }
//    }

    private void printOne(List<String> artist) {
        if(artist.isEmpty())
            System.out.println("No artist found");
        else {
            System.out.println("ID  Firstname   Lastname   Age");
            artist.forEach(detail -> System.out.print(detail + "   "));
        }
    }

    public Boolean artistNotFound(String firstName, String lastName) throws SQLException {
        List<String> artist = findByName(firstName, lastName);
        return artist.isEmpty();
    }


    //todo: check if artistNotFound BEFORE attempting add()
    public List<String> add(String firstName, String lastName, int age) throws SQLException {
        List<String> artist = findByName(firstName, lastName);

        if(!artist.isEmpty()) {
            //System.out.println(firstName + " " + lastName + " is already in the database");
            return artist;
        }

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Artist (first_name, last_name, age) VALUES (?, ?, ?)"
        );
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setInt(3, age);
        statement.executeUpdate();

        return findByName(firstName, lastName);

    }

    private List<String> findByName(String firstName, String lastName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Artist WHERE first_name = ? AND last_name = ?"
        );
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        ResultSet resultSet = statement.executeQuery();

        List<String> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(resultSet.getString("artist_id"));
            list.add(resultSet.getString("first_name"));
            list.add(resultSet.getString("last_name"));
            list.add(resultSet.getString("age"));
        }

        return list;
    }


    private void menu() {       //todo: add try-catch here?
        int selection;
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
                "artist_id int PRIMARY KEY AUTO_INCREMENT," +
                "first_name varchar(50) NOT NULL," +
                "last_name varchar(50) NOT NULL," +
                "age smallint NOT NULL," +
                "CONSTRAINT UniqueArtist UNIQUE (first_name, last_name)" +
                ")"
        );
        statement.execute();
    }

    private void createDatabase() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS laboration3");
        statement.execute();

        statement = connection.prepareStatement("USE laboration3");
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
