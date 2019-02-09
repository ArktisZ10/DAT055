package com.dat055.Model.Collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dat055.Model.Entity.Player;
import com.dat055.Model.Map.Tile.Tile;
import com.dat055.Model.Map.Tile.TileMap;


import java.util.ArrayList;


public class CollisionHandler {
    private final int tileSize = 64;
    private TileMap tileMap;
    private ArrayList<Rectangle> rectList;

    public CollisionHandler(TileMap map) {
        tileMap = map;
    }


    public boolean checkCollision(Player player) {
        Vector2 position = new Vector2(player.getPosition());
        Rectangle playerRect = new Rectangle((player.getRect()));
        int height = player.getHeight();
        int width = player.getWidth();
        rectList = new ArrayList();

        // If player is outside of the map, stop checking for collisions.
        if (position.x < 0 || position.y < 0 || position.x + width > tileMap.getWidthPixels() || position.y + height > tileMap.getHeightPixels())
            return true;

        if (!tileMap.getTile((int)(position.x/tileSize), (int)(position.y-2)/tileSize).getState()) {
            player.setGrounded(false);
        }

        // Loop through the tiles around the player.
        for (int row = (int) (position.y / tileSize); row < Math.ceil((position.y + height) / tileSize); row++) {
            for (int col = (int) (position.x / tileSize); col < Math.ceil((position.x + width) / tileSize); col++) {
                Tile tile = tileMap.getTile((int) Math.floor(col), (int) Math.floor(row));



                Rectangle tileRect = tile.getRect();
                Rectangle intersection = new Rectangle();
                rectList.add(tileRect);
                // Check for collision
                if (tile.getState()) {
                    if (Intersector.intersectRectangles(playerRect, tileRect, intersection)) {
                        // Check collisions above and below
                        if (checkYCollision(intersection)) {
                            player.setYVelocity(0);
                            if (tileRect.y < playerRect.y) {
                                player.setGrounded(true);
                                player.setYPosition((int)(intersection.y+intersection.height));
                            } else {
                                player.setYPosition((int)(tileRect.y-playerRect.height));
                            }
                        }

                        // Check collisions to the side
                        if (checkXCollision(intersection)) {
                            player.setXVelocity(0);
                            player.setXAcceleration(0);
                            // Left
                            if (playerRect.x < tileRect.x + tileRect.width/2) {
                                player.setXPosition(Math.round(tileRect.x - playerRect.width));
                            }
                            // Right
                            else if (playerRect.x < tileRect.x+tileRect.width) {
                                player.setXPosition((int)(tileRect.x + playerRect.width));

                            }

                        }
                        if (checkBothCollisions(intersection))
                            System.out.println("mweep");
                    }
                }
            }
        }
        return false;
    }
    private boolean checkYCollision(Rectangle intersection) {
        return (intersection.width > intersection.height);
    }
    private boolean checkXCollision(Rectangle intersection) {

        return (intersection.height > intersection.width);
    }
    private boolean checkBothCollisions(Rectangle intersection) {
        return (intersection.height == intersection.width);
    }
    public ArrayList<Rectangle> getCheckedTiles() {
        return rectList;
    }
}
