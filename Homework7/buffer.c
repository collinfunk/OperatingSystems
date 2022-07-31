#include "buffer.h"
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>
#include <semaphore.h>

buffer_item buffer[BUFFER_SIZE];
pthread_mutex_t mutex;
sem_t empty, full;
int insertPos = 0, removePos = 0;

int insert_item(buffer_item item) {
    int returnVal = 0;
    sem_wait(&full); // Wait until there is space in the buffer
    pthread_mutex_lock(&mutex);
    if (insertPos < BUFFER_SIZE) {
        buffer[insertPos++] = item;
        insertPos %= BUFFER_SIZE;
    } else {
        printf("Error: Buffer is full\n");
        returnVal = -1;
    }

    pthread_mutex_unlock(&mutex);
    sem_post(&empty);
    return returnVal;
}

int remove_item(buffer_item *item) {
    int returnVal = 0;
    sem_wait(&empty); // Wait until there is something in the buffer
    pthread_mutex_lock(&mutex);

    if (insertPos > 0) {
        *item = buffer[removePos];
        buffer[removePos++] = -1;
        removePos %= BUFFER_SIZE;
    }

    pthread_mutex_unlock(&mutex);
    sem_post(&full); // Signal that there is space in the buffer
    return returnVal;
}

int initialize_buffer() {
    sem_init(&empty, 0, 0); // Initialize empty semaphore to 0
    sem_init(&full, 0, BUFFER_SIZE); // Initialize full semaphore to BUFFER_SIZE
    pthread_mutex_init(&mutex, NULL); 
    return 0;
}

int deinitialize_buffer() {
    sem_destroy(&empty); // Destroy empty semaphore
    sem_destroy(&full); // Destroy full semaphore
    pthread_mutex_destroy(&mutex);
    return 0;
}