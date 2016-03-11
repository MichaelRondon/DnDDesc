package com.mfra.dnd.character;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mfra.dnd.util.DnDUtil;
import com.mfra.exceptions.GeneralException;

/**
 * @author Michael Felipe Rondón Acosta
 */
public class CharacterSerializer {

	private final IDnDCharacter dndCharacter;

	/**
	 * @param dndCharacter
	 */
	public CharacterSerializer(IDnDCharacter dndCharacter) {
		this.dndCharacter = dndCharacter;
	}

	/**
	 * @param fileName
	 * @return DnDCharacter
	 */
	public DnDCharacter getCharacter(String fileName) {
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
		DnDCharacter dnDCharacter = null;
		ObjectInputStream objectInputStream;
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
			dnDCharacter = (DnDCharacter) objectInputStream.readObject();
		} catch (FileNotFoundException e) {
			String simpleConcat = DnDUtil.getInstance().simpleConcat(e.getMessage(), System.lineSeparator(),
					" Filename: ", fileName);
			throw new GeneralException(simpleConcat);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dnDCharacter;
	}

	/**
	 * 
	 */
	public void process() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(dndCharacter.getName());
		stringBuilder.append(".ser");
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream((new FileOutputStream(stringBuilder.toString())));
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
