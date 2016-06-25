package com.ruseps.world.content;

import com.ruseps.util.Misc;
import com.ruseps.world.content.minigames.impl.Nomad;
import com.ruseps.world.content.minigames.impl.RecipeForDisaster;
import com.ruseps.world.content.skill.impl.slayer.SlayerTasks;
import com.ruseps.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		/**
		 * General info
		 */
		player.getPacketSender().sendString(39159, "@or3@ - @whi@ General Information");

		if(ShootingStar.CRASHED_STAR == null) {
			player.getPacketSender().sendString(39162, "@or2@Crashed star: @yel@N/A");
		} else {
			player.getPacketSender().sendString(39162, "@or2@Crashed star: @yel@"+ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame+"");
		}
		
		if(WellOfGoodwill.isActive()) {
			player.getPacketSender().sendString(39163, "@or2@Well of Goodwill: @yel@Active");
		} else {
			player.getPacketSender().sendString(39163, "@or2@Well of Goodwill: @yel@N/A");
		}
		
		/**
		 * Account info
		 */
		player.getPacketSender().sendString(39165, "@or3@ - @whi@ Account Information");
		player.getPacketSender().sendString(39167, "@or2@Username:  @yel@"+player.getUsername());
		player.getPacketSender().sendString(39168, "@or2@Claimed:  @yel@$"+player.getAmountDonated());
		player.getPacketSender().sendString(39169, "@or2@Rank:  @yel@"+Misc.formatText(player.getRights().toString().toLowerCase()));
		player.getPacketSender().sendString(39170, "@or2@Email:  @yel@"+(player.getEmailAddress() == null || player.getEmailAddress().equals("null") ? "-" : player.getEmailAddress()));
		player.getPacketSender().sendString(39171, "@or2@Music:  @yel@"+(player.musicActive() ? "On" : "Off")+"");
		player.getPacketSender().sendString(39172, "@or2@Sounds:  @yel@"+(player.soundsActive() ? "On" : "Off")+"");
		player.getPacketSender().sendString(39173, "@or2@Exp Lock:  @yel@"+(player.experienceLocked() ? "Locked" : "Unlocked")+"");

		/**
		 * Points
		 */
		player.getPacketSender().sendString(39175, "@or3@ - @whi@ Statistics");
		player.getPointsHandler().refreshPanel();

		/**
		 * Slayer
		 */
		player.getPacketSender().sendString(39189, "@or3@ - @whi@ Slayer");
		player.getPacketSender().sendString(39190, "@or2@Open Kills Tracker");
		player.getPacketSender().sendString(39191, "@or2@Open Drop Log");
		player.getPacketSender().sendString(39192, "@or2@Master:  @yel@"+Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));
		if(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) 
			player.getPacketSender().sendString(39193, "@or2@Task:  @yel@"+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"");
		else
			player.getPacketSender().sendString(39193, "@or2@Task:  @yel@"+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"s");
		player.getPacketSender().sendString(39194, "@or2@Task Streak:  @yel@"+player.getSlayer().getTaskStreak()+"");
		player.getPacketSender().sendString(39195, "@or2@Task Amount:  @yel@"+player.getSlayer().getAmountToSlay()+"");
		if(player.getSlayer().getDuoPartner() != null)
			player.getPacketSender().sendString(39196, "@or2@Duo Partner:  @yel@"+player.getSlayer().getDuoPartner()+"");
		else
			player.getPacketSender().sendString(39196, "@or2@Duo Partner:");

		/**
		 * Quests
		 */
		player.getPacketSender().sendString(39198, "@or3@ - @whi@ Quests");
		player.getPacketSender().sendString(39199, RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster");
		player.getPacketSender().sendString(39200, Nomad.getQuestTabPrefix(player) + "Nomad's Requeim");

		/**
		 * Links
		 */
		player.getPacketSender().sendString(39202, "@or3@ - @whi@ Links");
		player.getPacketSender().sendString(39203, "@or2@Forum");
		player.getPacketSender().sendString(39204, "@or2@Rules");
		player.getPacketSender().sendString(39205, "@or2@Store");
		player.getPacketSender().sendString(39206, "@or2@Vote");
		player.getPacketSender().sendString(39207, "@or2@Hiscores");
		player.getPacketSender().sendString(39208, "@or2@Report");
	}

}
