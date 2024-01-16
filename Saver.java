package mancala;

import java.io.*;

//use files class to find current diectory, find assets folder

//paths.get(system.getProperty("user.dir")); (slides)

public class Saver implements Serializable{
    private static final long serialVersionUID = 1L;

    public static void saveObject(Serializable toSave, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("assets/" +filename))) {
            oos.writeObject(toSave);
        }
    }

    public static Serializable loadObject(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Serializable) ois.readObject();
        }
    }
}