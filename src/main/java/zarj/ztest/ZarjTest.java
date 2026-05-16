package zarj.ztest;

import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zarj.zarjlib.drops.DropChanges;
import zarj.zarjlib.drops.StaticDropChanges;
import zarj.ztest.Net.ModPackets;
import zarj.ztest.UI.TochkaScreenHandler;
import zarj.ztest.blocks.BlocksHandler;
import zarj.ztest.runes.RuneItems;
import zarj.ztest.tochka.TochkaItems;

import java.util.HashMap;
import java.util.Map;

public class ZarjTest implements ModInitializer {
	public static final String MOD_ID = "zarjtest";
    public static final Logger LOGGER = LoggerFactory.getLogger("zarjtest");
    private static final Map<Item, Double> TOCHKA_CHANCES = new HashMap<>();
    static {
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_LOW, 0.15);
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_2, 0.12);
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_3, 0.09);
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_4, 0.06);
    }
    public static DropChanges ALL_DROPS = new DropChanges();

    public static final ScreenHandlerType<TochkaScreenHandler> TOCHKA_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier("zarjtest", "sharpening"),
                    new ScreenHandlerType<>(TochkaScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    @Override
	public void onInitialize() {

        TochkaItems.initialize();
        RuneItems.initialize();
        BlocksHandler.initialize();
        ModPackets.registerC2SPackets();
        StaticDropChanges.changeDrop(TOCHKA_CHANCES);

        ALL_DROPS.ensureAllRegistered();

	}


}