package pl.inder00.dghc.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.inder00.dghc.api.API;
import pl.inder00.dghc.api.basic.Guild;
import pl.inder00.dghc.api.basic.User;
import pl.inder00.dghc.api.basic.utils.UserUtils;
import pl.inder00.dghc.api.storage.database.MySQL;
import pl.inder00.dghc.guilds.Main;
import pl.inder00.dghc.guilds.utils.Util;

public class OpuscCommand implements CommandExecutor {

    public OpuscCommand(Main main) {

        main.getCommand("opusc").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        Player p = (Player) sender;
        User u = UserUtils.get(p.getUniqueId());
        Guild g = u.getGuild();

        if(g == null){
            p.sendMessage(Util.fixColor("&4Blad: &cNie posiadasz gildii"));
            return false;
        }
        if(p.getName().equalsIgnoreCase(g.getOwner())){
            p.sendMessage(Util.fixColor("&4Blad: &cLider nie moze opuscic gildii"));
            return false;
        }
        u.setGuild(null);
        g.getMembers().remove(p.getName());
        g.updateRank();
        Bukkit.broadcastMessage(Util.fixColor(" &8» &7Gracz &e"+p.getName()+" &7opuscil gildie &6"+g.getName()+" &8[&6"+g.getTag()+"&8]"));
        API.getInstance().getMySQLDatabase().updateField(MySQL.Selection.PLAYER, MySQL.Field.UPDATE_GUILDNAME, p.getUniqueId().toString(), null);
        API.getInstance().getMySQLDatabase().updateField(MySQL.Selection.GUILD, MySQL.Field.UPDATE_MEMBERS, g.getUUID().toString(), g.splitMembers());
        for(User users: UserUtils.getUsers()){
            u.getInvidualTag().update(users);
            users.getInvidualTag().update(u);
        }
        return false;
    }
}
