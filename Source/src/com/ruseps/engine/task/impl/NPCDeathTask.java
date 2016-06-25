package com.ruseps.engine.task.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.world.World;
import com.ruseps.world.content.Achievements;
import com.ruseps.world.content.Achievements.AchievementData;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.content.KillsTracker.KillsEntry;
import com.ruseps.world.content.combat.strategy.impl.KalphiteQueen;
import com.ruseps.world.content.combat.strategy.impl.Nex;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Represents an npc's death task, which handles everything
 * an npc does before and after their death animation (including it), 
 * such as dropping their drop table items.
 * 
 * @author relex lawl
 */

public class NPCDeathTask extends Task {

	/**
	 * The NPCDeathTask constructor.
	 * @param npc	The npc being killed.
	 */
	public NPCDeathTask(NPC npc) {
		super(2);
		this.npc = npc;
		this.ticks = 2;
	}

	/**
	 * The npc setting off the death task.
	 */
	private final NPC npc;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 2;

	/**
	 * The player who killed the NPC
	 */
	private Player killer = null;

	@SuppressWarnings("incomplete-switch")
	@Override
	public void execute() {
		try {
			npc.setEntityInteraction(null);
			switch (ticks) {
			case 2:
				npc.getMovementQueue().setLockMovement(true).reset();
				killer = npc.getCombatBuilder().getKiller(true);
				if(!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
					npc.performAnimation(new Animation(npc.getDefinition().getDeathAnimation()));

				/** CUSTOM NPC DEATHS **/
				if(npc.getId() == 13447) {
					Nex.handleDeath();
				}

				break;
			case 0:
				if(killer != null) {

					boolean boss = (npc.getDefaultConstitution() > 2000);
					if(!Nex.nexMinion(npc.getId()) && npc.getId() != 1158 && !(npc.getId() >= 3493 && npc.getId() <= 3497)) {
						KillsTracker.submit(killer, new KillsEntry(npc.getDefinition().getName(), 1, boss));
						if(boss) {
							Achievements.doProgress(killer, AchievementData.DEFEAT_500_BOSSES);
						}
					}
					
					Achievements.doProgress(killer, AchievementData.DEFEAT_10000_MONSTERS);
					if(npc.getId() == 50) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_KING_BLACK_DRAGON);
					} else if(npc.getId() == 3200) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CHAOS_ELEMENTAL);
					} else if(npc.getId() == 8349) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_A_TORMENTED_DEMON);
					} else if(npc.getId() == 3491) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CULINAROMANCER);
					} else if(npc.getId() == 8528) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_NOMAD);
					} else if(npc.getId() == 2745) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_JAD);
					} else if(npc.getId() == 4540) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_BANDOS_AVATAR);
					} else if(npc.getId() == 6260) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_GENERAL_GRAARDOR);
						killer.getAchievementAttributes().setGodKilled(0, true);
					} else if(npc.getId() == 6222) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_KREE_ARRA);
						killer.getAchievementAttributes().setGodKilled(1, true);
					} else if(npc.getId() == 6247) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_COMMANDER_ZILYANA);
						killer.getAchievementAttributes().setGodKilled(2, true);
					} else if(npc.getId() == 6203) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_KRIL_TSUTSAROTH);
						killer.getAchievementAttributes().setGodKilled(3, true);
					} else if(npc.getId() == 8133) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CORPOREAL_BEAST);
					} else if(npc.getId() == 13447) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_NEX);
						killer.getAchievementAttributes().setGodKilled(4, true);
					}
					/** ACHIEVEMENTS **/
					switch(killer.getLastCombatType()) {
					case MAGIC:
						Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MAGIC);
						break;
					case MELEE:
						Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MELEE);
						break;
					case RANGED:
						Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_RANGED);
						break;
					}

					/** LOCATION KILLS **/
					if(npc.getLocation().handleKilledNPC(killer, npc)) {
						stop();
						return;
					}

					/** PARSE DROPS **/
					NPCDrops.dropItems(killer, npc);

					/** SLAYER **/
					killer.getSlayer().killedNpc(npc);
				}
				stop();
				break;
			}
			ticks--;
		} catch(Exception e) {
			e.printStackTrace();
			stop();
		}
	}

	@Override
	public void stop() {
		setEventRunning(false);

		npc.setDying(false);

		//respawn
		if(npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.GRAVEYARD && npc.getLocation() != Location.DUNGEONEERING) {
			TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime()));
		}

		World.deregister(npc);

		if(npc.getId() == 1158 || npc.getId() == 1160) {
			KalphiteQueen.death(npc.getId(), npc.getPosition());
		}
		if(Nex.nexMob(npc.getId())) {
			Nex.death(npc.getId());
		}
	}
}
