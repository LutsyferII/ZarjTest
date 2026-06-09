package zarj.ztest.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import zarj.ztest.ZarjTest;
import zarj.ztest.client.UI.ZSharpeningScreen;
import zarj.ztest.client.UI.ZStarOpenScreen;

public class ZarjTestClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        HandledScreens.register(ZarjTest.TOCHKA_SCREEN_HANDLER, ZSharpeningScreen::new);
        HandledScreens.register(ZarjTest.STAR_OPEN_SCREEN_HANDLER, ZStarOpenScreen::new);
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}