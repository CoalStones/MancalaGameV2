package mancala;

import java.io.Serializable;

public class Store implements Countable, Serializable{

    private static final long serialVersionUID = 1L;
    
    private Player owner;
    private int playerStore; // can i initialize here?
    private int total;


    public Store(){
        playerStore = 0;
    }

    public void setOwner(final Player player){
        owner = player;
    }

    public Player getOwner(){
        return owner;
    }

    @Override
    public void addStone(){
        playerStore++;
    }

    @Override
    public void addStones(final int amount){
        playerStore += amount;
    }

    @Override
    public int getStoneCount(){
        return playerStore;
    }

    @Override
    public int removeStones(){
        total = playerStore;
        playerStore = 0;
        return total;
    }

    @Override
    public String toString(){
        return String.valueOf(playerStore);
    }
}