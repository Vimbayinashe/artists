import secret.Secret;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Artists {
    private Connection connection;

    public Artists() {
        try{
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    Secret.user,
                    Secret.password
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> getArtistsByName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Artist WHERE first_name LIKE ? OR last_name LIKE ?"
        );
        statement.setString(1, name + "%");
        statement.setString(2, name + "%");
        ResultSet resultSet = statement.executeQuery();

        return manyArtistsAsList(resultSet);
    }

    public List<List<String>> getArtistsByAge(int age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist WHERE age = ?");
        statement.setInt(1, age);
        ResultSet resultSet = statement.executeQuery();

        return manyArtistsAsList(resultSet);
    }

    public List<List<String>> manyArtistsAsList(ResultSet resultSet) throws SQLException {
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

    public void deleteArtist(int id) throws SQLException {
        if(!artistExists(id))
            return;

        PreparedStatement statement = connection.prepareStatement("DELETE FROM Artist WHERE artist_id = ?");
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    public void updateAge(int age, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE Artist SET age = ? WHERE artist_id = ?");
        statement.setInt(1, age);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void updateLastName(String lastName, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE Artist SET last_name = ? WHERE artist_id = ?");
        statement.setString(1, lastName);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void updateFirstName(String firstName, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Artist SET first_name = ? WHERE artist_id = ?"
        );
        statement.setString(1, firstName);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void updateArtist(int id, String firstName, String lastName, int age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Artist SET first_name = ?, last_name = ?, age = ? WHERE artist_id = ?"
        );
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setInt(3, age);
        statement.setInt(4, id);
        statement.executeUpdate();
    }

    public List<String> getByID(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist WHERE artist_id = ?");
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        return artistAsList(resultSet);
    }

    public List<List<String>> getArtists() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist");
        ResultSet resultSet = statement.executeQuery();

        return manyArtistsAsList(resultSet);
    }

    public Boolean artistExists(int id) throws SQLException {
        List<String> artist = getByID(id);
        return !artist.isEmpty();
    }

    public Boolean artistExists(String firstName, String lastName) throws SQLException {
        List<String> artist = getByName(firstName, lastName);
        return !artist.isEmpty();
    }

    public List<String> addArtist(String firstName, String lastName, int age) throws SQLException {
        List<String> artist = getByName(firstName, lastName);

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

        return getByName(firstName, lastName);
    }

    public List<String> getByName(String firstName, String lastName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Artist WHERE first_name = ? AND last_name = ?"
        );
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        ResultSet resultSet = statement.executeQuery();

        return artistAsList(resultSet);
    }

    public List<String> artistAsList(ResultSet resultSet) throws SQLException {
        List<String> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(resultSet.getString("artist_id"));
            list.add(resultSet.getString("first_name"));
            list.add(resultSet.getString("last_name"));
            list.add(resultSet.getString("age"));
        }

        return list;
    }

    public void start()  {
        try {
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

    public void addTestData() throws SQLException {
        addArtist("Miriam", "Makeba", 76);
        addArtist("Stevie", "Wonder", 71);
        addArtist("Alicia", "Keys", 40);
        addArtist("Gemma", "Griffiths", 34);
        addArtist("Jah", "Prayzah", 41);
        addArtist("Mary J", "Blige", 50);
        addArtist("Michelle", "Williams", 42);
        addArtist("Kelly", "Rowlands", 40);
        addArtist("Shingisai", "Suluma", 56);
        addArtist("Fungisai", "Zvakavepano", 46);
    }

}
