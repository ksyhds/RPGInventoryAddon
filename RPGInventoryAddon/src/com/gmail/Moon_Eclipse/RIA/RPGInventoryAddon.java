package com.gmail.Moon_Eclipse.RIA;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAUtil;
import com.gmail.Moon_Eclipse.RIA.commands.Commands;
import com.gmail.Moon_Eclipse.RIA.event.*;


public class RPGInventoryAddon extends JavaPlugin
{
	Configuration c;
	
	public void onEnable()
	{
		/*
		 * 사용되는 네이티브 이벤트 목록
		 * EntityDamageByEntityEvent 	- 데미지를 입히거나 받는 경우
		 * InventoryCloseEvent			- 장비 설정을 끝내고 장비창을 닫는 경우
		 * PlayerItemHeldEvent			- 마우스 스크롤을 돌리거나 아이템을 주워서 아이템을 손에 든 경우
		 * 
		 * 
		*/

		// 서버에 커스텀 이벤트를 등록함.
		AddEvent(new EntityDamageByEntity());
		AddEvent(new InventoryClose());
		AddEvent(new PlayerItemHeld());
		AddEvent(new PlayerJoin());
		AddEvent(new WrapperManager());
		
		// 커맨드 클래스를 설정
		getCommand("RIA").setExecutor(new Commands(this));
		
		//서버를 켜면서 config설정을 저장
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
		// config 데이터를 불러옴.
		c = this.getConfig();
		
		// config 파일에 명시된 능력치 이름을 받아와 저장
		RIAUtil.Attribute_Names = c.getStringList("config.AttriuteList");
		
		// util의 능력치 맵을 초기화
		RIAUtil.ResetAttributeMap();
	}
}

