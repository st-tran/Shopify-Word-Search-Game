package com.example.shopifywordsearchgame.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.shopifywordsearchgame.R;
import com.example.shopifywordsearchgame.data.WordLoader;
import com.example.shopifywordsearchgame.data.IWordService;
import com.example.shopifywordsearchgame.presenter.IWordSearchPresenter;
import com.example.shopifywordsearchgame.presenter.WordSearchPresenter;

public class WordSearchActivity extends AppCompatActivity implements IWordSearchView {
    private final int ROWS = 10;
    private final int COLS = 15;

    private GridView gridView;
    private LinearLayout scrollViewContainer;
    private IWordSearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove notification bar
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.word_search_activity);

        gridView = findViewById(R.id.game_grid);
        scrollViewContainer = findViewById(R.id.word_list);

        if (savedInstanceState == null) {
            IWordService wordLoader = new WordLoader(getResources().openRawResource(R.raw.wordlist));
            presenter = new WordSearchPresenter(ROWS, COLS, this, wordLoader);
        } else {
            presenter = (IWordSearchPresenter) savedInstanceState.getSerializable("presenter");
            presenter.updateView(this);
            presenter.onViewChanged();

            final View root = findViewById(android.R.id.content);
            ViewTreeObserver vto = root.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    presenter.reHighlight();
                    presenter.reCrossout();
                    presenter.setSolved();
                    root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("presenter", presenter);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void setWords(String[] words) {
        for (String word : words) {
            TextView tv = new TextView(this);
            tv.setText(word);
            tv.setTag(word.toUpperCase());
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextSize(24f);
            tv.setPadding(16, 0, 16, 0);
            tv.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            tv.setLayoutParams(new ScrollView.LayoutParams(
                    ScrollView.LayoutParams.WRAP_CONTENT,
                    ScrollView.LayoutParams.MATCH_PARENT)
            );
            scrollViewContainer.addView(tv);
        }
    }

    @Override
    public void markWordSolved(String word) {
        ((TextView) scrollViewContainer.findViewWithTag(word.toUpperCase())
        ).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void setGrid(char[][] grid) {
        GridAdapter gridAdapter = new GridAdapter(WordSearchActivity.this, grid);
        gridView.setAdapter(gridAdapter);
        gridView.setNumColumns(COLS);
    }

    @Override
    public void highlightCharAtPos(int row, int col) {
        getTextViewAt(row, col).setTextColor(Color.RED);
    }

    @Override
    public void unhighlightCharAtPos(int row, int col) {
        getTextViewAt(row, col).setTextColor(getColor(R.color.colorPrimary));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setGridTouchListener() {
        gridView.setOnTouchListener(new GridView.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    presenter.onPress(x, y);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    presenter.onMove(x, y);
                    // Touch event out of bounds
                    if (!(0 <= x && x <= gridView.getWidth() && 0 <= y && y <= gridView.getHeight())) {
                        return false;
                    }
                } else {
                    presenter.onLiftUp();
                }
                return true;
            }
        });
    }

    @Override
    public void setSolved(String solvedText) {
        ((TextView) findViewById(R.id.words_found)).setText(solvedText);
    }

    @Override
    public void showGameFinishedPopup() {
        final AppCompatActivity game = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(game);
                builder.setCancelable(false);
                builder.setTitle("All words found!");

                LinearLayout rows = new LinearLayout(game);
                rows.setOrientation(LinearLayout.VERTICAL);
                builder.setView(rows);

                LinearLayout cols = new LinearLayout(game);
                cols.setOrientation(LinearLayout.HORIZONTAL);
                cols.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                        ));
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0.5f);

                final Button retryButton = new Button(game);
                retryButton.setLayoutParams(buttonParams);
                retryButton.setText(R.string.play_again);

                retryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = new Intent(game, game.getClass());
                        game.startActivity(intent);
                    }
                });
                cols.addView(retryButton);
                rows.addView(cols);

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    @Override
    public int getLetterHeight() {
        return gridView.getChildAt(0).getHeight();
    }

    @Override
    public int getLetterWidth() {
        return gridView.getChildAt(0).getWidth();
    }

    private TextView getTextViewAt(int row, int col) {
        return (TextView) (
                (ConstraintLayout) gridView.getChildAt(row * COLS + col)
        ).getChildAt(0);
    }
}
