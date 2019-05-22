package com.sonic.aoeleague;

import java.util.List;

import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.service.AoeLeagueCache;
import com.sonic.aoeleague.service.GenerateMatchService;
import com.sonic.aoeleague.service.ResultService;
import com.sonic.aoeleague.util.Constants;
import com.sonic.aoeleague.util.Utilities;

public class MainApp {
	/*public static void main(String[] args) {
		try {
			List<Match> results = GenerateMatchService.generateMatches("Hieupx Farmer bikini crooks V Sonic3k steve peotuan", 40);
			for (Match match : results) {
					
				System.out.println(GenerateMatchService.getMatchAsString(match));
				
			}
			Match match = results.get(1);
			match.setResult("0-1");
			match.getTeam2().setNumOfShang(0);
			match.getTeam1().setNumOfShang(0);
			match.setDate(Utilities.fromStringToXMLCalendar("2017-02-02T12:45:00.000Z", Constants.SIMPLE_DATE_FORMAT));
			ResultService resultService = new ResultService();
			resultService.play(match);
			List<Match> history = ResultService.getHistory(match);
			System.out.println("History:");
			for (Match hist : history) {
				
				System.out.println(GenerateMatchService.getMatchAsString(hist));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}
