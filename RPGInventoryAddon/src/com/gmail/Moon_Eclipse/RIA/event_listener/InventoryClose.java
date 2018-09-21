package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
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
		
		// 현재 핫바의 위치를 저장
		int slot = player.getInventory().getHeldItemSlot();
		
		// 플레이어의 레벨을 받아와 저장
		int Level = player.getLevel();
		
		// 손에 들고있는 아이템을 가져옴 
		ItemStack Item_In_Hand = player.getEquipment().getItemInMainHand();
		
		// 만약 레벨이 낮아서 아이템을 장비할 수 없다면
		if(!RIAUtil.CanEquipIt(Item_In_Hand, Level))
		{
			// 플레이어에게 메세지 송출
			player.sendMessage(RIAStats.Level_Not_Enough_String_Prefix + Item_In_Hand.getItemMeta().getDisplayName() + RIAStats.Level_Not_Enough_String_Suffix);
			
			// 핫바의 위치를 변경
			player.getInventory().setHeldItemSlot((slot+1)%8);
			
			//더 이상 연산하지 않도록 리턴			
			return;
		}
		
		// 이벤트를 발생시킨 인벤토리가 RPG인벤토리인지 아닌지 확인
		if(InventoryAPI.isRPGInventory(inv))
		{
			// 플레이어의 스탯 맵과 손에든 아이템 갱신
			RIAUtil.SetPlayerDataAndStat(player, slot);
		}
	}
}
