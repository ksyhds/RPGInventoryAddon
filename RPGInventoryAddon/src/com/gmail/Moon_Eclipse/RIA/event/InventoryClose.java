package com.gmail.Moon_Eclipse.RIA.event;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAUtil;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;

import ru.endlesscode.rpginventory.api.InventoryAPI;

public class InventoryClose implements Listener
{
	WrapperManager wm = new WrapperManager();
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent event)
	{
		// 플레이어가 인벤토리를 닫으면 발생하는 이벤트
		
		// 이벤트를 발생시킨 인벤토리를 받아와 변수에 저장.
		Inventory inv = event.getInventory();
		
		// 이벤트를 발생시킨 플레이어를 받아와 변수에 저장.
		Player player = (Player) event.getPlayer();
		
		// 이벤트를 발생시킨 인벤토리가 RPG인벤토리인지 아닌지 확인
		if(InventoryAPI.isRPGInventory(inv))
		{
			// RPG 인벤토리일 경우 플레이어의 RPG 인벤토리 장비들을 받아옴
			List<ItemStack> Armors = RIAUtil.getPlayerArmor(player);
			
			// 장비가 하나도 없지 않다면
			if(!Armors.isEmpty())
			{
				// 인벤토리의 장비목록을 통해 능력치 정보를 모은 맵을 얻어옴
				Map<String,Float> AttributeMap = RIAUtil.ArmorDamataExtractor(Armors);
				
				// 손에 든 아이템의 최종 스킬 공격력을 바꾸기 위해 타겟이 될 아이템을 가져옴
				ItemStack HandItem = player.getItemInHand();

				// 타겟 아이템의 정보를 기반으로 최종 스킬 공격력을 수정한 같은 아이템을 만듦
				ItemStack new_HandItem = RIAUtil.DamageDataFixer(HandItem, AttributeMap);
				
				// 방어구의 스탯 정보를 갖고 있는 맵을 해당 플레이어 객체에 저장해둠. 차후에 데미지 연산 등에 쓰임.
				wm.getRIAPlayer(player).setAttributeMap(AttributeMap);

				// 플레이어의 손에 있는 아이템을 새로운 결과 아이템으로 변경함.
				player.setItemInHand(new_HandItem);
			}
		}
	}
}
