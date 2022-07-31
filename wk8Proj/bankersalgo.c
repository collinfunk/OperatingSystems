#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

#define NUMBER_OF_CUSTOMERS 5 // n - number of threads
#define NUMBER_OF_RESOURCES 4 // m - number of resources

int available[NUMBER_OF_RESOURCES];
int maximum[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
int allocation[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
int need[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];


// Function to check if the system is in safe state
int request_resources(int customer_num, int request[]) {
    // work and finish are used to check if the system is in safe state
    int work[NUMBER_OF_RESOURCES];
    int finish[NUMBER_OF_CUSTOMERS];

    //work = available and finish[i] is false for i < n
    for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
        work[i] = available[i];
        finish[i] = 0;
    }

    //Find an index i such that both finish[i] == false and need sub i is less than or equal to work
    int i = 0;
    while (finish[i] == 0) {
        int flag = 0;
        for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
            if (need[i][j] > work[j]) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            finish[i] = 1;
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                work[j] += allocation[i][j];
            }
        }
        i++;
    }

    // if finish[i] == true for all i, then the system is in safe state
    int flag = 0;
    for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
        if (finish[i] == 0) {
            flag = 1;
            break;
        }
    }

    // if the system is not in safe state, then work = work + allocation and finish[i] == true
    if (flag == 1) {
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            work[i] += allocation[customer_num][i];
        }
        finish[customer_num] = 1;
    }
}

void release_resources(int customer_num, int release[]) {
    //If Requesti ≤ Needi, go to step 2. Otherwise, raise an error condition, since the thread has exceeded its maximum claim.
    for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
        if (release[i] > need[customer_num][i]) {
            printf("Error: Thread %d has exceeded its maximum claim.\n", customer_num);
            exit(1);
        }
    }
    //If Requesti ≤ Available, go to step 2. Otherwise, raise an error condition, since the thread has exceeded its maximum claim.


}