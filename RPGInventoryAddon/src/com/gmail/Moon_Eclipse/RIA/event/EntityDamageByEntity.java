package com.gmail.Moon_Eclipse.RIA.event;

import java.util.Map;

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

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

public class EntityDamageByEntity implements Listener 
{
	// RIA 플레이어 매니저 인스턴스를 생성
	WrapperManager wm = new WrapperManager();

	// 맵의 키로서 사용할 각 스탯의 이름을 얻어옴
	// 기본 데미지. offset으로써 활용됨
	String Base_Attack_Damage_Name = RIAStats.Base_Attack_Damage_Name;
	
	// 일반 데미지. 기본 공격시 들어가게 되는 데미지
	String Normal_Attack_Damage_Name = RIAStats.Normal_Attack_Damage_Name;
	
	// 스킬 데미지. Skill api에 사용되는 스탯을 계산하는데 사용 됨.
	String Skill_Attack_Damage_Name = RIAStats.Skill_Attack_Damage_Name;
	
	// 치명타 확률. 치명타 피해가 발생할 수 있는지 없는지 여부를 확인할 때 필요함.
	String Critical_Attack_Damage_Name = RIAStats.Critical_Attack_Damage_Name;
			
	// 치명타 피해. % 계산이 진행되야 함.
	String Critical_Probability_Name = RIAStats.Critical_Probability_Name;
			
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

		
		
		
		//------------------------------ 데미지 처리 파트 ------------------------------
		
		
		//--------------------데미지 증가 파트--------------------
		//--------------------데미지 감소 파트--------------------
		//--------------------데미지 기타 파트--------------------
		//--------------------데미지 설정 파트--------------------
		
		
		
		// 이벤트 발생시의 데미지를 얻어와 저장해둠.
		double Event_Damage = event.getDamage();
		
		// 플레이어끼리의 전투일 경우
		if(damager instanceof Player && target instanceof Player)
		{
			// 데미지의 가감 처리를 위해 데미지량을 복사해 저장
			double New_Damage = Event_Damage;
			
			// 데미지를 가하는 플레이어를 형변환 하여 저장
			Player player_damager = (Player) damager;
			
			// 데미지를 받는 플레이어를 형변환 하여 저장
			Player player_target = (Player) target;	
			
			// 공격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Damager = wm.getRIAPlayer(player_damager);
			
			// 피격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Target = wm.getRIAPlayer(player_target);
			
			// 공격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			
			// 피격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Target_Stat_map = RIA_Target.getAttributeMap();
			
	//--------------------데미지 증가 파트--------------------
			
			
			// 공격자의 기본 데미지 스탯을 얻어옴
			double Damager_Base_Damage = RIA_Damager_Stat_map.get(Base_Attack_Damage_Name);
			
			// 공격자의 일반 데미지 스탯을 얻어옴
			double Damager_Normal_Damage = RIA_Damager_Stat_map.get(Normal_Attack_Damage_Name);
			
			// 공격자의 크리티컬 성공 확률을 얻어옴
			double Critical_Probability = RIA_Damager_Stat_map.get(Critical_Probability_Name);
					
			// 공격자의 크리티컬 데미지 퍼센트를 얻어옴. 몇%를 원래 데미지에 더하는 연산에 사용
			double Critical_Damage = RIA_Damager_Stat_map.get(Critical_Attack_Damage_Name);	
			
			// 공격자의 크리티컬이 성공하는지 아닌지 계산
			boolean Can_Critical = RIAUtil.CanPlayerActivateCriticalDamage(Critical_Probability);
			
			// 데미지는 offset인 기본 데미지와 일반 데미지의 합으로 이루어짐 
			// Base_Attack_Damage_Name + Normal_Attack_Damage_Name = New_Damage
			New_Damage = Damager_Base_Damage + Damager_Normal_Damage;
			
			// 만약 크리티컬이 터졌다면
			if(Can_Critical) 
			{
				// 크리티컬 데미지의 퍼센트 만큼의 추가 공격력을 얻게 되므로 최종 공격력 + (최종공격력 * @/100)
				double temp_damage = New_Damage * (Critical_Damage/100);
				New_Damage = New_Damage + temp_damage;
			}
			
	//--------------------데미지 감소 파트--------------------
	//--------------------데미지 기타 파트--------------------
	//--------------------데미지 설정 파트--------------------
			
			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 설정함.
			event.setDamage(New_Damage);
		}
		// 플레이어가 몬스터를 가격할 경우
		else if(damager instanceof Player && (target instanceof Creature || target instanceof Slime))
		{
			// 데미지의 가감 처리를 위해 데미지량을 복사해 저장
			double New_Damage = Event_Damage;
						
			Player player_damager = (Player)damager;
			LivingEntity monster_target = (LivingEntity)target;
			
			// 공격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Damager = wm.getRIAPlayer(player_damager);
			
			// 공격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			
	//--------------------데미지 증가 파트--------------------
			
			// 공격자의 기본 데미지 스탯을 얻어옴
			double Damager_Base_Damage = RIA_Damager_Stat_map.get(Base_Attack_Damage_Name);
			
			// 공격자의 일반 데미지 스탯을 얻어옴
			double Damager_Normal_Damage = RIA_Damager_Stat_map.get(Normal_Attack_Damage_Name);
			
			// 공격자의 크리티컬 성공 확률을 얻어옴
			double Critical_Probability = RIA_Damager_Stat_map.get(Critical_Probability_Name);
					
			// 공격자의 크리티컬 데미지 퍼센트를 얻어옴. 몇%를 원래 데미지에 더하는 연산에 사용
			double Critical_Damage = RIA_Damager_Stat_map.get(Critical_Attack_Damage_Name);	
			
			// 공격자의 크리티컬이 성공하는지 아닌지 계산
			boolean Can_Critical = RIAUtil.CanPlayerActivateCriticalDamage(Critical_Probability);
			
			// 데미지는 offset인 기본 데미지와 일반 데미지의 합으로 이루어짐 
			// Base_Attack_Damage_Name + Normal_Attack_Damage_Name = New_Damage
			New_Damage = Damager_Base_Damage + Damager_Normal_Damage;
			
			// 만약 크리티컬이 터졌다면
			if(Can_Critical) 
			{
				// 크리티컬 데미지의 퍼센트 만큼의 추가 공격력을 얻게 되므로 최종 공격력 + (최종공격력 * @/100)
				double temp_damage = New_Damage * (Critical_Damage/100);
				New_Damage = New_Damage + temp_damage;
			}
			
	//--------------------데미지 감소 파트--------------------
	//--------------------데미지 기타 파트--------------------
	//--------------------데미지 설정 파트--------------------

			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 설정함.
			event.setDamage(New_Damage);
						
		}
		// 몬스터가 플레이어를 가격하는 경우
		else if((damager instanceof Creature || damager instanceof Slime) && target instanceof Player)
		{
			// 데미지의 가감 처리를 위해 데미지량을 복사해 저장
			double New_Damage = Event_Damage;
			
			LivingEntity monster_damager = (LivingEntity)damager;
			Player player_target = (Player)target;
			
			// 피격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Target = wm.getRIAPlayer(player_target);
			
			// 피격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Target_Stat_map = RIA_Target.getAttributeMap();
			
	//--------------------데미지 증가 파트--------------------
	//--------------------데미지 감소 파트--------------------
	//--------------------데미지 기타 파트--------------------
	//--------------------데미지 설정 파트--------------------

			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 설정함.
			event.setDamage(New_Damage);
		}
	}
}
	