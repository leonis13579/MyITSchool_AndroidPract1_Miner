package ru.realityfamily.miner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int width = 6;
    int height = 11;

    int bombs_count = 10;
    RelativeLayout[][] cells;
    boolean [][] bombs;
    boolean [][] checked;

    boolean game_on;

    GridLayout cellsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cellsLayout = findViewById(R.id.cellsContainer);

        createBombs();
        generate();
    }

    private void createBombs() {
        bombs = new boolean[height][width];
        checked = new boolean[height][width];
        int count = 0;

        while (count < bombs_count) {
            int i = (int) (Math.random() * height);
            int j = (int) (Math.random() * width);
            if (!bombs[i][j]) {
                bombs[i][j] = true;
                count++;
            }
        }
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getBomb(v)) {
                v.setBackgroundResource(R.color.Red);
                game_on = false;
                switch_off();
            } else {
                colorToGray(v);
            }
        }
    };

    private void colorToGray(View v) {
        Drawable background = v.getBackground();
        if (((ColorDrawable) background).getColor() == getColor(R.color.white)) {
            v.setBackgroundResource(R.color.Gray);
            if (scanBombs(v) != 0) {
                RelativeLayout rl = (RelativeLayout) v;
                TextView tv = (TextView) rl.getChildAt(0);
                tv.setVisibility(View.VISIBLE);
                tv.setText(Integer.toString(scanBombs(v)));
            } else {
                int x = getX(v);
                int y = getY(v);

                if (y > 0) {
                    if (!bombs[y - 1][x]) {
                        colorToGray(cells[y - 1][x]);
                    }
                }
                if (x > 0) {
                    if (!bombs[y][x - 1]) {
                        colorToGray(cells[y][x - 1]);
                    }
                }
                if (y < height - 1) {
                    if (!bombs[y + 1][x]) {
                        colorToGray(cells[y + 1][x]);
                    }
                }
                if (x < width - 1) {
                    if (!bombs[y][x + 1]) {
                        colorToGray(cells[y][x + 1]);
                    }
                }
                if (y < height - 1 && x > 0) {
                    if (!bombs[y + 1][x - 1]) {
                        colorToGray(cells[y + 1][x - 1]);
                    }
                }
                if (y > 0 && x < width - 1) {
                    if (!bombs[y - 1][x + 1]) {
                        colorToGray(cells[y - 1][x + 1]);
                    }
                }
                if (y > 0 && x > 0) {
                    if (!bombs[y - 1][x - 1]) {
                        colorToGray(cells[y - 1][x - 1]);
                    }
                }
                if (y < height - 1 && x < width - 1) {
                    if (!bombs[y + 1][x + 1]) {
                        colorToGray(cells[y + 1][x + 1]);
                    }
                }
            }
        }
    }

    private int scanBombs(View v) {
        int x = getX(v);
        int y = getY(v);
        int count = 0;

        if (y > 0) {
            if (bombs[y - 1][x]) {
                count++;
            }
        }
        if (x > 0) {
            if (bombs[y][x - 1]) {
                count++;
            }
        }
        if (y < height - 1) {
            if (bombs[y + 1][x]) {
                count++;
            }
        }
        if (x < width - 1) {
            if (bombs[y][x + 1]) {
                count++;
            }
        }
        if (y < height - 1 && x > 0) {
            if (bombs[y + 1][x - 1]) {
                count++;
            }
        }
        if (y > 0 && x < width - 1) {
            if (bombs[y - 1][x + 1]) {
                count++;
            }
        }
        if (y > 0 && x > 0) {
            if (bombs[y - 1][x - 1]) {
                count++;
            }
        }
        if (y < height - 1 && x < width - 1) {
            if (bombs[y + 1][x + 1]) {
                count++;
            }
        }

        return count;
    }


    private void switch_off() {
        for (int i = 0; i < cellsLayout.getChildCount(); i++) {
            RelativeLayout rl = (RelativeLayout) cellsLayout.getChildAt(i);
            if (bombs[getY(rl)][getX(rl)]) {
                rl.setBackgroundResource(R.color.Red);
            }
            rl.setOnLongClickListener(null);
            rl.setOnClickListener(null);
        }
    }

    View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Drawable background = v.getBackground();
            if (background instanceof ColorDrawable) {
                if (!checked[getY(v)][getX(v)] && ((ColorDrawable) (background)).getColor() == getColor(R.color.white) ) {
                    v.setBackgroundResource(R.color.Blue);
                    checked[getY(v)][getX(v)] = true;
                }
            }
            return true;
        }
    };

    int getX(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[1]);
    }

    int getY(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[0]);
    }

    boolean getBomb(View v) {
        return Boolean.parseBoolean(((String) v.getTag()).split(",")[2]);
    }

    private void generate() {
        cells = new RelativeLayout[height][width];

        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(width);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                cells[i][j] = (RelativeLayout) inflater.inflate(R.layout.cell, cellsLayout, false);
                cells[i][j].setTag(i + "," + j + "," + bombs[i][j]);

                cells[i][j].setOnClickListener(click);
                cells[i][j].setOnLongClickListener(longClick);

                cellsLayout.addView(cells[i][j]);
            }
        }
    }
}