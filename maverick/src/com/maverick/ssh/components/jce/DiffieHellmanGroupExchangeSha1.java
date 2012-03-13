package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
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

public class DiffieHellmanGroupExchangeSha1 extends SshKeyExchangeClient
  implements AbstractKeyExchange
{
  public static final String DIFFIE_HELLMAN_GROUP_EXCHANGE_SHA1 = "diffie-hellman-group-exchange-sha1";
  BigInteger f;
  BigInteger d;
  static BigInteger c = BigInteger.valueOf(1L);
  BigInteger h = null;
  BigInteger g = null;
  BigInteger l = null;
  BigInteger k = null;
  String b;
  String e;
  byte[] i;
  byte[] n;
  KeyPairGenerator m;
  KeyAgreement j;

  public boolean isKeyExchangeMessage(int paramInt)
  {
    switch (paramInt)
    {
    case 30:
    case 31:
    case 32:
    case 33:
    case 34:
      return true;
    }
    return false;
  }

  public String getAlgorithm()
  {
    return "diffie-hellman-group-exchange-sha1";
  }

  public void performClientExchange(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SshException
  {
    try
    {
      this.b = paramString1;
      this.e = paramString2;
      this.i = paramArrayOfByte1;
      this.n = paramArrayOfByte2;
      try
      {
        this.m = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyPairGenerator.getInstance("DH") : KeyPairGenerator.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
        this.j = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyAgreement.getInstance("DH") : KeyAgreement.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new SshException("JCE does not support Diffie Hellman key exchange", 16);
      }
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(30);
      localByteArrayWriter.writeInt(1024);
      this.transport.sendMessage(localByteArrayWriter.toByteArray(), true);
      byte[] arrayOfByte = this.transport.nextMessage();
      if (arrayOfByte[0] != 31)
      {
        this.transport.disconnect(3, "Expected SSH_MSG_KEX_GEX_GROUP");
        throw new SshException("Key exchange failed: Expected SSH_MSG_KEX_GEX_GROUP", 5);
      }
      ByteArrayReader localByteArrayReader = new ByteArrayReader(arrayOfByte, 1, arrayOfByte.length - 1);
      this.d = localByteArrayReader.readBigInteger();
      this.f = localByteArrayReader.readBigInteger();
      try
      {
        DHParameterSpec localDHParameterSpec = new DHParameterSpec(this.d, this.f);
        this.m.initialize(localDHParameterSpec);
        KeyPair localKeyPair = this.m.generateKeyPair();
        this.j.init(localKeyPair.getPrivate());
        this.l = ((DHPrivateKey)localKeyPair.getPrivate()).getX();
        this.h = ((DHPublicKey)localKeyPair.getPublic()).getY();
      }
      catch (InvalidKeyException localInvalidKeyException)
      {
        throw new SshException("Failed to generate DH value", 16);
      }
      catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
      {
        throw new SshException("Failed to generate DH value", 16);
      }
      localByteArrayWriter.reset();
      localByteArrayWriter.write(32);
      localByteArrayWriter.writeBigInteger(this.h);
      this.transport.sendMessage(localByteArrayWriter.toByteArray(), true);
      arrayOfByte = this.transport.nextMessage();
      if (arrayOfByte[0] != 33)
      {
        this.transport.disconnect(3, "Expected SSH_MSG_KEXDH_GEX_REPLY");
        throw new SshException("Key exchange failed: Expected SSH_MSG_KEXDH_GEX_REPLY", 5);
      }
      localByteArrayReader = new ByteArrayReader(arrayOfByte, 1, arrayOfByte.length - 1);
      this.hostKey = localByteArrayReader.readBinaryString();
      this.g = localByteArrayReader.readBigInteger();
      this.signature = localByteArrayReader.readBinaryString();
      this.secret = this.g.modPow(this.l, this.d);
      calculateExchangeHash();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public String getProvider()
  {
    if (this.j != null)
      return this.j.getProvider().getName();
    return "";
  }

  protected void calculateExchangeHash()
    throws SshException
  {
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("SHA-1");
    localDigest.putString(this.b);
    localDigest.putString(this.e);
    localDigest.putInt(this.i.length);
    localDigest.putBytes(this.i);
    localDigest.putInt(this.n.length);
    localDigest.putBytes(this.n);
    localDigest.putInt(this.hostKey.length);
    localDigest.putBytes(this.hostKey);
    localDigest.putInt(1024);
    localDigest.putBigInteger(this.d);
    localDigest.putBigInteger(this.f);
    localDigest.putBigInteger(this.h);
    localDigest.putBigInteger(this.g);
    localDigest.putBigInteger(this.secret);
    this.exchangeHash = localDigest.doFinal();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.DiffieHellmanGroupExchangeSha1
 * JD-Core Version:    0.6.0
 */