package zarj.ztest.UI;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zarj.ztest.ZarjTest;
import zarj.ztest.tochka.TochkaItems;
import zarj.ztest.tochka.TochkaLow;
import zarj.ztest.utils.ZLogger;

import java.util.Map;
import java.util.UUID;

public class TochkaScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private static final String MODIFIER_PREFIX = "zsharpen.lvl";
    private static final String MODIFIER_ARMOR_PREFIX = "zsharpen.armor.lvl";
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
        this.addSlot(new IconSlot(inventory, 1, 140,  25, "textures/gui/slot1.png", 20));

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

    public void handleSharpening(ServerPlayerEntity player) {

        ItemStack inputStack = this.getSlot(0).getStack(); // Слот для предмета
        ItemStack tochkaStack = this.getSlot(1).getStack(); // Слот для точки
        ZLogger.Text("ДОШЛО ДО ХЕНДЛЕРА!!");
        if(tochkaStack.isEmpty()){
            player.sendMessageToClient(Text.of("§cОшибка: Отсутствует заточка в слоте!"), false);
            ZLogger.Text("ТЕСТ: НЕТ ЗАТОЧКИ!!");
            return;
        }
        if(inputStack.isEmpty()){
            player.sendMessage(Text.of("§cОшибка: Отсутствует предмет в слоте!"), false);
            ZLogger.Text("ТЕСТ: НЕТ ПРЕДМЕТА!!");
            return;
        }
        if(!(tochkaStack.getItem() instanceof  TochkaLow)){
            player.sendMessage(Text.of("§cОшибка: В слоте точки не точка!!!"), false);
            ZLogger.Text("ТЕСТ: НЕ ТОТ ПРЕДМЕТ!!");
            return;
        }
        ZLogger.Text("ТЕСТ: ПРОВЕРКА ЗАТОЧКИ ПРОЙДЕНА!!");
        boolean haveDamage = inputStack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        boolean haveArmor = inputStack.getAttributeModifiers(EquipmentSlot.CHEST).containsKey(EntityAttributes.GENERIC_ARMOR);
        if(!haveDamage && !haveArmor){
            player.sendMessage(Text.of("§cОшибка: Этот предмет нельзя заточить!!!"), false);
            return;
        }
        TochkaLow tochkaItem = (TochkaLow) tochkaStack.getItem();
        ItemStack result = inputStack.copy();
        NbtCompound nbt = result.getOrCreateNbt();
        int level = nbt.getInt("ZSharpenLevel");

        if(!tochkaItem.isEnoughLevel(level)){
            player.sendMessage(Text.of("§cОшибка: Уровень предмета выше или равен уровню точки!!!"), false);
            return;
        }
        NbtList modifiersList = new NbtList();
        tochkaStack.decrement(1);
        if(!tochkaItem.willUpgrade(level)){
            player.sendMessage(Text.of("§cНеудача. Заточка не удалась!"), false);

            if (level > 0) {

                int newLevel = level - 1;
                nbt.putInt("ZSharpenLevel", newLevel);

                if(haveDamage){
                    NbtList damageHistory =  nbt.getList("ZDamageHistory", 6);
                    double decrimer = damageHistory.getDouble(level-1);
                    damageHistory.remove(level-1);
                    nbt.put("ZDamageHistory", damageHistory);
                    NbtList attrList = nbt.getList("AttributeModifiers",10);
                    for(NbtElement elem : attrList){
                        NbtCompound comp = (NbtCompound) elem;
                        String attrName = comp.getString("AttributeName");
                        String expected = Registries.ATTRIBUTE.getId(EntityAttributes.GENERIC_ATTACK_DAMAGE).toString();
                        if(attrName.equals(expected )){
                            double amount = comp.getDouble("Amount") - decrimer;
                            comp.putDouble("Amount", amount);
                            break;
                        }
                    }
                    nbt.put("AttributeModifiers", attrList);
                }

                result.setNbt(nbt);
                this.getSlot(0).setStack(result);
            }
            return;
        }
        int newLevel = level + 1;
        nbt.putInt("ZSharpenLevel", newLevel);


        if(haveDamage){
            double baseDamage = getTotalBaseDamage(inputStack);
            ZLogger.Text("ТЕСТ: final baseDamage = "+baseDamage);
            double bonus = baseDamage*0.2;
            if(level==0&&!nbt.contains("AttributeModifiers")){
                NbtList modifiers = new NbtList();
                for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry :
                        inputStack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).entries()) {

                    EntityAttribute attribute = entry.getKey();
                    EntityAttributeModifier modifier = entry.getValue();
                    NbtCompound mod = new NbtCompound();

                    // AttributeName: "minecraft:generic.attack_damage"
                    Identifier attrId = Registries.ATTRIBUTE.getId(attribute);
                    mod.putString("AttributeName", attrId.toString());

                    mod.putString("Name", modifier.getName());
                    mod.putUuid("UUID", modifier.getId());
                    mod.putDouble("Amount", modifier.getValue());

                    // Operation: 0=ADDITION, 1=MULTIPLY_BASE, 2=MULTIPLY_TOTAL
                    int opId;
                    if (modifier.getOperation() == EntityAttributeModifier.Operation.ADDITION) opId = 0;
                    else if (modifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE) opId = 1;
                    else opId = 2;
                    mod.putInt("Operation", opId);

                    // Slot: "mainhand", "chest" и т.д.
                    mod.putString("Slot", EquipmentSlot.MAINHAND.getName());

                    modifiers.add(mod);
                }
                nbt.put("AttributeModifiers", modifiers);
            }
            NbtList attrList = nbt.getList("AttributeModifiers",10);
            for(NbtElement elem : attrList){
                NbtCompound comp = (NbtCompound) elem;
                String attrName = comp.getString("AttributeName");
                String expected = Registries.ATTRIBUTE.getId(EntityAttributes.GENERIC_ATTACK_DAMAGE).toString();
                if(attrName.equals(expected )){
                    double amount = comp.getDouble("Amount") + bonus;
                    comp.putDouble("Amount", amount);
                    break;
                }
            }
            nbt.put("AttributeModifiers", attrList);
            NbtList damHist =nbt.getList("ZDamageHistory",6);
            damHist.add(NbtDouble.of(bonus));
            nbt.put("ZDamageHistory", damHist);

        }
        if (haveArmor) {
            double baseArmor = getTotalBaseArmor(inputStack);
            double bonus = baseArmor * 0.2 * newLevel;
            addModifier(nbt, buildModifierName(newLevel, false, true),
                    "generic.armor", bonus, "chest");
        }
        result.setNbt(nbt);
        this.getSlot(0).setStack(result);

    }
    private String buildModifierName(int level, boolean isDamage, boolean isArmor) {
        if (isDamage) return MODIFIER_PREFIX + level;
        if (isArmor) return MODIFIER_ARMOR_PREFIX + level;
        return MODIFIER_PREFIX + level;
    }

    private void changeModifier(NbtCompound nbt, String name, String attributeName,
                                double amount, String slot){
        nbt.putDouble("Amount",amount);
    }



    private void addModifier(NbtCompound nbt, String name, String attributeName,
                             double amount, String slot) {
        NbtList modifiers = nbt.getList("AttributeModifiers", NbtElement.COMPOUND_TYPE);
        if (modifiers == null) {
            modifiers = new NbtList();
        }

        NbtCompound modifier = new NbtCompound();
        modifier.putString("AttributeName", attributeName);
        modifier.putString("Name", name);
        // UUID на основе имени — всегда одинаковый для одного имени
        UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
        modifier.putUuid("UUID", uuid);
        modifier.putDouble("Amount", amount);
        modifier.putInt("Operation", 0); // Addition
        modifier.putString("Slot", slot);

        modifiers.add(modifier);
        nbt.put("AttributeModifiers", modifiers);
    }
    private void removeModifierByName(NbtCompound nbt, String nameToRemove) {
        if (!nbt.contains("AttributeModifiers", NbtElement.LIST_TYPE)) {
            return;
        }

        NbtList oldList = nbt.getList("AttributeModifiers", NbtElement.COMPOUND_TYPE);
        NbtList newList = new NbtList();
        boolean removed = false;

        for (int i = 0; i < oldList.size(); i++) {
            NbtCompound modifier = oldList.getCompound(i);
            String name = modifier.getString("Name");

            if (name.equals(nameToRemove)) {
                removed = true; // Пропускаем (удаляем) этот
                continue;
            }
            newList.add(modifier);
        }

        if (removed) {
            if (newList.isEmpty()) {
                nbt.remove("AttributeModifiers");
            } else {
                nbt.put("AttributeModifiers", newList);
            }
        }
    }
    private double getTotalBaseDamage(ItemStack stack) {
        double total = 1;
        var modifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        for (EntityAttributeModifier mod : modifiers) {
            total += mod.getValue();
        }
        return total;
    }

    private double getTotalBaseArmor(ItemStack stack) {
        double total = 0;
        var modifiers = stack.getAttributeModifiers(EquipmentSlot.CHEST)
                .get(EntityAttributes.GENERIC_ARMOR);
        for (EntityAttributeModifier mod : modifiers) {
            total += mod.getValue();
        }
        return total;
    }
}
