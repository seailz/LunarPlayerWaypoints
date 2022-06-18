package com.seailz.playerpoints.commands;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.seailz.playerpoints.core.Locale;
import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import games.negative.framework.inputlistener.InputListener;
import games.negative.framework.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "waypoint",
        description = "Sets a waypoint for you to travel to",
        playerOnly = true,
        args = {"player", "name"}
)
public class CommandWaypoint extends Command                    {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (!LunarClientAPI.getInstance().isRunningLunarClient(p)) {
            Locale.NOT_ON_LUNAR.send(p);
            return;
        }

        Player target = getPlayer(args[0]);
        if (target == null) {
            Locale.INVALID_PLAYER.send(p);
            return;
        }

        new Message("&a" + p.getName() + " is trying to set a waypoint at your location, do you accept?").send(target);
        new Message("Type &ayes&f or &cno&f").send(target);
        InputListener.listen(target.getUniqueId(), (event) -> {
            if (event.getMessage().equals("yes")) {
                LCWaypoint waypoint = new LCWaypoint(args[1], target.getLocation(), Color.AQUA.asRGB(), true, true);
                LunarClientAPI.getInstance().sendWaypoint(p, waypoint);

                Locale.WAYPOINT_SET.send(p);
                new Message("&aWaypoint set!").send(target);
            } else {
                new Message("&cYou have declined the waypoint request").send(target);
                new Message("&c" + p.getName() + " has declined your request").send(p);
            }
        });
    }
}
