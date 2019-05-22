package com.sonic.aoeleague.service;

import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.sonic.aoeleague.graph.GraphCache;
import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.NewbieGraph;
import com.sonic.aoeleague.model.NewbieGraphElement;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.util.Utilities;

public class NewbieService {
	private static AoeLeagueCache aoeLeagueCache = AoeLeagueCache.getInstance();
	private static GraphCache graphCache = GraphCache.getInstance();
	
	public double getUpdatedPlayerPoint(Player player, Match match) {
		boolean hasWon = false;
		boolean hasLost = false;
		NewbieGraphElement newbieGraphElement = addNewbieGraph(player.getId(), match);
		SimpleRegression simpleRegression = new SimpleRegression();
		NewbieGraph newbieGraph = graphCache.getNewbieGraph(player.getId());
		if (newbieGraph != null) {
			for (NewbieGraphElement element : newbieGraph.getNewbieGraphElements()) {
				simpleRegression.addData(element.getResult(), element.getAssumedPlayerPoint());
				if (element.getResult() > 0) {
					hasWon = true;
				}
				if (element.getResult() < 0) {
					hasLost = true;
				}
			}
		}
		
		Double updatedPoint = simpleRegression.predict(0);
		if (updatedPoint.isNaN() || !(hasWon && hasLost)) {
			double thisAssumedPlayerPoint = newbieGraphElement.getAssumedPlayerPoint();
			int thisResult = newbieGraphElement.getResult();
			double check = (thisAssumedPlayerPoint - player.getPlayerPoint()) * thisResult;
			if (check > 0) {
				return (thisAssumedPlayerPoint + thisResult);
			} else if (check < 0) {
				return (player.getPlayerPoint() + thisResult);
			} else {
				return thisAssumedPlayerPoint;
			}
		}
		return updatedPoint;
	}
	
	public void refreshNewbieGraph(Integer playerId, List<Match> matches) {
		graphCache.cleanNewbieGraph(playerId);
		for (Match match : matches) {
			addNewbieGraph(playerId, match);
		}
	}
	
	private NewbieGraphElement addNewbieGraph(Integer playerId, Match match) {
		double team1Points = match.getTeam1().getTeampoint();
		double team2Points = match.getTeam2().getTeampoint();
		int [] score = Utilities.getScore(match.getResult());
		for (Player player : match.getTeam1().getPlayers()) {
			if (playerId.equals(player.getId())) {
				int result = score[0] - score[1];
				return addNewbieGraph(team1Points, team2Points, result, playerId, player.getPlayerPoint(), match.getId());
			}
		}
		for (Player player : match.getTeam2().getPlayers()) {
			if (playerId.equals(player.getId())) {
				int result = score[1] - score[0];
				return addNewbieGraph(team2Points, team1Points, result, playerId, player.getPlayerPoint(), match.getId());
			}
		}
		return null;
	}
	
	private NewbieGraphElement addNewbieGraph(double teamPoint, double opponentTeamPoint, int result, Integer playerId, double playerPoint, String matchId) {
		double assumedPlayerPoint = opponentTeamPoint - (teamPoint - playerPoint);
		return graphCache.addNewbieGraph(playerId, matchId, result, assumedPlayerPoint);
	}
}
