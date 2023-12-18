package mancala;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MancalaGame implements Serializable{

    private static final long serialVersionUID  = 1L;

    private final List<Player> players = new ArrayList<>();

    private GameRules mancalaBoard;
    private /*final*/ MancalaDataStructure playBoard;

    private Player currentPlayer;

    /**
     * Constructor
     */
    public MancalaGame(){
        setBoard(new KalahRules()); // change this later too
    }

    public MancalaGame(final int whichGame, final Player playerOne, final Player playerTwo){
        gameSelection(whichGame);
        setPlayers(playerOne, playerTwo);
    }

    private void gameSelection(final int whichGame){
        if(whichGame == 1){
            setBoard(new KalahRules());
        } else {
            setBoard(new AyoRules());
        }
    }

    /**
     * I think this is only supposed to set names
     * SET THE BOARD BEFORE THE PLAYERS
     */
    public void setPlayers(final Player onePlayer, final Player twoPlayer){
        onePlayer.setPlayerNumber(1);
        players.add(onePlayer);

        twoPlayer.setPlayerNumber(2);
        players.add(twoPlayer);
        mancalaBoard.registerPlayers(onePlayer, twoPlayer);

        setCurrentPlayer(onePlayer);
    }

    /**
     * Returns the arraylist of players
     */
    public List<Player> getPlayers(){
        return players;
    }

    /**
     * returns the current player
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public int getCurrentPlayerNum(){
        return currentPlayer.getPlayerNumber();
    }

    /**
     * sets the current player
     */
    public void setCurrentPlayer(final Player player){
        currentPlayer = player;
    }

    private void changePlayer(){
        if(mancalaBoard.whichPlayersTurn() == 1){
            setCurrentPlayer(players.get(0));
        } else{
            setCurrentPlayer(players.get(1));
        }
    }

    /**
     * creates the board object for this class
     * SET THE BOARD BEFORE THE PLAYERS
     */
    public void setBoard(final GameRules theBoard){
        mancalaBoard = theBoard;
        playBoard = mancalaBoard.getDataStructure();
        //mancalaBoard.setUpStores();
        //mancalaBoard.setUpPits();
    }

    /**
     * returns the playing board
     */
    public GameRules getBoard(){
        return mancalaBoard;
    }

    /**
     * returns the number of stones in a certain pit
     */
    public int getNumStones(final int pitNum) throws PitNotFoundException{
        return mancalaBoard.getNumStones(pitNum);
    }

    /**
     * counts the number of stones the player has left
     * Throws illegal player exception if player 1 tried to play on player 2 or vise versa
     * TODO add the exception + visibility
     */
    public int move(final int startPit) throws InvalidMoveException{ // check on this PLEASE

        if(mancalaBoard.getNumStones(startPit) == 0){
            throw new InvalidMoveException();
        }
        int totalStones;
        // System.out.println(mancalaBoard.moveStones(startPit, currentPlayer.getPlayerNumber()) 
        //                     + " stones were placed in a store that turn");
        mancalaBoard.moveStones(startPit, currentPlayer.getPlayerNumber());
        totalStones = totalSide(getCurrentPlayer());
        changePlayer();
        return totalStones;
    }

    private int totalSide(final Player player){
        int totalStones = 0;

        // try{
        if(player.getPlayerNumber() == 1){
            for(int i = 1; i < 7; i++){
                totalStones += mancalaBoard.getNumStones(i);
            }
        } else{
            for(int i = 7; i < 13; i++){
                //System.out.println(playBoard.pitPos(i));
                totalStones += mancalaBoard.getNumStones(i);
            }
        }

        return totalStones;
    }

    /**
     * returns the store count of the player
     */
    public int getStoreCount(final Player player){
        return player.getStoreCount();
    }

    public Store getStore(final int playerNum){
        return mancalaBoard.getStore(playerNum);
    }

    /**
     * Finds and returns the winner
     */
    public Player getWinner() throws GameNotOverException{
        if(!isGameOver()){
            throw new GameNotOverException();
        }
        Player winner;
        //System.out.println("In the getWinner method");
        if(getPlayerStoreCount(players.get(0)) > getPlayerStoreCount(players.get(1))){
            //System.out.println("Player One Won");
            winner = players.get(0);
        } else if(getPlayerStoreCount(players.get(0)) < getPlayerStoreCount(players.get(1))){
            //System.out.println("Player Two Won");
            winner = players.get(1);
        } else{
            //System.out.println("It was a Draw!");
            winner = null; // this has to be NULL
        }
        return winner;
    }

    private int getPlayerStoreCount(final Player getCount){
        return getCount.getStoreCount();
    }

    /**
     * Checks to see if the game should be over
     */
    public boolean isGameOver(){
        
        boolean endTheGame = false;

        try{
            
            if(mancalaBoard.isSideEmpty(0) || mancalaBoard.isSideEmpty(7)){
                endTheGame = true;
            }

            if(endTheGame){
                mancalaBoard.tallySide();
            }

        } catch (PitNotFoundException err){
            err.getMessage();
        }

        return endTheGame;
        
    }

    public void startNewGame(){
        setCurrentPlayer(players.get(0));
        mancalaBoard.setPlayersTurn(1);
        playBoard.resetBoard();
        mancalaBoard.registerPlayers(players.get(0),players.get(1));
    }

    @Override
    public String toString(){

        return mancalaBoard.toString();
        
    }

    /**
     * Might have to delete this function later
     * All helper functions defined below this
     * @return
     */
    public String boardLayout(){
        String board = "";

        board += "\n\t";
        for(int i = 12; i >= 7; i--){
            board += i;
            board += "\t";
        }
        board += "\n";

        board += getNumStones(playBoard.getPit(13));
        board += "\t \t \t \t \t \t \t";
        board += getNumStones(playBoard.getPit(6));
        board += "\n";

        board += "\t";
        for(int i = 1; i <= 6; i++){
            board += i;
            board += "\t";
        }
        board += "\n\n\n";

        return board;
    }

    private int getNumStones(final Countable getStones){
        return getStones.getStoneCount();
    }

    // public void whoseTurn(){
    //     if(mancalaBoard.whichPlayersTurn() == 1){
    //         System.out.println("Which pit would you like to play from " + getPlayerName(players.get(0)) + "?");
    //     } else{
    //         System.out.println("Which pit would you like to play from " + getPlayerName(players.get(1)) + "?");
    //     }
    // }

    private String getPlayerName(final Player getNameFrom){
        return getNameFrom.getName();
    }

    public String getCurrentPlayerName(){
        return currentPlayer.getName();
    }

    public void changePlayerName(final int playerNum, final String newName){
        if(playerNum == getNum(players.get(0))){
            changeName(players.get(0), newName);
        } else {
            changeName(players.get(1), newName);
        }
    }

    private int getNum(final Player numToGet){
        return numToGet.getPlayerNumber();
    }

    private void changeName(final Player playerToChange, final String newName){
        playerToChange.setName(newName);
    }

}
