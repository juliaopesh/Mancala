package mancala;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Store store;
    private UserProfile userProfile;

    public Player(){
    }

    public Player(final UserProfile userProfile) {
        this.userProfile = userProfile;
        this.name = userProfile.getName();  // Set the name from UserProfile
    }

    public String getName() {
        return name;
    }

    public Store getStore() {
        return store;
    }

    public int getStoreCount() {
        return store.getStoneCount(); //check if null
    }

    public void setName(final String myName) {
        this.name = myName;
    }

    public void setStore(final Store myStore) {
        this.store = myStore;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(final UserProfile userProfile) {
        this.userProfile = userProfile;
        this.name = userProfile.getName();  // Set the name from UserProfile
    }

    @Override
    public String toString() {
        return "Player: " + name + ", Store: " + store;
    }
}
