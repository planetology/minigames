/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.minigames.command;

import com.mcmiddleearth.minigames.MiniGamesPlugin;
import com.mcmiddleearth.minigames.data.PluginData;
import com.mcmiddleearth.minigames.game.QuizGame;
import java.io.File;
import java.util.Calendar;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class QuizGameQuestionsReview extends AbstractGameCommand{
    
    public QuizGameQuestionsReview(String... permissionNodes) {
        super(0, true, permissionNodes);
        cmdGroup = CmdGroup.LORE_QUIZ;
        setShortDescription(": Creates a quiz game with submitted questions.");
        setUsageDescription(": Creates a lore quiz game with all submitted question which have not been reviewed before. The quiz is save in a quiz file with filename <rYYYY_MM_DD> you can then edit or remove questions and accept (save) them for the MCME lore question table.");
    }
    
    @Override
    protected void execute(CommandSender cs, String... args) {
        if(args.length>0 && args[0].equalsIgnoreCase("check")) {
            sendReviewCheckMessage(cs, PluginData.getQuestionSubmitGame().countQuestions());
            return;
        }
        if(!isAlreadyInGame((Player)cs) && !isAlreadyManager((Player) cs)) {
            File submittedFile = PluginData.getSubmittedQuestionsFile();
            if(submittedFile.exists()) {
                Calendar calendar = Calendar.getInstance();
                String filename = "r"
                                   +calendar.get(Calendar.YEAR)+"_"
                                   +calendar.get(Calendar.MONTH)+"_"
                                   +calendar.get(Calendar.DAY_OF_MONTH);
                File file = new File(PluginData.getQuestionDir(),filename+".json");
                int i = 1;
                while(file.exists()) {
                    i++;
                    file = new File(PluginData.getQuestionDir(),filename+"_"+i+".json");
                }
                if(i>1) {
                    filename+="_"+i;
                }
                submittedFile.renameTo(file);
                PluginData.getQuestionSubmitGame().clearQuestions();
                int index = 1;
                while(PluginData.getGame("review"+index)!=null) {
                    index++;
                }
                PluginData.stopSpectating((Player)cs);
                QuizGame game = new QuizGame((Player) cs, "review"+index);
                sendQuizGameCreateMessage(cs,filename);
                PluginData.addGame(game);
                game.setPrivat(true);
                MiniGamesPlugin.getPluginInstance().getCommand("game").getExecutor().
                                onCommand(cs, null, "game", new String[]{"loadquiz",filename});
            } else {
                sendNoQuestionsSubmittedMessage(cs);
            }
        }
    }
    
    public void sendQuizGameCreateMessage(CommandSender cs, String filename) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "A quiz game with all submitted questions was created to review.");
        PluginData.getMessageUtil().sendIndentedInfoMessage(cs, "Submitted questions saved in file: "+ filename);
        PluginData.getMessageUtil().sendIndentedInfoMessage(cs, "Don't forget to delete when no loger needed:");
        PluginData.getMessageUtil().sendIndentedInfoMessage(cs, ChatColor.DARK_AQUA+"/game delete quiz "+filename);
    }

    private void sendNoQuestionsSubmittedMessage(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "There are no submitted questions to review.");
    }

    private void sendReviewCheckMessage(CommandSender cs, int count) {
        if(count==0) {
            PluginData.getMessageUtil().sendInfoMessage(cs, "There are no submitted questions to review.");
        } else if (count==1) {
            PluginData.getMessageUtil().sendInfoMessage(cs, "There is ONE submitted questions to review.");
        } else {
            PluginData.getMessageUtil().sendInfoMessage(cs, "There are "+count+" submitted questions to review.");
        }
    }

 }
