package zarj.ztest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zarj.ztest.UI.TochkaScreenHandler;

public class TochStolBlock extends Block {
    public TochStolBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            // Серверная логика здесь
            // Открытие GUI, изменение стейта, выдача предмета и т.д.
            player.openHandledScreen(createScreenHandlerFactory());
            //player.sendMessage(Text.of("ПКМ по блоку!"), false);
        }
        return ActionResult.SUCCESS; // Чтобы анимация руки проиграла
    }
    private NamedScreenHandlerFactory createScreenHandlerFactory() {
        return new SimpleNamedScreenHandlerFactory((syncId, inv, p) -> {
            return new TochkaScreenHandler(syncId, inv);
        }, Text.literal("Заточка оружия"));
    }
}
