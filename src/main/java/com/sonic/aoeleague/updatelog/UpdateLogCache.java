package com.sonic.aoeleague.updatelog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sonic.aoeleague.dao.DataExporter;
import com.sonic.aoeleague.dao.DataImporter;
import com.sonic.aoeleague.model.UpdateLog;
import com.sonic.aoeleague.model.UpdateLogData;
import com.sonic.aoeleague.util.Constants;
import com.sonic.aoeleague.util.Utilities;

public class UpdateLogCache {
	private UpdateLogData updateLogData = null;
	private Map<String, UpdateLog> updateLogCache = new ConcurrentHashMap<String, UpdateLog>();
	private static UpdateLogCache instance = null;

	private UpdateLogCache() {
		load();
	}
	
	public static UpdateLogCache getInstance() {
		if (instance == null) {
			instance = new UpdateLogCache();
		}
		return instance;
	}
	
	private void load() {
		updateLogData = (UpdateLogData) DataImporter.importXml(Constants.PATH_UPDATE_LOG, UpdateLogData.class);
		if (updateLogData != null) {
			for (UpdateLog updateLog : updateLogData.getUpdateLogs()) {
				updateLogCache.put(updateLog.getMatchId(), updateLog);
			}
		} else {
			updateLogData = new UpdateLogData();
		}
	}
	
	void save() {
		updateLogData.setUpdatedTime(Utilities.getCurrentTime());
		DataExporter.exportToXMl(updateLogData, Constants.PATH_UPDATE_LOG, UpdateLogData.class);
	}
	
	void addUpdateLog(UpdateLog updateLog) {
		updateLogData.getUpdateLogs().add(updateLog);
		updateLogCache.put(updateLog.getMatchId(), updateLog);
		save();
	}
}
