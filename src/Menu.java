import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern pattern = Pattern.compile(",");

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.run();
    }

    private void run() {
        Artists artists = new Artists();
        artists.start();
        menu(artists);
    }

    private void menu(Artists artists) {       //todo: add try-catch here?
        int selection;
        do {
            Menu.printMenuOptions();
            selection = Menu.handleSelection(scanner, 7);
            executeSelection(selection, artists);
        } while (selection != 0);
    }

    private void executeSelection(int selection, Artists artists) {
        try {
            switch (selection) {
                case 0 -> shutDown();
                case 1 -> addArtist(artists);
                case 4 -> showAllArtists(artists);
                case 5 -> findArtistByID(artists);
                default -> System.out.println("Invalid selection");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findArtistByID(Artists artists) throws SQLException {
        int id = getArtistIdFromUser(artists);

        if(id == 0) {
            cancel();
            return;
        }

        List<String> artist = artists.getByID(id);

        System.out.println("Artist ID number was found.");
        printOne(artist);
    }

    private void cancel() {
        System.out.println("cancelling...");
    }

    private int getArtistIdFromUser(Artists artists) throws SQLException {
        String userInput;
        while(true) {
            System.out.println("Please enter an Artist's ID or 'x' to cancel:");
            userInput = scanner.nextLine().trim();
            
            if(isCancelled(userInput)) {
                userInput = "0";
                break;
            }
            
            if(Guard.Against.InvalidInt(userInput) || Integer.parseInt(userInput) < 1) {
                System.out.println("Please enter a valid Artist ID number.");
                continue;
            }
            
            if(!artists.artistExists(Integer.parseInt(userInput))) {
                System.out.println("Artist ID could not be found.");
                continue;
            }
            
            break;
        }
        return Integer.parseInt(userInput);
    }

    private void shutDown() {
        System.out.println("Closing program...");
    }

    private void showAllArtists(Artists artists) throws SQLException {
        printMany(artists.getArtists());
    }

    private void addArtist(Artists artists) throws SQLException {
        String[] userInput;

        userInput = getNewArtistDetails();

        if(isCancelled(userInput)) {
            System.out.println("Adding new artist cancelled..");
            return;
        }

        String firstName = userInput[0].trim();
        String lastName = userInput[1].trim();
        int age = Integer.parseInt(userInput[2].trim());

        if(artists.artistExists(firstName, lastName)) {
            System.out.println(firstName + " " + lastName + " is already saved.");
            return;
        }

        List<String> newArtist = artists.addArtist(firstName, lastName, age);

        System.out.println("New artist added:");
        printOne(newArtist);
    }

    private String[] getNewArtistDetails() {
        String[] userInput;
        while(true) {
            System.out.println(
                    "Please enter new artist's firstname, lastname & age (separated by a comma) or 'x' to cancel:");
            userInput = pattern.split(scanner.nextLine());

            if(isCancelled(userInput)) {
                break;
            }

            if(userInput.length < 3) {
                System.out.println("All the details stated below are required for a new artist.");
                continue;
            }

            if(userInput[0].trim().equals("")) {
                System.out.println("An artist requires a firstname.");
                continue;
            }

            if(userInput[1].trim().equals("")) {
                System.out.println("An artist requires a lastname.");
                continue;
            }
            if(Guard.Against.InvalidInt(userInput[2].trim())) {
                System.out.println("An artist requires a valid age.");
                continue;
            }
            if(Integer.parseInt(userInput[2].trim()) < 16) {
                System.out.println("An artist should be at least 16 years old.");
                continue;
            }

            break;
        }
        return userInput;
    }

    private boolean isCancelled(String[] userInput) {
        return userInput.length == 1 && userInput[0].equalsIgnoreCase("x");
    }

    private boolean isCancelled(String userInput) {
        return userInput.equalsIgnoreCase("x");
    }

    public static void printMenuOptions() {
        System.out.println(
                """
                
                Please select a number:
                1. Add an artist
                2. Delete an artist
                3. Update an artist
                4. Show all artists
                5. Find an artist by ArtistID
                6. Find artists by age
                7. Find artists by name
                0. Exit program
                """
        );
    }

    public static int handleSelection (Scanner scanner, int count) {
        int selection = 0;
        while (true) {
            try {
                selection = Integer.parseInt(scanner.nextLine());
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number corresponding to the menu.");
                continue;
            }

            if(selection < 0 || selection > count) {
                System.out.println("Try again. Please enter a number corresponding to the menu.");
            } else
                break;
        }
        return selection;
    }

    public void printMany(List<List<String>> artists) {
        printHeader();
        artists.forEach(this::printArtistDetails);
    }

    public void printOne(List<String> artist) {
        printHeader();
        printArtistDetails(artist);
    }

    public void printArtistDetails(List<String> artist) {
        artist.forEach(detail -> System.out.print(detail + "   "));
        System.out.println();
    }

    public void printHeader() {
        System.out.println("ID  Firstname   Lastname   Age");
    }

}
