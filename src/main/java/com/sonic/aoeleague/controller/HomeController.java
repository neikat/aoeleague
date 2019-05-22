package com.sonic.aoeleague.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sonic.aoeleague.model.AoeRequest;
import com.sonic.aoeleague.model.AoeResponse;
import com.sonic.aoeleague.model.Match;
import com.sonic.aoeleague.model.NickName;
import com.sonic.aoeleague.model.Player;
import com.sonic.aoeleague.model.PlayerInfo;
import com.sonic.aoeleague.service.AoeLeagueCache;
import com.sonic.aoeleague.service.GenerateMatchService;
import com.sonic.aoeleague.service.HistoryService;
import com.sonic.aoeleague.service.ResultService;
import com.sonic.aoeleague.util.Constants;
import com.sonic.aoeleague.util.Utilities;
@Controller
public class HomeController {
    private static AoeLeagueCache aoeLeagueCache = AoeLeagueCache.getInstance();

    @RequestMapping("/aoe")
    public String  aoe(Model mav) {
        List<String> players = aoeLeagueCache.getListPlayerNames();
        mav.addAttribute("players", players);
        return "match";
    }

   /* @RequestMapping("/player")
    @ResponseBody
    public List<Player> getPlayer() {
        return aoeLeagueCache.getListPlayers();
    }*/

    @RequestMapping("/chiakeo")
    @ResponseBody
    public AoeResponse chiakeo(@RequestBody NickName info) {
        System.out.println(info.getId());
        AoeResponse response = new AoeResponse();
        try {
            List<Match> results = GenerateMatchService.generateMatches(info.getId(), 30);
            if (!Utilities.isEmpty(results)) {
            	response.getMatchList().addAll(results);
            }
            response.setStatus(Constants.STATUS_SUCCESS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	response.setStatus(Constants.STATUS_FAILURE);
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping("/score")
    @ResponseBody
    public AoeResponse score(@RequestBody AoeRequest request) {
    	AoeResponse response = new AoeResponse();
        try {
            Match match = request.getMatch();
			match.setResult(request.getScore());
			ResultService resultService = new ResultService();
			resultService.play(match);
			response.setStatus(Constants.STATUS_SUCCESS);
        } catch (Exception e) {
        	response.setStatus(Constants.STATUS_FAILURE);
            e.printStackTrace();
        }
        return response;
    }
    
    @RequestMapping("/player")
    public String getPlayerInfo(@RequestParam(name="id") Integer id, Model mav) {
    	PlayerInfo playerInfo = HistoryService.getPlayerInfo(id);
    	if (playerInfo == null)
    		return "player_info_empty";
        mav.addAttribute("player", playerInfo);
        return "player_info";
    }
    
    @RequestMapping("/table")
    public String getPlayerList(Model mav) {
    	List<PlayerInfo> players = HistoryService.getPlayerInfoList();
        mav.addAttribute("players", players);
        return "table";
    }
    
    @RequestMapping("/history/match")
    @ResponseBody
    public AoeResponse matchHistory(@RequestBody AoeRequest request) {
    	AoeResponse response = new AoeResponse();
        try {
            Match match = request.getMatch();
			List<Match> matches = HistoryService.getMatchHistory(match);
			if (matches != null) {
				String text = "";
				for (Match element : matches) {
					text += GenerateMatchService.getMatchWithScoreAsString(element);
					text += "\n";
				}
				response.setText(text);
			}
			response.setStatus(Constants.STATUS_SUCCESS);
        } catch (Exception e) {
        	response.setStatus(Constants.STATUS_FAILURE);
            e.printStackTrace();
        }
        return response;
    }
    
}