package com.gmail.Moon_Eclipse.RIA.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.RPGInventoryAddon;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;

public class Commands implements CommandExecutor
{
	RPGInventoryAddon plugin;
	
	public Commands(RPGInventoryAddon instance)
	{
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		// 커맨드의 이름으로 따로 하기위한 변수 생성
		String cmd = command.getName();
		
		// 만약 커맨드가 RIA 라면
		if(cmd.equalsIgnoreCase("RIA"))
		{
			// 만약 커맨드 입력자가 플레이어라면
			if(sender instanceof Player)
			{
				// 커맨드 입력자를 플레이어형으로 변환후 저장
				Player player = (Player) sender;
				
				if( args.length < 1 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help"))
				{			
					if(player.isOp())
					{
						player.sendMessage("/RIA inv - 자신의 RPG인벤토리 내용을 출력");
						player.sendMessage("/RIA get - 가장 마지막 행동의 디버그 로그 송출");
					}
				}
				if(args.length >= 1)
				{
					switch(args[0])
					{
						case "inv":
							// 플레이어에게 메세지 출력
							player.sendMessage("장비 중인 장비의 이름을 출력합니다. through RPG Inventory");
							
							// 플레이어의 아이템들의 정보를 저장할 리스트 생성 후 리스트 입력
							List<ItemStack> ActiveList = RIAUtil.getActiveItemStackList(player);
							
							// 리스트들을 전부 확인
							for(ItemStack i : ActiveList)
							{
								// 액티브 아이템의 이름을 플레이어에게 출력
								player.sendMessage("Active Item: " + i.getItemMeta().getDisplayName());
							}
							
							// 플레이어의 아이템 정보를 저장할 리스트 생성후 리스트 입력
							List<ItemStack> PassiveList = RIAUtil.getPassiveItemStack(player);
							
							// 리스트들을 전부 확인
							for(ItemStack i : PassiveList)
							{
								// 패시브 아이템의 이름을 플레이어에게 출력
								player.sendMessage("Passive Item: " + i.getItemMeta().getDisplayName());
							}
						case "get":
							RIADebugger.SendStackedDebugMessage_ConsoleAndOperator();
					}
				}

				
				
			}
		}
		return true;
	}
}
