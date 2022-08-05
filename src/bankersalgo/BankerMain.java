package bankersalgo;

import java.util.Scanner;
import java.util.Arrays;

public class BankerMain {
    private static final int NUMBER_OF_CUSTOMERS = 5;
    private static final int NUMBER_OF_RESOURCES = 4; // m - number of resources

    private int avaliable[] = new int[NUMBER_OF_RESOURCES];
    private int maximum[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    private int allocation[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    private int need[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];

    private Scanner scanner = new Scanner(System.in);
    private static BankerMain banker = new BankerMain();


    public static void main(String[] args) {
        System.out.println("Welcome to the Banker's Algorithm");
        banker.init();
        banker.printMenu();
        
    }

    public void init() {
        // Initialize the avaliable resources
        System.out.println("Enter the avaliable resources: ");
        // ensure all inputs are integers values and are positive
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            System.out.print("Resource " + (i + 1) + ": ");
            int avaliable = scanner.nextInt();
            if (avaliable < 0) {
                System.out.println("Invalid input. Please enter a positive integer value.");
                i--;
            } else {
                this.avaliable[i] = avaliable;
            }
        }
        System.out.println("\n");

        // Initialize the maximum resources with random values
        System.out.println("Setting up the maximum resources...");
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                maximum[i][j] = (int) (Math.random() * 10);
            }
        }
        System.out.println("Maximum resources: ");
        print2D(maximum);

        // Initialize the allocation matrix with random values
        System.out.println("Setting up the allocation matrix...");
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                allocation[i][j] = (int) (Math.random() * 10);
            }
        }
        System.out.println("Allocation matrix: ");
        print2D(allocation);
        

        // Initialize the need matrix
        System.out.println("Setting up the need matrix...");
        calc_need();
        print2D(need);
    }
    
    private void calc_need() {
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
    }

    public void release_resources(int customer_num, int release[]) {
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            avaliable[i] += release[i];
            allocation[customer_num][i] -= release[i];
            need[customer_num][i] += release[i];
        }
    }

     public void show_allocation() {
        System.out.println("Allocation: ");
        print2D(allocation);
        
    }

    public void show_need() {
        System.out.println("Need: ");
        calc_need();
        print2D(need);
    }

    public void show_avaliable() {
        System.out.println("Avaliable: ");
        System.out.println(Arrays.toString(avaliable) + "\n\n");
    }

    public void show_maximum() {
        System.out.println("Maximum: ");
        print2D(maximum);
    }

    public void print2D(int[][] arr) {
        int count = 0;
        for (int[] row : arr) {
            System.out.print("Customer " + (count + 1) + ": ");
            for (int elem : row) {
                System.out.printf("%4d", elem);
            }
            count++;
            System.out.println();
        }
        System.out.println();
        System.out.println("\n\n");
    }

    public void show_all() {
        show_allocation();
        show_need();
        show_avaliable();
        show_maximum();
    }

    public int safteyCheck() {
        boolean finish[] = new boolean[NUMBER_OF_CUSTOMERS];

        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_CUSTOMERS; j++) {
                if (!finish[j]) {
                    boolean temp = true;
                    for (int k = 0; k < NUMBER_OF_RESOURCES; k++) {
                        if (need[j][k] > avaliable[k]) {
                            temp = false;
                        }
                    }

                    if (temp) {
                        finish[j] = true;
                        for (int k = 0; k < NUMBER_OF_CUSTOMERS; k++) {
                            avaliable[k] += allocation[j][k];
                        }
                    }

                }
            }

        }
        // if finish array is all true, then the system is in a safe state
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            if (!finish[i]) {
                return 1;
            }
        }
        return 0;
    }

    private void printMenu() {
        System.out.println("1. Request Resources");
        System.out.println("2. Release Resources");
        System.out.println("3. Show Allocation");
        System.out.println("4. Show Need");
        System.out.println("5. Show Available");
        System.out.println("6. Show Max");
        System.out.println("7. Show All");
        System.out.println("8. Exit");
        takeInput();
    }

    private void takeInput() {
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                requestResources_prompt();
                break;
            case 2:
                releaseResources();
                break;
            case 3:
                show_allocation();
                break;
            case 4:
                show_need();
                break;
            case 5:
                show_avaliable();
                break;
            case 6:
                show_maximum();
                break;
            case 7:
                show_all();
                break;
            case 8:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
        printMenu();
        takeInput();
    }

    private void requestResources_prompt() {
        int customerNum;
        System.out.print("Enter the customer number: ");
        // Input Validation
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter the customer number: ");
            scanner.next();
        }
        customerNum = scanner.nextInt();

        System.out.print("Enter the request resources: ");
        int request[] = new int[NUMBER_OF_RESOURCES];

        // Input Validation
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            System.out.print("Enter the request resources for resource " + (i + 1) + ": ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter the request resources: ");
                scanner.next();
            }
            request[i] = scanner.nextInt();
        }

        clearScreen();
        int result = request_resources(customerNum, request);
        if (result == -1) {
            System.out.println("Requested resources are not available");
        } else {
            System.out.println("Requested resources are available");
        }

    }

    private void releaseResources() {
        int customerNum = 0;
        System.out.print("Enter the customer number: ");

        // Input Validation
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter the customer number: ");
            scanner.next();
        }
        customerNum = scanner.nextInt();

        int release[] = new int[NUMBER_OF_RESOURCES];

        // Input Validation
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            System.out.print("Enter the release resources for resource " + (i + 1) + ": ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter the release resources: ");
                scanner.next();
            }
            release[i] = scanner.nextInt();
        }
        release_resources(customerNum, release);
        clearScreen();
        System.out.print("Resources are released");

    }

    public int request_resources(int customer_num, int request[]) {
        System.out.println("Customer " + customer_num + " requests: " + Arrays.toString(request));
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            avaliable[i] -= request[i];
            allocation[customer_num][i] += request[i];
            need[customer_num][i] -= request[i];
        }
        int result = safteyCheck();

        // if system is in a safe state, then update the avaliable array
        if (result == 1) {
            return 0;
        }
        // if system is not in a safe state, then rollback the changes
        else {
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                avaliable[i] += request[i];
                allocation[customer_num][i] -= request[i];
                need[customer_num][i] += request[i];
            }
            return -1;
        }
    }


    private void clearScreen() {
        System.out.print("\n\n");
    }
    
}


