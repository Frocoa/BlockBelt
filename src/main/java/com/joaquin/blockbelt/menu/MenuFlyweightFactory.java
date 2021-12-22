package com.joaquin.blockbelt.menu;


import java.util.HashMap;
import java.util.List;

/**
 * Class that represents the Belt
 */
public class MenuFlyweightFactory {

    private final HashMap<Integer, Menu> cache = new HashMap<>();
    private static MenuFlyweightFactory uniqueInstance;

    private MenuFlyweightFactory() {

    }

    public static MenuFlyweightFactory getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuFlyweightFactory();
        }
        return uniqueInstance;
    }

    private Menu createMenu(Menu menu) {
        int hashCode = menu.hashCode();
        if(!cache.containsKey(menu.hashCode())) {
            cache.put(hashCode, menu);
        }
        return cache.get(hashCode);
    }

    public Menu createMenu(List<String> materialsList) {
        return createMenu(new Menu(materialsList));
    }
}
