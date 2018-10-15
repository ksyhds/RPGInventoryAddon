package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener
{
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event)
	{
		Entity Target = event.getEntity();
		
		Player Damager = event.getEntity().getKiller();
		
		String Target_Name = Target.getCustomName();
		
		// [ Lv.1 ] 늑대
		String Target_Level_String = (Target_Name.split(".")[1]).replaceAll(" ", "");
		
		//1]늑대
		int Target_Level = Integer.parseInt(Target_Level_String.substring(0, Target_Level_String.charAt(']')));
		
		int Damager_Level = Damager.getLevel();
		
		if(Damager_Level < (Target_Level - 4))
		{
			event.setDroppedExp(0);
		}
	}
}
