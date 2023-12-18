package mancala;

import java.io.Serializable;

public class KalahRules extends GameRules implements Serializable{

    private static final long serialVersionUID = 1L;

    private final static int PLAYER_ONE_PIT = 6;
    
    private MancalaDataStructure gameBoard;

    public KalahRules(){
        super();
        gameBoard = getDataStructure();
    }
    // private int currentPlayer = 1;
    /**
     * This method should only be passing the starting pit to the distribute stones
     * @param startPit
     * @param player
     * @return
     */
    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException{

        //gameBoard = getDataStructure();

        if((startPit < 1 || startPit > 6) && playerNum == 1){
            throw new InvalidMoveException();
        } else if((startPit > 12 || startPit < 7) && playerNum == 2){
            throw new InvalidMoveException();
        }

        int numOfStones = gameBoard.getStoreCount(playerNum);
        //System.out.println("Passing value: " + (startPit) + " from moveStones");
        distributeStones(startPit);

        numOfStones = gameBoard.getStoreCount(playerNum) - numOfStones;
        return numOfStones;
    }
    
    /**
     * Distributes the stones throughout the arrayList
     * @param startingPoint
     * @return
     */
    @Override
    public int distributeStones(final int startingPoint){

        int stonesToPlace;
        int totalPlaced;
        final int newStart = findStartingPoint(startingPoint);

        Countable currentPit = gameBoard.getPit(newStart);

        stonesToPlace = getAndRemoveStones(currentPit);
        totalPlaced = stonesToPlace;
        currentPit = gameBoard.getPit(newStart);

        while(stonesToPlace > 0){
            currentPit = gameBoard.next();
            currentPit.addStone();
            stonesToPlace--;
        }

        totalPlaced += checkForCapture(currentPit);

        if(gameBoard.getIteratorPos() == 6 || gameBoard.getIteratorPos() == 13){
            switchCurrentPlayer();
        }

        switchCurrentPlayer();

        return totalPlaced;
    }

    private int checkForCapture(final Countable currentPit){
        int totalPlaced = 0;
        if(currentPit.getStoneCount() == 1 && gameBoard.getIteratorPos() < 6 && getCurrentPlayer() == 1){
            totalPlaced += captureStones(gameBoard.getIteratorPos() + 1);
        } else if(currentPit.getStoneCount() == 1 && gameBoard.getIteratorPos() < 13 && gameBoard.getIteratorPos() > 6 && getCurrentPlayer() == 2){
            totalPlaced += captureStones(gameBoard.getIteratorPos() + 1);
        }
        return totalPlaced;
    }

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    @Override
    public int captureStones(final int stoppingPoint){

        int temp = stoppingPoint - 1;
        //System.out.println("Temp is: " + temp);
        final int oppositeSide = 12 - temp;
        int totalStones = 0;

        final int currentPlayer = getCurrentPlayer();

        // the player's side
        totalStones += getStones(gameBoard.getPit(temp));
        addStones(getStones(gameBoard.getPit(temp)),gameBoard.getPlayerStore(currentPlayer));
        removeStones(gameBoard.getPit(temp));

        // the opposing player's side
        totalStones += getStones(gameBoard.getPit(oppositeSide));
        addStones(getStones(gameBoard.getPit(oppositeSide)),gameBoard.getPlayerStore(currentPlayer));
        removeStones(gameBoard.getPit(oppositeSide));

        return totalStones;
    }

    private int findStartingPoint(final int startingPoint){
        int newStart = startingPoint;
        if(startingPoint > PLAYER_ONE_PIT){
            gameBoard.setIterator(startingPoint, 2, false);
        } else{
            //System.out.println("The start Pit is: " + startingPoint);
            gameBoard.setIterator(startingPoint, 1, false);
            newStart -= 1;
        }
        return newStart;
    }

    private int getAndRemoveStones(final Countable removeFrom){
        return removeFrom.removeStones();
    }

    private void removeStones(final Countable removeFrom){
        removeFrom.removeStones();
    }

    private void addStones(final int stoneCount, final Countable storeToAdd){
        storeToAdd.addStones(stoneCount);
    }

    private int getStones(final Countable getFrom){
        return getFrom.getStoneCount();
    }

    private void switchCurrentPlayer(){
        if (getCurrentPlayer() == 1){
            setPlayer(2);
        } else{
            setPlayer(1);
        }
    }
}
