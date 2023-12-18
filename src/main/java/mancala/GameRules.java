package mancala;


import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private final MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
        gameBoard.initializeBoard();
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    public MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    public boolean isSideEmpty(final int pitNum) throws PitNotFoundException{
        // This method can be implemented in the abstract class.
        int temp;
        if(pitNum >= 0 && pitNum <= 5){
            temp = 1;
        } else if(pitNum >= 7 && pitNum <= 12){
            temp = 7;
        } else{
            throw new PitNotFoundException();
        }
        int total = 0;
        //System.out.println("Counting pits " + pitNum + " to " + (pitNum + 5));
        for(int i = temp; i < temp+6; i++){
            //System.out.println("Checking index: [" + getDataStructure().pitPos(i) + "] " + (pitNum+5));
            total += getNumStones(i);
        }
        //System.out.println("Is the side empty? " + !(total > 0));
        return total == 0;
    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(final int playerNum) {
        currentPlayer = playerNum;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    public abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    public abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        // this method can be implemented in the abstract class.
        /* make a new store in this method, set the owner
         then use the setStore(store,playerNum) method of the data structure*/
        final Store storeOne = new Store();
        final Store storeTwo = new Store();

        storeOne.setOwner(one);
        storeTwo.setOwner(two);

        one.setStore(storeOne);
        two.setStore(storeTwo);

        gameBoard.setStore(storeOne,1);
        gameBoard.setStore(storeTwo,2);
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    @Override
    public String toString() {
        String returnString = "";
        for(int i = 12; i > 6; i--){
            returnString += "\t";
            returnString += gameBoard.getPit(i);
        }
        returnString += "\n";
        returnString += gameBoard.getPit(13);
        returnString += "\t\t\t\t\t\t\t";
        returnString += gameBoard.getPit(6);
        returnString += "\n";
        for(int i = 0; i < 6; i++){
            returnString += "\t";
            returnString += gameBoard.getPit(i);
        }
        return returnString;
    }

    public int whichPlayersTurn(){
        return currentPlayer;
    }

    public void setPlayersTurn(final int playerTurn){
        currentPlayer = playerTurn;
    }

    public void tallySide(){
        final Countable p1Pit = gameBoard.getPit(6);
        final Countable p2Pit = gameBoard.getPit(13);
        for(int i = 0; i < 6; i++){
            p1Pit.addStones(getAndRemoveStones(gameBoard.getPit(i)));
        }
        for(int i = 7; i < 13; i++){
            p2Pit.addStones(getAndRemoveStones(gameBoard.getPit(i)));
        }
    }

    private int getAndRemoveStones(final Countable removeFrom){
        return removeFrom.removeStones();
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public Store getStore(final int playerNum){
        return gameBoard.getPlayerStore(playerNum);
    }

    // int getOppositePlayer(){
    //     if(currentPlayer == 1){
    //         return 2;
    //     }
    //     return 1;
    // }
}
