#include "buffer.h"
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

void *producer (void *param) {
    buffer_item item;

    while (1) {
        int sleepTime = rand() % 5;
        sleep(sleepTime);
        item = rand() % 100 + 1; // Generate a random number between 1 and 100
        if (insert_item(item)) {
            printf("Error placing into buffer\n");
        } else {
            printf("produced %d\n", item);
        }
    }
}
