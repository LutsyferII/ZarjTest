package zarj.ztest.UI;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import zarj.zarjlib.UI.ZScreenHandler;
import zarj.zarjlib.UI.IconSlot;
import zarj.ztest.ZarjTest;
import zarj.ztest.utils.ZLogger;
import zarj.ztest.zvezdy.StarsItems;

public class StarOpenScreenHandler extends ZScreenHandler {
    private static final Identifier GREEN_SLOT = new Identifier("zarjtest", "textures/gui/slot4.png");
    private static final Identifier RED_SLOT = new Identifier("zarjtest", "textures/gui/slot3.png");


    public StarOpenScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(syncId, playerInventory, 2, ZarjTest.STAR_OPEN_SCREEN_HANDLER);
    }
    @Override
    public void addingSlots(){
        this.addSlot(new IconSlot(inventory, 0, 30,  32, "textures/gui/slot3.png", 20){
            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                if(stack.isOf(StarsItems.STAR_ROLLER)){
                    this.changeTexture(GREEN_SLOT);
                }else{
                    this.changeTexture(RED_SLOT);
                }
            }
        });
        this.addSlot(new IconSlot(inventory, 1, 130,  32, "textures/gui/slot3.png", 20){
            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                if(stack.isOf(StarsItems.STAR_ROLLER)){
                    this.changeTexture(GREEN_SLOT);
                }else{
                    this.changeTexture(RED_SLOT);
                }
            }
        });
    }


    public void openStar(){

    }


}
