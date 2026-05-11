package zarj.ztest.tochka;


import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import zarj.ztest.UI.TochkaScreenHandler;

import java.util.List;
import java.util.Random;

public class TochkaLow  extends Item {
    protected final int maxLevel;
    protected final double baseChance;
    protected final double decr;
    protected int upgrader;
    public Random rand = new Random();

    public TochkaLow(Settings properties, int maxLevel, double baseChance, double decr, int upgrader) {
        super(properties);
        this.maxLevel = maxLevel;
        this.baseChance = baseChance;
        this.decr = decr;
        this.upgrader = upgrader;
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
    public boolean isEnoughLevel(int lvl){
        return lvl<maxLevel;
    }
    public int getMaxLevel(){
        return maxLevel;
    }

    // :::2
    // :::3
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.getTranslationKey()).formatted(Formatting.GRAY));
    }
    public boolean willUpgrade(int level) {
        double new_chance=baseChance-(decr*level);
        double fill =  rand.nextDouble(0.0, 100.0);
        return fill<=new_chance;
    }
    public boolean willUpgradeWithBuff(int level, double buff) {
        double new_chance=baseChance-(decr*level)+buff;
        double fill =  rand.nextDouble(0.0, 100.0);
        return fill<=new_chance;
    }
    public double realChance(int level, double buff){
        return baseChance-(decr*level)+buff;
    }
    public int getUpgrader(){
        return upgrader;
    }

}
