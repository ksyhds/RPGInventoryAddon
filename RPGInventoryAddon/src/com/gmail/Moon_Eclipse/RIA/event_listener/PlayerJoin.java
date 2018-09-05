package com.gmail.Moon_Eclipse.RIA.event_listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.Moon_Eclipse.RIA.RPGInventoryAddon;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAStats;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

import ru.endlesscode.rpginventory.inventory.InventoryManager;
import ru.endlesscode.rpginventory.inventory.PlayerWrapper;

public class PlayerJoin implements Listener
{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event)
	{	
		// 플레이어가 서버에 접속했을때 발생하는 이벤트
		
		// 이벤트를 발생시킨 플레이어의 정보를 받아옴
		Player player = event.getPlayer();
		
		//RIA 플레이어 인스턴스를 얻어옴
		RIAPlayer RIAplayer = WrapperManager.getRIAPlayer(player.getName());
		
		BukkitTask task = new BukkitRunnable() 
		{
            @Override
            public void run() 
            {           	
        		PlayerWrapper pm = InventoryManager.get(player);
        		
        		pm.openInventory(true);
        		
        		Bukkit.getPlayer(player.getName()).closeInventory();
            }
        }.runTaskLater(RPGInventoryAddon.getInstance(), 20);
	
		// 재접속 시 스크롤을 돌리면 RIAPlayer초기화에 의해 무기 공격력이 0이 됨. 이부분을 수정해야함.	.
        
     // 해당 플레이어의 기본 걷기 속도를 플러그인에서 지정받은 기본속도로 변경
     // 오류를 피하기 위해 로그인 직후에는 입고있는 장비와 상관없이 기본 속도로 고정.
        
     RIAplayer.setMoveSpeedByAttributeMap();
		
	}
}
