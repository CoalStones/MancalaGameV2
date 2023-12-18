package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable{
    private static final long serialVersionUID = 1L;

    private String usersName;
    private Player player;

    //game stats
    private int totalKalahGames = 0;
    private int totalKalahWins = 0;
    private int totalAyoGames = 0;
    private int totalAyoWins = 0;

    public UserProfile(Player player){
        usersName = player.getName();
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public void changePlayer(final Player newPlayer){
        player = newPlayer;
    }

    public Store getStore(){
        return player.getStore();
    }

    public int getStoreCount(){
        return player.getStoreCount();
    }

    public void setName(final String name){
        usersName = name;
    }

    public String getName(){
        return usersName;
    }

    public int getKalahGames(){
        return totalKalahGames;
    }

    public int getAyoGames(){
        return totalAyoGames;
    }

    public int getKalahWins(){
        return totalKalahWins;
    }

    public int getAyoWins(){
        return totalAyoWins;
    }

    // test comment
    public void addGamePlayed(final int whichGame){
        if(whichGame == 1){
            totalKalahGames++;
        } else {
            totalAyoGames++;
        }
    }

    public void addWin(final int whichGame){
        if(whichGame == 1){
            totalKalahWins++;
        } else {
            totalAyoWins++;
        }
    }

    public String getGameInfo(){
        return ("<html>" + usersName + "<br>Kalah Games Played: " + totalKalahGames + "<br>Kalah Games Won: " + totalKalahWins + "<br>Ayo Games Played: " + totalAyoGames + "<br>Ayo Games Won: " + totalAyoWins + "</html>");
    }

}
