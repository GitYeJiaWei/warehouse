package com.ioter.warehouse.ui.adapter;



import com.ioter.warehouse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to hold the data for Navigation Drawer Items
 */
public class DrawerListContent {
    //An array of sample (Settings) items.
    public static List<DrawerItem> ITEMS = new ArrayList<>();

    //A map of sample (Settings) items, by ID.
    public static Map<String, DrawerItem> ITEM_MAP = new HashMap<>();

    static {

        addItem(new DrawerItem("1", "设置", R.drawable.dl_sett));
        //addItem(new DrawerItem("2", "Reader列表", R.drawable.dl_rdl));
        //addItem(new DrawerItem("2", "sku转epc", R.drawable.dl_access));
        //addItem(new DrawerItem("3", "清除ScanData", R.mipmap.ic_menu_clear));
        addItem(new DrawerItem("2", "退出", R.mipmap.exit));
    }

    /**
     * Method to add a new item
     *
     * @param item - Item to be added
     */
    private static void addItem(DrawerItem item) {

        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A Drawer item represents an entry in the navigation drawer.
     */
    public static class DrawerItem {
        public String id;
        public String content;
        public int icon;

        public DrawerItem(String id, String content, int icon_id) {
            this.id = id;
            this.content = content;
            this.icon = icon_id;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
