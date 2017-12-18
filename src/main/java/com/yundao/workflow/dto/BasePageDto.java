/**
 * Project Name:a-crm-ms
 * File Name:AbstractBaseDto.java
 * Package Name:com.zcmall.crmms.dto
 * Date:2016年10月11日上午11:47:02
 *
*/

package com.yundao.workflow.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.yundao.core.pagination.PaginationSupport;

import io.swagger.annotations.ApiModelProperty;

/**
 * ClassName:AbstractBaseDto Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年10月11日 上午11:47:02
 * 
 * @author wucq
 * @version
 */
public class BasePageDto implements Serializable {
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "当前页")
	private int currentPage;
	/**
	 * 分页的偏移量
	 */
	@ApiModelProperty(value = "不需要填写，后台自动计算")
	private int offset;
	@ApiModelProperty(value = "每页显示条数")
	private int pageSize;
	/**
	 * 排序列名
	 */
	@ApiModelProperty(value = "排序字段名")
	private String orderColumn;
	/**
	 * 升序，降序
	 */
	@ApiModelProperty(value = "升序，降序")
	private String sort;

	public int getOffset() {
		return (this.getCurrentPage() - 1) * this.getPageSize();
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPageSize() {
		if (pageSize == 0) {
			pageSize = 10;// 默认取10条
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getCurrentPage() {
		if (this.currentPage == 0) {
			this.currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 获取分页列表数据结构 newDefault:
	 * 
	 * @author: wucq
	 * @param page
	 * @return
	 * @description:
	 */
	public <T> PaginationSupport<T> getPaginationSupport() {
		PaginationSupport<T> result = new PaginationSupport<T>();
		result.setCurrentPage(this.getCurrentPage());
		result.setPageSize(this.getPageSize());
		return result;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
