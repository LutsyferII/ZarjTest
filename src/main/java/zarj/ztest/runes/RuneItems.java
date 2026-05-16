package zarj.ztest.runes;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import zarj.ztest.ZarjTest;
import zarj.ztest.tochka.TochkaItems;
import zarj.ztest.tochka.TochkaLow;


public class RuneItems {
    public static final Item RUNE_1= register(new TochRune(new FabricItemSettings(), false,0), "rune_1");
    public static final Item RUNE_2= register(new TochRune(new FabricItemSettings(), true,0), "rune_2");
    public static final Item RUNE_3= register(new TochRune(new FabricItemSettings(), true,10), "rune_3");


    public static Item register(Item item, String id) {
        Identifier itemID = new Identifier(ZarjTest.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }
    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(TochkaItems.ZMOD_GROUP_KEY).register(content -> {
            content.add(RUNE_1);
            content.add(RUNE_2);
            content.add(RUNE_3);
        });
        ZarjTest.ALL_DROPS.addDrop(RUNE_1,1.08);
        ZarjTest.ALL_DROPS.addDrop(RUNE_2,1.04);
        ZarjTest.ALL_DROPS.addDrop(RUNE_3,1.02);
    }
}
