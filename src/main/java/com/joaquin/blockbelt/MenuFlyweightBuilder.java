package com.joaquin.blockbelt;


import java.util.HashMap;
import java.util.List;

/**
 * Class that represents the Belt
 */
public class MenuFlyweightBuilder {

    private final HashMap<Integer, Menu> cache = new HashMap<>();
    private static MenuFlyweightBuilder uniqueInstance;

    private MenuFlyweightBuilder() {

    }

    public static MenuFlyweightBuilder getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuFlyweightBuilder();
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
