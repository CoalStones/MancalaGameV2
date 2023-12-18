package mancala;

import java.io.Serializable;

public class Player implements Serializable{

    private static final long serialVersionUID = 1L;

    private String playerName;
    private Store playerStore;

    private int playerNumber;

    private UserProfile userProfile;

    public Player(){
        setName("");
        playerNumber = 0;
    }

    public Player(final String name){
        playerName = name;
        playerNumber = 0;
    }

    public Player(final String name, final int playerNum){
        playerName = name;
        playerNumber = playerNum;
    }

    public Player(final String name, final Store storeValue){
        playerName = name;
        playerStore = storeValue;
    }

    public void setUserProfile(final UserProfile user){
        userProfile = user;
    }

    public UserProfile getUserProfile(){
        return userProfile;
    }

    public void setName(final String name){
        playerName = name;
    }

    public String getName(){
        return playerName;
    }

    public void setStore(final Store store){
        playerStore = store;
    }

    public int getStoreCount(){
        return playerStore.getStoneCount();
    }

    public Store getStore(){
        return playerStore;
    }

    public void addWin(int gameRules){
        userProfile.addWin(gameRules);
    }

    @Override
    public String toString(){
        return playerName + " " + getStoreCount() + " " + playerNumber;
    }

    public void setPlayerNumber(final int playerNum){
        playerNumber = playerNum;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

}