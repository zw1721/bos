package com.taotao.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.search.vo.Item;
import com.taotao.search.vo.PageResult;
@Service
public class SearchService {
	@Autowired
	private HttpSolrServer server;
	
	public PageResult<Item> search(String keyword, Integer page, int rows) {
		//查询条件
		SolrQuery query=new SolrQuery("title:"+keyword);
		//设置过滤条件，只要是上架的商品
		query.setFilterQueries("status:1");
		//设置分页。page至少》=1
		query.setStart((Math.max(page, 1)-1)*rows);
		query.setRows(rows);
		
		//判断是否需要高亮
		boolean isHighlight=StringUtils.isNotEmpty(keyword)&& !StringUtils.equals("*", keyword);
		if(isHighlight){
			//开启高亮
			query.setHighlightSimplePre("<em>");
			query.setHighlightSimplePost("</em>");
			query.addHighlightField("title");
		}
		try {
			//查询并获取响应
			QueryResponse response=server.query(query);
			//获取非高亮结果
			List<Item> items=response.getBeans(Item.class);
			if(isHighlight){
				//获取高亮结果
				Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
				for (Item item : items) {
					Long id = item.getId();
					//将高亮结果回填
					item.setTitle(highlighting.get(id+"").get("title").get(0));
					
				}
			}
			   return new PageResult<>(response.getResults().getNumFound(), items);
		} catch (Exception e) {
			   e.printStackTrace();
	            // 如果有异常，返回空的结果集。不要返回null
	            return new PageResult<>(0L, new ArrayList<Item>(0));
		}
		
	}

}
