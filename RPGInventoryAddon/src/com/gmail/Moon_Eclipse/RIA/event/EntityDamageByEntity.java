package com.gmail.Moon_Eclipse.RIA.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener 
{
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event)
	{
		// 활은 사용하지 않음
		// 이번 서버에서 공격력 스탯은 3개가 존재함. "공격력", "일반 공격력", "스킬 공격력"
		// 이들 일반, 스킬 공격력들은 "공격력" 스탯이 있을경우 "공격력" 스탯의 값이 추가되어 데미지가 적용되야함. 일종의 Offset으로 생각하면 편함.
		// 이 "공격력" 스탯은 맨손일때도 적용되야 하므로 데미지 연산의 가장 마지막에 혹은 가장 처음에 더해두면 될 듯 함.
	}
}
	