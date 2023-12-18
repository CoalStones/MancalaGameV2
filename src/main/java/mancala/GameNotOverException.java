package mancala;

public class GameNotOverException extends InvalidMoveException{

    private static final long serialVersionUID = 1L;

    public GameNotOverException(){
        super("The game is not over yet.");
    }

    public GameNotOverException(final String output){
        super(output);
    }
}
