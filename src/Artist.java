import secret.Secret;

import java.sql.*;


public class Artist {
     Connection connection;
     private final String databaseName = "laboration3";     //todo: keep global variable or write inline?

    public static void main(String[] args)  {
        Artist artist = new Artist();
        artist.start();
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

//        PreparedStatement statement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS ?");
//        statement.setString(1, databaseName);

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
