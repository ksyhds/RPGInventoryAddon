package com.gmail.Moon_Eclipse.RIA.RIA_Player;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.Moon_Eclipse.RIA.RPGInventoryAddon;
import com.gmail.Moon_Eclipse.RIA.Link_Plugin.SkillApi.Link_SkillAPI;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

public class RIAPlayer 
{
	//RIA 플레이어의 기본값이 될 마인크래프트 플레이어값을 저장하기위한 공간을 마련함.
	public final Player MineCraftPlayer;
	
	public double SkillAPI_Health_Point;
	
	public static Map<String, Double> AttributeMap;
	
	public static Map<String, String> PotionEffectMap;

	public Timestamp Time_Stamp;
	

	// 생성자를 생성해서 기본 변수를 초기화.
	public RIAPlayer(Player p)
	{
		// 생성된 RIA 클래스의 기존 마인크래프트 클래스를 저장.
		MineCraftPlayer = p;
		AttributeMap = RIAUtil.ResetAttributeMap();
		SkillAPI_Health_Point = RIAStats.Default_Health_Point;
		RunPersonnalTimer();
		Time_Stamp = new Timestamp(RIAUtil.getCooledTime(1.0d));
	}
	public void setAttributeMap(Map<String, Double> Map)
	{
		AttributeMap = Map;
	}
	public Map<String, Double> getAttributeMap()
	{
		return AttributeMap;
	}
	public Player returnMinecraftPlayer() 
	{
		return MineCraftPlayer;
	}
	public double getPlayerHealth() 
	{
		return MineCraftPlayer.getHealth();
	}
	public double getPlayerMaxHealth() 
	{
		AttributeInstance healthAttribute = MineCraftPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		
		return healthAttribute.getBaseValue();
	}
	public void setPlayerHealth(double hp) 
	{	
		MineCraftPlayer.setHealth(hp);
	}
	public void setMoveSpeedByAttributeMap()
	{
		// 플레이어의 기본 걷기 속도를 계산을 위해 지정. 걷기: 0.2f, 날기:0.1f
		double default_walk_speed = 0.2d;
		
		// 이동 속도 계수를 구함. 아마 150~200% = 1.5~2배 또한 // 1%변화당 1.5배 변화되야 하므로  +0.5
		double Speed_Coefficient = (AttributeMap.get(RIAStats.Walk_Speed_Name) / 100d) + 0.5;
		
		// 계수를 이용해 적용할 속도를 구함
		double new_walk_speed = default_walk_speed * Speed_Coefficient;
		
		
		
		
		// 플레이어의 걷기 속도를 설정
		MineCraftPlayer.setWalkSpeed((float)new_walk_speed);
	}
	public void setHitPointByAttributeMap() 
	{
		//체력을 변경하기전에 스킬api의 값을 얻어와 기본 체력값 조절
		UpdateRIAPlayerMaxHealth();
		
		// 맵에 저장되어 있는 생명력을 얻어옴
		double HP = AttributeMap.get(RIAStats.Additioanl_Health_Name);
		
		// 맵의 생명력과 기본 생명력을 더해서 최대 체력을 지정함, setMaxHealth를 대신할Attribute를 처음 사용 
		AttributeInstance healthAttribute = MineCraftPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		
		// 최대 체력 정의
		double MaxHealth = SkillAPI_Health_Point + HP;
		
		// 기반이 될 체력은 만약 skillapi의직업이 없다면 이 플러그인 기반 체력이 될 것이고, 직업이 있다면 그 직업의 체력으로 변할 것임.
		healthAttribute.setBaseValue(MaxHealth);
		
		// 하트 한칸당 몇개의 체력을 나타낼 것인지 계산 최대체력 / 스케일 값 = (반 하트 개수)
		MineCraftPlayer.setHealthScale(RIAStats.Slot_of_Health_Gauge);
		
	}
	public void AddHitPointByAttributeMap()
	{
		// 맵에 저장되어 있는 생명력 재생값을 얻어옴
		double Additional_HP = AttributeMap.get(RIAStats.Regeneration_Health_Name);
		
		// 설정할 값을 미리 계산해둠
		double Final_HP = MineCraftPlayer.getHealth() + Additional_HP;
		
		// 설정할 체력이 최대 체력보다 작거나 같다면
		if(Final_HP <= getPlayerMaxHealth())
		{
			// 체력을 계산된 만큼 회복
			MineCraftPlayer.setHealth(Final_HP);
		}
		else
		{
			// 계산된 체력이 최대 체력보다 크다면 최대 체력으로 대상의 체력을 회복
			MineCraftPlayer.setHealth(getPlayerMaxHealth());
		}
	}
	public void RunPersonnalTimer() 
	{
        BukkitTask task = new BukkitRunnable() 
		{
        	
            @Override
            public void run() 
            {	
            	
            	if(!MineCraftPlayer.isOnline())
            	{
            		this.cancel();
            	}
            	
				double Maxhealth = UpdateRIAPlayerMaxHealth() + AttributeMap.get(RIAStats.Additioanl_Health_Name);
				
				if(MineCraftPlayer.getHealth() < Maxhealth)
				{
					AddHitPointByAttributeMap();
				}
				else if(MineCraftPlayer.getHealth() > Maxhealth)
				{
					MineCraftPlayer.setHealth(Maxhealth);	
				}
			}
        }.runTaskTimer(RPGInventoryAddon.getInstance(), 0, 20);
	}
	public double UpdateRIAPlayerMaxHealth()
	{
		double health = Link_SkillAPI.getPlayerBaseMaxHealth(MineCraftPlayer);
		if(health == 0)
		{
			SkillAPI_Health_Point = RIAStats.Default_Health_Point;
			health = RIAStats.Default_Health_Point;
		}
		else
		{
			SkillAPI_Health_Point = health;
		}
		
		return health;
		
	}
	public int getPlayerLevel()
	{
		return MineCraftPlayer.getLevel();
	}
	
	public void setAttackSpeed(double speed)
	{
		AttributeInstance AttackSpeeds = MineCraftPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		
		AttackSpeeds.setBaseValue(AttackSpeeds.getDefaultValue());
		
	}
	public void ApplyPotionEffect(PotionEffect potion)
	{
		MineCraftPlayer.addPotionEffect(potion);
	}
	public void setCooldownTime(double CoolTime_Second)
	{
		Time_Stamp = new Timestamp(RIAUtil.getCooledTime(CoolTime_Second));
	}
	public void ApplySLOW_DIGGINGByValue(double value)
	{
		int new_value = (int) value;
		
		/*
		 * 
		매우 빠름//채굴 피로 3단계
		빠름//채굴 피로 6단계
		보통//채굴 피로 10단계
		느림//채굴 피로 18단계
		매우 느림//채굴 피로27단계
		 * 
		 */
		
		// 5가 매우 느림 1이 매우 빠름
		switch(new_value)
		{
			// 손에 관련된 물건이 없으므로 제한 해제
			case 0:
			{
				MineCraftPlayer.removePotionEffect(PotionEffectType.SLOW_DIGGING);
			}
			break;
			
			//매우 빠름
			case 1:
			{
				// 채굴 피로 3단계 적용
				PotionEffect potion = new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 3);
				ApplyPotionEffect(potion);
			}
			break;
			
			// 빠름
			case 2:
			{
				// 채굴 피로 6단계
				PotionEffect potion = new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 6);
				ApplyPotionEffect(potion);
			}
			break;
			
			case 3:
			{
				// 채굴 피로 10단계
				PotionEffect potion = new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 10);
				ApplyPotionEffect(potion);
			}
			break;
			case 4:
			{
				// 채굴 피로 18단계
				PotionEffect potion = new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 18);
				ApplyPotionEffect(potion);
			}
			break;
			
			case 5:
			{
				// 채굴 피로 27단계
				PotionEffect potion = new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 27);
				ApplyPotionEffect(potion);
			}
			break;
		}
	}
	public void ApplyAttackCooldownTimeByValue(double value)
	{
		int new_value = (int) value;
		
		
		/*
		 * 
		매우 빠름//0.35초
		빠름// 0.5초
		보통//0.7초
		느림//1초
		매우 느림//1.3초 
		 
		 * 
		*/
		// 5가 매우 느림 1이 매우 빠름
		switch(new_value)
		{
			// 관련 없는 경우
			case 0:
				;
			break;
			
			//매우 빠름
			
			case 1:
				setCooldownTime(0.35d);
			break;
			
			// 빠름
			case 2:
				setCooldownTime(0.5d);
			break;
			
			case 3:
				setCooldownTime(0.7d);
			break;
			case 4:
				setCooldownTime(1.0d);
			break;
			
			case 5:
				setCooldownTime(1.3d);
			break;
		}
		
	}
	public long getCooledTime()
	{
		return Time_Stamp.getTime();
	}
	public void setPlayerArmorCoefficient(double value)
	{
		AttributeInstance ArmorAttribute = MineCraftPlayer.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
		ArmorAttribute.setBaseValue(value);
		
		AttributeInstance ArmorAttribute2 = MineCraftPlayer.getAttribute(Attribute.GENERIC_ARMOR);
		ArmorAttribute2.setBaseValue(value);
	}
	public double getPlayerArmorCoefficient()
	{
		AttributeInstance ArmorAttribute = MineCraftPlayer.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
		return ArmorAttribute.getBaseValue();
		
		
	}
}
