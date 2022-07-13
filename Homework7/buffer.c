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
    sem_wait(&empty);
    pthread_mutex_lock(&mutex);

    if (insertPos < BUFFER_SIZE) {
        buffer[insertPos++] = item;
        insertPos %= BUFFER_SIZE;
    } else {
        returnVal = -1;
    }

    pthread_mutex_unlock(&mutex);
    sem_post(&empty);
    return returnVal;
}

int remove_item(buffer_item *item) {
    int returnVal = 0;
    sem_wait(&empty);
    pthread_mutex_lock(&mutex);

    if (insertPos > 0) {
        *item = buffer[removePos];
        buffer[removePos++] = -1;
        removePos %= BUFFER_SIZE;
    }

    pthread_mutex_unlock(&mutex);
    sem_post(&empty);
    return returnVal;
}

int initialize_buffer() {
    sem_init(&empty, 0, BUFFER_SIZE - 1);
    sem_init(&full, 0, 0);
    pthread_mutex_init(&mutex, NULL);
    return 0;
}

int deinitialize_buffer() {
    sem_destroy(&empty);
    sem_destroy(&full);
    pthread_mutex_destroy(&mutex);
    return 0;
}