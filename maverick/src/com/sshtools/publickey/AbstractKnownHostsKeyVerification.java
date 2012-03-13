package com.sshtools.publickey;

import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshHmac;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.util.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public abstract class AbstractKnownHostsKeyVerification
  implements HostKeyVerification
{
  private Hashtable<String, Hashtable<String, SshPublicKey>> g = new Hashtable();
  private Hashtable<String, Hashtable<String, SshPublicKey>> f = new Hashtable();
  private String e;
  private boolean b;
  private boolean d = true;
  private File c;

  public AbstractKnownHostsKeyVerification()
    throws SshException
  {
    this(null);
  }

  public File getKnownHostsFile()
  {
    return this.c;
  }

  public AbstractKnownHostsKeyVerification(String paramString)
    throws SshException
  {
    FileInputStream localFileInputStream = null;
    Object localObject1;
    if (paramString == null)
    {
      localObject1 = "";
      try
      {
        localObject1 = System.getProperty("user.home");
      }
      catch (SecurityException localSecurityException)
      {
      }
      this.c = new File((String)localObject1, ".ssh" + File.separator + "known_hosts");
      paramString = this.c.getAbsolutePath();
    }
    else
    {
      this.c = new File(paramString);
    }
    try
    {
      if (System.getSecurityManager() != null)
        System.getSecurityManager().checkRead(paramString);
      Object localObject2;
      if (this.c.exists())
      {
        localFileInputStream = new FileInputStream(this.c);
        localObject1 = new BufferedReader(new InputStreamReader(localFileInputStream));
        while ((localObject2 = ((BufferedReader)localObject1).readLine()) != null)
        {
          localObject2 = ((String)localObject2).trim();
          if (((String)localObject2).equals(""))
            continue;
          StringTokenizer localStringTokenizer = new StringTokenizer((String)localObject2, " ");
          if (!localStringTokenizer.hasMoreTokens())
          {
            onInvalidHostEntry((String)localObject2);
            continue;
          }
          String str1 = (String)localStringTokenizer.nextElement();
          try
          {
            if (!localStringTokenizer.hasMoreTokens())
            {
              onInvalidHostEntry((String)localObject2);
              continue;
            }
            Integer.parseInt((String)localStringTokenizer.nextElement());
            if (!localStringTokenizer.hasMoreTokens())
            {
              onInvalidHostEntry((String)localObject2);
              continue;
            }
            String str2 = (String)localStringTokenizer.nextElement();
            if (!localStringTokenizer.hasMoreTokens())
            {
              onInvalidHostEntry((String)localObject2);
              continue;
            }
            str3 = (String)localStringTokenizer.nextElement();
            localObject3 = new BigInteger(str2);
            BigInteger localBigInteger = new BigInteger(str3);
            c(str1, ComponentManager.getInstance().createRsaPublicKey(localBigInteger, (BigInteger)localObject3, 1), true);
          }
          catch (OutOfMemoryError localOutOfMemoryError1)
          {
            throw new SshException("Error parsing known_hosts file, is your file corrupt? " + this.c.getAbsolutePath(), 17);
          }
          catch (NumberFormatException localNumberFormatException)
          {
            Object localObject3;
            if (!localStringTokenizer.hasMoreTokens())
            {
              onInvalidHostEntry((String)localObject2);
              continue;
            }
            String str3 = (String)localStringTokenizer.nextElement();
            try
            {
              localObject3 = SshPublicKeyFileFactory.decodeSSH2PublicKey(Base64.decode(str3));
              c(str1, (SshPublicKey)localObject3, true);
            }
            catch (IOException localIOException4)
            {
              onInvalidHostEntry((String)localObject2);
            }
            catch (OutOfMemoryError localOutOfMemoryError2)
            {
              throw new SshException("Error parsing known_hosts file, is your file corrupt? " + this.c.getAbsolutePath(), 17);
            }
          }
        }
        ((BufferedReader)localObject1).close();
        localFileInputStream.close();
        this.b = this.c.canWrite();
      }
      else
      {
        localObject1 = new File(this.c.getParent());
        ((File)localObject1).mkdirs();
        localObject2 = new FileOutputStream(this.c);
        ((FileOutputStream)localObject2).write(toString().getBytes());
        ((FileOutputStream)localObject2).close();
        this.b = true;
      }
      this.e = paramString;
    }
    catch (IOException localIOException3)
    {
      this.b = false;
      localIOException2.printStackTrace();
    }
    finally
    {
      if (localFileInputStream != null)
        try
        {
          localFileInputStream.close();
        }
        catch (IOException localIOException5)
        {
        }
    }
  }

  public void setHashHosts(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }

  protected void onInvalidHostEntry(String paramString)
    throws SshException
  {
  }

  public boolean isHostFileWriteable()
  {
    return this.b;
  }

  public abstract void onHostKeyMismatch(String paramString, SshPublicKey paramSshPublicKey1, SshPublicKey paramSshPublicKey2)
    throws SshException;

  public abstract void onUnknownHost(String paramString, SshPublicKey paramSshPublicKey)
    throws SshException;

  public void allowHost(String paramString, SshPublicKey paramSshPublicKey, boolean paramBoolean)
    throws SshException
  {
    if (this.d)
    {
      SshHmac localSshHmac = (SshHmac)ComponentManager.getInstance().supportedHMacsCS().getInstance("hmac-sha1");
      byte[] arrayOfByte1 = new byte[localSshHmac.getMacLength()];
      ComponentManager.getInstance().getRND().nextBytes(arrayOfByte1);
      localSshHmac.init(arrayOfByte1);
      localSshHmac.update(paramString.getBytes());
      byte[] arrayOfByte2 = localSshHmac.doFinal();
      String str = "|1|" + Base64.encodeBytes(arrayOfByte1, false) + "|" + Base64.encodeBytes(arrayOfByte2, false);
      c(str, paramSshPublicKey, paramBoolean);
    }
    else
    {
      c(paramString, paramSshPublicKey, paramBoolean);
    }
    if (paramBoolean)
      try
      {
        saveHostFile();
      }
      catch (IOException localIOException)
      {
        throw new SshException("knownhosts file could not be saved! " + localIOException.getMessage(), 5);
      }
  }

  public Hashtable allowedHosts()
  {
    return this.g;
  }

  public void removeAllowedHost(String paramString)
  {
    if (this.g.containsKey(paramString))
      this.g.remove(paramString);
  }

  public boolean verifyHost(String paramString, SshPublicKey paramSshPublicKey)
    throws SshException
  {
    return b(paramString, paramSshPublicKey, true);
  }

  private boolean b(String paramString, SshPublicKey paramSshPublicKey, boolean paramBoolean)
    throws SshException
  {
    String str1 = null;
    String str2 = null;
    if (System.getProperty("maverick.knownHosts.enableReverseDNS", "true").equalsIgnoreCase("true"))
      try
      {
        InetAddress localInetAddress = InetAddress.getByName(paramString);
        str1 = localInetAddress.getHostName();
        str2 = localInetAddress.getHostAddress();
      }
      catch (UnknownHostException localUnknownHostException)
      {
      }
    Enumeration localEnumeration = this.g.keys();
    String str3;
    StringTokenizer localStringTokenizer;
    String str4;
    while (localEnumeration.hasMoreElements())
    {
      str3 = (String)localEnumeration.nextElement();
      if (str3.startsWith("|1|"))
      {
        if (c(str3, paramString))
          return b(str3, paramSshPublicKey);
        if ((str2 != null) && (c(str3, str2)))
          return b(str3, paramSshPublicKey);
      }
      else if (str3.equals(paramString))
      {
        return b(str3, paramSshPublicKey);
      }
      localStringTokenizer = new StringTokenizer(str3, ",");
      while (localStringTokenizer.hasMoreElements())
      {
        str4 = (String)localStringTokenizer.nextElement();
        if (((str1 != null) && (str4.equals(str1))) || ((str2 != null) && (str4.equals(str2))))
          return b(str4, paramSshPublicKey);
      }
    }
    localEnumeration = this.f.keys();
    while (localEnumeration.hasMoreElements())
    {
      str3 = (String)localEnumeration.nextElement();
      if (str3.startsWith("|1|"))
      {
        if (c(str3, paramString))
          return b(str3, paramSshPublicKey);
        if ((str2 != null) && (c(str3, str2)))
          return b(str3, paramSshPublicKey);
      }
      else if (str3.equals(paramString))
      {
        return b(str3, paramSshPublicKey);
      }
      localStringTokenizer = new StringTokenizer(str3, ",");
      while (localStringTokenizer.hasMoreElements())
      {
        str4 = (String)localStringTokenizer.nextElement();
        if (((str1 != null) && (str4.equals(str1))) || ((str2 != null) && (str4.equals(str2))))
          return b(str4, paramSshPublicKey);
      }
    }
    onUnknownHost(paramString, paramSshPublicKey);
    return b(paramString, paramSshPublicKey, false);
  }

  private boolean c(String paramString1, String paramString2)
    throws SshException
  {
    SshHmac localSshHmac = (SshHmac)ComponentManager.getInstance().supportedHMacsCS().getInstance("hmac-sha1");
    String str1 = paramString1.substring("|1|".length());
    String str2 = str1.substring(0, str1.indexOf("|"));
    String str3 = str1.substring(str1.indexOf("|") + 1);
    byte[] arrayOfByte1 = Base64.decode(str3);
    localSshHmac.init(Base64.decode(str2));
    localSshHmac.update(paramString2.getBytes());
    byte[] arrayOfByte2 = localSshHmac.doFinal();
    return Arrays.equals(arrayOfByte1, arrayOfByte2);
  }

  private boolean b(String paramString, SshPublicKey paramSshPublicKey)
    throws SshException
  {
    SshPublicKey localSshPublicKey = b(paramString, paramSshPublicKey.getAlgorithm());
    if ((localSshPublicKey != null) && (paramSshPublicKey.equals(localSshPublicKey)))
      return true;
    if (localSshPublicKey == null)
      onUnknownHost(paramString, paramSshPublicKey);
    else
      onHostKeyMismatch(paramString, localSshPublicKey, paramSshPublicKey);
    return c(paramString, paramSshPublicKey);
  }

  private boolean c(String paramString, SshPublicKey paramSshPublicKey)
  {
    SshPublicKey localSshPublicKey = b(paramString, paramSshPublicKey.getAlgorithm());
    return (localSshPublicKey != null) && (localSshPublicKey.equals(paramSshPublicKey));
  }

  private SshPublicKey b(String paramString1, String paramString2)
  {
    String str;
    Hashtable localHashtable2;
    try
    {
      Iterator localIterator = this.f.keySet().iterator();
      while (localIterator.hasNext())
      {
        str = (String)localIterator.next();
        if ((str.startsWith("|")) && (c(str, paramString1)))
        {
          localHashtable2 = (Hashtable)this.f.get(str);
          return (SshPublicKey)localHashtable2.get(paramString2);
        }
      }
    }
    catch (SshException localSshException1)
    {
    }
    Object localObject;
    if (this.f.containsKey(paramString1))
    {
      localObject = (Hashtable)this.f.get(paramString1);
      return (SshPublicKey)((Hashtable)localObject).get(paramString2);
    }
    try
    {
      localObject = this.g.keySet().iterator();
      while (((Iterator)localObject).hasNext())
      {
        str = (String)((Iterator)localObject).next();
        if ((str.startsWith("|")) && (c(str, paramString1)))
        {
          localHashtable2 = (Hashtable)this.g.get(str);
          return (SshPublicKey)localHashtable2.get(paramString2);
        }
      }
    }
    catch (SshException localSshException2)
    {
    }
    if (this.g.containsKey(paramString1))
    {
      Hashtable localHashtable1 = (Hashtable)this.g.get(paramString1);
      return (SshPublicKey)localHashtable1.get(paramString2);
    }
    return (SshPublicKey)null;
  }

  private void c(String paramString, SshPublicKey paramSshPublicKey, boolean paramBoolean)
  {
    Hashtable localHashtable;
    if (paramBoolean)
    {
      if (!this.g.containsKey(paramString))
        this.g.put(paramString, new Hashtable());
      localHashtable = (Hashtable)this.g.get(paramString);
      localHashtable.put(paramSshPublicKey.getAlgorithm(), paramSshPublicKey);
    }
    else
    {
      if (!this.f.containsKey(paramString))
        this.f.put(paramString, new Hashtable());
      localHashtable = (Hashtable)this.f.get(paramString);
      localHashtable.put(paramSshPublicKey.getAlgorithm(), paramSshPublicKey);
    }
  }

  public void saveHostFile()
    throws IOException
  {
    if (!this.b)
      throw new IOException("Host file is not writeable.");
    try
    {
      File localFile = new File(this.e);
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
      localFileOutputStream.write(toString().getBytes());
      localFileOutputStream.close();
    }
    catch (IOException localIOException)
    {
      throw new IOException("Could not write to " + this.e);
    }
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("");
    String str1 = System.getProperty("line.separator");
    Enumeration localEnumeration1 = this.g.keys();
    while (localEnumeration1.hasMoreElements())
    {
      String str2 = (String)localEnumeration1.nextElement();
      Hashtable localHashtable = (Hashtable)this.g.get(str2);
      Enumeration localEnumeration2 = localHashtable.keys();
      while (localEnumeration2.hasMoreElements())
      {
        String str3 = (String)localEnumeration2.nextElement();
        SshPublicKey localSshPublicKey = (SshPublicKey)localHashtable.get(str3);
        if (((localSshPublicKey instanceof SshRsaPublicKey)) && (((SshRsaPublicKey)localSshPublicKey).getVersion() == 1))
        {
          SshRsaPublicKey localSshRsaPublicKey = (SshRsaPublicKey)localSshPublicKey;
          localStringBuffer.append(str2 + " " + String.valueOf(localSshRsaPublicKey.getModulus().bitLength()) + " " + localSshRsaPublicKey.getPublicExponent() + " " + localSshRsaPublicKey.getModulus() + str1);
        }
        else
        {
          try
          {
            localStringBuffer.append(str2 + " " + localSshPublicKey.getAlgorithm() + " " + Base64.encodeBytes(localSshPublicKey.getEncoded(), true) + str1);
          }
          catch (SshException localSshException)
          {
          }
        }
      }
    }
    return localStringBuffer.toString();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.AbstractKnownHostsKeyVerification
 * JD-Core Version:    0.6.0
 */