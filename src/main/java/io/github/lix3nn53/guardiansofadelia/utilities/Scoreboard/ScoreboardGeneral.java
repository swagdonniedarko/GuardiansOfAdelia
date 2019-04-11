package io.github.lix3nn53.guardiansofadelia.utilities.Scoreboard;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ScoreboardGeneral implements MyScoreboard {

    Scoreboard board;
    List<Player> registeredPlayers = new ArrayList<>();
    HashMap<Integer, String> rowLines = new HashMap<>();

    public ScoreboardGeneral(String displayName) {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("GoA", "dummy", displayName);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setLine(String text, int row) {
        if (row <= 15 && row > 0) {
            removeLine(row);
            Objective obj = board.getObjective("GoA");
            Score score = obj.getScore(text);
            score.setScore(15 - row);
            rowLines.put(row, text);
        }
    }

    public void removeLine(int row) {
        if (row <= 15 && row > 0) {
            if (rowLines.containsKey(row)) {
                board.resetScores(rowLines.get(row));
                rowLines.remove(row);
            }
        }
    }

    public void show(Player p) {
        p.setScoreboard(board);
        registeredPlayers.add(p);
    }

    public void hide(Player p) {
        if (registeredPlayers.contains(p)) {
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            registeredPlayers.remove(p);
        }
    }

    public HashMap<Integer, String> getRowLines() {
        return rowLines;
    }
}
