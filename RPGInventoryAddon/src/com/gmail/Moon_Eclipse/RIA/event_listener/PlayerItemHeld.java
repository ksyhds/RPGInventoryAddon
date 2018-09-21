package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.ReadOnlySettings;
import com.sucy.skill.api.SkillPlugin;
import com.sucy.skill.api.player.PlayerAccounts;
import com.sucy.skill.api.player.PlayerData;

public class PlayerItemHeld implements Listener
{	
	
	@EventHandler
	public void onChooseItemEvent(PlayerItemHeldEvent event)
	{
		// 플레이어가 핫바의 아이템을 변경했을때 발생하는 이벤트
		
		// 이벤트를 발생시킨 플레이어를 받아와 변수에 저장
		Player player = event.getPlayer();
		
		// 플레이어가 집고자 하는 아이템이 있는 핫바의 위치를 얻어와 저장
		int New_Slot = event.getNewSlot();
		
		// 플레이어의 레벨을 얻어와 저장
		int Level = player.getLevel();
		
		// 플레이어가 새로 손에 들고자 하는 아이템을 얻어옴
		ItemStack Item_In_Hand = player.getInventory().getItem(New_Slot);
		
		// 만약 레벨이 낮아서 아이템을 장비할 수 없다면
		if(!RIAUtil.CanEquipIt(Item_In_Hand, Level))
		{
			// 플레이어에게 메세지 송출
			player.sendMessage(RIAStats.Level_Not_Enough_String_Prefix + Item_In_Hand.getItemMeta().getDisplayName() + RIAStats.Level_Not_Enough_String_Suffix);
			
			// 이벤트 취소
			event.setCancelled(true);
			
			//더 이상 연산하지 않도록 리턴			
			return;
		}
		
		// 플레이어의 스탯 맵과 손에든 아이템 갱신
		RIAUtil.SetPlayerDataAndStat(player, New_Slot);
		
	}
}
