package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.bossbar.HealthBar;
import io.github.lix3nn53.guardiansofadelia.bossbar.HealthBarManager;
import io.github.lix3nn53.guardiansofadelia.creatures.MobExperienceList;
import io.github.lix3nn53.guardiansofadelia.creatures.drops.DropManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.party.PartyManager;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.utilities.SkillAPIUtils;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.FakeIndicator;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;
import java.util.UUID;

public class MyEntityDamageByEntityEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        double finalDamage = event.getFinalDamage();

        if (target.getType().equals(EntityType.PLAYER)) { //player is target

        } else {
            if (damager.getType().equals(EntityType.PLAYER)) { //player is attacker
                Player player = (Player) damager;
                onPlayerAttackEntity(event, player, target, finalDamage);
            } else if (damager instanceof Projectile) { //projectile is attacker
                Projectile projectile = (Projectile) damager;
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Player) {
                    Player player = (Player) shooter;
                    onPlayerAttackEntity(event, player, target, finalDamage);
                }
            }
        }
    }

    private void onPlayerAttackEntity(EntityDamageByEntityEvent event, Player player, Entity target, double finalDamage) {
        //if living entity
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity) target;
            //show bossbar
            HealthBar healthBar = new HealthBar(livingTarget, (int) (finalDamage + 0.5));
            HealthBarManager.showToPlayerFor10Seconds(player, healthBar);

            UUID uniqueId = player.getUniqueId();
            if (GuardianDataManager.hasGuardianData(uniqueId)) {
                GuardianData guardianData = GuardianDataManager.getGuardianData(uniqueId);
                if (guardianData.hasActiveCharacter()) {
                    RPGCharacter activeCharacter = guardianData.getActiveCharacter();

                    //progress deal damage tasks
                    List<Quest> questList = activeCharacter.getQuestList();
                    for (Quest quest : questList) {
                        quest.progressDealDamageTasks(player, livingTarget, (int) (finalDamage + 0.5));
                    }
                    PartyManager.progressDealDamageTasksOfOtherMembers(player, livingTarget, finalDamage);

                    //on Kill
                    if (finalDamage >= livingTarget.getHealth()) {

                        if (livingTarget.isCustomNameVisible()) {
                            SkillAPIUtils.giveMobExp(player, MobExperienceList.getExperience(livingTarget.getCustomName()));
                        }

                        //progress kill tasks
                        for (Quest quest : questList) {
                            quest.progressKillTasks(player, livingTarget);
                        }
                        PartyManager.progressMobKillTasksOfOtherMembers(player, livingTarget);

                        if (MiniGameManager.isInMinigame(player)) {
                            if (livingTarget.getType().equals(EntityType.PLAYER)) {
                                MiniGameManager.onPlayerKill(player);
                            } else {
                                MiniGameManager.onMobKillDungeon(player, livingTarget);
                            }
                        }
                    }
                }
            }
            DropManager.onPlayerDealDamageToMob(player, livingTarget, (int) (finalDamage + 0.5));
        }

        //indicator
        String text = ChatColor.RED.toString() + (int) (finalDamage + 0.5) + " ➹";
        FakeIndicator.showPlayer(player, text, target.getLocation());
    }

}
