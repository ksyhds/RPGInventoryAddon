package com.gmail.Moon_Eclipse.RIA;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.commands.Commands;
import com.gmail.Moon_Eclipse.RIA.event.*;


public class RPGInventoryAddon extends JavaPlugin
{
	public void onEnable()
	{
		/*
		 * 사용되는 네이티브 이벤트 목록
		 * EntityDamageByEntityEvent 	- 데미지를 입히거나 받는 경우
		 * InventoryCloseEvent			- 장비 설정을 끝내고 장비창을 닫는 경우
		 * PlayerItemHeldEvent			- 마우스 스크롤을 돌리거나 아이템을 주워서 아이템을 손에 든 경우
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
		
		
	}
	public void onDisable(){}

	public void AddEvent(Listener Event)
	{
		Bukkit.getPluginManager().registerEvents(Event, this);
	}
}

