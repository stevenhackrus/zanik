package com.ruseps.model.input.impl;

import com.ruseps.model.input.EnterAmount;
import com.ruseps.world.content.skill.impl.summoning.PouchMaking;
import com.ruseps.world.entity.impl.player.Player;

public class EnterAmountToInfuse extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(player.getInterfaceId() != 63471) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		PouchMaking.infusePouches(player, amount);
	}

}
