package com.sonic.aoeleague.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;

import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.model.Team;
import com.sonic.aoeleague.util.Constants;

public class GenerateMatchService {
	private static AoeLeagueCache aoeLeagueCache = AoeLeagueCache.getInstance();
	
	public static List<Match> generateMatches(String inputText, int maxResults) throws Exception {
		if (StringUtils.isBlank(inputText))
			return null;
		
		String [] splitter = inputText.split(",");
		List<String> playerNames = new ArrayList<String>();
		for (String playerName : splitter) {
			playerName = playerName.trim();
			if (aoeLeagueCache.hasPlayer(playerName)) {
				playerNames.add(playerName);
			}
		}
		
		return generateMatches(playerNames, maxResults);
	}
	
	public static List<Match> generateMatches(List<String> playerNames, int maxResults) throws Exception {
		// calculate number of players
		int totalPlayers = playerNames.size();
		if (totalPlayers < 2 || totalPlayers > 8)
			throw new Exception("Number of players must be from 2 to 8.");
		
		// team2Quantity <= team1Quantity
		int team2Quantity = (int) totalPlayers / 2;
		int team1Quantity = totalPlayers - team2Quantity;
		
		//int team2Quantity = 5;
		//int team1Quantity = 3;
		
		// Calculate Players info
		List<Player> players = new ArrayList<Player>();
		for (String playerName : playerNames) {
			Player player = clonePlayerIntoMatch(aoeLeagueCache.getPlayer(playerName));
			players.add(player);
		}
		// Team 1 combinations:
		
		TreeMap<Double, List<Match>> matchTreeMap = new TreeMap<Double, List<Match>>();
		Set<String> combineValidations = new HashSet<String>();
		Iterator<int[]> combinationIter = CombinatoricsUtils.combinationsIterator(totalPlayers, team1Quantity);
		while (combinationIter.hasNext()) {
			Team team1 = new Team();
			Team team2 = new Team();
			int [] combination = combinationIter.next();
			String combinationTeam1Str = "";
			String combinationTeam2Str = "";
			Set<Integer> team1Index = new HashSet<Integer>();
			for (Integer index : combination) {
				team1Index.add(index);
			}
			
			for (int i = 0; i < totalPlayers; i++) {
				if (team1Index.contains(i)) {
					team1.getPlayers().add(players.get(i));
					combinationTeam1Str += i;
				} else {
					team2.getPlayers().add(players.get(i));
					combinationTeam2Str += i;
				}
			}
			if (combineValidations.contains(combinationTeam2Str))
				continue;
			
			combineValidations.add(combinationTeam1Str);
			
			//calculate team point
			team1.setTeampoint(getTeamPoint(team1));
			team2.setTeampoint(getTeamPoint(team2));
			
			//calculate match point
			Match match = new Match();
			match.setTeam1(team1);
			match.setTeam2(team2);
			match.setMatchPoint(team1.getTeampoint() - team2.getTeampoint());
			double matchPoint = team1.getTeampoint() - team2.getTeampoint();
			double matchPointAsAbs = Math.abs(matchPoint);
			
			// sort match
			if (matchTreeMap.get(matchPointAsAbs) != null) {
				matchTreeMap.get(matchPointAsAbs).add(match);
			} else {
				matchTreeMap.put(matchPointAsAbs, new ArrayList<Match>());
				matchTreeMap.get(matchPointAsAbs).add(match);
			}
			
			//check balance
			/*if (team1.getTeampoint() > team2.getTeampoint()) {
				team2.setNumOfShang((int) (matchPointAsAbs / 10));
				team1.setNumOfShang(0);
			} else {
				team1.setNumOfShang((int) (matchPointAsAbs / 10));
				team2.setNumOfShang(0);
			}*/
			
		}
		
		// print
		List<Match> matchesResult = new ArrayList<Match>();
		int numOfMatchResults = 1;
		for (List<Match> matches : matchTreeMap.values()) {
			for (Match match : matches) {
				if (numOfMatchResults > maxResults)
					break;
				
				matchesResult.add(match);
				numOfMatchResults ++;
			}
			
		}
		return matchesResult;
	}
	
	/*public static double getPlayerPoint(Player player) {
		double topPoint = 40 - player.getTop() * 10;
		return topPoint + (player.getPoint() * Constants.PLAYER_POINT_MULTIPLIER);
	}*/
	
	public static double getTeamPoint(Team team) {
		double teamPoint = 0;
		for (Player player : team.getPlayers()) {
			teamPoint += player.getPlayerPoint();
		}
		
		return teamPoint;
	}
	
	public static String getMatchAsString(Match match) {
		String text = "";
		for (Player player : match.getTeam1().getPlayers()) {
			text += player.getName();
			text += " ";
		}
		if (match.getTeam1().getNumOfShang() != null && match.getTeam1().getNumOfShang() > 0) {
			text += "(" + match.getTeam1().getNumOfShang() + " Shang) ";
		}
		text += "vs ";
		for (Player player : match.getTeam2().getPlayers()) {
			text += player.getName();
			text += " ";
		}
		if (match.getTeam2().getNumOfShang() != null && match.getTeam2().getNumOfShang() > 0) {
			text += "(" + match.getTeam2().getNumOfShang() + " Shang) ";
		}
		text += "- ";
		text += match.getMatchPoint();
		return text;
	}
	
	public static String getMatchWithScoreAsString(Match match) {
		if (match.getResult() == null) {
			
		}
		String text = "";
		text += match.getDate() + " - ";
		for (Player player : match.getTeam1().getPlayers()) {
			text += player.getName();
			text += " ";
		}
		if (match.getTeam1().getNumOfShang() != null && match.getTeam1().getNumOfShang() > 0) {
			text += "(" + match.getTeam1().getNumOfShang() + " Shang) ";
		}
		text += match.getResult() + " ";
		for (Player player : match.getTeam2().getPlayers()) {
			text += player.getName();
			text += " ";
		}
		if (match.getTeam2().getNumOfShang() != null && match.getTeam2().getNumOfShang() > 0) {
			text += "(" + match.getTeam2().getNumOfShang() + " Shang) ";
		}
		return text;
	}
	
	private static Player clonePlayerIntoMatch(Player player) {
		if (player == null)
			return null;
		
		Player clone = new Player();
		clone.setId(player.getId());
		clone.setName(player.getName());
		clone.setPlayerPoint(player.getPlayerPoint());
		clone.setTop(player.getTop());
		clone.setType(player.getType());
		
		return clone;
	}
}
