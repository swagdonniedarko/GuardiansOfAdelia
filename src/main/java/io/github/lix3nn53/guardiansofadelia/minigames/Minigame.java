package io.github.lix3nn53.guardiansofadelia.minigames;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.party.Party;
import io.github.lix3nn53.guardiansofadelia.party.PartyManager;
import io.github.lix3nn53.guardiansofadelia.utilities.Scoreboard.BoardWithPlayers;
import io.github.lix3nn53.guardiansofadelia.utilities.centermessage.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Minigame {

    private final String gameTypeName;
    private final ChatColor gameColor;
    private final String mapName;
    private final HashMap<Integer, Integer> teamToScore = new HashMap<>();
    private final List<Location> startLocations;
    private final Location backLocation;
    private final int levelReq;
    private final int roomNo;
    private final int queueTimeLimitInMinutes;
    private final int timeLimitInMinutes;
    private final HashMap<Integer, Party> teams = new HashMap<>();
    private final int teamSize;

    private final int teamAmount;
    private final int maxLives;
    private final int minTeamsAlive;
    private final HashMap<Integer, Integer> teamDeathCount = new HashMap<>();
    private final int respawnDelayInSeconds;
    private final int requiredPlayerAmountToStart;
    private BukkitRunnable gameCountDown;
    private BukkitRunnable queueCountDown;
    private boolean isInGame = false;

    public Minigame(String gameTypeName, ChatColor gameColor, String mapName, int roomNo, int levelReq, int teamSize, int teamAmount, List<Location> startLocations, int timeLimitInMinutes,
                    int queueTimeLimitInMinutes, Location backLocation, int maxLives, int minTeamsAlive, int respawnDelayInSeconds, int requiredPlayerAmountToStart) {
        this.gameTypeName = gameTypeName;
        this.gameColor = gameColor;
        this.mapName = mapName;
        this.backLocation = backLocation;
        this.levelReq = levelReq;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.startLocations = startLocations;
        this.roomNo = roomNo;
        this.queueTimeLimitInMinutes = queueTimeLimitInMinutes;
        this.teamSize = teamSize;
        this.teamAmount = teamAmount;
        this.maxLives = maxLives;
        this.minTeamsAlive = minTeamsAlive;
        this.respawnDelayInSeconds = respawnDelayInSeconds;
        this.requiredPlayerAmountToStart = requiredPlayerAmountToStart;

        this.gameCountDown = new BukkitRunnable() {
            @Override
            public void run() {
                cancel();
            }
        };
        gameCountDown.runTaskTimer(GuardiansOfAdelia.getInstance(), 5L, 20L);

        for (int i = 1; i <= teamAmount; i++) {
            BoardWithPlayers boardWithPlayers = new BoardWithPlayers(new ArrayList<>(), gameTypeName, getScoreboardTopLines(), getTeamTextColor(i));
            teams.put(i, new Party(new ArrayList<>(), teamSize, 1, boardWithPlayers));
            teamToScore.put(i, 0);
        }
    }

    public void reformParties() {
        for (int teamNo : teams.keySet()) {
            Party party = teams.get(teamNo);
            for (Player member : party.getMembers()) {
                PartyManager.removeMember(member);
            }
        }
        teams.clear();
        for (int i = 1; i <= teamAmount; i++) {
            BoardWithPlayers boardWithPlayers = new BoardWithPlayers(new ArrayList<>(), gameTypeName, getScoreboardTopLines(), getTeamTextColor(i));
            Party party = new Party(new ArrayList<>(), teamSize, 1, boardWithPlayers);
            teams.put(i, party);
            teamToScore.put(i, 0);
        }
    }

    public int getLevelReq() {
        return levelReq;
    }

    public HashMap<Integer, Party> getTeams() {
        return teams;
    }

    public BukkitRunnable getGameCountDown() {
        return gameCountDown;
    }

    public int getTimeLimitInMinutes() {
        return timeLimitInMinutes;
    }

    public void startGame() {
        if (!getPlayersInGame().isEmpty()) {
            isInGame = true;

            for (int teamNo : teams.keySet()) {
                Party party = teams.get(teamNo);
                for (Player member : party.getMembers()) {
                    if (member.isOnline()) {
                        member.teleport(startLocations.get(teamNo - 1));
                    }
                }
            }

            this.gameCountDown = new BukkitRunnable() {

                int secondsPass = 0;

                @Override
                public void run() {
                    if (secondsPass == timeLimitInMinutes * 60) {
                        //end minigame
                        endGame();
                    } else {
                        updateTimeOnScoreBoards(timeLimitInMinutes * 60 - secondsPass);
                        onGameTick();
                        secondsPass++;
                    }
                }
            };
            gameCountDown.runTaskTimer(GuardiansOfAdelia.getInstance(), 5L, 20L);
        }
    }

    public void onGameTick() {

    }

    public void endGame() {
        if (!gameCountDown.isCancelled()) {
            gameCountDown.cancel();
            List<Integer> winnerTeams = getWinnerTeams();
            for (Integer teamNo : teams.keySet()) {
                Party party = teams.get(teamNo);
                for (Player member : party.getMembers()) {
                    if (member.isOnline()) {
                        MessageUtils.sendCenteredMessage(member, ChatColor.GRAY + "------------------------");
                        if (winnerTeams.contains(teamNo)) {
                            if (winnerTeams.size() == 1) {
                                member.sendTitle(ChatColor.GREEN + "Congratulations!", ChatColor.YELLOW + "", 30, 80, 30);
                                MessageUtils.sendCenteredMessage(member, "You have have won the " + ChatColor.GREEN + getMinigameName() + " !");
                            } else {
                                member.sendTitle(ChatColor.GREEN + "Tie!", ChatColor.YELLOW + "", 30, 80, 30);
                                MessageUtils.sendCenteredMessage(member, "You are sharing first place with another team in " + ChatColor.GREEN + getMinigameName());
                            }
                        } else {
                            member.sendTitle(ChatColor.RED + "Failed..", ChatColor.YELLOW + "", 30, 80, 30);
                            MessageUtils.sendCenteredMessage(member, "You lose the " + ChatColor.GREEN + getMinigameName());
                        }
                        MessageUtils.sendCenteredMessage(member, ChatColor.GRAY + "------------------------");
                        member.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }

            new BukkitRunnable() {

                int count = 0;
                List<Player> playersInGame = getPlayersInGame();

                @Override
                public void run() {
                    if (count == 3) {
                        cancel();

                        if (isInGame) {
                            for (Player member : playersInGame) {
                                MiniGameManager.removePlayer(member);
                                member.teleport(backLocation);
                                member.setGameMode(GameMode.ADVENTURE);
                                if (PartyManager.inParty(member)) {
                                    Party party = PartyManager.getParty(member);
                                    party.leave(member);
                                }
                            }

                            isInGame = false;
                            teams.clear();
                            teamToScore.clear();
                            for (int i = 1; i <= teamAmount; i++) {
                                BoardWithPlayers boardWithPlayers = new BoardWithPlayers(new ArrayList<>(), gameTypeName, getScoreboardTopLines(), getTeamTextColor(i));
                                teams.put(i, new Party(new ArrayList<>(), teamSize, 1, boardWithPlayers));
                                teamToScore.put(i, 0);
                            }
                            teamDeathCount.clear();
                            gameCountDown.cancel();

                            createNormalPartyAfterMinigame(playersInGame);
                        }
                    } else {
                        for (Player member : playersInGame) {
                            if (member.isOnline()) {
                                MessageUtils.sendCenteredMessage(member, getGameColor() + "You will be teleported in " + (15 - (count * 5)) + " seconds");
                            }
                        }
                        count++;
                    }
                }
            }.runTaskTimer(GuardiansOfAdelia.getInstance(), 5L, 20 * 5L);
        }
    }

    private void createNormalPartyAfterMinigame(List<Player> players) {
        if (players.size() > 1 && players.size() <= 4) {
            Party party = new Party(players, 4, 2, ChatColor.AQUA);

            PartyManager.addParty(players, party);
        }
    }

    public ChatColor getGameColor() {
        return gameColor;
    }

    public int getQueueTimeLimitInMinutes() {
        return queueTimeLimitInMinutes;
    }

    public BukkitRunnable getQueueCountDown() {
        return queueCountDown;
    }

    public void setQueueCountDown(BukkitRunnable queueCountDown) {
        this.queueCountDown = queueCountDown;
    }

    public boolean addPlayerNoCheck(Player player) {
        if (PartyManager.inParty(player)) {
            Party party = PartyManager.getParty(player);
            party.leave(player);
        }
        for (Integer team : teams.keySet()) {
            Party party = teams.get(team);
            if (party.getMembers().size() < this.teamSize) {
                party.addMember(player);
                teams.put(team, party);
                return true;
            }
        }
        return false;
    }

    private void removePlayer(Player player) {
        for (Integer team : teams.keySet()) {
            Party party = teams.get(team);
            if (party.getMembers().contains(player)) {
                party.leave(player);
                teams.put(team, party);
                break;
            }
        }
    }

    public boolean joinQueue(Player player) {
        if (PartyManager.inParty(player)) {
            Party party = PartyManager.getParty(player);
            List<Player> partyMembers = party.getMembers();
            boolean canPartyJoin = canPartyJoin(partyMembers);
            if (canPartyJoin) {
                party.destroy();

                for (Player partyMember : partyMembers) {
                    addPlayerNoCheck(partyMember);
                    for (Player member : getPlayersInGame()) {
                        MessageUtils.sendCenteredMessage(member, partyMember.getName() + getGameColor() + " joined queue for " + getMinigameName());
                    }
                    MiniGameManager.addPlayer(partyMember, this);
                    onPlayerJoinQueueCountdownCheck();
                }

                return true;
            }

            return false;
        }

        return addPlayerWithChecks(player);
    }

    private boolean canPartyJoin(List<Player> partyMembers) {
        if (!this.isInGame) {
            int emptySize = getEmptySize();
            if (partyMembers.size() > emptySize) {
                partyMembers.get(0).sendMessage(ChatColor.RED + "There is not enough space for your party");
                return false;
            }
            for (Player player : partyMembers) {
                if (!player.getWorld().getName().equals("world")) {
                    player.sendMessage(ChatColor.RED + "You must be in normal world");
                    return false;
                }
                if (!MiniGameManager.isInMinigame(player)) {
                    if (!getPlayersInGame().contains(player) && getPlayersInGame().size() < this.teamAmount * this.teamSize) {
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You are already in a minigame");
                    return false;
                }
            }
        }
        return false;
    }

    private boolean addPlayerWithChecks(Player player) {
        if (!this.isInGame) {
            if (!player.getWorld().getName().equals("world")) {
                player.sendMessage(ChatColor.RED + "You must be in normal world");
                return false;
            }
            if (!MiniGameManager.isInMinigame(player)) {
                if (!getPlayersInGame().contains(player) && getPlayersInGame().size() < this.teamAmount * this.teamSize) {
                    addPlayerNoCheck(player);
                    for (Player member : getPlayersInGame()) {
                        MessageUtils.sendCenteredMessage(member, player.getName() + getGameColor() + " joined queue for " + getMinigameName());
                    }
                    MiniGameManager.addPlayer(player, this);
                    onPlayerJoinQueueCountdownCheck();
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are already in a minigame");
                return false;
            }
        }

        return false;
    }

    public void onPlayerJoinQueueCountdownCheck() {
        if (this.requiredPlayerAmountToStart == getPlayersInGame().size()) {
            for (Player member : getPlayersInGame()) {
                MessageUtils.sendCenteredMessage(member, getGameColor() + "Countdown for " + getMinigameName() + " has started");
            }
            //start countdown
            this.queueCountDown = new BukkitRunnable() {

                int count = 0;

                @Override
                public void run() {
                    if (count * 10 == queueTimeLimitInMinutes * 60) {
                        //start minigame
                        cancel();
                        startGame();
                    } else {
                        for (Player member : getPlayersInGame()) {
                            if (member.isOnline()) {
                                MessageUtils.sendCenteredMessage(member, getGameColor().toString() + (queueTimeLimitInMinutes * 60 - (10 * count)) + " seconds left until " + getMinigameName() + " starts");
                            }
                        }
                        count++;
                    }
                }
            };
            this.queueCountDown.runTaskTimer(GuardiansOfAdelia.getInstance(), 1L, 20 * 10L);
        }
    }

    private void onPlayerLeaveQueueCountdownCheck() {
        if (this.requiredPlayerAmountToStart > getPlayersInGame().size()) {
            if (this.queueCountDown != null) {
                if (!this.queueCountDown.isCancelled()) {
                    for (Player member : getPlayersInGame()) {
                        MessageUtils.sendCenteredMessage(member, ChatColor.RED + "Countdown for " + getMinigameName() + " is canceled");
                    }
                    this.queueCountDown.cancel();
                }
            }
        }
    }

    public int getTeamSize() {
        return teamSize;
    }

    public int getTeamAmount() {
        return teamAmount;
    }

    public String getMinigameName() {
        return this.gameColor + this.gameTypeName + " #" + getRoomNo();
    }

    public Location getStartLocation(int teamNo) {
        return startLocations.get(teamNo - 1);
    }

    public boolean isInGame() {
        return isInGame;
    }

    public boolean isFull() {
        return getPlayersInGame().size() >= this.teamAmount * this.teamSize;
    }

    public int getMaxPlayerSize() {
        return this.teamAmount * this.teamSize;
    }

    public int getEmptySize() {
        int playersInGameAmount = getPlayersInGame().size();
        int maxPlayerSize = getMaxPlayerSize();
        return maxPlayerSize - playersInGameAmount;
    }

    public int getPlayersInGameSize() {
        return getPlayersInGame().size();
    }

    public void leave(Player player) {
        if (player.isOnline()) {
            if (!player.getLocation().getWorld().getName().equals("world")) {
                player.teleport(this.backLocation);
            }
            MessageUtils.sendCenteredMessage(player, ChatColor.RED + "You have left " + getMinigameName());
        }

        int teamOfPlayer = getTeamOfPlayer(player);

        removePlayer(player);

        if (getTeams().get(teamOfPlayer).getMembers().isEmpty()) {
            setLivesOfTeam(teamOfPlayer, 0);
        }

        if (getAliveTeams().size() == minTeamsAlive) {
            endGame();
        }

        MiniGameManager.removePlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        onPlayerLeaveQueueCountdownCheck();
        if (getPlayersInGame().isEmpty()) {
            endGame();
        }
    }

    public List<Integer> getWinnerTeams() {
        List<Integer> teamsAtBestScore = new ArrayList<>();
        int bestScore = 0;
        for (int team : getTeams().keySet()) {
            int teamScore = getScoreOfTeam(team);
            if (teamScore > bestScore) {
                bestScore = teamScore;
            }
        }
        for (int team : getTeams().keySet()) {
            int teamScore = getScoreOfTeam(team);
            if (teamScore == bestScore) {
                teamsAtBestScore.add(team);
            }
        }
        return teamsAtBestScore;
    }

    public int getScoreOfTeam(int teamNo) {
        return teamToScore.get(teamNo);
    }

    public List<Location> getStartLocations() {
        return startLocations;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public List<Player> getPlayersInGame() {
        List<Player> playersInGame = new ArrayList<>();
        for (Integer teamNo : teams.keySet()) {
            Party party = teams.get(teamNo);
            playersInGame.addAll(party.getMembers());
        }
        return playersInGame;
    }

    public void addScore(Player player, int scoreToAdd) {
        int teamNo = getTeamOfPlayer(player);
        if (teamToScore.containsKey(teamNo)) {
            Integer score = teamToScore.get(teamNo);
            score += scoreToAdd;
            teamToScore.put(teamNo, score);
            updateTeamScoresOnScoreBoard(teamNo, score);
        }
    }

    public void addScore(int teamNo, int scoreToAdd) {
        if (teamToScore.containsKey(teamNo)) {
            Integer score = teamToScore.get(teamNo);
            score += scoreToAdd;
            teamToScore.put(teamNo, score);
            updateTeamScoresOnScoreBoard(teamNo, score);
        }
    }

    public HashMap<Integer, Integer> getTeamToScore() {
        return teamToScore;
    }

    public List<String> getScoreboardTopLines() {
        List<String> topLines = new ArrayList<>();
        topLines.add("Time remaining: " + this.timeLimitInMinutes * 60);
        for (int i = 0; i < teamAmount; i++) {
            topLines.add(getTeamTextColor(i + 1) + "Team" + (i + 1) + " score: " + getScoreOfTeam(i + 1));
        }
        return topLines;
    }

    public void updateTeamScoresOnScoreBoard(int teamNoToChange, int newScore) {
        for (Integer teamNo : teams.keySet()) {
            Party party = teams.get(teamNo);
            if (!party.getMembers().isEmpty()) {
                BoardWithPlayers board = party.getBoard();
                for (int k : board.getRowLines().keySet()) {
                    String s = board.getRowLines().get(k);
                    if (s.contains("Team" + teamNoToChange + " score: ")) {
                        board.setLine(getTeamTextColor(teamNoToChange) + "Team" + teamNoToChange + " score: " + newScore, k);
                        break;
                    }
                }
            }
        }
    }

    public void updateTimeOnScoreBoards(int seconds) {
        for (Integer teamNo : teams.keySet()) {
            Party party = teams.get(teamNo);
            if (!party.getMembers().isEmpty()) {
                BoardWithPlayers board = party.getBoard();
                for (int k : board.getRowLines().keySet()) {
                    String s = board.getRowLines().get(k);
                    if (s.contains("Time remaining: ")) {
                        board.setLine("Time remaining: " + seconds, k);
                        break;
                    }
                }
            }
        }
    }

    public String getGameTypeName() {
        return gameTypeName;
    }

    private void updateTeamLivesOnScoreBoard(int teamNoToChange, int teamLives) {
        for (Integer teamNo : getTeams().keySet()) {
            Party party = getTeams().get(teamNo);
            if (!party.getMembers().isEmpty()) {
                BoardWithPlayers board = party.getBoard();
                for (int k : board.getRowLines().keySet()) {
                    String s = board.getRowLines().get(k);
                    if (s.contains("Team" + teamNoToChange + " lives: ")) {
                        board.setLine(getTeamTextColor(teamNoToChange) + "Team" + teamNoToChange + " lives: " + teamLives, k);
                        break;
                    }
                }
            }
        }
    }

    private Location getStartWatchLocation(Player player, int teamOfPlayer) {
        Location startWatchLocation = getStartLocation(teamOfPlayer);
        if (this.teamSize > 1) {
            Party party = teams.get(teamOfPlayer);
            for (Player member : party.getMembers()) {
                if (!member.getUniqueId().equals(player.getUniqueId())) {
                    startWatchLocation = member.getLocation();
                    break;
                }
            }
        }
        return startWatchLocation;
    }

    public void onPlayerKill(Player killer) {
        addScore(killer, 1);
    }

    public Material getTeamWool(int teamNo) {
        if (teamNo == 1) {
            return Material.LIGHT_BLUE_WOOL;
        } else if (teamNo == 2) {
            return Material.RED_WOOL;
        } else if (teamNo == 3) {
            return Material.YELLOW_WOOL;
        } else if (teamNo == 4) {
            return Material.LIME_WOOL;
        }
        return Material.WHITE_WOOL;
    }

    public ChatColor getTeamTextColor(int teamNo) {
        if (teamNo == 1) {
            return ChatColor.AQUA;
        } else if (teamNo == 2) {
            return ChatColor.RED;
        } else if (teamNo == 3) {
            return ChatColor.YELLOW;
        } else if (teamNo == 4) {
            return ChatColor.GREEN;
        }
        return ChatColor.WHITE;
    }

    public void onPlayerDeath(Player player) {
        int teamOfPlayer = getTeamOfPlayer(player);
        Location startWatchLocation = getStartWatchLocation(player, teamOfPlayer);
        if (maxLives > 1) {
            int deathCount = 0;
            if (teamDeathCount.containsKey(teamOfPlayer)) {
                deathCount = teamDeathCount.get(teamOfPlayer);
            }
            deathCount++;
            teamDeathCount.put(teamOfPlayer, deathCount);
            updateTeamLivesOnScoreBoard(teamOfPlayer, getLivesOfTeam(teamOfPlayer));
            if (deathCount >= this.maxLives) {
                fail(teamOfPlayer, startWatchLocation);
            } else {
                player.setGameMode(GameMode.SPECTATOR);
                //respawn countdown
                new BukkitRunnable() {

                    int count = 0;

                    @Override
                    public void run() {
                        if (count == respawnDelayInSeconds) {
                            //respawn
                            for (Integer teamNo : teams.keySet()) {
                                Party party = teams.get(teamNo);
                                if (party.getMembers().contains(player)) {
                                    player.teleport(startLocations.get(teamNo - 1));
                                    player.setGameMode(GameMode.ADVENTURE);
                                    break;
                                }
                            }
                            cancel();
                        } else {
                            if (count == 0) {
                                player.teleport(startWatchLocation);
                            }
                            player.sendTitle(ChatColor.DARK_PURPLE + "Respawn in", ChatColor.LIGHT_PURPLE.toString() + (respawnDelayInSeconds - count) + " seconds", 0, 20, 0);
                            count++;
                        }
                    }
                }.runTaskTimer(GuardiansOfAdelia.getInstance(), 1L, 20L);
            }
        } else {
            fail(teamOfPlayer, startWatchLocation);
        }
    }

    public void fail(int teamNo, Location startWatchLocation) {
        for (Player player : teams.get(teamNo).getMembers()) {
            player.setGameMode(GameMode.SPECTATOR);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(startWatchLocation);
                }
            }.runTaskLater(GuardiansOfAdelia.getInstance(), 5L);
        }
        //check game end
        if (getAliveTeams().size() == minTeamsAlive) {
            endGame();
        }
    }

    public int getLivesOfTeam(int teamNo) {
        int deathCount = 0;
        if (teamDeathCount.containsKey(teamNo)) {
            deathCount = teamDeathCount.get(teamNo);
        }
        return maxLives - deathCount;
    }

    public int getTeamOfPlayer(Player player) {
        for (Integer teamNo : teams.keySet()) {
            Party party = teams.get(teamNo);
            if (party.getMembers().contains(player)) {
                return teamNo;
            }
        }
        return -1;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public List<Integer> getAliveTeams() {
        List<Integer> aliveTeams = new ArrayList<>();
        for (Integer teamNo : teams.keySet()) {
            int livesOfTeam = getLivesOfTeam(teamNo);
            if (livesOfTeam > 0) {
                aliveTeams.add(teamNo);
            }
        }
        return aliveTeams;
    }

    public void setLivesOfTeam(int teamNo, int lives) {
        int deathCount = this.maxLives - lives;
        if (deathCount < 0) {
            deathCount = 0;
        }
        teamDeathCount.put(teamNo, deathCount);
        updateTeamLivesOnScoreBoard(teamNo, lives);
    }

    public String getMapName() {
        return mapName;
    }

    public int getNonEmptyTeamAmount() {
        int i = 0;
        for (int teamNo : teams.keySet()) {
            if (!teams.get(teamNo).getMembers().isEmpty()) {
                i++;
            }
        }
        return i;
    }
}
