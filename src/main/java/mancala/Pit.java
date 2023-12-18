package mancala;

import java.io.Serializable;

public class Pit implements Countable, Serializable{

    private static final long serialVersionUID = 1L;

    private int stones;

    public Pit(){
        stones = 0;
    } // I don't think we need to start a pit with stones

    /**
     * Returns the number of stones in the pit
     */
    @Override
    public int getStoneCount(){
        return stones;
    }

    /**
     * Adds a stone to the pit
     */
    @Override
    public void addStone(){
        stones++;
    }

    @Override
    public void addStones(final int numToAdd){
        stones += numToAdd;
    }

    /**
     * Removes a stone from the pit
     */
    @Override
    public int removeStones(){
        int total = stones;
        stones = 0;
        return total;
    }

    @Override
    public String toString(){
        return String.valueOf(stones);
    }

}