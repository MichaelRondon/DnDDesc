package com.mfra.dnd.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import junit.framework.Assert;
import com.mfra.dnd.character.CharacterManager;
import com.mfra.dnd.character.DnDCharacter;
import com.mfra.dnd.checker.AttackBonus;
import com.mfra.dnd.dndclass.ADnDClass;
import com.mfra.dnd.dndclass.ADnDClass.Level;
import com.mfra.dnd.util.DnDUtil;

/**
 * @author Michael Felipe Rondón Acosta
 */
public class Test {

    /**
     * 
     */
    @org.junit.Test
    public void testLevel() {
        try {
            Class<Level> declaringClass = ADnDClass.Level.FIRST.getDeclaringClass();
            Method[] methods = declaringClass.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println(method.getName());
            }

            Method declaredMethod = declaringClass.getDeclaredMethod("getRequiredXP");
            declaredMethod.setAccessible(true);
            Integer invoke = (Integer) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals(invoke.intValue(), 190000);
            declaredMethod = declaringClass.getDeclaredMethod("getBaseSaveBonusGood");
            declaredMethod.setAccessible(true);
            invoke = (Integer) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals(invoke.intValue(), 12);
            declaredMethod = declaringClass.getDeclaredMethod("getBaseSaveBonusPoor");
            declaredMethod.setAccessible(true);
            invoke = (Integer) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals(invoke.intValue(), 6);
            
            declaredMethod = declaringClass.getDeclaredMethod("getBaseAttackBonusGood");
            declaredMethod.setAccessible(true);
            int[] invokeArray = (int[]) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals(DnDUtil.getInstance().intArrayToString(invokeArray), "20/15/10/5");
            declaredMethod = declaringClass.getDeclaredMethod("getBaseAttackBonusAverage");
            declaredMethod.setAccessible(true);
            invokeArray = (int[]) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals(DnDUtil.getInstance().intArrayToString(invokeArray), "15/10/5");
            declaredMethod = declaringClass.getDeclaredMethod("getBaseAttackBonusPoor");
            declaredMethod.setAccessible(true);
            invokeArray = (int[]) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals(DnDUtil.getInstance().intArrayToString(invokeArray), "10/5");
            
            declaredMethod = declaringClass.getDeclaredMethod("getMaxClassSkillRanks");
            declaredMethod.setAccessible(true);
            int i = (Integer) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals(i, 23);
            declaredMethod = declaringClass.getDeclaredMethod("getMaxNOClassSkillRanks");
            declaredMethod.setAccessible(true);
            BigDecimal bg = (BigDecimal) declaredMethod.invoke(ADnDClass.Level.TWENTY);
            Assert.assertEquals((new BigDecimal(11.5)).doubleValue(), 11.5);

            
            
            DnDCharacter testCharacter = getTestCharacter(ADnDClass.Level.TWENTY);
            testCharacter.checkAction(AttackBonus.AttackName.MELEE_ATTACK);
            
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    private DnDCharacter getTestCharacter(ADnDClass.Level level){
        DnDCharacter testCharacter = getTestCharacter();
        Field declaredField;
        try {
            declaredField = DnDCharacter.class.getDeclaredField("descProperties");
            declaredField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Object>  desc = (HashMap<String, Object>)declaredField.get(testCharacter);
            ((ADnDClass)desc.get(ADnDClass.KEY_NAME)).setLevel(level);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return testCharacter;
    }
    
    /**
     * 
     */
    private DnDCharacter getTestCharacter(){
        DnDCharacter resp = null;
        CharacterManager ch = new CharacterManager();
        Class<CharacterManager> declaringClass = CharacterManager.class;
        Method declaredMethod;
        try {
            declaredMethod = declaringClass.getDeclaredMethod("getElfTestCharacter");
            declaredMethod.setAccessible(true);
            resp= (DnDCharacter) declaredMethod.invoke(ch);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return resp;
    }
}
