# Banker's Algorithm

## What is the Banker's Algorithm?

The banker's algorithm is an algorithm that was initially developed by Edsger Dijkstra. The algorithm is used for resource allocation and is used to avoid deadlocks. It does this by simulating the allocation of a pre-defined set of maximum resources to each process. Using this information, the algorithm creates a safe state to check for possible deadlock conditions for the pending activities. The algorithm then decides if it is safe to allocate the resources. The algorithm was named the Banker's Algorithm because it modeled a system that could be used by a bank to ensure it never allocated its available cash in a way that it could no longer service its customers.

## Deadlocks

As mentioned before, the banker's algorithm is a form of deadlock avoidance; meaning that it is not a form of deadlock prevention. To understand this distinction, it is important to understand what a deadlock is. A deadlock is a situation in a computer system where no process can proceed because each process is waiting for another member, including itself, to complete an action, such as sending a message or releasing a lock (e.g. a mutex).

### Deadlock Conditions

-   Mutual Exclusion
-   Hold and Wait
-   No Preemption
-   Circular Wait

## Code

### Safety Algorithm

-   TODO

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

       }
       // if finish array is all true, then the system is in a safe state
       for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
           if (!finish[i]) {
               return -1;
           }
       }
       return 0;
  }
```

### Request Resources + Helper Functions

-   TODO

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
