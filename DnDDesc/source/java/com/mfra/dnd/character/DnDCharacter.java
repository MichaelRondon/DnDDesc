package com.mfra.dnd.character;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import com.mfra.dnd.checker.ACheckeable;
import com.mfra.dnd.checker.Ability;
import com.mfra.dnd.checker.Ability.AbilityName;
import com.mfra.dnd.checker.AttackBonus;
import com.mfra.dnd.checker.SavingThrows;
import com.mfra.dnd.checker.Skill;
import com.mfra.dnd.checker.checkerManager.Action;
import com.mfra.dnd.checker.checkerManager.ActionManager;
import com.mfra.dnd.difficultyclass.ADifficultyClass;
import com.mfra.dnd.difficultyclass.ArmorClass;
import com.mfra.dnd.difficultyclass.HitPoints;
import com.mfra.dnd.dndclass.ADnDClass;
import com.mfra.dnd.equipment.Coins;
import com.mfra.dnd.equipment.Equipment;
import com.mfra.dnd.factory.DnDFactory;
import com.mfra.dnd.feat.AFeat;
import com.mfra.dnd.language.Language;
import com.mfra.dnd.language.LanguagesManager;
import com.mfra.dnd.language.LanguagesManager.LanguagesManagerName;
import com.mfra.dnd.manager.AbilityManager;
import com.mfra.dnd.manager.AttackBonusManager;
import com.mfra.dnd.manager.SavingThrowsManager;
import com.mfra.dnd.manager.SkillManager;
import com.mfra.dnd.misc.Misc;
import com.mfra.dnd.race.ACharacterElement;
import com.mfra.dnd.race.ARace;
import com.mfra.dnd.util.CoinsBuilder;
import com.mfra.dnd.util.DnDUtil;
import com.mfra.exceptions.GeneralException;

/**
 * @author Michael Felipe Rondón Acosta
 */
public class DnDCharacter implements Cloneable, IDnDCharacter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param name
	 */
	public static IDnDCharacter getCharacter(String name) {
		return new DnDCharacter(name);
	}

	private AbilityManager abilityManager;
	private final ActionManager actionManager;
	private AttackBonusManager attackBonusManager;
	private HashMap<Enum<?>, ACheckeable> checkProperties = new HashMap<Enum<?>, ACheckeable>();
	private HashMap<String, Object> descProperties = new HashMap<String, Object>();
	private final DnDFactory dndFactory;
	private SavingThrowsManager savingThrowsManager;
	private SkillManager skillManager;

	/**
	 * @param name
	 */
	private DnDCharacter(String name) {
		this.dndFactory = new DnDFactory(checkProperties, descProperties);
		this.abilityManager = new AbilityManager(this.checkProperties);
		this.skillManager = new SkillManager(this.checkProperties);
		this.abilityManager.init();
		this.descProperties.put(Skill.SKILL_POINTS, 0);
		this.descProperties.put(Misc.CHARACTER_NAME.name(), name);
		this.actionManager = new ActionManager(checkProperties, descProperties);
	}

	/**
	 * @param language
	 */
	@Override
	public void addBonusLanguage(Language language) {
		DnDUtil.getInstance().validIsClassSet(this.descProperties);
		DnDUtil.getInstance().validIsRaceSet(this.descProperties);
		LanguagesManager languagesManager = ((LanguagesManager) this.descProperties
				.get(LanguagesManagerName.LANGUAGES.toString()));
		languagesManager.addBonusLanguage(language);

	}

	/**
	 * @param coinsBuilder
	 */
	@Override
	public void addCoins(CoinsBuilder coinsBuilder) {

		Object object = this.descProperties.get(Equipment.COINS.toString());
		if (object instanceof Coins) {
			((Coins) object).addCoins(coinsBuilder);
		}
	}

	/**
	 * @param goldToAdd
	 * @param silverToAdd
	 * @param copperToAdd
	 */
	public void addCoins(int goldToAdd, int silverToAdd, int copperToAdd) {
		CoinsBuilder coinsBuilder = new CoinsBuilder();
		coinsBuilder.setGold(goldToAdd);
		coinsBuilder.setSilver(silverToAdd);
		coinsBuilder.setCopper(copperToAdd);

		this.addCoins(coinsBuilder);
	}

	/**
	 * @param language
	 */
	@Override
	public void addLanguage(Language language) {
		this.skillManager.usePoints(language, this.descProperties);
	}

	/**
	 * @param action
	 */
	@Override
	public void checkAction(Action action) {
		actionManager.checkAction(action);
	}

	/**
	 * @param checkeableName
	 */
	@Override
	public void checkAction(Enum<?> checkeableName) {
		actionManager.checkAction(checkeableName, null);
	}

	/**
	 * @param checkeableName
	 * @param difficultyClass
	 */
	@Override
	public void checkAction(Enum<?> checkeableName, Integer difficultyClass) {
		actionManager.checkAction(checkeableName, difficultyClass);
	}

	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			this.showErrorMessage(e);
			throw new GeneralException(DnDUtil.getInstance().simpleConcat(e.getMessage()));
		}
	}

	/**
	 * In a round, you can do one of these four things: Take a standard action
	 * and then a move action; take a move action and then a standard action;
	 * take two move actions; or perform a full-round action. (See Chapter 8:
	 * Combat for details.)
	 */
	public void doSomeThing() {
	}

	/**
	 * @return Name
	 */
	@Override
	public String getName() {
		return String.valueOf(descProperties.get(Misc.CHARACTER_NAME.name()));
	}

	/**
	 * @return RamdomCoins
	 */
	@Override
	public CoinsBuilder getRamdomCoins() {
		return ((ADnDClass) this.descProperties.get(ADnDClass.KEY_NAME)).getRamdomCoins();
	}

	/**
	 * @param classTo
	 * @param name
	 */
	private void setCharacterElement(Class<?> classTo, Enum<?> name) {
		try {
			dndFactory.getCharacterElementInstance(classTo, name).setElement();
		} catch (GeneralException e) {
			this.showErrorMessage(e);
		}
	}

	/**
	 * @param value
	 */
	@Override
	public void setCharisma(int value) {
		this.abilityManager.setAbility(AbilityName.CHARISMA, value);
	}

	/**
	 * @param className
	 */
	@Override
	public void setClass(ADnDClass.DnDClassName className) {
		this.setCharacterElement(className.getClassTo(), className);
		this.setCharacterElement(Coins.class, Equipment.COINS);
		this.setCharacterElement(HitPoints.class, ADifficultyClass.DCKeyName.HIT_POINTS);
		this.setCharacterElement(ArmorClass.class, ADifficultyClass.DCKeyName.ARMOR_CLASS);
		this.attackBonusManager = new AttackBonusManager(this.checkProperties, this.descProperties);
		this.attackBonusManager.init();
		this.savingThrowsManager = new SavingThrowsManager(this.checkProperties, this.descProperties);
		this.savingThrowsManager.init();
		this.setCharacterElement(LanguagesManager.class, LanguagesManager.LanguagesManagerName.LANGUAGES);
	}

	/**
	 * @param value
	 */
	@Override
	public void setConstitution(int value) {
		this.abilityManager.setAbility(AbilityName.CONSTITUTION, value);
	}

	/**
	 * @param value
	 */
	@Override
	public void setDexterity(int value) {
		this.abilityManager.setAbility(AbilityName.DEXTERITY, value);

	}

	/**
	 * @param featName
	 */
	@Override
	public void setFeat(AFeat.FeatName featName) {
		this.setCharacterElement(featName.getClassTo(), featName);
	}

	/**
	 * @param featName
	 * @param element
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setFeat(AFeat.FeatName featName, Object element) {
		try {
			ACharacterElement<?> characterElementInstance = dndFactory
					.getCharacterElementInstance(featName.getClassTo(), featName);
			if (characterElementInstance instanceof AFeat) {
				((AFeat<Object>) characterElementInstance).setElement(element, false);
			} else {
				throw new GeneralException("The instace is not an AFeat");
			}
		} catch (GeneralException e) {
			this.showErrorMessage(e);
		}
	}

	/**
	 * @param value
	 */
	@Override
	public void setIntelligence(int value) {
		this.abilityManager.setAbility(AbilityName.INTELLIGENCE, value);
	}

	/**
	 * @param raceName
	 */
	@Override
	public void setRace(ARace.RaceName raceName) {
		this.setCharacterElement(raceName.getClassTo(), raceName);
	}

	/**
	 * @param value
	 */
	@Override
	public void setStrength(int value) {
		this.abilityManager.setAbility(AbilityName.STRENGTH, value);
	}

	/**
	 * @param value
	 */
	@Override
	public void setWisdom(int value) {
		this.abilityManager.setAbility(AbilityName.WISDOM, value);
	}

	/**
	 * 
	 */
	@Override
	public void showAbilities() {
		StringBuilder showProperties = this.abilityManager.showProperties(Ability.AbilityName.values());
		System.out.println(showProperties.toString());
	}

	/**
	 * 
	 */
	@Override
	public void showAttacks() {
		StringBuilder showProperties = this.attackBonusManager.showProperties(AttackBonus.AttackName.values());
		System.out.println(showProperties.toString());
	}

	/**
	 * 
	 */
	@Override
	public void showDescProperties() {
		Set<Entry<String, Object>> entrySet = this.descProperties.entrySet();
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, Object> entry : entrySet) {
			stringBuilder.append(String.format("%-15s:\t%s", entry.getKey(), entry.getValue()));
			stringBuilder.append(System.getProperty("line.separator"));
		}
		System.out.println(stringBuilder.toString());
	}

	private void showErrorMessage(Exception e) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append("------------------------------------------------------------------------------------------");
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder.append("ERROR:");
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder.append(e.getMessage());
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder
				.append("------------------------------------------------------------------------------------------");
		System.out.println(stringBuilder.toString());
		throw new GeneralException(e);
	}

	/**
	 * 
	 */
	@Override
	public void showSavingThrows() {
		StringBuilder showProperties = this.savingThrowsManager.showProperties(SavingThrows.SavingThrowName.values());
		System.out.println(showProperties.toString());
	}

	/**
	 * 
	 */
	@Override
	public void showSkills() {
		StringBuilder showProperties = this.skillManager.showProperties(Skill.SkillName.values());
		System.out.println(showProperties.toString());
	}

	/**
	 * @param points
	 * @param skillName
	 */
	@Override
	public void useSkillPoints(int points, Skill.SkillName skillName) {
		this.skillManager.usePoints(points, skillName, this.descProperties);
	}
}
