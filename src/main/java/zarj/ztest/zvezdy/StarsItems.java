package zarj.ztest.zvezdy;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import zarj.ztest.ZarjTest;
import zarj.ztest.tochka.TochkaItems;

public class StarsItems {
    public static final Item STAR_OPENER = register(new Item(new FabricItemSettings().maxCount(1).fireproof()), "star_opener");
    public static final Item STAR_ROLLER = register(new Item(new FabricItemSettings().maxCount(1).fireproof()), "star_roller");
    public static final Item STAR_SLOT_EXTENDER = register(new Item(new FabricItemSettings().maxCount(1).fireproof()), "star_slot_extender");


    public static Item register(Item item, String id) {
        Identifier itemID = new Identifier(ZarjTest.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }
    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(TochkaItems.ZMOD_GROUP_KEY).register(content -> {
            content.add(STAR_OPENER );
            content.add(STAR_ROLLER);
            content.add(STAR_SLOT_EXTENDER);
        });
    }
}
