<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page isELIgnored="false"%>
<link href="<%=request.getContextPath()%>/css/front/findMusic/rankList.css"rel="stylesheet">

<div id="rank_list">
	<div style = "width:740px;height:208px; margin: 0 auto;">
		<div id = "item" style = "width:473px; height:114x; float:right;margin-right:50px; margin-top: 15px;">
			<p style ="line-height: 24px;font-size: 20px;font-weight: normal;">${typeName}</p>
			<p style = "margin-left: 28px;color:#999">每日更新</p>
		</div>
		<div id = "item" style = "width:473px; float:right;clear:both;margin-right: 50px;margin-top: 20px;">
			<div style = "width:380px;height:40px;">	<!--操作-->				
								<button style = "border:0;background-image: url('<%=request.getContextPath()%>/img/front/home/button3.png');width:66px;height:31px;text-align:right;color:white;float:left;">播放</button>
								<button onclick="addPlayList()" style = "border:0;background-image: url('<%=request.getContextPath()%>/img/front/home/button4.png');width:33px;height:31px;float:left;margin-top:-1px;" title="添加到播放列表"></button>
								<button class="btn btn-default" style = "width:75px;height:29px;float:left;margin-left:10px;font-size:12px;margin-top:1px;"><i class="glyphicon glyphicon-folder-open" style="margin-right:5px;"></i>收藏</button>
								<button class="btn btn-default" style = "width:75px;height:29px;float:left;margin-left:10px;font-size:12px;margin-top:1px;"><i class=" 	glyphicon glyphicon-download-alt" style="margin-right:5px;"></i>下载</button>
								<a href = "#pinglun" class = "btn btn-default"style = "width:100px;height:29px;float:left;margin-left:10px;font-size:12px;margin-top:1px;"><i class="glyphicon glyphicon-comment" style="margin-right:5px;"></i>评论（10）</a>
							
							</div>
		</div>
		<div id = "coverImage" style = "width:158px;height:158px;margin-top:40px;margin-left:30px; border: 1px solid #ccc;">
			<img src = "<%=request.getContextPath()%>/img/front/home/biaoshengbang.jpg" style = "margin-top:3px; margin-left: 3px;">
		</div>
	</div>
	<div id="topList">
		<div id="hotList">
			<table id="rank_musicList" class="table table-striped" style="">

				<caption>
					<h3>歌曲列表</h3>
				</caption>
				<tbody>
					<tr>
						<th></th>
						<th style = "padding-left: 50px;">歌曲</th>
						<th>时长</th>
						<th style = "padding-left: 25px;">操作</th>
						<th>歌手</th>
					</tr>
					<c:forEach items="${musicList}" var="music" varStatus="i">
						<tr>
							<td class="rank_list" id="rank_list_${i.count}" style = "width:40px;">${i.count }</td>
							<td style="">
									<span class="rank_m" id="rank_m_${music.id }" onmouseover="change_rank_music_state1(${music.id })" onmouseout="change_rank_music_state2(${music.id })" onclick="change_rank_play_state(${music.id})" title="播放"  > 
									 <i id="0"  class="glyphicon glyphicon-expand" style="width: 20px;font-size:18px;top:3px;"></i></span>
									 &nbsp;&nbsp;&nbsp; <a href="<%=request.getContextPath()%>/home/music?id=${music.id}">${music.musicname}</a>
							</td>
							<td>${music.musictime}</td>
							<td style="font-size:12px;"><a href="#" onclick="addPlayList(${music.id})" title="添加到播放列表"><span id="coll"
									class="glyphicon glyphicon-plus"  style="margin-left: 5px;"></span>&nbsp;&nbsp;</a>
								<a href="#" title="收藏"><span class="glyphicon glyphicon-heart"></span>&nbsp;&nbsp;</a>
								<a href="#" title="下载"><span class="glyphicon glyphicon-save"></span></a></td>
							<c:forEach items="${singerList}" var="singer" varStatus="j">
								<c:if test="${i.index==j.index}">
									<td><a href="<%=request.getContextPath()%>/home/singer?id=${singer.id}">${singer.singername }</a></td>
								</c:if>
							</c:forEach>
						</tr>
					
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>

<script>
	var rankList = $(".rank_list");
	
	for(var i=0;i<rankList.length;i++) {
		if(rankList.eq(i).attr("id") == "rank_list_" + 1) {
			rankList.eq(i).css({"font-size": "15px","color": "gold","font-style": "italic","font-weight": "bold"});
		}else if(rankList.eq(i).attr("id") == "rank_list_" + 2){
			rankList.eq(i).css({"font-size": "15px","color": "silver","font-style": "italic","font-weight": "bold"});
		}else if(rankList.eq(i).attr("id") == "rank_list_" + 3){
			rankList.eq(i).css({"font-size": "15px","color": "brown","font-style": "italic","font-weight": "bold"});
		}
	}
	function change_rank_music_state1(id){
		if($("#rank_m_"+id +" i").attr("id") == 0){
			//alert("#singer_m_"+id+" i");
			$("#rank_m_"+id+" i").css("color","red");
			$("#rank_m_"+id).css("cursor","pointer");
			
		}
		
	}
	
	function change_rank_music_state2(id){
		if($("#rank_m_"+id +" i").attr("id") == 0){
			$("#rank_m_"+id+" i").css("color","black");
		}
	}
	function change_rank_play_state(id) {
		var singer_m_list = $(".rank_m i");
		//alert(singer_m_list.length);
		for(var i=0;i<singer_m_list.length;i++){
			singer_m_list.eq(i).css("color","black"); 
			singer_m_list.eq(i).attr("id",0);
		}
		$("#rank_m_"+id+" i").css("color","red");
		$("#rank_m_"+id +" i").attr("id",1);
		playSongById(id);
	}
	
</script>