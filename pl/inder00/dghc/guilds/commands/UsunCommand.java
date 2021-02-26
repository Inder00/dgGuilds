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

public class UsunCommand implements CommandExecutor {

    public UsunCommand(Main main){
        main.getCommand("usun").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;

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

            p.sendMessage(Util.fixColor(" &8» &7Wpisz &6/usun potwierdz&7, aby potwierdzic usuwanie gildii!"));
            p.sendMessage(Util.fixColor(" &8» &7Ta czynnosc jest nieodwracalna, wiec zastanow sie dwa razy!"));
            return false;

        } else if(args.length > 0){

            String method = args[0];
            if(method.equalsIgnoreCase("potwierdz")){

                for(String mate : g.getMembers()){
                    User gMate = UserUtils.getTotal(mate);
                    gMate.setGuild(null);
                    API.getInstance().getMySQLDatabase().updateField(MySQL.Selection.PLAYER, MySQL.Field.UPDATE_GUILDNAME, gMate.getUUID().toString(), null);
                    if(Bukkit.getPlayer(gMate.getUUID()) != null){
                        for(User users: UserUtils.getUsers()){
                            gMate.getInvidualTag().update(users);
                            users.getInvidualTag().update(gMate);
                        }
                    }
                }
                GuildUtils.removeGuild(g);
                GuildUtils.remove(g,false);
                Bukkit.broadcastMessage(Util.fixColor(" &8» &7Gracz &e"+p.getName()+" &7usunal gildie &6"+g.getName()+" &8[&6"+g.getTag()+"&8]"));
                for(User users: UserUtils.getUsers()){
                    u.getInvidualTag().update(users);
                    users.getInvidualTag().update(u);
                }
                return true;

            } else {
                p.sendMessage(Util.fixColor(" &8» &7Wpisz &6/usun potwierdz&7, aby potwierdzic usuwanie gildii!"));
                p.sendMessage(Util.fixColor(" &8» &7Ta czynnosc jest nieodwracalna, wiec zastanow sie dwa razy!"));
                return false;
            }

        }

        return false;
    }
}
