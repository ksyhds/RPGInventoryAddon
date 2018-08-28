package com.gmail.Moon_Eclipse.RIA.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RIADebugger 
{
	public static boolean DebugMod = false;
	public static List<String> MessageStack = new ArrayList<String>();
	
	// 문제를 서버와 op들에게 알리는 메소드
	public static void SendErrorMessage_OperatorAndConsole(String message) 
	{
		// 콘솔창에 기록을 남기기위해 기본라이브러리 사용
		System.out.println(message);
		
		// 접속해 있는 모든 플레이어를 대상으로
		for (Player player : Bukkit.getServer().getOnlinePlayers()) 
		{
			// op인지를 검사해서
			if (player.isOp())
			{
				// 메세지를 전송
				player.sendMessage(message);
			}
		}
	}
	
	public static void SendDebugMessage_OperatorAndConsole(String Message) 
	{
		if(DebugMod) 
		{
			// 콘솔창에 기록을 남기기위해 기본라이브러리 사용
			System.out.println(Message);
			
			// 접속해 있는 모든 플레이어를 대상으로
			for (Player player : Bukkit.getServer().getOnlinePlayers()) 
			{
				// op인지를 검사해서
				if (player.isOp())
				{
					// 메세지를 전송
					player.sendMessage(Message);
				}
			}
		}
	}
	public static void initialize_MessageStack() 
	{
		// 스택을 초기화
		MessageStack.clear();
		
		// 시간을 적기 위한 부분
		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyy년 MM월 dd일 - a hh시 mm분 ss초 ");
		
		// 시간을 형태에 맞게 추가
		RIADebugger.AddMessage_to_MessageStack("일시: " + date.format(today));
	}
	public static void AddMessage_to_MessageStack(String message) 
	{
		MessageStack.add(message);
	}
	public static void SendStackedDebugMessage_Console() 
	{
		for(String Message : MessageStack) 
		{
			System.out.println(Message);
		}
		
	}
	public static void SendStackedDebugMessage_ConsoleAndOperator() 
	{
		for(String Message : MessageStack) 
		{
			System.out.println(Message);
			
			// 접속해 있는 모든 플레이어를 대상으로
			for (Player player : Bukkit.getServer().getOnlinePlayers()) 
			{
				// op인지를 검사해서
				if (player.isOp())
				{
					// 메세지를 전송
					player.sendMessage(Message);
				}
			}
		}
		
	}
}
