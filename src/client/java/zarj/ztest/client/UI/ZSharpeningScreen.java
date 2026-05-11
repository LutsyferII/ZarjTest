package zarj.ztest.client.UI;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import zarj.ztest.Net.ModPackets;
import zarj.ztest.UI.IconSlot;
import zarj.ztest.UI.TochkaScreenHandler;
import zarj.ztest.runes.TochRune;
import zarj.ztest.tochka.TochkaLow;
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
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

        ItemStack itemStack = this.handler.getSlot(0).getStack();
        ItemStack tochStack = this.handler.getSlot(1).getStack();
        ItemStack runeStack = this.handler.getSlot(2).getStack();
        if(runeStack.isEmpty() || !(runeStack.getItem() instanceof TochRune)){
            context.drawText(this.textRenderer,Text.of("БЕЗ РУНЫ ПРЕДМЕТ МОЖЕТ СЛОМАТЬСЯ!!!"),-5,6, 0xFF5555,false);
        }
        if(!itemStack.isEmpty()&&!tochStack.isEmpty()&& (tochStack.getItem() instanceof TochkaLow)){

            TochkaLow tochka = (TochkaLow) tochStack.getItem();
            NbtCompound nbt = itemStack.getOrCreateNbt();
            int level = nbt.getInt("ZSharpenLevel");

            double runeBuff = 0;
            if(runeStack.getItem() instanceof TochRune){
                runeBuff = ((TochRune) runeStack.getItem()).getChanceBoost();
            }
            double chance = tochka.realChance(level, runeBuff);
            context.drawText(this.textRenderer,Text.of("Шанс заточки: "+chance),14,50, 0x55FF55,false);
        }

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
                .dimensions(x +58, y + 63, 55, 16) // Позиция внутри UI и размер
                .build());
    }

    private void onButtonClick() {
        ZLogger.Text("КНОПКА В UI НАЖАТА!");
        ClientPlayNetworking.send(ModPackets.SHARPEN_START_PACKET_ID, PacketByteBufs.create());
    }
}
