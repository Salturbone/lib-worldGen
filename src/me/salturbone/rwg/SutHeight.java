package me.salturbone.rwg;

import org.bukkit.World;

/**
 * SutHeightCalculator
 */
public abstract class SutHeight implements Comparable<SutHeight> {

    private int priority;
    private int start = -1, end = -1;

    public SutHeight(int priority) {
        this.priority = priority;
    }

    public abstract void generateXZ(World world, long seed, int x, int height, int z);

    public boolean isIn(int height) {
        boolean startBool = false;
        if (start == -1)
            startBool = true;
        else
            startBool = height >= start;
        boolean endBool = false;
        if (end == -1)
            endBool = true;
        else
            endBool = height <= end;
        return startBool && endBool;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(SutHeight o) {
        return o.priority - priority;
    }

}