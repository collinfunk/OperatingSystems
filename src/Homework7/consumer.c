#include "buffer.h"
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

void *consumer (void *param) {
    buffer_item item;
    
    while (1) {
        int sleepTime = rand() % 5;
        sleep(sleepTime);
        if (remove_item(&item)) {
            printf("Error removing from buffer\n");
        } else {
            printf("consumed %d\n", item);
        }
    }
}