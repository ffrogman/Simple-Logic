/*
SimpleLogic: A Bukkit plugin that adds sign based logic gates to Minecraft
Copyright (C) 2012  ffrogman

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ffrogman.simplelogic;

import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class LogicSign {

    public Block inputA;
    public Block inputB;
    public Block output;
    public World world;
    public int[] coords;
    public String operator;

    LogicSign() {
        coords = new int[3];
    }

    LogicSign(World w, int x, int y, int z, String s, String o) {
        coords = new int[3];
        world = w;
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
        operator = o;
        findBlocks(s);
    }

    public void setOperator() {
        Block block = getBlock();
        Sign sign;
        if (block.getType() == Material.WALL_SIGN) {
            BlockState state = block.getState();
            if (state instanceof Sign) {
                sign = (Sign) state;
                operator = sign.getLine(2);
            }
        }
    }

    public final void findBlocks(String outputString) {
        Block block = getBlock();
        Sign sign;
        if (block.getType() == Material.WALL_SIGN) {
            BlockState state = block.getState();
            if (state instanceof Sign) {
                sign = (Sign) state;
            } else {
                return;
            }
            Block left, right, back;
            switch (block.getData()) {
                //nsew
                case 4:
                    left = world.getBlockAt(sign.getX() + 1, sign.getY() - 1, sign.getZ() - 1);
                    right = world.getBlockAt(sign.getX() + 1, sign.getY() - 1, sign.getZ() + 1);
                    back = world.getBlockAt(sign.getX() + 2, sign.getY() - 1, sign.getZ());
                    break;
                case 5:
                    left = world.getBlockAt(sign.getX() - 1, sign.getY() - 1, sign.getZ() + 1);
                    right = world.getBlockAt(sign.getX() - 1, sign.getY() - 1, sign.getZ() - 1);
                    back = world.getBlockAt(sign.getX() - 2, sign.getY() - 1, sign.getZ());
                    break;
                case 2:
                    left = world.getBlockAt(sign.getX() + 1, sign.getY() - 1, sign.getZ() + 1);
                    right = world.getBlockAt(sign.getX() - 1, sign.getY() - 1, sign.getZ() + 1);
                    back = world.getBlockAt(sign.getX(), sign.getY() - 1, sign.getZ() + 2);
                    break;
                case 3:
                    left = world.getBlockAt(sign.getX() - 1, sign.getY() - 1, sign.getZ() - 1);
                    right = world.getBlockAt(sign.getX() + 1, sign.getY() - 1, sign.getZ() - 1);
                    back = world.getBlockAt(sign.getX(), sign.getY() - 1, sign.getZ() - 2);
                    break;
                default:
                    return;
            }
            if (outputString == null) {
                outputString = sign.getLine(1);
            }
            if (outputString.equals("back")) {
                inputA = left;
                inputB = right;
                output = back.getRelative(0, 1, 0);
                return;
            }
            if (outputString.equals("left")) {
                inputA = back;
                inputB = right;
                output = left.getRelative(0, 1, 0);
                return;
            }
            if (outputString.equals("right")) {
                inputA = left;
                inputB = back;
                output = right.getRelative(0, 1, 0);
                return;
            }
        }
    }

    public boolean canTorch() {
        if (output == null) {
            return false;
        }
        return ((output.getType() == Material.AIR || output.getTypeId() == 75 || output.getTypeId() == 76) && (output.getRelative(0, -1, 0).getType() != Material.AIR));
    }

    public boolean isSign() {
        Block block = getBlock();
        if (block.getType() == Material.WALL_SIGN) {
            BlockState state = block.getState();
            if (state instanceof Sign) {
                return true;
            }
        }
        return false;
    }

    public Block getBlock() {
        return world.getBlockAt(coords[0], coords[1], coords[2]);
    }

    @Override
    public String toString() {
        return world.getName() + ":" + coords[0] + "," + coords[1] + "," + coords[2];
    }
}
