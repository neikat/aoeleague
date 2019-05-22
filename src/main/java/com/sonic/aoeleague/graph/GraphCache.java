package com.sonic.aoeleague.graph;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sonic.aoeleague.dao.DataExporter;
import com.sonic.aoeleague.dao.DataImporter;
import com.sonic.aoeleague.model.GraphData;
import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.NewbieGraph;
import com.sonic.aoeleague.model.NewbieGraphElement;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.util.Constants;
import com.sonic.aoeleague.util.Utilities;

public class GraphCache {
	private GraphData graphData = null;
	private Map<Integer, NewbieGraph> newbieGraphCache = new ConcurrentHashMap<Integer, NewbieGraph>();
	private static GraphCache instance = null;

	private GraphCache() {
		load();
	}
	
	public static GraphCache getInstance() {
		if (instance == null) {
			instance = new GraphCache();
		}
		return instance;
	}
	
	private void load() {
		graphData = (GraphData) DataImporter.importXml(Constants.PATH_GRAPH, GraphData.class);
		if (graphData != null) {
			for (NewbieGraph newbieGraph : graphData.getNewbieGraphs()) {
				newbieGraphCache.put(newbieGraph.getPlayerId(), newbieGraph);
			}
		} else {
			graphData = new GraphData();
		}
	}
	
	void save() {
		graphData.setUpdatedTime(Utilities.getCurrentTime());
		DataExporter.exportToXMl(graphData, Constants.PATH_GRAPH, GraphData.class);
	}
	
	public NewbieGraphElement addNewbieGraph(Integer playerId, String matchId, int result, double assumedPlayerPoint) {
		NewbieGraphElement newbieGraphElement = new NewbieGraphElement();
		newbieGraphElement.setAssumedPlayerPoint(assumedPlayerPoint);
		newbieGraphElement.setMatchId(matchId);
		newbieGraphElement.setResult(result);
		
		NewbieGraph newbieGraph = newbieGraphCache.get(playerId);
		if (newbieGraph == null) {
			newbieGraph = new NewbieGraph();
			newbieGraph.setPlayerId(playerId);
			newbieGraphCache.put(playerId, newbieGraph);
			graphData.getNewbieGraphs().add(newbieGraph);
		}
		newbieGraph.getNewbieGraphElements().add(newbieGraphElement);
		save();
		return newbieGraphElement;
	}
	
	
	
	public NewbieGraph getNewbieGraph(Integer playerId) {
		return newbieGraphCache.get(playerId);
	}
	
	public void cleanNewbieGraph(Integer playerId) {
		NewbieGraph newbieGraph = newbieGraphCache.get(playerId);
		if (newbieGraph != null) {
			newbieGraph.getNewbieGraphElements().clear();
		}
	}
}
