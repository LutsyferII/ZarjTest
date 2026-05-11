package zarj.ztest.runes;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TochRune extends Item {
    private final boolean isSaveLevel;
    private final double chanceBoost;

    public TochRune(Settings properties, boolean willSaveLevel, double chanceBaff){
        super(properties);
        isSaveLevel = willSaveLevel;
        chanceBoost = chanceBaff;
    }




    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.getTranslationKey()).formatted(Formatting.GRAY));
    }
    public boolean isSaveLevel(){
        return isSaveLevel;
    }
    public double getChanceBoost(){
        return chanceBoost;
    }




}
