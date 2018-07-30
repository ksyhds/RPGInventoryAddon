package com.gmail.Moon_Eclipse.RIA.RIA_Player;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.endlesscode.rpginventory.api.*;

public class RIAUtil 
{
	public static List<ItemStack> getActiveItemStackList(Player player)
	{
		return InventoryAPI.getActiveItems(player);
	}
	public static List<ItemStack> getPassiveItemStack(Player player)
	{
		return InventoryAPI.getPassiveItems(player);
	}
	
}
