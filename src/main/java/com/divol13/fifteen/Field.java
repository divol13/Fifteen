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

    public Field() {
        grid = new Tile[App.WIDTH][App.HEIGHT];

        Random random = new Random();
        int gap_x = random.nextInt(App.WIDTH);
        int gap_y = random.nextInt(App.HEIGHT);

        ArrayList<String> nums = new ArrayList();
        for (int i = 1; i < App.WIDTH * App.HEIGHT; i++) {
            nums.add(Integer.toString(i));
        }

        Collections.shuffle(nums);
        // System.out.println(nums);

        int counter=0;
        for (int x = 0; x < App.WIDTH; x++) {
            for (int y = 0; y < App.HEIGHT ; y++) {

                if ( x == gap_x && y == gap_y) {
                    grid[x][y] = null;
                    System.out.println("gap at position [" + x + "," + y + "]");
                    continue;
                }

                String number = nums.get(counter);
                Tile tile = new Tile(number);
                counter++;

                tile.setPosition(x, y);

                getChildren().add(tile);

                grid[x][y] = tile;
            }
        }

    }

    /**
     * Move tiles according tile we click on
     * @param tile - tile we click on
     */

    public void move(Tile tile) {
        int gap_x = -1;
        int gap_y = -1;

        // find the empty space in a grid
        for (int i = 0; i < App.WIDTH ; i++) {
            for (int j = 0; j < App.HEIGHT; j++) {
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

            // when animation is done
            anim.setOnFinished(e -> {
                // set new position
                t.setPosition(new_x, new_y);
                // null the privous position
                grid[old_x][old_y] = null;
                // set new position in grid
                grid[new_x][new_y] = t;
            });
        }
    }

    public void check() {
        boolean win = false;

        int counter = 1;
        for (int q = 0; q < App.WIDTH * App.HEIGHT; q++) {
            int x = q % App.WIDTH;
            int y = q / App.HEIGHT;
            Tile t = grid[x][y];

            if(t != null) {
                int value = Integer.valueOf(t.getText());
                System.out.println("value = " + value + " , counter = " + counter);
                if(value == counter) {
                    t.setColor(Color.ROYALBLUE);
                    System.out.println("perfect " + value + "=" + counter);
                    if (counter == 15)
                    {
                        win = true;
                        System.out.println("herere!");
                        break;
                    } else {
                        counter++;
                    }
                } else {
                    t.setColor(Color.AQUA);
                    System.out.println("break at " + q);
                    break;
                }


            }
//            else {
//                System.out.println("Chain is broken.");
//                break;
//            }
        }

        System.out.println("win : " + win);
    }

    public void swap(Tile tile) {
        if(tile != null){

        }
    }
}
