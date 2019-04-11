package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.economy.bazaar.Bazaar;
import io.github.lix3nn53.guardiansofadelia.economy.bazaar.BazaarManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.DungeonTheme;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

import java.util.List;
import java.util.UUID;

public class MyPlayerAnimationEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(PlayerAnimationEvent event) {
        if (!event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
            return; // off hand packet, ignore.
        }

        Player player = event.getPlayer();

        if (player.getLocation().getWorld().getName().equals("world")) {
            Block targetBlock = player.getTargetBlock(null, 5);

            if (targetBlock.getType().equals(Material.EMERALD_BLOCK) || targetBlock.getType().equals(Material.DIAMOND_BLOCK) || targetBlock.getType().equals(Material.GOLD_BLOCK)) {
                DungeonTheme theme = MiniGameManager.getDungeonFromGate(targetBlock.getLocation());
                if (theme != null) {
                    theme.getJoinQueueGui().openInventory(player);
                }
            } else {
                List<Entity> nearbyEntities = player.getNearbyEntities(1, 1, 1);
                for (Entity entity : nearbyEntities) {
                    if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                        if (BazaarManager.isBazaar(entity)) {
                            Player owner = BazaarManager.getOwner(entity);
                            UUID uuid = owner.getUniqueId();
                            if (GuardianDataManager.hasGuardianData(uuid)) {
                                GuardianData guardianData = GuardianDataManager.getGuardianData(uuid);
                                if (guardianData.hasBazaar()) {
                                    Bazaar bazaar = guardianData.getBazaar();
                                    bazaar.showToCustomer(player);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
