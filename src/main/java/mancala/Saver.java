package mancala;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Saver{

    public static void saveObject(final Serializable toSave, final String filename) throws IOException{
        //System.out.println("Saving to: " + "assets/" + filename + ".save"); // Paths.get("assets/" + filename)
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("assets/"+filename))){
            output.writeObject(toSave);
        }
    }

    public static Serializable loadObject(final String filename) throws IOException{
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))){
            return (Serializable) input.readObject();
        } catch(ClassNotFoundException err){
            err.getMessage();
        }
        return null;
    }

}