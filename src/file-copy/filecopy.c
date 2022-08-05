#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

int main(int argc, char *argv[]) {
    //create a pipe
    int pipe1[2];
    pid_t pid;
    char parentBuffer[100];
    char childBuffer[100];

    //Error if arguements in wrong format
    if (argc != 3) {
        printf("Error: Arguements arent in form ./filecopy input.txt copy.txt\n");
        exit(1);
    }

    // if pipe1 fails to open
    if (pipe(pipe1) == -1) {
        printf("Pipe 1 failed to open\n");
        exit(1);
    }

    // pointers to source and destination file
    char *source = argv[1];
    char *destination = argv[2];
    
    // fork process
    pid = fork();

    // parent process
    if (pid > 0) {
        close(pipe1[0]);

        int sourceFile = open(source, O_RDONLY);
        ssize_t readSize = read(sourceFile, parentBuffer, sizeof(parentBuffer));
        write(pipe1[1], parentBuffer, readSize);

        close(pipe1[1]);
    }
    // child process
    if (pid == 0) {
        close(pipe1[1]);

        ssize_t readSize = read(pipe1[0], childBuffer, sizeof(childBuffer));
        close(pipe1[0]);
        int destinationFile = open(destination, O_WRONLY | O_CREAT, 0644);

        write(destinationFile, childBuffer, readSize);
        printf("File copied\n");

    }
    return 0;
}