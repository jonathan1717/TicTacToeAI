package net.gogume1er.tpe;

import java.util.Random;

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
    private final boolean useAI;
    private final TicTacToeAI ai;
    private final TicTacToeGraphic graphic;

    private int currentRound;
    private int currentPlayer;

    private TicTacToe() {
        this.primaryThread = new Thread(this, "Game");
        this.matrix = new int[3][3];
        this.currentPlayer = new Random().nextBoolean() ? 1 : 2;
        this.currentRound = 1;
        this.graphic = new TicTacToeConsole(this);

        this.useAI = this.graphic.useAI();

        System.out.println("\nIntelligence artificielle " + (!useAI ? "dés": "") + "activée !\n");

        if(useAI)
            this.ai = new TicTacToeAI(this, 2);
        else
            this.ai = null;

        this.graphic.updateMatrix();
        this.primaryThread.start();
    }

    public void run() {
        while(!this.primaryThread.isInterrupted()) {
            this.graphic.updateCurrentPlayer();

            int choosenCase;

            if(this.useAI && this.currentPlayer == 2)
                choosenCase = ai.play();
            else
                choosenCase = this.graphic.play();

            this.matrix[choosenCase / 3][choosenCase % 3] = this.currentPlayer;
            this.graphic.played(choosenCase);

            int potentialWinner = checkWin(this.matrix);

            if(potentialWinner != 0) {
                this.graphic.win(potentialWinner);
                break;
            }

            if(checkFull()) {
                this.graphic.fullMatrix();
                break;
            }

            if(this.useAI)
                this.ai.played(choosenCase);

            if(currentPlayer == 1)
                currentPlayer = 2;
            else
                currentPlayer = 1;

            currentRound++;
        }

    }

    private boolean checkFull() {
        return this.currentRound > 8;
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
