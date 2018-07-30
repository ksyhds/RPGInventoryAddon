package com.gmail.Moon_Eclipse.RIA.RIA_Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class WrapperManager implements Listener
{
	// RIA 플레이어와 기존 플레이어의 연결을 위해 맵 생성
	public Map<Player, RIAPlayer> RIAPlayerMap;
	public static Plugin plugin;
	
	//생성자 선언
	public WrapperManager()
	{
		// 래퍼 매니저 객체를 사용할때 맵을 초기화
		RIAPlayerMap = new HashMap<Player, RIAPlayer>();
	}
	// 플레이어를 통해 RIA 플레이어를 얻어와 반환함.
	public RIAPlayer getRIAPlayer(Player player)
	{
		return RIAPlayerMap.get(player);
	}
	// 맵의 정보를 온라인 중인 플레이어만으로 다시 채워주는 함수
	public void wrapCollection(Collection<? extends Player> players)
	{
		clear();
		players.stream().forEach(p -> RIAPlayerMap.put(p, new RIAPlayer(p)));
	}
	// 실질적으로 사용되는 리필 함수
	public void WrapAllonline()
	{
		wrapCollection(Bukkit.getOnlinePlayers());
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		// 플레이어가 로그인시 맵에 데이터를 저장
		RIAPlayerMap.put(event.getPlayer(), new RIAPlayer(event.getPlayer()));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		// 플레이어가 로그아웃시 맵에서 정보를 삭제
		RIAPlayerMap.remove(event.getPlayer());
	}
	public void clear()
	{
		// 맵을 초기화.
		RIAPlayerMap.clear();
	}
	
}
