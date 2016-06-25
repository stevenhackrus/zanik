package com.ruseps.world.content.skill.impl.agility;

import com.ruseps.model.GameObject;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.util.Misc;
import com.ruseps.world.content.Achievements;
import com.ruseps.world.content.Achievements.AchievementData;
import com.ruseps.world.entity.impl.player.Player;

public class Agility {

	public static boolean handleObject(Player p, GameObject object) {
		if(object.getId() == 2309) {
			if(p.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
				p.getPacketSender().sendMessage("You need an Agility level of at least 55 to enter this course.");
				return true;
			}
		}
		ObstacleData agilityObject = ObstacleData.forId(object.getId());
		if(agilityObject != null) {
			if(p.isCrossingObstacle())
				return true;
			p.setPositionToFace(object.getPosition());
			p.setResetPosition(p.getPosition());
			p.setCrossingObstacle(true);
			//boolean wasRunning = p.getAttributes().isRunning();
			//if(agilityObject.mustWalk()) {
				//p.getAttributes().setRunning(false);
			//	p.getPacketSender().sendRunStatus();
			//}
			agilityObject.cross(p);
			Achievements.finishAchievement(p, AchievementData.CLIMB_AN_AGILITY_OBSTACLE);
			Achievements.doProgress(p, AchievementData.CLIMB_50_AGILITY_OBSTACLES);
		}
		return false;
	}

	public static boolean passedAllObstacles(Player player) {
		for(boolean crossedObstacle : player.getCrossedObstacles()) {
			if(!crossedObstacle)
				return false;
		}
		return true;
	}

	public static void resetProgress(Player player) {
		for(int i = 0; i < player.getCrossedObstacles().length; i++)
			player.setCrossedObstacle(i, false);
	}
	
	public static boolean isSucessive(Player player) {
		return Misc.getRandom(player.getSkillManager().getCurrentLevel(Skill.AGILITY) / 2) > 1;
	}
	
	public static void addExperience(Player player, int experience) {
		boolean agile = player.getEquipment().get(Equipment.BODY_SLOT).getId() == 14936 && player.getEquipment().get(Equipment.LEG_SLOT).getId() == 14938;
		player.getSkillManager().addExperience(Skill.AGILITY, agile ? (experience *= 1.5) : experience);
	}
}
