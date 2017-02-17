package com.ruanko.hwm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ruanko.hwm.bean.Singer;
import com.ruanko.hwm.bean.SingerTypeRela;
import com.ruanko.hwm.bean.User;
import com.ruanko.hwm.service.IUserService;
import com.ruanko.hwm.utl.DateTime;
import com.ruanko.hwm.utl.MD5Util;
import com.ruanko.hwm.utl.Upload_Download;
import com.sun.mail.handlers.message_rfc822;

@Controller
@RequestMapping("/home")
public class UserController {
	
	@Resource
	private IUserService userService;
	
	//每页项数
	private Integer pageSize = 5;
	
	/**
	 * 删除用户
	 * 
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping({ "/deleteUser" })
	public String deleteUser(Model model, HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		User user = userService.getUserById(id);
		userService.deleteUserById(id);
		// 删除歌曲相关文件
		String root = request.getSession().getServletContext().getRealPath("/WEB-INF/user");
		String username = user.getUsername();
		if(user.getImg() != "" && user.getImg() != null) {
			Upload_Download.deleteFile(root + "/img/" + username + ".jpg");
		}
		

		List<User> userList = userService.findAllUser();
		// System.out.println(musicList);
		model.addAttribute("userList", userList);
		model.addAttribute("pageSize", pageSize);
  		model.addAttribute("counts", userList.size());
		model.addAttribute(new User());
		return "showManageUser";
	}

	/**
	 * 修改用户获取用户
	 * 
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping({ "/getUser" })
	public String getUser(Model model, HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		User user = userService.getUserById(id);

//		// 获取所有的歌手并返回前台
//		List<Singer> singerList = singerService.getAllSinger();
//		model.addAttribute("singerList", singerList);

		List<User> userList = userService.findAllUser();
		// System.out.println(musicList);
		model.addAttribute("pageSize", pageSize);
  		model.addAttribute("counts", userList.size());
		model.addAttribute("singerList", userList);
		model.addAttribute("user",user);
		return "showManageUser";
	}

	/**
	 * 更新用户
	 * 
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping({ "/updateUser" })
	public String updateMusic(@ModelAttribute("user") User user, @RequestParam("imageInfo") MultipartFile image, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		User us = userService.getUserById(id);
		
		us.setAge(user.getAge());
		us.setEmail(user.getEmail());
		us.setImg(user.getUsername() + ".jpg");
		us.setTel(user.getTel());
		us.setUsername(user.getUsername());
		
		
		//System.out.println(image.getContentType());
		//String s[] = lrc.getOriginalFilename().split("\\.");
		// System.out.println(s.length);
		// 判断上传的文件类型
		if (!image.getContentType().equals("image/jpeg")) {
			model.addAttribute("message", "图片文件必须是jpg格式");
			List<User> userList = userService.findAllUser();
			//System.out.println(musicList);
			model.addAttribute("userList", userList);
			model.addAttribute("pageSize", pageSize);
	  		model.addAttribute("counts", userList.size());
			model.addAttribute(user);
			return "showManageUser";
		} 

		try {
			Upload_Download.upload_img2(image, request, user.getUsername());
		} catch (IOException e) {
			System.out.println("文件上传失败");
			e.printStackTrace();
		}

		// 播放时长
//		String time = Upload_Download
//				.getMusicLength(new File(request.getSession().getServletContext().getRealPath("/WEB-INF/music/song")
//						+ "/" + music.getMusicname() + ".mp3"));
//		mus.setMusictime(time);

		userService.updateUser(us);

//		// 更新至歌曲类别表中
//		String radio = request.getParameter("radio");
//		// System.out.println(musicId);
//		// for(String s1 : checkbox) {
//		// System.out.println(s1);
//		// }
//		updateSingerAndTypeToRela(radio, id);

		// 更新至歌曲歌手关联表
//		int singerId = Integer.parseInt(request.getParameter("select"));
//		MusicSingerRela msr = new MusicSingerRela();
//		msr.setMusicid(id);
//		msr.setSingerid(singerId);
//		musicSingerService.updateMS(msr);

		List<User> userList = userService.findAllUser();
		// System.out.println(musicList);
		model.addAttribute("singerList", userList);
		model.addAttribute("message", "修改成功");
		model.addAttribute(user);
		return "showManageUser";
	}


	/**
	 * ajax
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param totalPage
	 * @return
	 */
	@RequestMapping("/ajax_operation")
	public @ResponseBody List<User> findSingerAjax1(String pageIndex, String pageSize, String totalPage, String userName) {
		//System.out.println(musicName);
		return ajax_common1(pageIndex, pageSize, totalPage, userName);
		
	}
	
	/**
	 * 返回每页的数据
	 * @param pageIndex
	 * @param pageSize
	 * @param totalPage
	 * @return
	 */
	public List<User> ajax_common1(String pageIndex, String pageSize, String totalPage, String username) {
		List<User> userList = new ArrayList<User>();
		//判断是否为搜索
		if(username == "") {
			userList = userService.findAllUser();
			
		}else {
			List<User> sl = userService.findAllUser();
			for(User u : sl) {
				if(u.getUsername().contains((username))) {
					userList.add(u);
				}
			}
		}
//		//获取对应的歌手
//		for(Music m : resultList) {
//			//System.out.println(m.getId());
//			Singer singer = singerService.getSingerById(musicSingerService.getSingerByMusicId(m.getId()).getSingerid());
//			singerList.add(singer);
//		}
		
		Integer pageIndex1 = Integer.parseInt(pageIndex);
		Integer pageSize1 = Integer.parseInt(pageSize);
		Integer totalPage1 = Integer.parseInt(totalPage);
		List<User> resultList = new ArrayList<User>();
		if (pageIndex1 <= totalPage1 / pageSize1) {
			resultList = userList.subList((pageIndex1 - 1) * pageSize1, pageIndex1 * pageSize1);
		} else {
			resultList = userList.subList((pageIndex1 - 1) * pageSize1, totalPage1);
		}
		
		return resultList;
	}
	
	
	@RequestMapping({"/discover/"})
	public String toHome(Model model, HttpServletRequest request) {
		model.addAttribute("title", "心随乐动");
		model.addAttribute(new User());
		return "showHome";
	}
	
	@RequestMapping({"/discover/1"})
	public String toHome1(Model model, HttpServletRequest request) {
		model.addAttribute("title", "心随乐动");
		model.addAttribute(new User());
		return "showHome1";
	}
	@RequestMapping({"/discover/rankList"})
	public String toRankList(Model model, HttpServletRequest request) {
		model.addAttribute("title", "排行榜");
		return "showRankList";
	}
	
	@RequestMapping({"/discover/musicList"})
	public String toMusicList(Model model, HttpServletRequest request) {
		model.addAttribute("title", "歌单");
		return "showMusicList";
	}
	
	@RequestMapping({"/discover/radio"})
	public String toRadio(Model model, HttpServletRequest request) {
		model.addAttribute("title", "电台");
		return "showRadio";
	}
	
	@RequestMapping({"/discover/singer"})
	public String toSinger(Model model, HttpServletRequest request) {
		model.addAttribute("title", "歌手");
		return "showSinger";
	}
	
	@RequestMapping({"/discover/album"})
	public String toAlbum(Model model, HttpServletRequest request) {
		model.addAttribute("title", "新碟上架");
		return "showAlbum";
	}
	
	@RequestMapping({"/music"})
	public String toMusic(Model model, HttpServletRequest request) {
		model.addAttribute("title", "阳光宅男");
		return "showMusicInfo";
	}
	@RequestMapping({"/singer"})
	public String toSingerMess(Model model, HttpServletRequest request) {
		model.addAttribute("title", "JayChou");
		return "showSingerInfo";
	}
	@RequestMapping({"/logup"})
	public String toLogup(Model model, HttpServletRequest request) {
		model.addAttribute("title", "用户注册");
		model.addAttribute(new User());
		return "showLogup";
	}
	@RequestMapping({"/doLogup"}) //用户注册
	public String doLogup(@ModelAttribute("user") User user, Model model, HttpServletRequest request){
		System.out.println(user.getUsername());
		User user1 = userService.getUserByName(user.getUsername());
		String message = "";
		if(user1 == null){
			user.setPassword(MD5Util.getMD5Code(user.getPassword()));
			user.setImg("0");
			user.setLevel(0);
			user.setGrade(0);
			user.setPlaycount(0);
			userService.addUser(user);
			message = "注册成功！";
			model.addAttribute("message",message);
		}
		else{
			message = "该用户名已经存在";
			model.addAttribute("message",message);
			
		}
		model.addAttribute(new User());
		return "showLogup";
	}
	@RequestMapping({"/doLogin"})//用户登录
	public String doLogin(@ModelAttribute("user") User user, Model model,HttpServletRequest request){
		String message = "";
		User user1 = userService.getUserByName(user.getUsername());		
		if(user1 == null){
			message = "用户名不存在";
		}
		else if(user1.getPassword().equalsIgnoreCase(MD5Util.getMD5Code(user.getPassword()))){
			request.getSession().setAttribute("user", user1);			
		}
		else{
			message = "密码错误";
			
		}
		model.addAttribute(new User());
		request.getSession().setAttribute("message",message);	
		return "showHome";
	}
	@RequestMapping({"/clearSession/"})
	public @ResponseBody void clearSession(HttpServletRequest request) {
		request.getSession().setAttribute("message", "");
	}
	@RequestMapping({"/getPassword/"})
	public String toGetPwd(Model model, HttpServletRequest request){
		model.addAttribute("title", "找回密码");
		model.addAttribute(new User());
		model.addAttribute("message","");
		return "showGetPassword";
	}
	@RequestMapping({"/doGetPwd/"})//用户找回密码
	public String doGetPwd(@ModelAttribute("user") User user,Model model, HttpServletRequest request){
		String message = "";
		System.out.println(user.getUsername());
		User user1 = userService.getUserByName(user.getUsername());
		if(user1 == null){
			message = "该用户名未注册";	
			//model.addAttribute(new User());
			model.addAttribute("message",message);
			
		}
		else if(user1.getEmail().equals(user.getEmail())){
			//message = "验证成功";
			//model.addAttribute("message",message);
			return "showAlterPwd";
		}
		else{
			System.out.println(user1.getEmail());
			message = "邮箱和用户名不一致";
			model.addAttribute("message",message);
			
		}
		return "showGetPassword";		
	}
	@RequestMapping({"/doAlterPwd/"})//用户找回密码
	public String doAlterPwd(@ModelAttribute("user") User user,Model model, HttpServletRequest request){
		
		return null;
	}
}
