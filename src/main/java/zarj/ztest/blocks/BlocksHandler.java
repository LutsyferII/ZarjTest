package zarj.ztest.blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import zarj.ztest.ZarjTest;
import zarj.ztest.tochka.TochkaItems;

public class BlocksHandler {
    public static final Block STOL_BLOCK = new TochStolBlock(AbstractBlock.Settings.create()
            .hardness(3.0f)
            .requiresTool());

    public static final Item STOL_BLOCK_ITEM = new BlockItem(STOL_BLOCK, new Item.Settings());

    public static void initialize() {
        Registry.register(Registries.BLOCK, Identifier.of(ZarjTest.MOD_ID, "toch_stol"), STOL_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of(ZarjTest.MOD_ID, "toch_stol"), STOL_BLOCK_ITEM);
        ItemGroupEvents.modifyEntriesEvent(TochkaItems.ZMOD_GROUP_KEY).register(content -> {
            content.add(STOL_BLOCK );
        });
    }
}
