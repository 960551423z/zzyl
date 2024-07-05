package com.zzyl.service;

import java.util.Map;

/**
 * 业务service 的接口 必须实现接口 实现其方法
 */
public interface IActFlowCustomService {
	
	
	/**
	 * 设置流程变量
	 * @param id
	 * @return
	 */
	Map<String, Object> setvariables(Long id);

}