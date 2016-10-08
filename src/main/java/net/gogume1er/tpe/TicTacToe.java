package net.gogume1er.tpe;

import java.util.Random;
import java.util.Scanner;

/**
 * Copyright (c) 2015 - 2016 UHCFr. All rights reserved
 * This file is a part of UHCFr project.
 *
 * @author Gogume1er
 */
public class TicTacToe implements Runnable {

    private final Thread primaryThread;
    // Value: 0 = undefined, 1 = player 1, 2 = player 2
    private final int[][] matrix;
    private final Scanner input;
    private final boolean useAI;
    private final TicTacToeAI ai;

    private int currentRound;
    private int currentPlayer;

    private TicTacToe() {
        this.primaryThread = new Thread(this, "Game");
        this.matrix = new int[3][3];
        this.input = new Scanner(System.in);
        this.currentPlayer = new Random().nextBoolean() ? 1 : 2;

        this.useAI = true;

        if(useAI)
            this.ai = new TicTacToeAI(this);
        else
            this.ai = null;

        this.printMatrix();
        this.primaryThread.start();
    }

    public void run() {
        while(!primaryThread.isInterrupted()) {
            System.out.println("\nAu joueur " + currentPlayer + ":");

            int choosenCase = 0;

            if(useAI && currentPlayer == 2) {
                choosenCase = ai.play();
                System.out.println("J'ai joué en " + (choosenCase + 1) + " !\n");
            } else {
                boolean valid = false;

                while(!valid) {
                    try {
                        choosenCase = Integer.parseInt(input.next()) - 1;

                        if(choosenCase > 8 || choosenCase < 0)
                            System.out.println("\n\u001B[31mCase inconnue !\u001B[0m");
                        else if(matrix[choosenCase / 3][choosenCase % 3] != 0)
                            System.out.println("\n\u001B[31mCette case est déjà utilisée !\u001B[0m");
                        else {
                            System.out.println("Votre coup a bien été enregistré !\n");
                            valid = true;
                        }
                    } catch(NumberFormatException exc) {
                        System.out.println("\n\u001B[31mNombre invalide\u001B[0m");
                    }
                }
            }

            matrix[choosenCase / 3][choosenCase % 3] = currentPlayer;

            if(useAI)
                ai.played(choosenCase);

            int potentialWinner = checkWin(this.matrix);

            if(potentialWinner != 0) {
                System.out.println("Victoire du joueur " + potentialWinner);
                printMatrix();
                break;
            }
            if(checkFull())
                break;

            System.out.println("Carte actuelle:");
            this.printMatrix();

            if(currentPlayer == 1)
                currentPlayer = 2;
            else
                currentPlayer = 1;

            currentRound++;
        }

    }

    private boolean checkFull() {
        if(this.currentRound < 8)
            return false;

        System.out.println("La carte est pleine, fin de la partie !");
        printMatrix();
        return true;
    }

    private void printMatrix() {
        for(int i = 0; i < matrix.length; i++) {
            int[] ints = matrix[i];
            if(i != 0)
                System.out.println("---------");

            for(int j = 0; j < ints.length; j++) {
                int value = ints[j];
                String point;

                if(value == 1)
                    point = "\u001B[31mX\u001B[0m";
                else if(value == 2)
                    point = "\u001B[34mO\u001B[0m";
                else
                    point = " ";

                System.out.print((j != 0 ? " | " : "") + point);
            }

            System.out.println();
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public static int checkWin(int[][] matrix) {
        for(int i = 0; i < 3; i++)
            if(matrix[i][0] != 0 && matrix[i][0] == matrix[i][1] && matrix[i][0] == matrix[i][2])
                return matrix[i][0];


        for(int i = 0; i < 3; i++)
            if(matrix[0][i] != 0 && matrix[0][i] == matrix[1][i] && matrix[0][i] == matrix[2][i])
                return matrix[0][i];


        if(matrix[0][0] != 0 && matrix[0][0] == matrix[1][1] && matrix[0][0] == matrix[2][2])
            return matrix[0][0];

        if(matrix[2][0] != 0 && matrix[2][0] == matrix[1][1] && matrix[2][0] == matrix[0][2])
            return matrix[2][0];

        return 0;
    }

    public static int[][] copyMatrix(int[][] original) {
        int[][] matrix = new int[3][3];

        for(int i = 0; i < original.length; i++) {
            int[] lines = original[i];
            System.arraycopy(lines, 0, matrix[i], 0, lines.length);
        }

        return matrix;
    }

    public static void main(String[] args) {
        new TicTacToe();
    }

}
