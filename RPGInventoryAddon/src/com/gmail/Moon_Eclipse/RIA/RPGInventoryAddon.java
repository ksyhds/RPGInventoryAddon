package com.gmail.Moon_Eclipse.RIA;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;
import com.gmail.Moon_Eclipse.RIA.commands.Commands;
import com.gmail.Moon_Eclipse.RIA.event.*;


public class RPGInventoryAddon extends JavaPlugin
{
	
	static Configuration c;
	
	//인스턴스 반환을 위한 변수 선언
	private static RPGInventoryAddon instance;
	
	public void onEnable()
	{
		/*
		 * 사용되는 네이티브 이벤트 목록
		 * EntityDamageByEntityEvent 	- 데미지를 입히거나 받는 경우
		 * InventoryCloseEvent			- 장비 설정을 끝내고 장비창을 닫는 경우
		 * PlayerItemHeldEvent			- 마우스 스크롤을 돌리거나 아이템을 주워서 아이템을 손에 든 경우
		 * PlayerJoinEvent				- 플레이어가 서버에 접속한 경우
		 * 
		*/
		
		//기본 컨피그 파일을 생성
		this.saveDefaultConfig();
		
		//인스턴스 반환을 위한 변수 초기화
		instance = this;
		
		// 서버에 커스텀 이벤트를 등록함.
		AddEvent(new EntityDamageByEntity());
		AddEvent(new InventoryClose());
		AddEvent(new PlayerItemHeld());
		AddEvent(new PlayerJoin());
		AddEvent(new WrapperManager());
		
		// 커맨드 클래스를 설정
		getCommand("RIA").setExecutor(new Commands(this));
		
		//서버를 켜면서 config설정을 저장, 이벤트에 config 문서를 넘기기 위해 이벤트 등록보다 먼저 처리함.
		ReloadConfig();
		
	}
	public void onDisable(){}

	//이벤트 추가 메소드
	public void AddEvent(Listener Event)
	{
		// 이벤트 추가
		Bukkit.getPluginManager().registerEvents(Event, this);
	}
	
	// 리로드 시에 사용될 메소드
	public void ReloadConfig()
	{
		// config 파일 리로드
		this.reloadConfig();
		
		// config 데이터를 불러옴.
		c = this.getConfig();
				
		// RIAUtil.Attribute_Name을 configuration section을 통해 초기화
		RIAUtil.setRIAAttributeList(c.getConfigurationSection("config.AttriuteList"));;
		
		// config 파일에 명시된 총 스킬 공격력 이름을 받아와 저장
		RIAStats.Total_Skill_Damage_Name  = c.getString("config.Total_Skill_Damage_Name");
		
		// config 파일에 명시된 스탯 식별자를 받아와 저장
		RIAStats.Attribute_Lore_Identifier = c.getString("config.Lore_Identifier");
		
		// config 파일에 명시된 기본 이동 속도를 받아와 저장
		RIAStats.Default_Walk_Speed = c.getInt("config.Default_Walk_Speed") * 1.0d;
		
		// config 파일에 명시된 기본 체력을 받아와 저장
		RIAStats.Default_Health_Point = c.getInt("config.Default_Health_Point") * 1.0d;
		
		// config 파일에 명시된 기본 치명타 피해를 받아와 저장
		RIAStats.Default_Critical_Damage = c.getInt("config.Default_Critical_Damage") * 1.0d;
		
		// config 파일에 명시된 기본 공격력 스탯 이름을 받아와 저장
		RIAStats.Base_Attack_Damage_Name = c.getString("config.AttriuteList.Base_Attack_Damage_Name");
		
		// config 파일에 명시된 일반 공격력 스탯 이름을 받아와 저장
		RIAStats.Normal_Attack_Damage_Name = c.getString("config.AttriuteList.Normal_Attack_Damage_Name");
		
		// config 파일에 명시된 스킬 공격력 스탯 이름을 받아와 저장
		RIAStats.Skill_Attack_Damage_Name = c.getString("config.AttriuteList.Skill_Attack_Damage_Name");
	
		// config 파일에 명시된 이동 속도 이름을 받아와 저장
		RIAStats.Walk_Speed_Name = c.getString("config.AttriuteList.Walk_Speed_Name");
		
		// config 파일에 명시된 치명타피해 이름을 받아와 저장
		RIAStats.Critical_Attack_Damage_Name = c.getString("config.AttriuteList.Critical_Attack_Damage_Name");
		
		// config 파일에 명시된 치명타확률 이름을 받아와 저장
		RIAStats.Critical_Probability_Name = c.getString("config.AttriuteList.Critical_Probability_Name");
		 
		// config 파일에 명시된 피해 감소의 이름을 받아와 저장
		RIAStats.Defence_Damage_Name = c.getString("config.AttriuteList.Defence_Damage_Name");
		
		// config 파일에 명시된 피해 무시의 이름을 받아와 저장
		RIAStats.Reduce_Damage_Name = c.getString("config.AttriuteList.Reduce_Damage_Name");
		
		// config 파일에 명시된 생명력 흡수의 이름을 받아와 저장
		RIAStats.Absorption_Health_Name = c.getString("config.AttriuteList.Absorption_Health_Name");
		
		// config 파일에 명시된 추가적 생명력의 이름을 받아와 저장
		RIAStats.Additioanl_Health_Name = c.getString("config.AttriuteList.Additioanl_Health_Name");
		
		//config 파일에 명시된 생명력 재생의 이름을 받아와 저장
		RIAStats.Regeneration_Health_Name = c.getString("config.AttriuteList.Regeneration_Health_Name");
		
		// util의 능력치 맵을 초기화
		RIAUtil.ResetAttributeMap();
	
	}
	public static RPGInventoryAddon getInstance()
	{
		return instance;
	}	
}

