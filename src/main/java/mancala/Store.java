package mancala;
import java.io.Serializable;

public class Store implements Countable, Serializable {
    private static final long serialVersionUID = 1L;
    private Player owner;
    private int totalStones;
    private static final int DEFAULT_VAL = 0;


    public void setOwner(final Player myPlayer) {
        this.owner = myPlayer;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public int getStoneCount() {
        return totalStones;
    }

    @Override
    public void addStone() {
        totalStones++;
    }

    @Override
    public void addStones(final int numToAdd) {
        totalStones += numToAdd;
    }

    @Override
    public int removeStones() {
        final int stonesRemoved = totalStones;
        totalStones = DEFAULT_VAL;
        return stonesRemoved;
    }

    @Override
    public String toString() {
        return "Store [owner=" + owner + ", totalStones=" + totalStones + "]";
    }
}