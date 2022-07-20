#include "buffer.h"
#include "producer.c"
#include "consumer.c"
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>
#include <semaphore.h>

int main(int argc, char* argv[]) {
    int sleepTime, producerCount, consumerCount;
    int producerThreadCount, consumerThreadCount;
    char* remainder;

    if (argc != 4) {
        printf("Error: Enter in format ./buffer (Integer representing how long to sleep between producing and consuming) (Integer representing how many producers) (Integer representing how many consumers)\n");
        exit(1);
    }

    sleepTime = strtol(argv[1], &remainder, 0);
    producerCount = strtol(argv[2], &remainder, 0);
    consumerCount = strtol(argv[3], &remainder, 0);

    if (sleepTime <= 0 || producerCount <= 0 || consumerCount <= 0) {
        printf("Inputs must be positive integers\n");
        exit(1);
    }

    initialize_buffer();

    pthread_t producers[producerCount];
    pthread_t consumers[consumerCount];

    for (producerThreadCount = 0; producerThreadCount < producerCount; producerThreadCount++) {
        pthread_create(&producers[producerThreadCount], NULL, &producer, NULL);
    }

    for (consumerThreadCount = 0; consumerThreadCount < consumerCount; consumerThreadCount++) {
        pthread_create(&consumers[consumerThreadCount], NULL, &consumer, NULL);
    }

    sleep(sleepTime);

    for (producerThreadCount = 0; producerThreadCount < producerCount; producerThreadCount++) {
        pthread_join(producers[producerThreadCount], NULL);
    }

    for (consumerThreadCount = 0; consumerThreadCount < consumerCount; consumerThreadCount++) {
        pthread_join(consumers[consumerThreadCount], NULL);
    }

    deinitialize_buffer();
    return 0;
}