package com.sonic.aoeleague.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Constants {
	public static String PATH_DATA_AOELEAGUE = "data/aoeleague-data.xml";
	public static String PATH_UPDATE_LOG = "data/aoeleague-update-log.xml";
	public static String PATH_GRAPH = "data/aoeleague-graph.xml";
	public static String STATUS_SUCCESS = "SUCCESS";
	public static String STATUS_FAILURE = "FAILURE";

	public static final SimpleDateFormat SIMPLE_ID_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd'_'HHmmss'_'SSS");
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	public static final TimeZone TIME_ZONE = TimeZone.getDefault();
	public static double PLAYER_POINT_MULTIPLIER = 0.8;
	public static double MAX_GROUP_BONUS_PERCENTAGE = 80;
	public static double INITIAL_RATING_DIFFERENCE = 10;
	public static int UPDATE_POINT_LIMITATION_TIMES = 3;
	public static double K_FACTOR_INITIAL = 0.2;
	public static double K_FACTOR_LARGE_MAP = 0.5;
	public static double K_FACTOR_SHANG = 0.5;
	public static double K_FACTOR_TOP_4 = 2;
	public static double K_FACTOR_TOP_3 = 1.3;
	public static double K_FACTOR_NEWBIE = 3;
	public static double K_FACTOR_NEWBIE_2 = 2;
	public static double K_FACTOR_DISAPPOINTED = 1.3;
	public static double K_FACTOR_SUPERB = 1.3;
	
	public static String MAP_LARGE = "Large";
	public static String MAP_HUGE = "Huge";
	public static String MAP_GIGANTIC = "Gigantic";
	
	public static String PERFOMANCE_0 = "Disappointed";
	public static String PERFOMANCE_0_NEWBIE = "Keep Fighting";
	public static String PERFOMANCE_1 = "Poor";
	public static String PERFOMANCE_2 = "Normal";
	public static String PERFOMANCE_3 = "Good";
	public static String PERFOMANCE_4 = "Superb";
	public static String PERFOMANCE_NOT_PLAY = "N/A";
	
	public static String TYPE_NEWBIE = "newbie";
	public static String TYPE_NEWBIE_2 = "newbie2";
	public static int NEWBIE_LIMIT_GAMES = 15;
	public static int NEWBIE_2_LIMIT_GAMES = 30;
	
	public static String AVATAR_URL_DEFAULT = "../img/avatar/default_ava.jpg";
	public static int RECENT_RESULT_DAYS = 7;
	static {
		SIMPLE_ID_DATE_FORMAT.setTimeZone(TIME_ZONE);
		SIMPLE_DATE_FORMAT.setTimeZone(TIME_ZONE);
	}
}
