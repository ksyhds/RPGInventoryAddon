package com.gmail.Moon_Eclipse.RIA.event;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAUtil;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;

public class PlayerItemHeld implements Listener
{
	WrapperManager wm = new WrapperManager();
	
	@EventHandler
	public void onChooseItemEvent(PlayerItemHeldEvent event)
	{
		// 플레이어가 핫바의 아이템을 변경했을때 발생하는 이벤트
		
		// 이벤트를 발생시킨 플레이어를 받아와 변수에 저장
		Player player = event.getPlayer();
		
		// 이벤트가 발생해서 손에 든 아이템이 바뀐 뒤의 아이템 (타겟)
		ItemStack HandItem = player.getItemInHand();

		// 플레이어의 데이터를 받아오기위해 인스턴스를 호출
		RIAPlayer rp = wm.getRIAPlayer(player);
		
		// 맵의 정보를 기반으로 손에 들고 있는아이템 중 최종 스킬 공격력만 수정한 똑같은 아이템을 생성
		ItemStack new_Item = RIAUtil.DamageDataFixer(HandItem, rp.getAttributeMap());
		
		// 플레이어의 손에 있는 아이템을 새로 생성된 아이템으로 변경
		player.setItemInHand(new_Item);

	}
}
