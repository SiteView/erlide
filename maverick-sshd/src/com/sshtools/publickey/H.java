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

class H extends Base64EncodedFileFormat
  implements SshPrivateKeyFile
{
  public static String H = "---- BEGIN SSHTOOLS ENCRYPTED PRIVATE KEY ----";
  public static String J = "---- END SSHTOOLS ENCRYPTED PRIVATE KEY ----";
  private int G = 1391688382;
  byte[] I;

  H(byte[] paramArrayOfByte)
    throws IOException
  {
    super(H, J);
    this.I = getKeyBlob(paramArrayOfByte);
  }

  H(SshKeyPair paramSshKeyPair, String paramString1, String paramString2)
    throws IOException
  {
    super(H, J);
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
      this.I = B(localByteArrayWriter.toByteArray(), paramString1);
    }
    else if ((paramSshKeyPair.getPrivateKey() instanceof SshRsaPrivateKey))
    {
      localObject1 = (SshRsaPrivateKey)paramSshKeyPair.getPrivateKey();
      localObject2 = (SshRsaPublicKey)paramSshKeyPair.getPublicKey();
      localByteArrayWriter.writeString("ssh-rsa");
      localByteArrayWriter.writeBigInteger(((SshRsaPublicKey)localObject2).getPublicExponent());
      localByteArrayWriter.writeBigInteger(((SshRsaPublicKey)localObject2).getModulus());
      localByteArrayWriter.writeBigInteger(((SshRsaPrivateKey)localObject1).getPrivateExponent());
      this.I = B(localByteArrayWriter.toByteArray(), paramString1);
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
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.I);
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

  private byte[] B(byte[] paramArrayOfByte, String paramString)
    throws IOException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter1 = new ByteArrayWriter();
      String str = "none";
      if ((paramString != null) && (!paramString.trim().equals("")))
      {
        str = "3DES-CBC";
        byte[] arrayOfByte1 = B(paramString);
        byte[] arrayOfByte2 = new byte[8];
        ComponentManager.getInstance().getRND().nextBytes(arrayOfByte2);
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
        localSshCipher.init(0, arrayOfByte2, arrayOfByte1);
        ByteArrayWriter localByteArrayWriter2 = new ByteArrayWriter();
        localByteArrayWriter1.writeString(str);
        localByteArrayWriter1.write(arrayOfByte2);
        localByteArrayWriter2.writeInt(this.G);
        localByteArrayWriter2.writeBinaryString(paramArrayOfByte);
        if (localByteArrayWriter2.size() % localSshCipher.getBlockSize() != 0)
        {
          int i = localSshCipher.getBlockSize() - localByteArrayWriter2.size() % localSshCipher.getBlockSize();
          byte[] arrayOfByte4 = new byte[i];
          for (int j = 0; j < i; j++)
            arrayOfByte4[j] = (byte)i;
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

  private byte[] C(String paramString)
    throws IOException, InvalidPassphraseException
  {
    try
    {
      ByteArrayReader localByteArrayReader1 = new ByteArrayReader(this.I);
      String str = localByteArrayReader1.readString();
      byte[] arrayOfByte1;
      if (str.equalsIgnoreCase("3des-cbc"))
      {
        byte[] arrayOfByte2 = B(paramString);
        byte[] arrayOfByte3 = new byte[8];
        if (str.equals("3DES-CBC"))
          localByteArrayReader1.read(arrayOfByte3);
        arrayOfByte1 = localByteArrayReader1.readBinaryString();
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
        localSshCipher.init(1, arrayOfByte3, arrayOfByte2);
        localSshCipher.transform(arrayOfByte1, 0, arrayOfByte1, 0, arrayOfByte1.length);
        ByteArrayReader localByteArrayReader2 = new ByteArrayReader(arrayOfByte1);
        if (localByteArrayReader2.readInt() == this.G)
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
    return formatKey(this.I);
  }

  public SshKeyPair toKeyPair(String paramString)
    throws IOException, InvalidPassphraseException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(C(paramString));
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
    this.I = B(C(paramString1), paramString2);
  }

  private byte[] B(String paramString)
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

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.H
 * JD-Core Version:    0.6.0
 */