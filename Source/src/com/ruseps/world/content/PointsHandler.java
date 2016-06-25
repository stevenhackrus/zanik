package com.ruseps.world.content;

import com.ruseps.world.entity.impl.player.Player;

public class PointsHandler {

	private Player p;
	
	public PointsHandler(Player p) {
		this.p = p;
	}
	
	public void reset() {
		dungTokens = commendations = (int) (loyaltyPoints = votingPoints = slayerPoints = pkPoints = 0);
		p.getPlayerKillingAttributes().setPlayerKillStreak(0);
		p.getPlayerKillingAttributes().setPlayerKills(0);
		p.getPlayerKillingAttributes().setPlayerDeaths(0);
		p.getDueling().arenaStats[0] = p.getDueling().arenaStats[1] = 0;
	}

	public PointsHandler refreshPanel() {
		p.getPacketSender().sendString(39176, "@or2@Prestige Points: @yel@"+prestigePoints);
		p.getPacketSender().sendString(39177, "@or2@Commendations: @yel@ "+commendations);
		p.getPacketSender().sendString(39178, "@or2@Loyalty Points: @yel@"+(int)loyaltyPoints);
		p.getPacketSender().sendString(39179, "@or2@Dung. Tokens: @yel@ "+dungTokens);
		p.getPacketSender().sendString(39180, "@or2@Voting Points: @yel@ "+votingPoints);
		p.getPacketSender().sendString(39181, "@or2@Slayer Points: @yel@"+slayerPoints);
		p.getPacketSender().sendString(39182, "@or2@Pk Points: @yel@"+pkPoints);
		p.getPacketSender().sendString(39183, "@or2@Wilderness Killstreak: @yel@"+p.getPlayerKillingAttributes().getPlayerKillStreak());
		p.getPacketSender().sendString(39184, "@or2@Wilderness Kills: @yel@"+p.getPlayerKillingAttributes().getPlayerKills());		
		p.getPacketSender().sendString(39185, "@or2@Wilderness Deaths: @yel@"+p.getPlayerKillingAttributes().getPlayerDeaths());
		p.getPacketSender().sendString(39186, "@or2@Arena Victories: @yel@"+p.getDueling().arenaStats[0]);
		p.getPacketSender().sendString(39187, "@or2@Arena Losses: @yel@"+p.getDueling().arenaStats[1]);
		
		return this;
	}

	private int prestigePoints;
	private int slayerPoints;
	private int commendations;
	private int dungTokens;
	private int pkPoints;
	private double loyaltyPoints;
	private int votingPoints;
	private int achievementPoints;
	
	public int getPrestigePoints() {
		return prestigePoints;
	}
	
	public void setPrestigePoints(int points, boolean add) {
		if(add)
			this.prestigePoints += points;
		else
			this.prestigePoints = points;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public void setSlayerPoints(int slayerPoints, boolean add) {
		if(add)
			this.slayerPoints += slayerPoints;
		else
			this.slayerPoints = slayerPoints;
	}

	public int getCommendations() {
		return this.commendations;
	}

	public void setCommendations(int commendations, boolean add) {
		if(add)
			this.commendations += commendations;
		else
			this.commendations = commendations;
	}

	public int getLoyaltyPoints() {
		return (int)this.loyaltyPoints;
	}

	public void setLoyaltyPoints(int points, boolean add) {
		if(add)
			this.loyaltyPoints += points;
		else
			this.loyaltyPoints = points;
	}
	
	public void incrementLoyaltyPoints(double amount) {
		this.loyaltyPoints += amount;
	}
	
	public int getPkPoints() {
		return this.pkPoints;
	}

	public void setPkPoints(int points, boolean add) {
		if(add)
			this.pkPoints += points;
		else
			this.pkPoints = points;
	}
	
	public int getDungeoneeringTokens() {
		return dungTokens;
	}

	public void setDungeoneeringTokens(int dungTokens, boolean add) {
		if(add)
			this.dungTokens += dungTokens;
		else
			this.dungTokens = dungTokens;
	}
	
	public int getVotingPoints() {
		return votingPoints;
	}
	
	public void setVotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}
	
	public void incrementVotingPoints() {
		this.votingPoints++;
	}
	
	public void incrementVotingPoints(int amt) {
		this.votingPoints += amt;
	}
	
	public void setVotingPoints(int points, boolean add) {
		if(add)
			this.votingPoints += points;
		else
			this.votingPoints = points;
	}
	
	public int getAchievementPoints() {
		return achievementPoints;
	}
	
	public void setAchievementPoints(int points, boolean add) {
		if(add)
			this.achievementPoints += points;
		else
			this.achievementPoints = points;
	}
}
