package com.gmail.Moon_Eclipse.RIA.Link_Plugin.SkillApi.EventListener;

import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.sucy.skill.api.event.SkillDamageEvent;

public class SkillDamage implements Listener
{
	@EventHandler
	public void onSkillDamage(SkillDamageEvent event)
	{
		LivingEntity damager = event.getDamager();
		if(damager instanceof Player)
		{
			Player Player_Damager = (Player) damager;
			
			RIAPlayer rp = WrapperManager.getRIAPlayer(Player_Damager.getName());
			
			Map<String, Double> map = rp.getAttributeMap();
			
			map.put("ENTITY_ATTACK_FLAG", 1d);
			
			rp.setAttributeMap(map);
		}

	}
}
