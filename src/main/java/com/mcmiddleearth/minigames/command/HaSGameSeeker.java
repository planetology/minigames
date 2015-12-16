/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.minigames.command;

import com.mcmiddleearth.minigames.game.AbstractGame;
import com.mcmiddleearth.minigames.game.HideAndSeekGame;
import com.mcmiddleearth.minigames.utils.BukkitUtil;
import com.mcmiddleearth.minigames.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class HaSGameSeeker extends AbstractHideAndSeekCommand{
    
    public HaSGameSeeker(String... permissionNodes) {
        super(1, true, permissionNodes);
        setShortDescription(": ");
        setUsageDescription(": ");
    }
    
    @Override
    protected void execute(CommandSender cs, String... args) {
        AbstractGame game = getGame((Player) cs);
        if(game != null && isManager((Player) cs, game) && isHideAndSeekGame((Player) cs, game)) {
            OfflinePlayer seeker = game.getPlayer(args[0]);
            if(seeker==null) {
                sendPlayerNotFoundErrorMessage(cs);
            }
            else {
                ((HideAndSeekGame)game).setSeeker(seeker);
                sendSeekerSetMessage(cs, seeker);
            }
        }
    }
    
    private void sendPlayerNotFoundErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "Player not found.");
    }

    private void sendSeekerSetMessage(CommandSender cs, OfflinePlayer seeker) {
        MessageUtil.sendInfoMessage(cs, seeker.getName() +" will be the next seeker.");
        if(BukkitUtil.getOnlinePlayer(seeker)!=null)
            MessageUtil.sendInfoMessage(BukkitUtil.getOnlinePlayer(seeker), 
                                        "You are assigned to be the next seeker.");
    }

 }