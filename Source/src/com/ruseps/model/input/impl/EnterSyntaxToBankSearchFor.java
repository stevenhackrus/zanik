package com.ruseps.model.input.impl;

import com.ruseps.model.container.impl.Bank.BankSearchAttributes;
import com.ruseps.model.input.Input;
import com.ruseps.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank();
		if(searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
