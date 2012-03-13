package com.maverick.sshd.platform;

import com.maverick.ssh.components.SshPublicKey;
import com.maverick.sshd.SshContext;
import com.maverick.util.Base64;
import java.io.File;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AuthenticationProvider
{
  static Map E = Collections.synchronizedMap(new HashMap());
  static Map G = Collections.synchronizedMap(new HashMap());
  static Map A = Collections.synchronizedMap(new HashMap());
  static Map B = Collections.synchronizedMap(new HashMap());
  static Map C = Collections.synchronizedMap(new HashMap());
  NativeAuthenticationProvider F;
  SshContext D;

  public AuthenticationProvider(NativeAuthenticationProvider paramNativeAuthenticationProvider, SshContext paramSshContext)
  {
    this.F = paramNativeAuthenticationProvider;
    this.D = paramSshContext;
  }

  public SshContext getContext()
  {
    return this.D;
  }

  public boolean logon(byte[] paramArrayOfByte, String paramString1, String paramString2, SocketAddress paramSocketAddress)
    throws PasswordChangeException
  {
    if (this.F.logonUser(paramArrayOfByte, paramString1, paramString2, paramSocketAddress))
    {
      String str = Base64.encodeBytes(paramArrayOfByte, true);
      G.put(str, paramString1);
      A.put(str, paramSocketAddress);
      return true;
    }
    return false;
  }

  public boolean isSessionLoggedOn(byte[] paramArrayOfByte)
  {
    return G.containsKey(Base64.encodeBytes(paramArrayOfByte, true));
  }

  public boolean logon(byte[] paramArrayOfByte, String paramString1, String paramString2, String paramString3, SocketAddress paramSocketAddress)
    throws PasswordChangeException
  {
    if (this.F.changePassword(paramArrayOfByte, paramString1, paramString2, paramString3))
      return logon(paramArrayOfByte, paramString1, paramString3, paramSocketAddress);
    return false;
  }

  public int getAuthenticationStatus()
  {
    return this.F.getAuthenticationStatus();
  }

  public boolean logon(byte[] paramArrayOfByte, String paramString, SocketAddress paramSocketAddress)
  {
    if (this.F.logonUser(paramArrayOfByte, paramString, paramSocketAddress))
    {
      String str = Base64.encodeBytes(paramArrayOfByte, true);
      C.remove(str);
      G.put(str, paramString);
      A.put(str, paramSocketAddress);
      return true;
    }
    return false;
  }

  public boolean logon(byte[] paramArrayOfByte, String paramString, SocketAddress paramSocketAddress, SshPublicKey paramSshPublicKey, boolean paramBoolean)
  {
    if (this.F.logonUser(paramArrayOfByte, paramString, paramSocketAddress, paramSshPublicKey, paramBoolean))
    {
      if (!paramBoolean)
      {
        String str = Base64.encodeBytes(paramArrayOfByte, true);
        C.remove(str);
        G.put(str, paramString);
        A.put(str, paramSocketAddress);
      }
      return true;
    }
    return false;
  }

  public void registerSession(byte[] paramArrayOfByte, String paramString, SocketAddress paramSocketAddress)
  {
    C.put(Base64.encodeBytes(paramArrayOfByte, true), paramString);
    A.put(Base64.encodeBytes(paramArrayOfByte, true), paramSocketAddress);
  }

  public String[] getLoggedOnUsers()
  {
    HashSet localHashSet = new HashSet();
    localHashSet.addAll(G.values());
    String[] arrayOfString = new String[localHashSet.size()];
    localHashSet.toArray(arrayOfString);
    return arrayOfString;
  }

  public void logoff(byte[] paramArrayOfByte, SocketAddress paramSocketAddress)
  {
    try
    {
      if (paramArrayOfByte != null)
      {
        if (this.F != null)
          this.F.logoffUser(paramArrayOfByte, getUsername(paramArrayOfByte), paramSocketAddress);
        String str = Base64.encodeBytes(paramArrayOfByte, true);
        G.remove(str);
        A.remove(str);
        C.remove(str);
      }
    }
    catch (IOException localIOException)
    {
    }
  }

  public String getGroup(byte[] paramArrayOfByte)
    throws IOException
  {
    String str1 = Base64.encodeBytes(paramArrayOfByte, true);
    String str2 = getUsername(paramArrayOfByte);
    if ((G.containsKey(str1)) || (C.containsKey(str1)))
    {
      if (!B.containsKey(str2))
        B.put(str2, this.F.getUserGroup(paramArrayOfByte, str2));
      return (String)B.get(str2);
    }
    throw new IOException("Invalid Session ID!");
  }

  public static String getUsername(byte[] paramArrayOfByte)
    throws IOException
  {
    String str = Base64.encodeBytes(paramArrayOfByte, true);
    if (G.containsKey(str))
      return (String)G.get(str);
    if (C.containsKey(str))
      return (String)C.get(str);
    throw new IOException("Invalid Session ID!");
  }

  public static SocketAddress getRemoteAddress(byte[] paramArrayOfByte)
    throws IOException
  {
    String str = Base64.encodeBytes(paramArrayOfByte, true);
    if (A.containsKey(str))
      return (SocketAddress)A.get(str);
    throw new IOException("Invalid Session ID!");
  }

  public String getHomeDirectory(byte[] paramArrayOfByte)
    throws IOException
  {
    return getHomeDirectory(paramArrayOfByte, getUsername(paramArrayOfByte));
  }

  public String getHomeDirectory(byte[] paramArrayOfByte, String paramString)
    throws IOException
  {
    String str = this.F.getHomeDirectory(paramArrayOfByte, paramString);
    if ((str != null) && (str.length() > 0))
    {
      File localFile = new File(str);
      if (!localFile.exists())
        throw new IOException("Home directory:" + str + "does not exist for user:" + paramString);
    }
    return str;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.platform.AuthenticationProvider
 * JD-Core Version:    0.6.0
 */