package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.geom.Ellipse2D;
import mancala.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MancalaGUI extends JFrame {

    private JPanel mainPanel;
    private JLabel infoLabel;
    private PositionAwareButton[][] pitButtons;
    private JLabel[][] pitLabels;  // Representing the Mancala pits
    private JLabel leftLabel;
    private JLabel rightLabel;
    private MancalaGame mancalaGame;  // Added reference to MancalaGame
    private MancalaDataStructure gameBoard;
    private Player player1;
    private Player player2;
    private UserProfile userProfilePlayer1;
    private UserProfile userProfilePlayer2;
    private GameRules rules;
    private JFileChooser fileChooser;
    private boolean isGameRunning = false;

    public MancalaGUI() {

        super ("Mancala Game");
        setSize(800,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setVisible(true);

        mancalaGame = new MancalaGame();
        gameBoard = new MancalaDataStructure();
        //profiles only set once but players can be called again
        userProfilePlayer1 = new UserProfile("PlayerOne", 0, 0, 0, 0);
        userProfilePlayer2 = new UserProfile("PlayerTwo", 0, 0, 0, 0);

        showMainMenuDialog();
        //showGameSelectionDialog();
    }
    private void showGameSelectionDialog() {
        Object[] options = {"Kalah", "Ayo"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose a game",
                "Game Selection",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            // Kalah is chosen
            rules = new KalahRules(); // Assuming you have a KalahDataStruct class
        } else {
            // Ayo is chosen
            rules = new AyoRules(); // Assuming you have an AyoDataStruct class
            
        }
        mancalaGame.setBoard(rules);
        gameBoard.setUpPits();
        mancalaGame.startNewGame();
        playerSetup();
    }
    public void playerSetup() {
        player1 = new Player();
        player2 = new Player();
        rules.registerPlayers(player1, player2); 
        mancalaGame.setPlayers(player1, player2);
        mancalaGame.setCurrentPlayer(player1);
        player1.setUserProfile(userProfilePlayer1);
        player2.setUserProfile(userProfilePlayer2);
        

        initializeUI();
    }

    private void initializeUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel pitPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 200);
            }
        };
        // Create panels for the store and pits
        JPanel storePanelLeft = new JPanel();
        JPanel storePanelRight = new JPanel();
        
        pitPanel.setLayout(new GridLayout(2, 6));

        // Initialize pit buttons
        pitButtons = new PositionAwareButton[2][6];
        pitLabels = new JLabel[2][6];
        int pitPos = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                if (row == 0){
                    pitPos = ((row * 6 + (5 - col) + 6) % 12) + 1;
                } else {
                    pitPos = ((row * 6 + col + 6) % 12) + 1;
                }
                pitButtons[row][col] = new PositionAwareButton("Pit " + pitPos);
                pitButtons[row][col].setAcross(col);  // Set horizontal position
                pitButtons[row][col].setDown(row);    // Set vertical position
                pitButtons[row][col].addActionListener(this::pitButtonClicked);
                pitPanel.add(pitButtons[row][col]);

                pitLabels[row][col] = new JLabel();
                pitLabels[row][col].setFont(new Font("Arial", Font.PLAIN, 20));
                pitLabels[row][col].setForeground(Color.PINK);
                pitLabels[row][col].setOpaque(true);
                pitLabels[row][col].setBackground(Color.WHITE);
                pitLabels[row][col].setHorizontalAlignment(SwingConstants.CENTER);
                pitLabels[row][col].setVerticalAlignment(SwingConstants.CENTER);
                pitButtons[row][col].add(pitLabels[row][col]);
            }
        }

        JButton storeButtonLeft = new JButton();
        JButton storeButtonRight = new JButton();
        fileChooser = new JFileChooser();

        leftLabel = new JLabel("Player 2's store");
        leftLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        leftLabel.setForeground(Color.PINK);
        leftLabel.setOpaque(true);
        leftLabel.setBackground(Color.WHITE);
        leftLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftLabel.setVerticalAlignment(SwingConstants.CENTER);
        storeButtonLeft.add(leftLabel);

        rightLabel = new JLabel("Player 1's store");
        rightLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        rightLabel.setForeground(Color.PINK);
        rightLabel.setOpaque(true);
        rightLabel.setBackground(Color.WHITE);
        rightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightLabel.setVerticalAlignment(SwingConstants.CENTER);
        storeButtonRight.add(rightLabel);

        storeButtonLeft.setEnabled(false);
        storeButtonRight.setEnabled(false);
        storeButtonLeft.setHorizontalAlignment(SwingConstants.CENTER);
        storeButtonRight.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the components to the pit panel
        storePanelLeft.add(storeButtonLeft); //2
        storePanelRight.add(storeButtonRight); //1
        mainPanel.add(storePanelLeft, BorderLayout.WEST);
        mainPanel.add(storePanelRight, BorderLayout.EAST);
        mainPanel.add(pitPanel, BorderLayout.CENTER);


        infoLabel = new JLabel("Welcome to Mancala!");

        // Create menu bar and menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Click for file options");
        JMenuItem saveItem = new JMenuItem("Save Game");
        JMenuItem loadItem = new JMenuItem("Load Game");
        JMenuItem saveProfileItem = new JMenuItem("Save Profile");
        JMenuItem loadProfileItem = new JMenuItem("Load Profile");
        JMenuItem mainItem = new JMenuItem("Main menu");
        //JMenuItem quitItem = new JMenuItem("Quit Current Game");
        //JMenuItem newGameItem = new JMenuItem("Start New Game");
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(saveProfileItem);
        fileMenu.add(loadProfileItem);
        fileMenu.add(mainItem);

        menuBar.add(fileMenu);

        // Add action listeners for menu items
        
        saveItem.addActionListener(this::saveGame);
        loadItem.addActionListener(this::loadGame);
        saveProfileItem.addActionListener(this::saveProfile);
        loadProfileItem.addActionListener(this::loadProfile);
        mainItem.addActionListener((event) -> showMainMenuDialog());


        // Add the info label and menu bar to the main panel
        mainPanel.add(infoLabel, BorderLayout.NORTH);
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        setContentPane(mainPanel); // Set the main panel as the content pane

        // Set up the main frame
        // Initialize the UI based on the MancalaGame state
        updateUI();
        
    }

    private void updateUI() {
        int store1Count = 0;
        int store2Count = 0;
        int pitPos = 0;
        // Update pit labels based on the MancalaGame state
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                if (row == 0){
                    pitPos = ((row * 6 + (5 - col) + 6) % 12) + 1;
                } else {
                    pitPos = ((row * 6 + col + 6) % 12) + 1;
                }
                int stonesInPit = mancalaGame.getNumStones(pitPos);
                pitLabels[row][col].setText(String.valueOf(stonesInPit));
            }
        }
        try{
            store1Count = mancalaGame.getStoreCount(player1);
            store2Count = mancalaGame.getStoreCount(player2);
        }catch (NoSuchPlayerException e){
            
        }
        leftLabel.setText(String.valueOf(store2Count));
        rightLabel.setText(String.valueOf(store1Count));
        if (mancalaGame.isGameOver()){
            displayGameResult();
        }
    }

    private void updateInfoLabel(String message) {
        infoLabel.setText(message);
    }

    private void pitButtonClicked(ActionEvent e) {
        // Handle pit button click event
        JButton clickedButton = (JButton) e.getSource();
        int pitNumber = Integer.parseInt(clickedButton.getText().split(" ")[1]);
        try {
            int stonesLeft = mancalaGame.move(pitNumber);
            infoLabel.setText(mancalaGame.getCurrentPlayer().getName() + "'s turn");
            updateUI();
        } catch (InvalidMoveException ex) {
            infoLabel.setText("Invalid Move: " + ex.getMessage());
        }
    }

    private void displayGameResult() {
    try {
        Player winner = mancalaGame.getWinner();
        if (winner != null) {
            updateInfoLabel(winner.getName() + " wins!");

        } else {
            updateInfoLabel("It's a tie!");
        }

        JDialog gameOverDialog = new JDialog(this, "Game Over.", true);
        gameOverDialog.setSize(300, 200);
        gameOverDialog.setLayout(new FlowLayout());
    

        JButton mainButton = new JButton("Return to Main Menu");

        mainButton.addActionListener(actionEvent -> {
            showMainMenuDialog();
            gameOverDialog.dispose(); // Close the dialog after handling the exit action
        });

        gameOverDialog.add(mainButton);
        gameOverDialog.setVisible(true);

    } catch (GameNotOverException e) {
        updateInfoLabel("The game is not over yet.");
    }
}

    private void updateProfiles() {
        if (mancalaGame.isGameOver()) {
            try{
                UserProfile winningProfile = mancalaGame.getWinner() == player1 ? userProfilePlayer1 : userProfilePlayer2;

                if (rules instanceof KalahRules) {
                    int kalahCount = winningProfile.getKalahGamesWon();
                    winningProfile.setKalahGamesWon(kalahCount+1);
                    //losingProfile.incrementKalahGameCount();
                } else if (rules instanceof AyoRules) {
                    int ayoCount = winningProfile.getAyoGamesWon();
                    winningProfile.setAyoGamesWon(ayoCount+1);
                }
            }catch(GameNotOverException e){

            }
        }
        if (rules instanceof KalahRules) {
            int kalah1 = userProfilePlayer1.getKalahGamesPlayed();
            int kalah2 = userProfilePlayer2.getKalahGamesPlayed();
            userProfilePlayer1.setKalahGamesPlayed(kalah1+1);
            userProfilePlayer2.setKalahGamesPlayed(kalah2+1);
            //losingProfile.incrementKalahGameCount();
        } else if (rules instanceof AyoRules) {
            int ayo1 = userProfilePlayer1.getKalahGamesPlayed();
            int ayo2 = userProfilePlayer2.getKalahGamesPlayed();
            userProfilePlayer1.setKalahGamesPlayed(ayo1+1);
            userProfilePlayer2.setKalahGamesPlayed(ayo2+1);
        }

    }
    private void showMainMenuDialog() {
        
            String[] options = {"Exit", "Quit Current Game", "Start New Game"};
        
            int result = JOptionPane.showOptionDialog(
                    this,
                    "Select an option:",
                    "Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
        
            ActionEvent dummyEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "dummy");

            if (result == 0) {
                exitGame(dummyEvent);
            } else if (result == 1) {
                quitCurrentGame(dummyEvent);
            } else if (result == 2) {
                startNewGame(dummyEvent);
            }
    }

    private void showProfilePopup(UserProfile userProfile) {
        JFrame profileFrame = new JFrame("User Profile");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JTextArea profileTextArea = new JTextArea(userProfile.toString());
        profileTextArea.setEditable(false);

        profileTextArea.setText(userProfile.toString());
    
        JScrollPane scrollPane = new JScrollPane(profileTextArea);
        profileFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    
        profileFrame.setSize(400, 300);
        profileFrame.setLocationRelativeTo(this);
        profileFrame.setVisible(true);
    }

    private void saveProfile(ActionEvent e) {
        try {
            UserProfile currentUserProfile = mancalaGame.getCurrentPlayer().getUserProfile();
            String fileName = currentUserProfile.getName() + "_profile.ser";
            Saver.saveObject(currentUserProfile, fileName);
            updateInfoLabel("User profile saved successfully!");
        } catch (IOException ex) {
            updateInfoLabel("Error saving user profile: " + ex.getMessage());
        }
    }
    private void loadProfile(ActionEvent e) {
        try {
            JFileChooser fileChooser = new JFileChooser("assets");
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                UserProfile loadedProfile = (UserProfile) Saver.loadObject(selectedFile.getPath());
                showProfilePopup(loadedProfile);

                mancalaGame.getCurrentPlayer().setUserProfile(loadedProfile);
                updateInfoLabel("User profile loaded successfully!");
            } else {
                updateInfoLabel("Profile loading canceled by user.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            updateInfoLabel("Error loading user profile: " + ex.getMessage());
        }
    }

    private void loadGame(ActionEvent e) {
        try {
            JFileChooser fileChooser = new JFileChooser("assets");
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                MancalaGame loadedGame = (MancalaGame) Saver.loadObject(selectedFile.getPath());
                mancalaGame = loadedGame;
                updateUI();
                updateInfoLabel("Game loaded successfully!");
            } else {
                updateInfoLabel("Game loading canceled by user.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            updateInfoLabel("Error loading game: " + ex.getMessage());
        }
    }

     
    private void saveGame(ActionEvent e) {
        try {
            String fileName = mancalaGame.getCurrentPlayer().getName() + "_saved_game.ser";
            Saver.saveObject(mancalaGame, fileName);
            updateInfoLabel("Game saved successfully!");
        } catch (IOException ex) {
            updateInfoLabel("Error saving game: " + ex.getMessage());
        }
    }

    private void exitGame(ActionEvent e) {
        // Implement exit game functionality
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            int save = JOptionPane.showConfirmDialog(this, "Do you want to save first?", "Save Game", JOptionPane.YES_NO_OPTION);
            if (save == JOptionPane.YES_OPTION) {
                saveProfile(e);
            }
            updateProfiles();
            System.exit(0);
        }
    }

    private void quitCurrentGame(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to quit the game?", "Quit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            int save = JOptionPane.showConfirmDialog(this, "Do you want to save first?", "Save Game", JOptionPane.YES_NO_OPTION);
            if (save == JOptionPane.YES_OPTION) {
                updateProfiles();
                saveProfile(e);
            }
            showMainMenuDialog();
        }
    }

    private void startNewGame(ActionEvent e) {
        if (isGameRunning) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to start a new game?", "Start again", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                int save = JOptionPane.showConfirmDialog(this, "Do you want to save first?", "Save Game", JOptionPane.YES_NO_OPTION);
                if (save == JOptionPane.YES_OPTION) {
                    saveProfile(e);
                }
                updateProfiles();
                showGameSelectionDialog();
            }
        } else {
            showGameSelectionDialog();
            isGameRunning = true;
            JOptionPane.showMessageDialog(this, "Game initialized.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MancalaGUI());
    }
}
