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

public class DiffieHellmanGroup14Sha1 extends SshKeyExchangeClient
  implements AbstractKeyExchange
{
  public static final String DIFFIE_HELLMAN_GROUP14_SHA1 = "diffie-hellman-group14-sha1";
  static final BigInteger db = BigInteger.valueOf(1L);
  static final BigInteger ib = BigInteger.valueOf(2L);
  static final BigInteger gb = ib;
  static final BigInteger eb = DiffieHellmanGroups.group14;
  BigInteger jb = null;
  BigInteger hb = null;
  BigInteger nb = null;
  BigInteger mb = null;
  String cb;
  String fb;
  byte[] kb;
  byte[] pb;
  KeyPairGenerator ob;
  KeyAgreement lb;

  public void performClientExchange(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SshException
  {
    this.cb = paramString1;
    this.fb = paramString2;
    this.kb = paramArrayOfByte1;
    this.pb = paramArrayOfByte2;
    try
    {
      this.ob = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyPairGenerator.getInstance("DH") : KeyPairGenerator.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
      this.lb = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyAgreement.getInstance("DH") : KeyAgreement.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new SshException("JCE does not support Diffie Hellman key exchange", 16);
    }
    try
    {
      DHParameterSpec localDHParameterSpec = new DHParameterSpec(eb, gb);
      this.ob.initialize(localDHParameterSpec);
      localObject = this.ob.generateKeyPair();
      this.lb.init(((KeyPair)localObject).getPrivate());
      this.nb = ((DHPrivateKey)((KeyPair)localObject).getPrivate()).getX();
      this.jb = ((DHPublicKey)((KeyPair)localObject).getPublic()).getY();
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new SshException("Failed to generate DH value", 16, localInvalidKeyException);
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new SshException("Failed to generate DH value", 16, localInvalidAlgorithmParameterException);
    }
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(30);
      localByteArrayWriter.writeBigInteger(this.jb);
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
      this.hb = ((ByteArrayReader)localObject).readBigInteger();
      this.signature = ((ByteArrayReader)localObject).readBinaryString();
      this.secret = this.hb.modPow(this.nb, eb);
      calculateExchangeHash();
    }
    catch (IOException localIOException2)
    {
      throw new SshException("Failed to read SSH_MSG_KEXDH_REPLY from message buffer", 5);
    }
  }

  public String getProvider()
  {
    if (this.lb != null)
      return this.lb.getProvider().getName();
    return "";
  }

  protected void calculateExchangeHash()
    throws SshException
  {
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("SHA-1");
    localDigest.putString(this.cb);
    localDigest.putString(this.fb);
    localDigest.putInt(this.kb.length);
    localDigest.putBytes(this.kb);
    localDigest.putInt(this.pb.length);
    localDigest.putBytes(this.pb);
    localDigest.putInt(this.hostKey.length);
    localDigest.putBytes(this.hostKey);
    localDigest.putBigInteger(this.jb);
    localDigest.putBigInteger(this.hb);
    localDigest.putBigInteger(this.secret);
    this.exchangeHash = localDigest.doFinal();
  }

  public String getAlgorithm()
  {
    return "diffie-hellman-group14-sha1";
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
 * Qualified Name:     com.maverick.ssh.components.jce.DiffieHellmanGroup14Sha1
 * JD-Core Version:    0.6.0
 */