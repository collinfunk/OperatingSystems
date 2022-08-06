

package bankersalgo;

import java.util.Scanner;

import java.util.Arrays;

public class BankerMain {
    private static final int NUMBER_OF_CUSTOMERS = 3;
    private static final int NUMBER_OF_RESOURCES = 4; // m - number of resources

    private int avaliable[] = new int[NUMBER_OF_RESOURCES];
    private int maximum[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    private int allocation[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    private int need[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];

    private Scanner scanner = new Scanner(System.in);
    private static BankerMain banker = new BankerMain();

    public static void main(String[] args) {
        System.out.println("Welcome to the Banker's Algorithm");
        System.out.println("You have " + NUMBER_OF_CUSTOMERS + " customers");
        System.out.println("You have " + NUMBER_OF_RESOURCES + " resources");
        System.out.println(
                "You will prompted to enter the amount avaliable for each resource, maximum for each customer, and allocation for each customer");
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
            while (avaliable < 0) {
                System.out.println("Please enter a positive value");
                avaliable = scanner.nextInt();
            }
            this.avaliable[i] = avaliable;
        }
        System.out.println("\n");

        // Initialize the allocation for each customer
        System.out.println("Enter the allocation for each customer: ");
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            System.out.print("Customer " + (i + 1) + ": ");
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                int allocation = scanner.nextInt();
                if (allocation < 0) {
                    System.out.println("Invalid input. Please enter a positive integer value.");
                    i--;
                    j--;
                } else {
                    this.allocation[i][j] = allocation;
                }
            }
        }
        System.out.println("\n");
        System.out.println("Allocation matrix: ");
        print2D(allocation);

        // Initialize the maximum resources for each customer
        System.out.println("Enter the maximum resources for each customer: ");
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            System.out.print("Customer " + (i + 1) + ": ");
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                int maximum = scanner.nextInt();
                if (maximum < 0) {
                    System.out.println("Invalid input. Please enter a positive integer value.");
                    i--;
                    j--;
                } else {
                    this.maximum[i][j] = maximum;
                }
            }
        }

        System.out.println("\nMaximum resources: ");
        print2D(maximum);

        // Initialize the need matrix
        System.out.println("Setting up the need matrix...");
        calc_need();
        System.out.println("Need matrix: ");
        print2D(need);

        // Check if the system is in a safe state
        System.out.println("Checking if the system is in a safe state...");
        if (safteyCheck() == 0) {
            System.out.println("The system is in a safe state");
        } 
        else {
            System.out.println("The system is not in a safe state");
        }
    }

    private void calc_need() {
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
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
        System.out.println(Arrays.toString(avaliable) + "\n");
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
        System.out.println("\n");
    }

    private void clearScreen() {
        System.out.print("\n");
    }

    public void show_all() {
        show_allocation();
        show_need();
        show_avaliable();
        show_maximum();
    }


    private void printMenu() {
        System.out.println("\n");
        System.out.println("1. Request Resources");
        System.out.println("2. Show Allocation");
        System.out.println("3. Show Need");
        System.out.println("4. Show Available");
        System.out.println("5. Show Max");
        System.out.println("6. Show All");
        System.out.println("7. Exit");
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
                show_allocation();
                break;
            case 3:
                show_need();
                break;
            case 4:
                show_avaliable();
                break;
            case 5:
                show_maximum();
                break;
            case 6:
                show_all();
                break;
            case 7:
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
        
        while (!scanner.hasNextInt() || (customerNum = scanner.nextInt()) < 1 || customerNum > NUMBER_OF_CUSTOMERS) {
            System.out.print(
                    "Invalid input. Please enter a positive integer value between 1 and " + NUMBER_OF_CUSTOMERS + ": ");
            scanner.nextLine();
        }

        int request[] = new int[NUMBER_OF_RESOURCES];

        // Input Validation loop, integer input, and range check
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            System.out.print("Enter the amount of resource " + (i + 1) + ": ");
            while (!scanner.hasNextInt() || (request[i] = scanner.nextInt()) < 0) {
                System.out.print("Invalid input. Please enter a positive integer value: ");
                scanner.nextLine();
            }
        }
        clearScreen();
        // print resquests length for debugging
        int result = request_resources(customerNum, request);
        if (result == -1) {
            System.out.println("Requested resources are not available");
        } else {
            System.out.println("Requested resources are available");
        }

    }
    

    // Function to request resources from the system, checks if the request is safe or not, returns values to original state if not safe
    public int request_resources(int customer_num, int request[]) {
        System.out.println("Customer " + customer_num + " requests: " + Arrays.toString(request));
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            // if requests less than equal to need, continue
            if (request[i] <= need[customer_num][i]) {
                // if request is less than avaliable, continue
                if (request[i] <= avaliable[i]) {
                    continue;

                } else {
                    // if request is greater than avaliable, return -1
                    System.out.println("Requested resources are not available");
                    return -1;
                }

            } else {
                // if request is greater than need, return -1
                System.out.println("Request is greater than need");
                return -1;
            }
        }
        chng_resources(customer_num, request, -1);
        if (safteyCheck() == 0) {
            return 0;
        } else {
            chng_resources(customer_num, request, 1);
            System.out.println("Requested resources are not available, deadlock detected");
            return -1;
        }

    }


    // realease function
    public void release_resources(int customer_num, int release[]) {
        chng_resources(customer_num, release, 1);
    }
    
    public void chng_resources(int customer_num, int request[], int change) {
        chng_add(avaliable, request, change);
        chng_add(allocation[customer_num], request, -change);
        chng_add(need[customer_num], request, change);
    }
    
    public void chng_add(int[] array, int request[], int change) {
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            array[i] += request[i] * change;
        }
    }
    
    public void chng_add(int customer_num, int request[], int change) {
        chng_add(allocation[customer_num], request, -change);
        chng_add(need[customer_num], request, change);
    }


     // returns 0 if the system is in a safe state, 1 otherwise
     public int safteyCheck() {
         boolean finish[] = new boolean[NUMBER_OF_CUSTOMERS];
         int work[] = new int[NUMBER_OF_RESOURCES];

         // initalize work to avaliable
         for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
             work[i] = avaliable[i];
         }
         // initialize finish to false
         for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
             finish[i] = false;

             // check if the system is in a safe state
             for (int j = 0; j < NUMBER_OF_CUSTOMERS; j++) { // for each customer
                 if (!finish[j]) { // if the customer is not finished
                     boolean temp = true; // condition to check if the customer is in a safe state
                     for (int k = 0; k < NUMBER_OF_RESOURCES; k++) { // check resources
                         // if the customer needs more than the work, the system is not in a safe state
                         if (need[j][k] > work[k]) {
                             temp = false;
                         }
                     }
                     // if the customer is in a safe state, mark the customer as finished
                     if (temp) {
                         for (int k = 0; k < NUMBER_OF_RESOURCES; k++) {
                             work[k] += allocation[j][k];
                         }
                         finish[j] = true; // the customer is finished
                     }
                 }
             }

         }
         // if finish array is all true, then the system is in a safe state
         for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
             if (!finish[i]) {
                 return -1;
             }
         }
         return 0;
    }
}