package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
			ItemStack Cusor_Item = event.getCursor();
			
			// 만약 마우스 커서에 아이템이 있다면
			if(!(Cusor_Item == null || Cusor_Item.getType().equals(Material.AIR)))
			{
				Bukkit.broadcastMessage(Cusor_Item.toString());
			}
		}
	}
}
