package mancala;
/*
    BOARD LAYOUT

        12  11  10  09  08  07
    S2                          S1
        01  02  03  04  05  06    
 */

 //Mancala game -> has gameRules -> has mancalaDataStruct
 //ALL METHODS MADE
//import java.util.ArrayList;
import java.io.Serializable;

public class MancalaGame implements Serializable{

    private static final long serialVersionUID = 1L;

    private GameRules gameRules; // Use the GameRules interface
    private Player currentPlayer;
    private Player[] players;
    private final int lowPit = 1;
    private final int hiPit = 12;
    private final int pNum1 = 1;
    private final int pNum2 = 2;
    private final int defaultVal = 0;


    public MancalaGame() {
        //gameRules = new GameRules();
        currentPlayer = null;
        players = new Player[2];
    }

    public GameRules getBoard() {
        return gameRules;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getNumStones(final int pitNum){
        int num = 0;
        try {
            num = gameRules.getNumStones(pitNum);
        } catch (RuntimeException e) {

        }
        return num;
    }
    public int getStoreCount(final Player player) throws NoSuchPlayerException{
        Store playerStore = player.getStore();
        if (playerStore == null) {
            throw new NoSuchPlayerException();
        }
        return playerStore.getStoneCount();
       
    }

    public Player getWinner() throws GameNotOverException{
        Player player;
        if (!isGameOver()) {
            throw new GameNotOverException();
        }else{
            final int storeCountPlayer1 = players[0].getStore().getStoneCount();
            final int storeCountPlayer2 = players[1].getStore().getStoneCount();
    
            if (storeCountPlayer1 > storeCountPlayer2) {
                player = players[0];
            } else if (storeCountPlayer1 < storeCountPlayer2) {
                player = players[1];
            } else {
                return null; // Tie
            }
            return player;
        }
    }

    public boolean isGameOver() {
        boolean result = false;
        try {
            final boolean p1Empty = gameRules.isSideEmpty(3);
            final boolean p2Empty = gameRules.isSideEmpty(9);
            result = p1Empty || p2Empty;
        } catch (PitNotFoundException e) {

        }
        return result;
    }

//Move & support methods
    public int move(final int startPit) throws InvalidMoveException {
        try{
            validateMove(startPit);
        } catch (InvalidMoveException e){
            //System.out.println("Invalid Move");
        }
    
        final int stonesInPit = gameRules.getNumStones(startPit);
        final int stopPit = (startPit + stonesInPit) % 12;
    
        // Perform the move
        int playerNum = getPlayerNum(currentPlayer);
        gameRules.moveStones(startPit, playerNum);

        // Check for stone capture
        if (gameRules.getIfCapture()) {
            try{
                capture(stopPit);
            }catch (PitNotFoundException e){
                //System.out.println("Pit not found");
            }
        }

        // Check for a free turn
        if (!gameRules.getFreeTurn()) {
            switchTurns();
        }
    
        return gameRules.getNumStones(startPit); //stones left
    }

    private int getPlayerNum(Player player){
        int playerNum = defaultVal;
        if (player == players[0]){
            playerNum = pNum1;
        } else if (player == players[1]){
            playerNum = pNum2;
        }
        return playerNum;
    }
    
    private void validateMove(final int startPit) throws InvalidMoveException {
        if (currentPlayer == null) {
            throw new InvalidMoveException("No current player set.");
        } else if (startPit < lowPit || startPit > hiPit) {
            throw new InvalidMoveException("Invalid starting pit: " + startPit);
        }
    }
    
    private void capture(final int stopPit) throws PitNotFoundException{
        try {
            final int capturedStones = gameRules.captureStones(stopPit);
            currentPlayer.getStore().addStones(capturedStones);
        } catch (PitNotFoundException e) {
            //System.out.println("Pit not found.");
        }
    }

    public void switchTurns() {
        final Player current = getCurrentPlayer();
        if (current == players[0]) {
            setCurrentPlayer(players[1]);
        } else {
            setCurrentPlayer(players[0]);
        }
    }

//End of move

    public void setBoard(final GameRules theBoard) {
        this.gameRules = theBoard;
    }

    public void setCurrentPlayer(final Player player) {
        this.currentPlayer = player;
    }

    public void setPlayers(final Player onePlayer, final Player twoPlayer) {
        this.players[0] = onePlayer;
        this.players[1] = twoPlayer;

        gameRules.registerPlayers(onePlayer, twoPlayer);
    }

    public void startNewGame() {
        gameRules.resetBoard();
        currentPlayer = null;
    }

    @Override //HAVE IT CALL OTHER TOSTRINGS
    public String toString() {
        return gameRules.toString();
    }

}