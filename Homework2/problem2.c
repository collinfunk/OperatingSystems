#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {

    int sum = 0;
    char *remainder;

    if (argc == 4) {
        sum += strtol(argv[1], &remainder, 0);
        sum += strtol(argv[2], &remainder, 0);
        sum += strtol(argv[3], &remainder, 0);

        printf("The sum of these 3 numbers is %d\n", sum);
    }

    else if (argc == 3) {
        sum += strtol(argv[1], &remainder, 0);
        sum += strtol(argv[2], &remainder, 0);
        printf("The sum of these 2 numbers is %d\n", sum);
    }

    else {
        printf("Not enough arguments.\n");
    }
}