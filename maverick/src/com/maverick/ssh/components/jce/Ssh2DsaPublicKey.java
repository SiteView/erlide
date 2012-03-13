package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshKeyFingerprint;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import com.maverick.util.SimpleASNWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;

public class Ssh2DsaPublicKey
  implements SshDsaPublicKey
{
  protected DSAPublicKey pubkey;

  public Ssh2DsaPublicKey()
  {
  }

  public Ssh2DsaPublicKey(DSAPublicKey paramDSAPublicKey)
  {
    this.pubkey = paramDSAPublicKey;
  }

  public Ssh2DsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
    throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    KeyFactory localKeyFactory = JCEProvider.getProviderForAlgorithm("DSA") == null ? KeyFactory.getInstance("DSA") : KeyFactory.getInstance("DSA", JCEProvider.getProviderForAlgorithm("DSA"));
    DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(paramBigInteger4, paramBigInteger1, paramBigInteger2, paramBigInteger3);
    this.pubkey = ((DSAPublicKey)localKeyFactory.generatePublic(localDSAPublicKeySpec));
  }

  public String getAlgorithm()
  {
    return "ssh-dss";
  }

  public int getBitLength()
  {
    return this.pubkey.getParams().getP().bitLength();
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(getAlgorithm());
      localByteArrayWriter.writeBigInteger(this.pubkey.getParams().getP());
      localByteArrayWriter.writeBigInteger(this.pubkey.getParams().getQ());
      localByteArrayWriter.writeBigInteger(this.pubkey.getParams().getG());
      localByteArrayWriter.writeBigInteger(this.pubkey.getY());
      return localByteArrayWriter.toByteArray();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Failed to encoded DSA key", 5, localIOException);
  }

  public String getFingerprint()
    throws SshException
  {
    return SshKeyFingerprint.getFingerprint(getEncoded());
  }

  public void init(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte, paramInt1, paramInt2);
      String str = localByteArrayReader.readString();
      if (!str.equals(getAlgorithm()))
        throw new SshException("The encoded key is not DSA", 5);
      BigInteger localBigInteger1 = localByteArrayReader.readBigInteger();
      BigInteger localBigInteger2 = localByteArrayReader.readBigInteger();
      BigInteger localBigInteger3 = localByteArrayReader.readBigInteger();
      BigInteger localBigInteger4 = localByteArrayReader.readBigInteger();
      DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(localBigInteger4, localBigInteger1, localBigInteger2, localBigInteger3);
      KeyFactory localKeyFactory = JCEProvider.getProviderForAlgorithm("DSA") == null ? KeyFactory.getInstance("DSA") : KeyFactory.getInstance("DSA", JCEProvider.getProviderForAlgorithm("DSA"));
      this.pubkey = ((DSAPublicKey)localKeyFactory.generatePublic(localDSAPublicKeySpec));
    }
    catch (Exception localException)
    {
      throw new SshException("Failed to obtain DSA key instance from JCE", 5, localException);
    }
  }

  public boolean verifySignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SshException
  {
    try
    {
      if (paramArrayOfByte1.length != 40)
      {
        localObject1 = new ByteArrayReader(paramArrayOfByte1);
        localObject2 = ((ByteArrayReader)localObject1).readBinaryString();
        localObject3 = new String(localObject2);
        if (!((String)localObject3).equals("ssh-dss"))
          throw new SshException("The encoded signature is not DSA", 5);
        paramArrayOfByte1 = ((ByteArrayReader)localObject1).readBinaryString();
      }
      Object localObject1 = new ByteArrayOutputStream();
      Object localObject2 = new ByteArrayOutputStream();
      Object localObject3 = new SimpleASNWriter();
      ((SimpleASNWriter)localObject3).writeByte(2);
      if (((paramArrayOfByte1[0] & 0x80) == 128) && (paramArrayOfByte1[0] != 0))
      {
        ((ByteArrayOutputStream)localObject1).write(0);
        ((ByteArrayOutputStream)localObject1).write(paramArrayOfByte1, 0, 20);
      }
      else
      {
        ((ByteArrayOutputStream)localObject1).write(paramArrayOfByte1, 0, 20);
      }
      ((SimpleASNWriter)localObject3).writeData(((ByteArrayOutputStream)localObject1).toByteArray());
      ((SimpleASNWriter)localObject3).writeByte(2);
      if (((paramArrayOfByte1[20] & 0x80) == 128) && (paramArrayOfByte1[20] != 0))
      {
        ((ByteArrayOutputStream)localObject2).write(0);
        ((ByteArrayOutputStream)localObject2).write(paramArrayOfByte1, 20, 20);
      }
      else
      {
        ((ByteArrayOutputStream)localObject2).write(paramArrayOfByte1, 20, 20);
      }
      ((SimpleASNWriter)localObject3).writeData(((ByteArrayOutputStream)localObject2).toByteArray());
      SimpleASNWriter localSimpleASNWriter = new SimpleASNWriter();
      localSimpleASNWriter.writeByte(48);
      localSimpleASNWriter.writeData(((SimpleASNWriter)localObject3).toByteArray());
      byte[] arrayOfByte = localSimpleASNWriter.toByteArray();
      Signature localSignature = JCEProvider.getProviderForAlgorithm("SHA1WithDSA") == null ? Signature.getInstance("SHA1WithDSA") : Signature.getInstance("SHA1WithDSA", JCEProvider.getProviderForAlgorithm("SHA1WithDSA"));
      localSignature.initVerify(this.pubkey);
      localSignature.update(paramArrayOfByte2);
      return localSignature.verify(arrayOfByte);
    }
    catch (Exception localException)
    {
    }
    throw new SshException(16, localException);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof SshDsaPublicKey))
      try
      {
        return ((SshPublicKey)paramObject).getFingerprint().equals(getFingerprint());
      }
      catch (SshException localSshException)
      {
      }
    return false;
  }

  public int hashCode()
  {
    try
    {
      return getFingerprint().hashCode();
    }
    catch (SshException localSshException)
    {
    }
    return 0;
  }

  public BigInteger getG()
  {
    return this.pubkey.getParams().getG();
  }

  public BigInteger getP()
  {
    return this.pubkey.getParams().getP();
  }

  public BigInteger getQ()
  {
    return this.pubkey.getParams().getQ();
  }

  public BigInteger getY()
  {
    return this.pubkey.getY();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh2DsaPublicKey
 * JD-Core Version:    0.6.0
 */