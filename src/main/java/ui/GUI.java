package ui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.Position;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

import mancala.GameNotOverException;
import mancala.InvalidMoveException;
import mancala.MancalaGame;
import mancala.PitNotFoundException;
import mancala.Player;
import mancala.Saver;
import mancala.UserProfile;

public class GUI extends JFrame{

    private final int KALAH_RULES = 1;
    private final int AYO_RULES = 2;

    private JPanel gameContainer;

    private PositionAwareButton[][] buttons;
    private MancalaGame mancalaRules;

    private JPanel playerPanel;
    private JPanel topPanel;
    private JButton customButton;

    private Player player1;
    private Player player2;

    private UserProfile userProfile1;
    private UserProfile userProfile2;

    private int gameRules = 1;

    private Saver saveLoad;

    private JMenuBar menuBar;


    /***************************************************************************************
    *    Title: swing tutorial
    *    Author: Judi McCuaig
    *    Date: 14/11/2023
    *    Code version: 1.0
    *    Availability: Moodle
    *
    ***************************************************************************************/
    public GUI(final String name){
        super();
        basicSetUp(name);
        setupGameContainer();
        mancalaRules = new MancalaGame(); // sets default game to kalah rules
        add(gameContainer, BorderLayout.CENTER);
        add(makeButtonPanel(), BorderLayout.EAST);

        player1 = new Player("Player One", 1);
        player2 = new Player("Player Two", 2);

        userProfile1 = new UserProfile(player1);
        userProfile2 = new UserProfile(player2);

        player1.setUserProfile(userProfile1);
        player2.setUserProfile(userProfile2);

        playerPanel = makePlayerInfo(new JPanel(), player1, player2);
        add(playerPanel, BorderLayout.WEST);
        mancalaRules.setPlayers(player1, player2);

        topPanel = startUpMessage(new JPanel());
        add(topPanel, BorderLayout.NORTH);

        makeMenu();
        setJMenuBar(menuBar);

        updateButtons();

        saveLoad = new Saver();

        pack();
    }

    /***************************************************************************************
    *    Title: swing tutorial
    *    Author: Judi McCuaig
    *    Date: 14/11/2023
    *    Code version: 1.0
    *    Availability: Moodle
    *
    ***************************************************************************************/
    private void basicSetUp(final String title){
        this.setTitle(title);
        gameContainer = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel makePlayerInfo(JPanel playerInfo, final Player player1, final Player player2){

        playerInfo.setLayout(new BoxLayout(playerInfo, BoxLayout.Y_AXIS));
        playerInfo.add(new JLabel("<html> <br>" + userProfile1.getGameInfo() + "<br><br></html>"));
        JButton button = new JButton("Load Player One");
        button.addActionListener(e -> choosePlayerInfo(player1, userProfile1));

        playerInfo.add(button);

        button = new JButton("Change Player Name");
        button.addActionListener(e -> changePlayerName(player1));

        playerInfo.add(button);

        button = new JButton("Save Player");
        button.addActionListener(e -> savePlayer(userProfile1));

        playerInfo.add(button);
        
        playerInfo.add(new JLabel("<html> <br> <br>" + userProfile2.getGameInfo() + "</html>"));
        button = new JButton("Load Player Two");
        button.addActionListener(e -> choosePlayerInfo(player2, userProfile2));

        playerInfo.add(button);

        button = new JButton("Change Player Name");
        button.addActionListener(e -> changePlayerName(player2));


        playerInfo.add(button);
        button = new JButton("Save Player");
        button.addActionListener(e -> savePlayer(userProfile2));

        playerInfo.add(button);

        this.player1 = player1;
        this.player2 = player2;

        return playerInfo;
    }

    private void changePlayerName(final Player playerToChange){
        final String playerName = JOptionPane.showInputDialog("What is the new Player's name?");
        playerToChange.setName(playerName);
        playerToChange.getUserProfile().setName(playerName);
        updateAll();
    }


    private String fileFinder(){
        final JFileChooser fileChooser = new JFileChooser();
        final int response = fileChooser.showOpenDialog(null);

        if(response == JFileChooser.APPROVE_OPTION){
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
                JOptionPane.showMessageDialog(this, "File Explorer Closed", "Load error", JOptionPane.ERROR_MESSAGE);
        }

        return "";
    }

    private void choosePlayerInfo(Player playerToLoadTo, UserProfile profileToLoadTo){
        
        int currentPlayerNum = playerToLoadTo.getPlayerNumber();
        UserProfile newProfile;
        try{

            newProfile = (UserProfile) saveLoad.loadObject(fileFinder());

            if(playerToLoadTo.getPlayerNumber() == 1){
                player1.setUserProfile(newProfile);
                userProfile1 = newProfile;
            } else if(playerToLoadTo.getPlayerNumber() == 2){
                player2.setUserProfile(newProfile);
                userProfile2 = newProfile;
            }
            
            playerToLoadTo.setName(profileToLoadTo.getName());
            
            updateAll();

            // playerToLoadTo.setPlayerNumber(currentPlayerNum);
        } catch (IOException err){
            JOptionPane.showMessageDialog(this, "Profile could not be loaded", "Load Profile error", JOptionPane.ERROR_MESSAGE);
        }
        
        

    }

    private JPanel makeButtonPanel(){
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(makeNewKalahGameButton());
        buttonPanel.add(makeNewAyoGameButton());
        return buttonPanel;
    }

    private JButton makeNewKalahGameButton(){
        final JButton button = new JButton("Make a new Kalah game");
        button.addActionListener(e -> {
            newKalahGame();
        });
        return button;
    }

    private JButton makeNewAyoGameButton(){
        final JButton button = new JButton("Make a new Ayo game");
        button.addActionListener(e -> {
            newAyoGame();
        });
        return button;
    }

    public void setupGameContainer(){
        gameContainer.add(makeButtonGrid(8, 3));
    }

    private JPanel startUpMessage(JPanel topPanel){
        topPanel.add(new JLabel("<html>Welcome to mancala games!<br>Please choose to play either Kalah or Ayo rules.<br>The default ruleset is Kalah.<br>You are currently playing on "
         + whichGameRuleset() + " <br>It is currently " + mancalaRules.getCurrentPlayerName() + "'s turn.</html>"));
        return topPanel;
    }

    private String whichGameRuleset(){
        String ruleSet;
        if(gameRules == 1){
            ruleSet = "Kalah Rules";
        } else {
            ruleSet = "Ayo Rules";
        }
        return ruleSet;
    }

    /***************************************************************************************
    *    Title: swing tutorial
    *    Author: Judi McCuaig
    *    Date: 14/11/2023
    *    Code version: 1.0
    *    Availability: Moodle
    *
    ***************************************************************************************/
    private JPanel makeButtonGrid(final int wide, final int tall){
        final JPanel grid = new JPanel();
        buttons = new PositionAwareButton[tall][wide];
        grid.setLayout(new GridLayout(tall, wide));
        //top row of buttons
        for(int x = 0; x < wide; x++){
            buttons[0][x] = new PositionAwareButton();
            if(x != 0 && x != 7){ // may need this if later
                buttons[0][x].setAcross(x + 1);
                buttons[0][x].setDown(1);
                buttons[0][x].addActionListener(e -> {
                    guiMove(e);
                    checkGameState(); 
                }); 
                //buttons.setText();
            } else {
                buttons[0][x].setEnabled(false);
            }
            buttons[0][x].setPreferredSize(new Dimension(50, 50));
            grid.add(buttons[0][x]);
        }

        for(int x = 0; x < wide; x++){
            buttons[1][x] = new PositionAwareButton();
            if(x == 0 || x == 7){
                buttons[1][x].setAcross(1);
                buttons[1][x].setDown(2);
            } else {
                buttons[1][x].setEnabled(false);
            }
            grid.add(buttons[1][x]);
        }

        for(int x = 0; x < wide; x++){
            buttons[2][x] = new PositionAwareButton();
            if(x != 0 && x != 7){ // may need this if later
                buttons[2][x].setAcross(x + 1);
                buttons[2][x].setDown(3);
                buttons[2][x].addActionListener(e -> {
                    guiMove(e);
                    checkGameState(); 
                });
            } else {
                buttons[2][x].setEnabled(false);
            }
            grid.add(buttons[2][x]);
        }
        
        return grid;
    }

    private void makeMenu(){
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Save/Load Game Menu");
        JMenuItem item = new JMenuItem("Save the Game");
        item.addActionListener(e -> saveTheGame());
        
        menu.add(item);

        item = new JMenuItem("Load the Game");
        item.addActionListener(e -> loadSavedGame());

        menu.add(item);

        menuBar.add(menu);
    }

    private void guiMove(ActionEvent e){

        int startingPit = 0;
        PositionAwareButton pressedButton = (PositionAwareButton) e.getSource();

        int j = 7;
        for(int i = 0; i < 7; i++){
            if(pressedButton == buttons[0][i]){
                startingPit = j;
            } else if(pressedButton == buttons[2][i]){
                startingPit = i + 6;
            }
            j--;
        }
        try{
            mancalaRules.move(startingPit);
            updateAll();
            setStore();
        } catch (InvalidMoveException err){
            JOptionPane.showMessageDialog(this, "Invalid Move.", "Move Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void setStore(){
        player1.setStore(mancalaRules.getStore(1));
        player2.setStore(mancalaRules.getStore(2));
    }

    /***************************************************************************************
    *    Title: swing tutorial
    *    Author: Judi McCuaig
    *    Date: 14/11/2023
    *    Code version: 1.0
    *    Availability: Moodle
    *
    ***************************************************************************************/
    private void checkGameState(){
        int selection = 0;
        JOptionPane gameOver = new JOptionPane();
        String congrats = "";
        String button = "Play Again?";
        if(mancalaRules.isGameOver()){
            try{
                if(mancalaRules.getWinner() == null){
                    congrats = "It was a tie!";
                    userProfile1.addGamePlayed(gameRules);
                    userProfile2.addGamePlayed(gameRules);
                } else{
                    congrats = "Congratulations on winning " + mancalaRules.getWinner().getName() + "! Do you want to play again?";
                    Player winner = mancalaRules.getWinner();
                    userProfile1.addGamePlayed(gameRules);
                    userProfile2.addGamePlayed(gameRules);
                    winner.addWin(gameRules);
                }
                
            } catch(GameNotOverException err){
                err.getMessage();
            }

            selection = gameOver.showConfirmDialog(null, congrats, button, JOptionPane.YES_NO_OPTION);
            if(selection == JOptionPane.NO_OPTION){
                System.exit(0);
            } else {
                mancalaRules.startNewGame();
                updateAll();
            }
        }
    }

    /**
     * saves the current state of the game
     * 
     */
    private void saveTheGame(){
        final String fileName = JOptionPane.showInputDialog("Please enter a file name to save to");
        try{
            saveLoad.saveObject(mancalaRules, fileName);
        } catch (IOException err){
            JOptionPane.showMessageDialog(this, "Error Saving game.", "Save Error", JOptionPane.ERROR_MESSAGE);
            //err.printStackTrace();
            err.getMessage();
        }
    }

    /**
     * Loads the last saved game
     */
    private void loadSavedGame(){
        try{
            mancalaRules = (MancalaGame) saveLoad.loadObject(fileFinder());
            updateAll();
        } catch (IOException err){
            JOptionPane.showMessageDialog(this, "Error loading game.", "Load Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void savePlayer(final UserProfile playerToSave){
        final String fileName = JOptionPane.showInputDialog("Please enter a file name to save to");
        try{
            saveLoad.saveObject(playerToSave, fileName);
        } catch (IOException err){
            JOptionPane.showMessageDialog(this, "Error Saving game.", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Makes a new Kalah game
     */
    private void newKalahGame(){
        mancalaRules = new MancalaGame(KALAH_RULES, player1, player2);
        gameRules = 1;
        updateAll();
    }

    /**
     * Makes a new Ayo game
     */
    private void newAyoGame(){
        mancalaRules = new MancalaGame(AYO_RULES, player1, player2);
        gameRules = 2;
        updateAll();
    }

    private void updateAll(){
        updateTop();
        updatePlayerPanel();
        updateButtons();
        pack();
    }

    private void updatePlayerPanel(){
        playerPanel.removeAll();

        makePlayerInfo(playerPanel, player1, player2);
        //mancalaRules.setPlayers(player1, player2);
        playerPanel.repaint();
        playerPanel.revalidate();
    }

    private void updateTop(){
        topPanel.removeAll();
        topPanel = startUpMessage(topPanel);
        topPanel.repaint();
        topPanel.revalidate();
    }

    private void updateButtons(){
        gameContainer.removeAll();
        setupGameContainer();
        //System.out.println("Printing to infinity");
        try{
            int j = 1;
            for(int i = 6; i >= 1; i--){
                buttons[0][i].setText(Integer.toString(mancalaRules.getNumStones(j)));
                if(mancalaRules.getCurrentPlayerNum() == 1){
                    buttons[2][i].setEnabled(false);
                    buttons[0][i].setEnabled(true);

                    buttons[1][0].setEnabled(true);
                    buttons[1][7].setEnabled(false);
                }
                j++;
            }
            buttons[1][0].setText(Integer.toString(player1.getStoreCount()));
            //System.out.println("Player one store count is: " + player1.getStoreCount());
            for(int i = 1; i < 7; i++){
                buttons[2][i].setText(Integer.toString(mancalaRules.getNumStones(i + 6)));
                if(mancalaRules.getCurrentPlayerNum() == 2){
                    buttons[2][i].setEnabled(true);
                    buttons[0][i].setEnabled(false);

                    buttons[1][0].setEnabled(false);
                    buttons[1][7].setEnabled(true);
                }
            }
            buttons[1][7].setText(Integer.toString(player2.getStoreCount()));
            //System.out.println("Player two store count is: " + player2.getStoreCount());
            gameContainer.repaint();
            gameContainer.revalidate();
        } catch (PitNotFoundException err){
            JOptionPane.showMessageDialog(this, "Invalid Pit.", "Pit Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(final String[] args){
        final GUI game = new GUI("MANCALA GAMES");
        game.setVisible(true);
    }
}
