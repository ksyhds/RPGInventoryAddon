package com.gmail.Moon_Eclipse.RIA.RIA_Player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.Moon_Eclipse.RIA.RPGInventoryAddon;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;

public class RIAPlayer 
{
	//RIA 플레이어의 기본값이 될 마인크래프트 플레이어값을 저장하기위한 공간을 마련함.
	public final Player MineCraftPlayer;
	
	public static Map<String, Double> AttributeMap;

	// 생성자를 생성해서 기본 변수를 초기화.
	public RIAPlayer(Player p)
	{
		// 생성된 RIA 클래스의 기존 마인크래프트 클래스를 저장.
		MineCraftPlayer = p;
		AttributeMap = new HashMap<String,Double>();
		initialize_attmap();
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
	public void initialize_attmap()
	{
		// 저장 공간을 모든 유저가 공유하는것을 방지하기위해 매 초기화마다 새로운 공간을 생성해 할당함.
		AttributeMap.clear();
		
		for(String string : RIAStats.Attribute_Names)
		{
			AttributeMap.put(string,0d);
		}
		
		// 이동속도 계수의 계산을 위해 초기값을 100으로 지정. (% 계산이기 때문)
		AttributeMap.put(RIAStats.Walk_Speed_Name, RIAStats.Default_Walk_Speed);
		
		// 치명타 피해의 계산을 위해 초기값을 설정
		AttributeMap.put(RIAStats.Critical_Attack_Damage_Name, RIAStats.Default_Critical_Damage);
	}
	public void setMoveSpeedByAttributeMap()
	{
		// 플레이어의 기본 걷기 속도를 계산을 위해 지정. 걷기: 0.2f, 날기:0.1f
		double default_walk_speed = 0.2d;
		
		// 이동 속도 계수를 구함. 아마 150~200% = 1.5~2배
		double Speed_Coefficient = AttributeMap.get(RIAStats.Walk_Speed_Name) / 100d;
		
		// 계수를 이용해 적용할 속도를 구함
		double new_walk_speed = default_walk_speed * Speed_Coefficient;
		
		// 플레이어의 걷기 속도를 설정
		MineCraftPlayer.setWalkSpeed((float)new_walk_speed);
	}
	public void setHitPointByAttributeMap() 
	{
		// 맵에 저장되어 있는 생명력을 얻어옴
		double HP = AttributeMap.get(RIAStats.Additioanl_Health_Name);
		
		// 맵의 생명력과 기본 생명력을 더해서 최대 체력을 지정함, setMaxHealth를 대신할Attribute를 처음 사용 
		AttributeInstance healthAttribute = MineCraftPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(RIAStats.Default_Health_Point + HP);
		
	}
	public void AddHitPointByAttributeMap()
	{
		// 맵에 저장되어 있는 생명력 재생값을 얻어옴
		double Additional_HP = AttributeMap.get(RIAStats.Regeneration_Health_Name);
		
		MineCraftPlayer.setHealth(getPlayerMaxHealth() + Additional_HP);	
		
	}
	public void runpersonnalTimer() 
	{
		RIAPlayer RIAplayer = this;
        BukkitTask task = new BukkitRunnable() 
		{
        	
            @Override
            public void run() 
            {	
				double Maxhealth = RIAplayer.getPlayerMaxHealth();
				
				if(MineCraftPlayer.getHealth() < Maxhealth)
				{
					AddHitPointByAttributeMap();
				}
			}
        }.runTaskTimer(RPGInventoryAddon.getInstance(), 0, 20);
	}
}
