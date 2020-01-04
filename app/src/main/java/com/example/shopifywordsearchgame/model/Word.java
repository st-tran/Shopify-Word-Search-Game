package com.example.shopifywordsearchgame.model;

import android.graphics.Point;

import androidx.annotation.NonNull;

import com.example.shopifywordsearchgame.common.Direction;

public class Word {
    private String word;
    private Point position;
    private Direction direction;

    public Word(String word, Point pos, Direction dir) {
        this.word = word;
        this.position = pos;
        this.direction = dir;
    }

    public Point getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    @NonNull
    @Override
    public String toString() {
        return this.word;
    }

}
