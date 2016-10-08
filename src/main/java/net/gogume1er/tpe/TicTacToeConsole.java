package net.gogume1er.tpe;

import java.util.Scanner;

/**
 * Copyright (c) 2015 - 2016 UHCFr. All rights reserved
 * This file is a part of UHCFr project.
 *
 * @author Gogume1er
 */
class TicTacToeConsole implements TicTacToeGraphic {

    private final TicTacToe game;
    private final Scanner input;

    TicTacToeConsole(TicTacToe game) {
        this.game = game;
        this.input = new Scanner(System.in);
    }

    @Override
    public boolean useAI() {
        System.out.println("Intelligence artificielle activée ? (O/N)");

        while(true) {
            String response = input.next();

            if(response.equalsIgnoreCase("O"))
                return true;
            else if(response.equalsIgnoreCase("N"))
                return false;
            else
                System.out.println("\n\u001B[31mRéponse invalide !\u001B[0m");
        }
    }

    @Override
    public int play() {
        while(true) {
            try {
                int choosenCase = Integer.parseInt(input.next()) - 1;

                if(choosenCase > 8 || choosenCase < 0)
                    System.out.println("\n\u001B[31mCase inconnue !\u001B[0m");
                else if(this.game.getMatrix()[choosenCase / 3][choosenCase % 3] != 0)
                    System.out.println("\n\u001B[31mCette case est déjà utilisée !\u001B[0m");
                else {
                    System.out.println("Votre coup a bien été enregistré !\n");
                    return choosenCase;
                }
            } catch(NumberFormatException exc) {
                System.out.println("\n\u001B[31mNombre invalide !\u001B[0m");
            }
        }
    }

    @Override
    public void played(int playedCase) {
        System.out.println("J'ai joué en " + (playedCase + 1) + " !\n");
        this.updateMatrix();
    }

    @Override
    public void win(int winner) {
        System.out.println("\nVictoire du joueur " + winner + " !\n");
        this.updateMatrix();
    }

    @Override
    public void fullMatrix() {
        System.out.println("\nLa carte est pleine, fin de la partie !\n");
        this.updateMatrix();
    }

    @Override
    public void updateMatrix() {
        for(int i = 0; i < this.game.getMatrix().length; i++) {
            int[] ints = this.game.getMatrix()[i];
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

    @Override
    public void updateCurrentPlayer() {
        System.out.println("\nAu joueur " + this.game.getCurrentPlayer() + ":");
    }

}
