package core.webservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import core.service.IBOService;

import system.DateTime;
import system.Guid;
import system.Collections.ArrayList;
import system.Collections.ICollection;

import Siteview.AutoTaskDef;
import Siteview.DataSetResult;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.IVirtualBusObKeyList;
import Siteview.QueryGroupDef;
import Siteview.SiteviewQuery;
import Siteview.UpdateResult;
import Siteview.Api.AutoTaskContext;
import Siteview.Api.AutoTaskExecutor;
import Siteview.Api.BusinessObject;
import Siteview.Api.BusinessObjectCollection;
import Siteview.Api.IAutoTaskExecutor;
import Siteview.Api.Relationship;
import Siteview.Api.Actions.AffectedObject;

@WebService
public class CoreBOService implements IBOService {
	private  Map<String,VirtualBusObKeyListHolder> PageQueryJobList = new HashMap<String,VirtualBusObKeyListHolder>();

	/*
	 * 创建业务对象
	 */
	/* (non-Javadoc)
	 * @see core.webservice.IBOService#CreateBO(java.lang.String)
	 */
	@Override
	public  BusinessObject CreateBO(String strDefObjectName) {
		if (ApiBroker.Login()) {
			return ApiBroker.getApi().get_BusObService()
					.Create(strDefObjectName);
		} else {
			return null;
		}
	}
	
	/*
	 * 执行快速操作
	 */
	/* (non-Javadoc)
	 * @see core.webservice.IBOService#ExecuteQuickAction(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public  List<AffectedObject> ExecuteQuickAction(String name,String linkto,String id){
		if (ApiBroker.Login()){
			AutoTaskDef defByName = (AutoTaskDef) GetDefByName(name, AutoTaskDef.get_ClassName());
			if (defByName!=null){
				BusinessObject businessObject = ApiBroker.getApi().get_BusObService().GetBusinessObject(linkto, id);
				if (businessObject != null){
					AutoTaskContext context = null;
					if (defByName.get_LinkedTo().equals("")){
						 context = new AutoTaskContext(ApiBroker.getApi(),(BusinessObject)null, null);
					}else if (defByName.get_LinkedTo().equals(businessObject.get_Name()))
	                {
	                    context = new AutoTaskContext(ApiBroker.getApi(), businessObject, null);
	                }
					IAutoTaskExecutor executor = new AutoTaskExecutor();
					executor.Initialize(ApiBroker.getApi(), context, defByName);
					executor.Execute();
					return context.get_AffectedObjects();
				}
			}
		}
		return null;
	}
	
	/*
	 * 取得业务对象
	 */
	 /* (non-Javadoc)
	 * @see core.webservice.IBOService#GetBO(java.lang.String, java.lang.String)
	 */
	@Override
	public  BusinessObject GetBO(String BOType,String BOID){
		 if (ApiBroker.Login()){
			 BusinessObject businessObject = ApiBroker.getApi().get_BusObService().GetBusinessObject(BOType, BOID);
			 return businessObject;
		 }else{
			 return null;
		 }
	 }
	 
	 /*
	  * 取业务对象的关系
	  */
	 /* (non-Javadoc)
	 * @see core.webservice.IBOService#GetRelated(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public BusinessObjectCollection GetRelated(String BOType,String BOID,String RelationType) throws Exception{
		 if (ApiBroker.Login()){
			 BusinessObject businessObject = ApiBroker.getApi().get_BusObService().GetBusinessObject(BOType, BOID);
			 if (businessObject == null){
				 throw new Exception("Unable to get object");
			 }
			 
			 Relationship relationship = businessObject.GetRelationship(RelationType);
			 if (relationship == null)
             {
                 throw new Exception("Unable to get relations");
             }
			 
			 return relationship.get_BusinessObjects();
		 }
		 return null;
	 }
	 
	 /*
	  * 查询
	  */
	 /* (non-Javadoc)
	 * @see core.webservice.IBOService#Query(java.lang.String, java.util.Map)
	 */
	@Override
	public ICollection QueryList(String qId, Map<String,String> queryParmList) throws Exception{
		 if(ApiBroker.Login()){
			 QueryGroupDef qDef = (QueryGroupDef) ApiBroker.getApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(QueryGroupDef.get_ClassName(), qId));
			 if (qDef == null){
				 throw new Exception(String.format("QueryGroupDef %s not found", qId));
			 }
			 SiteviewQuery siteviewQuery = qDef.get_SiteviewQuery();
			 if (siteviewQuery == null)
             {
                 throw new Exception(String.format("QueryGroupDef %s does not contain a siteview query.", qId));
             }
			 
			 for(String key : queryParmList.keySet()){
				 siteviewQuery.UpdateParameterValue(key, queryParmList.get(key));
			 }
			 
			 ICollection bos = ApiBroker.getApi().get_BusObService().get_SimpleQueryResolver().ResolveQueryToBusObList(siteviewQuery);
			 
			 return bos;
		 }
		 return new ArrayList();
	 }
	 
	 /*
	  * 查询
	  */
	 /* (non-Javadoc)
	 * @see core.webservice.IBOService#Query(Siteview.SiteviewQuery)
	 */
	@Override
	public ICollection Query(SiteviewQuery query) throws Exception{
		 if(ApiBroker.Login()){
			 ICollection bos = ApiBroker.getApi().get_BusObService().get_SimpleQueryResolver().ResolveQueryToBusObList(query);
			 
			 return bos;
		 }
		 return new ArrayList();
	 }
	 
	 /*
	  * 查询
	  */
	 /* (non-Javadoc)
	 * @see core.webservice.IBOService#QueryForPagedBOList(Siteview.QueryGroupDef, java.lang.String[], int, Siteview.DataSetResult[])
	 */
	@Override
	public  ICollection QueryForPagedBOList(QueryGroupDef qDef,String[] sJobId, int size, DataSetResult[] dr){
		 
		 if(ApiBroker.Login()){
			 DataSetResult uninitialized = DataSetResult.Uninitialized;
			 VirtualBusObKeyListHolder[] holder = new VirtualBusObKeyListHolder[1];
	         boolean[] bFirstRequest = new boolean[]{false};
	         GetKeyListHolder(qDef, size, sJobId, holder,bFirstRequest);
	         String pagedQueryJobId = holder[0].get_PagedQueryJobId();
	         ICollection bos = null;
	         if (bFirstRequest[0]){
	        	 holder[0].get_KeyList().FirstPage();
	        	 bos = ApiBroker.getApi().get_BusObService().get_VirtualQueryResolver().ResolveCurrentPageToBusObList(holder[0].get_KeyList());
	        	 if (bos == null){
	        	 uninitialized = DataSetResult.NotFound;
	             }
	             else if (holder[0].get_KeyList().get_CurrentPage() < (holder[0].get_KeyList().get_TotalPages() - 1))
	             {
	                 uninitialized = DataSetResult.FoundAndMoreAvailable;
	             }
	             else
	             {
	                 uninitialized = DataSetResult.Found;
	             }
	         }else{
	        	 
	        	 holder[0].set_LastAccessedDate(DateTime.get_Now());
	        	 
	        	 if (holder[0].get_KeyList().NextPage())
                 {
                     bos = ApiBroker.getApi().get_BusObService().get_VirtualQueryResolver().ResolveCurrentPageToBusObList(holder[0].get_KeyList());
                     if (holder[0].get_KeyList().get_CurrentPage() < (holder[0].get_KeyList().get_TotalPages() - 1))
                     {
                         uninitialized = DataSetResult.FoundAndMoreAvailable;
                     }
                     else
                     {
                         uninitialized = DataSetResult.Found;
                     }
                 }
                 else
                 {
                     uninitialized = DataSetResult.NotFound;
                     PageQueryJobList.remove(pagedQueryJobId);
                 }
	         }
	         sJobId[0] = pagedQueryJobId;
	         dr[0] = uninitialized;
	         return bos;
		 }
		 
		 return null;
         
	 }
	 
	 /*
	  * 保存业务对象
	  */
	 /* (non-Javadoc)
	 * @see core.webservice.IBOService#SaveBO(Siteview.Api.BusinessObject)
	 */
	@Override
	public boolean SaveBO(BusinessObject bo) throws Exception{
		 if (ApiBroker.Login()){
			 UpdateResult result = bo.SaveObject(ApiBroker.getApi(), true, true);
			 if (!result.get_Success()){
				 throw new Exception("Save failed:" + result.get_ErrorMessages());
			 }
			 return true;
		 }
		 return false;
	 }

	 private  void GetKeyListHolder(QueryGroupDef qDef,int size, String[] sJobId, VirtualBusObKeyListHolder[] holder, boolean[] bFirstRequest){
		 
		 holder[0] = PageQueryJobList.get(sJobId);
		 
		 bFirstRequest[0] = false;
		 if (sJobId == null || sJobId.equals("") || holder[0] == null){
			 sJobId[0] = NextPageQueryJobId();
			 IVirtualBusObKeyList keylist = null;
			 if (size > 0){
				 keylist = ApiBroker.getApi().get_BusObService().get_VirtualQueryResolver().ResolveQueryToVirtualKeyList(qDef.get_SiteviewQuery(), size, size);
			 }else{
				 keylist = ApiBroker.getApi().get_BusObService().get_VirtualQueryResolver().ResolveQueryToVirtualKeyList(qDef.get_SiteviewQuery());
			 }
			 
			 holder[0] = new VirtualBusObKeyListHolder(sJobId[0], keylist);
			 PageQueryJobList.put(sJobId[0], holder[0]);
			 bFirstRequest[0] = true;
		 }
	 }
	 
	 

	 
	 private  String NextPageQueryJobId(){
		 return Guid.NewGuid().ToString("N");
	 }
	/*
	 * 取得对象定义
	 */
	private  IDefinition GetDefByName(String sQueryName, String sClassName) {
		if ((sQueryName == null) || (sQueryName.equals(""))) {
			return null;
		}
		IDefinition definition = null;

		for (int scope : ApiBroker.getApi().get_LiveDefinitionLibrary()
				.SupportedScopes(sClassName)) {
			if (scope != Siteview.Xml.Scope.Dependent) {
				String strScopeOwner = ApiBroker.getApi()
						.get_LiveDefinitionLibrary().AutoScopeOwner(scope);
				definition = ApiBroker
						.getApi()
						.get_LiveDefinitionLibrary()
						.GetDefinition(
								DefRequest.ByName(scope, strScopeOwner,
										sClassName, sQueryName));
				if (definition != null) {
					return definition;
				}
			}
		}
		return definition;
	}
}
