package core.service;

import java.util.List;
import java.util.Map;

import system.Collections.ICollection;
import Siteview.DataSetResult;
import Siteview.QueryGroupDef;
import Siteview.SiteviewQuery;
import Siteview.Api.BusinessObject;
import Siteview.Api.BusinessObjectCollection;
import Siteview.Api.Actions.AffectedObject;

public interface IBOService {

	/*
	 * ����ҵ�����
	 */
	public abstract BusinessObject CreateBO(String strDefObjectName);

	/*
	 * ִ�п��ٲ���
	 */
	public abstract List<AffectedObject> ExecuteQuickAction(String name,
			String linkto, String id);

	/*
	 * ȡ��ҵ�����
	 */
	public abstract BusinessObject GetBO(String BOType, String BOID);

	/*
	 * ȡҵ�����Ĺ�ϵ
	 */
	public abstract BusinessObjectCollection GetRelated(String BOType,
			String BOID, String RelationType) throws Exception;

	/*
	 * ��ѯ
	 */
	public abstract ICollection QueryList(String qId,
			Map<String, String> queryParmList) throws Exception;

	/*
	 * ��ѯ
	 */
	public abstract ICollection Query(SiteviewQuery query) throws Exception;

	/*
	 * ��ѯ
	 */
	public abstract ICollection QueryForPagedBOList(QueryGroupDef qDef,
			String[] sJobId, int size, DataSetResult[] dr);

	/*
	 * ����ҵ�����
	 */
	public abstract boolean SaveBO(BusinessObject bo) throws Exception;

}