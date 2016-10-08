package net.gogume1er.tpe;

import java.util.Random;

/**
 * Copyright (c) 2015 - 2016 UHCFr. All rights reserved
 * This file is a part of UHCFr project.
 *
 * @author Gogume1er
 */
public class TicTacToeAI {

    private final Random random;
    private Movement[][] possibilities;

    public TicTacToeAI(TicTacToe game) {
        this.random = new Random();
        this.possibilities = new Movement(game.getMatrix(), game.getCurrentRound(),
                game.getCurrentPlayer(), 0, 0).possibilities;
    }

    public int play() {
        Movement bestPossibility = null;
        int bestPossibilityWeight = 0;

        for(Movement[] lines : possibilities) {
            for(Movement possibility : lines) {
                if(possibility == null)
                    continue;

                int possibilityWeight = possibility.getWeight();

                if(bestPossibility == null) {
                    bestPossibility = possibility;
                    bestPossibilityWeight = possibilityWeight;
                    continue;
                }

                if(possibilityWeight > bestPossibilityWeight || (possibilityWeight == bestPossibilityWeight && random.nextBoolean())) {
                    bestPossibility = possibility;
                    bestPossibilityWeight = possibilityWeight;
                }
            }
        }

        if(bestPossibility == null)
            throw new IllegalStateException("Cannot play");

        return bestPossibility.playedCase;
    }

    public void played(int position) {
        this.possibilities = this.possibilities[position / 3][position % 3].possibilities;
    }

    private class Movement {
        private final int[][] matrix;
        private final int round;
        private final int currentPlayer;
        private final int playedCase;
        private final int winner;
        private final Movement[][] possibilities;

        private Movement(int[][] matrix, int round, int currentPlayer, int playedCase, int winner) {
            this.matrix = matrix;
            this.round = round;
            this.currentPlayer = currentPlayer;
            this.playedCase = playedCase;
            this.winner = winner;
            this.possibilities = this.registerPossibilities();
        }

        private Movement[][] registerPossibilities() {
            if(this.winner != 0 || this.round >= 9)
                return null;

            Movement[][] possibilities = new Movement[3][3];

            for(int i = 0; i < this.matrix.length; i++) {
                int[] lines = this.matrix[i];
                for(int j = 0; j < lines.length; j++) {
                    int currentCase = lines[j];

                    if(currentCase != 0)
                        continue;

                    int[][] tested = TicTacToe.copyMatrix(this.matrix);

                    tested[i][j] = this.currentPlayer;

                    int winner = TicTacToe.checkWin(tested);

                    possibilities[i][j] = new Movement(tested, this.round + 1, this.currentPlayer == 1 ? 2 : 1, i * 3 + j, winner);
                }
            }

            return possibilities;
        }

        private int getWeight() {
            int weight = 0;

            if(this.possibilities == null)
                return this.winner == 1 ? -10 : 10;


            for(Movement[] lines : this.possibilities) {
                for(Movement possibility : lines) {
                    if(possibility == null)
                        continue;

                    if(possibility.currentPlayer == 1 && possibility.getWeight() > weight)
                        weight = possibility.getWeight() - 1;
                    else if(possibility.currentPlayer == 2 && possibility.getWeight() < weight)
                        weight = possibility.getWeight() + 1;
                }
            }

            return weight;
        }

    }

}