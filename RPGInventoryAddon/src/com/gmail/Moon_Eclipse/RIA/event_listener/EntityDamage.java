package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.Moon_eclipse.EclipseLib.LibMain;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

public class EntityDamage implements Listener
{
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		// 데미지를 받은 대상을 받아와 저장
		Entity Target = event.getEntity();
		
		// 만약 대상이 플레이어라면
		if(Target instanceof Player)
		{
			
			// 데미지 출처를 얻어와 저장
			DamageCause cause = event.getCause();
			
			// 플레이어를 저장
			Player player = (Player) Target;
			
			
			
			// 플레이어의 최대 체력을 받아와 저장
			double Max_Health = player.getMaxHealth();
			
			// 데미지 버퍼를 생성
			double damage = 0d;
			
			// 만약 데미지를 받은 경우가 블럭이 내려와서 생긴 경우라면
			switch(cause)
			{
				// 질식의 경우
				case SUFFOCATION:
					damage= RIAUtil.getPercented_PlayerMaxHealth(Max_Health, RIAStats.Entity_Damage_Event_Coefficient);
					event.setDamage(damage);
				break;
				
				// 용암에 빠진 경우
				case LAVA:
					damage = RIAUtil.getPercented_PlayerMaxHealth(Max_Health, RIAStats.Entity_Damage_Event_Coefficient);
					event.setDamage(damage);
				break;
				
				// 물에서 익사하는 경우
				case DROWNING:
					damage = RIAUtil.getPercented_PlayerMaxHealth(Max_Health, RIAStats.Entity_Damage_Event_Coefficient);
					event.setDamage(damage);
				break;
				
				// 높은곳에서 떨어진 경우
				case FALL:
					// 이벤트가 발생할 경우 4칸 위에 있어도 4칸위에서 뛰면 3.34가 나오는 버그가 있음. 따라서 높이 값을 최소 4로 조절함
					// 이벤트가 발생할 최소 조건은 적어도 4칸 위에서 떨어져 내렸을때이기 때문.
					double Fall_Distance = Math.round(player.getFallDistance());
					
					if (Fall_Distance < 4)  Fall_Distance= 4.0d;
					
					// 높이 각 1당 체력 2%이므로 
					double percent = Fall_Distance * 2.0d;
						
					// 계산될 데미지를 가져옴
					damage = RIAUtil.getPercented_PlayerMaxHealth(Max_Health, percent);
					
					// 추락 데미지 설정
					event.setDamage(damage);					
					
				break;
			}
		}
		
		
	}
}
