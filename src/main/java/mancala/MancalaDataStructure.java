package mancala;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 * Represents a Mancala data structure for the Mancala game.
 * Do not change the signature of any of the methods provided.
 * You may add methods if you need them.
 * Do not add game logic to this class
 */
public class MancalaDataStructure implements Serializable{

    private static final long serialVersionUID = 1L;

    private final static int PLAYER_ONE = 6;
    private final static int PLAYER_TWO = 13;
    private int startStones;  //not final because we might want a different size board in the future

    private final List<Countable> data = new ArrayList<>(); // possibly change this
    private int iteratorPos = 0; 
    private int playerSkip = PLAYER_TWO;
    private int pitSkip = -1; // will never match the iteratorPos unless set specifically

    
    /**
     * Constructor to initialize the MancalaDataStructure.
     * 
     * @param startStones The number of stones to place in pits at the start of the game. Default values is 4.
     */
    public MancalaDataStructure(final int startStones){
        this.startStones = startStones;
        for (int i = 0; i < PLAYER_ONE; i++) {
            data.add(new Pit());
        }
        data.add(new Store());
        for (int i = 7; i < PLAYER_TWO; i++) {
            data.add(new Pit());
        }
        data.add(new Store());
    }


    /**
     * Constructor to initialize the MancalaDataStructure.
     */
    public MancalaDataStructure() {
        this(4);
    }

    /**
     * Adds stones to a pit.
     *
     * @param pitNum   The number of the pit.
     * @param numToAdd The number of stones to add.
     * @return The current number of stones in the pit.
     */
    public int addStones(final int pitNum, final int numToAdd) {
        addStonesToCountable(data.get(pitPos(pitNum)), numToAdd);
        return getStoneCount(data.get(pitPos(pitNum)));
    }

    private void addStonesToCountable(final Countable toAdd, final int numOfStones){
        toAdd.addStones(numOfStones);
    }

    private int getStoneCount(final Countable toGet){
        return toGet.getStoneCount();
    }

    /**
     * Removes stones from a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones removed.
     */
    public int removeStones(final int pitNum) {
        return getAndRemoveStones(data.get(pitPos(pitNum)));
    }

    private int getAndRemoveStones(final Countable toRemoveFrom){
        return toRemoveFrom.removeStones();
    }

    /**
     * Adds stones to a player's store.
     *
     * @param playerNum The player number (1 or 2).
     * @param numToAdd  The number of stones to add to the store.
     * @return The current number of stones in the store.
     */
    public int addToStore(final int playerNum, final int numToAdd) {
        addStonesToCountable(data.get(storePos(playerNum)), numToAdd);
        return getStoneCount(data.get(storePos(playerNum)));
    }

    /**
     * Gets the stone count in a player's store.
     *
     * @param playerNum The player number (1 or 2).
     * @return The stone count in the player's store.
     */
    public int getStoreCount(final int playerNum) {
        return getStoneCount(data.get(storePos(playerNum)));
    }

    /**
     * Gets the stone count in a given  pit.
     *
     * @param pitNum The number of the pit.
     * @return The stone count in the pit.
     */
    public int getNumStones(final int pitNum) {
        return getStoneCount(data.get(pitPos(pitNum)));
    }    

    /*helper method to convert 1 based pit numbers into array positions*/
    public int pitPos(final int pitNum) {
        /*Runtime execeptions don't need to be declared and are
        automatically passed up the chain until caught. This can
        replace the PitNotFoundException*/
        if(pitNum < 1 || pitNum > 12){
            throw new RuntimeException("Pit Number Out of Range");
        }
        int pos = pitNum;
        if (pos <= PLAYER_ONE) {
            pos--;
        }
        return pos;
    }

    /*helper method to convert player number to an array position*/
    private int storePos(final int playerNum) {
        if(playerNum < 1 || playerNum > 2){
            throw new RuntimeException("Invalid Player Position");
        }

        int pos = PLAYER_ONE;
        if (playerNum == 2) {
            pos = PLAYER_TWO;
        }
        return pos;
    }

    /**
     * Empties both players' stores.
     */
    public void emptyStores() {
        data.set(storePos(1), new Store());
        data.set(storePos(2), new Store());
    }

    /**
     * Sets up pits with a specified number of starting stones.
     *
     * @param startingStonesNum The number of starting stones for each pit.
     */
    public void setUpPits() {
        for (int i = 0; i < PLAYER_ONE; i++) {
            addStonesToCountable(data.get(i),startStones);
        }

        for (int i = 7; i < PLAYER_TWO; i++) {
            addStonesToCountable(data.get(i),startStones);
        }
    }

    /**
     * Adds a store that is already connected to a Player.
     *
     * @param store     The store to set.
     * @param playerNum The player number (1 or 2).
     */
    public void setStore(final Countable store, final int playerNum) {
        data.set(storePos(playerNum), store);
    }
    
    /*helper method for wrapping the iterator around to the beginning again*/
    private void loopIterator() {
        if (iteratorPos == PLAYER_TWO + 1) {
            iteratorPos = 0; // possibly change this
        }
    }

    private void skipPosition() {
        while (iteratorPos == playerSkip || iteratorPos == pitSkip) {
            iteratorPos++;
            loopIterator();
        }
    }

    private void setSkipPlayer(final int playerNum) {
        //sets the skip store to be the opposite player
        playerSkip = PLAYER_TWO;
        if (playerNum == 2) {
            playerSkip = PLAYER_ONE;
        }
    }

    private void setSkipPit(final int pitNum) {
        pitSkip = pitPos(pitNum);
    }

    /**
     * Sets the iterator position and positions to skip when iterating.
     *
     * @param startPos       The starting position for the iterator.
     * @param playerNum      The player number (1 or 2).
     * @param skipStartPit   Whether to skip the starting pit.
     */
    public void setIterator(final int startPos, final int playerNum, final boolean skipStartPit) {
        //System.out.println(startPos); // remove this later
        iteratorPos = pitPos(startPos);
        setSkipPlayer(playerNum);
        if (skipStartPit) {
            setSkipPit(startPos);
        }
    }

    /**
     * Moves the iterator to the next position.
     *
     * @return The countable object at the next position.
     */
    public Countable next() {
        iteratorPos++;
        loopIterator(); // in case we've run off the end
        skipPosition(); // skip store and start position if necessary
        return data.get(iteratorPos);
    }

    public int getIteratorPos(){
        return iteratorPos;
    }

    public Countable getPit(final int index){
        return data.get(index);
    }

    /**
     * Initializes the board
     * Only distributes stones
     * Stores start with 0 stones
     * Pits start with 4 stones
     */
    public void initializeBoard(){
        //System.out.println("This function was called");
        emptyStores();
        setUpPits();
    }

    /**
     * Returns the board to 4 stones per pit and 0 in the store
     */
    public void resetBoard(){
        initializeBoard();
    }

    public Store getPlayerStore(final int playerNumber){
        if(playerNumber == 1){
            return (Store) data.get(PLAYER_ONE);
        } else {
            return (Store) data.get(PLAYER_TWO);
        }
    }

    @Override
    public String toString(){
        String board = "";

        board += "\n\t";
        for(int i = PLAYER_TWO - 1; i >= PLAYER_ONE + 1; i--){
            board += getStoneCount(data.get(i));
            board += "\t";
        }
        board += "\n";

        board += getStoneCount(data.get(PLAYER_TWO));
        board += "\t \t \t \t \t \t \t";
        board += getStoneCount(data.get(PLAYER_ONE));
        board += "\n";

        board += "\t";
        for(int i = 0; i < PLAYER_ONE; i++){
            board += getStoneCount(data.get(i));
            board += "\t";
        }
        board += "\n\n\n";

        return board;
    }
}
