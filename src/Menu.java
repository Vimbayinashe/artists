import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.run();
    }

    private void run() {
        Artists artists = new Artists();
        artists.start();
        menu();
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

}
