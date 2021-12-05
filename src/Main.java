import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //Menu menu = new Menu();     //todo: use Menu as utitlity class
        Artists artists = new Artists();

        artists.run();

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

}
