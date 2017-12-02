package com.taotao.web.utils;

import com.taotao.web.vo.User;

public abstract class UserThreadLocal {
	private static final ThreadLocal<User> TL=new ThreadLocal<>();
	
	public static void set(User user){
		TL.set(user);
	}
	
	public static User get(){
		return TL.get();
	}
	public static void remove(){
		TL.remove();
	}
	
}
