package zarj.ztest.UI;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import zarj.ztest.ZarjTest;

public class TochkaScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public TochkaScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9));
    }
    public TochkaScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ZarjTest.TOCHKA_SCREEN_HANDLER, syncId);
        checkSize(inventory, 9);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        int m;
        int l;
        this.addSlot(new IconSlot(inventory, 0, 20,  25, "textures/gui/slot1.png", 20));
        //this.addSlot(new IconSlot(inventory, 0, 20,  25, Identifier.of("zarjtest", "textures/gui/slot1.png") ));
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        return ItemStack.EMPTY;
    }
    @Override
    public void onClosed(PlayerEntity player){
        super.onClosed(player);
        this.dropInventory(player, this.inventory);
    }
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
