package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

public class PlayerInteract implements Listener
{
	@EventHandler(priority =EventPriority.LOW)
	public void onQuickEquip(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		// 현재 핫바의 위치를 저장
		int slot = player.getInventory().getHeldItemSlot();
		
		// 플레이어의 스탯 맵과 손에든 아이템 갱신
		RIAUtil.SetPlayerDataAndStat(player, slot);
		
		Material HandItem = player.getEquipment().getItemInMainHand().getType();
		
		if(HandItem.equals(Material.ELYTRA))
		{
			event.setCancelled(true);
		}
	}
}
