package com.example.shopifywordsearchgame.common;

import android.graphics.Point;

import java.util.Random;

/**
 * Represents a direction on a cartesian plane
 */
public enum Direction {
    NORTH(0, 1),
    EAST(1, 0),
    SOUTH(0, -1),
    WEST(-1, 0),
    NORTH_EAST(1, 1),
    SOUTH_EAST(1, -1),
    SOUTH_WEST(-1, -1),
    NORTH_WEST(-1, 1);

    private final int xOffset, yOffset;
    private static final Random random = new Random();
    private static final Direction[] values = values();
    public static final int size = values.length;

    Direction(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * @param pos1 initial position
     * @param pos2 ending position
     * @return closest Direction representing the direction of the segment between two points
     */
    public static Direction between(Point pos1, Point pos2) {
        if (pos1.x == pos2.x) {
            if (pos1.y < pos2.y) {
                return NORTH;
            } else {
                return SOUTH; // when pos1.equals(pos2), Direction.SOUTH is returned which is OK
            }
        } else if (pos1.y == pos2.y) {
            if (pos1.x < pos2.x) {
                return EAST;
            } else {
                return WEST; // when pos1.equals(pos2), Direction.SOUTH is still returned
            }
        } else {
            int xDiff = pos2.x - pos1.x;
            int yDiff = pos2.y - pos1.y;

            if (xDiff < 0) {
                if (yDiff < 0) {
                    return SOUTH_WEST;
                }
                return NORTH_WEST;
            } else {
                if (yDiff < 0) {
                    return SOUTH_EAST;
                }
                return NORTH_EAST;
            }
        }
    }

    public static Direction getRandomDirection() {
        return values[random.nextInt(size)];
    }

    public Direction getNextDirection() {
        return values[(ordinal() + 1) % size];
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }
}
