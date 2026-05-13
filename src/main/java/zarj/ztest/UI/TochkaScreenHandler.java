package zarj.ztest.UI;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zarj.ztest.runes.TochRune;
import zarj.ztest.ZarjTest;
import zarj.ztest.tochka.TochkaLow;
import zarj.ztest.utils.ZLogger;

import java.util.Map;

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
        this.addSlot(new IconSlot(inventory, 0, 20,  25, "textures/gui/slot1.png", 20, true));
        this.addSlot(new IconSlot(inventory, 1, 140,  25, "textures/gui/slot2.png", 20));

        this.addSlot(new IconSlot(inventory, 2, 80,  25, "textures/gui/slot2.png", 20));
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
        ItemStack runeStack = this.getSlot(2).getStack(); // Слот для рун


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
        EquipmentSlot slot;
        if (inputStack.getItem() instanceof ArmorItem armorItem) {
            slot = armorItem.getSlotType(); // HEAD, CHEST, LEGS, FEET
        } else {
            slot = EquipmentSlot.MAINHAND; // или OFFHAND для инструментов
        }
        boolean haveDamage = inputStack.getAttributeModifiers(slot).containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        boolean haveArmor = inputStack.getAttributeModifiers(slot).containsKey(EntityAttributes.GENERIC_ARMOR);
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
        TochRune activeRune = null;
        boolean hasRune = false;
        double runeBuff = 0;


        if(runeStack.isEmpty()){
            player.sendMessage(Text.of("§cПРЕДУПРЕЖДЕНИЕ: В слоте руны ничего нет!"), false);
        }else if(!(runeStack.getItem() instanceof TochRune)){
            player.sendMessage(Text.of("§cПРЕДУПРЕЖДЕНИЕ: В слоте руны не руна!!!"), false);
        }else{
            hasRune = true;
            activeRune = (TochRune) runeStack.getItem();
            runeBuff=activeRune.getChanceBoost();
            runeStack.decrement(1);
        }
        tochkaStack.decrement(1);
        if(!tochkaItem.willUpgradeWithBuff(level,runeBuff)){
            player.sendMessage(Text.of("§cНеудача. Заточка не удалась!"), false);
            if(!hasRune){
                result.decrement(1);
                this.getSlot(0).setStack(result);
                player.sendMessage(Text.of("§cРУНА ОТСУТСТВУЕТ! ПРЕДМЕТ СЛОМАН!"), false);
            }else if (level > 0 && !(activeRune.isSaveLevel())) {
                int newLevel = level - 1;
                nbt.putInt("ZSharpenLevel", newLevel);
                if(haveDamage){

                    downgradeAttribute(nbt,level,EntityAttributes.GENERIC_ATTACK_DAMAGE,"Damage");
                }
                if(haveArmor){

                    downgradeAttribute(nbt,level,EntityAttributes.GENERIC_ARMOR,"Armor");
                }
                result.setCustomName(Text.of(changeTochkaName(result.getName().getString(),newLevel)));
                this.getSlot(0).setStack(result);
            }else{
                return;
            }
            return;
        }
        int newLevel = level + 1;
        nbt.putInt("ZSharpenLevel", newLevel);

        if(level==0&&!nbt.contains("AttributeModifiers")){
            NbtList modifiers = new NbtList();
            for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : inputStack.getItem().getAttributeModifiers(slot).entries()) {

                EntityAttribute attribute = entry.getKey();
                EntityAttributeModifier modifier = entry.getValue();
                NbtCompound mod = AttributeToNBT(attribute,modifier,slot);
                modifiers.add(mod);
            }
            nbt.put("AttributeModifiers", modifiers);
        }

        if(haveDamage){
            double baseDamage = getTotalBaseStat(inputStack,slot,EntityAttributes.GENERIC_ATTACK_DAMAGE);
            upgradeAttribute(nbt,tochkaItem,baseDamage,EntityAttributes.GENERIC_ATTACK_DAMAGE,"Damage");
        }
        if (haveArmor) {
            double baseArmor = getTotalBaseStat(inputStack, slot,EntityAttributes.GENERIC_ARMOR);
            upgradeAttribute(nbt,tochkaItem,baseArmor,EntityAttributes.GENERIC_ARMOR,"Armor");
        }
        result.setCustomName(Text.of(changeTochkaName(result.getName().getString(),newLevel)));
        this.getSlot(0).setStack(result);

    }
    public String changeTochkaName(String originalName, int level){
        String regex = "\\s*\\+\\d+$";
        if(originalName.matches(".*" +regex)){
            if(level!=0){
                return originalName.replaceAll(regex, " +" + level);
            }else{
                return originalName.replaceAll(regex, "");
            }
        }
        if (level <= 0) return originalName;
        return originalName+" +"+level;
    }

    public void upgradeAttribute(NbtCompound nbt, TochkaLow tochkaItem, double baseStat, EntityAttribute entityAttribute, String historyName){
        double bonus = baseStat/100*tochkaItem.getUpgrader();

        NbtList attrList = nbt.getList("AttributeModifiers",10);
        for(NbtElement elem : attrList){
            NbtCompound comp = (NbtCompound) elem;
            String attrName = comp.getString("AttributeName");
            String expected = Registries.ATTRIBUTE.getId(entityAttribute).toString();
            if(attrName.equals(expected )){
                double amount = comp.getDouble("Amount") + bonus;
                comp.putDouble("Amount", amount);
                break;
            }
        }
        nbt.put("AttributeModifiers", attrList);
        NbtList damHist =nbt.getList("Z"+historyName+"History",6);
        if (damHist == null) {
            damHist = new NbtList();
        }
        damHist.add(NbtDouble.of(bonus));
        nbt.put("Z"+historyName+"History", damHist);
    }
    public void downgradeAttribute(NbtCompound nbt, int level, EntityAttribute entityAttribute, String historyName){
        NbtList damageHistory =  nbt.getList("Z"+historyName+"History", 6);
        double decrimer = 0.0;
        if(damageHistory!=null && level - 1 < damageHistory.size()) {
            decrimer = damageHistory.getDouble(level - 1);
            damageHistory.remove(level - 1);
            nbt.put("Z" + historyName + "History", damageHistory);
        }
        NbtList attrList = nbt.getList("AttributeModifiers",10);
        if (attrList == null) {
            return;
        }
        for(NbtElement elem : attrList){
            NbtCompound comp = (NbtCompound) elem;
            String attrName = comp.getString("AttributeName");
            String expected = Registries.ATTRIBUTE.getId(entityAttribute).toString();
            if(attrName.equals(expected )){
                double amount = comp.getDouble("Amount") - decrimer;
                comp.putDouble("Amount", amount);
                break;
            }
        }
        nbt.put("AttributeModifiers", attrList);
    }
    public NbtCompound AttributeToNBT(EntityAttribute attribute, EntityAttributeModifier modifier, EquipmentSlot slot){
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
        mod.putString("Slot", slot.getName());
        return mod;
    }


    private double getTotalBaseStat(ItemStack stack, EquipmentSlot slot, EntityAttribute attr) {
        double total = attr == EntityAttributes.GENERIC_ATTACK_DAMAGE ? 1 : 0;
        var modifiers = stack.getAttributeModifiers(slot).get(attr);
        for (EntityAttributeModifier mod : modifiers) {
            total += mod.getValue();
        }
        return total;
    }
}
