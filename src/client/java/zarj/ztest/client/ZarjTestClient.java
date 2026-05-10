package zarj.ztest.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import zarj.ztest.ZarjTest;
import zarj.ztest.client.UI.ZSharpeningScreen;

public class ZarjTestClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        HandledScreens.register(ZarjTest.TOCHKA_SCREEN_HANDLER, ZSharpeningScreen::new);
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}