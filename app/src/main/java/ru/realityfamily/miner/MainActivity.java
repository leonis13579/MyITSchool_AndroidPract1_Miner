package ru.realityfamily.miner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    int width = 6;
    int height = 11;

    RelativeLayout[][] cells;

    GridLayout cellsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cellsLayout = findViewById(R.id.cellsContainer);

        generate();
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setBackgroundResource(R.color.Red);
        }
    };

    View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            v.setBackgroundResource(R.color.Blue);
            return true;
        }
    };

    private void generate() {
        cells = new RelativeLayout[height][width];

        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(width);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                cells[i][j] = (RelativeLayout) inflater.inflate(R.layout.cell, cellsLayout, false);
                cells[i][j].setTag(i + "," + j);

                cells[i][j].setOnClickListener(click);
                cells[i][j].setOnLongClickListener(longClick);

                cellsLayout.addView(cells[i][j]);
            }
        }
    }
}