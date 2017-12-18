package com.yundao.workflow.dto.task.audit;

import java.util.List;

import com.yundao.workflow.dto.BasePageDto;

/**
 * 客户池分页请求数据
 *
 * @author jan
 * @create 2017-08-14 AM9:10
 **/
public class TaskPageReqDto extends BasePageDto {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;

	private List<String> candidateGroups;

	private String auditGroups;

	public String getAuditGroups() {
		return auditGroups;
	}

	public void setAuditGroups(String auditGroups) {
		this.auditGroups = auditGroups;
	}

	public List<String> getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(List<String> candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

}
