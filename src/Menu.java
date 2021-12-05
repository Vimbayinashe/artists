import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

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
