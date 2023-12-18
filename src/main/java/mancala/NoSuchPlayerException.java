package mancala;

public class NoSuchPlayerException extends Exception{

    private static final long serialVersionUID = 1L;

    public NoSuchPlayerException(){
        super("Invalid Player.");
    }

    public NoSuchPlayerException(final String output){
        super(output);
    }
}
