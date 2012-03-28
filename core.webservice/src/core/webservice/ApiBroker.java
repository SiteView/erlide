package core.webservice;

import Siteview.IAuthenticationBundle;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.SiteviewApi;

public class ApiBroker {
	
	private static ISiteviewApi api;
	private static String connName;
	private static String userName;
	private static String password;

	public static ISiteviewApi getApi()
    {
        if (api == null)
        {
        	api = SiteviewApi.get_CreateInstance();
        }
        return api;
    }
	
	public static boolean Login(String connName, String strLoginId, String strLoginPassword){

		boolean flag = false;
		
		getApi().Connect(connName);
		
		 IAuthenticationBundle authenticationBundle = getApi().GetAuthenticationBundle();
         authenticationBundle.set_UserType("User");
         authenticationBundle.set_AuthenticationId(strLoginId);
         authenticationBundle.set_Password(strLoginPassword);
         flag = getApi().Login(authenticationBundle);
         
         return flag;
		
	}
	
	public static boolean Login(String strLoginId, String strLoginPassword){
		boolean flag = false;
		
		IAuthenticationBundle authenticationBundle = getApi().GetAuthenticationBundle();
        authenticationBundle.set_UserType("User");
        authenticationBundle.set_AuthenticationId(strLoginId);
        authenticationBundle.set_Password(strLoginPassword);
        flag = getApi().Login(authenticationBundle);
        
        return flag;
	}
	
	public static boolean Login(){
		if (!getApi().get_Connected()){
			if (connName==null)
				connName = System.getProperty("core.service.conn");
			getApi().Connect(connName);
			
		}
		
		if (!getApi().get_LoggedIn()){
			if (userName==null)
				userName = System.getProperty("core.service.userName");
			if (password==null)
				password = System.getProperty("core.service.password");
			Login(userName,password);
			
		}
		return getApi().get_LoggedIn();
	}
}
