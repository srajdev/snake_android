package com.android.model;

public class ScoreModel {

	int playerRank;

	String playerName;

	String gameMode;

	public int getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(int playerRank) {
		this.playerRank = playerRank;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getGameMode() {
		return gameMode;
	}

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	public long getGameScore() {
		return gameScore;
	}

	public void setGameScore(long gameScore) {
		this.gameScore = gameScore;
	}

	long gameScore;

}
