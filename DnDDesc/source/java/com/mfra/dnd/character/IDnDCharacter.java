package com.mfra.dnd.character;

import com.mfra.dnd.checker.Skill;

import java.io.Serializable;

import com.mfra.dnd.checker.checkerManager.Action;
import com.mfra.dnd.dndclass.ADnDClass;
import com.mfra.dnd.feat.AFeat;
import com.mfra.dnd.language.Language;
import com.mfra.dnd.race.ARace;
import com.mfra.dnd.util.CoinsBuilder;

public interface IDnDCharacter extends Serializable {
	/**
	 * @param language
	 */
	public void addBonusLanguage(Language language);

	/**
	 * @param coinsBuilder
	 */
	public void addCoins(CoinsBuilder coinsBuilder);

	/**
	 * @param language
	 */
	public void addLanguage(Language language);

	/**
	 * @param action
	 */
	public void checkAction(Action action);

	/**
	 * @param checkeableName
	 */
	public void checkAction(Enum<?> checkeableName);

	/**
	 * @param checkeableName
	 * @param difficultyClass
	 */
	public void checkAction(Enum<?> checkeableName, Integer difficultyClass);

	/**
	 * @return Name
	 */
	public String getName();

	/**
	 * @return RamdomCoins
	 */
	public CoinsBuilder getRamdomCoins();

	/**
	 * @param value
	 */
	public void setCharisma(int value);

	/**
	 * @param className
	 */
	public void setClass(ADnDClass.DnDClassName className);

	/**
	 * @param value
	 */
	public void setConstitution(int value);

	/**
	 * @param value
	 */
	public void setDexterity(int value);

	/**
	 * @param featName
	 */
	public void setFeat(AFeat.FeatName featName);

	/**
	 * @param featName
	 * @param element
	 */
	public void setFeat(AFeat.FeatName featName, Object element);

	/**
	 * @param value
	 */
	public void setIntelligence(int value);

	/**
	 * @param raceName
	 */
	public void setRace(ARace.RaceName raceName);

	/**
	 * @param value
	 */
	public void setStrength(int value);

	/**
	 * @param value
	 */
	public void setWisdom(int value);

	/**
	 * 
	 */
	public void showAbilities();

	/**
	 * 
	 */
	public void showAttacks();

	/**
	 * 
	 */
	public void showDescProperties();

	/**
	 * 
	 */
	public void showSavingThrows();

	/**
	 * 
	 */
	public void showSkills();

	/**
	 * @param points
	 * @param skillName
	 */
	public void useSkillPoints(int points, Skill.SkillName skillName);
	
	public void create();
}
