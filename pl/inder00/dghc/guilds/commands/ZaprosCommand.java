package pl.inder00.dghc.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.inder00.dghc.api.basic.Guild;
import pl.inder00.dghc.api.basic.User;
import pl.inder00.dghc.api.basic.utils.UserUtils;
import pl.inder00.dghc.guilds.Main;
import pl.inder00.dghc.guilds.utils.Util;

public class ZaprosCommand implements CommandExecutor {

    public ZaprosCommand(Main main){
        main.getCommand("zapros").setExecutor(this);
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
            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/zapros <nick>"));
            return false;
        } else if(args.length == 1){

            String name = args[0];
            Player invite = Bukkit.getPlayer(name);

            if(invite == null){
                p.sendMessage(Util.fixColor("&4Blad: &cPodany gracz jest offline"));
                return false;
            }
            User inviteUser = UserUtils.get(invite.getUniqueId());
            if(inviteUser == null){
                p.sendMessage(Util.fixColor("&4Blad: &cBlad, skontakuj sie z administratorem (kod: XZX184)"));
                return false;
            }
            Guild inviteGuild = inviteUser.getGuild();
            if(inviteGuild != null){
                p.sendMessage(Util.fixColor("&4Blad: &cTen gracz posiada juz gildie"));
                return false;
            }
            if(inviteUser.getInvination() != null && inviteUser.getInvination().equals(g)){
                p.sendMessage(Util.fixColor(" &8» &7Zaproszenie zostalo anulowane"));
                inviteUser.setInvination(null);
                return true;
            }
            inviteUser.setInvination(g);
            p.sendMessage(Util.fixColor(" &8» &7Zaprosiles gracza &6"+invite.getName()+" &7do gildii!"));
            p.sendMessage(Util.fixColor(" &8» &7Aby anulowac zaproszenie wpisz ponownie ta sama komende"));
            invite.sendMessage(Util.fixColor(" &8» &7Zostales zaproszony do gildii &e"+g.getName()+" &8[&e"+g.getTag()+"&8]"));
            invite.sendMessage(Util.fixColor(" &8» &7Wpisz &6/dolacz "+g.getTag()+"&7,aby dolaczyc do gildii"));
            return true;

        } else {
            p.sendMessage(Util.fixColor(" &8» &7Poprawne uzycie &6/zapros <nick>"));
            return false;
        }
    }
}
