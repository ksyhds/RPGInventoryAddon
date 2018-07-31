package com.gmail.Moon_Eclipse.RIA.event;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAUtil;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;

import ru.endlesscode.rpginventory.api.InventoryAPI;

public class InventoryClose implements Listener
{
	WrapperManager wm = new WrapperManager();
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent event)
	{
		Inventory inv = event.getInventory();
		
		Player player = (Player) event.getPlayer();

		if(InventoryAPI.isRPGInventory(inv))
		{
			List<ItemStack> Armors = RIAUtil.getPlayerArmor(player);

			if(!Armors.isEmpty())
			{
				Map<String,Integer> AttributeMap = RIAUtil.ArmorDamataExtractor(Armors);

				ItemStack HandItem = player.getItemInHand();

				ItemStack new_HandItem = RIAUtil.DamageDataFixer(HandItem, AttributeMap);

				wm.getRIAPlayer(player).setAttributeMap(AttributeMap);

				player.setItemInHand(new_HandItem);
			}
		}
	}
}
