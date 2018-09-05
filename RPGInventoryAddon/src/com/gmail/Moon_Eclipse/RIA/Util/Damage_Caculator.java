package com.gmail.Moon_Eclipse.RIA.Util;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;

public class Damage_Caculator 
{
	
	// RIA 플레이어 매니저 인스턴스를 생성
/*
	// 맵의 키로서 사용할 각 스탯의 이름을 얻어옴
	// 기본 데미지. offset으로써 활용됨
	static String Base_Attack_Damage_Name = RIAStats.Base_Attack_Damage_Name;
	
	// 일반 데미지. 기본 공격시 들어가게 되는 데미지
	static  String Normal_Attack_Damage_Name = RIAStats.Normal_Attack_Damage_Name;
	
	// 스킬 데미지. Skill api에 사용되는 스탯을 계산하는데 사용 됨.
	static  String Skill_Attack_Damage_Name = RIAStats.Skill_Attack_Damage_Name;
	
	// 치명타 확률. 치명타 피해가 발생할 수 있는지 없는지 여부를 확인할 때 필요함.
	static  String Critical_Attack_Damage_Name = RIAStats.Critical_Attack_Damage_Name;
			
	// 치명타 피해. % 계산이 진행되야 함.
	static String Critical_Probability_Name = RIAStats.Critical_Probability_Name;
	*/
	public static double Damage_Calculate(double Event_Damage, Entity damager, Entity target) 
	{
		
		
		//------------------------------ 디버그 기록 파트 ------------------------------

		// 디버깅을 위해 메세지 스택 초기화
		RIADebugger.initialize_MessageStack();
		
		//config 항목을 확인하기위해 각종 스탯을 메모
		RIADebugger.AddMessage_to_MessageStack("Base_Attack_Damage_Name: "+ RIAStats.Base_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Normal_Attack_Damage_Name: " + RIAStats.Normal_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Skill_Attack_Damage_Name: " + RIAStats.Skill_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Critical_Attack_Damage_Name: " + RIAStats.Critical_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Critical_Probability_Name: " + RIAStats.Critical_Probability_Name);
		
		//공격자와 피격자 기록
		RIADebugger.AddMessage_to_MessageStack("공격자: "+ damager.getType().getName());
		RIADebugger.AddMessage_to_MessageStack("피격자: "+ target.getType().getName());
		
		
		// 데미지 기록
		RIADebugger.AddMessage_to_MessageStack("플러그인이 최초에 넘겨 받은 데미지: " + Event_Damage);
		//------------------------------ 데미지 처리 파트 ------------------------------
		
	
		//--------------------데미지 증가 파트--------------------
		//--------------------데미지 감소 파트--------------------
		//--------------------데미지 기타 파트--------------------
		//--------------------데미지 설정 파트--------------------
		
		
		
		
		
		// 플레이어끼리의 전투일 경우
		if(damager instanceof Player && target instanceof Player)
		{
			// 데미지의 가감 처리를 위해 데미지량을 복사해 저장
			double New_Damage = Event_Damage;
			
			// 데미지를 가하는 플레이어를 형변환 하여 저장
			Player player_damager = (Player) damager;
			
			// 데미지를 받는 플레이어를 형변환 하여 저장
			Player player_target = (Player) target;	
			
			// 공격자의 RIA 정보를 얻어옴, 디버그에 추가
			RIAPlayer RIA_Damager = WrapperManager.getRIAPlayer(player_damager.getName());
			
			// 피격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Target = WrapperManager.getRIAPlayer(player_target.getName());			
			
			// 공격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Double> RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("공격자의 스탯 테이블: " + RIA_Damager_Stat_map.toString());
			
			// 피격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Double> RIA_Target_Stat_map = RIA_Target.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("피격자의 스탯 테이블: " + RIA_Target_Stat_map.toString());
			
	//--------------------데미지 증가 파트--------------------
			
			// 공격자의 기본 데미지 스탯을 얻어옴
			double Damager_Base_Damage = RIA_Damager_Stat_map.get(RIAStats.Base_Attack_Damage_Name);
			RIADebugger.AddMessage_to_MessageStack("공격자의 기본 데미지: " + Damager_Base_Damage);
			
			// 공격자의 일반 데미지 스탯을 얻어옴
			double Damager_Normal_Damage = RIA_Damager_Stat_map.get(RIAStats.Normal_Attack_Damage_Name);
			RIADebugger.AddMessage_to_MessageStack("공격자의 일반 데미지: " + Damager_Normal_Damage);
			
			// 공격자의 크리티컬 성공 확률을 얻어옴
			double Critical_Probability = RIA_Damager_Stat_map.get(RIAStats.Critical_Probability_Name);
			RIADebugger.AddMessage_to_MessageStack("공격자의 크리티컬 성공 확률: " + Critical_Probability);
				
			// 공격자의 크리티컬 데미지 퍼센트를 얻어옴. 몇%를 원래 데미지에 더하는 연산에 사용
			double Critical_Damage = RIA_Damager_Stat_map.get(RIAStats.Critical_Attack_Damage_Name);	
			RIADebugger.AddMessage_to_MessageStack("공격자의 크리티컬 데미지: " + Critical_Damage);
			
			// 공격자의 크리티컬이 성공하는지 아닌지 계산
			boolean Can_Critical = RIAUtil.CanPlayerActivateCriticalDamage(Critical_Probability);
			RIADebugger.AddMessage_to_MessageStack("크리티컬 성공?: " + Can_Critical);
			
			// 데미지는 offset인 기본 데미지와 일반 데미지의 합으로 이루어짐 
			// Base_Attack_Damage_Name + Normal_Attack_Damage_Name = New_Damage
			New_Damage += (Damager_Base_Damage + Damager_Normal_Damage);
			
			// 레벨마다 1씩 공격력이 오르게 됨.
			New_Damage += RIA_Damager.getPlayerLevel();
			RIADebugger.AddMessage_to_MessageStack("레벨에 의해 데미지 증가: " + RIA_Damager.getPlayerLevel());
			
			// 만약 크리티컬이 터졌다면
			if(Can_Critical) 
			{
				// 크리티컬 데미지의 퍼센트 만큼의 추가 공격력을 얻게 되므로 최종 공격력 + (최종공격력 * @/100)
				New_Damage = getPercentedDamage_Plus(New_Damage, Critical_Damage);
			}
			
	//--------------------데미지 감소 파트--------------------
			
			// 피격자의 피해 감소량을 얻어옴. %적용이 필요함.
			double Target_Defence_Damage = RIA_Target_Stat_map.get(RIAStats.Defence_Damage_Name);
			
			// 피격자의 피해 무시량을 얻어옴. 정수 연산
			double Target_Reduce_Damage = RIA_Target_Stat_map.get(RIAStats.Reduce_Damage_Name);
			
			// %만큼 감소한 데미지가 적용된 데미지값을 변수에 저장
			New_Damage = getPercentedDamage_Minus(New_Damage, Target_Defence_Damage);
			
			// 지정된 값만큼 데미지를 감소시킴
			New_Damage -= Target_Reduce_Damage;
			
	//--------------------데미지 기타 파트--------------------
			
			// 공격자의 생명력 흡수량을 얻어옴.
			double Damager_Absorption_Health = RIA_Damager_Stat_map.get(RIAStats.Absorption_Health_Name);
			
			// 생명력 흡수를 플레이어에게 적용함.
			BloodSuck(RIA_Damager, Damager_Absorption_Health, New_Damage);
			
			// 포션 이펙트를 피격자에게 적용함
			ApplyDeBuffPotionEffect(RIA_Damager_Stat_map, player_target);
			
			// 포션 이펙트를 공격자에게 적용함
			ApplyDeBuffPotionEffect(RIA_Target_Stat_map, player_damager);
			
			
	//--------------------데미지 설정 파트--------------------
			
			// 공격 속도에 의한 플레이어의 공격 쿨타임을 설정함
			RIA_Damager.ApplyAttackCooldownTimeByValue(RIA_Damager_Stat_map.get(RIAStats.Attack_Speed_Name));
			
			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 반환함.
			RIADebugger.AddMessage_to_MessageStack("연산 마지막 데미지: " + New_Damage);
			return New_Damage;
		}
		// 플레이어가 몬스터를 가격할 경우
		else if(damager instanceof Player && (target instanceof Creature || target instanceof Slime))
		{
			// 데미지의 가감 처리를 위해 데미지량을 복사해 저장
			double New_Damage = Event_Damage;
						
			Player player_damager = (Player)damager;
			LivingEntity monster_target = (LivingEntity)target;
			
			// 공격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Damager = WrapperManager.getRIAPlayer(player_damager.getName());
			
			// 공격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Double> RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("공격자의 스탯 테이블: " + RIA_Damager_Stat_map.toString());
			
	//--------------------데미지 증가 파트--------------------
			
			// 공격자의 기본 데미지 스탯을 얻어옴
			double Damager_Base_Damage = RIA_Damager_Stat_map.get(RIAStats.Base_Attack_Damage_Name);
			RIADebugger.AddMessage_to_MessageStack("공격자의 기본 데미지: " + Damager_Base_Damage);
			
			// 공격자의 일반 데미지 스탯을 얻어옴
			double Damager_Normal_Damage = RIA_Damager_Stat_map.get(RIAStats.Normal_Attack_Damage_Name);
			RIADebugger.AddMessage_to_MessageStack("공격자의 일반 데미지: " + Damager_Normal_Damage);
			
			// 공격자의 크리티컬 성공 확률을 얻어옴
			double Critical_Probability = RIA_Damager_Stat_map.get(RIAStats.Critical_Probability_Name);
			RIADebugger.AddMessage_to_MessageStack("공격자의 크리티컬 성공 확률: " + Critical_Probability);
					
			// 공격자의 크리티컬 데미지 퍼센트를 얻어옴. 몇%를 원래 데미지에 더하는 연산에 사용
			double Critical_Damage = RIA_Damager_Stat_map.get(RIAStats.Critical_Attack_Damage_Name);	
			RIADebugger.AddMessage_to_MessageStack("공격자의 크리티컬 데미지: " + Critical_Damage);
			
			// 공격자의 크리티컬이 성공하는지 아닌지 계산
			boolean Can_Critical = RIAUtil.CanPlayerActivateCriticalDamage(Critical_Probability);
			RIADebugger.AddMessage_to_MessageStack("크리티컬 성공?: " + Can_Critical);
			
			// 데미지는 offset인 기본 데미지와 일반 데미지의 합으로 이루어짐 
			// Base_Attack_Damage_Name + Normal_Attack_Damage_Name = New_Damage
			New_Damage += (Damager_Base_Damage + Damager_Normal_Damage);
			
			// 레벨마다 1씩 공격력이 오르게 됨.
			New_Damage += RIA_Damager.getPlayerLevel();
			RIADebugger.AddMessage_to_MessageStack("레벨에 의해 데미지 증가: " + RIA_Damager.getPlayerLevel());
			
			// 만약 크리티컬이 터졌다면
			if(Can_Critical) 
			{
				// 크리티컬 데미지의 퍼센트 만큼의 추가 공격력을 얻게 되므로 최종 공격력 + (최종공격력 * @/100)
				double temp_damage = New_Damage * (Critical_Damage/100);
				New_Damage = New_Damage + temp_damage;
			}
			
	//--------------------데미지 감소 파트--------------------
	//--------------------데미지 기타 파트--------------------
			
			// 공격자의 생명력 흡수량을 얻어옴.
			double Damager_Absorption_Health = RIA_Damager_Stat_map.get(RIAStats.Absorption_Health_Name);
			
			// 생명력 흡수를 플레이어에게 적용함.
			BloodSuck(RIA_Damager, Damager_Absorption_Health, New_Damage);
			
			// 포션 이펙트를 피격자 에게 적용함
			ApplyBuffPotionEffect(RIA_Damager_Stat_map, monster_target);
			
	//--------------------데미지 설정 파트--------------------
	
			// 공격 속도에 의한 플레이어의 공격 쿨타임을 설정함
			RIA_Damager.ApplyAttackCooldownTimeByValue(RIA_Damager_Stat_map.get(RIAStats.Attack_Speed_Name));
						
			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 반환함.
			RIADebugger.AddMessage_to_MessageStack("연산 마지막 데미지: " + New_Damage);
			return New_Damage;
						
		}
		// 몬스터가 플레이어를 가격하는 경우
		else if((damager instanceof Creature || damager instanceof Slime) && target instanceof Player)
		{
			// 데미지의 가감 처리를 위해 데미지량을 복사해 저장
			double New_Damage = Event_Damage;
			
			LivingEntity monster_damager = (LivingEntity)damager;
			Player player_target = (Player)target;
			
			// 피격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Target = WrapperManager.getRIAPlayer(player_target.getName());
			
			// 피격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Double> RIA_Target_Stat_map = RIA_Target.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("피격자의 스탯 테이블: " + RIA_Target_Stat_map.toString());
			
	//--------------------데미지 증가 파트--------------------
	//--------------------데미지 감소 파트--------------------
			// 피격자의 피해 감소량을 얻어옴. %적용이 필요함.
			double Target_Defence_Damage = RIA_Target_Stat_map.get(RIAStats.Defence_Damage_Name);
			
			// 피격자의 피해 무시량을 얻어옴. 정수 연산
			double Target_Reduce_Damage = RIA_Target_Stat_map.get(RIAStats.Reduce_Damage_Name);
			
			// %만큼 감소한 데미지가 적용된 데미지값을 변수에 저장
			New_Damage = getPercentedDamage_Minus(New_Damage, Target_Defence_Damage);
			
			// 지정된 값만큼 데미지를 감소시킴
			New_Damage -= Target_Reduce_Damage;
	//--------------------데미지 기타 파트--------------------
			
			// 포션 이펙트를 공격자에게 적용함
			ApplyDeBuffPotionEffect(RIA_Target_Stat_map, monster_damager);
			
	//--------------------데미지 설정 파트--------------------
	
			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 반환함.
			RIADebugger.AddMessage_to_MessageStack("연산 마지막 데미지: " + New_Damage);
			return New_Damage;
		}
		else
		{
			return Event_Damage;
		}
	}
	public static double getPercentedDamage_Plus(double Damage, double per) 
	{
		double temp_damage = Damage * (per/100);
		return Damage + temp_damage;
	}
	public static double getPercentedDamage_Minus(double Damage, double per) 
	{
		double temp_damage = Damage * (per/100);
		return Damage - temp_damage;
	}
	public static void BloodSuck(RIAPlayer RIAplayer, double per, double damage)
	{
		RIADebugger.AddMessage_to_MessageStack("--------흡혈 메소드에서 넘겨받은 데미지: " + damage + "------");
		
		double health = RIAplayer.getPlayerHealth();
		RIADebugger.AddMessage_to_MessageStack("플레이어의 현재 체력: " + health);
		
		double SuckValue = per;
		RIADebugger.AddMessage_to_MessageStack("흡혈할 정도: " + SuckValue + "%");
		
		double UpHealth = (damage/100) * SuckValue;
		RIADebugger.AddMessage_to_MessageStack("흡혈체력의 양: " + UpHealth);
		
		double Maxhealth = RIAplayer.getPlayerMaxHealth();
		if((health+UpHealth) > Maxhealth)
		{
			RIAplayer.setPlayerHealth(Maxhealth);
			RIADebugger.AddMessage_to_MessageStack("흡혈 후 최대 채력을 넘으므로 최대 체력으로 회복: " + Maxhealth);
		}
		else if((health+UpHealth) < 0)
		{
			RIAplayer.setPlayerHealth(0.1);
			RIADebugger.AddMessage_to_MessageStack("흡혈 후 체력이 0보다 낮으므로 0.1로 임시 지정: " + (health + UpHealth));
		}
		else
		{
			RIAplayer.setPlayerHealth(health + UpHealth);
			RIADebugger.AddMessage_to_MessageStack("흡혈 후 체력: " + (health + UpHealth));
		}
	}
	public static void ApplyBuffPotionEffect(Map<String,Double> RIA_Damager_Stat_map, LivingEntity target)
	{
		// 포션 이펙트를 피격자 에게 적용함
		
		// 포션 이펙트 이름을 하나씩 불러와 사용하기위해 반복문 사용
		for(String Effect_Name : RIAStats.Buff_Potion_Names)
		{
			// 저장되어있는 포션 이펙트의 레벨을 얻어옴
			double Effect_Level = RIA_Damager_Stat_map.get(Effect_Name + "_LEVEL");
			
			// 만약 레벨이 0이 아니라면, 즉 맵에 처리할 포션 값이 있다면
			if(Effect_Level != 0)
			{
				// 이펙트 발동 확률을 정의
				double Effect_Percent = RIA_Damager_Stat_map.get(Effect_Name + "_Per");
				
				// 크리티컬이나 이펙트나 발동 가능 여부의 계산은 똑같으므로 아래 메소드를 사용
				boolean Can_Apply_Effect = RIAUtil.CanPlayerActivateCriticalDamage(Effect_Percent);
				
				// 만약 퍼센트 조건을 만족 했다면
				if(Can_Apply_Effect)
				{
					// 지속 시간을 정의
					double Effect_Second = RIA_Damager_Stat_map.get(Effect_Name + "_Second"); 
					
					// 이름으로부터 이펙트 타입을 받아옴
					PotionEffectType EffectType = RIAUtil.getPotionEffectTypeFromString(Effect_Name);
					
					// 이펙트 타입, 레벨, 지속시간을 이용해 이펙트를 만듦
					PotionEffect Effect = new PotionEffect(EffectType, (int) Effect_Second, (int) Effect_Level);
					
					// 피격자에게 이펙트 적용
					target.addPotionEffect(Effect);
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
	}
	public static void ApplyDeBuffPotionEffect(Map<String,Double> RIA_Damager_Stat_map, LivingEntity target)
	{
		// 포션 이펙트를 피격자 에게 적용함
		
		// 포션 이펙트 이름을 하나씩 불러와 사용하기위해 반복문 사용
		for(String Effect_Name : RIAStats.DeBuff_Potion_Names)
		{
			// 저장되어있는 포션 이펙트의 레벨을 얻어옴
			double Effect_Level = RIA_Damager_Stat_map.get(Effect_Name + "_LEVEL");
			
			// 만약 레벨이 0이 아니라면, 즉 맵에 처리할 포션 값이 있다면
			if(Effect_Level != 0)
			{
				// 이펙트 발동 확률을 정의
				double Effect_Percent = RIA_Damager_Stat_map.get(Effect_Name + "_Per");
				
				// 크리티컬이나 이펙트나 발동 가능 여부의 계산은 똑같으므로 아래 메소드를 사용
				boolean Can_Apply_Effect = RIAUtil.CanPlayerActivateCriticalDamage(Effect_Percent);
				
				// 만약 퍼센트 조건을 만족 했다면
				if(Can_Apply_Effect)
				{
					// 지속 시간을 정의
					double Effect_Second = RIA_Damager_Stat_map.get(Effect_Name + "_Second"); 
					
					// 이름으로부터 이펙트 타입을 받아옴
					PotionEffectType EffectType = RIAUtil.getPotionEffectTypeFromString(Effect_Name);
					
					// 이펙트 타입, 레벨, 지속시간을 이용해 이펙트를 만듦
					PotionEffect Effect = new PotionEffect(EffectType, (int) Effect_Second, (int) Effect_Level);
					
					// 피격자에게 이펙트 적용
					target.addPotionEffect(Effect);
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
	}
}
