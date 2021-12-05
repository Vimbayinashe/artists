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


            testData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<String> updateAge(int age, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Artist SET age = ? WHERE artist_id = ?;"
        );
        statement.setInt(1, age);
        statement.setInt(2, id);

        statement.executeUpdate();

        return getByID(id);
    }

    private List<String> updateLastName(String lastName, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Artist SET last_name = ? WHERE artist_id = ?;"
        );
        statement.setString(1, lastName);
        statement.setInt(2, id);

        statement.executeUpdate();

        return getByID(id);
    }

    private List<String> updateFirstName(String firstName, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Artist SET first_name = ? WHERE artist_id = ?;"
        );
        statement.setString(1, firstName);
        statement.setInt(2, id);

        statement.executeUpdate();

        return getByID(id);
    }

    private List<String> updateArtist(String firstName, String lastName, int age, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Artist SET first_name = ?, last_name = ?, age = ? WHERE artist_id = ?;"
        );
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setInt(3, age);
        statement.setInt(4, id);

        statement.executeUpdate();

        return getByID(id);
    }

    private List<String> getByID(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist WHERE artist_id = ?");
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        return artistAsList(resultSet);
    }

    private void testData() throws SQLException {
        //adding artists
//        List<String> newArtist = add("Yvonne", "Chaka Chaka", 75);
//        List<String> newArtist1 = add("Michael", "Jackson", 56);
//        List<String> newArtist2 = add("Michelle", "Knowlands", 41);
//        List<String> newArtist3 = add("Michael", "Smith", 51);
        //printOne(newArtist);

        //updateData
//        List<String> updateName = updateFirstName("Kelly", 3);
//        List<String> updateName1 = updateLastName("Chaka-Chaka", 1);
//        List<String> updateAge = updateAge(56, 1);

        //get all artists
        List<List<String>> artists = getArtists();
        printMany(artists);
    }

    private List<List<String>> getArtists() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist");
        ResultSet resultSet = statement.executeQuery();

        List<List<String>> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(List.of(
                    resultSet.getString("artist_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("age")
            ));
        }

        return list;
    }

    public Boolean artistNotFound(String firstName, String lastName) throws SQLException {
        List<String> artist = findByName(firstName, lastName);
        return artist.isEmpty();
    }


    //todo: check if artistNotFound BEFORE attempting add()
    public List<String> add(String firstName, String lastName, int age) throws SQLException {
        List<String> artist = findByName(firstName, lastName);

        if(!artist.isEmpty()) {
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

        return artistAsList(resultSet);
    }

    private List<String> artistAsList(ResultSet resultSet) throws SQLException {
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


    private void printMany(List<List<String>> artists) {
        printHeader();
        artists.forEach(this::printArtistDetails);
    }

    private void printOne(List<String> artist) {
        printHeader();
        printArtistDetails(artist);
    }

    private void printArtistDetails(List<String> artist) {
        artist.forEach(detail -> System.out.print(detail + "   "));
        System.out.println();
    }

    private void printHeader() {
        System.out.println("ID  Firstname   Lastname   Age");
    }
}
