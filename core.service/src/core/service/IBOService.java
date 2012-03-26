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
	 * 创建业务对象
	 */
	public abstract BusinessObject CreateBO(String strDefObjectName);

	/*
	 * 执行快速操作
	 */
	public abstract List<AffectedObject> ExecuteQuickAction(String name,
			String linkto, String id);

	/*
	 * 取得业务对象
	 */
	public abstract BusinessObject GetBO(String BOType, String BOID);

	/*
	 * 取业务对象的关系
	 */
	public abstract BusinessObjectCollection GetRelated(String BOType,
			String BOID, String RelationType) throws Exception;

	/*
	 * 查询
	 */
	public abstract ICollection QueryList(String qId,
			Map<String, String> queryParmList) throws Exception;

	/*
	 * 查询
	 */
	public abstract ICollection Query(SiteviewQuery query) throws Exception;

	/*
	 * 查询
	 */
	public abstract ICollection QueryForPagedBOList(QueryGroupDef qDef,
			String[] sJobId, int size, DataSetResult[] dr);

	/*
	 * 保存业务对象
	 */
	public abstract boolean SaveBO(BusinessObject bo) throws Exception;

}