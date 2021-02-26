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

import java.util.UUID;

public class ZalozCommand implements CommandExecutor {

    public ZalozCommand(Main main) {
        main.getCommand("zaloz").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player p = (Player)  sender;
        User u = UserUtils.get(p.getUniqueId());

        if(u.getGuild() != null){
            p.sendMessage(Util.fixColor("&4Blad: &cPosiadasz juz gildie"));
            return false;
        }
        if(args.length == 0){
            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/zaloz <tag> <nazwa>"));
            return false;
        } else if(args.length == 2){

            String tag = args[0];
            String name = args[1];

            if(tag.length() != 4){
                p.sendMessage(Util.fixColor("&4Blad: &cTag gildii musi miec 4 znaki"));
                return false;
            }
            if(name.length() < 4){
                p.sendMessage(Util.fixColor("&4Blad: &cNazwa gildii musi posiadac przynajmniej 4 znaki"));
                return false;
            }
            if(name.length() > 24){
                p.sendMessage(Util.fixColor("&4Blad: &cNazwa gildii musi posiadac maksymalnie 24 znakow"));
                return false;
            }
            if(!tag.matches("[a-zA-Z0-9]+")){
                p.sendMessage(Util.fixColor("&4Blad: &cTag gildii nie moze zawierac specjalnych znakow"));
                return false;
            }
            if(!name.matches("[a-zA-Z0-9]+")){
                p.sendMessage(Util.fixColor("&4Blad: &cNazwa gildii nie moze zawierac specjalnych znakow"));
                return false;
            }
            if(u.getCoins() < 2500){
                p.sendMessage(Util.fixColor("&4Blad: &cAby zalozyc gildie, potrzebujesz 2500 monet!"));
                return false;
            }
            if(u.getRank() < 1200){
                p.sendMessage(Util.fixColor("&4Blad: &cAby zalozyc gildie, potrzebujesz 1200 rankingu!"));
                return false;
            }
            Guild gName = GuildUtils.get(name);
            if(gName != null){
                p.sendMessage(Util.fixColor("&4Blad: &cGildia o podanej nazwie juz istnieje"));
                return false;
            }
            Guild gTag = GuildUtils.getByTag(tag);
            if(gTag != null){
                p.sendMessage(Util.fixColor("&4Blad: &cGildia o podanym tagu juz istnieje"));
                return false;
            }
            u.setCoins(u.getCoins()-2500);
            UUID guildUUID = UUID.nameUUIDFromBytes(("Guild: "+tag.toUpperCase()+System.currentTimeMillis()).getBytes());
            Guild g = new Guild(guildUUID,tag,name,p.getName());
            u.setGuild(g);
            GuildUtils.insertGuild(g);
            API.getInstance().getMySQLDatabase().updateField(MySQL.Selection.PLAYER, MySQL.Field.UPDATE_GUILDNAME, p.getUniqueId().toString(), g.getTag());
            Bukkit.broadcastMessage(Util.fixColor(" &8» &7Gracz &6"+p.getName()+" &7zalozyl gildie &6"+g.getName()+" &8[&6"+g.getTag()+"&8]"));
            for(User users: UserUtils.getUsers()){
                users.getInvidualTag().update(u);
            }
            return true;
        } else {
            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/zaloz <tag> <nazwa>"));
            return false;
        }
    }
}
