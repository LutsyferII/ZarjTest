package zarj.ztest;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zarj.ztest.Net.ModPackets;
import zarj.ztest.UI.TochkaScreenHandler;
import zarj.ztest.blocks.BlocksHandler;
import zarj.ztest.runes.RuneItems;
import zarj.ztest.tochka.TochkaItems;
import zarj.ztest.tochka.TochkaLow;

import java.util.HashMap;
import java.util.Map;

public class ZarjTest implements ModInitializer {
	public static final String MOD_ID = "zarjtest";
    public static final Logger LOGGER = LoggerFactory.getLogger("zarjtest");
 private static final Map<Item, Double> TOCHKA_CHANCES = new HashMap<>();
    static {
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_LOW, 0.15);
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_2, 0.12);
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_3, 0.09);
        TOCHKA_CHANCES.put(TochkaItems.TOCHKA_4, 0.06);
    }

    public static final ScreenHandlerType<TochkaScreenHandler> TOCHKA_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier("zarjtest", "sharpening"),
                    new ScreenHandlerType<>(TochkaScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    @Override
	public void onInitialize() {

        TochkaItems.initialize();
        RuneItems.initialize();
        BlocksHandler.initialize();
        ModPackets.registerC2SPackets();
        changeDrop();


	}


    private void changeDrop(){
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity.getWorld().isClient) return; // Только сервер
            if (entity instanceof PlayerEntity) return;

            // Характеристики моба
            double maxHealth = entity.getMaxHealth();
            double attackDamage = entity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            // double armor = entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR);

            // Твоя формула шанса
            double baseChance = (maxHealth * 0.05 + attackDamage * 0.2) / 100.0;

            // Убийца — игрок?
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                baseChance += player.getLuck() * 0.01;
            }
            for(Map.Entry<Item, Double> entry : TOCHKA_CHANCES.entrySet()){
                if (entity.getWorld().random.nextDouble() < baseChance+entry.getValue()) {
                    ItemStack dropStack = new ItemStack(entry.getKey()); // или что там дропается
                    ItemEntity itemEntity = new ItemEntity(
                            entity.getWorld(),
                            entity.getX(), entity.getY() + 0.5, entity.getZ(),
                            dropStack
                    );
                    itemEntity.setPickupDelay(10); // Небольшая задержка на подбор
                    entity.getWorld().spawnEntity(itemEntity);
                }
            }
        });
    }
}