package io.github.lix3nn53.guardiansofadelia.socket;

import io.github.lix3nn53.guardiansofadelia.chat.PremiumRank;
import org.bukkit.inventory.ItemStack;

public class WebProduct {

    private final WebProductType type;
    private final int cost;
    private final ItemStack itemStack;
    private final PremiumRank premiumRank;

    public WebProduct(WebProductType type, int cost, ItemStack itemStack) {
        this.type = type;
        this.cost = cost;
        this.itemStack = itemStack;
        this.premiumRank = PremiumRank.NONE;
    }

    public WebProduct(WebProductType type, int cost, ItemStack itemStack, PremiumRank premiumRank) {
        this.type = type;
        this.cost = cost;
        this.itemStack = itemStack;
        this.premiumRank = premiumRank;
    }

    public WebProductType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public PremiumRank getPremiumRank() {
        return premiumRank;
    }
}
