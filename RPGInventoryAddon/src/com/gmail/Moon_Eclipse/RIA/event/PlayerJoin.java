package com.gmail.Moon_Eclipse.RIA.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAUtil;

public class PlayerJoin implements Listener
{
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		// 플레이어가 서버에 접속했을때 발생하는 이벤트
		
		// 이벤트를 발생시킨 플레이어의 정보를 받아옴
		Player player = event.getPlayer();
		
		// 해당 플레이어의 기본 걷기 속도를 플러그인에서 지정받은 기본속도로 변경
		// 오류를 피하기 위해 로그인 직후에는 입고있는 장비와 상관없이 기본 속도로 고정.
		player.setWalkSpeed(RIAUtil.Default_Walk_Speed);
	}
}
