package pl.inder00.dghc.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.inder00.dghc.api.API;
import pl.inder00.dghc.api.basic.Guild;
import pl.inder00.dghc.api.basic.User;
import pl.inder00.dghc.api.basic.utils.GuildUtils;
import pl.inder00.dghc.api.basic.utils.UserUtils;
import pl.inder00.dghc.api.storage.database.MySQL;
import pl.inder00.dghc.guilds.Main;
import pl.inder00.dghc.guilds.utils.Util;

public class DolaczCommand implements CommandExecutor {

    public DolaczCommand(Main main){
        main.getCommand("dolacz").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        User u = UserUtils.get(p.getUniqueId());
        Guild g = u.getGuild();

        if(g != null){
            p.sendMessage(Util.fixColor("&4Blad: &cPosiadasz juz gildie"));
            return false;
        }
        if(u.getInvination() == null){
            p.sendMessage(Util.fixColor("&4Blad: &cNie posiadasz zadnego zaproszenia"));
            return false;
        }
        if(args.length == 0){
            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/dolacz <tag gildii>"));
            return false;
        } else if(args.length == 1){

            String tag = args[0];
            Guild gildia = GuildUtils.getByTag(tag);

            if(gildia == null){
                p.sendMessage(Util.fixColor("&4Blad: &cGildia o podanym tagu nie istnieje"));
                return false;
            }
            if(!u.getInvination().equals(gildia)){
                p.sendMessage(Util.fixColor("&4Blad: &cNie otrzymales zaproszenia od tej gildii"));
                return false;
            }
            if(u.getCoins() < 300){
                p.sendMessage(Util.fixColor("&4Blad: &cAby dolaczyc do gildii potrzebujesz 300 monet!"));
                return false;
            }
            u.setCoins(u.getCoins()-300);
            u.setGuild(gildia);
            u.setInvination(null);
            gildia.getMembers().add(p.getName());
            gildia.updateRank();
            API.getInstance().getMySQLDatabase().updateField(MySQL.Selection.PLAYER, MySQL.Field.UPDATE_GUILDNAME, p.getUniqueId().toString(), gildia.getTag());
            p.sendMessage(Util.fixColor(" &8» &7Dolaczyles do gildii &6"+gildia.getName()+" &8[&6"+gildia.getTag()+"&8]"));
            Bukkit.broadcastMessage(Util.fixColor(" &8» &7Gracz &6"+p.getName()+" &7dolaczyl do gildii &6"+gildia.getName()+" &8[&6"+gildia.getTag()+"&8]"));
            for(User users: UserUtils.getUsers()){
                if(users.getGuild() != null && users.getGuild().equals(gildia)){
                    u.getInvidualTag().update(users);
                }
                users.getInvidualTag().update(u);
            }
            return true;
        } else {
            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/dolacz <tag gildii>"));
            return false;
        }
    }
}
