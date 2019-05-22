package com.sonic.aoeleague.updatelog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.model.UpdateLog;
import com.sonic.aoeleague.model.UpdatePlayer;
import com.sonic.aoeleague.model.UpdatePlayerInfo;
import com.sonic.aoeleague.service.AoeLeagueCache;

public class UpdateLogService {
	private UpdateLog currentUpdateLog = null;
	private AoeLeagueCache aoeLeagueCache = AoeLeagueCache.getInstance();
	private UpdateLogCache updateLogCache = UpdateLogCache.getInstance();
	private Map<Integer, UpdatePlayer> updatePlayerCache = new HashMap<Integer, UpdatePlayer>();
	
	public void newUpdateLog(String matchId) {
		currentUpdateLog = new UpdateLog();
		currentUpdateLog.setMatchId(matchId);
		updatePlayerCache.clear();
		List<Player> players = aoeLeagueCache.getListPlayers();
		for (Player player : players) {
			UpdatePlayer updatePlayer = createUpdatePlayer(player);
			currentUpdateLog.getUpdatePlayers().add(updatePlayer);
			updatePlayerCache.put(player.getId(), updatePlayer);
		}
	}
	
	public void addUpdateLog() {
		if (currentUpdateLog != null) {
			List<UpdatePlayer> tempUpdatePlayers = new ArrayList<UpdatePlayer>(currentUpdateLog.getUpdatePlayers());
			for (UpdatePlayer updatePlayer : tempUpdatePlayers) {
				if (updatePlayer.getNew() == null) {
					currentUpdateLog.getUpdatePlayers().remove(updatePlayer);
				}
			}
			
			updateLogCache.addUpdateLog(currentUpdateLog);
			currentUpdateLog = null;
			updatePlayerCache.clear();
		}
	}

	public void updatePlayerPoint(Integer playerId, double playerPoint) {
		UpdatePlayer updatePlayer = updatePlayerCache.get(playerId);
		if (updatePlayer == null)
			return;
		if (updatePlayer.getNew() == null) {
			updatePlayer.setNew(new UpdatePlayerInfo());
		}
		updatePlayer.getNew().setPlayerPoint(playerPoint);
	}
	
	public void updatePlayerTop(Integer playerId, int top) {
		UpdatePlayer updatePlayer = updatePlayerCache.get(playerId);
		if (updatePlayer == null)
			return;
		if (top == updatePlayer.getOld().getTop()) {
			return;
		}
		if (updatePlayer.getNew() == null) {
			updatePlayer.setNew(new UpdatePlayerInfo());
		}
		updatePlayer.getNew().setTop(top);
	}
	
	public void updatePlayerRank(Integer playerId, int rank) {
		UpdatePlayer updatePlayer = updatePlayerCache.get(playerId);
		if (updatePlayer == null)
			return;
		if (updatePlayer.getOld().getRank() != null && rank == updatePlayer.getOld().getRank()) {
			return;
		}
		if (updatePlayer.getNew() == null) {
			updatePlayer.setNew(new UpdatePlayerInfo());
		}
		updatePlayer.getNew().setRank(rank);
	}

	private UpdatePlayer createUpdatePlayer(Player player) {
		UpdatePlayer updatePlayer = new UpdatePlayer();
		updatePlayer.setId(player.getId());
		updatePlayer.setOld(createUpdatePlayerInfo(player));
		return updatePlayer;
	}
	
	private UpdatePlayerInfo createUpdatePlayerInfo(Player player) {
		UpdatePlayerInfo updatePlayerInfo = new UpdatePlayerInfo();
		updatePlayerInfo.setPlayerPoint(player.getPlayerPoint());
		updatePlayerInfo.setRank(player.getRank());
		updatePlayerInfo.setTop(player.getTop());
		
		return updatePlayerInfo;
	}
	
	
}
