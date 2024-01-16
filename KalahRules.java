package mancala;

public class KalahRules extends GameRules{

    private static final long serialVersionUID = 1L;

    private final int lowIndex = 0;
    private final int highIndex = 11;
    private final int pNum1 = 1;
    private final int pNum2 = 2;
    private final int storeIndex1 = 6;

    
    public KalahRules(){
        super();
    }

    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
        try{
            validatePitBounds(startPit);
        }catch (PitNotFoundException e){
           
        }

        validatePitOwnership(startPit, playerNum);
        validateNonEmptyPit(startPit); //Validation cleared
    
        final int stonesAdded = distributeStonesAndUpdateCount(startPit, playerNum); //Distribute & return cleared
    
        return stonesAdded;
    }

    private void validatePitBounds(final int checkPit) throws PitNotFoundException {
        if (checkPit < LOW_PIT || checkPit > HI_PIT) {
            throw new PitNotFoundException("Pit out of bounds.");
        }
    }

    private void validatePitOwnership(final int myPit,final int playerNum) throws InvalidMoveException {
        if ((myPit >= 7 && myPit <= HI_PIT && playerNum == pNum1) ||
            (myPit >= LOW_PIT && myPit <= storeIndex1 && playerNum == pNum2)) {
            throw new InvalidMoveException("Select a pit on your side.");
        }
    }
    private void validateNonEmptyPit(final int pitNum) throws InvalidMoveException {
        if (getDataStructure().getNumStones(pitNum) == 0) {
            throw new InvalidMoveException("The selected pit is empty.");
        }
    }
    
    private int distributeStonesAndUpdateCount(final int pitStart, final int playerNum) {
        final int beforeCount = getDataStructure().getStoreCount(playerNum);
        try {
            distributeStones(pitStart);
        } catch (PitNotFoundException e) {
            return 0;
        }
        int afterCount = getDataStructure().getStoreCount(playerNum);
    
        return afterCount - beforeCount;
    }
    //End of refactored move


    @Override
    public int distributeStones(final int startingPoint) throws PitNotFoundException {
        validateStartingPoint(startingPoint);
    
        //Pit pit = getPit(startingPoint);
        final int count = distributeStonesFromPit(startingPoint);
    
        return count;
    }
    
    private void validateStartingPoint(final int startPoint) throws PitNotFoundException {
        if (startPoint < lowIndex || startPoint > highIndex) {
            throw new PitNotFoundException();
        }
    }

    
    private int distributeStonesFromPit(final int firstPit) {
        final int count = getDataStructure().getNumStones(firstPit);
        getDataStructure().removeStones(firstPit);
    
        final int currentPitIndex = distributeStonesToPits(count, firstPit);
    
        checkCapture(currentPitIndex, firstPit);
    
        return count;
    }
    
    private int distributeStonesToPits(int stoneCount, int startingPit) {
        int currentPitIndex = startingPit;
        startingPit --;
        for (int i = 1; i <= stoneCount; i++) {
            setFreeTurn(false);
            currentPitIndex = (startingPit + i) % 12;
            if (currentPitIndex == storeIndex1 && startingPit >= lowIndex && startingPit <= 5) {
                distributeStoneToStoreAndPit(0, currentPitIndex, stoneCount, i);
                stoneCount--;
            } else if (currentPitIndex == lowIndex && startingPit >= storeIndex1 && startingPit <= highIndex) {
                distributeStoneToStoreAndPit(1, currentPitIndex, stoneCount, i);
                stoneCount--;
            } else {
                distributeStoneToPit(currentPitIndex);
            }
        }
    
        return currentPitIndex;
    }
    
 
    
    private void distributeStoneToStoreAndPit(final int storeIndex, final int currentPitIndex, final int stoneCount, final int index) {
        distributeStoneToStore(storeIndex + 1);
        if (index == stoneCount) {
            setFreeTurn(true);
        } else {
            getDataStructure().addStones((currentPitIndex + 1), 1);
        }
    }
    
    private void distributeStoneToStore(final int storeNum) {
        getDataStructure().addToStore(storeNum, 1);
        //stores.get(storeIndex).getOwner().setStore(stores.get(storeIndex));
    }
    
    private void distributeStoneToPit(final int currentPitIndex) {
        getDataStructure().addStones(currentPitIndex + 1, 1);
    }
    
    private void checkCapture(final int currentPitIndex, final int pitFirst) {
        if(currentPitIndex < 6 && pitFirst < 6){
            if (getDataStructure().getNumStones(currentPitIndex + 1) == 1){ //Check if its on the current players side!!{
                super.setIfCapture(true);
            }
        }else if(currentPitIndex >= 6 && pitFirst >= 6){
            if (getDataStructure().getNumStones(currentPitIndex + 1) == 1){ //Check if its on the current players side!!{
                super.setIfCapture(true);
            }
        }
    }
    
    //End of Distribute methods

    @Override
    public int captureStones(final int stoppingPoint) throws PitNotFoundException {
        validatePitBounds(stoppingPoint);

        final int pitStopIndex = stoppingPoint -1;
        final int oppositePitIndex = calculateOppositePitIndex(stoppingPoint);
        final int capturedStones = pullFromPits(pitStopIndex, oppositePitIndex);

        return capturedStones;
    }

    private int calculateOppositePitIndex(final int endPoint) {
        return 11 - (endPoint - 1);
    }

    private int pullFromPits(final int pitStopIndex, final int oppositePitIndex) {
        final int stonesTaken = 1 + getDataStructure().getNumStones(oppositePitIndex + 1);
        getDataStructure().removeStones(pitStopIndex + 1);
        getDataStructure().removeStones(oppositePitIndex + 1);

        return stonesTaken;
    }

    // Implement other Kalah-specific rules
    // Override methods as needed
}