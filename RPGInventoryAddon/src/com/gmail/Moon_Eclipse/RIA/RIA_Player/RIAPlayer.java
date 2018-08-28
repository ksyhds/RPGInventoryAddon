package com.gmail.Moon_Eclipse.RIA.RIA_Player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;

public class RIAPlayer 
{
	//RIA 플레이어의 기본값이 될 마인크래프트 플레이어값을 저장하기위한 공간을 마련함.
	public final Player MineCraftPlayer;
	
	public static Map<String, Float> AttributeMap;

	// 생성자를 생성해서 기본 변수를 초기화.
	public RIAPlayer(Player p)
	{
		// 생성된 RIA 클래스의 기존 마인크래프트 클래스를 저장.
		MineCraftPlayer = p;
		AttributeMap = new HashMap<String,Float>();
	}
	public void setAttributeMap(Map<String, Float> Map)
	{
		AttributeMap = Map;
	}
	public Map<String, Float> getAttributeMap()
	{
		return AttributeMap;
	}
	public Player returnMinecraftPlayer() 
	{
		return MineCraftPlayer;
	}
	public void setMoveSpeedByAttributeMap()
	{
		// 플레이어의 기본 걷기 속도를 계산을 위해 지정. 걷기: 0.2f, 날기:0.1f
		float default_walk_speed = 0.2f;
		
		// 이동 속도 계수를 구함. 아마 150~200% = 1.5~2배
		float Speed_Coefficient = AttributeMap.get("이동속도") / 10f;
		
		// 계수를 이용해 적용할 속도를 구함
		float new_walk_speed = default_walk_speed * Speed_Coefficient;
		
		// 플레이어의 걷기 속도를 설정
		MineCraftPlayer.setWalkSpeed(new_walk_speed);
	}
	public void setHitPointByAttributeMap() 
	{
		// 맵에 저장되어 있는 생명력을 얻어옴
		double HP = AttributeMap.get("생명력");
		
		// 맵의 생명력과 기본 생명력을 더해서 최대 체력을 지정함, setMaxHealth를 대신할Attribute를 처음 사용 
		AttributeInstance healthAttribute = MineCraftPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(RIAStats.Default_Health_Point + HP);
		
		// 최대 체력으로 체력을 회복시킴
		MineCraftPlayer.setHealth(healthAttribute.getBaseValue());	
	}
}
