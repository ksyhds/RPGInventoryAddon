package com.gmail.Moon_Eclipse.RIA.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

import ru.endlesscode.rpginventory.api.InventoryAPI;

public class InventoryClose implements Listener
{	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent event)
	{
		// 플레이어가 인벤토리를 닫으면 발생하는 이벤트
		
		// 이벤트를 발생시킨 인벤토리를 받아와 변수에 저장.
		Inventory inv = event.getInventory();
		
		// 이벤트를 발생시킨 플레이어를 받아와 변수에 저장.
		Player player = (Player) event.getPlayer();
		
		// 이벤트를 발생시킨 인벤토리가 RPG인벤토리인지 아닌지 확인
		if(InventoryAPI.isRPGInventory(inv))
		{
			// 플레이어의 스탯 맵과 손에든 아이템 갱신
			RIAUtil.SetPlayerDataAndStat(player, player.getInventory().getHeldItemSlot());
		}
	}
}
