package bankersalgo;

import java.util.Scanner;

public class Banker {
    static int NUMBER_OF_CUSTOMERS = 5;
    static int NUMBER_OF_RESOURCES = 3;
    int avaliable[] = new int[NUMBER_OF_RESOURCES];
    int maximum[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    int allocation[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    int need[][] = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];

}