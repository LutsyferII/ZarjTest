package zarj.ztest.tochka;


import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import zarj.ztest.UI.TochkaScreenHandler;

import java.util.List;

public class TochkaLow  extends Item {
    protected final int maxLevel;
    protected final int baseChance;

    public TochkaLow(Settings properties, int maxLevel, int baseChance) {
        super(properties);
        this.maxLevel = maxLevel;
        this.baseChance = baseChance;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.openHandledScreen(createScreenHandlerFactory(user, hand));
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    private NamedScreenHandlerFactory createScreenHandlerFactory(PlayerEntity player, Hand hand) {
        return new SimpleNamedScreenHandlerFactory((syncId, inv, p) -> {
            return new TochkaScreenHandler(syncId, inv);
        }, Text.literal("Заточка оружия"));
    }
    // :::2
    // :::3
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("itemTooltip.zmod.tochka-low"));
    }

}
