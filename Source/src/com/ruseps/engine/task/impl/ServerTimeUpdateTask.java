package com.ruseps.engine.task.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.model.Locations;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.minigames.impl.PestControl;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;

	@Override
	protected void execute() {
		World.updateServerTime();
		
		if(tick >= 6 && (Locations.PLAYERS_IN_WILD >= 3 || Locations.PLAYERS_IN_DUEL_ARENA >= 3 || PestControl.TOTAL_PLAYERS >= 3)) {
			if(Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA && Locations.PLAYERS_IN_WILD > PestControl.TOTAL_PLAYERS || Misc.getRandom(3) == 1 && Locations.PLAYERS_IN_WILD >= 2) {
				World.sendMessage("<img=10> @blu@There are currently "+Locations.PLAYERS_IN_WILD+" players roaming the Wilderness!");
			} else if(Locations.PLAYERS_IN_DUEL_ARENA > Locations.PLAYERS_IN_WILD && Locations.PLAYERS_IN_DUEL_ARENA > PestControl.TOTAL_PLAYERS) {
				World.sendMessage("<img=10> @blu@There are currently "+Locations.PLAYERS_IN_DUEL_ARENA+" players at the Duel Arena!");
			} else if(PestControl.TOTAL_PLAYERS > Locations.PLAYERS_IN_WILD && PestControl.TOTAL_PLAYERS > Locations.PLAYERS_IN_DUEL_ARENA) {
				World.sendMessage("<img=10> @blu@There are currently "+PestControl.TOTAL_PLAYERS+" players at Pest Control!");
			}
			tick = 0;
		}

		tick++;
	}
}