package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamage implements Listener
{
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		// 데미지 출처를 얻어와 저장
		DamageCause cause = event.getCause();
		// 만약 데미지를 받은 경우가 블럭이 내려와서 생긴 경우라면
		switch(cause)
		{
			case SUFFOCATION:
				
			break;
			
			case LAVA:
			break;
			
			case DROWNING:
			break;
			
			case FALL:
			break;
		}
	}
}
