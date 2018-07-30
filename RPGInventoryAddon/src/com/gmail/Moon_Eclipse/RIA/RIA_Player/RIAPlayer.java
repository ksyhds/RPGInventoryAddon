package com.gmail.Moon_Eclipse.RIA.RIA_Player;

import org.bukkit.entity.Player;

public class RIAPlayer 
{
	//RIA 플레이어의 기본값이 될 마인크래프트 플레이어값을 저장하기위한 공간을 마련함.
	public final Player MineCraftPlayer;
	
	// 생성자를 생성해서 기본 변수를 초기화.
	public RIAPlayer(Player p)
	{
		// 생성된 RIA 클래스의 기존 마인크래프트 클래스를 저장.
		MineCraftPlayer = p;
	}
}
