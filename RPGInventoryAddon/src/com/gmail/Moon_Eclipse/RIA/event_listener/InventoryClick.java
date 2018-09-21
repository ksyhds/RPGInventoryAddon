package com.gmail.Moon_Eclipse.RIA.event_listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

import ru.endlesscode.rpginventory.api.InventoryAPI;

public class InventoryClick implements Listener
{
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		// 플레이어가 인벤토리에서 아이템을 클릭하면 발생하는 이벤트
		
		// 이벤트를 발생시킨 인벤토리를 받아와 변수에 저장.
		Inventory inv = event.getInventory();
		
		// 이벤트를 발생시킨 플레이어를 받아와 변수에 저장.
		Player player = (Player) event.getWhoClicked();
		
		// 이벤트를 발생시킨 인벤토리가 RPG인벤토리인지 아닌지 확인
		if(InventoryAPI.isRPGInventory(inv))
		{
			// 플레이어의 레벨을 받아옴
			int Level = player.getLevel();
			
			// 마우스 커서에 있는 아이템을 가져옴
			ItemStack Cursor_Item = event.getCursor();
			
			
			// 상자 공간인지 플레이어 공간인지 구분하기위해 클릭한 곳의 번호를 구함
			Integer slotClicked = event.getRawSlot();
			
			// 만약 인벤토리 크기보다 클릭한곳이 작다면 상자 공간 
			if( slotClicked < inv.getSize()) 
			{
				// RPG 인벤토리 공간
				
				// 만약 레벨이 낮아서 아이템을 장비할 수 없다면
				if(!RIAUtil.CanEquipIt(Cursor_Item, Level))
				{
					// 플레이어에게 메세지 송출
					player.sendMessage(RIAStats.Level_Not_Enough_String_Prefix + Cursor_Item.getItemMeta().getDisplayName() + RIAStats.Level_Not_Enough_String_Suffix);
					
					// 이벤트 취소
					event.setCancelled(true);
					
					return;
				}
			}
		}
	}
}
