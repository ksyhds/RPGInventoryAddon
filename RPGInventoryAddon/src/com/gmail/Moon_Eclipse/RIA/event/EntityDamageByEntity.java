package com.gmail.Moon_Eclipse.RIA.event;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageByEntity implements Listener 
{
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event)
	{
		// 활은 사용하지 않음
		// 이번 서버에서 공격력 스탯은 3개가 존재함. "공격력", "일반 공격력", "스킬 공격력"
		// 이들 일반, 스킬 공격력들은 "공격력" 스탯이 있을경우 "공격력" 스탯의 값이 추가되어 데미지가 적용되야함. 일종의 Offset으로 생각하면 편함.
		// 이 "공격력" 스탯은 맨손일때도 적용되야 하므로 데미지 연산의 가장 마지막에 혹은 가장 처음에 더해두면 될 듯 함.
		
		
		// 맞는 대상인 타겟을 얻어와 저장
		Entity target = event.getEntity();
		
		// 데미지를 입히는 주체인 공격자를 얻어와 저장
		LivingEntity damager = (LivingEntity) event.getDamager();
		
		// 만약 공격자가 투사체 라면
		if(event.getCause().toString().equals("PROJECTILE"))
		{
			// 투사체를 투척한 사람을 가져와 공격자를 재설정
			damager = (LivingEntity) ((Projectile) damager).getShooter();
		}
		
		// 만약 손에 철 곡괭이를 들고있고, 일반 좌클릭으로 공격한 경우라면
		if(damager.getEquipment().getItemInMainHand().getType().equals(Material.IRON_PICKAXE) && event.getCause().equals(DamageCause.ENTITY_ATTACK))
		{
			//이 이벤트의 데미지를 0으로 설정함
			event.setDamage(0d);
			
			// 이벤트 종료
			return;
		}
		
		// 플레이어끼리의 전투일 경우
		if(damager instanceof Player && target instanceof Player)
		{
			Player player_damager = (Player) damager;
			Player player_target = (Player) target;	
		}
		// 플레이어가 몬스터를 가격할 경우
		else if(damager instanceof Player && (target instanceof Creature || target instanceof Slime))
		{
			Player player_damager = (Player)damager;
			LivingEntity monster_target = (LivingEntity)target;
		}
		// 몬스터가 플레이어를 가격하는 경우
		else if((damager instanceof Creature || damager instanceof Slime) && target instanceof Player)
		{
			LivingEntity monster_damager = (LivingEntity)damager;
			Player player_target = (Player)target;
		}
	}
}
	