package com.gmail.Moon_Eclipse.RIA.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffectType;

public class RIAStats 
{
	public static int Slot_of_Health_Gauge = 0;
	
	public static double Default_Health_Point = 0d;
	
	public static double Default_Walk_Speed = 0d;
	
	public static double Default_Critical_Damage = 0d;
	
	public static List<String> Attribute_Names = new ArrayList<String>();
	
	public static List<String> PotionEffect_Names = new ArrayList<String>();
	
	public static List<String> Buff_Potion_Names = new ArrayList<String>();
	
	public static List<String> DeBuff_Potion_Names = new ArrayList<String>();
	
	public static String Walk_Speed_Name = "";
	
	public static String Total_Skill_Damage_Name = "";
	
	public static String Attribute_Lore_Identifier = "";
	
	public static String Base_Attack_Damage_Name = "";

	public static String Normal_Attack_Damage_Name = "";

	public static String Skill_Attack_Damage_Name = "";
	
	public static String Critical_Attack_Damage_Name = "";
	
	public static String Critical_Probability_Name = "";
	
	public static String Defence_Damage_Name = "";
	
	public static String Reduce_Damage_Name = "";
	
	public static String Absorption_Health_Name = "";
	
	public static String Additioanl_Health_Name = "";
	
	public static String Regeneration_Health_Name = "";
	
	public static String Attack_Speed_Name = "";
	
	public static void initialize_PotionEffect_Names()
	{
		PotionEffect_Names.add("신속");
		PotionEffect_Names.add("구속");
		PotionEffect_Names.add("성급함");
		PotionEffect_Names.add("피로");
		PotionEffect_Names.add("힘");
		PotionEffect_Names.add("즉시 회복");
		PotionEffect_Names.add("즉시 데미지");
		PotionEffect_Names.add("점프 강화");
		PotionEffect_Names.add("멀미");
		PotionEffect_Names.add("재생");
		PotionEffect_Names.add("저항");
		PotionEffect_Names.add("화염 저항");
		PotionEffect_Names.add("수중 호흡");
		PotionEffect_Names.add("투명화");
		PotionEffect_Names.add("실명");
		PotionEffect_Names.add("야간 투시");
		PotionEffect_Names.add("허기");
		PotionEffect_Names.add("나약함");
		PotionEffect_Names.add("중독");
		PotionEffect_Names.add("위더");
		PotionEffect_Names.add("체력 증진");
		PotionEffect_Names.add("흡수");
		PotionEffect_Names.add("포화");
		PotionEffect_Names.add("발광");
		PotionEffect_Names.add("공중부양");
		PotionEffect_Names.add("느린 낙하");
		PotionEffect_Names.add("행운");
		PotionEffect_Names.add("불운");
		PotionEffect_Names.add("전달체의 힘");
		PotionEffect_Names.add("돌고래의 축복");
		
		initialize_Buff_PotionEffect_Names();
		initialize_DeBuff_PotionEffect_Names();
	}
	public static void initialize_Buff_PotionEffect_Names()
	{
		Buff_Potion_Names.add("신속");
		Buff_Potion_Names.add("성급함");
		Buff_Potion_Names.add("힘");
		Buff_Potion_Names.add("즉시 회복");
		Buff_Potion_Names.add("점프 강화");
		Buff_Potion_Names.add("재생");
		Buff_Potion_Names.add("저항");
		Buff_Potion_Names.add("화염 저항");
		Buff_Potion_Names.add("수중 호흡");
		Buff_Potion_Names.add("투명화");
		Buff_Potion_Names.add("야간 투시");
		Buff_Potion_Names.add("체력 증진");
		Buff_Potion_Names.add("흡수");
		Buff_Potion_Names.add("포화");
		Buff_Potion_Names.add("발광");
		Buff_Potion_Names.add("공중부양");
		Buff_Potion_Names.add("느린 낙하");
		Buff_Potion_Names.add("행운");
		Buff_Potion_Names.add("전달체의 힘");
		Buff_Potion_Names.add("돌고래의 축복");
	}
	public static void initialize_DeBuff_PotionEffect_Names()
	{
		DeBuff_Potion_Names.add("구속");
		DeBuff_Potion_Names.add("피로");
		DeBuff_Potion_Names.add("즉시 데미지");
		DeBuff_Potion_Names.add("멀미");
		DeBuff_Potion_Names.add("실명");
		DeBuff_Potion_Names.add("허기");
		DeBuff_Potion_Names.add("나약함");
		DeBuff_Potion_Names.add("중독");
		DeBuff_Potion_Names.add("위더");
		DeBuff_Potion_Names.add("불운");
	}
}
