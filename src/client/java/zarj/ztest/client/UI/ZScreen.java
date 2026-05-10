package zarj.ztest.client.UI;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.awt.*;

public class ZScreen extends Screen {

    public Screen parent;
    protected ZScreen() {
        super(Text.literal("Заточка"));

    }
    protected ZScreen(Screen parent) {
        super(Text.literal("Заточка"));
        this.parent = parent;

    }

}
