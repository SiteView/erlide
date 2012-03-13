package com.sshtools.publickey;

import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SshPrivateKeyFileFactory
{
  public static final int OPENSSH_FORMAT = 0;
  public static final int SSHTOOLS_FORMAT = 1;
  public static final int SSH1_FORMAT = 3;

  public static SshPrivateKeyFile parse(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      if (I.C(paramArrayOfByte))
        return new I(paramArrayOfByte);
      if (Base64EncodedFileFormat.isFormatted(paramArrayOfByte, H.H, H.J))
        return new H(paramArrayOfByte);
      if (C.A(paramArrayOfByte))
        return new C(paramArrayOfByte);
      if (F.B(paramArrayOfByte))
        return new F(paramArrayOfByte);
      if (G.D(paramArrayOfByte))
        return new G(paramArrayOfByte);
      throw new IOException("A suitable key format could not be found!");
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
    }
    throw new IOException("An error occurred parsing a private key file! Is the file corrupt?");
  }

  public static SshPrivateKeyFile parse(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      int i;
      while ((i = paramInputStream.read()) > -1)
        localByteArrayOutputStream.write(i);
      localSshPrivateKeyFile = parse(localByteArrayOutputStream.toByteArray());
    }
    finally
    {
      try
      {
        SshPrivateKeyFile localSshPrivateKeyFile;
        paramInputStream.close();
      }
      catch (IOException localIOException)
      {
      }
    }
  }

  public static SshPrivateKeyFile create(SshKeyPair paramSshKeyPair, String paramString1, String paramString2, int paramInt)
    throws IOException
  {
    if ((!(paramSshKeyPair.getPrivateKey() instanceof SshRsaPrivateCrtKey)) && (paramInt == 3))
      throw new IOException("SSH1 format requires rsa key pair!");
    switch (paramInt)
    {
    case 0:
      return new I(paramSshKeyPair, paramString1);
    case 1:
      return new H(paramSshKeyPair, paramString1, paramString2);
    case 3:
      return new C(paramSshKeyPair, paramString1, paramString2);
    case 2:
    }
    throw new IOException("Invalid key format!");
  }

  public static void createFile(SshKeyPair paramSshKeyPair, String paramString1, String paramString2, int paramInt, File paramFile)
    throws IOException
  {
    SshPrivateKeyFile localSshPrivateKeyFile = create(paramSshKeyPair, paramString1, paramString2, paramInt);
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    try
    {
      localFileOutputStream.write(localSshPrivateKeyFile.getFormattedKey());
      localFileOutputStream.flush();
    }
    finally
    {
      localFileOutputStream.close();
    }
  }

  public static void convertFile(File paramFile1, String paramString1, String paramString2, int paramInt, File paramFile2)
    throws IOException, InvalidPassphraseException
  {
    SshPrivateKeyFile localSshPrivateKeyFile = parse(new FileInputStream(paramFile1));
    createFile(localSshPrivateKeyFile.toKeyPair(paramString1), paramString1, paramString2, paramInt, paramFile2);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.SshPrivateKeyFileFactory
 * JD-Core Version:    0.6.0
 */