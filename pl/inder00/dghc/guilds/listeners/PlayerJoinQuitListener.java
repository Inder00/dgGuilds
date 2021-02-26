package pl.inder00.dghc.guilds.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.inder00.dghc.api.basic.InvidualTag;
import pl.inder00.dghc.api.basic.User;
import pl.inder00.dghc.api.basic.utils.UserUtils;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e){

        Player p = e.getPlayer();
        User u = UserUtils.get(p.getUniqueId());
        if(u.getInvidualTag() == null){
            u.setInvidualTag(new InvidualTag(u));
        }
        p.setScoreboard(u.getInvidualTag().getScoreboard());
        for(User usr : UserUtils.getUsers()){
            usr.getInvidualTag().update(u);
            u.getInvidualTag().update(usr);
        }

    }

}
