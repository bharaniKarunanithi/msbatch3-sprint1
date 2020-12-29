package com.sl.ms.sprint1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
@Cacheable
@Service
public class CacheManager {

	private List<StockDo> list = new ArrayList();;

	/**
	 * @return the list
	 */
	public List<StockDo> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<StockDo> list) {
		this.list = list;
	}
}
