package mancala;

public class InvalidMoveException extends Exception{

    private static final long serialVersionUID = 1L;

    public InvalidMoveException(){
        super("Invalid Move.");
    }

    public InvalidMoveException(final String output){
        super(output);
    }
}
