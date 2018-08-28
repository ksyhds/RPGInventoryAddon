package com.gmail.Moon_Eclipse.RIA.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.Moon_Eclipse.RIA.RPGInventoryAddon;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
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
						player.sendMessage("/RIA reload - 리로드");
						player.sendMessage("/RIA inv - 자신의 RPG인벤토리 내용을 출력");
						player.sendMessage("/RIA get - 가장 마지막 행동의 디버그 로그 송출");
						player.sendMessage("/RIA map player - 플레이어의 스탯 맵을 확인");
						
					}
				}
				if(args.length >= 1)
				{
					switch(args[0])
					{
						case "reload":
							player.sendMessage("RIA reload");
							RPGInventoryAddon.getInstance().ReloadConfig();
						break;
						case "inv":
							// 플레이어에게 메세지 출력
							player.sendMessage("장비 중인 장비의 이름을 출력합니다. through RPG Inventory");
							
							// 플레이어의 액티브 아이템들의 정보를 저장할 리스트 생성 후 리스트 입력
							List<ItemStack> ActiveList = RIAUtil.getActiveItemStackList(player);
							
							// 플레이어의 패시브 아이템 정보를 저장할 리스트 생성후 리스트 입력
							List<ItemStack> PassiveList = RIAUtil.getPassiveItemStack(player);
							
							// 플레이어의 기본 아이템 정보를 저장함
							ItemStack[] NativeItems = player.getEquipment().getArmorContents();
							
							// 리스트들을 전부 확인
							
							for(ItemStack i : NativeItems)
							{
								// 액티브 아이템의 이름을 플레이어에게 출력
								player.sendMessage("Native Item: " + i.getItemMeta().getDisplayName());
							}
							
							for(ItemStack i : ActiveList)
							{
								// 액티브 아이템의 이름을 플레이어에게 출력
								player.sendMessage("Active Item: " + i.getItemMeta().getDisplayName());
							}
							
							for(ItemStack i : PassiveList)
							{
								// 패시브 아이템의 이름을 플레이어에게 출력
								player.sendMessage("Passive Item: " + i.getItemMeta().getDisplayName());
							}
						break;
						case "get":
							RIADebugger.SendStackedDebugMessage_ConsoleAndOperator();
							RIADebugger.initialize_MessageStack();
						break;
						case "map":
							player.sendMessage(WrapperManager.getRIAPlayer(args[1]).getAttributeMap().toString());
						break;
					}
				}

				
				
			}
		}
		return true;
	}
}
