package com.afirez.wevideo.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Created by afirez on 18-3-22.
 */

public class Fragments {

    private Fragments() {
    }

    private static volatile Fragments instance;

    public static Fragments getInstance() {
        if (instance == null) {
            synchronized (Fragments.class) {
                if (instance == null) {
                    instance = new Fragments();
                }
            }
        }
        return instance;
    }

    public Fragment newInstance(Class<? extends Fragment> clazz) {
        return newInstance(clazz, new Bundle());
    }

    public Fragment newInstance(Class<? extends Fragment> clazz, Bundle args) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) clazz.newInstance();
            fragment.setArguments(args);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    private HashMap<String, Fragment> fragmentMap;

    public Fragment findOrNewInstance(Class<? extends Fragment> clazz) {
        return findOrNewInstance(clazz, true);
    }

    public Fragment findOrNewInstance(Class<? extends Fragment> clazz, boolean needCache) {
        Fragment fragment;
        String name = clazz.getName();
        if (fragmentMap.containsKey(name)) {
            fragment = fragmentMap.get(name);
        } else {
            fragment = newInstance(clazz);
        }
        if (fragment != null && needCache) {
            if (fragmentMap == null) {
                fragmentMap = new HashMap<>();
            }
            fragmentMap.put(name, fragment);
        }
        return fragment;
    }
}
