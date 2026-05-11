package zarj.ztest.tochka;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zarj.ztest.ZarjTest;

import java.util.ArrayList;
import java.util.List;

public class TochkaItems {

    public static final Item TOCHKA_LOW= register(new TochkaLow(new FabricItemSettings(), 5,80,5,10), "tochka_low");
    public static final Item TOCHKA_2= register(new TochkaLow(new FabricItemSettings(), 10,50,3,5), "toch_2");
    public static final Item TOCHKA_3= register(new TochkaLow(new FabricItemSettings(), 15,30,2,5), "toch_3");
    public static final Item TOCHKA_4= register(new TochkaLow(new FabricItemSettings(), 20,10,1,4), "toch_4");
    public static final RegistryKey<ItemGroup> ZMOD_GROUP_KEY = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            new Identifier(ZarjTest.MOD_ID, "item_group")
    );
  public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(TochkaItems.TOCHKA_LOW))
            .displayName(Text.translatable("itemGroup.ztest"))
            .entries((context, entries) -> {
                entries.add(TochkaItems.TOCHKA_LOW);
                entries.add(TochkaItems.TOCHKA_2);
                entries.add(TochkaItems.TOCHKA_3);
                entries.add(TochkaItems.TOCHKA_4);
            })
            .build();

    public static Item register(Item item, String id) {
        Identifier itemID = new Identifier(ZarjTest.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ZMOD_GROUP_KEY,CUSTOM_ITEM_GROUP);
    }

}
