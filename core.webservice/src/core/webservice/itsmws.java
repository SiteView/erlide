package core.webservice;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Endpoint;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.service.factory.AbstractServiceConfiguration;
import org.eclipse.rwt.RWT;


public class itsmws extends org.apache.cxf.transport.servlet.CXFServlet {

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		
		//Endpoint.publish("/hello", new HelloImp());
		Endpoint.publish("/hello", new Hello());

	}


}
