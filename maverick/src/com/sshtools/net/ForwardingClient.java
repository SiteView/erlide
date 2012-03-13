package com.sshtools.net;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import com.maverick.ssh.ChannelAdapter;
import com.maverick.ssh.Client;
import com.maverick.ssh.ForwardingRequestListener;
import com.maverick.ssh.SshChannel;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.SshTunnel;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.IOStreamConnector;
import com.maverick.util.IOStreamConnector.IOStreamConnectorListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ForwardingClient
  implements Client
{
  SshClient f;
  Hashtable h = new Hashtable();
  Hashtable k = new Hashtable();
  Hashtable i = new Hashtable();
  Hashtable e = new Hashtable();
  _c g = new _c();
  _b d = new _b();
  Vector j = new Vector();
  public static final String X11_KEY = "X11";
  boolean b = false;
  public final int LOWEST_RANDOM_PORT = 49152;
  public final int HIGHEST_RANDOM_PORT = 65535;
  static Log c = LogFactory.getLog(ForwardingClient.class);

  public ForwardingClient(SshClient paramSshClient)
  {
    this.f = paramSshClient;
  }

  public void addListener(ForwardingClientListener paramForwardingClientListener)
  {
    if (paramForwardingClientListener != null)
    {
      this.j.addElement(paramForwardingClientListener);
      Enumeration localEnumeration = this.e.elements();
      while (localEnumeration.hasMoreElements())
      {
        _d local_d = (_d)localEnumeration.nextElement();
        if (!local_d.c())
          continue;
        paramForwardingClientListener.forwardingStarted(1, b(local_d.f, local_d.i), local_d.g, local_d.d);
      }
      if (c.isDebugEnabled())
        c.debug("enumerated socketlisteners");
      localEnumeration = this.h.keys();
      while (localEnumeration.hasMoreElements())
      {
        String str1 = (String)localEnumeration.nextElement();
        if ((str1.equals("X11")) || ((this.f.getContext().getX11Display() != null) && (this.f.getContext().getX11Display().equals(str1))))
          continue;
        String str2 = (String)this.k.get(str1);
        String str3 = str2.substring(0, str2.indexOf(':'));
        int m = Integer.parseInt(str2.substring(str2.indexOf(':') + 1));
        paramForwardingClientListener.forwardingStarted(2, str1, str3, m);
      }
      if (c.isDebugEnabled())
        c.debug("enumerated incomingtunnels");
      String str4 = this.f.getContext().getX11Display();
      if (c.isDebugEnabled())
        c.debug("display is " + str4);
      if ((str4 != null) && (this.b))
      {
        String str5 = "localhost";
        int i1 = str4.indexOf(':');
        int n;
        if (i1 != -1)
        {
          str5 = str4.substring(0, i1);
          n = Integer.parseInt(str4.substring(i1 + 1));
        }
        else
        {
          n = Integer.parseInt(str4);
        }
        paramForwardingClientListener.forwardingStarted(3, "X11", str5, n);
      }
    }
  }

  public boolean hasRemoteForwarding(String paramString, int paramInt)
  {
    return this.k.containsKey(b(paramString, paramInt));
  }

  public boolean hasLocalForwarding(String paramString, int paramInt)
  {
    return this.e.containsKey(b(paramString, paramInt));
  }

  public void removeListener(ForwardingClientListener paramForwardingClientListener)
  {
    this.j.removeElement(paramForwardingClientListener);
  }

  public void startLocalForwarding(String paramString1, int paramInt1, String paramString2, int paramInt2)
    throws SshException
  {
    String str = b(paramString1, paramInt1);
    _d local_d = new _d(paramString1, paramInt1, paramString2, paramInt2);
    local_d.d();
    this.e.put(str, local_d);
    this.i.put(str, new Vector());
    for (int m = 0; m < this.j.size(); m++)
      ((ForwardingClientListener)this.j.elementAt(m)).forwardingStarted(1, str, paramString2, paramInt2);
    EventServiceImplementation.getInstance().fireEvent(new Event(this, 16, true).addAttribute("FORWARDING_TUNNEL_ENTRANCE", str).addAttribute("FORWARDING_TUNNEL_EXIT", paramString2 + ":" + paramInt2));
  }

  public int startLocalForwardingOnRandomPort(String paramString1, int paramInt1, String paramString2, int paramInt2)
    throws SshException
  {
    int m = 0;
    while (m < paramInt1)
      try
      {
        int n = selectRandomPort();
        String str = b(paramString1, n);
        _d local_d = new _d(paramString1, n, paramString2, paramInt2);
        local_d.d();
        this.e.put(str, local_d);
        this.i.put(str, new Vector());
        for (int i1 = 0; i1 < this.j.size(); i1++)
          ((ForwardingClientListener)this.j.elementAt(i1)).forwardingStarted(1, str, paramString2, paramInt2);
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 16, true).addAttribute("FORWARDING_TUNNEL_ENTRANCE", str).addAttribute("FORWARDING_TUNNEL_EXIT", paramString2 + ":" + paramInt2));
        return n;
      }
      catch (Throwable localThrowable)
      {
        m++;
      }
    throw new SshException("Maximum retry limit reached for random port selection", 14);
  }

  public String[] getRemoteForwardings()
  {
    String[] arrayOfString = new String[this.k.size() - (this.k.containsKey("X11") ? 1 : 0)];
    int m = 0;
    Enumeration localEnumeration = this.k.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (!str.equals("X11"))
        arrayOfString[(m++)] = str;
    }
    return arrayOfString;
  }

  public String[] getLocalForwardings()
  {
    String[] arrayOfString = new String[this.e.size()];
    int m = 0;
    Enumeration localEnumeration = this.e.keys();
    while (localEnumeration.hasMoreElements())
      arrayOfString[(m++)] = ((String)localEnumeration.nextElement());
    return arrayOfString;
  }

  public ActiveTunnel[] getLocalForwardingTunnels(String paramString)
    throws IOException
  {
    if (!this.e.containsKey(paramString))
      throw new IOException(paramString + " is not a valid local forwarding configuration");
    if (this.i.containsKey(paramString))
    {
      Vector localVector = (Vector)this.i.get(paramString);
      ActiveTunnel[] arrayOfActiveTunnel = new ActiveTunnel[localVector.size()];
      localVector.copyInto(arrayOfActiveTunnel);
      return arrayOfActiveTunnel;
    }
    return new ActiveTunnel[0];
  }

  public ActiveTunnel[] getLocalForwardingTunnels(String paramString, int paramInt)
    throws IOException
  {
    return getLocalForwardingTunnels(b(paramString, paramInt));
  }

  public ActiveTunnel[] getRemoteForwardingTunnels(String paramString)
    throws IOException
  {
    if (!this.k.containsKey(paramString))
      throw new IOException(paramString + " is not a valid remote forwarding configuration");
    if (this.h.containsKey(paramString))
    {
      Vector localVector = (Vector)this.h.get(paramString);
      ActiveTunnel[] arrayOfActiveTunnel = new ActiveTunnel[localVector.size()];
      localVector.copyInto(arrayOfActiveTunnel);
      return arrayOfActiveTunnel;
    }
    return new ActiveTunnel[0];
  }

  public boolean isXForwarding()
  {
    return this.b;
  }

  public ActiveTunnel[] getRemoteForwardingTunnels(String paramString, int paramInt)
    throws IOException
  {
    return getRemoteForwardingTunnels(b(paramString, paramInt));
  }

  public ActiveTunnel[] getX11ForwardingTunnels()
    throws IOException
  {
    if (!this.k.containsKey("X11"))
      throw new IOException("Failed to find X11 forwarding key");
    if (this.h.containsKey("X11"))
    {
      Vector localVector = (Vector)this.h.get("X11");
      ActiveTunnel[] arrayOfActiveTunnel = new ActiveTunnel[localVector.size()];
      localVector.copyInto(arrayOfActiveTunnel);
      return arrayOfActiveTunnel;
    }
    return new ActiveTunnel[0];
  }

  public boolean requestRemoteForwarding(String paramString1, int paramInt1, String paramString2, int paramInt2)
    throws SshException
  {
    if (this.f.requestRemoteForwarding(paramString1, paramInt1, paramString2, paramInt2, this.g))
    {
      String str = b(paramString1, paramInt1);
      this.h.put(str, new Vector());
      this.k.put(str, paramString2 + ":" + paramInt2);
      for (int m = 0; m < this.j.size(); m++)
        ((ForwardingClientListener)this.j.elementAt(m)).forwardingStarted(2, str, paramString2, paramInt2);
      return true;
    }
    return false;
  }

  public void allowX11Forwarding(String paramString1, String paramString2)
    throws SshException
  {
    if (this.k.containsKey("X11"))
      throw new SshException("X11 forwarding is already in use!", 14);
    this.h.put("X11", new Vector());
    this.f.getContext().setX11Display(paramString1);
    this.f.getContext().setX11RequestListener(this.g);
    byte[] arrayOfByte = new byte[16];
    if (paramString2.length() != 32)
      throw new SshException("Invalid MIT-MAGIC_COOKIE-1 value " + paramString2, 14);
    for (int m = 0; m < 32; m += 2)
      arrayOfByte[(m / 2)] = (byte)Integer.parseInt(paramString2.substring(m, m + 2), 16);
    this.f.getContext().setX11RealCookie(arrayOfByte);
    String str = "localhost";
    int n = 0;
    int i1 = paramString1.indexOf(':');
    if (i1 != -1)
    {
      str = paramString1.substring(0, i1);
      paramString1 = paramString1.substring(i1 + 1);
    }
    if ((i1 = paramString1.indexOf('.')) > -1)
      n = Integer.parseInt(paramString1.substring(i1 + 1));
    for (int i2 = 0; i2 < this.j.size(); i2++)
      ((ForwardingClientListener)this.j.elementAt(i2)).forwardingStarted(3, "X11", str, n);
    this.b = true;
  }

  public void allowX11Forwarding(String paramString)
    throws SshException
  {
    String str = "";
    try
    {
      str = System.getProperty("user.home");
    }
    catch (SecurityException localSecurityException)
    {
    }
    allowX11Forwarding(paramString, new File(str, ".Xauthority"));
  }

  public void allowX11Forwarding(String paramString, File paramFile)
    throws SshException
  {
    if (this.h.containsKey("X11"))
      throw new SshException("X11 forwarding is already in use!", 14);
    this.h.put("X11", new Vector());
    this.f.getContext().setX11Display(paramString);
    this.f.getContext().setX11RequestListener(this.g);
    try
    {
      if (paramFile.exists())
      {
        str1 = "";
        m = 0;
        n = paramString.indexOf(':');
        if (n != -1)
        {
          str1 = paramString.substring(0, n);
          m = Integer.parseInt(paramString.substring(n + 1));
        }
        FileInputStream localFileInputStream = new FileInputStream(paramFile);
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        int i2;
        while ((i2 = localFileInputStream.read()) != -1)
          localByteArrayOutputStream.write(i2);
        localFileInputStream.close();
        byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
        ByteArrayReader localByteArrayReader = new ByteArrayReader(arrayOfByte1);
        while (localByteArrayReader.available() > 0)
        {
          int i3 = localByteArrayReader.readShort();
          int i4 = localByteArrayReader.readShort();
          byte[] arrayOfByte2 = new byte[i4];
          localByteArrayReader.read(arrayOfByte2);
          i4 = localByteArrayReader.readShort();
          byte[] arrayOfByte3 = new byte[i4];
          localByteArrayReader.read(arrayOfByte3);
          i4 = localByteArrayReader.readShort();
          byte[] arrayOfByte4 = new byte[i4];
          localByteArrayReader.read(arrayOfByte4);
          i4 = localByteArrayReader.readShort();
          byte[] arrayOfByte5 = new byte[i4];
          localByteArrayReader.read(arrayOfByte5);
          String str2 = new String(arrayOfByte3);
          int i5 = Integer.parseInt(str2);
          String str3 = new String(arrayOfByte4);
          if (str3.equals("MIT-MAGIC-COOKIE-1"))
          {
            String str4;
            if (i3 == 0)
            {
              str4 = (arrayOfByte2[0] & 0xFF) + "." + (arrayOfByte2[1] & 0xFF) + "." + (arrayOfByte2[2] & 0xFF) + "." + (arrayOfByte2[3] & 0xFF);
              InetAddress localInetAddress = InetAddress.getByName(str4);
              if (((localInetAddress.getHostAddress().equals(str1)) || (localInetAddress.getHostName().equals(str1))) && (m == i5))
              {
                this.f.getContext().setX11RealCookie(arrayOfByte5);
                break;
              }
            }
            else if (i3 == 256)
            {
              str4 = new String(arrayOfByte2);
              if ((str4.equals(str1)) && (m == i5))
              {
                this.f.getContext().setX11RealCookie(arrayOfByte5);
                break;
              }
            }
          }
        }
      }
      String str1 = "localhost";
      int m = 0;
      int n = paramString.indexOf(':');
      if (n != -1)
      {
        str1 = paramString.substring(0, n);
        paramString = paramString.substring(n + 1);
      }
      if ((n = paramString.indexOf('.')) > -1)
        m = Integer.parseInt(paramString.substring(n + 1));
      for (int i1 = 0; i1 < this.j.size(); i1++)
        ((ForwardingClientListener)this.j.elementAt(i1)).forwardingStarted(3, "X11", str1, m);
      this.b = true;
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException.getMessage(), 14);
    }
  }

  public void cancelRemoteForwarding(String paramString, int paramInt)
    throws SshException
  {
    cancelRemoteForwarding(paramString, paramInt, false);
  }

  public void cancelRemoteForwarding(String paramString, int paramInt, boolean paramBoolean)
    throws SshException
  {
    String str1 = b(paramString, paramInt);
    if (!this.k.containsKey(str1))
      throw new SshException("Remote forwarding has not been started on " + str1, 14);
    if (paramBoolean)
      try
      {
        ActiveTunnel[] arrayOfActiveTunnel = getRemoteForwardingTunnels(paramString, paramInt);
        if (arrayOfActiveTunnel != null)
          for (m = 0; m < arrayOfActiveTunnel.length; m++)
            arrayOfActiveTunnel[m].stop();
      }
      catch (IOException localIOException)
      {
      }
    if (this.f == null)
      return;
    this.f.cancelRemoteForwarding(paramString, paramInt);
    this.h.remove(str1);
    String str2 = (String)this.k.get(str1);
    int m = str2.indexOf(":");
    if (m == -1)
      throw new SshException("Invalid port reference in remote forwarding key!", 5);
    String str3 = str2.substring(0, m);
    int n = Integer.parseInt(str2.substring(m + 1));
    for (int i1 = 0; i1 < this.j.size(); i1++)
    {
      if (this.j.elementAt(i1) == null)
        continue;
      ((ForwardingClientListener)this.j.elementAt(i1)).forwardingStopped(2, str1, str3, n);
    }
    this.k.remove(str1);
  }

  public synchronized void cancelAllRemoteForwarding()
    throws SshException
  {
    cancelAllRemoteForwarding(false);
  }

  public synchronized void cancelAllRemoteForwarding(boolean paramBoolean)
    throws SshException
  {
    if (this.k == null)
      return;
    Enumeration localEnumeration = this.k.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (str == null)
        return;
      try
      {
        int m = str.indexOf(':');
        int n = -1;
        if (m == -1)
        {
          n = Integer.parseInt(str);
          str = "";
        }
        else
        {
          n = Integer.parseInt(str.substring(m + 1));
          str = str.substring(0, m);
        }
        cancelRemoteForwarding(str, n, paramBoolean);
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
    }
  }

  protected int selectRandomPort()
  {
    try
    {
      int m = 16384;
      int n = ComponentManager.getInstance().getRND().nextInt() % m;
      if (n < 0)
        n = -n;
      return 49152 + n;
    }
    catch (SshException localSshException)
    {
    }
    throw new RuntimeException(localSshException.getMessage());
  }

  public synchronized void stopAllLocalForwarding()
    throws SshException
  {
    stopAllLocalForwarding(false);
  }

  public synchronized void stopAllLocalForwarding(boolean paramBoolean)
    throws SshException
  {
    Enumeration localEnumeration = this.e.keys();
    while (localEnumeration.hasMoreElements())
      stopLocalForwarding((String)localEnumeration.nextElement(), paramBoolean);
  }

  public synchronized void stopLocalForwarding(String paramString, int paramInt)
    throws SshException
  {
    stopLocalForwarding(paramString, paramInt, false);
  }

  public synchronized void stopLocalForwarding(String paramString, int paramInt, boolean paramBoolean)
    throws SshException
  {
    String str = b(paramString, paramInt);
    stopLocalForwarding(str, paramBoolean);
  }

  public synchronized void stopLocalForwarding(String paramString, boolean paramBoolean)
    throws SshException
  {
    if (paramString == null)
      return;
    if (!this.e.containsKey(paramString))
      throw new SshException("Local forwarding has not been started for " + paramString, 14);
    _d local_d = (_d)this.e.get(paramString);
    if (local_d == null)
      return;
    if (paramBoolean)
      try
      {
        ActiveTunnel[] arrayOfActiveTunnel = getLocalForwardingTunnels(paramString);
        if (arrayOfActiveTunnel != null)
          for (int n = 0; n < arrayOfActiveTunnel.length; n++)
            arrayOfActiveTunnel[n].stop();
      }
      catch (IOException localIOException)
      {
      }
    local_d.b();
    this.i.remove(paramString);
    this.e.remove(paramString);
    for (int m = 0; m < this.j.size(); m++)
    {
      if (this.j.elementAt(m) == null)
        continue;
      ((ForwardingClientListener)this.j.elementAt(m)).forwardingStopped(1, paramString, local_d.g, local_d.d);
    }
    EventServiceImplementation.getInstance().fireEvent(new Event(this, 18, true).addAttribute("FORWARDING_TUNNEL_ENTRANCE", paramString).addAttribute("FORWARDING_TUNNEL_EXIT", local_d.g + ":" + local_d.d));
  }

  String b(String paramString, int paramInt)
  {
    return paramString + ":" + String.valueOf(paramInt);
  }

  public void exit()
    throws SshException
  {
    stopAllLocalForwarding();
    cancelAllRemoteForwarding();
  }

  class _d
    implements Runnable
  {
    String f;
    int i;
    String g;
    int d;
    ServerSocket h;
    private Thread c;
    private boolean e;

    public _d(String paramInt1, int paramString1, String paramInt2, int arg5)
    {
      this.f = paramInt1;
      this.i = paramString1;
      this.g = paramInt2;
      int j;
      this.d = j;
    }

    public boolean c()
    {
      return this.e;
    }

    public void run()
    {
      try
      {
        this.e = true;
        while ((this.e) && (ForwardingClient.this.f.isConnected()))
        {
          Socket localSocket = this.h.accept();
          if ((!this.e) || (localSocket == null))
            break;
          try
          {
            ForwardingClient.this.f.openForwardingChannel(this.g, this.d, this.f, this.i, localSocket.getInetAddress().getHostAddress(), localSocket.getPort(), new SocketWrapper(localSocket), ForwardingClient.this.d);
          }
          catch (Exception localException)
          {
            try
            {
              localSocket.close();
              for (int j = 0; j < ForwardingClient.this.j.size(); j++)
                ((ForwardingClientListener)ForwardingClient.this.j.elementAt(j)).channelFailure(1, this.f + ":" + this.i, this.g, this.d, ForwardingClient.this.f.isConnected(), localException);
            }
            catch (IOException k)
            {
              for (int k = 0; k < ForwardingClient.this.j.size(); k++)
                ((ForwardingClientListener)ForwardingClient.this.j.elementAt(k)).channelFailure(1, this.f + ":" + this.i, this.g, this.d, ForwardingClient.this.f.isConnected(), localException);
            }
            finally
            {
              for (int m = 0; m < ForwardingClient.this.j.size(); m++)
                ((ForwardingClientListener)ForwardingClient.this.j.elementAt(m)).channelFailure(1, this.f + ":" + this.i, this.g, this.d, ForwardingClient.this.f.isConnected(), localException);
            }
          }
        }
      }
      catch (IOException localIOException1)
      {
      }
      finally
      {
        b();
        this.c = null;
      }
    }

    public void d()
      throws SshException
    {
      try
      {
        this.h = new ServerSocket(this.i, 50, this.f.equals("") ? null : InetAddress.getByName(this.f));
        this.c = new Thread(this);
        this.c.setDaemon(true);
        this.c.setName("SocketListener " + this.f + ":" + String.valueOf(this.i));
        this.c.start();
      }
      catch (IOException localIOException)
      {
        throw new SshException("Failed to local forwarding server. ", 6, localIOException);
      }
    }

    public void b()
    {
      try
      {
        if (this.h != null)
          this.h.close();
      }
      catch (IOException localIOException)
      {
      }
      this.h = null;
      this.e = false;
    }
  }

  public class ActiveTunnel
  {
    SshTunnel d;
    IOStreamConnector c;
    IOStreamConnector f;
    _b e = new _b();

    ActiveTunnel(SshTunnel arg2)
    {
      Object localObject;
      this.d = localObject;
    }

    void b()
      throws IOException
    {
      try
      {
        for (int i = 0; i < ForwardingClient.this.j.size(); i++)
          ((ForwardingClientListener)ForwardingClient.this.j.elementAt(i)).channelOpened(this.d.isX11() ? 3 : this.d.isLocal() ? 1 : 2, this.d.isX11() ? "X11" : ForwardingClient.this.b(this.d.getListeningAddress(), this.d.getListeningPort()), this.d);
        this.f = new IOStreamConnector();
        this.f.addListener(this.e);
        this.f.setCloseInput(false);
        this.f.connect(this.d.getInputStream(), this.d.getTransport().getOutputStream());
        this.c = new IOStreamConnector();
        this.c.addListener(this.e);
        this.c.setCloseOutput(false);
        this.c.connect(this.d.getTransport().getInputStream(), this.d.getOutputStream());
        String str = ForwardingClient.this.b(this.d.getListeningAddress(), this.d.getListeningPort());
        Hashtable localHashtable = this.d.isLocal() ? ForwardingClient.this.i : ForwardingClient.this.h;
        if (!localHashtable.containsKey(str))
          localHashtable.put(str, new Vector());
        Vector localVector = (Vector)localHashtable.get(str);
        localVector.addElement(this);
      }
      catch (Exception localException1)
      {
        try
        {
          this.d.close();
        }
        catch (Exception localException2)
        {
        }
        throw new IOException("The tunnel failed to start: " + localException1.getMessage());
      }
    }

    public synchronized void stop()
    {
      if (!this.f.isClosed())
        this.f.close();
      if (!this.c.isClosed())
        this.c.close();
      String str = ForwardingClient.this.b(this.d.getListeningAddress(), this.d.getListeningPort());
      Hashtable localHashtable = this.d.isLocal() ? ForwardingClient.this.i : ForwardingClient.this.h;
      Vector localVector = (Vector)localHashtable.get(str);
      if ((localVector != null) && (localVector.contains(this)))
      {
        localVector.removeElement(this);
        for (int i = 0; i < ForwardingClient.this.j.size(); i++)
          ((ForwardingClientListener)ForwardingClient.this.j.elementAt(i)).channelClosed(this.d.isX11() ? 3 : this.d.isLocal() ? 1 : 2, this.d.isX11() ? "X11" : str, this.d);
      }
    }

    class _b
      implements IOStreamConnector.IOStreamConnectorListener
    {
      _b()
      {
      }

      public synchronized void connectorClosed(IOStreamConnector paramIOStreamConnector)
      {
        if (!ForwardingClient.ActiveTunnel.this.d.isClosed())
        {
          try
          {
            ForwardingClient.ActiveTunnel.this.d.getTransport().close();
          }
          catch (IOException localIOException)
          {
          }
          try
          {
            ForwardingClient.ActiveTunnel.this.d.close();
          }
          catch (Exception localException)
          {
          }
        }
        ForwardingClient.ActiveTunnel.this.stop();
      }

      public void dataTransfered(byte[] paramArrayOfByte, int paramInt)
      {
      }
    }
  }

  class _b extends ChannelAdapter
  {
    _b()
    {
    }

    public void channelOpened(SshChannel paramSshChannel)
    {
      if ((paramSshChannel instanceof SshTunnel))
      {
        ForwardingClient.ActiveTunnel localActiveTunnel = new ForwardingClient.ActiveTunnel(ForwardingClient.this, (SshTunnel)paramSshChannel);
        try
        {
          localActiveTunnel.b();
        }
        catch (IOException localIOException)
        {
        }
      }
    }
  }

  class _c
    implements ForwardingRequestListener
  {
    _c()
    {
    }

    public SshTransport createConnection(String paramString, int paramInt)
      throws SshException
    {
      try
      {
        return new SocketTransport(paramString, paramInt);
      }
      catch (IOException localIOException)
      {
        for (int i = 0; i < ForwardingClient.this.j.size(); i++)
          ((ForwardingClientListener)ForwardingClient.this.j.elementAt(i)).channelFailure(2, paramString + ":" + paramInt, paramString, paramInt, ForwardingClient.this.f.isConnected(), localIOException);
      }
      throw new SshException("Failed to connect", 10);
    }

    public void initializeTunnel(SshTunnel paramSshTunnel)
    {
      paramSshTunnel.addChannelEventListener(ForwardingClient.this.d);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.ForwardingClient
 * JD-Core Version:    0.6.0
 */