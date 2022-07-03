#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>


int *fibSequence;
void *runner (void *param);

int main(int argc, char *argv[]) {
    int num_of_args = argc - 1;
    pthread_t *tid;
    
    if (argc != 2) {
        printf("Error: Enter in format ./Fibonacci (Integer representating how many Fibonacci numbers to print)\n");
        exit(1);
    }

    int num = atoi(argv[1]);
    if (num < 0) {
        printf("Input must be an integer greater than 0\n");
        exit(1);
    }

    fibSequence = malloc(num * sizeof(int));
    tid = (pthread_t *)malloc(num * sizeof(pthread_t));

    for (int i = 0; i < num; i++) {
        pthread_create(&tid[i], 0, runner, (void *)i);
    }

    for (int i = 0; i < num; i++) {
        pthread_join(tid[i], NULL);
    }

    for (int i = 0; i < num; i++) {
        printf("%d\n", fibSequence[i]);
    }
    return 0;
}

void *runner (void *param) {
    int i = (int)param;
    if (i == 0) {
        fibSequence[i] = 0;
    } else if (i == 1) {
        fibSequence[i] = 1;
    } else {
        fibSequence[i] = fibSequence[i-1] + fibSequence[i-2];
    }
    pthread_exit(0);
}