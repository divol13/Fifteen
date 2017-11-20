package com.divol13.fifteen;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.divol13.fifteen.Tile.TILE_SIZE;

class Field extends StackPane {
    private Tile[][] grid;
    private static final int TRANSITION_SPEED = 75;
    private static final boolean DEBUG = true;
    private int width;
    private int height;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        // create a grid with tiles
        grid = new Tile[width][height];

        // create position for empty place in the grid
        Random random = new Random();
        int gap_x = DEBUG ? width - 2: random.nextInt(width);
        int gap_y = DEBUG ? height - 1: random.nextInt(height);

        // fill the array with numbers ("1"-"15")
        ArrayList<String> nums = new ArrayList();
        for (int i = 1; i < width * height; i++) {
            nums.add(Integer.toString(i));
        }

        // shuffle numbers if it is needed
        if(!DEBUG) {
            Collections.shuffle(nums);
        }

        int counter = 0;

        // through the game field
        int size = width * height;
        for (int i = 0; i < size; i++) {
            // hack to get 2-dimensial coordinates for 1-dimensial array
            int x = i % width;
            int y = i / height;

            // if it is gap time so place it
            if (x == gap_x && y == gap_y) {
                grid[x][y] = null;
                continue;
            }

            // get the tile number and create tile with it
            String number = nums.get(counter);
            Tile tile = new Tile(number);
            counter++;

            // set position on screen and in grid array
            tile.setPosition(x, y);
            grid[x][y] = tile;

            // add it to the screen
            getChildren().add(tile);
        }

        // check if we already win :) without any move
        check();
    }

    /**
     * Move tiles according tile we click on
     * @param tile - tile we click on
     */

    public void move(Tile tile) {
        int gap_x = -1;
        int gap_y = -1;

        // find the empty space in a grid
        for (int i = 0; i < this.width ; i++) {
            for (int j = 0; j < this.height; j++) {
                if(grid[i][j] == null) {
                    gap_x = i;
                    gap_y = j;
                }
            }

        }

        // if we can't find it then return
        if(gap_x == -1 || gap_y == -1) {
            System.out.println("gap not found");
            return;
        }

        // check tile we click on and empty space in grid have same in common
        if(gap_x != tile.grid_x && gap_y != tile.grid_y) {
            System.out.println("Sorry, no gaps here.");
            return;
        }

        // how many tiles do we shift and what is their direction
        int shift_x = gap_x - tile.grid_x;
        int shift_y = gap_y - tile.grid_y;

        System.out.println("shift_x=" + shift_x + " shift_y=" + shift_y);

        // create a new list of tiles we need to move on
        ArrayList<Tile> tiles = new ArrayList<>();

        // extract direction
        int dirX = shift_x == 0 ? 0 : shift_x / Math.abs(shift_x);
        int dirY = shift_y == 0 ? 0 : shift_y / Math.abs(shift_y);

        // extract how many tiles to move on
        int how_many = dirY == 0 ? Math.abs(shift_x) : Math.abs(shift_y);

        // iterate hom_many times
        for(int i = 0; i != how_many; i++) {
            // get coordinate
            int x = i * dirX + tile.grid_x;
            int y = i * dirY + tile.grid_y;

            // get tile with this coordinates
            Tile t = grid[x][y];

            // add this tile to the list, at first position
            tiles.add(0, t);

            // just show what tile we added to the list
            System.out.println("tile " + t + " added to the list");
        }

        // move collected tiles in given direction
        MoveXY(tiles, dirX, dirY);
    }

    /**
     * Method for moving list of tiles in any direction
     * @param tiles - list of tiles we need to move on the field
     * @param dirX - horizontal direction shift
     * @param dirY - vertical direction shift
     */
    private void MoveXY(ArrayList<Tile> tiles, int dirX, int dirY) {
        // get number of iterations
        int count = tiles.size();
        int counter = 0;

        // for each tile in arraylist
        for (Tile t:tiles) {

            // get old coordinate and calculate new one
            int old_x = t.grid_x;
            int new_x = t.grid_x + dirX;

            // get old coordinate and calculate new one
            int old_y = t.grid_y;
            int new_y = t.grid_y + dirY;

            // simple animation of tiles transition
            TranslateTransition anim = new TranslateTransition();
            anim.setNode(t);
            anim.setToX(new_x * TILE_SIZE);
            anim.setToY(new_y * TILE_SIZE);
            anim.setInterpolator(Interpolator.LINEAR);
            anim.setDuration(new Duration(TRANSITION_SPEED));
            anim.play();

            int finalCounter = ++counter;

            // when animation is done
            anim.setOnFinished(e -> {
                // set new position
                t.setPosition(new_x, new_y);
                // null the privous position
                grid[old_x][old_y] = null;
                // set new position in grid
                grid[new_x][new_y] = t;

                // check if puzzle solved
                // using dirty hack to call it once
                // without hack it calls as many times
                // as tiles moving
                if(count == finalCounter) {
                    check();
                }
            });
        }
    }

    public void check() {
        boolean win = false;
        int counter = 1;

        System.out.println("checking...");

        // spagetty code, sorry guys
        int size = width * height;
        for (int q = 0; q < size; q++) {
            int x = q % width;
            int y = q / height;
            Tile t = grid[x][y];

            if(t != null) {
                int value = Integer.valueOf(t.getText());

                if(value == counter) {
                    t.setColor(Color.ROYALBLUE);

                    if (counter == (size-1))
                    {
                        win = true;
                        System.out.println("CONGRATULATIONS! YOU ARE WIN");
                        break;
                    } else {
                        counter++;
                    }
                } else {
                    t.setColor(Color.AQUA);
                    // System.out.println("break at " + q);
                    break;
                }

            } else {
                // System.out.println("Chain is broken.");
                // mark the rest tiles to their usual colors
                for (int i = q; i < size; i++) {
                    int xt = i % width;
                    int yt = i / height;
                    Tile tl = grid[xt][yt];
                    if(tl != null) {
                        tl.setColor(Color.AQUA);
                    }
                }
                break;
            }
        }

        System.out.println("win : " + win);
    }

}
