package com.gmail.Moon_Eclipse.RIA.event_listener;

import java.util.Date;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.Moon_Eclipse.RIA.RPGInventoryAddon;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.Util.Damage_Caculator;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.sucy.skill.api.event.PhysicalDamageEvent;
import com.sucy.skill.api.event.SkillDamageEvent;

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
		Entity damager = event.getDamager();
		
		//Bukkit.broadcastMessage("이벤트 타입: " + event.getCause());
		
		// 만약 이벤트가 커스텀 형식이라면
		if(event.getCause().toString().equals("CUSTOM"))
		{
			// 투사체를 투척한 사람을 가져와 공격자를 재설정
			//damager = (Entity)((Projectile) damager).getShooter();
			RIADebugger.AddMessage_to_MessageStack("스킬 커스텀 공격이므로 RIA 데미지 계산 취소");
			return;
		}
		
		// 공격자가 스킬을 사용해 공격했다면 이번 공격에서의 일반 공격을 무효화
		if(damager instanceof Player)
		{
			// 플레이어로 공격자 변환
			Player Player_Damager = (Player) damager;
						
			// 공격자의 RIAPlayer 데이터를 받아옴
			RIAPlayer rp = WrapperManager.getRIAPlayer(Player_Damager.getName());
			
			// 공격자의 스탯 맵을 받아옴
			Map<String, Double> map = rp.getAttributeMap();
			
			// 스탯 맵의 플래그를 받아와서 스킬 공격인지 아닌지 판별
			double flag = map.get("ENTITY_ATTACK_FLAG");
			
			// 만약 스킬 공격이라면
			if(flag == 1d)
			{
				RIADebugger.AddMessage_to_MessageStack("스킬 근거리 공격이므로 RIA 데미지 계산 취소");
				// 스킬 공격이 한차례 지나갔으므로 플래그 재설정
				map.put("ENTITY_ATTACK_FLAG", 0d);
				
				// 맵을 저장
				rp.setAttributeMap(map);
				
				// 이벤트 종료
				return;
			}
			
			// 만약 공격 속도에 의해 설정된 쿨타임이 아직 지나지 않았다면
			long date_value = new Date().getTime();
			long Cooled_time = rp.getCooledTime();
			
			if(Cooled_time > date_value)
			{
				event.setCancelled(true);
				return;
			}
		}

		// 만약 전투에 사람이 포함되어 있다면
		if(damager instanceof Player || target instanceof Player)
		{
			// 만약 손에 철 곡괭이를 들고있고, 일반 좌클릭으로 공격한 경우라면
			if(((LivingEntity)damager).getEquipment().getItemInMainHand().getType().equals(Material.IRON_PICKAXE) && event.getCause().equals(DamageCause.ENTITY_ATTACK))
			{
				//이 이벤트의 데미지를 0으로 설정함
				event.setDamage(0d);
				
				// 이벤트 종료
				return;
			}
			
			// 이벤트 발생시의 데미지를 얻어와 저장해둠.
			double Event_Damage = event.getDamage();
			
			// util의 데미지 계산기를 사용해 이 이벤트의 데미지를 계산
			double New_Damage = Damage_Caculator.Damage_Calculate(Event_Damage, damager, target);
			
			// 데미지의 계산이 끝났으므로 데미지를 적용.
			event.setDamage(New_Damage);
		}		
	}
}
	