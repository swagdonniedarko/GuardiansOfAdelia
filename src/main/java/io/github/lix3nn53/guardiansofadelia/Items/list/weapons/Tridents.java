package io.github.lix3nn53.guardiansofadelia.Items.list.weapons;

import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.AttackSpeed;
import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.ItemTier;
import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.WeaponRanged;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

class Tridents {

    public static ItemStack get(int placementNumber, ItemTier tier, String itemTag, double bonusPercent, int minStatValue,
                                int maxStatValue, int minNumberofStats) {
        String name = "Wooden Spear";
        Material material = Material.TRIDENT;
        int customModelDataId = 1;
        int level = 1;
        RPGClass rpgClass = RPGClass.MONK;
        int meleeDamage = 12;
        int rangedDamage = 10; //do not add bonusPercent to rangedDamage because WeaponRanged constructor already does that

        AttackSpeed attackSpeed = AttackSpeed.NORMAL;

        if (placementNumber == 2) {
            name = "Steel Spear";
            customModelDataId = 2;
            level = 10;
            meleeDamage = 20;
            rangedDamage = 16;
        } else if (placementNumber == 3) {
            name = "Crimson Spear";
            customModelDataId = 3;
            level = 20;
            meleeDamage = 50;
            rangedDamage = 40;
        } else if (placementNumber == 4) {
            name = "Spear of Teva";
            customModelDataId = 4;
            level = 30;
            meleeDamage = 120;
            rangedDamage = 100;
        } else if (placementNumber == 5) {
            name = "Seashell Spear";
            customModelDataId = 5;
            level = 40;
            meleeDamage = 200;
            rangedDamage = 160;
        } else if (placementNumber == 6) {
            name = "Midian Spear";
            customModelDataId = 6;
            level = 50;
            meleeDamage = 310;
            rangedDamage = 255;
        } else if (placementNumber == 7) {
            name = "RedEye SpearRENAME";
            customModelDataId = 8;
            level = 60;
            meleeDamage = 430;
            rangedDamage = 360;
        } else if (placementNumber == 8) {
            name = "Nebula Spear";
            customModelDataId = 10;
            level = 70;
            meleeDamage = 570;
            rangedDamage = 480;
        } else if (placementNumber == 9) {
            name = "Royal Spear";
            customModelDataId = 12;
            level = 80;
            meleeDamage = 720;
            rangedDamage = 600;
        } else if (placementNumber == 10) {
            name = "Ocean Spirit Spear";
            customModelDataId = 14;
            level = 90;
            meleeDamage = 960;
            rangedDamage = 800;
        }

        meleeDamage = (int) ((meleeDamage * bonusPercent) + 0.5); //add bonusPercent to meleeDamage because WeaponRanged constructor adds only to rangedDamage

        final WeaponRanged weapon = new WeaponRanged(name, tier, itemTag, material, customModelDataId, level, rpgClass, meleeDamage, rangedDamage, bonusPercent,
                attackSpeed, minStatValue, maxStatValue, minNumberofStats);
        ItemStack itemStack = weapon.getItemStack();
        itemStack.addEnchantment(Enchantment.LOYALTY, 3);
        return itemStack;
    }
}
