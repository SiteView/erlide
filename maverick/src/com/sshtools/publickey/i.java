package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshRsaPrivateKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;

class i extends Base64EncodedFileFormat
  implements SshPrivateKeyFile
{
  public static String i = "---- BEGIN SSHTOOLS ENCRYPTED PRIVATE KEY ----";
  public static String k = "---- END SSHTOOLS ENCRYPTED PRIVATE KEY ----";
  private int h = 1391688382;
  byte[] j;

  i(byte[] paramArrayOfByte)
    throws IOException
  {
    super(i, k);
    this.j = getKeyBlob(paramArrayOfByte);
  }

  i(SshKeyPair paramSshKeyPair, String paramString1, String paramString2)
    throws IOException
  {
    super(i, k);
    setHeaderValue("Comment", paramString2);
    ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
    Object localObject1;
    Object localObject2;
    if ((paramSshKeyPair.getPrivateKey() instanceof SshDsaPrivateKey))
    {
      localObject1 = (SshDsaPrivateKey)paramSshKeyPair.getPrivateKey();
      localObject2 = ((SshDsaPrivateKey)localObject1).getPublicKey();
      localByteArrayWriter.writeString("ssh-dss");
      localByteArrayWriter.writeBigInteger(((SshDsaPublicKey)localObject2).getP());
      localByteArrayWriter.writeBigInteger(((SshDsaPublicKey)localObject2).getQ());
      localByteArrayWriter.writeBigInteger(((SshDsaPublicKey)localObject2).getG());
      localByteArrayWriter.writeBigInteger(((SshDsaPrivateKey)localObject1).getX());
      this.j = c(localByteArrayWriter.toByteArray(), paramString1);
    }
    else if ((paramSshKeyPair.getPrivateKey() instanceof SshRsaPrivateKey))
    {
      localObject1 = (SshRsaPrivateKey)paramSshKeyPair.getPrivateKey();
      localObject2 = (SshRsaPublicKey)paramSshKeyPair.getPublicKey();
      localByteArrayWriter.writeString("ssh-rsa");
      localByteArrayWriter.writeBigInteger(((SshRsaPublicKey)localObject2).getPublicExponent());
      localByteArrayWriter.writeBigInteger(((SshRsaPublicKey)localObject2).getModulus());
      localByteArrayWriter.writeBigInteger(((SshRsaPrivateKey)localObject1).getPrivateExponent());
      this.j = c(localByteArrayWriter.toByteArray(), paramString1);
    }
    else
    {
      throw new IOException("Unsupported private key type!");
    }
  }

  public String getType()
  {
    return "SSHTools";
  }

  public boolean supportsPassphraseChange()
  {
    return true;
  }

  public boolean isPassphraseProtected()
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.j);
      String str = localByteArrayReader.readString();
      if (str.equals("none"))
        return false;
      if (str.equalsIgnoreCase("3des-cbc"))
        return true;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  private byte[] c(byte[] paramArrayOfByte, String paramString)
    throws IOException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter1 = new ByteArrayWriter();
      String str = "none";
      if ((paramString != null) && (!paramString.trim().equals("")))
      {
        str = "3DES-CBC";
        byte[] arrayOfByte1 = c(paramString);
        byte[] arrayOfByte2 = new byte[8];
        ComponentManager.getInstance().getRND().nextBytes(arrayOfByte2);
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
        localSshCipher.init(0, arrayOfByte2, arrayOfByte1);
        ByteArrayWriter localByteArrayWriter2 = new ByteArrayWriter();
        localByteArrayWriter1.writeString(str);
        localByteArrayWriter1.write(arrayOfByte2);
        localByteArrayWriter2.writeInt(this.h);
        localByteArrayWriter2.writeBinaryString(paramArrayOfByte);
        if (localByteArrayWriter2.size() % localSshCipher.getBlockSize() != 0)
        {
          int m = localSshCipher.getBlockSize() - localByteArrayWriter2.size() % localSshCipher.getBlockSize();
          byte[] arrayOfByte4 = new byte[m];
          for (int n = 0; n < m; n++)
            arrayOfByte4[n] = (byte)m;
          localByteArrayWriter2.write(arrayOfByte4);
        }
        byte[] arrayOfByte3 = localByteArrayWriter2.toByteArray();
        localSshCipher.transform(arrayOfByte3, 0, arrayOfByte3, 0, arrayOfByte3.length);
        localByteArrayWriter1.writeBinaryString(arrayOfByte3);
        return localByteArrayWriter1.toByteArray();
      }
      localByteArrayWriter1.writeString(str);
      localByteArrayWriter1.writeBinaryString(paramArrayOfByte);
      return localByteArrayWriter1.toByteArray();
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  private byte[] d(String paramString)
    throws IOException, InvalidPassphraseException
  {
    try
    {
      ByteArrayReader localByteArrayReader1 = new ByteArrayReader(this.j);
      String str = localByteArrayReader1.readString();
      byte[] arrayOfByte1;
      if (str.equalsIgnoreCase("3des-cbc"))
      {
        byte[] arrayOfByte2 = c(paramString);
        byte[] arrayOfByte3 = new byte[8];
        if (str.equals("3DES-CBC"))
          localByteArrayReader1.read(arrayOfByte3);
        arrayOfByte1 = localByteArrayReader1.readBinaryString();
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
        localSshCipher.init(1, arrayOfByte3, arrayOfByte2);
        localSshCipher.transform(arrayOfByte1, 0, arrayOfByte1, 0, arrayOfByte1.length);
        ByteArrayReader localByteArrayReader2 = new ByteArrayReader(arrayOfByte1);
        if (localByteArrayReader2.readInt() == this.h)
          arrayOfByte1 = localByteArrayReader2.readBinaryString();
        else
          throw new InvalidPassphraseException();
      }
      else
      {
        arrayOfByte1 = localByteArrayReader1.readBinaryString();
      }
      return arrayOfByte1;
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  public byte[] getFormattedKey()
    throws IOException
  {
    return formatKey(this.j);
  }

  public SshKeyPair toKeyPair(String paramString)
    throws IOException, InvalidPassphraseException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(d(paramString));
      String str = localByteArrayReader.readString();
      BigInteger localBigInteger1;
      BigInteger localBigInteger2;
      BigInteger localBigInteger3;
      Object localObject;
      if (str.equals("ssh-dss"))
      {
        localBigInteger1 = localByteArrayReader.readBigInteger();
        localBigInteger2 = localByteArrayReader.readBigInteger();
        localBigInteger3 = localByteArrayReader.readBigInteger();
        localObject = localByteArrayReader.readBigInteger();
        SshDsaPrivateKey localSshDsaPrivateKey = ComponentManager.getInstance().createDsaPrivateKey(localBigInteger1, localBigInteger2, localBigInteger3, (BigInteger)localObject, localBigInteger3.modPow((BigInteger)localObject, localBigInteger1));
        SshKeyPair localSshKeyPair = new SshKeyPair();
        localSshKeyPair.setPublicKey(localSshDsaPrivateKey.getPublicKey());
        localSshKeyPair.setPrivateKey(ComponentManager.getInstance().createDsaPrivateKey(localBigInteger1, localBigInteger2, localBigInteger3, (BigInteger)localObject, localBigInteger3.modPow((BigInteger)localObject, localBigInteger1)));
        return localSshKeyPair;
      }
      if (str.equals("ssh-rsa"))
      {
        localBigInteger1 = localByteArrayReader.readBigInteger();
        localBigInteger2 = localByteArrayReader.readBigInteger();
        localBigInteger3 = localByteArrayReader.readBigInteger();
        localObject = new SshKeyPair();
        ((SshKeyPair)localObject).setPublicKey(ComponentManager.getInstance().createRsaPublicKey(localBigInteger2, localBigInteger1, 2));
        ((SshKeyPair)localObject).setPrivateKey(ComponentManager.getInstance().createRsaPrivateKey(localBigInteger2, localBigInteger3));
        return localObject;
      }
      throw new IOException("Unsupported private key algorithm type " + str);
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  public void changePassphrase(String paramString1, String paramString2)
    throws IOException, InvalidPassphraseException
  {
    this.j = c(d(paramString1), paramString2);
  }

  private byte[] c(String paramString)
    throws SshException
  {
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("MD5");
    localDigest.putBytes(paramString.getBytes());
    byte[] arrayOfByte1 = localDigest.doFinal();
    localDigest.reset();
    localDigest.putBytes(paramString.getBytes());
    localDigest.putBytes(arrayOfByte1);
    byte[] arrayOfByte2 = localDigest.doFinal();
    byte[] arrayOfByte3 = new byte[32];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, 16);
    System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 16, 16);
    return arrayOfByte3;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.i
 * JD-Core Version:    0.6.0
 */