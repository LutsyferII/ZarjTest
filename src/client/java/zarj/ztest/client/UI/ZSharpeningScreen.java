package zarj.ztest.client.UI;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zarj.ztest.Net.ModPackets;
import zarj.ztest.UI.IconSlot;
import zarj.ztest.UI.TochkaScreenHandler;
import zarj.ztest.utils.ZLogger;

import java.util.logging.Logger;

public class ZSharpeningScreen extends HandledScreen<TochkaScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("zarjtest", "textures/gui/first_ui.png");

    public ZSharpeningScreen(TochkaScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 176,166);
        for (Slot slot : this.handler.slots) {
            if (slot instanceof IconSlot iconSlot) {
                int offset = (iconSlot.size - 16) / 2;
                context.drawTexture(iconSlot.iconTexture, x + slot.x - offset, y + slot.y- offset, 0, 0, iconSlot.size, iconSlot.size, iconSlot.size, iconSlot.size);
            }
        }
        //context.drawTexture(PERMANENT_ICON, x + 18, y + 23, 0, 0, 20, 20, 20, 20);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context); // Затемнение заднего плана
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY); // Тултипы предметов
    }
    @Override
    protected void init() {
        super.init();
        // Центрируем кнопку относительно фона
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        ZLogger.Text("ИНИЦИАЛИЗАЦИЯ!");
        // Добавляем кнопку
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Заточить"), button -> {
                    onButtonClick();
                })
                .dimensions(x +55, y + 45, 60, 20) // Позиция внутри UI и размер
                .build());
    }

    private void onButtonClick() {
        ZLogger.Text("КНОПКА В UI НАЖАТА!");
        ClientPlayNetworking.send(ModPackets.SHARPEN_START_PACKET_ID, PacketByteBufs.create());
    }
}
