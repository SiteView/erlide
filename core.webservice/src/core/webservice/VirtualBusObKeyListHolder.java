package core.webservice;

import Siteview.IVirtualBusObKeyList;
import system.DateTime;

public class VirtualBusObKeyListHolder {
	private DateTime m_dtCreatedDate = DateTime.get_Now();
    private DateTime m_dtLastAccessedDate = DateTime.get_Now();
    private IVirtualBusObKeyList m_keylist = null;
    private String m_sPageQeryJobId = "";
    
    public VirtualBusObKeyListHolder(String sJobId, IVirtualBusObKeyList keylist)
    {
        this.m_sPageQeryJobId = sJobId;
        this.m_keylist = keylist;
        this.m_dtCreatedDate = DateTime.get_Now();
        this.m_dtLastAccessedDate = DateTime.get_Now();
    }
    
    public DateTime get_CreateDate(){
    	return m_dtCreatedDate;
    }
    
    public IVirtualBusObKeyList get_KeyList(){
    	return m_keylist;
    }
    
    public DateTime get_LastAccessedDate(){
    	return this.m_dtLastAccessedDate;
    }
    
    public void set_LastAccessedDate(DateTime dt){
    	this.m_dtLastAccessedDate = dt;
    }
    
    public String get_PagedQueryJobId(){
    	return this.m_sPageQeryJobId;
    }
}
