package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.DiffieHellmanGroups;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshKeyExchangeClient;
import com.maverick.ssh2.TransportProtocol;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class DiffieHellmanGroup1Sha1 extends SshKeyExchangeClient
  implements AbstractKeyExchange
{
  public static final String DIFFIE_HELLMAN_GROUP1_SHA1 = "diffie-hellman-group1-sha1";
  static final BigInteger p = BigInteger.valueOf(1L);
  static final BigInteger u = BigInteger.valueOf(2L);
  static final BigInteger s = u;
  static final BigInteger q = DiffieHellmanGroups.group1;
  BigInteger v = null;
  BigInteger t = null;
  BigInteger z = null;
  BigInteger y = null;
  String o;
  String r;
  byte[] w;
  byte[] bb;
  KeyPairGenerator ab;
  KeyAgreement x;

  public String getAlgorithm()
  {
    return "diffie-hellman-group1-sha1";
  }

  public String getProvider()
  {
    if (this.x != null)
      return this.x.getProvider().getName();
    return "";
  }

  public void performClientExchange(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SshException
  {
    this.o = paramString1;
    this.r = paramString2;
    this.w = paramArrayOfByte1;
    this.bb = paramArrayOfByte2;
    try
    {
      this.ab = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyPairGenerator.getInstance("DH") : KeyPairGenerator.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
      this.x = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyAgreement.getInstance("DH") : KeyAgreement.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new SshException("JCE does not support Diffie Hellman key exchange", 16);
    }
    try
    {
      DHParameterSpec localDHParameterSpec = new DHParameterSpec(q, s);
      this.ab.initialize(localDHParameterSpec);
      localObject = this.ab.generateKeyPair();
      this.x.init(((KeyPair)localObject).getPrivate());
      this.z = ((DHPrivateKey)((KeyPair)localObject).getPrivate()).getX();
      this.v = ((DHPublicKey)((KeyPair)localObject).getPublic()).getY();
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new SshException("Failed to generate DH value", 16);
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new SshException("Failed to generate DH value", 16);
    }
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(30);
      localByteArrayWriter.writeBigInteger(this.v);
      this.transport.sendMessage(localByteArrayWriter.toByteArray(), true);
    }
    catch (IOException localIOException1)
    {
      throw new SshException("Failed to write SSH_MSG_KEXDH_INIT to message buffer", 5);
    }
    byte[] arrayOfByte = this.transport.nextMessage();
    if (arrayOfByte[0] != 31)
    {
      this.transport.disconnect(3, "Key exchange failed");
      throw new SshException("Key exchange failed", 5);
    }
    Object localObject = new ByteArrayReader(arrayOfByte, 1, arrayOfByte.length - 1);
    try
    {
      this.hostKey = ((ByteArrayReader)localObject).readBinaryString();
      this.t = ((ByteArrayReader)localObject).readBigInteger();
      this.signature = ((ByteArrayReader)localObject).readBinaryString();
      this.secret = this.t.modPow(this.z, q);
      calculateExchangeHash();
    }
    catch (IOException localIOException2)
    {
      throw new SshException("Failed to read SSH_MSG_KEXDH_REPLY from message buffer", 5);
    }
  }

  protected void calculateExchangeHash()
    throws SshException
  {
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("SHA-1");
    localDigest.putString(this.o);
    localDigest.putString(this.r);
    localDigest.putInt(this.w.length);
    localDigest.putBytes(this.w);
    localDigest.putInt(this.bb.length);
    localDigest.putBytes(this.bb);
    localDigest.putInt(this.hostKey.length);
    localDigest.putBytes(this.hostKey);
    localDigest.putBigInteger(this.v);
    localDigest.putBigInteger(this.t);
    localDigest.putBigInteger(this.secret);
    this.exchangeHash = localDigest.doFinal();
  }

  public boolean isKeyExchangeMessage(int paramInt)
  {
    switch (paramInt)
    {
    case 30:
    case 31:
      return true;
    }
    return false;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.DiffieHellmanGroup1Sha1
 * JD-Core Version:    0.6.0
 */