package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;

class h extends Base64EncodedFileFormat
  implements SshPrivateKeyFile
{
  static String m = "---- BEGIN SSH2 ENCRYPTED PRIVATE KEY ----";
  static String o = "---- END SSH2 ENCRYPTED PRIVATE KEY ----";
  byte[] n;

  h(byte[] paramArrayOfByte)
    throws IOException
  {
    super(m, o);
    if (!e(paramArrayOfByte))
      throw new IOException("Key is not formatted in the ssh.com format");
    this.n = paramArrayOfByte;
  }

  public String getType()
  {
    return "SSH Communications Security";
  }

  public static boolean e(byte[] paramArrayOfByte)
  {
    return isFormatted(paramArrayOfByte, m, o);
  }

  public boolean supportsPassphraseChange()
  {
    return false;
  }

  public boolean isPassphraseProtected()
  {
    try
    {
      byte[] arrayOfByte = getKeyBlob(this.n);
      ByteArrayReader localByteArrayReader = new ByteArrayReader(arrayOfByte);
      long l = localByteArrayReader.readInt();
      if (l != 1064303083L)
        throw new IOException("Invalid ssh.com key! Magic number not found");
      localByteArrayReader.readInt();
      localByteArrayReader.readString();
      String str = localByteArrayReader.readString();
      return str.equals("3des-cbc");
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public SshKeyPair toKeyPair(String paramString)
    throws IOException, InvalidPassphraseException
  {
    byte[] arrayOfByte1 = getKeyBlob(this.n);
    int i = 0;
    ByteArrayReader localByteArrayReader1 = new ByteArrayReader(arrayOfByte1);
    long l1 = localByteArrayReader1.readInt();
    if (l1 != 1064303083L)
      throw new IOException("Invalid ssh.com key! Magic number not found");
    localByteArrayReader1.readInt();
    String str1 = localByteArrayReader1.readString();
    String str2 = localByteArrayReader1.readString();
    byte[] arrayOfByte2 = localByteArrayReader1.readBinaryString();
    Object localObject1;
    Object localObject2;
    try
    {
      if (!str2.equals("none"))
      {
        if (!str2.equals("3des-cbc"))
          throw new IOException("Unsupported cipher type " + str2 + " in ssh.com private key");
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
        localObject1 = new byte[32];
        localObject2 = e(paramString);
        localSshCipher.init(1, localObject1, localObject2);
        localSshCipher.transform(arrayOfByte2);
        i = 1;
      }
    }
    catch (SshException localSshException)
    {
      throw new SshIOException(localSshException);
    }
    try
    {
      ByteArrayReader localByteArrayReader2 = new ByteArrayReader(arrayOfByte2, 4, arrayOfByte2.length - 4);
      BigInteger localBigInteger1;
      BigInteger localBigInteger2;
      BigInteger localBigInteger3;
      BigInteger localBigInteger4;
      Object localObject3;
      if (str1.startsWith("if-modn{sign{rsa"))
      {
        localObject1 = localByteArrayReader2.readMPINT32();
        localObject2 = localByteArrayReader2.readMPINT32();
        localBigInteger1 = localByteArrayReader2.readMPINT32();
        localBigInteger2 = localByteArrayReader2.readMPINT32();
        localBigInteger3 = localByteArrayReader2.readMPINT32();
        localBigInteger4 = localByteArrayReader2.readMPINT32();
        localObject3 = new SshKeyPair();
        ((SshKeyPair)localObject3).setPublicKey(ComponentManager.getInstance().createRsaPublicKey(localBigInteger1, (BigInteger)localObject1, 2));
        ((SshKeyPair)localObject3).setPrivateKey(ComponentManager.getInstance().createRsaPrivateCrtKey(localBigInteger1, (BigInteger)localObject1, (BigInteger)localObject2, localBigInteger3, localBigInteger4, localBigInteger2));
        return localObject3;
      }
      if (str1.startsWith("dl-modp{sign{dsa"))
      {
        long l2 = localByteArrayReader2.readInt();
        if (l2 != 0L)
          throw new IOException("Unexpected value in DSA key; this is an unsupported feature of ssh.com private keys");
        localBigInteger1 = localByteArrayReader2.readMPINT32();
        localBigInteger2 = localByteArrayReader2.readMPINT32();
        localBigInteger3 = localByteArrayReader2.readMPINT32();
        localBigInteger4 = localByteArrayReader2.readMPINT32();
        localObject3 = localByteArrayReader2.readMPINT32();
        SshKeyPair localSshKeyPair = new SshKeyPair();
        SshDsaPublicKey localSshDsaPublicKey = ComponentManager.getInstance().createDsaPublicKey(localBigInteger1, localBigInteger3, localBigInteger2, localBigInteger4);
        localSshKeyPair.setPublicKey(localSshDsaPublicKey);
        localSshKeyPair.setPrivateKey(ComponentManager.getInstance().createDsaPrivateKey(localBigInteger1, localBigInteger3, localBigInteger2, (BigInteger)localObject3, localSshDsaPublicKey.getY()));
        return localSshKeyPair;
      }
      throw new IOException("Unsupported ssh.com key type " + str1);
    }
    catch (Throwable localThrowable)
    {
      if (i != 0)
        throw new InvalidPassphraseException();
    }
    throw new IOException("Bad SSH.com private key format!");
  }

  private byte[] e(String paramString)
    throws IOException
  {
    try
    {
      Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("MD5");
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localDigest.putBytes(paramString.getBytes());
      byte[] arrayOfByte = localDigest.doFinal();
      localDigest.reset();
      localDigest.putBytes(paramString.getBytes());
      localDigest.putBytes(arrayOfByte);
      localByteArrayWriter.write(arrayOfByte);
      localByteArrayWriter.write(localDigest.doFinal());
      return localByteArrayWriter.toByteArray();
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  public void changePassphrase(String paramString1, String paramString2)
    throws IOException
  {
    throw new IOException("Changing passphrase is not supported by the ssh.com key format engine");
  }

  public byte[] getFormattedKey()
    throws IOException
  {
    return this.n;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.h
 * JD-Core Version:    0.6.0
 */