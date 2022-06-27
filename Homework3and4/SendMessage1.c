#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main() {
    char message[100];
    int pipe1[2];
    int pipe2[2];
    pid_t pid1;

    if (pipe(pipe1) == -1) {
        printf("Pipe 1 failed to open\n");
        exit(1);
    }

    if (pipe(pipe2) == -1) {
        printf("Pipe 2 failed to open\n");
        exit(1);
    }

    pid1 = fork();

    // if pid1 is less than 0 fork failed
    if (pid1 < 0) {
        printf("Fork failed\n");
        exit(1);
    }

    //parent process 
    if (pid1 > 0) {
        close(pipe1[0]);
        close(pipe2[1]);

        printf("Enter message: ");
        fgets(message, 100, stdin);
        //remove newline from message
        message[strcspn(message, "\n")] = 0;

        write(pipe1[1], message, sizeof(message));
        read(pipe2[0], message, sizeof(message));
        printf("Message in parent from child: %s\n", message);

        close(pipe1[1]);
        close(pipe2[0]);
    }

    //child process
    if (pid1 == 0) {
        close(pipe1[1]);
        close(pipe2[0]);

        read(pipe1[0], message, sizeof(message));
        int i = 0;
        while (message[i] != '\0') {
            if (islower(message[i])) {
                message[i] = toupper(message[i]);
            } else {
                message[i] = tolower(message[i]);
            }
            i++;
        }
        
        write(pipe2[1], message, sizeof(message));
        
        close(pipe1[0]);
        close(pipe2[1]);
    }
    return 0;
}