package mancala;

import java.io.Serializable;

// random comments
public class AyoRules extends GameRules implements Serializable{

    private static final long serialVersionUID = 1L;

    private MancalaDataStructure gameBoard;

    private final static int PLAYER_ONE_PIT = 6;

    public AyoRules(){
        super();
        gameBoard = getDataStructure();
    }

    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException{
        
        gameBoard = getDataStructure();

        if((startPit < 0 || startPit > 6) && playerNum == 1){
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
            //System.out.println("Position: " + gameBoard.getIteratorPos() + " Stones Left: " + stonesToPlace + " Current pit count: " + currentPit.getStoneCount());
            if(stonesToPlace == 0 && currentPit.getStoneCount() > 1 && gameBoard.getIteratorPos() != 6 && gameBoard.getIteratorPos() != 13){
                stonesToPlace = getAndRemoveStones(currentPit);
            }

        }

        totalPlaced += checkForCapture(currentPit);

        switchCurrentPlayer();

        return totalPlaced;
    }

    private int checkForCapture(final Countable currentPit){
        int totalPlaced = 0;
        if(currentPit.getStoneCount() == 1 && gameBoard.getIteratorPos() < 6 && getCurrentPlayer() == 1){
            //System.out.println("Capturing");
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
        final int oppositeSide = 12 - temp;
        int totalStones = 0;

        final int currentPlayer = getCurrentPlayer();

        if((getStones(gameBoard.getPit(oppositeSide)) != 0)){
            // the player's side
            // totalStones += getStones(gameBoard.getPit(temp));
            // addStones(getStones(gameBoard.getPit(temp)),gameBoard.getPlayerStore(currentPlayer));
            // removeStones(gameBoard.getPit(temp));

            // the opposing player's side
            totalStones += getStones(gameBoard.getPit(oppositeSide));
            addStones(getStones(gameBoard.getPit(oppositeSide)),gameBoard.getPlayerStore(currentPlayer));
            removeStones(gameBoard.getPit(oppositeSide));
        }

        return totalStones;
    }

    private int findStartingPoint(final int startingPoint){
        int newStart = startingPoint;
        if(startingPoint > PLAYER_ONE_PIT){
            gameBoard.setIterator(startingPoint, 2, true);
        } else{
            //System.out.println("The start Pit is: " + startingPoint);
            gameBoard.setIterator(startingPoint, 1, true);
            newStart -= 1;
        }
        return newStart;
    }

    private void removeStones(final Countable removeFrom){
        removeFrom.removeStones();
    }
    
    private int getAndRemoveStones(final Countable removeFrom){
        return removeFrom.removeStones();
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
