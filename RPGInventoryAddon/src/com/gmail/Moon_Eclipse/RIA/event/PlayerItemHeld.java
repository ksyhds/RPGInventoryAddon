package com.gmail.Moon_Eclipse.RIA.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

public class PlayerItemHeld implements Listener
{	
	
	@EventHandler
	public void onChooseItemEvent(PlayerItemHeldEvent event)
	{
		// 플레이어가 핫바의 아이템을 변경했을때 발생하는 이벤트
		
		// 이벤트를 발생시킨 플레이어를 받아와 변수에 저장
		Player player = event.getPlayer();
		
		// 플레이어의 스탯 맵과 손에든 아이템 갱신
		RIAUtil.SetPlayerDataAndStat(player);
	}
}
