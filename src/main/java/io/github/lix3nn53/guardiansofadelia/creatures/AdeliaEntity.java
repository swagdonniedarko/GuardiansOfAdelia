package io.github.lix3nn53.guardiansofadelia.creatures;

import io.github.lix3nn53.guardiansofadelia.Items.list.MonsterItem;
import io.github.lix3nn53.guardiansofadelia.creatures.entitySkills.EntitySkillComponents;
import io.github.lix3nn53.guardiansofadelia.creatures.entitySkills.EntitySkillSet;
import io.github.lix3nn53.guardiansofadelia.creatures.entitySkills.EntitySkills;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.SkillComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.DamageMechanic;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.ParticleMechanic;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.TeleportTargetMechanic;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.target.AreaTarget;
import io.github.lix3nn53.guardiansofadelia.sounds.GoaSound;
import io.github.lix3nn53.guardiansofadelia.utilities.EntityUtils;
import io.github.lix3nn53.guardiansofadelia.utilities.PersistentDataContainerUtil;
import io.github.lix3nn53.guardiansofadelia.utilities.particle.ArrangementParticle;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum AdeliaEntity {
    //VILLAGER
    VILLAGER_1,
    VILLAGER_2,
    VILLAGER_3,
    VILLAGER_4,
    VILLAGER_5,
    VILLAGER_HOBBIT,
    VILLAGER_SAILOR,
    VILLAGER_FARMER,
    //TUTORIAL
    TUTORIAL_1,
    TUTORIAL_2,
    TUTORIAL_BOSS,
    //HUNTING
    COW_BABY,
    SHEEP_BABY,
    COW,
    SHEEP,
    //AREA-1
    LIZARD,
    LIZARD_POISONOUS,
    SLIME,
    SLIME_STICKY,
    //AREA-2
    ZOMBIE,
    ZOMBIE_VILLAGER,
    ZOMBIE_SPLITTER,
    ZOMBIE_TANK,
    //AREA-3
    SKELETON_ARCHER,
    SKELETON_FIGHTER,
    SKELETON_ROGUE,
    SKELETON_MONK,
    SKELETON_MAGE,
    //AREA-4
    CREEPER,
    VEX,
    SHULKER,
    JELLYBEAN,
    //AREA-5
    PIRATE_SHOOTER,
    PIRATE_FIGHTER,
    PIRATE_SHARPSHOOTER,
    PIRATE_DUEL_MASTER,
    PIRATE_DROWNED,
    //AREA-6
    FROZEN_ROGUE,
    FROZEN_ILLUSIONER,
    FROZEN_ARCHER,
    ZOMBIE_JOCKEY,
    TIMBERMAN,
    //AREA-7
    MUMMY,
    MUMMY_GHOST,
    DESERT_SKELETON_CAVALRY,
    DESERT_SKELETON_ARCHER,
    SPIDER,
    SPIDER_GHOST,
    //AREA-8
    WITCH,
    GOBLIN_ROGUE,
    GOBLIN_FIGHTER,
    GOBLIN_MAGE,
    GOBLIN_JOCKEY,
    GOBLIN_SHAMAN,
    //AREA-9
    ORC_FIGHTER,
    ORC_MAGE,
    ORC_JOCKEY,
    ORC_SHAMAN,
    ORC_GLADIATOR,
    MAGMA_CUBE,
    BLAZE,
    //AREA-10
    ENDERMAN,
    PHANTOM,
    PILLAGER_ROGUE,
    PILLAGER_MAGE,
    PILLAGER_RAVAGER,
    PILLAGER_SHAMAN,
    PILLAGER_FIGHTER,
    //DUNGEON-BOSSES
    BOSS_SLIME,
    BOSS_ZOMBIE,
    BOSS_SKELETON,
    BOSS_COOK,
    BOSS_PIRATE,
    BOSS_ICE,
    BOSS_DESERT,
    BOSS_SWAMP,
    BOSS_LAVA,
    BOSS_DARKNESS;

    /**
     * Use this instead of minecraft attack attribute of entity
     *
     * @param entity
     * @param customDamage
     */
    public static void setCustomDamage(Entity entity, int customDamage) {
        PersistentDataContainerUtil.putInteger("customDamage", customDamage, entity);
    }

    private static void setEntityExperience(Entity entity, int experience) {
        PersistentDataContainerUtil.putInteger("experience", experience, entity);
    }

    private static void setEntityDropTableNo(Entity entity, int dropTableNumber) {
        PersistentDataContainerUtil.putInteger("dropTableNumber", dropTableNumber, entity);
    }

    private void startSkillLoop(LivingEntity livingEntity) {
        List<SkillComponent> skills = new ArrayList<>();
        List<Integer> skillLevels = new ArrayList<>();
        long cooldown = 150;

        if (this == AdeliaEntity.LIZARD_POISONOUS) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentPotionEffectMechanic(PotionEffectType.POISON, 50, 1));
            SkillComponent trigger = EntitySkills.getSkillProjectileParticle(ChatColor.DARK_GREEN + "Shoosh!", 40, children, GoaSound.SKILL_POISON_SLASH, Color.LIME, 1, 2.7);
            skills.add(trigger);
            skillLevels.add(1);
        } else if (this == AdeliaEntity.SLIME_STICKY) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentPullMechanic());
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.SLIME, 3.6, 12, null);
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.GREEN + "Kyaa!", 40, children, 7, GoaSound.SKILL_SPLASH, particleMechanic, true);
            skills.add(trigger);
            skillLevels.add(1);
        } else if (this == AdeliaEntity.ZOMBIE_VILLAGER) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MELEE));
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.VILLAGER_ANGRY, 2.8, 8, null);
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.DARK_GREEN + "Grr!", 40, children, 3.2, GoaSound.SKILL_LIGHTNING_NORMAL, particleMechanic, true);
            skills.add(trigger);
            skillLevels.add(2);
        } else if (this == AdeliaEntity.ZOMBIE_SPLITTER) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentPotionEffectMechanic(PotionEffectType.SLOW, 80, 2));
            SkillComponent trigger = EntitySkills.getSkillProjectileParticle(ChatColor.DARK_GREEN + "Brgrgg!", 40, children, GoaSound.SKILL_SPLASH, Color.LIME, 3, 1.4);
            skills.add(trigger);
            skillLevels.add(2);
        } else if (this == AdeliaEntity.SKELETON_MAGE) {
            List<SkillComponent> children = new ArrayList<>();
            AreaTarget componentAreaTarget = EntitySkillComponents.getComponentAreaTarget(2.8);
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MAGIC));
            children.add(componentAreaTarget);
            SkillComponent trigger = EntitySkills.getSkillProjectileFireball(ChatColor.RED + "Burrn!", 40, children, GoaSound.SKILL_PROJECTILE_FIRE, 1);
            skills.add(trigger);
            skillLevels.add(3);
        } else if (this == AdeliaEntity.CREEPER) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MAGIC));
            children.add(EntitySkillComponents.getComponentPushMechanic());
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.EXPLOSION_HUGE, 4.2, 4, null);
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.AQUA + "BOOM!", 40, children, 4.2, GoaSound.SKILL_SONIC_BOOM, particleMechanic, true);
            skills.add(trigger);
            skillLevels.add(4);
        } else if (this == AdeliaEntity.PIRATE_SHARPSHOOTER) {
            List<SkillComponent> children = new ArrayList<>();
            AreaTarget componentAreaTarget = EntitySkillComponents.getComponentAreaTarget(4);
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.RANGED));
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentPushMechanic());
            children.add(componentAreaTarget);
            SkillComponent trigger = EntitySkills.getSkillProjectileArrow(ChatColor.RED + "Firee!", 40, children, GoaSound.SKILL_SONIC_BOOM, Color.ORANGE, 1);
            skills.add(trigger);
            skillLevels.add(5);
        } else if (this == AdeliaEntity.PIRATE_DUEL_MASTER) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MELEE));
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.REDSTONE, 2.8, 8, new Particle.DustOptions(Color.AQUA, 2));
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.AQUA + "Slash!", 40, children, 3.2, GoaSound.SKILL_SWORD_MULTI_SLASH, particleMechanic, true);
            skills.add(trigger);
            skillLevels.add(5);
        } else if (this == AdeliaEntity.MUMMY_GHOST) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(new TeleportTargetMechanic(true, true));
            SkillComponent trigger = EntitySkills.getSkillProjectileParticle(ChatColor.DARK_GREEN + "Shoosh!", 40, children, GoaSound.SKILL_VOID_SIGNAL, Color.YELLOW, 1, 2.7);
            skills.add(trigger);
            skillLevels.add(7);
        } else if (this == AdeliaEntity.WITCH) {
            List<SkillComponent> children = new ArrayList<>();
            AreaTarget componentAreaTarget = EntitySkillComponents.getComponentAreaTarget(3.6);
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MAGIC));
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentLaunchMechanic());
            children.add(componentAreaTarget);
            ParticleMechanic particleMechanic = new ParticleMechanic(Particle.EXPLOSION_HUGE, ArrangementParticle.SPHERE, 4, 4,
                    0, 0, 0, 0, 1, 0, 0, null);
            children.add(particleMechanic);
            SkillComponent projectileNova = EntitySkills.getSkillProjectileNova(ChatColor.DARK_PURPLE + "Dragon Fireball!", 40, children, GoaSound.SKILL_FIRE_AURA, 1, Particle.DRAGON_BREATH);

            skills.add(projectileNova);
            skillLevels.add(8);
        } else if (this == AdeliaEntity.GOBLIN_MAGE) {
            List<SkillComponent> children = new ArrayList<>();
            AreaTarget componentAreaTarget = EntitySkillComponents.getComponentAreaTarget(2.8);
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MAGIC));
            children.add(componentAreaTarget);
            SkillComponent trigger = EntitySkills.getSkillProjectileFireball(ChatColor.RED + "Burrn!", 40, children, GoaSound.SKILL_PROJECTILE_FIRE, 1);
            skills.add(trigger);
            skillLevels.add(8);
        } else if (this == AdeliaEntity.GOBLIN_SHAMAN) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentHealByAmount());
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.HEART, 8, 12, null);
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.AQUA + "Don't fall, fools!", 10, children, 8, GoaSound.SKILL_HEAL, particleMechanic, false);
            skills.add(trigger);
            skillLevels.add(8);
        } else if (this == AdeliaEntity.ORC_GLADIATOR) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MELEE));
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.REDSTONE, 2.8, 8, new Particle.DustOptions(Color.AQUA, 2));
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.AQUA + "Slash!", 40, children, 2.8, GoaSound.SKILL_SWORD_MULTI_SLASH, particleMechanic, true);
            skills.add(trigger);
            skillLevels.add(9);
        } else if (this == AdeliaEntity.ORC_SHAMAN) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentHealByAmount());
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.HEART, 8, 12, null);
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.AQUA + "Stop them, fools!", 10, children, 8, GoaSound.SKILL_HEAL, particleMechanic, false);
            skills.add(trigger);
            skillLevels.add(9);
        } else if (this == AdeliaEntity.ORC_MAGE) {
            List<SkillComponent> children = new ArrayList<>();
            AreaTarget componentAreaTarget = EntitySkillComponents.getComponentAreaTarget(2.8);
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MAGIC));
            children.add(componentAreaTarget);
            SkillComponent trigger = EntitySkills.getSkillProjectileFireball(ChatColor.RED + "Burrn!", 40, children, GoaSound.SKILL_PROJECTILE_FIRE, 1);
            skills.add(trigger);
            skillLevels.add(9);
        } else if (this == AdeliaEntity.PILLAGER_MAGE) {
            List<SkillComponent> children = new ArrayList<>();
            AreaTarget componentAreaTarget = EntitySkillComponents.getComponentAreaTarget(3.6);
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentDamageMechanic(DamageMechanic.DamageType.MAGIC));
            componentAreaTarget.addChildren(EntitySkillComponents.getComponentLaunchMechanic());
            children.add(componentAreaTarget);
            ParticleMechanic particleMechanic = new ParticleMechanic(Particle.EXPLOSION_HUGE, ArrangementParticle.SPHERE, 4, 4,
                    0, 0, 0, 0, 1, 0, 0, null);
            children.add(particleMechanic);
            SkillComponent projectileNova = EntitySkills.getSkillProjectileNova(ChatColor.DARK_PURPLE + "Dragon Fireball!", 40, children, GoaSound.SKILL_FIRE_AURA, 1, Particle.DRAGON_BREATH);

            skills.add(projectileNova);
            skillLevels.add(10);
        } else if (this == AdeliaEntity.PILLAGER_SHAMAN) {
            List<SkillComponent> children = new ArrayList<>();
            children.add(EntitySkillComponents.getComponentHealByAmount());
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.HEART, 8, 12, null);
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.AQUA + "Stop them, fools!", 10, children, 8, GoaSound.SKILL_HEAL, particleMechanic, false);
            skills.add(trigger);
            skillLevels.add(10);
        } else if (this == AdeliaEntity.BOSS_SLIME) {
            List<SkillComponent> children = new ArrayList<>();
            List<AdeliaEntity> adeliaEntities = new ArrayList<>();
            adeliaEntities.add(SLIME);
            children.add(EntitySkillComponents.getComponentSpawnEntity(adeliaEntities, 2));
            ParticleMechanic particleMechanic = EntitySkillComponents.getComponentParticleMechanic(Particle.REDSTONE, 8, 8, new Particle.DustOptions(Color.AQUA, 2));
            SkillComponent trigger = EntitySkills.getSkillAoeAround(ChatColor.GREEN + "Heelp!", 10, children, 8, GoaSound.SKILL_HEAL, particleMechanic, true);
            skills.add(trigger);
            skillLevels.add(10);
        } else if (this == AdeliaEntity.BOSS_ZOMBIE) {

        } else if (this == AdeliaEntity.BOSS_SKELETON) {

        } else if (this == AdeliaEntity.BOSS_COOK) {

        } else if (this == AdeliaEntity.BOSS_PIRATE) {

        } else if (this == AdeliaEntity.BOSS_ICE) {

        } else if (this == AdeliaEntity.BOSS_DESERT) {

        } else if (this == AdeliaEntity.BOSS_SWAMP) {

        } else if (this == AdeliaEntity.BOSS_LAVA) {

        } else if (this == AdeliaEntity.BOSS_DARKNESS) {

        }

        EntitySkillSet entitySkillSet = new EntitySkillSet(skills, skillLevels, cooldown);
        entitySkillSet.startSkillLoop(livingEntity);
    }

    public LivingEntity getMob(Location loc) {
        LivingEntity livingEntity = null;
        switch (this) {
            case VILLAGER_HOBBIT: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Hobbit", 20000D, EntityType.VILLAGER);
                villager.setBaby();
                villager.setAgeLock(true);

                livingEntity = villager;
                break;
            }
            case VILLAGER_1: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.GREEN + "Roumen Villager", 40000D, EntityType.VILLAGER);
                villager.setAdult();
                Villager.Profession[] values = Villager.Profession.values();
                int i = new Random().nextInt(values.length);
                Villager.Profession value = values[i];
                villager.setProfession(value);
                villager.setVillagerLevel(2);
                livingEntity = villager;
                break;
            }
            case VILLAGER_2: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.GREEN + "Veloa Villager", 60000D, EntityType.VILLAGER);
                villager.setAdult();
                Villager.Profession[] values = Villager.Profession.values();
                int i = new Random().nextInt(values.length);
                Villager.Profession value = values[i];
                villager.setProfession(value);
                villager.setVillagerLevel(2);
                livingEntity = villager;
                break;
            }
            case VILLAGER_3: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.GREEN + "Elderine Villager", 80000D, EntityType.VILLAGER);
                villager.setAdult();
                Villager.Profession[] values = Villager.Profession.values();
                int i = new Random().nextInt(values.length);
                Villager.Profession value = values[i];
                villager.setProfession(value);
                villager.setVillagerLevel(2);
                livingEntity = villager;
                break;
            }
            case VILLAGER_4: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.GREEN + "Uruga Villager", 120000D, EntityType.VILLAGER);
                villager.setAdult();
                Villager.Profession[] values = Villager.Profession.values();
                int i = new Random().nextInt(values.length);
                Villager.Profession value = values[i];
                villager.setProfession(value);
                villager.setVillagerLevel(2);
                livingEntity = villager;
                break;
            }
            case VILLAGER_5: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.GREEN + "Aberstol Villager", 200000D, EntityType.VILLAGER);
                villager.setAdult();
                Villager.Profession[] values = Villager.Profession.values();
                int i = new Random().nextInt(values.length);
                Villager.Profession value = values[i];
                villager.setProfession(value);
                villager.setVillagerLevel(2);
                livingEntity = villager;
                break;
            }
            case VILLAGER_SAILOR: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.AQUA + "Sailor", 100000D, EntityType.VILLAGER);
                villager.setAdult();
                villager.setProfession(Villager.Profession.WEAPONSMITH);
                villager.setVillagerLevel(2);
                livingEntity = villager;
                break;
            }
            case VILLAGER_FARMER: {
                Villager villager = (Villager) EntityUtils.create(loc, ChatColor.YELLOW + "Farmer", 80000D, EntityType.VILLAGER);
                villager.setAdult();
                villager.setProfession(Villager.Profession.FARMER);
                villager.setVillagerLevel(2);
                livingEntity = villager;
                break;
            }
            case TUTORIAL_1: {
                WitherSkeleton entity = (WitherSkeleton) EntityUtils.create(loc, ChatColor.RED + "Aleesia's Soldier", 2800D, EntityType.WITHER_SKELETON);
                setCustomDamage(entity, 400);
                ItemStack sword = MonsterItem.SWORD_DARK.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                livingEntity = entity;
                break;
            }
            case TUTORIAL_2: {
                Stray entity = (Stray) EntityUtils.create(loc, ChatColor.RED + "Aleesia's Ranger", 1600D, EntityType.STRAY);
                ItemStack bow = MonsterItem.BOW_DARK.getItem(200);
                entity.getEquipment().setItemInMainHand(bow);
                livingEntity = entity;
                break;
            }
            case TUTORIAL_BOSS:
                double maxHealth = 200000D;
                Wither wither = (Wither) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Aleesia", maxHealth, EntityType.WITHER);
                wither.setCustomNameVisible(true);
                setCustomDamage(wither, 3000);

                MobDisguise disguise = new MobDisguise(DisguiseType.WITHER_SKELETON, false);

                LivingWatcher watcher = disguise.getWatcher();

                watcher.setCustomNameVisible(true);
                watcher.setCustomName(ChatColor.DARK_PURPLE + "Aleesia");
                watcher.setInvisible(true);
                watcher.setNoGravity(true);
                watcher.addPotionEffect(PotionEffectType.GLOWING);
                watcher.setMaxHealth(maxHealth);
                watcher.setHealth((float) maxHealth);

                EntityEquipment mobEquipment = watcher.getEquipment();

                ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setCustomModelData(11);
                itemMeta.setUnbreakable(true);
                itemStack.setItemMeta(itemMeta);
                mobEquipment.setHelmet(itemStack);

                DisguiseAPI.disguiseToAll(wither, disguise);

                livingEntity = wither;
                break;
            case COW: {
                Cow entity = (Cow) EntityUtils.create(loc, ChatColor.GREEN + "Cow", 1420D, EntityType.COW);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
                entity.setBreed(false);
                entity.setAdult();
                livingEntity = entity;
                break;
            }
            case COW_BABY: {
                Cow entity = (Cow) EntityUtils.create(loc, ChatColor.GREEN + "Baby Cow", 142D, EntityType.COW);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
                entity.setBreed(false);
                entity.setBaby();
                entity.setAgeLock(true);
                livingEntity = entity;
                break;
            }
            case SHEEP: {
                Sheep entity = (Sheep) EntityUtils.create(loc, ChatColor.GREEN + "Sheep", 1420D, EntityType.SHEEP);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
                entity.setBreed(false);
                entity.setAdult();
                livingEntity = entity;
                break;
            }
            case SHEEP_BABY: {
                Sheep entity = (Sheep) EntityUtils.create(loc, ChatColor.GREEN + "Baby Sheep", 142D, EntityType.SHEEP);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
                entity.setBreed(false);
                entity.setBaby();
                entity.setAgeLock(true);
                livingEntity = entity;
                break;
            }
            case LIZARD: {
                Silverfish entity = (Silverfish) EntityUtils.create(loc, ChatColor.GREEN + "Wild Lizard", 20D, EntityType.SILVERFISH);
                setCustomDamage(entity, 5);
                setEntityExperience(entity, 2);
                setEntityDropTableNo(entity, 0);
                livingEntity = entity;
                break;
            }
            case LIZARD_POISONOUS: {
                Silverfish entity = (Silverfish) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Poisonous Lizard", 20D, EntityType.SILVERFISH);
                setCustomDamage(entity, 5);
                setEntityExperience(entity, 2);
                setEntityDropTableNo(entity, 0);
                livingEntity = entity;
                break;
            }
            case SLIME: {
                double hp = 30D;
                Slime entity = (Slime) EntityUtils.create(loc, ChatColor.GREEN + "Baby Slime", hp, EntityType.SLIME);
                entity.setSize(2);
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                entity.setHealth(hp);
                setEntityExperience(entity, 5);
                setEntityDropTableNo(entity, 0);
                setCustomDamage(entity, 7);
                livingEntity = entity;
                break;
            }
            case SLIME_STICKY: {
                double hp = 40D;
                Slime entity = (Slime) EntityUtils.create(loc, ChatColor.GREEN + "Sticky Slime", hp, EntityType.SLIME);
                entity.setSize(3);
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                entity.setHealth(hp);
                setEntityExperience(entity, 7);
                setEntityDropTableNo(entity, 0);
                setCustomDamage(entity, 9);
                livingEntity = entity;
                break;
            }
            case ZOMBIE: {
                Zombie entity = (Zombie) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Zombie", 50D, EntityType.ZOMBIE);
                setCustomDamage(entity, 12);
                entity.setBaby(false);
                entity.getEquipment().clear();
                setEntityExperience(entity, 12);
                setEntityDropTableNo(entity, 1);
                livingEntity = entity;
                break;
            }
            case ZOMBIE_VILLAGER: {
                ZombieVillager entity = (ZombieVillager) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Zombie Villager", 60D, EntityType.ZOMBIE_VILLAGER);
                setCustomDamage(entity, 14);
                entity.setBaby(false);
                entity.getEquipment().clear();
                ItemStack axe = MonsterItem.AXE_WOODEN.getItem(0);
                entity.getEquipment().setItemInMainHand(axe);
                setEntityExperience(entity, 14);
                setEntityDropTableNo(entity, 1);
                livingEntity = entity;
                break;
            }
            case ZOMBIE_SPLITTER: {
                Zombie entity = (Zombie) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Splitter Zombie", 50D, EntityType.ZOMBIE);
                setCustomDamage(entity, 10);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2D);
                entity.setBaby(false);
                entity.getEquipment().clear();
                setEntityExperience(entity, 14);
                setEntityDropTableNo(entity, 1);
                livingEntity = entity;
                break;
            }
            case ZOMBIE_TANK: {
                Zombie entity = (Zombie) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Tank Zombie", 80D, EntityType.ZOMBIE);
                setCustomDamage(entity, 10);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15D);
                entity.setBaby(false);
                entity.getEquipment().clear();
                ItemStack shield = MonsterItem.SHIELD_WOODEN.getItem(0);
                entity.getEquipment().setItemInOffHand(shield);
                setEntityExperience(entity, 18);
                setEntityDropTableNo(entity, 1);
                livingEntity = entity;
                break;
            }
            case SKELETON_ARCHER: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.WHITE + "Archer Skeleton", 120D, EntityType.SKELETON);
                entity.getEquipment().clear();
                ItemStack bow = MonsterItem.BOW_WOODEN.getItem(20);
                entity.getEquipment().setItemInMainHand(bow);
                setEntityExperience(entity, 53);
                setEntityDropTableNo(entity, 2);
                livingEntity = entity;
                break;
            }
            case SKELETON_FIGHTER: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.WHITE + "Fighter Skeleton", 150D, EntityType.SKELETON);
                setCustomDamage(entity, 30);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SWORD_WOODEN.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                setEntityExperience(entity, 53);
                setEntityDropTableNo(entity, 2);
                livingEntity = entity;
                break;
            }
            case SKELETON_ROGUE: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.WHITE + "Rogue Skeleton", 120D, EntityType.SKELETON);
                setCustomDamage(entity, 25);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.DAGGER_WOOD.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                entity.getEquipment().setItemInOffHand(sword);
                setEntityExperience(entity, 53);
                setEntityDropTableNo(entity, 2);
                livingEntity = entity;
                break;
            }
            case SKELETON_MONK: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.WHITE + "Monk Skeleton", 140D, EntityType.SKELETON);
                setCustomDamage(entity, 25);
                entity.getEquipment().clear();
                ItemStack spear = MonsterItem.SPEAR_STEEL.getItem(0);
                entity.getEquipment().setItemInMainHand(spear);
                setEntityExperience(entity, 60);
                setEntityDropTableNo(entity, 2);
                livingEntity = entity;
                break;
            }
            case SKELETON_MAGE: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.WHITE + "Mage Skeleton", 120D, EntityType.SKELETON);
                setCustomDamage(entity, 20);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15D);
                entity.getEquipment().clear();
                ItemStack wand = MonsterItem.STAFF_BONE.getItem(0);
                entity.getEquipment().setItemInMainHand(wand);
                ItemStack helmet = MonsterItem.HELMET_GOLDEN.getItem(0);
                entity.getEquipment().setHelmet(helmet);
                setEntityExperience(entity, 68);
                setEntityDropTableNo(entity, 2);
                livingEntity = entity;
                break;
            }
            case CREEPER: {
                Creeper entity = (Creeper) EntityUtils.create(loc, ChatColor.AQUA + "Popping Rainbow", 320D, EntityType.CREEPER);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3D);
                setEntityExperience(entity, 153);
                setEntityDropTableNo(entity, 3);
                livingEntity = entity;
                break;
            }
            case VEX: {
                Vex entity = (Vex) EntityUtils.create(loc, ChatColor.LIGHT_PURPLE + "Taffy Spirit", 280D, EntityType.VEX);
                setCustomDamage(entity, 60);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4D);
                setEntityExperience(entity, 164);
                setEntityDropTableNo(entity, 3);
                livingEntity = entity;
                break;
            }
            case SHULKER: {
                Shulker entity = (Shulker) EntityUtils.create(loc, ChatColor.LIGHT_PURPLE + "Candy Box", 400D, EntityType.SHULKER);
                setEntityExperience(entity, 142);
                setEntityDropTableNo(entity, 3);
                livingEntity = entity;
                break;
            }
            case JELLYBEAN: {
                Endermite entity = (Endermite) EntityUtils.create(loc, ChatColor.LIGHT_PURPLE + "Jellybean", 360D, EntityType.ENDERMITE);
                setCustomDamage(entity, 60);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4D);
                setEntityExperience(entity, 164);
                setEntityDropTableNo(entity, 3);
                livingEntity = entity;
                break;
            }
            case PIRATE_SHOOTER: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.DARK_AQUA + "Shooter Pirate", 640D, EntityType.SKELETON);
                entity.getEquipment().clear();
                ItemStack pistol = MonsterItem.PIRATE_PISTOL.getItem(100);
                entity.getEquipment().setItemInMainHand(pistol);
                ItemStack helmet = MonsterItem.PIRATE_HAT.getItem(0);
                entity.getEquipment().setHelmet(helmet);
                setEntityExperience(entity, 253);
                setEntityDropTableNo(entity, 4);
                setCustomDamage(entity, 100);
                livingEntity = entity;
                break;
            }
            case PIRATE_FIGHTER: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.DARK_AQUA + "Fighter Pirate", 720D, EntityType.SKELETON);
                setCustomDamage(entity, 120);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SWORD_STEEL.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                ItemStack helmet = MonsterItem.PIRATE_HAT.getItem(0);
                entity.getEquipment().setHelmet(helmet);
                setEntityExperience(entity, 253);
                setEntityDropTableNo(entity, 4);
                livingEntity = entity;
                break;
            }
            case PIRATE_SHARPSHOOTER: {
                WitherSkeleton entity = (WitherSkeleton) EntityUtils.create(loc, ChatColor.DARK_AQUA + "Sharpshooter Pirate", 700D, EntityType.WITHER_SKELETON);
                entity.getEquipment().clear();
                ItemStack pistol = MonsterItem.PIRATE_PISTOL.getItem(120);
                entity.getEquipment().setItemInMainHand(pistol);
                ItemStack helmet = MonsterItem.PIRATE_HAT.getItem(0);
                entity.getEquipment().setHelmet(helmet);
                setEntityExperience(entity, 282);
                setEntityDropTableNo(entity, 4);
                setCustomDamage(entity, 120);
                livingEntity = entity;
                break;
            }
            case PIRATE_DUEL_MASTER: {
                WitherSkeleton entity = (WitherSkeleton) EntityUtils.create(loc, ChatColor.DARK_AQUA + "Duel Master Pirate", 780D, EntityType.WITHER_SKELETON);
                setCustomDamage(entity, 140);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.KATANA.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                ItemStack helmet = MonsterItem.PIRATE_HAT.getItem(0);
                entity.getEquipment().setHelmet(helmet);
                setEntityExperience(entity, 282);
                setEntityDropTableNo(entity, 4);
                livingEntity = entity;
                break;
            }
            case PIRATE_DROWNED: {
                Drowned entity = (Drowned) EntityUtils.create(loc, ChatColor.DARK_AQUA + "Drowned Pirate", 700D, EntityType.DROWNED);
                setCustomDamage(entity, 100);
                entity.getEquipment().clear();
                entity.setBaby(false);
                ItemStack sword = MonsterItem.SPEAR_STEEL.getItem(400);
                entity.getEquipment().setItemInMainHand(sword);
                setEntityExperience(entity, 282);
                setEntityDropTableNo(entity, 4);
                livingEntity = entity;
                break;
            }
            case FROZEN_ROGUE: {
                Stray entity = (Stray) EntityUtils.create(loc, ChatColor.AQUA + "Frozen Rogue", 1400D, EntityType.STRAY);
                setCustomDamage(entity, 210);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3D);
                entity.getEquipment().clear();
                ItemStack dagger = MonsterItem.DAGGER_CRIMSON.getItem(0);
                entity.getEquipment().setItemInMainHand(dagger);
                entity.getEquipment().setItemInOffHand(dagger);
                setEntityExperience(entity, 420);
                setEntityDropTableNo(entity, 5);
                livingEntity = entity;
                break;
            }
            case FROZEN_ILLUSIONER: {
                Illusioner entity = (Illusioner) EntityUtils.create(loc, ChatColor.AQUA + "Frozen Illusioner", 1200D, EntityType.ILLUSIONER);
                setCustomDamage(entity, 190);
                entity.getEquipment().clear();
                ItemStack dagger = MonsterItem.BOW_DARK.getItem(0);
                entity.getEquipment().setItemInMainHand(dagger);
                setEntityExperience(entity, 420);
                setEntityDropTableNo(entity, 5);
                livingEntity = entity;
                break;
            }
            case FROZEN_ARCHER: {
                Stray entity = (Stray) EntityUtils.create(loc, ChatColor.AQUA + "Frozen Ranger", 1200D, EntityType.STRAY);
                entity.getEquipment().clear();
                ItemStack bow = MonsterItem.BOW_DARK.getItem(200);
                entity.getEquipment().setItemInMainHand(bow);
                setEntityExperience(entity, 420);
                setEntityDropTableNo(entity, 5);
                livingEntity = entity;
                break;
            }
            case ZOMBIE_JOCKEY: {
                Zombie entity = (Zombie) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Jockey Zombie", 900D, EntityType.ZOMBIE);
                entity.getEquipment().clear();
                setCustomDamage(entity, 140);
                entity.setBaby(true);

                Chicken mount = (Chicken) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Zombie Chicken", 400D, EntityType.CHICKEN);
                mount.addPassenger(entity);

                setEntityExperience(mount, 253);
                setEntityExperience(entity, 400);
                setEntityDropTableNo(entity, 5);
                livingEntity = entity;
                break;
            }
            case TIMBERMAN: {
                Vindicator entity = (Vindicator) EntityUtils.create(loc, ChatColor.AQUA + "Frozen Timberman", 1500D, EntityType.VINDICATOR);
                setCustomDamage(entity, 240);
                entity.getEquipment().clear();
                ItemStack axe = MonsterItem.AXE_WOODEN.getItem(0);
                entity.getEquipment().setItemInMainHand(axe);
                setEntityExperience(entity, 450);
                setEntityDropTableNo(entity, 5);
                livingEntity = entity;
                break;
            }
            case MUMMY: {
                Husk entity = (Husk) EntityUtils.create(loc, ChatColor.YELLOW + "Mummy", 1900D, EntityType.HUSK);
                setCustomDamage(entity, 280);
                entity.getEquipment().clear();
                setEntityExperience(entity, 553);
                setEntityDropTableNo(entity, 6);
                livingEntity = entity;
                break;
            }
            case MUMMY_GHOST: {
                Husk entity = (Husk) EntityUtils.create(loc, ChatColor.YELLOW + "Ghost Mummy", 2100D, EntityType.HUSK);
                setCustomDamage(entity, 300);
                entity.getEquipment().clear();
                PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
                entity.addPotionEffect(invis);
                PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1);
                entity.addPotionEffect(glow);
                setEntityExperience(entity, 584);
                setEntityDropTableNo(entity, 6);
                livingEntity = entity;
                break;
            }
            case DESERT_SKELETON_ARCHER: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.YELLOW + "Desert Archer Skeleton", 1700D, EntityType.SKELETON);
                entity.getEquipment().clear();
                ItemStack bow = MonsterItem.BOW_SATET.getItem(250);
                entity.getEquipment().setItemInMainHand(bow);

                Spider mount = (Spider) EntityUtils.create(loc, ChatColor.WHITE + "Desert Spider", 600D, EntityType.SPIDER);
                mount.addPassenger(entity);

                setEntityExperience(mount, 420);
                setEntityExperience(entity, 553);
                setEntityDropTableNo(entity, 6);
                livingEntity = entity;
                break;
            }
            case DESERT_SKELETON_CAVALRY: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.YELLOW + "Desert Cavalry Skeleton", 1900D, EntityType.SKELETON);
                setCustomDamage(entity, 300);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SPEAR_RED.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);

                Spider mount = (Spider) EntityUtils.create(loc, ChatColor.WHITE + "Desert Spider", 600D, EntityType.SPIDER);
                mount.addPassenger(entity);

                setEntityExperience(mount, 420);
                setEntityExperience(entity, 553);
                setEntityDropTableNo(entity, 6);
                livingEntity = entity;
                break;
            }
            case SPIDER: {
                Spider entity = (Spider) EntityUtils.create(loc, ChatColor.YELLOW + "Desert Spider", 1900D, EntityType.SPIDER);
                setCustomDamage(entity, 250);
                setEntityExperience(entity, 584);
                setEntityDropTableNo(entity, 6);
                livingEntity = entity;
                break;
            }
            case SPIDER_GHOST: {
                CaveSpider entity = (CaveSpider) EntityUtils.create(loc, ChatColor.YELLOW + "Ghost Spider", 2100D, EntityType.CAVE_SPIDER);
                setCustomDamage(entity, 300);
                PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
                entity.addPotionEffect(invis);
                PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1);
                entity.addPotionEffect(glow);
                setEntityExperience(entity, 584);
                setEntityDropTableNo(entity, 6);
                livingEntity = entity;
                break;
            }
            case WITCH: {
                Witch entity = (Witch) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Witch", 3400D, EntityType.WITCH);
                setEntityExperience(entity, 764);
                setEntityDropTableNo(entity, 7);
                livingEntity = entity;
                break;
            }
            case GOBLIN_JOCKEY: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Jockey Goblin", 2000D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 240);
                entity.getEquipment().clear();
                entity.setBaby(true);
                ItemStack sword = MonsterItem.SWORD_WOODEN.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);

                Chicken mount = (Chicken) EntityUtils.create(loc, ChatColor.YELLOW + "Goblin Chicken", 900D, EntityType.CHICKEN);
                mount.addPassenger(entity);

                setEntityExperience(mount, 553);
                setEntityExperience(entity, 720);
                setEntityDropTableNo(entity, 7);
                livingEntity = entity;
                break;
            }
            case GOBLIN_FIGHTER: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Fighter Goblin", 3200D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 380);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SWORD_WOODEN.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                entity.setBaby(true);
                setEntityExperience(entity, 750);
                setEntityDropTableNo(entity, 7);
                livingEntity = entity;
                break;
            }
            case GOBLIN_ROGUE: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Rogue Goblin", 2800D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 320);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.DAGGER_WOOD.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                entity.getEquipment().setItemInOffHand(sword);
                entity.setBaby(true);
                setEntityExperience(entity, 750);
                setEntityDropTableNo(entity, 7);
                livingEntity = entity;
                break;
            }
            case GOBLIN_MAGE: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Mage Goblin", 2400D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 300);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15D);
                entity.getEquipment().clear();
                ItemStack wand = MonsterItem.STAFF_WOODEN.getItem(0);
                ItemStack circlet = MonsterItem.HELMET_GOLDEN.getItem(0);
                entity.getEquipment().setItemInMainHand(wand);
                entity.getEquipment().setHelmet(circlet);
                entity.setBaby(true);
                setEntityExperience(entity, 750);
                setEntityDropTableNo(entity, 7);
                livingEntity = entity;
                break;
            }
            case GOBLIN_SHAMAN: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Shaman Goblin", 2400D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 300);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15D);
                entity.getEquipment().clear();
                ItemStack wand = MonsterItem.STAFF_LEAF.getItem(0);
                ItemStack circlet = MonsterItem.HELMET_GOLDEN.getItem(0);
                entity.getEquipment().setItemInMainHand(wand);
                entity.getEquipment().setHelmet(circlet);
                entity.setBaby(true);
                setEntityExperience(entity, 750);
                setEntityDropTableNo(entity, 7);
                livingEntity = entity;
                break;
            }
            case ORC_FIGHTER: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Fighter Orc", 4500D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 480);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SWORD_WOODEN.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                entity.setBaby(false);
                setEntityExperience(entity, 880);
                setEntityDropTableNo(entity, 8);
                livingEntity = entity;
                break;
            }
            case ORC_JOCKEY: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Jockey Orc", 2800D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 320);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SWORD_WOODEN.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                entity.setBaby(false);

                Chicken mount = (Chicken) EntityUtils.create(loc, ChatColor.YELLOW + "Orc Chicken", 1200D, EntityType.CHICKEN);
                mount.addPassenger(entity);

                setEntityExperience(mount, 720);
                setEntityExperience(entity, 860);
                setEntityDropTableNo(entity, 8);
                livingEntity = entity;
                break;
            }
            case ORC_GLADIATOR: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Gladiator Orc", 5000D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 500);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SWORD_STEEL.getItem(0);
                ItemStack chest = MonsterItem.CHESTPLATE_IRON.getItem(0);
                ItemStack leg = MonsterItem.LEGGINGS_IRON.getItem(0);
                ItemStack boot = MonsterItem.BOOTS_IRON.getItem(0);
                ItemStack shield = MonsterItem.SHIELD_KNIGHT.getItem(0);
                entity.getEquipment().setChestplate(chest);
                entity.getEquipment().setLeggings(leg);
                entity.getEquipment().setBoots(boot);
                entity.getEquipment().setItemInMainHand(sword);
                entity.getEquipment().setItemInOffHand(shield);
                entity.setBaby(false);
                setEntityExperience(entity, 920);
                setEntityDropTableNo(entity, 8);
                livingEntity = entity;
                break;
            }
            case ORC_SHAMAN: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Shaman Orc", 3400D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 400);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15D);
                entity.getEquipment().clear();
                ItemStack wand = MonsterItem.STAFF_LEAF.getItem(0);
                ItemStack circlet = MonsterItem.HELMET_GOLDEN.getItem(0);
                entity.getEquipment().setItemInMainHand(wand);
                entity.getEquipment().setHelmet(circlet);
                entity.setBaby(false);
                setEntityExperience(entity, 920);
                setEntityDropTableNo(entity, 8);
                livingEntity = entity;
                break;
            }
            case ORC_MAGE: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.YELLOW + "Mage Orc", 3400D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 400);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15D);
                entity.getEquipment().clear();
                ItemStack wand = MonsterItem.STAFF_FIRE.getItem(0);
                ItemStack circlet = MonsterItem.HELMET_GOLDEN.getItem(0);
                entity.getEquipment().setItemInMainHand(wand);
                entity.getEquipment().setHelmet(circlet);
                entity.setBaby(false);
                setEntityExperience(entity, 920);
                setEntityDropTableNo(entity, 8);
                livingEntity = entity;
                break;
            }
            case MAGMA_CUBE: {
                double hp = 4000D;
                MagmaCube entity = (MagmaCube) EntityUtils.create(loc, ChatColor.RED + "Magma Cube", hp, EntityType.MAGMA_CUBE);
                setCustomDamage(entity, 420);
                entity.setSize(3);
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                entity.setHealth(hp);
                setEntityExperience(entity, 880);
                setEntityDropTableNo(entity, 8);
                livingEntity = entity;
                break;
            }
            case BLAZE: {
                Blaze entity = (Blaze) EntityUtils.create(loc, ChatColor.RED + "Blaze", 2800D, EntityType.BLAZE);
                setCustomDamage(entity, 340);
                setEntityExperience(entity, 880);
                setEntityDropTableNo(entity, 8);
                livingEntity = entity;
                break;
            }
            case ENDERMAN: {
                Enderman entity = (Enderman) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Void", 7200D, EntityType.ENDERMAN);
                setCustomDamage(entity, 640);
                setEntityExperience(entity, 1000);
                setEntityDropTableNo(entity, 9);
                livingEntity = entity;
                break;
            }
            case PHANTOM: {
                Phantom entity = (Phantom) EntityUtils.create(loc, ChatColor.LIGHT_PURPLE + "Phantom", 5400D, EntityType.PHANTOM);
                setCustomDamage(entity, 540);
                setEntityExperience(entity, 164);
                setEntityDropTableNo(entity, 3);
                livingEntity = entity;
                break;
            }
            case PILLAGER_ROGUE: {
                Pillager entity = (Pillager) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Rogue Pillager", 8000D, EntityType.PILLAGER);
                setCustomDamage(entity, 700);
                entity.getEquipment().clear();

                ItemStack item = MonsterItem.DAGGER_DARKEST.getItem(0);

                entity.getEquipment().setItemInMainHand(item);
                entity.getEquipment().setItemInOffHand(item);

                setEntityExperience(entity, 1000);
                setEntityDropTableNo(entity, 9);
                livingEntity = entity;
                break;
            }
            case PILLAGER_MAGE: {
                Pillager entity = (Pillager) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Mage Pillager", 7000D, EntityType.PILLAGER);
                setCustomDamage(entity, 500);
                entity.getEquipment().clear();
                ItemStack item = MonsterItem.STAFF_FIRE.getItem(0);
                entity.getEquipment().setItemInMainHand(item);

                setEntityExperience(entity, 1000);
                setEntityDropTableNo(entity, 9);
                livingEntity = entity;
                break;
            }
            case PILLAGER_RAVAGER: {
                Pillager entity = (Pillager) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Commander Pillager", 10000D, EntityType.PILLAGER);
                setCustomDamage(entity, 900);
                entity.getEquipment().clear();
                ItemStack item = MonsterItem.CROSSBOW_DARK.getItem(0);
                entity.getEquipment().setItemInMainHand(item);

                Ravager mount = (Ravager) EntityUtils.create(loc, ChatColor.YELLOW + "Ravager", 2400D, EntityType.RAVAGER);
                mount.addPassenger(entity);

                setEntityExperience(mount, 553);
                setEntityExperience(entity, 1000);
                setEntityDropTableNo(entity, 9);
                livingEntity = entity;
                break;
            }
            case PILLAGER_SHAMAN: {
                Pillager entity = (Pillager) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Shaman Pillager", 7000D, EntityType.PILLAGER);
                setCustomDamage(entity, 500);

                ItemStack item = MonsterItem.STAFF_MOON.getItem(0);
                entity.getEquipment().setItemInMainHand(item);

                setEntityExperience(entity, 1000);
                setEntityDropTableNo(entity, 9);
                livingEntity = entity;
                break;
            }
            case PILLAGER_FIGHTER: {
                Pillager entity = (Pillager) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Fighter Pillager", 9000D, EntityType.PILLAGER);
                setCustomDamage(entity, 800);

                ItemStack item = MonsterItem.MACE_DARK.getItem(0);
                ItemStack shield = MonsterItem.SHIELD_DARK.getItem(0);

                entity.getEquipment().setItemInMainHand(item);
                entity.getEquipment().setItemInOffHand(shield);

                setEntityExperience(entity, 1000);
                setEntityDropTableNo(entity, 9);
                livingEntity = entity;
                break;
            }
            case BOSS_SLIME: {
                double hp = 600D;
                Slime entity = (Slime) EntityUtils.create(loc, ChatColor.GOLD + "King Slime", hp, EntityType.SLIME);
                entity.setSize(6);
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                entity.setHealth(hp);
                setCustomDamage(entity, 24);
                livingEntity = entity;
                break;
            }
            case BOSS_ZOMBIE: {
                Zombie entity = (Zombie) EntityUtils.create(loc, ChatColor.DARK_GREEN + "Zombie Subject#471", 1000D, EntityType.ZOMBIE);
                setCustomDamage(entity, 50);
                entity.setBaby(false);
                entity.getEquipment().clear();
                ItemStack sword = MonsterItem.SWORD_STEEL.getItem(0);
                entity.getEquipment().setItemInMainHand(sword);
                ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
                ItemStack leg = new ItemStack(Material.IRON_LEGGINGS);
                ItemStack boots = new ItemStack(Material.IRON_BOOTS);
                entity.getEquipment().setChestplate(chest);
                entity.getEquipment().setLeggings(leg);
                entity.getEquipment().setBoots(boots);
                livingEntity = entity;
                break;
            }
            case BOSS_SKELETON: {
                Skeleton entity = (Skeleton) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Dark Magician Nimzuth", 2400D, EntityType.SKELETON);
                setCustomDamage(entity, 120);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.18D);
                entity.getEquipment().clear();
                ItemStack wand = MonsterItem.STAFF_FIRE.getItem(0);
                entity.getEquipment().setItemInMainHand(wand);
                ItemStack circlet = MonsterItem.HELMET_GOLDEN.getItem(0);
                entity.getEquipment().setHelmet(circlet);
                ItemStack chest = MonsterItem.CHESTPLATE_GOLDEN.getItem(0);
                ItemStack leg = MonsterItem.LEGGINGS_GOLDEN.getItem(0);
                ItemStack boots = MonsterItem.BOOTS_GOLDEN.getItem(0);
                entity.getEquipment().setChestplate(chest);
                entity.getEquipment().setLeggings(leg);
                entity.getEquipment().setBoots(boots);
                livingEntity = entity;
                break;
            }
            case BOSS_COOK: {
                Evoker entity = (Evoker) EntityUtils.create(loc, ChatColor.YELLOW + "Evil Cook", 7500D, EntityType.EVOKER);
                setCustomDamage(entity, 250);
                entity.getEquipment().clear();

                livingEntity = entity;
                break;
            }
            case BOSS_PIRATE: {
                WitherSkeleton entity = (WitherSkeleton) EntityUtils.create(loc, ChatColor.DARK_AQUA + "Captain Barbaros", 15000D, EntityType.WITHER_SKELETON);
                setCustomDamage(entity, 500);
                ItemStack sword = MonsterItem.KATANA.getItem(0);
                ItemStack helmet = MonsterItem.PIRATE_HAT.getItem(0);
                ItemStack pistol = MonsterItem.PIRATE_PISTOL.getItem(500);
                entity.getEquipment().clear();
                entity.getEquipment().setItemInMainHand(pistol);
                entity.getEquipment().setItemInOffHand(sword);
                entity.getEquipment().setHelmet(helmet);
                livingEntity = entity;
                break;
            }
            case BOSS_ICE: {
                Vindicator entity = (Vindicator) EntityUtils.create(loc, ChatColor.DARK_PURPLE + "Necromancer King", 30000D, EntityType.VINDICATOR);
                setCustomDamage(entity, 800);
                ItemStack axe = MonsterItem.AXE_FROST.getItem(0);
                entity.getEquipment().clear();
                entity.getEquipment().setItemInMainHand(axe);
                livingEntity = entity;
                break;
            }
            case BOSS_DESERT: {
                Husk entity = (Husk) EntityUtils.create(loc, ChatColor.YELLOW + "Pharaoh", 45000D, EntityType.HUSK);
                setCustomDamage(entity, 1200);
                entity.getEquipment().clear();
                livingEntity = entity;
                break;
            }
            case BOSS_SWAMP: {
                Witch entity = (Witch) EntityUtils.create(loc, ChatColor.BLUE + "Guardian Esobel", 70000D, EntityType.WITCH);
                setCustomDamage(entity, 1500);
                entity.getEquipment().clear();
                ItemStack bow = MonsterItem.BOW_SATET.getItem(500);
                entity.getEquipment().setItemInMainHand(bow);
                livingEntity = entity;
                break;
            }
            case BOSS_LAVA: {
                PigZombie entity = (PigZombie) EntityUtils.create(loc, ChatColor.RED + "Warchief Drogoth", 100000D, EntityType.PIG_ZOMBIE);
                setCustomDamage(entity, 2000);
                entity.setBaby(false);
                ItemStack sword = MonsterItem.AXE_TITAN.getItem(0);
                ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
                ItemStack leg = new ItemStack(Material.DIAMOND_LEGGINGS);
                ItemStack boot = new ItemStack(Material.DIAMOND_BOOTS);
                entity.getEquipment().clear();
                entity.getEquipment().setItemInMainHand(sword);
                entity.getEquipment().setChestplate(chest);
                entity.getEquipment().setLeggings(leg);
                entity.getEquipment().setBoots(boot);
                livingEntity = entity;
                break;
            }
            case BOSS_DARKNESS:
                livingEntity = TUTORIAL_BOSS.getMob(loc);
                break;
        }

        startSkillLoop(livingEntity);

        return livingEntity;
    }
}
