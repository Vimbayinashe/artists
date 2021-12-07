import java.sql.SQLException;
import java.util.*;
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
                case 2 -> deleteArtist(artists);
                case 3 -> updateArtist(artists);
                case 4 -> showAllArtists(artists);
                case 5 -> findArtistByID(artists);
                case 6 -> findArtistsByAge(artists);
                case 7 -> findArtistsByName(artists);
                default -> System.out.println("Invalid selection");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateArtist(Artists artists) throws SQLException {
        int id = getArtistIdFromUser(artists);

        if (id == 0) {
            cancel();
            return;
        }

        List<String> artist = artists.getByID(id);
        Map<String, String> newDetails = getUpdateDetails(artist);

        if(newDetails.isEmpty()) {
            cancel();
            return;
        }

        if(newDetails.containsKey("firstName"))
            artists.updateFirstName(newDetails.get("firstName"), id);

        if(newDetails.containsKey("lastName"))
            artists.updateLastName(newDetails.get("lastName"), id);

        if(newDetails.containsKey("age"))
            artists.updateAge(Integer.parseInt(newDetails.get("age")), id);

        List<String> updatedArtist = artists.getByID(id);

        System.out.println("The artist's updated details:");
        printOne(updatedArtist);
    }

    private Map<String, String> getUpdateDetails(List<String> artist) {
        Map<String, String> updateDetails = new HashMap<>();

        System.out.println("You are updating " + artist.get(1) + " " + artist.get(2) + " (" + artist.get(3) +" years).");
        System.out.println("Leave a blank line to skip or 'x' to cancel this update.");

        System.out.println("Enter a new firstname:");
        String firstName = scanner.nextLine().trim();

        if (isCancelled(firstName)) {
            return Map.of();
        } else if (!firstName.isEmpty()) {
            updateDetails.put("firstName", firstName);
        }

        System.out.println("Enter a new lastname:");
        String lastName = scanner.nextLine().trim();

        if (isCancelled(lastName)) {
            return Map.of();
        } else if (!lastName.isEmpty()) {
            updateDetails.put("lastName", lastName);
        }

        System.out.println("Enter a new age:");
        String age = getAgeUpdate();

        if (isCancelled(age)) {
            return Map.of();
        } else if (!age.isEmpty()) {
            updateDetails.put("age", age);
        }

        return updateDetails;
    }

    private String getAgeUpdate() {
        String userInput;

        while(true) {
            userInput = scanner.nextLine().trim();

            if (isCancelled(userInput)) {
                userInput = "x";
                break;
            }

            if(userInput.isEmpty())
                break;

            if (Guard.Against.InvalidInt(userInput)) {
                System.out.println("Please enter a valid age.");
                continue;
            }
            if (Integer.parseInt(userInput) < 16) {
                System.out.println("Minimum age allowed is 16 years. Please try again.");
                continue;
            }

            break;
        }
        return userInput;
    }

    private void deleteArtist(Artists artists) throws SQLException {
        int id = getArtistIdFromUser(artists);
        List<String> artist = artists.getByID(id);

        boolean deleteArtist = confirmDelete(artist.get(1), artist.get(2));

        if(deleteArtist) {
            artists.deleteArtist(id);
            System.out.println("Artist successfully deleted.");
        } else {
            System.out.println("Artist not deleted.");
        }

    }

    private boolean confirmDelete(String firstName, String lastName) {
        boolean delete;
        String userInput;

        while (true){
            System.out.println("Do you want to delete " + firstName + " " + lastName + "?");
            System.out.println("Enter Y for yes or N for no:");
            userInput = scanner.nextLine().trim();

            if(userInput.equalsIgnoreCase("y") ) {
                delete = true;
                break;
            }
            if(userInput.equalsIgnoreCase("n")) {
                delete = false;
                break;
            }
        }
        return delete;
    }

    private void findArtistsByAge(Artists artists) throws SQLException {
        int age = getAgeFromUser();

        if(age == 0) {
            cancel();
            return;
        }

        List<List<String>> selectedArtists = artists.getArtistsByAge(age);

        if(selectedArtists.isEmpty()) {
            System.out.println("No " + age + " year old artists found.");
            return;
        }

        System.out.println(age + " year old artists:");
        printMany(selectedArtists);
    }

    private int getAgeFromUser() {
        String userInput;
        while (true) {
            System.out.println("Enter the age to search for or 'x' to cancel:");
            userInput = scanner.nextLine().trim();

            if(isCancelled(userInput)) {
                userInput = "0";
                break;
            }

            if(Guard.Against.InvalidInt(userInput) || Integer.parseInt(userInput) < 16) {
                System.out.println("Please enter a valid age.");
                continue;
            }

            break;
        }

        return Integer.parseInt(userInput);
    }

    private void findArtistsByName(Artists artists) throws SQLException {
        String name = getName();

        if(isCancelled(name)) {
            cancel();
            return;
        }

        List<List<String>> selectedArtists = artists.getArtistsByName(name);

        if(selectedArtists.isEmpty()) {
            System.out.println(name + " not found.");
            return;
        }

        System.out.println("Artists with the name '" + name + "' :");
        printMany(selectedArtists);
    }

    private String getName() {
        String userInput;
        while (true) {
            System.out.println("Enter the name to search for or 'x' to cancel:");
            userInput = scanner.nextLine().trim();

            if(isCancelled(userInput))
                break;

            if(userInput.equals(""))
                continue;

            break;
        }

        return userInput;
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

            if(isCancelled(userInput))
                break;


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
        int selection;
        while (true) {
            try {
                selection = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number corresponding to the menu.");
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
