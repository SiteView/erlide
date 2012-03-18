

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

public class ref
{
	public static void main(String argv[])
	{
		HashMap<String,Integer> m= getModules();
		System.out.println("total " + m.size() + " modules");
		
		getRef(m);
	}
	
	static void getRef(HashMap<String,Integer> modules)
	{
		
		try
		{
			StringBuilder str = new StringBuilder();
			String line = new String(" ");
			File wf = new File("xref.txt");
			FileReader file = new FileReader(wf);
			BufferedReader in = new BufferedReader(file);
			
			TreeSet<String> out= new TreeSet<String>(); 
			int index = 1;
			String m = new String();
			boolean isCall= true;			
			while ((line = in.readLine()) != null)
			{
				if (line.isEmpty())
					continue;
				
				if (line.startsWith("{module_"))
					index = 1;
				else
					++index;
				
				String nline= line.trim();
				if (index == 1)
				{
					if(nline.startsWith("{module_call,"))
					{	
						m= nline.replace("{module_call,", "");
						isCall= true;
					}
					if(nline.startsWith("{module_use,"))
					{
						m= nline.replace("{module_use,", "");
						isCall= false;
					}
					m= m.replace("}:", "");
				} else
				{					
					nline= nline.replace("[", "");
					nline= nline.replace("]", "");
					Integer a= modules.get(m);
					if(a==null)
					{	
						System.out.println(m+ "'s number is null");
						continue;
					}
					
					String [] s= nline.split(",");
					int size= s.length;
					for(int i=0; i<size; ++i)
					{	
						Integer b= modules.get(s[i]);
						if(b==null)
							continue;
						if(a==b)
							continue;
						
						String onedata;
						if(isCall)
							onedata= a+" "+b+"\n";
						else
							onedata= b+" "+a+"\n";
						out.add(onedata);
					}
				}
			}
			file.close();
			
			for(String v:out)
				str.append(v);
			StringBuilder str0 = new StringBuilder();
			for(String key:modules.keySet())
				str0.append(modules.get(key)+" "+key+"\n");
			str0.append("#1\n");
			
			File wf2 = new File("xref.tgf");
			FileWriter file2 = new FileWriter(wf2);
			file2.write(str0.toString()+str.toString());
			file2.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	static HashMap<String,Integer> getModules()
	{
		HashMap<String,Integer> ret= new HashMap<String,Integer>();
		try
		{
			StringBuilder str = new StringBuilder();
			String line = new String(" ");
			File wf = new File("modules.txt");
			FileReader file = new FileReader(wf);
			BufferedReader in = new BufferedReader(file);
			
			while ((line = in.readLine()) != null)
			{
				String nline = new String();
				if (!line.isEmpty())
				{
					nline = line.trim();
					str.append(nline);
				} 
			}
//			System.out.println(str.toString());
			
			String [] s= str.toString().split(",");
			int size= s.length;
			for(int i=0; i<size; ++i)
			{
				if(s[i]==null || s[i].isEmpty())
					continue;
				ret.put(s[i], new Integer(i));
			}
			
			file.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
		
}
