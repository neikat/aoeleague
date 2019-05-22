package com.sonic.aoeleague.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.model.PlayerInfo;
import com.sonic.aoeleague.util.AoeLeagueUtil;

public class HistoryService {
	private static AoeLeagueCache aoeLeagueCache = AoeLeagueCache.getInstance();
	
	public static PlayerInfo getPlayerInfo(Integer id) {
		PlayerInfo playerInfo = aoeLeagueCache.getPlayerInfo(id);
		if (playerInfo == null)
			return null;
		
		DecimalFormat df = new DecimalFormat("#.##");
		String newRoundPoint = df.format(playerInfo.getPlayerPoint());
		playerInfo.setPlayerPoint(Double.valueOf(newRoundPoint));
		
		return playerInfo;
	}
	
	public static List<PlayerInfo> getPlayerInfoList() {
		List<Player> sortedPlayers = aoeLeagueCache.getListPlayers();
		List<PlayerInfo> playerInfoList = new ArrayList<PlayerInfo>();
		for (Player player : sortedPlayers) {
			PlayerInfo playerInfo = getPlayerInfo(player.getId());
			
			playerInfoList.add(playerInfo);
		}
		
		return playerInfoList;
	}
	
	public static List<Match> getMatchHistory(Match match) {
		AoeLeagueUtil.generateMatchGroupId(match);
		return aoeLeagueCache.getHistory(match.getGroupId());
	}
	
	public static List<Match> getPlayerHistory(Integer playerId) {
		return aoeLeagueCache.getHistoryByPlayer(playerId);
	}
	
}
