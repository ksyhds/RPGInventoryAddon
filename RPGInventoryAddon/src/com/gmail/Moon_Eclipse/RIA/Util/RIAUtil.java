package com.gmail.Moon_Eclipse.RIA.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;
import com.Moon_eclipse.EclipseLib.LibMain;

import ru.endlesscode.rpginventory.api.*;


public class RIAUtil
{
	
	static Random rnd = new Random();

	public static Map<String, Double> AttributeMap = new HashMap<String, Double>();

	public static void ResetAttributeMap()
	{
		// 저장 공간을 모든 유저가 공유하는것을 방지하기위해 매 초기화마다 새로운 공간을 생성해 할당함.
		AttributeMap = new HashMap<String, Double>();
		
		for(String string : RIAStats.Attribute_Names)
		{
			AttributeMap.put(string,0d);
		}
		
		// 이동속도 계수의 계산을 위해 초기값을 100으로 지정. (% 계산이기 때문)
		AttributeMap.put(RIAStats.Walk_Speed_Name, RIAStats.Default_Walk_Speed);
		
		// 치명타 피해의 계산을 위해 초기값을 설정
		AttributeMap.put(RIAStats.Critical_Attack_Damage_Name, RIAStats.Default_Critical_Damage);
	}
	public static List<ItemStack> getActiveItemStackList(Player player)
	{
		return InventoryAPI.getActiveItems(player);
	}

	public static List<ItemStack> getPassiveItemStack(Player player)
	{
		return InventoryAPI.getPassiveItems(player);
	}

	public static String[] Splitter(String lore)
	{
		// "* 스킬 공격력: +10%"
		String[] args = lore.split(": ");
		
		// 스탯 이름만을 반환하도록 * 를 제거 /  Dangling meta character 오류를 해결하기위해 //를 추가 split에 그냥 넣으면 오류가 나온다고 함.
		args[0] = args[0].replaceAll("\\" + RIAStats.Attribute_Lore_Identifier + " ", "");
		
		// 스탯 값만 반환하도록 %를 제거
		args[1] = args[1].replaceAll("%", "");
		
		return args;
	}
	public static void SetPlayerDataAndStat(Player player, int New_Item_Slot) 
	{
		// RIA 플레이어를 받아와 저장
		RIAPlayer rp = WrapperManager.getRIAPlayer(player.getName());

		// RPG 인벤토리일 경우 플레이어의 RPG 인벤토리 장비들을 받아옴
		List<ItemStack> Armors = RIAUtil.getPlayerArmor(player, New_Item_Slot);
				
		// 장비가 하나도 없지 않다면
		if(!Armors.isEmpty())
		{
			// 인벤토리의 장비목록을 통해 능력치 정보를 모은 맵을 얻어옴
			Map<String,Double> AttributeMap = RIAUtil.ArmorDamataExtractor(Armors);
			RIADebugger.AddMessage_to_MessageStack("아머 데이터 추출 후 능력치: " + AttributeMap.toString());
			
			// 플레이어 레벨을 기록해 두기 위해 맵을 사용
			AttributeMap.put("RIA_Player_Level", (double)rp.getPlayerLevel());
			
			// 메인 무기 아이템의 최종 스킬 공격력을 바꾸기 위해 타겟이 될 아이템을 가져옴
			ItemStack HandItem = player.getInventory().getItem(New_Item_Slot);
			
			// 만약 액티브아이템이 존재한다면
			if(!(HandItem == null || HandItem.getType().equals(Material.AIR)))
			{
				// 타겟 아이템의 정보를 기반으로 최종 스킬 공격력을 수정한 같은 아이템을 만듦
				ItemStack new_HandItem = RIAUtil.DamageDataFixer(HandItem, AttributeMap);

				// 플레이어의 손에 있는 아이템을 새로운 결과 아이템으로 변경함.
				// 이 부분은 차후에 변경할 것. 장비의 위치가 고정된다면 그 위치에 덮어쓰면 될 것
				player.getInventory().setItem(New_Item_Slot, new_HandItem);
			}
			
			// 장비의 스탯 정보를 갖고 있는 맵을 해당 플레이어 객체에 저장해둠. 차후에 데미지 연산 등에 쓰임.
			rp.setAttributeMap(AttributeMap);

		}
		
		// 플레이어의 이동 속도 설정
		rp.setMoveSpeedByAttributeMap();
		
		// 플레이어의 체력 설정
		rp.setHitPointByAttributeMap();
	}
	// 손에 든 장비를 수정하는 장비 정보 수정자.
	public static ItemStack DamageDataFixer(ItemStack item, Map<String, Double> map)
	{
		//ResetAttributeMap();
		
		// 새로운 로어가 담길 저장 공간을 생성
		List<String> New_Lore = new ArrayList<String>();
		
		// 만약 맨손이 아니고 손의 아이템에 아이템 속성이 있다면
		if(!(item == null || item.equals(Material.AIR)) && item.hasItemMeta())
		{
			// 아이템 속성을 저장
			ItemMeta meta = item.getItemMeta();
			
			// 만약 아이템 속성안에 로어가 있다면
			if(meta.hasLore()) 
			{
				// 로어를 저장
				List<String> Lores = meta.getLore();
				
				// 로어를 1개씩 돌려가며 하위 구문 실행
				for(String lore : Lores)
				{
					//로어가 처리 대상인지 아닌지 판별
					if(lore.contains(":")) 
					{
						//로어의 모든 색 코드를 제거
						String Striped_Lore = ChatColor.stripColor(lore);
						
						// 구분자를 통해 로어에서 스탯 이름을 얻어옴 (아마도 "최종 스킬 공격력")
						String Attribute_name = Splitter(Striped_Lore)[0];
						
						// 만약 로어의 이름과 목표하는 이름이 같다면
						if(Attribute_name.equals(RIAStats.Total_Skill_Damage_Name))
						{
							//"* &e최종 스킬 공격력: &d@.0"
							
							// 유저의 맵에서 스킬 공격력에 대한 값을 가져옴. 최종 스킬 공격력 = (스킬 공격력 + 기본 공격력) 이므로 둘을 더함.
							double att_value = map.get(RIAStats.Skill_Attack_Damage_Name) + map.get(RIAStats.Base_Attack_Damage_Name) + map.get("RIA_Player_Level");
							
							// 새로운 최종 스킬 공격력 로어를 설정. 자리수를 설정한 float 값을 적용
							String new_att = "§f" + RIAStats.Attribute_Lore_Identifier + " §e" + RIAStats.Total_Skill_Damage_Name + ": §d" + String.format("%.1f" , att_value);;
							
							// 새로운 로어에 새로 만든 최종 스킬 공격력을 추가
							New_Lore.add(new_att);
						}
						else
						{
							New_Lore.add(lore);
						}
					}
					else
					{
						New_Lore.add(lore);
					}
				}
				meta.setLore(New_Lore);
				item.setItemMeta(meta);
				return item;
			}
		}
		else
		{
			return item;
		}
		
		return item;
	}

	//RPG인벤에서 아이템을 얻어오는 장비취득자
	public static List<ItemStack> getPlayerArmor(Player player, int New_Slot)
	{
		// 실험을 통해 손에 들고 있는 아이템이 포함되는지 확인해봐야함.
		// 액티브 아이템: 무기
		// 패시브 아이템: 알과 공격무기,보조무기,갑옷을 제외한 나머지
		// 투구, 갑옷,레깅스,신발,보조무기슬롯 인식안됨
		
		// 입고있는 갑옷만 받아옴
		ItemStack[] NativeItems = player.getEquipment().getArmorContents();
		
		// 손에 들고있는 아이템을 얻어와서 저장
		ItemStack HandItem = player.getInventory().getItem(New_Slot);
		
		//  보조무기를 추가하기위해 보조무기 저장
		ItemStack OffHandItem = player.getEquipment().getItemInOffHand();
		
		
		// 액티브 아이템 - 주무기 저장 >> 설정에서 사용하지 않게 되면서 액티브 무기의 의미가 없어짐. 손의 무기를 수동으로 추가.
		List<ItemStack> ActiveItems = getActiveItemStackList(player);
		RIADebugger.AddMessage_to_MessageStack(ActiveItems.toString());
		
		// 패시브 아이템 저장
		List<ItemStack> PassiveItems = getPassiveItemStack(player);
		RIADebugger.AddMessage_to_MessageStack(PassiveItems.toString());
		

		// 종합 아이템 목록을 만들기위해 아이템 목록 생성
		List<ItemStack> re = new ArrayList<ItemStack>();

		// 목록에 아이템 추가
		for(ItemStack i : ActiveItems)
		{
			re.add(i);
		}
		for(ItemStack i : PassiveItems)
		{
			re.add(i);
		}
		for(ItemStack i : NativeItems)
		{
			// 만약 맨손이 아니고 손의 아이템에 아이템 속성이 있다면
			if(!(i == null || i.equals(Material.AIR)))
			{
				if( i.hasItemMeta())
				{
					// 아이템 속성을 저장
					ItemMeta meta = i.getItemMeta();
					
					// 만약 아이템 속성안에 로어가 있다면
					if(meta.hasLore()) 
					{
						re.add(i);
					}
				}

			}
		}
		
		// 주 무기 를 추가
		re.add(HandItem);
		
		// 마지막에 보조무기 추가
		re.add(OffHandItem);
		
		
		return re;
	}
	
	// 얻어온 장비의 스탯을 얻어와 맵을 반환하는 장비 정보 취득자
	public static Map<String,Double> ArmorDamataExtractor(List<ItemStack> items)
	{
		// 정보를 처리하기 전에 값을 담을 공간인 맵을 초기화
		ResetAttributeMap();

		for(ItemStack item : items)
		{
			if(!(item == null || item.equals(Material.AIR)) && item.hasItemMeta())
			{
				ItemMeta meta = item.getItemMeta();

				if(meta.hasLore())
				{
					List<String> Lores = meta.getLore();

					for(String lore : Lores)
					{
							
						//로어의 모든 색 코드를 제거
						lore = ChatColor.stripColor(lore);
						
						// 만약 로어가 공격 속도라면
						if(lore.contains(RIAStats.Attack_Speed_Name))
						{
							// 로어에 스탯 구분자를 추가해 스탯으로 인식하게끔 함
							lore = "* " + lore;
							
							String[] Speed = lore.split(": ");
							
							// 빠른 정도를 리스트화 함
							List<String> list = new ArrayList<String>();
							list.add("의미_없는_더미_값");list.add("매우 빠름");list.add("빠름");list.add("보통");list.add("느림");list.add("매우 느림");
							
							// 빠른 정도가 얼마나 되는지 숫자로 구해냄
							String target = LibMain.getNumberofList(list, Speed[1])+"";
							
							// 빠른 정도를 숫자로 변환함
							lore = Speed[0] + ": " + target;
							
							RIADebugger.AddMessage_to_MessageStack(lore);
							
						}
						// 만약 스탯 구분자가 있는 로어라면
						if(lore.contains(RIAStats.Attribute_Lore_Identifier))
						{
							// 구분자를 통해서 스탯 이름과 스탯 값을 구분 받음.
							String[] args = Splitter(lore);
							
							// 스탯 이름을 개별 변수에 저장 *을 없에고 저장함.
							String Attribute_Name = args[0];
							
							// 만약 로어의 내용이 속성이름에 등록되었다면.
							if(hasAttributeString(RIAStats.Attribute_Names, Attribute_Name))
							{
								RIADebugger.AddMessage_to_MessageStack("로어와 스탯의 이름: " + lore + ", " +  Attribute_Name);
								// 스탯 형식
								// "* 스탯이름: +@" (+/-/% 스탯 사용)
								// %연산은 데미지 계산부 혹은 인벤토리 이벤트 연산부에서 처리하게 될 것임. 지금은 그 정도에 대해 연산해둠.

								// 스탯 값을 새로운 변수에 저장함. + 혹은 -값이 될 수 있음. 더불어서 %를 제거함.
								String Att_value_string = args[1];
								
								// 반복문에 의해 맵에 스탯값이 누적될 수 있도록 함.
								// a 변수에는 정수형으로 치환된 스탯값을 저장.
								double Att_Value_a = Double.parseDouble(Att_value_string);
								
								// b 변수에는 스탯 이름을 키로 갖는 지금까지 누적된 스탯값을 얻어옴
								double Att_Value_b = AttributeMap.get(Attribute_Name);
								
								// c 변수에는 이전 값과 새로운 값을 더해서 저장함.
								double Att_Value_c = Att_Value_a + Att_Value_b;
									
								// 최종 값인 c변수의 값을 맵에 저장함.
								AttributeMap.put(Attribute_Name,Att_Value_c);
							}
						}
					}	
				}
				else
				{
					continue;
				}
			}
			else
			{
				continue;
			}
		}
		return AttributeMap;
	}
	//받은 리스트의 내용에 주어진 단어와 정확히 일치하는 값이 있는가?
	public static boolean hasAttributeString(List<String> Attribute_Names, String Target ) 
	{
		for(String name : Attribute_Names)
		{
			if(Target.equals(name))
			{
				return true;
			}
		}
		return false;
	}	
	// 리스트 하위항목의 value들로 리스트를 만들어서 반환해주는 메소드
	public static void setRIAAttributeList(ConfigurationSection c_sec)
	{
		// 섹션 내의 키를 모두 불러와 set에 저장
		Set<String> keys = c_sec.getKeys(false);
		
		// value 값을 담을 저장공간 리스트를 생성
		List<String> Att_names = new ArrayList<String>();
		
		// 키를 하나씩 불러와 반복
		for(String key : keys) 
		{
			// 각 키의 값을 불러와 변수에 저장 (configuration section을 받아왔기때문에 config.AttriuteList.의 절대 경로가 아닌 상대경로 만으로 충분함)
			String value = c_sec.getString(key);
			
			// 각 값을 리스트에 저장
			Att_names.add(value);
		}
		
		// 만들어진 목록을 클래스 저장공간에 저장
		RIAStats.Attribute_Names = Att_names;
	}
	public static boolean CanPlayerActivateCriticalDamage(double percent)
	{
		boolean re = false;

		int random = rnd.nextInt(99) + 1;
		double per = ((double)random / (double)100) * 100;
		//Bukkit.broadcastMessage(per + " + per");
		//Bukkit.broadcastMessage(percent + " + percent");
		if(per <= percent)
		{
			re = true;
		}
		//Bukkit.broadcastMessage("CanPlayerActivateCriticalDamage return: " + re);
		return re;
	}
}
