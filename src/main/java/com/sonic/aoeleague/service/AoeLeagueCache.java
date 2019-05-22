package com.sonic.aoeleague.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.sonic.aoeleague.dao.DataExporter;
import com.sonic.aoeleague.dao.DataImporter;
import com.sonic.aoeleague.model.AoeLeague;
import com.sonic.aoeleague.model.History;
import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.model.PlayerAnalysis;
import com.sonic.aoeleague.model.PlayerInfo;
import com.sonic.aoeleague.util.Constants;
import com.sonic.aoeleague.util.Utilities;

public class AoeLeagueCache {
	private AoeLeague aoeLeagueData = null;
	private Map<Integer, Player> playerCache = new ConcurrentHashMap<Integer, Player>();
	private Map<String, Integer> playerNameCache = new ConcurrentHashMap<String, Integer>();
	private TreeMap<String, Match> matchesCache = new TreeMap<String, Match>();
	private Map<String, TreeSet<String>> historyGroupCache = new ConcurrentHashMap<String, TreeSet<String>>();
	private Map<Integer, TreeSet<String>> historyPlayerCache = new ConcurrentHashMap<Integer, TreeSet<String>>();
	
	private Map<Integer, PlayerInfo> playerInfoCache = new ConcurrentHashMap<Integer, PlayerInfo>();
	// playerId - gameIds
	private Map<Integer, TreeSet<String>> winGamesCache = new ConcurrentHashMap<Integer, TreeSet<String>>();
	private Map<Integer, TreeSet<String>> gameIdsCache = new ConcurrentHashMap<Integer, TreeSet<String>>();
	private Map<Integer, TreeSet<String>> soloGamesCache = new ConcurrentHashMap<Integer, TreeSet<String>>();
	private static AoeLeagueCache instance = null;

	private AoeLeagueCache() {
		load();
	}
	
	public static AoeLeagueCache getInstance() {
		if (instance == null) {
			instance = new AoeLeagueCache();
		}
		return instance;
	}
	
	private void load() {
		aoeLeagueData = (AoeLeague) DataImporter.importXml(Constants.PATH_DATA_AOELEAGUE, AoeLeague.class);
		if (aoeLeagueData != null) {
			List<Player> players = aoeLeagueData.getPlayers();
			for (Player player : players) {
				playerCache.put(player.getId(), player);
				playerNameCache.put(player.getName().toLowerCase(), player.getId());
				
				//initial playerId - gameIds caches
				winGamesCache.put(player.getId(), new TreeSet<String>());
				gameIdsCache.put(player.getId(), new TreeSet<String>());
				soloGamesCache.put(player.getId(), new TreeSet<String>());
			}
			if (aoeLeagueData.getHistory() != null) {
				List<Match> matches = aoeLeagueData.getHistory().getMatches();
				for (Match match : matches) {
					addMatchToCaches(match);
				}
			}
			
			reloadPlayerInfoCache();
		} else {
			System.out.println("no data loaded!!");
		}
	}
	
	public void save() {
		aoeLeagueData.setUpdatedTime(Utilities.getCurrentTime());
		DataExporter.exportToXMl(aoeLeagueData, Constants.PATH_DATA_AOELEAGUE, AoeLeague.class);
	}
	
	public List<String> getListPlayerNames() {
		List<Player> players = aoeLeagueData.getPlayers();
		List<String> playerNames = new ArrayList<String>();
		for (Player player : players) {
			playerNames.add(player.getName());
		}
		return playerNames;
	}
	
	public List<Player> getListPlayers() {
		return aoeLeagueData.getPlayers();
	}
	
	public Player getPlayer(Integer playerId) {
		if (playerId == null)
			return null;
		return playerCache.get(playerId);
	}
	
	public Player getPlayer(String playerName) {
		if (playerName == null) 
			return null;
		Integer id = playerNameCache.get(playerName.toLowerCase());
		if (id == null)
			return null;
		return playerCache.get(id);
	}
	
	public boolean hasPlayer(String playerName) {
		if (playerName == null) 
			return false;
		return playerNameCache.containsKey(playerName.toLowerCase());
	}
	
	public PlayerInfo getPlayerInfo(Integer id) {
		return playerInfoCache.get(id);
	}
	
	void addMatch(Match match) {
		if (match == null)
			return;
		
		addMatchToCaches(match);
		
		if (aoeLeagueData.getHistory() == null) {
			aoeLeagueData.setHistory(new History());
		}
		aoeLeagueData.getHistory().getMatches().clear();
		aoeLeagueData.getHistory().getMatches().addAll(matchesCache.values());
		
		reloadPlayerInfoCache();
	}
	
	private void addMatchToCaches(Match match) {
		matchesCache.put(match.getId(), match);
		// match history by group Id
		if (historyGroupCache.get(match.getGroupId()) == null) {
			historyGroupCache.put(match.getGroupId(), new TreeSet<String>());
		}
		historyGroupCache.get(match.getGroupId()).add(match.getId());
		
		// history by player
		int [] score = Utilities.getScore(match.getResult());
		int gameIndex = 0;
		boolean isSolo = match.isSolo() != null ? match.isSolo() : false;
		Set<String> team1WinGameIds = new HashSet<String>();
		Set<String> team2WinGameIds = new HashSet<String>();
		// GameID = matchId#gameIndex
		if (score[0] > 0) {
			for (int i = 0; i < score[0]; i++) {
				gameIndex++;
				team1WinGameIds.add(match.getId() + "#" + gameIndex);
			}
		}
		if (score[1] > 0) {
			for (int i = 0; i < score[1]; i++) {
				gameIndex++;
				team2WinGameIds.add(match.getId() + "#" + gameIndex);
			}
		}
		for (Player player : match.getTeam1().getPlayers()) {
			if (historyPlayerCache.get(player.getId()) == null) {
				historyPlayerCache.put(player.getId(), new TreeSet<String>());
			}
			historyPlayerCache.get(player.getId()).add(match.getId());
			
			winGamesCache.get(player.getId()).addAll(team1WinGameIds);
			gameIdsCache.get(player.getId()).addAll(team1WinGameIds);
			gameIdsCache.get(player.getId()).addAll(team2WinGameIds);
			if (isSolo) {
				soloGamesCache.get(player.getId()).addAll(team1WinGameIds);
				soloGamesCache.get(player.getId()).addAll(team2WinGameIds);
			}
		}
		for (Player player : match.getTeam2().getPlayers()) {
			if (historyPlayerCache.get(player.getId()) == null) {
				historyPlayerCache.put(player.getId(), new TreeSet<String>());
			}
			historyPlayerCache.get(player.getId()).add(match.getId());
			
			winGamesCache.get(player.getId()).addAll(team2WinGameIds);
			gameIdsCache.get(player.getId()).addAll(team1WinGameIds);
			gameIdsCache.get(player.getId()).addAll(team2WinGameIds);
			if (isSolo) {
				soloGamesCache.get(player.getId()).addAll(team1WinGameIds);
				soloGamesCache.get(player.getId()).addAll(team2WinGameIds);
			}
		}
	}
	
	public History getHistory() {
		return aoeLeagueData.getHistory();
	}
	public List<Match> getHistory(String groupId) {
		TreeSet<String> matchIds = historyGroupCache.get(groupId);
		if (Utilities.isEmpty(matchIds))
			return null;
		List<Match> results = new ArrayList<Match>();
		for (String matchId : matchIds) {
			results.add(matchesCache.get(matchId));
		}
		return results;
	}
	
	private Match getLatestMatch() {
		return matchesCache.lastEntry().getValue();
	}
	
	private TreeSet<String> getGameIdsFrom(Calendar fromCal, Integer playerId) {
		String fromCalStr = Constants.SIMPLE_ID_DATE_FORMAT.format(fromCal.getTime());
		Set<String> tailSet = gameIdsCache.get(playerId).tailSet(fromCalStr, true);
		if (tailSet == null || tailSet.isEmpty())
			return new TreeSet<String>();
		return new TreeSet<String>(tailSet);
	}
	
	public List<Match> getHistoryByPlayer(Integer playerId) {
		TreeSet<String> matchIds = historyPlayerCache.get(playerId);
		if (Utilities.isEmpty(matchIds))
			return null;
		List<Match> results = new ArrayList<Match>();
		for (String matchId : matchIds) {
			results.add(matchesCache.get(matchId));
		}
		return results;
	}
	
	void generateMatchId(Match match) {
		if (StringUtils.isNotBlank(match.getId())) {
			return;
		}
		boolean isMatchIdExist = true;
		int index = 0;
		String id = null;
		while(isMatchIdExist) {
			id = Utilities.dateToString(match.getDate(), Constants.SIMPLE_ID_DATE_FORMAT);
			id += "_" + StringUtils.leftPad(index + "", 2, "0");
			if (matchesCache.containsKey(id)) {
				index ++;
			} else {
				break;
			}
		}
		match.setId(id);
	}
	
	private void reloadPlayerInfoCache() {
		playerInfoCache.clear();
		for (Player player : aoeLeagueData.getPlayers()) {
			PlayerInfo playerInfo = createPlayerInfo(player);
			playerInfoCache.put(player.getId(), playerInfo);
		}
	}
	
	private PlayerInfo createPlayerInfo(Player player) {
		PlayerInfo playerInfo = convertPlayer(player);
		
		//set ava
		if (playerInfo.getAvatar() == null)
			playerInfo.setAvatar(Constants.AVATAR_URL_DEFAULT);

		// matches
		calculatePlayerMatches(playerInfo);
		
		return playerInfo;
	}
	
	private static PlayerInfo convertPlayer(Player player) {
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setId(player.getId());
		playerInfo.setName(player.getName());
		playerInfo.setAvatar(player.getAvatar());
		playerInfo.setPlayerPoint(player.getPlayerPoint());
		playerInfo.setRank(player.getRank());
		playerInfo.setTop(player.getTop());
		return playerInfo;
	}
	
	private void calculatePlayerMatches(PlayerInfo playerInfo) {
		PlayerAnalysis totalResult = new PlayerAnalysis();
		PlayerAnalysis soloResult = new PlayerAnalysis();
		PlayerAnalysis teamResult = new PlayerAnalysis();
		PlayerAnalysis recentResult = new PlayerAnalysis();
		PlayerAnalysis recentSoloResult = new PlayerAnalysis();
		PlayerAnalysis recentTeamResult = new PlayerAnalysis();
		
		playerInfo.setTotalResult(totalResult);
		playerInfo.setSoloResult(soloResult);
		playerInfo.setTeamResult(teamResult);
		playerInfo.setRecentResult(recentResult);
		playerInfo.setRecentSoloResult(recentSoloResult);
		playerInfo.setRecentTeamResult(recentTeamResult);
		
		TreeSet<String> winGameIds = winGamesCache.get(playerInfo.getId());
		TreeSet<String> totalGameIds = gameIdsCache.get(playerInfo.getId());
		TreeSet<String> soloGameIds = soloGamesCache.get(playerInfo.getId());
		TreeSet<String> soloWinGameIds = new TreeSet<String>(CollectionUtils.intersection(soloGameIds, winGameIds));
		
		totalResult.setMatchQuantity(totalGameIds.size());
		totalResult.setWinQuantity(winGameIds.size());
		totalResult.setLoseQuantity(totalResult.getMatchQuantity() - totalResult.getWinQuantity());
		
		soloResult.setMatchQuantity(soloGameIds.size());
		soloResult.setWinQuantity(soloWinGameIds.size());
		soloResult.setLoseQuantity(soloResult.getMatchQuantity() - soloResult.getWinQuantity());
		
		teamResult.setMatchQuantity(totalGameIds.size() - soloGameIds.size());
		teamResult.setWinQuantity(winGameIds.size() - soloWinGameIds.size());
		teamResult.setLoseQuantity(teamResult.getMatchQuantity() - teamResult.getWinQuantity());
		
		Set<String> recentGameIds = getRecentGameIds(playerInfo.getId());
		Set<String> recentWinGameIds = new TreeSet<String>(CollectionUtils.intersection(winGameIds, recentGameIds));
		Set<String> recentSoloGameIds = new TreeSet<String>(CollectionUtils.intersection(soloGameIds, recentGameIds));
		Set<String> recentSoloWinGameIds = new TreeSet<String>(CollectionUtils.intersection(soloWinGameIds, recentGameIds));
		
		recentResult.setMatchQuantity(recentGameIds.size());
		recentResult.setWinQuantity(recentWinGameIds.size());
		recentResult.setLoseQuantity(recentResult.getMatchQuantity() - recentResult.getWinQuantity());
		
		recentSoloResult.setMatchQuantity(recentSoloGameIds.size());
		recentSoloResult.setWinQuantity(recentSoloWinGameIds.size());
		recentSoloResult.setLoseQuantity(recentSoloResult.getMatchQuantity() - recentSoloResult.getWinQuantity());
		
		recentTeamResult.setMatchQuantity(recentGameIds.size() - recentSoloGameIds.size());
		recentTeamResult.setWinQuantity(recentWinGameIds.size() - recentSoloWinGameIds.size());
		recentTeamResult.setLoseQuantity(recentTeamResult.getMatchQuantity() - recentTeamResult.getWinQuantity());
		
		totalResult.setWinRateQuantity(
				calculateWinRate(totalResult.getWinQuantity(), totalResult.getMatchQuantity()));
		teamResult.setWinRateQuantity(
				calculateWinRate(teamResult.getWinQuantity(), teamResult.getMatchQuantity()));
		soloResult.setWinRateQuantity(
				calculateWinRate(soloResult.getWinQuantity(), soloResult.getMatchQuantity()));
		recentResult.setWinRateQuantity(
				calculateWinRate(recentResult.getWinQuantity(), recentResult.getMatchQuantity()));
		recentTeamResult.setWinRateQuantity(
				calculateWinRate(recentTeamResult.getWinQuantity(), recentTeamResult.getMatchQuantity()));
		recentSoloResult.setWinRateQuantity(
				calculateWinRate(recentSoloResult.getWinQuantity(), recentSoloResult.getMatchQuantity()));
		
		playerInfo.setPerformance(
				getPerformance(recentTeamResult.getWinRateQuantity(), recentTeamResult.getMatchQuantity()));
		if (Constants.PERFOMANCE_0.equals(playerInfo.getPerformance())
				&& (Constants.TYPE_NEWBIE.equals(playerInfo.getType()) || Constants.TYPE_NEWBIE_2.equals(playerInfo.getType()))) {
			playerInfo.setPerformance(Constants.PERFOMANCE_0_NEWBIE);	
		}
	}
	
	private double calculateWinRate(int winQuantity, int quantity) {
		if (quantity == 0) {
			return 0.0d;
		}
		double rate = (((double) winQuantity) / ((double) quantity)) * 100;
		DecimalFormat df = new DecimalFormat("#.#");
		String newRateStr = df.format(rate);
		return Double.valueOf(newRateStr);
	}
	
	private TreeSet<String> getRecentGameIds(Integer playerId) {
		Match latestMatch = getLatestMatch();
		Calendar latestCalendar = Utilities.toCalendar(latestMatch.getDate());
		latestCalendar.add(Calendar.DATE, Constants.RECENT_RESULT_DAYS * -1);
		latestCalendar.set(Calendar.HOUR, 0);
		latestCalendar.set(Calendar.MINUTE, 0);
		latestCalendar.set(Calendar.SECOND, 0);
		return getGameIdsFrom(latestCalendar, playerId);
	}
	
	private String getPerformance(double winRate, int matchQuantity) {
		if ((winRate >= 60 && matchQuantity >= 15)
				|| (winRate >= 70 && matchQuantity >= 10) ) {
			return Constants.PERFOMANCE_4;
		} else if ((winRate >= 53 && matchQuantity >= 10)
				|| (winRate >= 65 && matchQuantity >= 5) ) {
			return Constants.PERFOMANCE_3;
		} else if ((winRate < 35 && matchQuantity >= 15)
				|| (winRate < 25 && matchQuantity >= 10)) {
			return Constants.PERFOMANCE_0;
		} else if ((winRate < 47 && matchQuantity >= 10)
				|| (winRate < 35 && matchQuantity >= 5)) {
			return Constants.PERFOMANCE_1;
		} else if (matchQuantity == 0) {
			return Constants.PERFOMANCE_NOT_PLAY;
		} else {
			return Constants.PERFOMANCE_2;
		}
	}
}
