package com.sonic.aoeleague.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.analysis.function.Constant;

import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.model.PlayerInfo;
import com.sonic.aoeleague.model.Team;
import com.sonic.aoeleague.updatelog.UpdateLogService;
import com.sonic.aoeleague.util.AoeLeagueUtil;
import com.sonic.aoeleague.util.Constants;
import com.sonic.aoeleague.util.Utilities;

public class ResultService {
	private static AoeLeagueCache aoeLeagueCache = AoeLeagueCache.getInstance();
	private UpdateLogService updateLogService = new UpdateLogService();
	private NewbieService newbieService = new NewbieService();
	
	public void playWithoutAnalysis(Match match) {
		fillMatchInfo(match);
		aoeLeagueCache.addMatch(match);
		aoeLeagueCache.save();

	}

	public void play(Match match) {
		fillMatchInfo(match);
		
		updateLogService.newUpdateLog(match.getId());
		
		analysisMatch(match);
		aoeLeagueCache.addMatch(match);
		aoeLeagueCache.save();
		
		updateLogService.addUpdateLog();
	}
	
	/**
	 * test only
	 * @param match
	 */
	public void playWithoutSave(Match match) {
		fillMatchInfo(match);
		
		updateLogService.newUpdateLog(match.getId());
		
		analysisMatch(match);
		aoeLeagueCache.addMatch(match);
		//aoeLeagueCache.save();
		
		updateLogService.addUpdateLog();
	}
	
	public List<Match> getHistory(Match match) {
		AoeLeagueUtil.generateMatchGroupId(match);
		return aoeLeagueCache.getHistory(match.getGroupId());
	}
	
	private void fillMatchInfo(Match match) {
		normalizeScore(match);
		AoeLeagueUtil.generateMatchGroupId(match);
		setMapSize(match);
		if (match.getDate() == null) {
			match.setDate(Utilities.getCurrentTime());
		}
		aoeLeagueCache.generateMatchId(match);
	}
	
	private void setMapSize(Match match) {
		int playerQuantity = 0;
		playerQuantity += match.getTeam1().getPlayers().size();
		playerQuantity += match.getTeam2().getPlayers().size();
		if (playerQuantity >= 7) {
			match.setMap(Constants.MAP_GIGANTIC);
		} else if (playerQuantity >= 5) {
			match.setMap(Constants.MAP_HUGE);
		} else if (playerQuantity >= 2) {
			match.setMap(Constants.MAP_LARGE);
		}
		if (playerQuantity == 2) {
			match.setSolo(true);
		}
	}
	
	
	private void normalizeScore(Match match) {
		String[] resultSplitter = match.getResult().split("-");
		if (resultSplitter.length != 2)
			throw new RuntimeException("invalid score");
		
		String newScore = resultSplitter[0].trim() + "-" + resultSplitter[1].trim();
		match.setResult(newScore);
	}

	private void analysisMatch(Match match) {
		
		if (match == null || StringUtils.isBlank(match.getResult())) {
			return;
		}
		
		// analysis score
		String[] resultSplitter = match.getResult().split("-");
		if (resultSplitter.length != 2)
			return;

		int[] score = new int[2];
		try {
			score[0] = Integer.parseInt(resultSplitter[0].trim());
			score[1] = Integer.parseInt(resultSplitter[1].trim());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// analysis result
		if (score[0] > score[1]) {
			int scoreDifference = score[0] - score[1];
			for (int i = 0; i < scoreDifference; i++) {
				if (i >= Constants.UPDATE_POINT_LIMITATION_TIMES)
					break;
				analysisMatch(match.getTeam1(), match.getTeam2(), match.getMatchPoint() * -1, false, match);
			}
		}
		if (score[1] > score[0]) {
			int scoreDifference = score[1] - score[0];
			for (int i = 0; i < scoreDifference; i++) {
				if (i >= Constants.UPDATE_POINT_LIMITATION_TIMES)
					break;
				analysisMatch(match.getTeam2(), match.getTeam1(), match.getMatchPoint(), false, match);
			}
		}
		if (score[0] == score[1] && score[0] > 0) {
			for (int i = 0; i < score[0]; i++) {
				if (i >= Constants.UPDATE_POINT_LIMITATION_TIMES)
					break;
				analysisMatch(match.getTeam2(), match.getTeam1(), match.getMatchPoint(), true, match);
			}
		}
		
		// change player points
		for (Player player : match.getTeam1().getPlayers()) {
			aoeLeagueCache.getPlayer(player.getId()).setPlayerPoint(player.getPlayerPoint());
			updateLogService.updatePlayerPoint(player.getId(), player.getPlayerPoint());
		}
		for (Player player : match.getTeam2().getPlayers()) {
			aoeLeagueCache.getPlayer(player.getId()).setPlayerPoint(player.getPlayerPoint());
			updateLogService.updatePlayerPoint(player.getId(), player.getPlayerPoint());
		}
		
		refreshPlayers();
	}
	
	public void refreshPlayers() {
		List<Player> players = aoeLeagueCache.getListPlayers();
		for (Player player : players) {
			// set Top
			double playerPoint = player.getPlayerPoint();
			if (playerPoint >= 40) {
				player.setTop(0);
			} else if (playerPoint >= 30) {
				player.setTop(1);
			} else if (playerPoint >= 20) {
				player.setTop(2);
			} else if (playerPoint >= 10) {
				player.setTop(3);
			} else if (playerPoint >= 0) {
				player.setTop(4);
			}
			updateLogService.updatePlayerTop(player.getId(), player.getTop());
			
			PlayerInfo playerInfo = aoeLeagueCache.getPlayerInfo(player.getId());
			
			if (Constants.TYPE_NEWBIE.equalsIgnoreCase(player.getType())) {
				if (playerInfo.getTeamResult().getMatchQuantity() > Constants.NEWBIE_LIMIT_GAMES) {
					player.setType(Constants.TYPE_NEWBIE_2);
				}
			}
			if (Constants.TYPE_NEWBIE_2.equalsIgnoreCase(player.getType())) {
				if (playerInfo.getTeamResult().getMatchQuantity() > Constants.NEWBIE_2_LIMIT_GAMES) {
					player.setType(null);
				}
			}
			
		}
		
		List<Player> sortedPlayers = sortPlayersByPlayerPoint(players);
		aoeLeagueCache.getListPlayers().clear();
		aoeLeagueCache.getListPlayers().addAll(sortedPlayers);
	}
	
	private List<Player> sortPlayersByPlayerPoint(List<Player> players) {
		if (Utilities.isEmpty(players))
			return null;
		
		TreeMap<Double, List<Player>> playerMap = new TreeMap<Double, List<Player>>();
		for (Player player : players) {
			if (playerMap.get(player.getPlayerPoint()) == null)
				playerMap.put(player.getPlayerPoint(), new ArrayList<Player>());
			playerMap.get(player.getPlayerPoint()).add(player);
		}
		
		List<Player> results = new ArrayList<Player>();
		for (List<Player> listPlayer : playerMap.values()) {
			results.addAll(listPlayer);
		}
		Collections.reverse(results);
		int rank = 0;
		for (Player player : results) {
			rank ++;
			player.setRank(rank);
			updateLogService.updatePlayerRank(player.getId(), rank);
		}
		
		return results;
	}
	
	/**
	 * 
	 * @param winningTeam
	 * @param losingTeam
	 * @param differentMatchPoint = losingTeam - winningTeam
	 * @param isDraw - assume that winningTeam is team 2 draws with losingTeam team 1
	 */
	private void analysisMatch(Team winningTeam, Team losingTeam, double differentMatchPoint, boolean isDraw, Match match) {
		double e1 = calculateEloEquation(differentMatchPoint);
		double e2 = calculateEloEquation(differentMatchPoint * -1);
		double k_factor = Constants.K_FACTOR_INITIAL;
		boolean hasShang = (winningTeam.getNumOfShang() != null && winningTeam.getNumOfShang() > 0)
				|| (losingTeam.getNumOfShang() != null && losingTeam.getNumOfShang() > 0);
		if (Constants.MAP_LARGE.equals(match.getMap())) {
			k_factor = k_factor * Constants.K_FACTOR_LARGE_MAP;
		}
		if (hasShang) {
			k_factor = k_factor * Constants.K_FACTOR_SHANG;
		}
		// Change players' playerPoints
		for (Player player : winningTeam.getPlayers()) {
			double updatedPoint = 0;
			double playerPoint = player.getPlayerPoint();
			double winningFactor = isDraw ? 0.5 : 1;
			double k = calculateKFactorForPlayer(player, k_factor, winningFactor);
				
			updatedPoint = playerPoint + calculateKEquation(e1, k, winningFactor);
			
			player.setPlayerPoint(updatedPoint);
			System.out.println(player.getName() + " was added " + (updatedPoint - playerPoint));
		}
		
		for (Player player : losingTeam.getPlayers()) {
			double updatedPoint = 0;
			double playerPoint = player.getPlayerPoint();
			double winningFactor = isDraw ? 0.5 : 0;
			double k = calculateKFactorForPlayer(player, k_factor, winningFactor);
				
			updatedPoint = playerPoint + calculateKEquation(e2, k, winningFactor);
			
			player.setPlayerPoint(updatedPoint);
			System.out.println(player.getName() + " was added " + (updatedPoint - playerPoint));
		}
	}
	
	private double calculateKFactorForPlayer(Player player, double k, double winningFactor) {
		if (player.getTop() == 4)
			k = k * Constants.K_FACTOR_TOP_4;
		if (player.getTop() == 3)
			k = k * Constants.K_FACTOR_TOP_3;
		if (Constants.TYPE_NEWBIE.equalsIgnoreCase(player.getType()))
			k = k * Constants.K_FACTOR_NEWBIE;
		if (Constants.TYPE_NEWBIE_2.equalsIgnoreCase(player.getType()))
			k = k * Constants.K_FACTOR_NEWBIE_2;
		
		PlayerInfo playerInfo = aoeLeagueCache.getPlayerInfo(player.getId());
		if (Constants.PERFOMANCE_4.equals(playerInfo.getPerformance()) && winningFactor > 0.5) {
			k = k * Constants.K_FACTOR_SUPERB;
		}
		if ((Constants.PERFOMANCE_0.equals(playerInfo.getPerformance()) || Constants.PERFOMANCE_0_NEWBIE.equals(playerInfo.getPerformance()))
				&& winningFactor < 0.5) {
			k = k * Constants.K_FACTOR_DISAPPOINTED;
		}
		return k;
	}
	
	private double calculateEloEquation(double differentMatchPoint) {
		// E = 1 / (1 + 10^(d/10))
		return ((double) 1 / ((double) 1 + Math.pow(10, differentMatchPoint / Constants.INITIAL_RATING_DIFFERENCE)));
	}
	
	/**
	 * 
	 * @param e
	 * @param k
	 * @param winningFactor - win 1, lose 0, draw 0.5
	 * @return
	 */
	private double calculateKEquation(double e, double k, double winningFactor) {
		return k * (winningFactor - e);
	}
	
	/*public static void main(String[] args) {
		List<Match> matches = aoeLeagueCache.getHistory().getMatches();
		for (Match match : matches) {
			fillMatchInfo(match);
		}
		aoeLeagueCache.save();
	}*/
	
}
