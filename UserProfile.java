
package mancala;

import java.io.Serializable;


public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L; // Ensure version control for serialization

    private String name;
    private int kalahGamesPlayed;
    private int ayoGamesPlayed;
    private int kalahGamesWon;
    private int ayoGamesWon;

    public UserProfile() {
        // Default constructor
    }

    // Constructor with parameters
    public UserProfile(final String name, final int kalahGamesPlayed, final int ayoGamesPlayed, final int kalahGamesWon, final int ayoGamesWon) {
        this.name = name;
        this.kalahGamesPlayed = kalahGamesPlayed;
        this.ayoGamesPlayed = ayoGamesPlayed;
        this.kalahGamesWon = kalahGamesWon;
        this.ayoGamesWon = ayoGamesWon;
    }

    // Getter and setter methods

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getKalahGamesPlayed() {
        return kalahGamesPlayed;
    }

    public void setKalahGamesPlayed(final int kalahGamesPlayed) {
        this.kalahGamesPlayed = kalahGamesPlayed;
    }

    public int getAyoGamesPlayed() {
        return ayoGamesPlayed;
    }

    public void setAyoGamesPlayed(final int ayoGamesPlayed) {
        this.ayoGamesPlayed = ayoGamesPlayed;
    }

    public int getKalahGamesWon() {
        return kalahGamesWon;
    }

    public void setKalahGamesWon(final int kalahGamesWon) {
        this.kalahGamesWon = kalahGamesWon;
    }

    public int getAyoGamesWon() {
        return ayoGamesWon;
    }

    public void setAyoGamesWon(final int ayoGamesWon) {
        this.ayoGamesWon = ayoGamesWon;
    }

//needed?
    @Override
    public String toString() {
        return "UserProfile:  \n " +
                "userName = '" + name + '\'' +
                ",\n kalahGamesPlayed = " + kalahGamesPlayed +
                ",\n ayoGamesPlayed = " + ayoGamesPlayed +
                ",\n kalahGamesWon = " + kalahGamesWon +
                ",\n ayoGamesWon = " + ayoGamesWon;
    }
}