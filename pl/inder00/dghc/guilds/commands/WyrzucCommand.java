package pl.inder00.dghc.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

public class WyrzucCommand implements CommandExecutor {

    public WyrzucCommand(Main main){
        main.getCommand("wyrzuc").setExecutor(this);
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
        if(!p.getName().equalsIgnoreCase(g.getOwner())){
            p.sendMessage(Util.fixColor("&4Blad: &cNie jestes liderem gildii"));
            return false;
        }
        if(args.length == 0){

            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/wyrzuc <nick>"));
            return false;

        } else if(args.length == 1){

            String name = args[0];
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            if(name.equalsIgnoreCase(p.getName())){
                p.sendMessage(Util.fixColor("&4Blad: &cNie mozesz siebie wyrzucic"));
                return false;
            }
            User mate = UserUtils.getTotal(offlinePlayer.getUniqueId());
            if(mate == null){
                p.sendMessage(Util.fixColor("&4Blad: &cTen gracz nigdy nie przebywal na serwerze"));
                return false;
            }
            if(mate.getGuild() == null){
                p.sendMessage(Util.fixColor("&4Blad: &cPodany gracz nie jest w Twojej gildii"));
                return false;
            }
            if(!mate.getGuild().equals(g)){
                p.sendMessage(Util.fixColor("&4Blad: &cPodany gracz nie jest w Twojej gildii"));
                return false;
            }
            g.getMembers().remove(mate.getName());
            g.updateRank();
            mate.setGuild(null);
            API.getInstance().getMySQLDatabase().updateField(MySQL.Selection.PLAYER, MySQL.Field.UPDATE_GUILDNAME, mate.getUUID().toString(), null);
            API.getInstance().getMySQLDatabase().updateField(MySQL.Selection.GUILD, MySQL.Field.UPDATE_MEMBERS, g.getUUID().toString(), g.splitMembers());
            Bukkit.broadcastMessage(Util.fixColor(" &8» &7Gracz &6"+mate.getName()+" &7zostal wyrzucony z gildii &6"+g.getName()+" &8[&6"+g.getTag()+"&8]"));
            for(User users: UserUtils.getUsers()){
                u.getInvidualTag().update(users);
                users.getInvidualTag().update(u);
            }
            return false;

        } else {

            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/wyrzuc <nick>"));
            return false;

        }
    }
}
