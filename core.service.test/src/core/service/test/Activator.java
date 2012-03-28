package core.service.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import system.Collections.ArrayList;
import system.Collections.ICollection;

import Siteview.QueryInfoToGet;
import Siteview.SiteviewQuery;
import Siteview.Api.BusinessObject;
import core.service.IBOService;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		//取得注册在OSGI服务
		ServiceReference<?> sr =bundleContext.getServiceReference(IBOService.class.getName());
		IBOService s = (IBOService) bundleContext.getService(sr);
		
		if (s!=null){
			//查询员工信息
			SiteviewQuery query = new SiteviewQuery();
			query.AddBusObQuery("Profile.Employee",QueryInfoToGet.All);
			ICollection col = s.Query(query);
			ArrayList al = new ArrayList();
			al.AddRange(col);
			for(int i= 0; i < al.get_Count(); i++){
				BusinessObject bo = (BusinessObject) al.get_Item(i);
				System.out.println(bo.get_Alias()+":" + bo.GetField("LoginID").get_NativeValue());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
