package com.gmail.Moon_Eclipse.RIA.Link_Plugin.SkillApi.EventListener;

import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.Util.Damage_Caculator;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;
import com.sucy.skill.api.event.SkillDamageEvent;

public class SkillDamage implements Listener
{
	@EventHandler
	public void onSkillDamage(SkillDamageEvent event)
	{
		/*
		 * 스킬을 통해 공격한 순간 바로 다음 공격은 스킬에 의한 일반데미지 공격이다. 이 공격을 취소하기 위해선 바로 직전에 스킬이 사용되었는지 여부를 알 필요가 있기 때문에
		 * 아래와 같은 방법을 통해 스킬 사용 여부를 판별하고자 한다.
		 */
		
		// 공격자를 얻어와 저장
		LivingEntity damager = event.getDamager();
		
		// 만약 공격자가 플레이어라면
		if(damager instanceof Player)
		{
			// 공격자를 플레이어형으로 변환해 저장
			Player Player_Damager = (Player) damager;
			
			// 공격자의 RIAPlayer를 받아와 저장
			RIAPlayer RIA_Damager = WrapperManager.getRIAPlayer(Player_Damager.getName());
			
			// 능력치 맵을 받아와 저장
			Map<String, Double> RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			
			// 맵에 스킬 공격이 발동했음을 기록
			RIA_Damager_Stat_map.put("ENTITY_ATTACK_FLAG", 1d);
			
			// 맵을 RIAPlayer에 저장
			RIA_Damager.setAttributeMap(RIA_Damager_Stat_map);
			
//-------------------------------------------------------------------------------------------------------------
			
			// 플레이어가 스킬을 통해 공격할 경우에, 해당 공격력을 조정하기위한 부분을 아래에서 다룬다.
			
			// 가감될 데미지가 저장될 변수를 선언
			double New_Damage = event.getDamage();
		
			// 이벤트에 사용될 능력치 맵을 다시 받아와 저장
			RIA_Damager_Stat_map = RIA_Damager.getAttributeMap();
			
			
			//------------------------------ 데미지 처리 파트 ------------------------------
			
			
			//--------------------데미지 증가 파트--------------------
			
			// 공격자의 크리티컬 성공 확률을 얻어옴
			double Critical_Probability = RIA_Damager_Stat_map.get(RIAStats.Critical_Probability_Name);
			RIADebugger.AddMessage_to_MessageStack("공격자의 크리티컬 성공 확률: " + Critical_Probability);
				
			// 공격자의 크리티컬 데미지 퍼센트를 얻어옴. 몇%를 원래 데미지에 더하는 연산에 사용
			double Critical_Damage = RIA_Damager_Stat_map.get(RIAStats.Critical_Attack_Damage_Name);	
			RIADebugger.AddMessage_to_MessageStack("공격자의 크리티컬 데미지: " + Critical_Damage);
			
			// 공격자의 크리티컬이 성공하는지 아닌지 계산
			boolean Can_Critical = RIAUtil.CanPlayerActivateCriticalDamage(Critical_Probability);
			RIADebugger.AddMessage_to_MessageStack("크리티컬 성공?: " + Can_Critical);
			
			// 만약 크리티컬이 터졌다면
			if(Can_Critical) 
			{
				// 크리티컬 데미지의 퍼센트 만큼의 추가 공격력을 얻게 되므로 최종 공격력 + (최종공격력 * @/100)
				New_Damage = Damage_Caculator.getPercentedDamage_Plus(New_Damage, Critical_Damage);
			}
			
			//--------------------데미지 감소 파트--------------------
			//--------------------데미지 기타 파트--------------------
			
			// 공격자의 생명력 흡수량을 얻어옴.
			double Damager_Absorption_Health = RIA_Damager_Stat_map.get(RIAStats.Absorption_Health_Name);
			
			// 생명력 흡수를 플레이어에게 적용함.
			Damage_Caculator.BloodSuck(RIA_Damager, Damager_Absorption_Health, New_Damage);
			
			//--------------------데미지 설정 파트--------------------
			
		}
	}
}
