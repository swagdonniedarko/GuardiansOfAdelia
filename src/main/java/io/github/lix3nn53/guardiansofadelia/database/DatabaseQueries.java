package io.github.lix3nn53.guardiansofadelia.database;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.chat.ChatTag;
import io.github.lix3nn53.guardiansofadelia.chat.PremiumRank;
import io.github.lix3nn53.guardiansofadelia.chat.StaffRank;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.PlayerRankInGuild;
import io.github.lix3nn53.guardiansofadelia.jobs.Job;
import io.github.lix3nn53.guardiansofadelia.jobs.JobType;
import io.github.lix3nn53.guardiansofadelia.npc.QuestNPCManager;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.quests.task.Task;
import io.github.lix3nn53.guardiansofadelia.rpginventory.RPGInventory;
import io.github.lix3nn53.guardiansofadelia.utilities.TablistUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DatabaseQueries {

    public static void onDisable() {
        ConnectionPool.closePool();
    }

    public static void createTables() {
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_guild`\n" +
                    "(\n" +
                    " `name`         varchar(20) NOT NULL ,\n" +
                    " `tag`          varchar(5) NOT NULL ,\n" +
                    " `war_point`    smallint ,\n" +
                    " `announcement` tinytext ,\n" +
                    " `hall_level`   smallint NOT NULL ,\n" +
                    " `bank_level`   smallint NOT NULL ,\n" +
                    " `lab_level`    smallint NOT NULL ,\n" +
                    " `storage`      text ,\n" +
                    "PRIMARY KEY (`name`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player`\n" +
                    "(\n" +
                    " `uuid`             varchar(40) NOT NULL ,\n" +
                    " `staff_rank`       varchar(20) ,\n" +
                    " `premium_rank`       varchar(20) ,\n" +
                    " `premium_rank_date`       datetime ,\n" +
                    " `storage_personal` mediumtext ,\n" +
                    " `storage_bazaar`   mediumtext ,\n" +
                    " `storage_premium`   mediumtext ,\n" +
                    "PRIMARY KEY (`uuid`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player_character`\n" +
                    "(\n" +
                    " `character_no`   smallint NOT NULL ,\n" +
                    " `uuid`           varchar(40) NOT NULL ,\n" +
                    " `off_hand`       text NULL ,\n" +
                    " `slot_parrot`    text NULL ,\n" +
                    " `slot_necklace`  text NULL ,\n" +
                    " `slot_ring`      text NULL ,\n" +
                    " `slot_earring`   text NULL ,\n" +
                    " `slot_glove`     text NULL ,\n" +
                    " `slot_pet`       text NULL ,\n" +
                    " `chat_tag`       varchar(45) NULL ,\n" +
                    " `job_type`       varchar(45) NULL ,\n" +
                    " `job_level`      smallint NULL ,\n" +
                    " `job_experience` mediumint NULL ,\n" +
                    " `inventory`      mediumtext NOT NULL ,\n" +
                    " `turnedinquests` text NULL ,\n" +
                    " `activequests`   text NULL ,\n" +
                    " `location`       text NOT NULL ,\n" +
                    " `armor_content`  text NOT NULL ,\n" +
                    " `rpg_class`      varchar(45) NOT NULL ,\n" +
                    " `totalexp`       int NOT NULL ,\n" +
                    " `attr_fire`      smallint NOT NULL ,\n" +
                    " `attr_water`     smallint NOT NULL ,\n" +
                    " `attr_earth`     smallint NOT NULL ,\n" +
                    " `attr_lightning` smallint NOT NULL ,\n" +
                    " `attr_wind`      smallint NOT NULL ,\n" +
                    " `skill_one`      smallint NOT NULL ,\n" +
                    " `skill_two`      smallint NOT NULL ,\n" +
                    " `skill_three`    smallint NOT NULL ,\n" +
                    " `skill_passive`  smallint NOT NULL ,\n" +
                    " `skill_ultimate` smallint NOT NULL ,\n" +
                    "\n" +
                    "UNIQUE KEY `Ind_88` (`uuid`, `character_no`),\n" +
                    "KEY `fkIdx_55` (`uuid`),\n" +
                    "CONSTRAINT `FK_55` FOREIGN KEY `fkIdx_55` (`uuid`) REFERENCES `goa_player` (`uuid`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player_friend`\n" +
                    "(\n" +
                    " `uuid`        varchar(40) NOT NULL ,\n" +
                    " `friend_uuid` varchar(40) NOT NULL ,\n" +
                    "UNIQUE KEY `Ind_89` (`friend_uuid`, `uuid`),\n" +
                    "KEY `fkIdx_22` (`uuid`),\n" +
                    "CONSTRAINT `FK_22` FOREIGN KEY `fkIdx_22` (`uuid`) REFERENCES `goa_player` (`uuid`),\n" +
                    "KEY `fkIdx_25` (`friend_uuid`),\n" +
                    "CONSTRAINT `FK_25` FOREIGN KEY `fkIdx_25` (`friend_uuid`) REFERENCES `goa_player` (`uuid`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player_guild`\n" +
                    "(\n" +
                    " `uuid` varchar(40) NOT NULL ,\n" +
                    " `name` varchar(20) NOT NULL ,\n" +
                    " `rank` varchar(20) NOT NULL ,\n" +
                    "PRIMARY KEY (`uuid`),\n" +
                    "KEY `fkIdx_38` (`uuid`),\n" +
                    "CONSTRAINT `FK_38` FOREIGN KEY `fkIdx_38` (`uuid`) REFERENCES `goa_player` (`uuid`),\n" +
                    "KEY `fkIdx_41` (`name`),\n" +
                    "CONSTRAINT `FK_41` FOREIGN KEY `fkIdx_41` (`name`) REFERENCES `goa_guild` (`name`)\n" +
                    ");");

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //GETTERS

    /* updates tabList of online friends */
    public static List<Player> getFriendsOfPlayer(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player_friend WHERE uuid = ?";
        List<Player> friendList = new ArrayList<>();
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                String friendUuidString = resultSet.getString("friend_uuid");
                Player friend = Bukkit.getPlayer(UUID.fromString(friendUuidString));
                if (friend != null) {
                    friendList.add(friend);
                    if (friend.isOnline()) {
                        TablistUtils.updateTablist(friend);
                    }
                }
            }
            resultSet.close();
            pst.close();
        }
        return friendList;
    }

    public static HashMap<UUID, PlayerRankInGuild> getGuildMembers(String guild) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player_guild WHERE name = ?";
        HashMap<UUID, PlayerRankInGuild> members = new HashMap<>();
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, guild);

            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                String uuidString = resultSet.getString("uuid");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    String rankString = resultSet.getString("rank");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        PlayerRankInGuild rank = PlayerRankInGuild.valueOf(rankString);
                        members.put(UUID.fromString(uuidString), rank);
                    }

                }
            }
            resultSet.close();
            pst.close();
        }
        return members;
    }

    public static String getGuildNameOfPlayer(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player_guild WHERE uuid = ?";
        String guildName = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                guildName = resultSet.getString("name");
            }
            resultSet.close();
            pst.close();
        }
        return guildName;
    }

    public static GuardianData getGuardianDataWithRanksAndStorages(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player WHERE uuid = ?";
        GuardianData guardianData = new GuardianData();
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String staffRankString = resultSet.getString("staff_rank");
                if (!resultSet.wasNull()) {
                    StaffRank staffRank = StaffRank.valueOf(staffRankString);
                    guardianData.setStaffRank(staffRank);
                }

                String premiumRankString = resultSet.getString("premium_rank");
                if (!resultSet.wasNull()) {
                    PremiumRank premiumRank = PremiumRank.valueOf(premiumRankString);
                    guardianData.setPremiumRank(premiumRank);
                }

                String storagePersonalString = resultSet.getString("storage_personal");
                if (!resultSet.wasNull()) {
                    ItemStack[] itemStacks = ItemSerializer.restoreModdedStacks(storagePersonalString);
                    guardianData.setPersonalStorage(itemStacks);
                }

                String storageBazaarString = resultSet.getString("storage_bazaar");
                if (!resultSet.wasNull()) {
                    ItemStack[] itemStacks = ItemSerializer.restoreModdedStacks(storageBazaarString);
                    guardianData.setBazaarStorage(itemStacks);
                }

                String storagePremiumString = resultSet.getString("storage_premium");
                if (!resultSet.wasNull()) {
                    ItemStack[] itemStacks = ItemSerializer.restoreModdedStacks(storagePremiumString);
                    guardianData.setPremiumStorage(itemStacks);
                }
            }
            resultSet.close();
            pst.close();
        }
        return guardianData;
    }

    public static ItemStack[] getPremiumStorage(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT storage_premium FROM goa_player WHERE uuid = ?";
        ItemStack[] itemStacks = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String storagePremiumString = resultSet.getString("storage_premium");
                if (!resultSet.wasNull()) {
                    itemStacks = ItemSerializer.restoreModdedStacks(storagePremiumString);
                }
            }
            resultSet.close();
            pst.close();
        }
        return itemStacks;
    }

    public static RPGCharacter getCharacterAndSetPlayerInventory(Player player, int characterNo) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        RPGCharacter rpgCharacter = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, player.getUniqueId().toString());
            pst.setInt(2, characterNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String rpgClassString = resultSet.getString("rpg_class");
                RPGClass rpgClass = RPGClass.valueOf(rpgClassString);

                int skill_one = resultSet.getInt("skill_one");
                int skill_two = resultSet.getInt("skill_two");
                int skill_three = resultSet.getInt("skill_three");
                int skill_passive = resultSet.getInt("skill_passive");
                int skill_ultimate = resultSet.getInt("skill_ultimate");

                rpgCharacter = new RPGCharacter(rpgClass, player, skill_one, skill_two, skill_three, skill_passive, skill_ultimate);
                RPGInventory rpgInventory = rpgCharacter.getRpgInventory();

                RPGCharacterStats rpgCharacterStats = rpgCharacter.getRpgCharacterStats();

                int totalexp = resultSet.getInt("totalexp");
                rpgCharacterStats.setTotalExp(totalexp);

                int attr_fire = resultSet.getInt("attr_fire");
                rpgCharacterStats.getFire().setInvested(attr_fire, rpgCharacterStats, false);
                int attr_water = resultSet.getInt("attr_water");
                rpgCharacterStats.getWater().setInvested(attr_water, rpgCharacterStats, true);
                int attr_earth = resultSet.getInt("attr_earth");
                rpgCharacterStats.getEarth().setInvested(attr_earth, rpgCharacterStats, true);
                int attr_lightning = resultSet.getInt("attr_lightning");
                rpgCharacterStats.getLightning().setInvested(attr_lightning, rpgCharacterStats, false);
                int attr_wind = resultSet.getInt("attr_wind");
                rpgCharacterStats.getWind().setInvested(attr_wind, rpgCharacterStats, false);

                String offHand = resultSet.getString("off_hand");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    player.getInventory().setItemInOffHand(ItemSerializer.restoreModdedItem(offHand));
                }

                String parrot = resultSet.getString("slot_parrot");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> rpgInventory.setParrot(ItemSerializer.restoreModdedItem(parrot), player));
                }

                String necklace = resultSet.getString("slot_necklace");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> rpgInventory.setNecklace(ItemSerializer.restoreModdedItem(necklace), player));
                }

                String ring = resultSet.getString("slot_ring");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> rpgInventory.setRing(ItemSerializer.restoreModdedItem(ring), player));
                }

                String earring = resultSet.getString("slot_earring");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> rpgInventory.setEarring(ItemSerializer.restoreModdedItem(earring), player));
                }

                String glove = resultSet.getString("slot_glove");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> rpgInventory.setGlove(ItemSerializer.restoreModdedItem(glove), player));
                }

                String pet = resultSet.getString("slot_pet");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> rpgInventory.setEgg(ItemSerializer.restoreModdedItem(pet), player));
                    Bukkit.getScheduler().runTaskLater(GuardiansOfAdelia.getInstance(), () -> PetManager.onEggEquip(player), 40L);
                }

                String jobString = resultSet.getString("job_type");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    JobType jobType = JobType.valueOf(jobString);
                    Job job = new Job(jobType);

                    int jobLevel = resultSet.getInt("job_level");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        job.setLevel(jobLevel);
                    }

                    int jobExperience = resultSet.getInt("job_experience");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        job.setExperience(jobExperience);
                    }

                    rpgCharacter.setJob(job);
                }

                String chatTagString = resultSet.getString("chat_tag");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    rpgCharacter.setChatTag(ChatTag.valueOf(chatTagString));
                }

                String activeQuestsString = resultSet.getString("activequests");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    List<Quest> questList = new ArrayList<>();

                    String[] allQuestsWithTheirTasks = activeQuestsString.split(";");
                    for (String aQuestAndItsTasksString : allQuestsWithTheirTasks) {
                        if (!aQuestAndItsTasksString.equals("")) {
                            String[] aQuestAndItsTasksArray = aQuestAndItsTasksString.split("-");
                            Quest quest = QuestNPCManager.getQuestCopyById(Integer.parseInt(aQuestAndItsTasksArray[0]));
                            List<Task> tasks = quest.getTasks();
                            for (int i = 1; i < aQuestAndItsTasksArray.length; i++) {
                                int taskIndex = i - 1;
                                if (taskIndex < tasks.size()) {
                                    tasks.get(taskIndex).setProgress(Integer.parseInt(aQuestAndItsTasksArray[i]));
                                }
                            }
                            questList.add(quest);
                        }
                    }
                    rpgCharacter.setQuestList(questList);
                }

                String turnedInQuestNumbersArray = resultSet.getString("turnedinquests");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    List<Integer> turnedInQuestNumberList = new ArrayList<>();

                    String[] turnedInQuestNumbersString = turnedInQuestNumbersArray.split(";");
                    for (String aTurnedInQuestNumberString : turnedInQuestNumbersString) {
                        if (!aTurnedInQuestNumberString.equals("")) {
                            turnedInQuestNumberList.add(Integer.parseInt(aTurnedInQuestNumberString));
                        }
                    }

                    rpgCharacter.setTurnedInQuests(turnedInQuestNumberList);
                }

                String inventoryString = resultSet.getString("inventory");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    ItemStack[] itemStacks = ItemSerializer.restoreModdedStacks(inventoryString);
                    player.getInventory().setContents(itemStacks);
                }

                String armorContentString = resultSet.getString("armor_content");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    ItemStack[] itemStacks = ItemSerializer.restoreModdedStacks(armorContentString);
                    player.getInventory().setArmorContents(itemStacks);
                }

                rpgCharacter.getRpgCharacterStats().recalculateEquipment(rpgCharacter.getRpgClass());
                rpgCharacter.getRpgCharacterStats().recalculateRPGInventory(rpgCharacter.getRpgInventory());
            }
            resultSet.close();
            pst.close();
        }
        return rpgCharacter;
    }

    public static Guild getGuild(Player player, String name) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_guild WHERE name = ?";
        Guild guild = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, name);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String tag = resultSet.getString("tag");
                if (!resultSet.wasNull()) {
                    guild = new Guild(name, tag);

                    String announcement = resultSet.getString("announcement");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        guild.setAnnouncement(announcement);
                    }

                    int currentValue = resultSet.getInt("war_point");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        guild.setWarPoints(currentValue);
                    }

                    currentValue = resultSet.getInt("hall_level");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        guild.setHallLevel(currentValue);
                    }

                    currentValue = resultSet.getInt("bank_level");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        guild.setBankLevel(currentValue);
                    }

                    currentValue = resultSet.getInt("lab_level");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        guild.setLabLevel(currentValue);
                    }

                    String storageString = resultSet.getString("storage");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        ItemStack[] itemStacks = ItemSerializer.restoreModdedStacks(storageString);
                        guild.setGuildStorage(itemStacks);
                    }
                }
            }
            resultSet.close();
            pst.close();
        }
        return guild;
    }

    public static Location getLastLocationOfCharacter(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "SELECT location FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        Location location = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String locationString = resultSet.getString("location");
                String[] locationArray = locationString.split(";");
                World world = Bukkit.getWorld(locationArray[0]);
                int x = Integer.parseInt(locationArray[1]);
                int y = Integer.parseInt(locationArray[2]);
                int z = Integer.parseInt(locationArray[3]);
                location = new Location(world, x, y, z);
            }
            resultSet.close();
            pst.close();
        }
        return location;
    }

    public static RPGClass getRPGClassCharacter(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "SELECT rpg_class FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        RPGClass rpgClass = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String rpgClassString = resultSet.getString("rpg_class");
                rpgClass = RPGClass.valueOf(rpgClassString);
            }
            resultSet.close();
            pst.close();
        }
        return rpgClass;
    }

    public static int getTotalExp(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "SELECT totalexp FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        int totalexp = -1;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                totalexp = resultSet.getInt("totalexp");
            }
            resultSet.close();
            pst.close();
        }
        return totalexp;
    }

    //SETTERS

    public static int setGuildOfPlayer(UUID uuid, String guild, PlayerRankInGuild rank) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_player_guild \n" +
                "\t(uuid, name, rank) \n" +
                "VALUES \n" +
                "\t(?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tname = VALUES(name),\n" +
                "\trank = VALUES(rank);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setString(2, guild);
            pst.setString(3, rank.name());

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setPlayerRanksAndStorages(UUID uuid, StaffRank staffRank, PremiumRank premiumRank, ItemStack[] personalStorage, ItemStack[] bazaarStorage, ItemStack[] premiumStorage) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_player \n" +
                "\t(uuid, staff_rank, premium_rank, storage_personal, storage_bazaar, storage_premium) \n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tstaff_rank = VALUES(staff_rank),\n" +
                "\tpremium_rank = VALUES(premium_rank),\n" +
                "\tstorage_personal = VALUES(storage_personal),\n" +
                "\tstorage_bazaar = VALUES(storage_bazaar),\n" +
                "\tstorage_premium = VALUES(storage_premium);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setString(2, staffRank.name());
            pst.setString(3, premiumRank.name());
            String personalStorageString = ItemSerializer.saveModdedStacksData(personalStorage);
            pst.setString(4, personalStorageString);
            String bazaarStorageString = ItemSerializer.saveModdedStacksData(bazaarStorage);
            pst.setString(5, bazaarStorageString);
            String premiumStorageString = ItemSerializer.saveModdedStacksData(premiumStorage);
            pst.setString(6, premiumStorageString);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setPremiumStorage(UUID uuid, ItemStack[] premiumStorage) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_player \n" +
                "\t(uuid, storage_premium) \n" +
                "VALUES \n" +
                "\t(?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tstorage_premium = VALUES(storage_premium);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            String premiumStorageString = ItemSerializer.saveModdedStacksData(premiumStorage);
            pst.setString(2, premiumStorageString);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setPremiumRankWithDate(UUID uuid, PremiumRank premiumRank) throws SQLException {
        LocalDate now = LocalDate.now();
        String currentDateString = now.toString();

        String SQL_QUERY = "INSERT INTO goa_player \n" +
                "\t(uuid, premium_rank, premium_rank_date) \n" +
                "VALUES \n" +
                "\t(?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tpremium_rank = VALUES(premium_rank),\n" +
                "\tpremium_rank_date = VALUES(premium_rank_date);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setString(2, premiumRank.name());
            pst.setString(3, currentDateString);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int clearExpiredPremiumRanks() throws SQLException {
        String SQL_QUERY = ("UPDATE goa_player SET premium_rank=NULL,premium_rank_date=NULL WHERE premium_rank_date < DATE_SUB(NOW(), INTERVAL 1 MONTH) OR premium_rank_date IS NULL");

        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static boolean setFriendsOfPlayer(UUID uuid, List<Player> friendList) {

        return false;
    }

    public static boolean setMembersOfGuild(String guild, HashMap<UUID, PlayerRankInGuild> playerPlayerRankInGuildHashMap) {
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch("DELETE FROM goa_player_guild WHERE name = '" + guild + "'");

            for (UUID player : playerPlayerRankInGuildHashMap.keySet()) {
                statement.addBatch("INSERT INTO goa_player_guild \n" +
                        "\t(uuid, name, rank) \n" +
                        "VALUES \n" +
                        "\t('" + player.toString() + "', '" + guild + "', '" + playerPlayerRankInGuildHashMap.get(player).name() + "')");
            }

            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int setGuild(Guild guild) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_guild \n" +
                "\t(name, tag, war_point, announcement, hall_level, bank_level, lab_level, storage) \n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tname = VALUES(name),\n" +
                "\ttag = VALUES(tag),\n" +
                "\twar_point = VALUES(war_point),\n" +
                "\tannouncement = VALUES(announcement),\n" +
                "\thall_level = VALUES(hall_level),\n" +
                "\tbank_level = VALUES(bank_level),\n" +
                "\tlab_level = VALUES(lab_level),\n" +
                "\tstorage = VALUES(storage);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, guild.getName());
            pst.setString(2, guild.getTag());
            pst.setInt(3, guild.getWarPoints());
            pst.setString(4, guild.getAnnouncement());
            pst.setInt(5, guild.getHallLevel());
            pst.setInt(6, guild.getBankLevel());
            pst.setInt(7, guild.getLabLevel());
            String moddedStacksData = ItemSerializer.saveModdedStacksData(guild.getGuildStorage());
            pst.setString(8, moddedStacksData);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setCharacter(UUID uuid, int charNo, RPGCharacter rpgCharacter, ItemStack[] inventory, Location location, ItemStack[] armorContent, ItemStack offHand) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_player_character \n" +
                "\t(uuid, character_no, off_hand, slot_parrot, slot_necklace, slot_ring, slot_earring, slot_glove, " +
                "slot_pet, chat_tag, job_type, job_level, job_experience, inventory, activequests, turnedinquests, location, armor_content, " +
                "rpg_class, totalexp, attr_fire, attr_water, attr_earth, attr_lightning, attr_wind, skill_one, skill_two, skill_three, skill_passive, skill_ultimate) \n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tcharacter_no = VALUES(character_no),\n" +
                "\toff_hand = VALUES(off_hand),\n" +
                "\tslot_parrot = VALUES(slot_parrot),\n" +
                "\tslot_necklace = VALUES(slot_necklace),\n" +
                "\tslot_ring = VALUES(slot_ring),\n" +
                "\tslot_earring = VALUES(slot_earring),\n" +
                "\tslot_glove = VALUES(slot_glove),\n" +
                "\tslot_pet = VALUES(slot_pet),\n" +
                "\tchat_tag = VALUES(chat_tag),\n" +
                "\tjob_type = VALUES(job_type),\n" +
                "\tjob_level = VALUES(job_level),\n" +
                "\tjob_experience = VALUES(job_experience),\n" +
                "\tinventory = VALUES(inventory),\n" +
                "\tactivequests = VALUES(activequests),\n" +
                "\tturnedinquests = VALUES(turnedinquests),\n" +
                "\tlocation = VALUES(location),\n" +
                "\tarmor_content = VALUES(armor_content),\n" +
                "\trpg_class = VALUES(rpg_class),\n" +
                "\ttotalexp = VALUES(totalexp),\n" +
                "\tattr_fire = VALUES(attr_fire),\n" +
                "\tattr_water = VALUES(attr_water),\n" +
                "\tattr_earth = VALUES(attr_earth),\n" +
                "\tattr_lightning = VALUES(attr_lightning),\n" +
                "\tattr_wind = VALUES(attr_wind),\n" +
                "\tskill_one = VALUES(skill_one),\n" +
                "\tskill_two = VALUES(skill_two),\n" +
                "\tskill_three = VALUES(skill_three),\n" +
                "\tskill_passive = VALUES(skill_passive),\n" +
                "\tskill_ultimate = VALUES(skill_ultimate);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);
            if (offHand != null) {
                String itemString = ItemSerializer.saveModdedItemData(offHand);
                pst.setString(3, itemString);
            } else {
                pst.setNull(3, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getParrotSlot().isEmpty()) {
                String itemString = ItemSerializer.saveModdedItemData(rpgCharacter.getRpgInventory().getParrotSlot().getItemOnSlot());
                pst.setString(4, itemString);
            } else {
                pst.setNull(4, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getNecklaceSlot().isEmpty()) {
                String itemString = ItemSerializer.saveModdedItemData(rpgCharacter.getRpgInventory().getNecklaceSlot().getItemOnSlot());
                pst.setString(5, itemString);
            } else {
                pst.setNull(5, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getRingSlot().isEmpty()) {
                String itemString = ItemSerializer.saveModdedItemData(rpgCharacter.getRpgInventory().getRingSlot().getItemOnSlot());
                pst.setString(6, itemString);
            } else {
                pst.setNull(6, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getEarringSlot().isEmpty()) {
                String itemString = ItemSerializer.saveModdedItemData(rpgCharacter.getRpgInventory().getEarringSlot().getItemOnSlot());
                pst.setString(7, itemString);
            } else {
                pst.setNull(7, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getGloveSlot().isEmpty()) {
                String itemString = ItemSerializer.saveModdedItemData(rpgCharacter.getRpgInventory().getGloveSlot().getItemOnSlot());
                pst.setString(8, itemString);
            } else {
                pst.setNull(8, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getEggSlot().isEmpty()) {
                String itemString = ItemSerializer.saveModdedItemData(rpgCharacter.getRpgInventory().getEggSlot().getItemOnSlot());
                pst.setString(9, itemString);
            } else {
                pst.setNull(9, Types.BLOB);
            }
            pst.setString(10, rpgCharacter.getChatTag().name());
            if (rpgCharacter.hasJob()) {
                Job job = rpgCharacter.getJob();
                pst.setString(11, job.getJobType().name());
                pst.setInt(12, job.getLevel());
                pst.setInt(13, job.getExperience());
            } else {
                pst.setNull(11, Types.BLOB);
                pst.setNull(12, Types.SMALLINT);
                pst.setNull(13, Types.INTEGER);
            }
            if (inventory.length > 0) {
                String moddedStacksData = ItemSerializer.saveModdedStacksData(inventory);
                pst.setString(14, moddedStacksData);
            } else {
                pst.setNull(14, Types.BLOB);
            }
            if (!rpgCharacter.getQuestList().isEmpty()) {
                List<Quest> questList = rpgCharacter.getQuestList();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < questList.size(); i++) {
                    if (i > 0) {
                        stringBuilder.append(";");
                    }
                    Quest quest = questList.get(i);
                    stringBuilder.append(quest.getQuestID());
                    stringBuilder.append("-");
                    List<Task> tasks = quest.getTasks();
                    for (int y = 0; y < tasks.size(); y++) {
                        if (y > 0) {
                            stringBuilder.append("-");
                        }
                        stringBuilder.append(tasks.get(y).getProgress());
                    }
                }
                String string = stringBuilder.toString();
                pst.setString(15, string);
            } else {
                pst.setNull(15, Types.BLOB);
            }
            if (!rpgCharacter.getTurnedInQuests().isEmpty()) {
                List<Integer> turnedInQuestList = rpgCharacter.getTurnedInQuests();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < turnedInQuestList.size(); i++) {
                    if (i > 0) {
                        stringBuilder.append(";");
                    }
                    int questNo = turnedInQuestList.get(i);
                    stringBuilder.append(questNo);
                }
                String string = stringBuilder.toString();
                pst.setString(16, string);
            } else {
                pst.setNull(16, Types.BLOB);
            }
            if (location != null) {
                String locationString = location.getWorld().getName() +
                        ";" +
                        (int) (location.getX() + 0.5) +
                        ";" +
                        (int) (location.getY() + 0.5) +
                        ";" +
                        (int) (location.getZ() + 0.5);
                pst.setString(17, locationString);
            } else {
                pst.setNull(17, Types.BLOB);
            }
            if (armorContent.length > 0) {
                String moddedStacksData = ItemSerializer.saveModdedStacksData(armorContent);
                pst.setString(18, moddedStacksData);
            } else {
                pst.setNull(18, Types.BLOB);
            }

            RPGClass rpgClass = rpgCharacter.getRpgClass();
            pst.setString(19, rpgClass.toString());

            RPGCharacterStats rpgCharacterStats = rpgCharacter.getRpgCharacterStats();

            int totalExp = rpgCharacterStats.getTotalExp();
            pst.setInt(20, totalExp);

            int fire = rpgCharacterStats.getFire().getInvested();
            pst.setInt(21, fire);
            int water = rpgCharacterStats.getWater().getInvested();
            pst.setInt(22, water);
            int earth = rpgCharacterStats.getEarth().getInvested();
            pst.setInt(23, earth);
            int lightning = rpgCharacterStats.getLightning().getInvested();
            pst.setInt(24, lightning);
            int wind = rpgCharacterStats.getWind().getInvested();
            pst.setInt(25, wind);

            SkillBar skillBar = rpgCharacter.getSkillBar();
            int skill_one = skillBar.getInvestedSkillPoints(0);
            pst.setInt(26, skill_one);
            int skill_two = skillBar.getInvestedSkillPoints(1);
            pst.setInt(27, skill_two);
            int skill_three = skillBar.getInvestedSkillPoints(2);
            pst.setInt(28, skill_three);
            int skill_passive = skillBar.getInvestedSkillPoints(3);
            pst.setInt(29, skill_passive);
            int skill_ultimate = skillBar.getInvestedSkillPoints(4);
            pst.setInt(30, skill_ultimate);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    //CLEANERS

    public static void clearGuild(String guild) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_guild WHERE name = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, guild);
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearMembersOfGuild(String guild) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_guild WHERE name = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, guild);
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearGuildOfPlayer(UUID uuid) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_guild WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearPlayerRanksAndStorages(UUID uuid) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearFriendsOfPlayer(UUID uuid) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_friend WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearCharacter(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);
            pst.executeUpdate();
            pst.close();
        }
    }

    //BOOLEAN

    public static boolean characterExists(UUID uuid, int charNo) {
        boolean charExists = false;
        String SQL_QUERY = "SELECT inventory FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                //row exists
                charExists = true;
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return charExists;
    }

    public static boolean uuidExists(UUID uuid) {
        boolean exists = false;
        String SQL_QUERY = "SELECT uuid FROM goa_player WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                //row exists
                exists = true;
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
