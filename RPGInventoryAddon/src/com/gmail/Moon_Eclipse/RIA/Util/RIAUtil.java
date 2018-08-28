package com.gmail.Moon_Eclipse.RIA.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.Moon_Eclipse.RIA.RIA_Player.RIAPlayer;
import com.gmail.Moon_Eclipse.RIA.RIA_Player.WrapperManager;

import ru.endlesscode.rpginventory.api.*;


public class RIAUtil
{
	
	static Random rnd = new Random();
	
	static WrapperManager wm = new WrapperManager();

	public static Map<String, Float> AttributeMap = new HashMap<String, Float>();

	public static void ResetAttributeMap()
	{
		// 저장 공간을 모든 유저가 공유하는것을 방지하기위해 매 초기화마다 새로운 공간을 생성해 할당함.
		AttributeMap = new HashMap<String, Float>();
		
		for(String string : RIAStats.Attribute_Names)
		{
			AttributeMap.put(string,0f);
		}
		
		// 이동속도 계수의 계산을 위해 초기값을 100으로 지정. (% 계산이기 때문)
		AttributeMap.put("Walk_Speed_Name", RIAStats.Default_Walk_Speed);
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
		
		// 스탯 이름만을 반환하도록 * 를 제거
		args[0] = args[0].replaceAll(RIAStats.Attribute_Lore_Identifier + " ", "");
		
		// 스탯 값만 반환하도록 %를 제거
		args[1] = args[1].replaceAll("%", "");
		
		return args;
	}
	public static void SetPlayerDataAndStat(Player player) 
	{
		// RIA 플레이어를 받아와 저장
		RIAPlayer rp = wm.getRIAPlayer(player);

		// RPG 인벤토리일 경우 플레이어의 RPG 인벤토리 장비들을 받아옴
		List<ItemStack> Armors = RIAUtil.getPlayerArmor(player);
		
		// 장비가 하나도 없지 않다면
		if(!Armors.isEmpty())
		{
			// 인벤토리의 장비목록을 통해 능력치 정보를 모은 맵을 얻어옴
			Map<String,Float> AttributeMap = RIAUtil.ArmorDamataExtractor(Armors);
			
			// 손에 든 아이템의 최종 스킬 공격력을 바꾸기 위해 타겟이 될 아이템을 가져옴
			ItemStack HandItem = player.getEquipment().getItemInMainHand();

			// 타겟 아이템의 정보를 기반으로 최종 스킬 공격력을 수정한 같은 아이템을 만듦
			ItemStack new_HandItem = RIAUtil.DamageDataFixer(HandItem, AttributeMap);
			
			// 방어구의 스탯 정보를 갖고 있는 맵을 해당 플레이어 객체에 저장해둠. 차후에 데미지 연산 등에 쓰임.
			rp.setAttributeMap(AttributeMap);

			// 플레이어의 손에 있는 아이템을 새로운 결과 아이템으로 변경함.
			player.getEquipment().setItemInMainHand(new_HandItem);
		}
		
		// 플레이어의 이동 속도 설정
		rp.setMoveSpeedByAttributeMap();
		
		// 플레이어의 체력 설정
		rp.setHitPointByAttributeMap();
	}
	// 손에 든 장비를 수정하는 장비 정보 수정자.
	public static ItemStack DamageDataFixer(ItemStack item, Map<String, Float> map)
	{
		//ResetAttributeMap();
		
		// 새로운 로어가 담길 저장 공간을 생성
		List<String> New_Lore = new ArrayList<String>();
		
		// 만약 아이템 속성이 있다면
		if(item.hasItemMeta())
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
						lore = ChatColor.stripColor(lore);
						
						// 구분자를 통해 로어에서 스탯 이름을 얻어옴 (아마도 "최종 스킬 공격력")
						String Attribute_name = Splitter(lore)[0];
						
						// 만약 로어의 이름과 목표하는 이름이 같다면
						if(Attribute_name.equals(RIAStats.Total_Skill_Damage_Name))
						{
							//"* &e최종 스킬 공격력: &d@.0"
							
							// 유저의 맵에서 스킬 공격력에 대한 값을 가져옴. 최종 스킬 공격력 = (스킬 공격력 + 기본 공격력) 이므로 둘을 더함.
							float att_value = map.get(RIAStats.Skill_Attack_Damage_Name) + map.get(RIAStats.Base_Attack_Damage_Name);
							
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
	public static List<ItemStack> getPlayerArmor(Player player)
	{
		// 실험을 통해 손에 들고 있는 아이템이 포함되는지 확인해봐야함.

		List<ItemStack> ActiveItems = getActiveItemStackList(player);
		List<ItemStack> PassiveItems = getPassiveItemStack(player);

		List<ItemStack> re = new ArrayList<ItemStack>();

		for(ItemStack i : ActiveItems)
		{
			re.add(i);
		}
		for(ItemStack i : PassiveItems)
		{
			re.add(i);
		}

		return re;
	}
	
	// 얻어온 장비의 스탯을 얻어와 맵을 반환하는 장비 정보 취득자
	public static Map<String,Float> ArmorDamataExtractor(List<ItemStack> items)
	{
		// 정보를 처리하기 전에 값을 담을 공간인 맵을 초기화
		ResetAttributeMap();

		for(ItemStack item : items)
		{
			if(item.hasItemMeta())
			{
				ItemMeta meta = item.getItemMeta();

				if(meta.hasLore())
				{
					List<String> Lores = meta.getLore();

					for(String lore : Lores)
					{
						//로어의 모든 색 코드를 제거
						lore = ChatColor.stripColor(lore);
						
						// 만약 로어의 내용이 속성이름에 등록되었다면.
						if(lore.contains(RIAStats.Attribute_Lore_Identifier) && hasAttributeString(RIAStats.Attribute_Names, lore))
						{
							
							// 스탯 형식
							// "* 스탯이름: +@" (+/-/% 스탯 사용)
							// %연산은 데미지 계산부 혹은 인벤토리 이벤트 연산부에서 처리하게 될 것임. 지금은 그 정도에 대해 연산해둠.
														
							
							// 구분자를 통해서 스탯 이름과 스탯 값을 구분 받음.
							String[] args = Splitter(lore);
							
							// 스탯 이름을 개별 변수에 저장 *을 없에고 저장함.
							String Attribute_Name = args[0];
							
							// 스탯 값을 새로운 변수에 저장함. + 혹은 -값이 될 수 있음. 더불어서 %를 제거함.
							String Att_value_string = args[1];
							
							// 반복문에 의해 맵에 스탯값이 누적될 수 있도록 함.
							// a 변수에는 정수형으로 치환된 스탯값을 저장.
							float Att_Value_a = Float.parseFloat(Att_value_string);
							
							// b 변수에는 스탯 이름을 키로 갖는 지금까지 누적된 스탯값을 얻어옴
							float Att_Value_b = AttributeMap.get(Attribute_Name);
							
							// c 변수에는 이전 값과 새로운 값을 더해서 저장함.
							float Att_Value_c = Att_Value_a + Att_Value_b;
								
							// 최종 값인 c변수의 값을 맵에 저장함.
							AttributeMap.put(Attribute_Name,Att_Value_c);
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
			// 각 키의 값을 불러와 변수에 저장
			String value = c_sec.getString("config.AttriuteList." + key);
			
			// 각 값을 리스트에 저장
			Att_names.add(value);
		}
		
		// 만들어진 목록을 클래스 저장공간에 저장
		RIAStats.Attribute_Names =  Att_names;
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
