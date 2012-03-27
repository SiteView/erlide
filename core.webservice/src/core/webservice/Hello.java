package core.webservice;

import java.util.List;
import java.util.Vector;

import javax.jws.WebService;

class a{
	private String b;

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}
}

@WebService
public class Hello {
	public List<a> sayHi(String msg){
		System.out.println(msg);
		return new Vector<a>();
	}
}
