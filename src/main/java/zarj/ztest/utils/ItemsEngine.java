package zarj.ztest.utils;

import zarj.ztest.blocks.BlocksHandler;
import zarj.ztest.runes.RuneItems;
import zarj.ztest.tochka.TochkaItems;
import zarj.ztest.zvezdy.StarsItems;

public class ItemsEngine {

    public static void InitializeALL(){
        TochkaItems.initialize();
        RuneItems.initialize();
        StarsItems.initialize();


        BlocksHandler.initialize();
    }
}
