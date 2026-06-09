package zarj.ztest.client.UI;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zarj.zarjlib.UI.IconSlot;
import zarj.zarjlib.client.UI.ZDecorElement;
import zarj.zarjlib.client.UI.ZScreen;
import zarj.ztest.Net.ModPackets;
import zarj.ztest.UI.StarOpenScreenHandler;
import zarj.ztest.tochka.TochkaLow;
import zarj.ztest.utils.ZLogger;
import zarj.ztest.zvezdy.StarsItems;

public class ZStarOpenScreen  extends ZScreen<StarOpenScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("zarjtest", "textures/gui/star_opening_ui.png");
    private static final Identifier GREEN_SLOT = new Identifier("zarjtest", "textures/gui/slot4.png");
    private static final Identifier RED_SLOT = new Identifier("zarjtest", "textures/gui/slot3.png");



    public ZStarOpenScreen(StarOpenScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, TEXTURE);
        customHeight=80;
        }
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        ItemStack itemStack = this.handler.getSlot(1).getStack();
        ItemStack openerStack = this.handler.getSlot(0).getStack();
        /*if(this.handler.getSlot(0) instanceof IconSlot){
            IconSlot slot = (IconSlot) this.handler.getSlot(0);
            if(openerStack.isOf(StarsItems.STAR_ROLLER)){
                slot.changeTexture(GREEN_SLOT);
            }else{
                slot.changeTexture(RED_SLOT);
            }
        }*/
    }


    @Override
    protected void init() {
        super.init();
        // Центрируем кнопку относительно фона
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        this.decorElements.add(new ZDecorElement(new Identifier("zarjtest", "textures/gui/point_right.png"),100,40,24,16));

        // Добавляем кнопку
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Раскрыть"), button -> {
                    onButtonClick();
                })
                .dimensions(x +58, y + 56, 55, 16) // Позиция внутри UI и размер
                .build());
    }

    private void onButtonClick() {
        ZLogger.Text("КНОПКА В UI НАЖАТА!");
        ClientPlayNetworking.send(ModPackets.STAR_OPEN_START_PACKET_ID, PacketByteBufs.create());
    }
}
