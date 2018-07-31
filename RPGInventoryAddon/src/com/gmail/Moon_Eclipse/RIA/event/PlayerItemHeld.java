package com.gmail.Moon_Eclipse.RIA.event;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAUtil;

public class PlayerItemHeld implements Listener
{
	@EventHandler
	public void onChooseItemEvent(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		ItemStack HandItem = player.getItemInHand();

		Map<String, Integer> Attribute_Map = RIAPlayer.getAttributeMap();

		ItemStack new_Item = RIAUtil.DamageDataFixer(HandItem, Attribute_Map);

		player.setItemInHand(new_Item);

	}
}
