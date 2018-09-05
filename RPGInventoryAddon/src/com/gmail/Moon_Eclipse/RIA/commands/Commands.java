package com.gmail.Moon_Eclipse.RIA.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.Moon_eclipse.EclipseLib.LibMain;
import com.gmail.Moon_Eclipse.RIA.RPGInventoryAddon;
import com.gmail.Moon_Eclipse.RIA.Link_Plugin.SkillApi.Link_SkillAPI;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.gmail.Moon_Eclipse.RIA.Util.RIADebugger;
import com.gmail.Moon_Eclipse.RIA.Util.RIAUtil;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

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
						
			if( args.length < 1 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help"))
			{			
				if(sender.isOp())
				{
					sender.sendMessage("/RIA reload - 리로드");
					sender.sendMessage("RIA debug - 디버그 모드 on/off");
					sender.sendMessage("/RIA inv - 자신의 RPG인벤토리 내용을 출력");
					sender.sendMessage("/RIA get - 가장 마지막 행동의 디버그 로그 송출");
					sender.sendMessage("/RIA map player - 플레이어의 스탯 맵을 확인");
					sender.sendMessage("/RIA armor- 플레이어에게 구속 효과 사용");
					sender.sendMessage("/RIA spd 값- 플레이어에게 구속 효과 사용");
					sender.sendMessage("/RIA copy- 손에 든 아이템의 속성을 지웁니다");
				}
			}
			if(args.length >= 1)
			{
				switch(args[0])
				{
					case "debug":
							RIADebugger.DebugMod = !RIADebugger.DebugMod;
							sender.sendMessage("디버그 모드: " + RIADebugger.DebugMod);
						
					break;
					case "copy":
						
					{
						Player player = (Player) sender;
						
						ItemStack item = player.getEquipment().getItemInMainHand();
						
						item = LibMain.hideFlags_Unbreak(item);
						
						player.getEquipment().setItemInMainHand(item);
						
					}
					break;
					case "armor":
						{
							Player player = (Player) sender;
							
							RIAPlayer rp = WrapperManager.getRIAPlayer(player.getName());	
							
							sender.sendMessage("아머 계수: " + rp.getPlayerArmorCoefficient());
						}
						
						
					break;
					
					case "spd":
						sender.sendMessage("구속 레벨 설정: " + args[1]);
						{
							Player player = (Player) sender;
							
							RIAPlayer rp = WrapperManager.getRIAPlayer(player.getName());							
							
							int level = Integer.parseInt(args[1]) - 1;
							
							PotionEffect ef = new PotionEffect(PotionEffectType.CONDUIT_POWER, 99999, level);
							
							rp.ApplyPotionEffect(ef);
						}
					break;
					case "reload":
						sender.sendMessage("RIA reload");
						RPGInventoryAddon.getInstance().ReloadConfig();
						
					break;
					case "inv":
						
						Player player = (Player) sender;
						// 플레이어에게 메세지 출력
						sender.sendMessage("장비 중인 장비의 이름을 출력합니다. through RPG Inventory");
						
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
							sender.sendMessage("Native Item: " + i.getItemMeta().getDisplayName());
						}
						
						for(ItemStack i : ActiveList)
						{
							// 액티브 아이템의 이름을 플레이어에게 출력
							sender.sendMessage("Active Item: " + i.getItemMeta().getDisplayName());
						}
						
						for(ItemStack i : PassiveList)
						{
							// 패시브 아이템의 이름을 플레이어에게 출력
							sender.sendMessage("Passive Item: " + i.getItemMeta().getDisplayName());
						}
					break;
					case "get":
						RIADebugger.SendStackedDebugMessage_ConsoleAndOperator();
						RIADebugger.initialize_MessageStack();
					break;
					case "map":
						Map<String, Double> attmap = WrapperManager.getRIAPlayer(args[1]).getAttributeMap();
						Set<String> keyset = attmap.keySet();
						for(String key : keyset)
						{
							Double attvalue = attmap.get(key);
							if(attvalue != 0.0d)
							{
								sender.sendMessage(key + ": " + attvalue);
							}
						}
					break;
				}
			}
			
		}
		return true;
	}
}
