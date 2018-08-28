package com.gmail.Moon_Eclipse.RIA.Util;

import java.util.Map;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;

public class Damage_Caculator 
{
	
	// RIA 플레이어 매니저 인스턴스를 생성
	static WrapperManager wm = new WrapperManager();

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
	
	public static double Damage_Calculate(double Event_Damage, Entity damager, Entity target) 
	{
		
		
		//------------------------------ 디버그 기록 파트 ------------------------------

		// 디버깅을 위해 메세지 스택 초기화
		RIADebugger.initialize_MessageStack();
		
		//config 항목을 확인하기위해 각종 스탯을 메모
		RIADebugger.AddMessage_to_MessageStack("Base_Attack_Damage_Name: "+ Base_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Normal_Attack_Damage_Name: " + Normal_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Skill_Attack_Damage_Name: " + Skill_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Critical_Attack_Damage_Name: " + Critical_Attack_Damage_Name);
		RIADebugger.AddMessage_to_MessageStack("Critical_Probability_Name: " + Critical_Probability_Name);
		
		
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
			RIAPlayer RIA_Damager = wm.getRIAPlayer(player_damager);
			
			// 피격자의 RIA 정보를 얻어옴
			RIAPlayer RIA_Target = wm.getRIAPlayer(player_target);			
			
			// 공격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("공격자의 스탯 테이블: " + RIA_Damager_Stat_map.toString());
			
			// 피격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Target_Stat_map = RIA_Target.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("피격자의 스탯 테이블: " + RIA_Target_Stat_map.toString());
			
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
			RIADebugger.AddMessage_to_MessageStack("크리티컬 성공?: " + Can_Critical);
			
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
			RIAPlayer RIA_Damager = wm.getRIAPlayer(player_damager);
			
			// 공격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("공격자의 스탯 테이블: " + RIA_Damager_Stat_map.toString());
			
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
	
			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 반환함.
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
			RIAPlayer RIA_Target = wm.getRIAPlayer(player_target);
			
			// 피격자의 데이터 스테이터스 맵을 받아옴
			Map<String,Float> RIA_Target_Stat_map = RIA_Target.getAttributeMap();
			RIADebugger.AddMessage_to_MessageStack("피격자의 스탯 테이블: " + RIA_Target_Stat_map.toString());
			
	//--------------------데미지 증가 파트--------------------
	//--------------------데미지 감소 파트--------------------
	//--------------------데미지 기타 파트--------------------
	//--------------------데미지 설정 파트--------------------
	
			// 데미지의 가감이 끝났기 때문에 이벤트의 데미지를 반환함.
			return New_Damage;
		}
		
		//어떤 부분에도 속하지 않을경우.  사실상 이런 경우는 없으므로 무조건 적으로 로그를 출력함.

		RIADebugger.SendErrorMessage_OperatorAndConsole("RIA: 데미지 계산에서 오류가 발생했습니다. 데미지가 0으로 수정됩니다.");
		RIADebugger.SendErrorMessage_OperatorAndConsole("RIA: 이 오류는 공격자와 피격자가 명확히 구분되지 못했을때 나타납니다.");
		RIADebugger.SendStackedDebugMessage_Console();
		return 0d;
	}
}
