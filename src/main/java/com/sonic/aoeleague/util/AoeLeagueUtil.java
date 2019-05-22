package com.sonic.aoeleague.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.service.AoeLeagueCache;

public class AoeLeagueUtil {
	private static AoeLeagueCache aoeLeagueCache = AoeLeagueCache.getInstance();
	
	public static void generateMatchGroupId(Match match) {
		String groupId = "";
		//sort team1
		String groupIdForTeam1 = generateGroupIdForTeam(match.getTeam1().getPlayers());
		String groupIdForTeam2 = generateGroupIdForTeam(match.getTeam2().getPlayers());
		if (groupIdForTeam1.compareTo(groupIdForTeam2) < 0) {
			groupId += groupIdForTeam1 + "_" + groupIdForTeam2;
		} else {
			groupId += groupIdForTeam2 + "_" + groupIdForTeam1;
		}
		
		match.setGroupId(groupId);
	}
	
	private static String generateGroupIdForTeam(List<Player> players) {
		List<Player> sortedPlayers = sortPlayersById(players);
		if (sortedPlayers == null)
			return "";
		
		String groupId = "";
		for (Player player : sortedPlayers) {
			groupId += player.getId() + "-";
		}
		
		return groupId;
	}
	
	private static List<Player> sortPlayersById(List<Player> players) {
		if (Utilities.isEmpty(players))
			return null;
		
		TreeMap<Integer, Player> playerMap = new TreeMap<Integer, Player>();
		for (Player player : players) {
			if (player.getId() == null) {
				Player cachedPlayer = aoeLeagueCache.getPlayer(player.getName());
				player.setId(cachedPlayer.getId());
			}
			playerMap.put(player.getId(), player);
		}
		
		return new ArrayList<Player>(playerMap.values());
	}
}
