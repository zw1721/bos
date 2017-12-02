package com.taotao.search;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.search.service.SearchService;
import com.taotao.search.vo.Item;
import com.taotao.search.vo.PageResult;

@Controller
@RequestMapping("search")
public class SearchController {
	@Autowired
	private SearchService searchService;
	private static final  int rows=32;
	
	//搜索功能
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView search(@RequestParam("q")String keyword,
			@RequestParam(value = "page", defaultValue = "1") Integer page){
		
		 try {
			keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		ModelAndView mv=new ModelAndView("search");
		//搜索获取分页的结果】
		
		PageResult<Item> result=this.searchService.search(keyword, page, rows);
		//查询关键词
		mv.addObject("query", keyword);
		//商品列表
		mv.addObject("itemList", result.getData());
		//当前页
		mv.addObject("page", page);
		//总页数
		int  total=result.getTotal().intValue();
		mv.addObject("pages", (total+rows-1)/rows);
		
				return mv;
		
	}
	
	
}
