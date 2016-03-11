package com.mfra.dnd.character;

import com.mfra.dnd.checker.AttackBonus;
import com.mfra.dnd.checker.Skill;
import com.mfra.dnd.checker.checkerManager.Action;
import com.mfra.dnd.dndclass.ADnDClass.AttackBonusTypes;
import com.mfra.dnd.dndclass.ADnDClass.DnDClassName;
import com.mfra.dnd.feat.AFeat;
import com.mfra.dnd.language.Language;
import com.mfra.dnd.race.ARace;
import com.mfra.dnd.util.DnDUtil;
import com.mfra.dnd.weapon.WeaponName;

/**
 * @author Michael Felipe Rondón Acosta
 */
public class CharacterManager {

    /**
     * @param args
     */
    public static void main(String[] args) {
        DnDUtil.getInstance().generateAbilityRamdomScores(true);
        DnDUtil.getInstance().generateAbilityRamdomScores(false);

        DnDCharacter halfOrcTestCharacter = getHalfOrcTestCharacter();
        halfOrcTestCharacter.showAbilities();
        halfOrcTestCharacter.showSkills();
        halfOrcTestCharacter.showDescProperties();

        DnDCharacter humanCharacter = getHumanTestCharacter();
        humanCharacter.showAbilities();
        humanCharacter.showSkills();
        humanCharacter.addLanguage(Language.DRUIDIC);
        humanCharacter.showDescProperties();

        CharacterSerializer characterSerializer = new CharacterSerializer(getElfTestCharacter());
        characterSerializer.process();
        DnDCharacter elfCharacter = characterSerializer.getCharacter("ElfTest1");
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

    private static DnDCharacter getElfTestCharacter() {
        DnDCharacter character = new DnDCharacter("ElfTest1");
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

    private static DnDCharacter getHalfOrcTestCharacter() {
        DnDCharacter character = new DnDCharacter("HumanTest1");
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

    private static DnDCharacter getHumanTestCharacter() {
        DnDCharacter character = new DnDCharacter("HalfOrcTest1");
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
}
