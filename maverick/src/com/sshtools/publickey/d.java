package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;

class d
  implements SshPrivateKeyFile
{
  String f;
  byte[] e;

  d(byte[] paramArrayOfByte)
    throws IOException
  {
    if (b(paramArrayOfByte))
      this.e = paramArrayOfByte;
    else
      throw new IOException("SSH1 RSA Key required");
  }

  d(SshKeyPair paramSshKeyPair, String paramString1, String paramString2)
    throws IOException
  {
    this.e = b(paramSshKeyPair, paramString1, paramString2);
  }

  public boolean supportsPassphraseChange()
  {
    return true;
  }

  public String getType()
  {
    return "SSH1";
  }

  public boolean isPassphraseProtected()
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.e);
      byte[] arrayOfByte = new byte["SSH PRIVATE KEY FILE FORMAT 1.1\n".length()];
      localByteArrayReader.read(arrayOfByte);
      String str = new String(arrayOfByte);
      localByteArrayReader.read();
      if (!str.equals("SSH PRIVATE KEY FILE FORMAT 1.1\n"))
        return false;
      int i = localByteArrayReader.read();
      return i != 0;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public SshKeyPair toKeyPair(String paramString)
    throws IOException, InvalidPassphraseException
  {
    return b(this.e, paramString);
  }

  public static boolean b(byte[] paramArrayOfByte)
  {
    String str = new String(paramArrayOfByte);
    return str.startsWith("SSH PRIVATE KEY FILE FORMAT 1.1\n".trim());
  }

  public SshKeyPair b(byte[] paramArrayOfByte, String paramString)
    throws IOException, InvalidPassphraseException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
      byte[] arrayOfByte1 = new byte["SSH PRIVATE KEY FILE FORMAT 1.1\n".length()];
      localByteArrayReader.read(arrayOfByte1);
      String str = new String(arrayOfByte1);
      localByteArrayReader.read();
      if (!str.equals("SSH PRIVATE KEY FILE FORMAT 1.1\n"))
        throw new IOException("RSA key file corrupt");
      int i = localByteArrayReader.read();
      if ((i != 3) && (i != 0))
        throw new IOException("Private key cipher type is not supported!");
      localByteArrayReader.readInt();
      localByteArrayReader.readInt();
      BigInteger localBigInteger1 = localByteArrayReader.readMPINT();
      BigInteger localBigInteger2 = localByteArrayReader.readMPINT();
      SshRsaPublicKey localSshRsaPublicKey = ComponentManager.getInstance().createRsaPublicKey(localBigInteger1, localBigInteger2, 1);
      this.f = localByteArrayReader.readString();
      byte[] arrayOfByte2 = new byte[8192];
      int j = localByteArrayReader.read(arrayOfByte2);
      byte[] arrayOfByte3 = new byte[j];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, j);
      if (i == 3)
      {
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh1CiphersCS().getInstance("3");
        byte[] arrayOfByte4 = new byte[localSshCipher.getBlockSize()];
        localSshCipher.init(1, arrayOfByte4, b(paramString));
        localSshCipher.transform(arrayOfByte3, 0, arrayOfByte3, 0, arrayOfByte3.length);
      }
      localByteArrayReader = new ByteArrayReader(arrayOfByte3);
      int k = (byte)localByteArrayReader.read();
      int m = (byte)localByteArrayReader.read();
      int n = (byte)localByteArrayReader.read();
      int i1 = (byte)localByteArrayReader.read();
      if ((k != n) || (m != i1))
        throw new InvalidPassphraseException();
      BigInteger localBigInteger3 = localByteArrayReader.readMPINT();
      BigInteger localBigInteger4 = localByteArrayReader.readMPINT();
      BigInteger localBigInteger5 = localByteArrayReader.readMPINT();
      BigInteger localBigInteger6 = localByteArrayReader.readMPINT();
      SshKeyPair localSshKeyPair = new SshKeyPair();
      localSshKeyPair.setPrivateKey(ComponentManager.getInstance().createRsaPrivateCrtKey(localSshRsaPublicKey.getModulus(), localSshRsaPublicKey.getPublicExponent(), localBigInteger3, localBigInteger5, localBigInteger6, localBigInteger4));
      localSshKeyPair.setPublicKey(localSshRsaPublicKey);
      return localSshKeyPair;
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  public byte[] b(SshKeyPair paramSshKeyPair, String paramString1, String paramString2)
    throws IOException
  {
    try
    {
      if ((paramSshKeyPair.getPrivateKey() instanceof SshRsaPrivateCrtKey))
      {
        SshRsaPrivateCrtKey localSshRsaPrivateCrtKey = (SshRsaPrivateCrtKey)paramSshKeyPair.getPrivateKey();
        ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(4096);
        byte[] arrayOfByte1 = new byte[2];
        SshSecureRandomGenerator localSshSecureRandomGenerator = ComponentManager.getInstance().getRND();
        localSshSecureRandomGenerator.nextBytes(arrayOfByte1);
        localByteArrayWriter.write(arrayOfByte1[0]);
        localByteArrayWriter.write(arrayOfByte1[1]);
        localByteArrayWriter.write(arrayOfByte1[0]);
        localByteArrayWriter.write(arrayOfByte1[1]);
        localByteArrayWriter.writeMPINT(localSshRsaPrivateCrtKey.getPrivateExponent());
        localByteArrayWriter.writeMPINT(localSshRsaPrivateCrtKey.getCrtCoefficient());
        localByteArrayWriter.writeMPINT(localSshRsaPrivateCrtKey.getPrimeP());
        localByteArrayWriter.writeMPINT(localSshRsaPrivateCrtKey.getPrimeQ());
        byte[] arrayOfByte2 = localByteArrayWriter.toByteArray();
        arrayOfByte1 = new byte[8 - arrayOfByte2.length % 8 + arrayOfByte2.length];
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, arrayOfByte2.length);
        arrayOfByte2 = arrayOfByte1;
        int i = 3;
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh1CiphersCS().getInstance("3");
        byte[] arrayOfByte3 = new byte[localSshCipher.getBlockSize()];
        localSshCipher.init(0, arrayOfByte3, b(paramString1));
        localSshCipher.transform(arrayOfByte2, 0, arrayOfByte2, 0, arrayOfByte2.length);
        localByteArrayWriter.reset();
        localByteArrayWriter.write("SSH PRIVATE KEY FILE FORMAT 1.1\n".getBytes());
        localByteArrayWriter.write(0);
        localByteArrayWriter.write(i);
        localByteArrayWriter.writeInt(0);
        localByteArrayWriter.writeInt(0);
        localByteArrayWriter.writeMPINT(localSshRsaPrivateCrtKey.getModulus());
        localByteArrayWriter.writeMPINT(localSshRsaPrivateCrtKey.getPublicExponent());
        localByteArrayWriter.writeString(paramString2);
        localByteArrayWriter.write(arrayOfByte2, 0, arrayOfByte2.length);
        return localByteArrayWriter.toByteArray();
      }
      throw new IOException("RSA Private key required!");
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  public void changePassphrase(String paramString1, String paramString2)
    throws IOException, InvalidPassphraseException
  {
    this.e = b(b(this.e, paramString1), paramString2, this.f);
  }

  public byte[] getFormattedKey()
  {
    return this.e;
  }

  private byte[] b(String paramString)
    throws SshException
  {
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("MD5");
    byte[] arrayOfByte1 = new byte[32];
    localDigest.putBytes(paramString.getBytes());
    byte[] arrayOfByte2 = localDigest.doFinal();
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 16);
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 16, 16);
    return arrayOfByte1;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.d
 * JD-Core Version:    0.6.0
 */