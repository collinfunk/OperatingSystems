# Banker's Algorithm

## What is the Banker's Algorithm?

The banker's algorithm is an algorithm that was initially developed by Edsger Dijkstra. The algorithm is used for resource allocation and is used to avoid deadlocks. It does this by simulating the allocation of a pre-defined set of maximum resources to each process. Using this information, the algorithm creates a safe state to check for possible deadlock conditions for the pending activities. The algorithm then decides if it is safe to allocate the resources. The algorithm was named the Banker's Algorithm because it modeled a system that could be used by a bank to ensure it never allocated its available cash in a way that it could no longer service its customers.

## Deadlocks

As mentioned before, the banker's algorithm is a form of deadlock avoidance; meaning that it is not a form of deadlock prevention. To understand this distinction, it is important to understand what a deadlock is. A deadlock is a situation in a computer system where no process can proceed because each process is waiting for another member, including itself, to complete an action, such as sending a message or releasing a lock (e.g. a mutex).

### Deadlock Conditions

-   Mutual Exclusion: Only one process can access a resource at a time.
-   Hold and Wait: A process is holding a resource and waiting for other resources.
-   No Preemption: Only a process holding a resource can release the said resource. Other processes cannot release it and "preempt" the process.
-   Circular Wait: The processes are waiting for each other in a way that forms a circle. If we have processes A, B, C, and D, A is waiting for B, B is waiting for C, and so on, but D is also waiting for A, creating a circle.

## Code

The following sections describe the code for Banker.java and the implementation of the Banker's Algorithm.

### Safety Algorithm

Below is the code for the banker's algorithm implementation. This algorithm checks if the current state of the system is safe or not. It begins by creating two arrays, a boolean array finish which is marked true when a customer process is finished. The second is an int array work which is initialized to be a copy of the available resources. The algorithm then loops over the number of customers and initializes each value in finish to false, we can refer to this loop as loop one to describe its use later. It then enters loop two, again looping over the number of customers. Inside this loop, we check that the customer is finished, or if the finished array is false at the index for the customer. If it is not finished, we set a temporary boolean to true and enter loop three, this time looping over the number of resources. Inside this loop, we check if the current customer and current resource are greater than the resource available (using the work array we initialized before). If they need more than is available, we set temp to false, and loop three is exited. Upon the exit of loop three, if temp is true (conditions were met and temp wasn't set to false) we add the allocation of the current resource in the work array by adding the allocation of the current customer. The index of the current customer is then used in the finished array to mark the customer as finished. This process is repeated for loops two and three to check each customer and each resource. After the process is completed we use loop one, the one that initialized the finished array to false for each index at the beginning. This loop is then used again to check if the finished array is false at any index. If it is, we return -1 (not safe) otherwise we return 0 (safe).

```
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

            // if not safe return -1
            if (!finish[i]) {
                return -1;
            }


         }
         // if finish array is all true, then the system is in a safe state
        return 0;
    }
}
```

### Release Resources + Helper Functions

These functions are used for releasing resources. Releasing the resources refers to modifying the state of the bank by updating a process due to a request while also changing the state of the system as a whole. the Initial release resources function takes the customer number and the resources to be released from that customer. This calls a helper function change resources, which takes the same arguments while also taking an integer. This integer is used to determine if we are adding or subtracting the request array from the available resources. The function has three lines, each calling a helper function that adds or subtracts two arrays. The add function takes the same arguments and adds the second array argument to the first one. If a -1 is passed as the third argument it adds the second array as negative (subtraction) or if it is 1 then it adds it unaltered (addition). In the change resource function, we see that we add or subtract the request to the available resources, do the opposite operation when modifying the allocation of the customer, and then add or subtract the request from the need of the customer, as passed in by the argument. The release resource function is used to update the state of the bank as a whole when modifications are made and a customer makes a request, and the helper functions make it easier to update the state and then eventually revert the changes if needed.

```
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
```

### Request Resources

The following function is used to request resources for a customer from the bank. The function prints out the request that it is handling and then enters a loop over the number of resources. Then two if statements are used to check if the request can be continued. We first check that the request is less than or equal to the needs of the customer and also that the bank has the resources available that are being requested. If these conditions aren't satisfied, we print out an error message and return -1 to indicate that the request was not successful. If these conditions are satisfied we modify the state of the bank to reflect the request. Then we check if the bank is in a safe state by using the function described earlier. If this function returns 0, then the request was successful and we return 0. If not we print an error and return -1.

```
public int request_resources(int customer_num, int request[]) {
        System.out.println("Customer " + customer_num + " requests: " + Arrays.toString(request));
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            // if requests less than equal to need, continue
            if (request[i] <= need[customer_num][i]) {
                // if request is less than avaliable, continue
                if (request[i] <= avaliable[i]) {
                    continue;

                }
                else {
                    // if request is greater than avaliable, return -1
                    System.out.println("Requested resources are not available");
                    return -1;
                }

            }
            else {
                // if request is greater than need, return -1
                System.out.println("Request is greater than need");
                return -1;
            }
        }
        chng_resources(customer_num, request, -1);
        if (safteyCheck() == 0) {
            return 0;
        }
        else {
            chng_resources(customer_num, request, 1);
            System.out.println("Requested resources are not available, deadlock detected");
            return -1;
        }

    }
```

### Simulation of Bank

The main function of this program begins by printing a header to let the user know the size of the array. It then tries to open the user's input file and if it is not successful prompts the user to input the name. After this, it reads the file with the numbers separated by commas and enters them into the maximum array. Following this, the allocation for each resource and customer is set up. To do this we set each value to a random value, however, we ensure that this random value cannot leave need to be negative. This ensures that we set up a situation that is safe for user input. Following this, we use the function input_loop(). This essentially is a switch statement that recursively calls itself. This handles commands that the user enters such as request, release, and printing the values of the data structures. I used a switch statement because it is easy to validate that the commands are valid. In the request and release sections, there are also conditionals to check that the values entered are valid for the bank.

```
private void readAvaliable() {
        System.out.print("Enter the avaliable resources seperated by spaces: ");
        String userInput = scanner.nextLine();
        String[] input = userInput.split(" ");
        while (input.length != NUMBER_OF_RESOURCES) {
            System.out.println("Error: You must enter " + NUMBER_OF_RESOURCES + " resources");
            userInput = scanner.nextLine();
            input = userInput.split(" ");
        }
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            avaliable[i] = Integer.parseInt(input[i]);
        }
    }

    private void setup_allocation() {
        // setup allocation with random numbers such that need is not less than 0
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                allocation[i][j] = (int) (Math.random() * (maximum[i][j] + 1));
            }
        }
    }

    private void read_file(String filename) throws Exception {
        Path file = Paths.get(filename);
        while (!Files.exists(file)) {
            System.out.print("File does not exist. Please enter a valid file name: ");
            filename = scanner.next();
            file = Paths.get(filename);

        }

        BufferedReader buffer = new BufferedReader(new FileReader(filename));
        String line;
        int row = 0;
        int size = NUMBER_OF_RESOURCES;

        while ((line = buffer.readLine()) != null) {
            //split at commas
            String[] vals = line.trim().split(",");
            for (int col = 0; col < size; col++) {
                maximum[row][col] = Integer.parseInt(vals[col]);
            }
            row++;
        }
        buffer.close();
        calc_need();
    }

private void input_loop() {
        String userInput = scanner.nextLine();
        String[] input = userInput.split(" ");
        int customer;
        int[] resources = new int[NUMBER_OF_RESOURCES];


        switch (input[0]) {
            case "RQ":
                if (input.length != 2 + NUMBER_OF_RESOURCES) { // OP CUST RESOURCE1 RESOURCE2 ...
                    System.out.println("Invalid input there must be " + (NUMBER_OF_RESOURCES + 2) + " arguments");
                    break;
                }
                customer = Integer.parseInt(input[1]) + 1;
                if (customer - 1 < 0 || customer -1 > NUMBER_OF_CUSTOMERS) {
                    System.out.println("Invalid customer");
                    break;
                }
                for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                    resources[i] = Integer.parseInt(input[i + 2]);
                }
                request_resources(customer - 1, resources);
                break;
            case "RL":
                if (input.length != 2 + NUMBER_OF_RESOURCES) { // OP CUST RESOURCE1 RESOURCE2 ...
                    System.out.println("Invalid input there must be " + (NUMBER_OF_RESOURCES + 2) + " arguments");
                    break;
                }
                customer = Integer.parseInt(input[1]);
                if (customer -1 < 0 || customer -1 > NUMBER_OF_CUSTOMERS) {
                    System.out.println("Invalid customer");
                    break;
                }
                for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                    resources[i] = Integer.parseInt(input[i + 2]);
                }
                release_resources(customer - 1, resources);
                break;
            case "*":
                System.out.println("\n");
                show_all();
                break;
            case "Q":
                System.out.println("Goodbye");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, RQ, RQ, *, or Q for quit");
                break;
        }
        input_loop();
    }
```

## Some test cases

# Conclusion

This project gave me a clear example of the idea of safe states and how the banker's algorithm uses them to avoid deadlocks. Also when researching how to write the algorithm I learned more about the distinction between avoiding deadlocks and preventing them. The banker's algorithm is a form of deadlock avoidance. In a practical sense, the banker's algorithm requires a lot of information to be able to be implemented. Knowing the allocation and maximum amounts a process can request is very often not possible but is required for the banker's algorithm. I don't know Java very well and had to relearn how to read files and initialize arrays, so if I had more time I would have liked to implement multi-threading to allow for multiple processes to run at the same time. I think that in a practical sense the banker's algorithm could lead to substantial waiting that is not ideal for certain situations. If I were to implement a multi-threaded version of the banker's algorithm, I feel that I would learn more about that. I feel like I could have done this in C, but I do not know Java well enough. Overall though, I feel like this project has taught me a lot about deadlock avoidance and deadlock prevention and the pros and cons of each. For example, Deadlock prevention would not have the problems that deadlock avoidance have and the risk of long wait times that the bankers algorithm could theoretically have, since a process could be preempted, for example.
