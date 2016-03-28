package com.mfra.dnd.character;

import java.util.List;
import com.mfra.dnd.checker.Ability;
import com.mfra.dnd.checker.Ability.AbilityName;
import com.mfra.dnd.checker.AbilityManager;
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
import com.mfra.dnd.manager.AttackBonusManager;
import com.mfra.dnd.manager.SavingThrowsManager;
import com.mfra.dnd.manager.SkillManager;
import com.mfra.dnd.race.ACharacterElement;
import com.mfra.dnd.race.ARace;
import com.mfra.dnd.util.BasicData;
import com.mfra.dnd.util.CoinsBuilder;
import com.mfra.dnd.util.DescProperty;
import com.mfra.dnd.util.DnDUtil;
import com.mfra.dnd.util.IBasicData;
import com.mfra.exceptions.GeneralException;

/**
 * @author Michael Felipe Rondón Acosta
 */
public class DnDCharacter implements Cloneable, IDnDCharacter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final AbilityManager abilityManager;

	private final ActionManager actionManager;
	private AttackBonusManager attackBonusManager;
	// private final HashMap<Enum<?>, ACheckeable> checkProperties = new
	// HashMap<Enum<?>, ACheckeable>();
	// private final HashMap<String, Object> descProperties = new
	// HashMap<String, Object>();
	private final DnDFactory dndFactory;
	private IBasicData iBasicData = new BasicData();
	private SavingThrowsManager savingThrowsManager;
	private final SkillManager skillManager;

	/**
	 * @param name
	 */
	private DnDCharacter(String name) {
		// this.iBasicData = new BasicData(this.checkProperties,
		// this.descProperties);
		this.iBasicData = new BasicData();

		this.dndFactory = new DnDFactory(this.iBasicData);
		this.abilityManager = new AbilityManager(this.iBasicData);
		this.skillManager = new SkillManager(this.iBasicData);
		this.abilityManager.init();
		this.iBasicData.putDescProperty(Skill.SKILL_POINTS, 0);
		this.iBasicData.putDescProperty(DescProperty.CHARACTER_NAME.name(), name);
		this.iBasicData.putDescProperty(DescProperty.AVAILABLE_ABILITY_POINTS.name(), 0);
		this.iBasicData.putDescProperty(DescProperty.CREATED.toString(), false);
		this.actionManager = new ActionManager(iBasicData);
	}

	/**
	 * @param language
	 */
	@Override
	public void addBonusLanguage(Language language) {
		this.iBasicData.validIsClassSet();
		this.iBasicData.validIsRaceSet();
		LanguagesManager languagesManager = (LanguagesManager) this.iBasicData
				.getDescProperty(LanguagesManagerName.LANGUAGES.toString());
		languagesManager.addBonusLanguage(language);

	}

	/**
	 * @param coinsBuilder
	 */
	@Override
	public void addCoins(CoinsBuilder coinsBuilder) {

		Object object = this.iBasicData.getDescProperty(Equipment.COINS.toString());
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
		this.skillManager.usePoints(language);
	}

	/**
	 * @param action
	 */
	@Override
	public void checkAction(Action action) {
		this.actionManager.checkAction(action);
	}

	/**
	 * @param checkeableName
	 */
	@Override
	public void checkAction(Enum<?> checkeableName) {
		this.actionManager.checkAction(checkeableName, null);
	}

	/**
	 * @param checkeableName
	 * @param difficultyClass
	 */
	@Override
	public void checkAction(Enum<?> checkeableName, Integer difficultyClass) {
		this.actionManager.checkAction(checkeableName, difficultyClass);
	}

	@Override
	public void create() {
		this.iBasicData.putDescProperty(DescProperty.CREATED.toString(), true);
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
	 * 
	 */
	@Override
	public List<Ability> getAbilities() {
		List<Ability> properties = this.abilityManager.getProperties(Ability.AbilityName.values(), Ability.class);
		return properties;
	}

	/**
	 * @return Name
	 */
	@Override
	public String getName() {
		return String.valueOf(this.iBasicData.getDescProperty(DescProperty.CHARACTER_NAME.name()));
	}

	/**
	 * @return RamdomCoins
	 */
	@Override
	public CoinsBuilder getRamdomCoins() {
		return ((ADnDClass) this.iBasicData.getDescProperty(ADnDClass.KEY_NAME)).getRamdomCoins();
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
		this.attackBonusManager = new AttackBonusManager(this.iBasicData);
		this.attackBonusManager.init();
		this.savingThrowsManager = new SavingThrowsManager(this.iBasicData);
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
			ACharacterElement<?> characterElementInstance = this.dndFactory
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
		System.out.println(iBasicData.showDescProperties());
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
		this.skillManager.usePoints(points, skillName, this.iBasicData);
	}

	/**
	 * @param classTo
	 * @param name
	 */
	private void setCharacterElement(Class<?> classTo, Enum<?> name) {
		try {
			this.dndFactory.getCharacterElementInstance(classTo, name).setElement();
		} catch (GeneralException e) {
			this.showErrorMessage(e);
		}
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
	 * 
	 * @param name
	 * @return Object with the character attributes.
	 */
	public static IDnDCharacter getCharacter(String name) {
		return new DnDCharacter(name);
	}
}
