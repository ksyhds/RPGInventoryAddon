package com.gmail.Moon_Eclipse.RIA.RIA_Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.endlesscode.rpginventory.api.*;

import com.Moon_eclipse.EclipseLib.*;

public class RIAUtil
{
	
	public static List<String> Attribute_Names;

	public static Map<String, Integer> AttributeMap = new HashMap<String, Integer>();

	

	public static void ResetAttributeMap()
	{
		for(String string : Attribute_Names)
		{
			AttributeMap.put(string,0);
		}
	}

	public static List<ItemStack> getActiveItemStackList(Player player)
	{
		return InventoryAPI.getActiveItems(player);
	}

	public static List<ItemStack> getPassiveItemStack(Player player)
	{
		return InventoryAPI.getPassiveItems(player);
	}

	public static String[] Splitter(String lore)
	{
		// 스킬 공격력: 10
		String[] args = lore.split(":");
		return args;
	}

	public static ItemStack DamageDataFixer(ItemStack item, Map<String, Integer> map)
	{
		ResetAttributeMap();

		List<String> New_Lore = new ArrayList<String>();
		if(item.hasItemMeta())
		{
			ItemMeta meta = item.getItemMeta();
		
			List<String> Lores = meta.getLore();
			
			for(String lore : Lores)
			{
				String Attribute_name = Splitter(lore)[0];
				int att_value = AttributeMap.get(Attribute_name);

				if(LibMain.hasString(Attribute_Names, Attribute_name))
				{
					String new_att = Attribute_name + ": " + att_value;
					New_Lore.add(new_att);
				}
				else
				{
					New_Lore.add(lore);
				}
			}
			meta.setLore(New_Lore);
			item.setItemMeta(meta);
			return item;
		}
		else
		{
			return item;
		}
	}

	public static List<ItemStack> getPlayerArmor(Player player)
	{
		// 실험을 통해 손에 들고 있는 아이템이 포함되는지 확인해봐야함.

		List<ItemStack> ActiveItems = getActiveItemStackList(player);
		List<ItemStack> PassiveItems = getPassiveItemStack(player);

		List<ItemStack> re = new ArrayList<ItemStack>();

		for(ItemStack i : ActiveItems)
		{
			re.add(i);
		}
		for(ItemStack i : PassiveItems)
		{
			re.add(i);
		}

		return re;
	}

	public static Map<String,Integer> ArmorDamataExtractor(List<ItemStack> items)
	{
		ResetAttributeMap();

		for(ItemStack item : items)
		{
			if(item.hasItemMeta())
			{
				ItemMeta meta = item.getItemMeta();

				if(meta.hasLore())
				{
					List<String> Lores = meta.getLore();

					for(String lore : Lores)
					{
						if(LibMain.hasString(Attribute_Names, lore))
						{
							
							String[] args = Splitter(lore);
							
							String Attribute_Name = args[0];
							String Att_value_string = args[1].replace(" ","");
							
							int Att_Value_a = Integer.parseInt(Att_value_string);
							int Att_Value_b = AttributeMap.get(Attribute_Name);
							int Att_Value_c = Att_Value_a + Att_Value_b;

							AttributeMap.put(Attribute_Name,Att_Value_c);
						}
					}
					
				}
				else
				{
					continue;
				}
			}
			else
			{
				continue;
			}
		}

		return AttributeMap;
	}
	
	
}
