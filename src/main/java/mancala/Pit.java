package mancala;
import java.io.Serializable;
//interfaces use implements instead of extends
public class Pit implements Countable, Serializable{

    private static final long serialVersionUID = 1L;
    private int stoneCount;
    private final int defaultVal = 0;
    

    public Pit() {
        this.stoneCount = defaultVal;
    }

    @Override
    public int getStoneCount() {
        return stoneCount;
    }

    @Override
    public void addStone() {
        stoneCount++;
    }

    @Override
    public void addStones(final int numToAdd) {
        stoneCount += numToAdd;
    }

    @Override
    public int removeStones() {
        int stonesRemoved = stoneCount;
        stoneCount = defaultVal;
        return stonesRemoved;
    }
    //Additional method for testing
    void setStones(final int stones) {
        this.stoneCount = stones;
    }

    @Override
    public String toString() {
        return "Pit: " + stoneCount + " stones";
    }
}