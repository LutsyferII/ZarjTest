package zarj.ztest.UI;

import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import zarj.ztest.ZarjTest;

public class IconSlot extends Slot {
    public final Identifier iconTexture;
    public int size = 16;
    public  boolean isOverIcon = false;
    public IconSlot(Inventory inventory, int index, int x, int y, Identifier iconTexture) {
        super(inventory, index, x, y);
        this.iconTexture = iconTexture;

    }
    public IconSlot(Inventory inventory, int index, int x, int y, Identifier iconTexture, int size) {
        super(inventory, index, x, y);
        this.size = size;
        this.iconTexture = iconTexture;

    }
    public IconSlot(Inventory inventory, int index, int x, int y, String path) {
        super(inventory, index, x, y);

        this.iconTexture = Identifier.of("zarjtest", path);
    }
    public IconSlot(Inventory inventory, int index, int x, int y, String path, int size) {
        super(inventory, index, x, y);
        this.size = size;
        this.iconTexture = Identifier.of("zarjtest", path);
    }

    public IconSlot(Inventory inventory, int index, int x, int y, String path, int size, boolean OverIcon) {
        super(inventory, index, x, y);
        this.size = size;
        this.iconTexture = Identifier.of("zarjtest", path);
        isOverIcon = OverIcon;
    }
    // Этот метод игра вызывает, когда слот пуст, чтобы понять, что рисовать

    @Override
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        if(!isOverIcon){
            return super.getBackgroundSprite();
        }
        if(iconTexture.getPath().contains("textures")){
            String rawPath = iconTexture.getPath().replace("textures/", "").replace(".png", "");
            return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("zarjtest", rawPath));
        }else{
            return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, iconTexture);
        }
    }
}
