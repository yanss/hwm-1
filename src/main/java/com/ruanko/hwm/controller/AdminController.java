package com.ruanko.hwm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruanko.hwm.bean.Admin;
import com.ruanko.hwm.bean.Music;
import com.ruanko.hwm.bean.Singer;
import com.ruanko.hwm.bean.User;
import com.ruanko.hwm.service.IAdminService;
import com.ruanko.hwm.service.IMusicService;
import com.ruanko.hwm.service.ISingerService;
import com.ruanko.hwm.service.IUserService;
import com.ruanko.hwm.utl.MD5Util;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Resource
	public IAdminService adminService;
	
	@Resource
	private IUserService userService;
	
	@Resource
	private IMusicService musicService;
	
	@Resource
	private ISingerService singerService;
	
	@RequestMapping({"/logup/"})
	public String toLogup(Model model, HttpServletRequest request) {
		model.addAttribute("title", "注册");
		model.addAttribute(new Admin());
		return "showAdminLogup";
	}
	
	@RequestMapping({"/index/"})
	public String toIndex(Model model, HttpServletRequest request) {
		model.addAttribute("title", "首页");
		return "showAdminIndex";
	}
	
	@RequestMapping({"/addMusic/","/addMusic"})
	public String toAddMusic(Model model, HttpServletRequest request) {
		//model.addAttribute("title", "首页");
		//获取所有的歌手并返回前台
		List<Singer> singerList = singerService.getAllSinger();
		model.addAttribute("singerList", singerList);
		
		model.addAttribute(new Music());
		return "showAddMusic";
	}
	
	@RequestMapping({"/manageMusic/"})
	public String toManageMusic(Model model, HttpServletRequest request) {
		//model.addAttribute("title", "首页");
		List<Music> musicList = musicService.getAllMusic();
		//System.out.println(musicList);
		model.addAttribute("musicList", musicList);
		model.addAttribute(new Music());
		return "showManageMusic";
	}
	
	@RequestMapping({"/addSinger/","/addSinger"})
	public String toAddSinger(Model model, HttpServletRequest request) {
		//model.addAttribute("title", "首页");
		model.addAttribute(new Singer());
		return "showAddSinger";
	}
	
	@RequestMapping({"/manageSinger/","/manageSinger"})
	public String toManageSinger(Model model, HttpServletRequest request) {
		//model.addAttribute("title", "首页");
		return "showManageSinger";
	}
	
	//获取所有用户
	@RequestMapping({"/manageUser/","/manageUser"})
	public String toManageUser(Model model, HttpServletRequest request) {
		System.out.println("admin中查询所有用户");
		
		List<User> userList=userService.findAllUser();
		System.out.println(userList.toString());
		model.addAttribute("userList", userList);
		
		return "showManageUser";
	}
	
	//删除用户
	@RequestMapping({"/deleteUser/","/deleteUser"})
	public String toDeleteUser(Model model, HttpServletRequest request) {
		System.out.println("管理员删除用户");
		int userId=Integer.parseInt(request.getParameter("userId"));
		System.out.println("页面获取参数："+userId);
		
		userService.deleteUserById(userId);

		return toManageUser(model, request);//spring相互调用
		
		
	}
	
	//由主键查找用户
	@RequestMapping({"/findUser/","/findUser"})
	public String toFindUser(Model model, HttpServletRequest request) {
		int userId=Integer.parseInt(request.getParameter("userId"));

		System.out.println("由主键"+userId+"查找用户");
		User userPast=userService.findUser(userId);
		System.out.println("查找到的用户"+userPast.toString());
		
		model.addAttribute("userPast", userPast);
		return "showUser";
	}
	
	//修改用户
	@RequestMapping({"/updateUser/","/updateUser"})
	public String toUpdateUser(Model model, HttpServletRequest request) {
		System.out.println("管理员修改用户");
		int userId=Integer.parseInt(request.getParameter("userId"));
		System.out.println("页面获取参数："+userId);
		
		userService.updateUser(userId);

		return toManageUser(model, request);//spring相互调用
		
		
	}
	
	
	@RequestMapping({"/addAdmin/","/addAdmin"})
	public String toAddAdmin(Model model, HttpServletRequest request) {
		//model.addAttribute("title", "首页");
		model.addAttribute(new Admin());
		return "showAddAdmin";
	}
	
	@RequestMapping({"/manageAdmin/","/manageAdmin"})
	public String toManageAdmin(Model model, HttpServletRequest request) {
		//model.addAttribute("title", "首页");
		return "showManageAdmin";
	}
	
	@RequestMapping({"/login/"})
	public String toLogin(Model model, HttpServletRequest request) {
		model.addAttribute("title", "登录");
		model.addAttribute(new Admin());
		return "showAdminLogin";
	}
	@RequestMapping({"/doLogin/"}) //登录
	public String login(@ModelAttribute("admin")Admin admin, Model model, HttpServletRequest request) throws Exception{
		System.out.println(admin.getAdminname());
		Admin ad = adminService.getAdminByName(admin.getAdminname());		
		String message = "";
		if(ad == null){
			message = "账号不存在";
			model.addAttribute("message",message);
		}else if(ad.getPassword().equalsIgnoreCase((MD5Util.getMD5Code(admin.getPassword())))){
			request.getSession().setAttribute("admin", admin);
			return "showAdminIndex";
		}else{
			message = "密码错误";
			model.addAttribute("message",message);
		}
		
		
		return "showAdminLogin";
	}
	@RequestMapping({"/doLogOut/"})
	public String LogOut(Model model, HttpServletRequest request){
		request.getSession().setAttribute("admin", null);
		model.addAttribute(new Admin());
		return "showAdminLogin";
	}
	
	@RequestMapping({"/doAddAdmin/"}) //添加管理员
	public String logup(@ModelAttribute("admin")Admin admin, Model model, HttpServletRequest request){
		//System.out.println(admin.getAdminname());	
		//model.addAttribute(new Admin());
		Admin ad = adminService.getAdminByName(admin.getAdminname());		
		String message = "";
		if(ad == null){
			admin.setPassword(MD5Util.getMD5Code(admin.getPassword()));
			adminService.addAdmin(admin);
			message ="添加成功！";
			model.addAttribute("message",message);
				
		}
		else{
			message ="该用户名已存在";
			model.addAttribute("message",message);			
		}
		model.addAttribute(new Admin());	
		return "showAddAdmin";
	}
	
}
