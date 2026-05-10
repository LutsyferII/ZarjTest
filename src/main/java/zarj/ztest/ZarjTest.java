package zarj.ztest;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zarj.ztest.Net.ModPackets;
import zarj.ztest.UI.TochkaScreenHandler;
import zarj.ztest.tochka.TochkaItems;

public class ZarjTest implements ModInitializer {
	public static final String MOD_ID = "zarjtest";
    public static final Logger LOGGER = LoggerFactory.getLogger("zarjtest");
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
 public static final ScreenHandlerType<TochkaScreenHandler> TOCHKA_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier("zarjtest", "sharpening"),
                    new ScreenHandlerType<>(TochkaScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    @Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        TochkaItems.initialize();
        ModPackets.registerC2SPackets();
		LOGGER.info("Hello Fabric world!");
	}
}