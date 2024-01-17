package mancala;

public class AyoRules extends GameRules{
    private static final long serialVersionUID = 1L;


    private final int lowIndex = 0;
    private final int highIndex = 11;
    private final int pNum1 = 1;
    private final int pNum2 = 2;
    private final int singleStone = 1;

    
    public AyoRules(){
        super();
    }

        //Should keep running (multi lap) UNLESS capture occurs (?)
    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
        try{
            validatePitBounds(startPit);
        }catch (InvalidMoveException e){

        }

        validatePitOwnership(startPit, playerNum);
        validateNonEmptyPit(startPit); //Validation cleared
    
        final int stonesAdded = distributeStonesAndUpdateCount(startPit, playerNum); //Distribute & return cleared
    
        return stonesAdded;
    }

    private void validatePitBounds(final int startPit) throws InvalidMoveException {
        if (startPit < LOW_PIT || startPit > HI_PIT) {
            throw new InvalidMoveException();
        }
    }
    
    private void validatePitOwnership(final int startPit, final int playerNum) throws InvalidMoveException {
        if ((startPit >= 7 && startPit <= HI_PIT && playerNum == pNum1) ||
            (startPit >= LOW_PIT && startPit <= 6 && playerNum == pNum2)) {
            throw new InvalidMoveException("Select a pit on your side.");
        }
        /*else{
            getDataStructure().setSkipPit(startPit);
        }
        */
    }
    private void validateNonEmptyPit(final int pitNum) throws InvalidMoveException {
        if (getDataStructure().getNumStones(pitNum) == 0) {
            throw new InvalidMoveException("The selected pit is empty.");
        }
    }
    
    private int updatePit (final int pit, final int stonesAdded){
        int newIndex = (pit + stonesAdded) % 13; // assuming 12 pits in total
        if (newIndex == lowIndex) {
            newIndex = 13;
        }
        return newIndex;
    }

    private int distributeStonesAndUpdateCount(final int startPit, final int playerNum) {
        int currentPit = startPit;
        final int beforeCount = getDataStructure().getStoreCount(playerNum);
        boolean flag = false;
        
        while (getDataStructure().getNumStones(currentPit) != 0 && !flag) {
                int stonesAdded = getDataStructure().getNumStones(currentPit);
                try {
                    distributeStones(currentPit);
                } catch (PitNotFoundException e) {

                }
                currentPit = updatePit(currentPit, stonesAdded);
                if (currentPit == 6 || currentPit == 13){
                    //isStore = true;
                    flag = true;
                    break;
                }
                 if (getDataStructure().getNumStones(currentPit) == singleStone){
                    flag = true;
                    break;
                }
                
            }


        final int afterCount = getDataStructure().getStoreCount(playerNum);
        return afterCount - beforeCount;
    }
    //End of refactored move


    @Override
    public int distributeStones(final int startingPoint) throws PitNotFoundException {
        validateStartingPoint(startingPoint);
    
        //int pitIndex = getPit(startingPoint);
        //getDataStructure().setIterator(startingPoint,super.currentPlayer,true)
        final int stonesDistributed = distributeStonesFromPit(startingPoint);
    
        return stonesDistributed;
    }
    
    private void validateStartingPoint(final int startingPoint) throws PitNotFoundException {
        if (startingPoint < lowIndex || startingPoint > highIndex) {
            throw new PitNotFoundException();
        }
    }
    
    private int distributeStonesFromPit(final int startingPoint) {
        final int count = getDataStructure().getNumStones(startingPoint);
        getDataStructure().removeStones(startingPoint);
    
        final int currentPitIndex = distributeStonesToPits(count, startingPoint);
        if (currentPitIndex != startingPoint){
            checkCapture(currentPitIndex, startingPoint);
        }
    
        return count;
    }
    
    private int distributeStonesToPits(int stoneCount, int startingPoint) {
        int currentPitIndex = startingPoint;
        startingPoint --; //index
        for (int i = 1; i <= stoneCount; i++) {
            setFreeTurn(false);
            currentPitIndex = (startingPoint + i) % 12;
            if (currentPitIndex != startingPoint) {
                if (currentPitIndex == 6 && startingPoint >= 0 && startingPoint <= 5) {
                    distributeStoneToStoreAndPit(pNum1, currentPitIndex, stoneCount, i);
                    stoneCount--;
                } else if (currentPitIndex == 0 && startingPoint >= 6 && startingPoint <= highIndex) {
                    distributeStoneToStoreAndPit(pNum2, currentPitIndex, stoneCount, i);
                    stoneCount--;
                } else {
                    distributeStoneToPit(currentPitIndex);
                }
            }
        }
    
        return currentPitIndex;
    }
 
    
    private void distributeStoneToStoreAndPit(final int storeNum, final int currentPitIndex, final int stoneCount, final int index) {
        distributeStoneToStore(storeNum);
        if (index != stoneCount) {
            getDataStructure().addStones((currentPitIndex + 1), singleStone);
        } 
    }
    
    private void distributeStoneToStore(final int storeNum) {
        getDataStructure().addToStore(storeNum, singleStone);
        //getDataStructure().setStore
        //stores.get(storeIndex).getOwner().setStore(stores.get(storeIndex));
    }
    
    private void distributeStoneToPit(final int currentPitIndex) {
        getDataStructure().addStones((currentPitIndex + 1), singleStone);
    }
    //CAPTURE MAY BE DIFFERENT IN AYO
    private void checkCapture(final int currentPitIndex, final int startingPoint) {
        if(currentPitIndex < 6 && startingPoint < 6){
            if (getDataStructure().getNumStones(currentPitIndex + 1) == 1){ //Check if its on the current players side!!{
                super.setIfCapture(true);
            }
        }else if(currentPitIndex >= 6 && startingPoint >= 6){
            if (getDataStructure().getNumStones(currentPitIndex + 1) == 1){ //Check if its on the current players side!!{
                super.setIfCapture(true);
            }
        }
    }
    
    //End of Distribute methods

    @Override
    public int captureStones(final int stoppingPoint) throws PitNotFoundException {
        validatePitBounds(stoppingPoint);

        final int pitStopIndex = stoppingPoint - 1;
        final int oppositePitIndex = calculateOppositePitIndex(stoppingPoint);
        final int capturedStones = pullFromPits(pitStopIndex, oppositePitIndex);

        return capturedStones;
    }

    private int calculateOppositePitIndex(final int stoppingPoint) {
        return highIndex - (stoppingPoint - 1);
    }

    private int pullFromPits(final int pitStop, final int oppositePitIndex) {
        final int stonesTaken = getDataStructure().getNumStones(oppositePitIndex + 1);
        //pitStop.removeStones(); //only capture opposite
        getDataStructure().removeStones(oppositePitIndex + 1);

        return stonesTaken;
    }

}