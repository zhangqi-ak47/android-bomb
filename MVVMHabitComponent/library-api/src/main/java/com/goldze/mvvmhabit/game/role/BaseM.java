package com.goldze.mvvmhabit.game.role;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseM {

    public Map toMap(){
        try {
            return BeanUtils.describe(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean toBean(Map map){
        boolean bl=false;
        try {
            BeanUtils.populate(this, map);
            bl=true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bl;
    }

}
