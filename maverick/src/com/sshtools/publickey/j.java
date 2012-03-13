package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import com.maverick.util.SimpleASNReader;
import com.maverick.util.SimpleASNWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Hashtable;

class j
  implements SshPrivateKeyFile
{
  byte[] l;

  j(byte[] paramArrayOfByte)
    throws IOException
  {
    if (!d(paramArrayOfByte))
      throw new IOException("Formatted key data is not a valid OpenSSH key format");
    this.l = paramArrayOfByte;
  }

  j(SshKeyPair paramSshKeyPair, String paramString)
    throws IOException
  {
    this.l = b(paramSshKeyPair, paramString);
  }

  public boolean isPassphraseProtected()
  {
    try
    {
      StringReader localStringReader = new StringReader(new String(this.l, "US-ASCII"));
      e locale = new e(localStringReader);
      return locale.c().containsKey("DEK-Info");
    }
    catch (IOException localIOException)
    {
    }
    return true;
  }

  public String getType()
  {
    return "OpenSSH";
  }

  public boolean supportsPassphraseChange()
  {
    return true;
  }

  public SshKeyPair toKeyPair(String paramString)
    throws IOException, InvalidPassphraseException
  {
    StringReader localStringReader = new StringReader(new String(this.l, "US-ASCII"));
    e locale = new e(localStringReader);
    byte[] arrayOfByte = locale.c(paramString);
    SimpleASNReader localSimpleASNReader = new SimpleASNReader(arrayOfByte);
    try
    {
      if ("DSA PRIVATE KEY".equals(locale.b()))
        return b(localSimpleASNReader);
      if ("RSA PRIVATE KEY".equals(locale.b()))
        return c(localSimpleASNReader);
      throw new IOException("Unsupported type: " + locale.b());
    }
    catch (IOException localIOException)
    {
        throw new InvalidPassphraseException(localIOException);
    }

  }

  SshKeyPair c(SimpleASNReader paramSimpleASNReader)
    throws IOException
  {
    try
    {
      paramSimpleASNReader.assertByte(48);
      paramSimpleASNReader.getLength();
      paramSimpleASNReader.assertByte(2);
      paramSimpleASNReader.getData();
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger1 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger2 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger3 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger4 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger5 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger6 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger7 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger8 = new BigInteger(paramSimpleASNReader.getData());
      SshKeyPair localSshKeyPair = new SshKeyPair();
      localSshKeyPair.setPublicKey(ComponentManager.getInstance().createRsaPublicKey(localBigInteger1, localBigInteger2, 2));
      localSshKeyPair.setPrivateKey(ComponentManager.getInstance().createRsaPrivateCrtKey(localBigInteger1, localBigInteger2, localBigInteger3, localBigInteger4, localBigInteger5, localBigInteger6, localBigInteger7, localBigInteger8));
      return localSshKeyPair;
    }
    catch (SshException localSshException)
    {
        throw new SshIOException(localSshException);
    }

  }

  SshKeyPair b(SimpleASNReader paramSimpleASNReader)
    throws IOException
  {
    try
    {
      paramSimpleASNReader.assertByte(48);
      paramSimpleASNReader.getLength();
      paramSimpleASNReader.assertByte(2);
      paramSimpleASNReader.getData();
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger1 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger2 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger3 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger4 = new BigInteger(paramSimpleASNReader.getData());
      paramSimpleASNReader.assertByte(2);
      BigInteger localBigInteger5 = new BigInteger(paramSimpleASNReader.getData());
      SshKeyPair localSshKeyPair = new SshKeyPair();
      SshDsaPublicKey localSshDsaPublicKey = ComponentManager.getInstance().createDsaPublicKey(localBigInteger1, localBigInteger2, localBigInteger3, localBigInteger4);
      localSshKeyPair.setPublicKey(localSshDsaPublicKey);
      localSshKeyPair.setPrivateKey(ComponentManager.getInstance().createDsaPrivateKey(localBigInteger1, localBigInteger2, localBigInteger3, localBigInteger5, localSshDsaPublicKey.getY()));
      return localSshKeyPair;
    }
    catch (SshException localSshException)
    {
        throw new SshIOException(localSshException);
    }

  }

  void b(SimpleASNWriter paramSimpleASNWriter, SshDsaPrivateKey paramSshDsaPrivateKey, SshDsaPublicKey paramSshDsaPublicKey)
  {
    SimpleASNWriter localSimpleASNWriter = new SimpleASNWriter();
    localSimpleASNWriter.writeByte(2);
    byte[] arrayOfByte1 = new byte[1];
    localSimpleASNWriter.writeData(arrayOfByte1);
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshDsaPublicKey.getP().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshDsaPublicKey.getQ().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshDsaPublicKey.getG().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshDsaPublicKey.getY().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshDsaPrivateKey.getX().toByteArray());
    byte[] arrayOfByte2 = localSimpleASNWriter.toByteArray();
    paramSimpleASNWriter.writeByte(48);
    paramSimpleASNWriter.writeData(arrayOfByte2);
  }

  void b(SimpleASNWriter paramSimpleASNWriter, SshRsaPrivateCrtKey paramSshRsaPrivateCrtKey)
  {
    SimpleASNWriter localSimpleASNWriter = new SimpleASNWriter();
    localSimpleASNWriter.writeByte(2);
    byte[] arrayOfByte1 = new byte[1];
    localSimpleASNWriter.writeData(arrayOfByte1);
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getModulus().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getPublicExponent().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getPrivateExponent().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getPrimeP().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getPrimeQ().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getPrimeExponentP().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getPrimeExponentQ().toByteArray());
    localSimpleASNWriter.writeByte(2);
    localSimpleASNWriter.writeData(paramSshRsaPrivateCrtKey.getCrtCoefficient().toByteArray());
    byte[] arrayOfByte2 = localSimpleASNWriter.toByteArray();
    paramSimpleASNWriter.writeByte(48);
    paramSimpleASNWriter.writeData(arrayOfByte2);
  }

  public byte[] b(SshKeyPair paramSshKeyPair, String paramString)
    throws IOException
  {
    c localc = new c();
    SimpleASNWriter localSimpleASNWriter = new SimpleASNWriter();
    byte[] arrayOfByte;
    if ((paramSshKeyPair.getPublicKey() instanceof SshDsaPublicKey))
    {
      b(localSimpleASNWriter, (SshDsaPrivateKey)paramSshKeyPair.getPrivateKey(), (SshDsaPublicKey)paramSshKeyPair.getPublicKey());
      arrayOfByte = localSimpleASNWriter.toByteArray();
      localc.b("DSA PRIVATE KEY");
    }
    else if ((paramSshKeyPair.getPrivateKey() instanceof SshRsaPrivateCrtKey))
    {
      b(localSimpleASNWriter, (SshRsaPrivateCrtKey)paramSshKeyPair.getPrivateKey());
      arrayOfByte = localSimpleASNWriter.toByteArray();
      localc.b("RSA PRIVATE KEY");
    }
    else
    {
      throw new IOException(paramSshKeyPair.getPublicKey().getAlgorithm() + " is not supported");
    }
    localc.b(arrayOfByte, paramString);
    StringWriter localStringWriter = new StringWriter();
    localc.b(localStringWriter);
    return localStringWriter.toString().getBytes("US-ASCII");
  }

  public void changePassphrase(String paramString1, String paramString2)
    throws IOException, InvalidPassphraseException
  {
    SshKeyPair localSshKeyPair = toKeyPair(paramString1);
    this.l = b(localSshKeyPair, paramString2);
  }

  public byte[] getFormattedKey()
  {
    return this.l;
  }

  public static boolean d(byte[] paramArrayOfByte)
  {
    try
    {
      StringReader localStringReader = new StringReader(new String(paramArrayOfByte, "US-ASCII"));
      new e(localStringReader);
      return true;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.j
 * JD-Core Version:    0.6.0
 */