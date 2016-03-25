package com.mfra.dnd.character;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mfra.dnd.util.DnDUtil;
import com.mfra.exceptions.GeneralException;

/**
 * @author Michael Felipe Rondón Acosta
 */
public class CharacterSerializer {

    // private final IDnDCharacter dndCharacter;
    private static final String SER_FILE_DIRECTORY = "serFiles";

    /**
     * @param dndCharacter
     */
    public CharacterSerializer() {
    }

    public List<IDnDCharacter> getAllCharacters() {
        List<IDnDCharacter> characters = new LinkedList<IDnDCharacter>();

        Path path = Paths.get(CharacterSerializer.SER_FILE_DIRECTORY);
        try (DirectoryStream<Path> newDirectoryStream = Files
                .newDirectoryStream(path, "*.ser")) {
            for (Iterator<Path> iterator = newDirectoryStream.iterator(); iterator
                    .hasNext();) {
                path = iterator.next();
                IDnDCharacter character = this.getCharacter(path);
                characters.add(character);
            }
        } catch (IOException e) {
            throw new GeneralException(e);
        }
        return characters;
    }

    /**
     * @param fileName
     * @return DnDCharacter
     */
    public IDnDCharacter getCharacter(Path path) {
        DnDCharacter dnDCharacter = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(path.toFile()));) {

            dnDCharacter = (DnDCharacter) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            String simpleConcat = DnDUtil.getInstance().simpleConcat(
                    e.getMessage(), System.lineSeparator(), " Filename: ",
                    path.toFile());
            throw new GeneralException(simpleConcat);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dnDCharacter;
    }

    /**
     * @param fileName
     * @return DnDCharacter
     */
    public IDnDCharacter getCharacter(String fileName) {
        Pattern pattern = Pattern.compile("[.]*/.ser");
        Matcher matcher = pattern.matcher(fileName);

        if (fileName == null || fileName.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid name: ");
            stringBuilder.append(fileName);
            throw new GeneralException(stringBuilder.toString());
        } else if (!matcher.matches()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(fileName);
            stringBuilder.append(".ser");
            fileName = stringBuilder.toString();
        }

        Path path = FileSystems.getDefault().getPath(SER_FILE_DIRECTORY,
                fileName);
        return this.getCharacter(path);
    }

    /**
	 * 
	 */
    public void process(IDnDCharacter dndCharacter) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dndCharacter.getName());
        stringBuilder.append(".ser");

        Path path = Paths.get(SER_FILE_DIRECTORY);

        try {
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException e) {
        } catch (IOException e) {
            throw new GeneralException(e);
        }
        path = Paths.get(path.toString(), stringBuilder.toString());

        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(
                    path.toString()));
            objectOutputStream.writeObject(dndCharacter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
