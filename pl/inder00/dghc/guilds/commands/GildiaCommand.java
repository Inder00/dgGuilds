package pl.inder00.dghc.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.inder00.dghc.api.basic.Guild;
import pl.inder00.dghc.api.basic.User;
import pl.inder00.dghc.api.basic.utils.GuildUtils;
import pl.inder00.dghc.api.basic.utils.UserUtils;
import pl.inder00.dghc.guilds.Main;
import pl.inder00.dghc.guilds.utils.Util;

import java.text.SimpleDateFormat;

public class GildiaCommand implements CommandExecutor {

    public GildiaCommand(Main main) {
        main.getCommand("gildia").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        User u = UserUtils.get(p.getUniqueId());

        if(args.length == 0){
            if(u.getGuild() != null){
                Bukkit.dispatchCommand(sender, "gildia "+u.getGuild().getTag());
            } else {
                p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &e/gildia <tag>"));
                return false;
            }
        } else if(args.length == 1){

            String tag = args[0];
            Guild g = GuildUtils.getByTag(tag);

            if(g == null){
                p.sendMessage(Util.fixColor("&4Blad: &cPodana gildia nie istnieje!"));
                return false;
            }
            p.sendMessage(Util.fixColor("§8{§f*§8}§8§m----------§8{ §6STATYSTYKI §8}§8§m----------§8{§f*§8}"));
            p.sendMessage(Util.fixColor("§8§L» §7Tag Gildii: §8[§6§L"+g.getTag()+"§8]"));
            p.sendMessage(Util.fixColor("§8§L» §7Pelna nazwa:: §8[§e"+g.getName()+"§8]"));
            p.sendMessage(Util.fixColor("§8§M----------------------"));
            p.sendMessage(Util.fixColor("§8» §7Zalozyciel: §f§n"+g.getOwner()));
            p.sendMessage(Util.fixColor("§8» §7Zalozono: §c"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(g.getCreated())));
            p.sendMessage(Util.fixColor("§8» §7Ranking: §c"+g.getRank()+" §7(#§c§l"+g.getPosition()+"§7)"));
            p.sendMessage(Util.fixColor("§8» §7Czlonkowie: §c"+g.splitMembers()));
            p.sendMessage(Util.fixColor("§8» §7Pozycja: §c#"+g.getPosition()));
            p.sendMessage(Util.fixColor("§8{§f*§8}§8§m----------§8{ §6STATYSTYKI §8}§8§m----------§8{§f*§8}"));
            return true;

        } else {
            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &e/gildia <tag>"));
            return false;
        }

        return false;
    }
}
