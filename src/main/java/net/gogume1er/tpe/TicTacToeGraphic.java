package net.gogume1er.tpe;

/**
 * Copyright (c) 2015 - 2016 UHCFr. All rights reserved
 * This file is a part of UHCFr project.
 *
 * @author Gogume1er
 */
public interface TicTacToeGraphic {

    int play();

    void win(int winner);

    void fullMatrix();

    void updateMatrix();

    void updateRound();

    void updateCurrentPlayer();

}
