package com.example.shopifywordsearchgame.presenter;

public interface IWordSearchPresenter {
    void onPress(int x, int y);
    void onMove(int x, int y);
    void onLiftUp();
}
