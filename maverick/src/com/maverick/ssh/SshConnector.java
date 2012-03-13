package com.maverick.ssh;

import com.maverick.events.EventListener;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class SshConnector
{
  public static final int SSH1 = 1;
  public static final int SSH2 = 2;
  static c f = new c();
  String e = "http://J2SSH_Maverick_1.5.17_rc_";
  String g = "http://J2SSH_Maverick_1.5.17_rc_";
  SshContext ssh1context;
  SshContext ssh2context;
  int SshVersion = 0;
  String h = "J2SSH";
  public static Throwable initException = null;
  static Log log = LogFactory.getLog(SshConnector.class);

  SshConnector()
  {
    try
    { 
//      System.out.println("this.SshVersion="+this.SshVersion);
      this.ssh1context = ((SshContext)Class.forName("com.maverick.ssh1.Ssh1Context").newInstance());
      this.SshVersion |= 1;
//      System.out.println("ssh1context this.SshVersion="+this.SshVersion);
    }
    catch (Throwable localThrowable1)
    {
      if (log.isInfoEnabled())
        log.info("SSH1 is not supported", localThrowable1);
    }
    try
    {
      this.ssh2context = ((SshContext)Class.forName("com.maverick.ssh2.Ssh2Context").newInstance());
      this.SshVersion |= 2;
//      System.out.println("ssh2context this.SshVersion="+this.SshVersion);
    }
    catch (Throwable localThrowable2)
    {
      if (log.isInfoEnabled())
        log.info("SSH2 is not supported", localThrowable2);
      initException = localThrowable2;
    }
    
    this.e += (f.c() == null ? "" : new StringBuilder().append("_").append(f.c()).toString());
//    System.out.println("this.SshVersion="+this.SshVersion+" this.e="+this.e);
  }

  public static SshConnector getInstance()
    throws SshException
  {
//    if (d.isInfoEnabled())
//      d.info("Validating license");
  
    return new SshConnector();
//    int j = f.d();
    
//    switch (j & 0x1F)
//    {
//    case 4:
//      if (d.isInfoEnabled())
//        d.info("License OK");
//      return new SshConnector();
//    case 2:
//      throw new SshException("Your license is invalid!", 11);
//    case 1:
//      throw new SshException("Your license has expired! visit http://www.sshtools.com to purchase a license", 11);
//    case 8:
//      throw new SshException("This copy of J2SSH Maverick is not licensed!", 11);
//    case 16:
//      throw new SshException("Your subscription has expired! visit http://www.sshtools.com to purchase a subscription", 11);
//    }
//    throw new SshException("Unexpected license status!", 11);    
    
  }

  public static void addEventListener(EventListener paramEventListener)
  {
    EventServiceImplementation.getInstance().addListener("", paramEventListener);
  }

  public static void addEventListener(String paramString, EventListener paramEventListener)
  {
    EventServiceImplementation.getInstance().addListener(paramString, paramEventListener);
  }

  public static void removeEventListener(String paramString)
  {
    EventServiceImplementation.getInstance().removeListener(paramString);
  }

  public final boolean isLicensed()
  {
    return true;
//    return (f.f() & 0x4) != 0;
  }

  static void b(String paramString)
  {
    f.b(paramString);
  }

  public SshContext getContext(int paramInt)
    throws SshException
  {
    if (((paramInt & 0x1) == 0) && ((paramInt & 0x2) == 0))
      throw new SshException("SshContext.getContext(int) requires value of either SSH1 or SSH2", 4);
    if ((paramInt == 1) && ((this.SshVersion & 0x1) != 0))
      return this.ssh1context;
    if ((paramInt == 2) && ((this.SshVersion & 0x2) != 0))
      return this.ssh2context;
    throw new SshException((paramInt == 1 ? "SSH1" : "SSH2") + " context is not available because it is not supported by this configuration", 4);
  }

  public void setSupportedVersions(int paramInt)
    throws SshException
  {
    if (((paramInt & 0x1) != 0) && (this.ssh1context == null))
      throw new SshException("SSH1 protocol support is not installed!", 4);
    if (((paramInt & 0x2) != 0) && (this.ssh2context == null))
      throw new SshException("SSH2 protocol support is not installed!" + initException.getMessage() + initException.getClass(), 4);
    if (((paramInt & 0x1) == 0) && ((paramInt & 0x2) == 0))
      throw new SshException("You must specify at least one supported version of the SSH protocol!", 4);
//    System.out.println("SSh Version is "+ paramInt);
    this.SshVersion = paramInt;
  }

  public int getSupportedVersions()
  {
    return this.SshVersion;
  }

  public void setKnownHosts(HostKeyVerification paramHostKeyVerification)
  {
    if (((this.SshVersion & 0x1) != 0) && (this.ssh1context != null))
      this.ssh1context.setHostKeyVerification(paramHostKeyVerification);
    if (((this.SshVersion & 0x2) != 0) && (this.ssh2context != null))
      this.ssh2context.setHostKeyVerification(paramHostKeyVerification);
  }

  public SshClient connect(SshTransport paramSshTransport, String paramString)
    throws SshException
  {
    return connect(paramSshTransport, paramString, false, null);
  }

  public SshClient connect(SshTransport paramSshTransport, String paramString, boolean paramBoolean)
    throws SshException
  {
    return connect(paramSshTransport, paramString, paramBoolean, null);
  }

  public SshClient connect(SshTransport paramSshTransport, String paramString, SshContext paramSshContext)
    throws SshException
  {
    return connect(paramSshTransport, paramString, false, paramSshContext);
  }

  public void setSoftwareVersionComments(String paramString)
  {
    this.e = paramString;
  }

  public SshClient connect(SshTransport paramSshTransport, String paramString, boolean paramBoolean, SshContext paramSshContext)
    throws SshException
  {
    if (log.isInfoEnabled()) {
      log.info("Connecting " + paramString + "@" + paramSshTransport.getHost() + ":" + paramSshTransport.getPort() + " [buffered=" + paramBoolean + "]");
      log.info("license code: " + f.b() + ", license: " + f.g());
    }
    String license;
    
    boolean bool;
    if ((f.b() == 65536) && (paramSshTransport.getHost() != "127.0.0.1") && 
    		(paramSshTransport.getHost() != "0:0:0:0:0:0:0:1") && (paramSshTransport.getHost() != "::1") && 
    		(paramSshTransport.getHost() != "localhost"))
    {
      license = f.g();
//      System.out.println("license: " + license);
      StringTokenizer localObject2 = new StringTokenizer(license, ",");
      for (bool = false; (localObject2.hasMoreTokens()) && (!bool); bool = true)
      {
    	  String localObject3 = localObject2.nextToken().trim();
        if (((!localObject3.startsWith("*.")) || (!paramSshTransport.getHost().endsWith(localObject3.substring(2)))) && (!localObject3.equalsIgnoreCase(paramSshTransport.getHost())));
      }
//      if (!bool)         throw new SshException("You are not licensed to connect to " + paramSshTransport.getHost() + " [VALID HOSTS " + license + "]", 11);
    }
    if (f.b() == 131072)
    {
      license = f.g();
//      System.out.println("license: " + license);
      StringTokenizer localObject2 = new StringTokenizer(license, ",");
      bool = false;
      Socket localObject3 = null;
      try
      {
        if (Socket.class.isAssignableFrom(paramSshTransport.getClass()))
          localObject3 = (Socket)paramSshTransport;
        else if (paramSshTransport.getClass().isAssignableFrom(Class.forName("com.sshtools.net.SocketWrapper")))
          try
          {
            Method localMethod = paramSshTransport.getClass().getMethod("getSocket", null);
            localObject3 = (Socket)localMethod.invoke(paramSshTransport, new Object[0]);
          }
          catch (Exception localException)
          {
            throw new SshException("Error attempting to determine localhost for licensing: " + localException.getMessage(), 11);
          }
//        else  throw new SshException("You are not licensed to connect using non-socket transports", 11);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        throw new SshException("Error attempting to determine localhost for licensing: " + localClassNotFoundException.getMessage(), 11);
      }
      if ((localObject3.getLocalAddress().getHostAddress() != "127.0.0.1") && 
    		  (localObject3.getLocalAddress().getHostAddress() != "0:0:0:0:0:0:0:1") && 
    		  (localObject3.getLocalAddress().getHostAddress() != "::1"))
      {
        while ((localObject2.hasMoreTokens()) && (!bool))
        {
          String str2 = localObject2.nextToken();
          bool = localObject3.getLocalAddress().getHostAddress().equals(str2);
        }
//        if (!bool) throw new SshException("You are not licensed to connect through " + ((Socket)localObject3).getLocalAddress().getHostAddress() + " [VALID HOSTS " + license + "]", 11);
      }
    }
    
    if (log.isInfoEnabled()) {
        log.info("this.SshVersion=" + this.SshVersion);
      }
    Throwable localObject2 = null;
    String str1 = null;
    String localString3 = null;
    if (((this.SshVersion & 0x2) != 0) && ((this.ssh2context != null) || ((paramSshContext != null) && (paramSshContext.getClass().getName().equals("com.maverick.ssh2.Ssh2Context")))))
    {
      str1 = "SSH-2.0-" + this.e.replace(' ', '_');
      if (str1.length() > 253)
        str1 = str1.substring(0, 253);
      str1 = str1 + "\r\n";
      try
      {
        paramSshTransport.getOutputStream().write(str1.getBytes());
      }
      catch (Throwable localThrowable1)
      {
        localObject2 = localThrowable1;
      }
      finally
      {
      }
      if (log.isDebugEnabled())
        log.debug("Attempting to determine remote version");
      localString3 = b(paramSshTransport);
      if ((checkVersion(localString3) & 0x2) != 0)
      {
        try
        {
          if (log.isDebugEnabled())
            log.debug("Attempting SSH2 connection");
          SshClient sshclient = (SshClient)Class.forName("com.maverick.ssh2.Ssh2Client").newInstance();
          if (log.isDebugEnabled())
            log.debug("Remote identification: " + localString3);
          if (log.isDebugEnabled())
            log.debug("Local identification: " + str1.trim() + " [" + this.g + "]");
          sshclient.connect(paramSshTransport, this.ssh2context == null ? paramSshContext : this.ssh2context, this, paramString, str1.trim(), localString3, paramBoolean);
          return sshclient;
        }
        catch (Throwable localThrowable2)
        {
          localObject2 = localThrowable2;
        }
        finally
        {
          if (localObject2 != null)
          {
            if ((localObject2 instanceof SshException))
              throw ((SshException)localObject2);
            throw new SshException(localObject2.getMessage() != null ? localObject2.getMessage() : localObject2.getClass().getName(), 10, localObject2);
          }
        }
      }
      else
      {
        try
        {
          paramSshTransport.close();
        }
        catch (IOException localIOException1)
        {
        }
//        if (f.b() == 262144)
//          throw new SshException("Failed to negotiate a version with the server! SSH1 is not supported by your license", 10);
        try
        {
          paramSshTransport = paramSshTransport.duplicate();
        }
        catch (IOException localIOException2)
        {
          throw new SshException("Failed to duplicate transport for SSH1 attempt", localIOException2);
        }
      }
    }
//    if (f.b() == 262144)
//      throw new SshException("Failed to negotiate a version with the server! SSH1 is not supported by your license", 10);
    try
    {
      str1 = "SSH-1.5-" + this.e.replace(' ', '_') + "\n";
      paramSshTransport.getOutputStream().write(str1.getBytes());
      localString3 = b(paramSshTransport);
      if (((this.ssh1context != null) || ((paramSshContext != null) && (paramSshContext.getClass().getName().equals("com.maverick.ssh1.Ssh1Context")))) && ((this.SshVersion & 0x1) != 0) && ((checkVersion((String)localString3) & 0x1) != 0))
      {
    	  SshClient sshclient = (SshClient)Class.forName("com.maverick.ssh1.Ssh1Client").newInstance();
        if (this.e.length() > 40)
          this.e = this.e.substring(0, 40);
        if (log.isDebugEnabled())
          log.debug("Remote identification: " + (String)localString3);
        if (log.isDebugEnabled())
          log.debug("Local identification: " + str1.trim() + " [" + this.g + "]");
        ((SshClient)sshclient).connect(paramSshTransport, this.ssh1context == null ? paramSshContext : this.ssh1context, this, paramString, str1.trim(), ((String)localString3).trim(), paramBoolean);
        return sshclient;
      }
    }
    catch (Throwable localThrowable3)
    {
      localObject2 = localThrowable3;
    }
    try
    {
      paramSshTransport.close();
    }
    catch (IOException localIOException3)
    {
      if (log.isDebugEnabled())
        log.debug("IOException when closing transport");
    }
    if (localObject2 == null)
      throw new SshException("Failed to negotiate a version with the server! supported=" + getSupportedVersions() + " id=" + (localString3 == null ? "" : (String)localString3), 10);
    if ((localObject2 instanceof SshException))
      throw ((SshException)localObject2);
    throw new SshException(((Throwable)localObject2).getMessage() != null ? ((Throwable)localObject2).getMessage() : localObject2.getClass().getName(), 10);
  }

  public static String getVersion()
  {
    return "1.5.17_rc";
  }

  public static Date getReleaseDate()
  {
    return new Date(1330107457502L);
  }

  public int determineVersion(SshTransport paramSshTransport)
    throws SshException
  {
    int j = checkVersion(b(paramSshTransport));
    try
    {
      paramSshTransport.close();
    }
    catch (IOException localIOException)
    {
    }
    return j;
  }

  String b(SshTransport paramSshTransport)
    throws SshException
  {
    try
    {
      String str = "";
      InputStream localInputStream = paramSshTransport.getInputStream();
      int j = 255;
      while (!str.startsWith("SSH-"))
      {
        StringBuffer localStringBuffer = new StringBuffer(j);
        int k;
        while (((k = localInputStream.read()) != 10) && (localStringBuffer.length() < j) && (k > -1))
        {
          if (k == 13)
            continue;
          localStringBuffer.append((char)k);
        }
        if (k == -1)
          throw new SshException("Failed to read remote identification " + localStringBuffer.toString(), 10);
        str = localStringBuffer.toString();
      }
      return str;
    }
    catch (Throwable localThrowable)
    {
    
    throw new SshException(localThrowable, 10);
    }
  }

  int checkVersion(String paramString)
    throws SshException
  {
    int j = paramString.indexOf("-");
    int k = paramString.indexOf("-", j + 1);
    String str = paramString.substring(j + 1, k);
    if (log.isInfoEnabled()) 
        log.info("paramString is: " + paramString+" version is "+str);
        
    if (str.equals("2.0"))
      return 2;
    if (str.equals("1.99"))
      return 3;
    if (str.equals("1.5"))
      return 1;
    if (str.equals("2.99"))
      return 2;
    throw new SshException("Unsupported version " + str + " detected!", 10);
  }

  public String getProduct()
  {
    return this.h;
  }

  public void setProduct(String paramString)
  {
    this.h = paramString;
  }
}