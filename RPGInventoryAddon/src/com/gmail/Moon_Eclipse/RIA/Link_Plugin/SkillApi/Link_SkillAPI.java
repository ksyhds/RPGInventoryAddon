package com.gmail.Moon_Eclipse.RIA.Link_Plugin.SkillApi;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

public class Link_SkillAPI 
{
	public static double getPlayerClassBaseHelath(Player player)
	{
		double ClassBaseHealth = 0;
		if(SkillAPI.getPlayerData(player).getMainClass() != null)
		{
			ClassBaseHealth = SkillAPI.getPlayerData(player).getMainClass().getData().getBaseHealth();
		}
		
		return ClassBaseHealth;
	}
	public static int getSkillAPIHealthPerLevel(Player player)
	{
		int healthPerLevel = 0;
		
		if(SkillAPI.getPlayerData(player).getMainClass() != null)
		{
			healthPerLevel = (int)SkillAPI.getPlayerData(player).getMainClass().getData().getHealthScale();
		}
		return healthPerLevel;
	}

	public static int getSkillAPILevel(Player player)
	{
		int level = 0;

		if(SkillAPI.getPlayerData(player).getMainClass() != null) 
		{
			level = SkillAPI.getPlayerData(player).getMainClass().getLevel();
		}
		
		return level-1;
	}
	public static double getPlayerBaseMaxHealth(Player player)
	{
		double MaxHealthBase = 0d;
		
		double ClassBaseHealth = getPlayerClassBaseHelath(player);
		
		int HealthScalePerLevel = getSkillAPIHealthPerLevel(player);
		
		int PlayerAPILevel = getSkillAPILevel(player);
		
		MaxHealthBase = ClassBaseHealth + HealthScalePerLevel * PlayerAPILevel;
		
		return MaxHealthBase;
	}
}
