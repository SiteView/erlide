package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.util.ByteArrayReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SshPublicKeyFileFactory
{
  public static final int OPENSSH_FORMAT = 0;
  public static final int SECSH_FORMAT = 1;
  public static final int SSH1_FORMAT = 2;

  public static SshPublicKey decodeSSH2PublicKey(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
      String str = localByteArrayReader.readString();
      try
      {
        SshPublicKey localSshPublicKey = (SshPublicKey)ComponentManager.getInstance().supportedPublicKeys().getInstance(str);
        localSshPublicKey.init(paramArrayOfByte, 0, paramArrayOfByte.length);
        return localSshPublicKey;
      }
      catch (SshException localSshException)
      {
        throw new SshIOException(localSshException);
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
    }
    throw new IOException("An error occurred parsing a public key file! Is the file corrupt?");
  }

  public static SshPublicKey decodeSSH2PublicKey(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      SshPublicKey localSshPublicKey = (SshPublicKey)ComponentManager.getInstance().supportedPublicKeys().getInstance(paramString);
      localSshPublicKey.init(paramArrayOfByte, 0, paramArrayOfByte.length);
      return localSshPublicKey;
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  public static SshPublicKeyFile parse(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      return new OpenSSHPublicKeyFile(paramArrayOfByte);
    }
    catch (IOException localIOException1)
    {
      try
      {
        return new SECSHPublicKeyFile(paramArrayOfByte);
      }
      catch (IOException localIOException2)
      {
        try
        {
          return new E(paramArrayOfByte);
        }
        catch (Exception localException)
        {
          throw new IOException("Unable to parse key, format could not be identified");
        }
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
    }
    throw new IOException("An error occurred parsing a public key file! Is the file corrupt?");
  }

  public static SshPublicKeyFile parse(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      int i;
      while ((i = paramInputStream.read()) > -1)
        localByteArrayOutputStream.write(i);
      localSshPublicKeyFile = parse(localByteArrayOutputStream.toByteArray());
    }
    finally
    {
      try
      {
        SshPublicKeyFile localSshPublicKeyFile;
        paramInputStream.close();
      }
      catch (IOException localIOException)
      {
      }
    }
  }

  public static SshPublicKeyFile create(SshPublicKey paramSshPublicKey, String paramString, int paramInt)
    throws IOException
  {
    switch (paramInt)
    {
    case 0:
      return new OpenSSHPublicKeyFile(paramSshPublicKey, paramString);
    case 1:
      return new SECSHPublicKeyFile(paramSshPublicKey, paramString);
    case 2:
      return new E(paramSshPublicKey);
    }
    throw new IOException("Invalid format type specified!");
  }

  public static void createFile(SshPublicKey paramSshPublicKey, String paramString, int paramInt, File paramFile)
    throws IOException
  {
    SshPublicKeyFile localSshPublicKeyFile = create(paramSshPublicKey, paramString, paramInt);
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    try
    {
      localFileOutputStream.write(localSshPublicKeyFile.getFormattedKey());
      localFileOutputStream.flush();
    }
    finally
    {
      localFileOutputStream.close();
    }
  }

  public static void convertFile(File paramFile1, int paramInt, File paramFile2)
    throws IOException
  {
    SshPublicKeyFile localSshPublicKeyFile = parse(new FileInputStream(paramFile1));
    createFile(localSshPublicKeyFile.toPublicKey(), localSshPublicKeyFile.getComment(), paramInt, paramFile2);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.SshPublicKeyFileFactory
 * JD-Core Version:    0.6.0
 */