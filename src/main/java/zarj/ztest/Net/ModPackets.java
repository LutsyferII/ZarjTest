package zarj.ztest.Net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import zarj.ztest.UI.TochkaScreenHandler;
import zarj.ztest.utils.ZLogger;

public class ModPackets {
    public static final Identifier SHARPEN_START_PACKET_ID = new Identifier("zarjtest", "sharpen_start");

    public static void registerC2SPackets() {
        // Регистрация на СЕРВЕРЕ (он слушает клиента)
        ServerPlayNetworking.registerGlobalReceiver(SHARPEN_START_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // ПРОВЕРКА: открыт ли у игрока нужный экран?
                ZLogger.Text("ЭТАП ПРОВЕРКИ ИГРОКА В ModPackets!");
                if (player.currentScreenHandler instanceof TochkaScreenHandler screenHandler) {
                    // ТУТ МАНИПУЛЯЦИЯ ПРЕДМЕТОМ
                    screenHandler.handleSharpening(player);
                }
            });
        });
    }
}
