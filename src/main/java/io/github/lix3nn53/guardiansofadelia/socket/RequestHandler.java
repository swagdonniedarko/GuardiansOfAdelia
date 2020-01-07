package io.github.lix3nn53.guardiansofadelia.socket;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.chat.PremiumRank;
import io.github.lix3nn53.guardiansofadelia.database.DatabaseQueries;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.socket.products.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RequestHandler {

    private final static HashMap<Integer, WebProduct> productIdToWebProduct = new HashMap<>();

    static {
        //list of ranks
        productIdToWebProduct.put(1, new WebProduct(WebProductType.RANK, 1, null, PremiumRank.HERO));
        productIdToWebProduct.put(2, new WebProduct(WebProductType.RANK, 1, null, PremiumRank.LEGEND));
        productIdToWebProduct.put(3, new WebProduct(WebProductType.RANK, 1, null, PremiumRank.TITAN));

        //List of items
        ItemStack itemSkinScroll1 = new ItemSkinScroll().getItemStack(1);
        ItemStack itemSkinScroll2 = new ItemSkinScroll().getItemStack(2);
        ItemStack itemSkinScroll5 = new ItemSkinScroll().getItemStack(5);
        productIdToWebProduct.put(4, new WebProduct(WebProductType.ITEM, 1, itemSkinScroll1));
        productIdToWebProduct.put(5, new WebProduct(WebProductType.ITEM, 1, itemSkinScroll2));
        productIdToWebProduct.put(6, new WebProduct(WebProductType.ITEM, 1, itemSkinScroll5));

        ItemStack helmetSkin1 = HelmetSkin.WINGS_ANGEL.getItemStack();
        ItemStack helmetSkin2 = HelmetSkin.WINGS_DEMON.getItemStack();
        ItemStack helmetSkin3 = HelmetSkin.WINGS_DRAGON_DARK.getItemStack();
        ItemStack helmetSkin4 = HelmetSkin.WINGS_DRAGON_WHITE.getItemStack();
        ItemStack helmetSkin5 = HelmetSkin.CROWN.getItemStack();
        productIdToWebProduct.put(7, new WebProduct(WebProductType.ITEM, 1, helmetSkin1));
        productIdToWebProduct.put(8, new WebProduct(WebProductType.ITEM, 1, helmetSkin2));
        productIdToWebProduct.put(9, new WebProduct(WebProductType.ITEM, 1, helmetSkin3));
        productIdToWebProduct.put(10, new WebProduct(WebProductType.ITEM, 1, helmetSkin4));
        productIdToWebProduct.put(11, new WebProduct(WebProductType.ITEM, 1, helmetSkin5));

        ItemStack petSkin1 = PetSkin.BEE.getItemStack();
        ItemStack petSkin2 = PetSkin.FOX_RED.getItemStack();
        ItemStack petSkin3 = PetSkin.FOX_SNOW.getItemStack();
        ItemStack petSkin4 = PetSkin.ICE_CREAM.getItemStack();
        ItemStack petSkin5 = PetSkin.VEX.getItemStack();
        ItemStack petSkin6 = PetSkin.MINI_DRAGON.getItemStack();

        productIdToWebProduct.put(12, new WebProduct(WebProductType.ITEM, 1, petSkin1));
        productIdToWebProduct.put(13, new WebProduct(WebProductType.ITEM, 1, petSkin2));
        productIdToWebProduct.put(14, new WebProduct(WebProductType.ITEM, 1, petSkin3));
        productIdToWebProduct.put(15, new WebProduct(WebProductType.ITEM, 1, petSkin4));
        productIdToWebProduct.put(16, new WebProduct(WebProductType.ITEM, 1, petSkin5));
        productIdToWebProduct.put(17, new WebProduct(WebProductType.ITEM, 1, petSkin6));

        ItemStack skinChest1 = new SkinChest().getItemStack(1);
        ItemStack skinChest2 = new SkinChest().getItemStack(2);
        ItemStack skinChest5 = new SkinChest().getItemStack(5);
        productIdToWebProduct.put(18, new WebProduct(WebProductType.ITEM, 1, skinChest1));
        productIdToWebProduct.put(19, new WebProduct(WebProductType.ITEM, 1, skinChest2));
        productIdToWebProduct.put(20, new WebProduct(WebProductType.ITEM, 1, skinChest5));

        ItemStack boost1 = BoostPremium.EXPERIENCE.getItemStack();
        ItemStack boost2 = BoostPremium.LOOT.getItemStack();
        ItemStack boost3 = BoostPremium.ENCHANT.getItemStack();
        ItemStack boost4 = BoostPremium.GATHER.getItemStack();
        productIdToWebProduct.put(21, new WebProduct(WebProductType.ITEM, 1, boost1));
        productIdToWebProduct.put(22, new WebProduct(WebProductType.ITEM, 1, boost2));
        productIdToWebProduct.put(23, new WebProduct(WebProductType.ITEM, 1, boost3));
        productIdToWebProduct.put(24, new WebProduct(WebProductType.ITEM, 1, boost4));

    }

    public static WebResponse onPurchase(WebPurchase webPurchase) {
        String minecraftUsername = webPurchase.getMinecraftUsername();
        int productId = webPurchase.getProductId();
        int payment = webPurchase.getPayment();

        if (!productIdToWebProduct.containsKey(productId)) {
            return new WebResponse(false, "No such product1", minecraftUsername, productId);
        }

        WebProduct webProduct = productIdToWebProduct.get(productId);
        int cost = webProduct.getCost();

        if (cost != payment) {
            return new WebResponse(false, "No such product2", minecraftUsername, productId);
        }

        WebProductType type = webProduct.getType();
        if (type.equals(WebProductType.ITEM)) {
            ItemStack itemStack = webProduct.getItemStack();

            Player playerExact = Bukkit.getPlayerExact(minecraftUsername);
            if (playerExact != null) {
                InventoryView openInventory = playerExact.getOpenInventory();
                String title = openInventory.getTitle();

                if (title.contains("Premium Storage")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            playerExact.closeInventory();
                        }
                    }.runTask(GuardiansOfAdelia.getInstance());
                }

                UUID uuid = playerExact.getUniqueId();
                if (GuardianDataManager.hasGuardianData(uuid)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(uuid);
                    boolean success = guardianData.addToPremiumStorage(itemStack);
                    if (!success) {
                        return new WebResponse(false, "Your premium-storage is full!", minecraftUsername, productId);
                    }
                }
            } else { //player is offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(minecraftUsername);
                UUID uuid = offlinePlayer.getUniqueId();

                if (!uuidExists(uuid)) {
                    return new WebResponse(false, "You must be logged in to game server at least once!", minecraftUsername, productId);
                }

                try {
                    ItemStack[] premiumStorage = DatabaseQueries.getPremiumStorage(uuid);

                    List<ItemStack> list = new ArrayList<>();

                    if (premiumStorage != null) list = new ArrayList<>(Arrays.asList(premiumStorage));

                    if (list.size() >= 54) {
                        return new WebResponse(false, "Your premium-storage is full!", minecraftUsername, productId);
                    }

                    list.add(itemStack);
                    ItemStack[] newPremiumStorage = list.toArray(new ItemStack[0]);
                    DatabaseQueries.setPremiumStorage(uuid, newPremiumStorage);
                } catch (Exception e) {
                    e.printStackTrace();

                    return new WebResponse(false, "A database error occurred.", minecraftUsername, productId);
                }
            }

            GuardiansOfAdelia.getInstance().getLogger().info("Web purchase: " + minecraftUsername + " bought " + itemStack.getItemMeta().getDisplayName() + " for " + payment + " credits!");
        } else if (type.equals(WebProductType.RANK)) {
            PremiumRank premiumRank = webProduct.getPremiumRank();

            Player playerExact = Bukkit.getPlayerExact(minecraftUsername);
            if (playerExact != null) {
                UUID uuid = playerExact.getUniqueId();
                if (GuardianDataManager.hasGuardianData(uuid)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(uuid);
                    guardianData.setPremiumRank(premiumRank);
                }
            } else { //player is offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(minecraftUsername);
                UUID uuid = offlinePlayer.getUniqueId();

                if (!uuidExists(uuid)) {
                    return new WebResponse(false, "You must be logged in to game server at least once!", minecraftUsername, productId);
                }

                try {
                    DatabaseQueries.setPremiumRank(uuid, premiumRank);
                } catch (Exception e) {
                    e.printStackTrace();

                    return new WebResponse(false, "A database error occurred.", minecraftUsername, productId);
                }
            }
        }

        return new WebResponse(true, "Item purchased successfully!", minecraftUsername, productId);
    }

    private static boolean uuidExists(UUID uuid) {
        return DatabaseQueries.uuidExists(uuid);
    }
}
