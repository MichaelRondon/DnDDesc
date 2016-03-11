package com.mfra.dnd.character;

import com.mfra.dnd.checker.AttackBonus;
import com.mfra.dnd.checker.Skill;
import com.mfra.dnd.checker.checkerManager.Action;
import com.mfra.dnd.dndclass.ADnDClass.DnDClassName;
import com.mfra.dnd.feat.AFeat;
import com.mfra.dnd.language.Language;
import com.mfra.dnd.race.ARace;
import com.mfra.dnd.util.DnDUtil;

/**
 * @author Michael Felipe Rondón Acosta
 */
public class CharacterManager {

	private static IDnDCharacter getElfTestCharacter() {
		IDnDCharacter character = DnDCharacter.getCharacter("ElfTest1");
		character.setCharisma(7);
		character.setWisdom(10);
		character.setStrength(11);
		character.setConstitution(16);
		character.setDexterity(16);
		character.setIntelligence(17);
		character.setRace(ARace.RaceName.ELF);
		character.setClass(DnDClassName.DRUID);
		character.useSkillPoints(3, Skill.SkillName.APPRAISE);
		character.setFeat(AFeat.FeatName.ACROBATIC);
		character.addBonusLanguage(Language.SYLVAN);
		return character;
	}

	private static IDnDCharacter getHalfOrcTestCharacter() {
		IDnDCharacter character = DnDCharacter.getCharacter("HumanTest1");
		character.setCharisma(7);
		character.setWisdom(10);
		character.setStrength(11);
		character.setConstitution(16);
		character.setDexterity(16);
		character.setIntelligence(3);
		character.setRace(ARace.RaceName.HALF_ORC);
		character.setClass(DnDClassName.DRUID);
		return character;
	}

	private static IDnDCharacter getHumanTestCharacter() {
		IDnDCharacter character = DnDCharacter.getCharacter("HalfOrcTest1");
		character.setCharisma(7);
		character.setWisdom(10);
		character.setStrength(11);
		character.setConstitution(16);
		character.setDexterity(16);
		character.setIntelligence(10);
		character.setRace(ARace.RaceName.HUMAN);
		character.setClass(DnDClassName.DRUID);
		return character;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DnDUtil.getInstance().generateAbilityRamdomScores(true);
		DnDUtil.getInstance().generateAbilityRamdomScores(false);

		IDnDCharacter halfOrcTestCharacter = getHalfOrcTestCharacter();
		halfOrcTestCharacter.showAbilities();
		halfOrcTestCharacter.showSkills();
		halfOrcTestCharacter.showDescProperties();

		IDnDCharacter humanCharacter = getHumanTestCharacter();
		humanCharacter.showAbilities();
		humanCharacter.showSkills();
		humanCharacter.addLanguage(Language.DRUIDIC);
		humanCharacter.showDescProperties();

		CharacterSerializer characterSerializer = new CharacterSerializer(getElfTestCharacter());
		characterSerializer.process();
		IDnDCharacter elfCharacter = characterSerializer.getCharacter("ElfTest1");
		elfCharacter.addCoins(elfCharacter.getRamdomCoins());
		elfCharacter.showAbilities();
		elfCharacter.showSkills();
		elfCharacter.showAttacks();
		elfCharacter.showSavingThrows();
		elfCharacter.showDescProperties();
		elfCharacter.checkAction(Action.INICIATIVE);
		elfCharacter.checkAction(AttackBonus.AttackName.RANGED_ATTACK);
		elfCharacter.checkAction(AttackBonus.AttackName.MELEE_ATTACK);
	}
}
