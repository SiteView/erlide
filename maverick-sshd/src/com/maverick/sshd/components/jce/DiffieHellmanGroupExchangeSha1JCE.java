package com.maverick.sshd.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.DiffieHellmanGroups;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshPrivateKey;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.jce.AbstractKeyExchange;
import com.maverick.ssh.components.jce.JCEProvider;
import com.maverick.sshd.SshMessage;
import com.maverick.sshd.TransportProtocol;
import com.maverick.sshd.components.ServerComponentManager;
import com.maverick.sshd.components.SshKeyExchangeServer;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import com.maverick.util.UnsignedInteger32;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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

public class DiffieHellmanGroupExchangeSha1JCE extends SshKeyExchangeServer
  implements AbstractKeyExchange
{
  static final BigInteger n = BigInteger.valueOf(1L);
  static final BigInteger t = BigInteger.valueOf(2L);
  static final BigInteger r = t;
  BigInteger o = null;
  BigInteger u = null;
  BigInteger s = null;
  BigInteger y = null;
  BigInteger x = null;
  UnsignedInteger32 q = null;
  UnsignedInteger32 p = null;
  UnsignedInteger32 w = null;
  KeyPairGenerator z;
  KeyAgreement v;
  public static final String DIFFIE_HELLMAN_GROUP_EXCHANGE_SHA1 = "diffie-hellman-group-exchange-sha1";

  public String getAlgorithm()
  {
    return "diffie-hellman-group-exchange-sha1";
  }

  public void init(TransportProtocol paramTransportProtocol, String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, SshPrivateKey paramSshPrivateKey, SshPublicKey paramSshPublicKey, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    this.clientId = paramString1;
    this.serverId = paramString2;
    this.clientKexInit = paramArrayOfByte1;
    this.serverKexInit = paramArrayOfByte2;
    this.prvkey = paramSshPrivateKey;
    this.pubkey = paramSshPublicKey;
    this.firstPacketFollows = paramBoolean1;
    this.useFirstPacket = paramBoolean2;
    this.transport = paramTransportProtocol;
    try
    {
      this.z = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyPairGenerator.getInstance("DH") : KeyPairGenerator.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
      this.v = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyAgreement.getInstance("DH") : KeyAgreement.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new SshIOException(new SshException("JCE does not support Diffie Hellman key exchange", 16));
    }
  }

  public String getProvider()
  {
    if (this.v != null)
      return this.v.getProvider().getName();
    return "";
  }

  public boolean exchangeGroup(byte[] paramArrayOfByte)
    throws SshException, IOException
  {
    if ((this.firstPacketFollows) && (!this.useFirstPacket))
    {
      this.firstPacketFollows = false;
      return true;
    }
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    switch (localByteArrayReader.read())
    {
    case 30:
      this.p = localByteArrayReader.readUINT32();
      this.o = DiffieHellmanGroups.getSafePrime(this.p);
      break;
    case 34:
      this.q = localByteArrayReader.readUINT32();
      this.p = localByteArrayReader.readUINT32();
      this.w = localByteArrayReader.readUINT32();
      this.o = DiffieHellmanGroups.getSafePrime(this.w);
      break;
    default:
      return false;
    }
    this.transport.postMessage(new SshMessage()
    {
      public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
      {
        paramByteBuffer.put(31);
        byte[] arrayOfByte = DiffieHellmanGroupExchangeSha1JCE.this.o.toByteArray();
        paramByteBuffer.putInt(arrayOfByte.length);
        paramByteBuffer.put(arrayOfByte);
        arrayOfByte = DiffieHellmanGroupExchangeSha1JCE.r.toByteArray();
        paramByteBuffer.putInt(arrayOfByte.length);
        paramByteBuffer.put(arrayOfByte);
        return true;
      }

      public void messageSent()
      {
      }
    }
    , true);
    BigInteger localBigInteger = this.o.subtract(n).divide(t);
    try
    {
      DHParameterSpec localDHParameterSpec = new DHParameterSpec(this.o, r);
      this.z.initialize(localDHParameterSpec);
      KeyPair localKeyPair = this.z.generateKeyPair();
      this.v.init(localKeyPair.getPrivate());
      this.x = ((DHPrivateKey)localKeyPair.getPrivate()).getX();
      this.s = ((DHPublicKey)localKeyPair.getPublic()).getY();
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new SshException("Failed to generate DH value", 16);
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new SshException("Failed to generate DH value", 16);
    }
    return true;
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws SshException, IOException
  {
    if (exchangeGroup(paramArrayOfByte))
      return true;
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    if (localByteArrayReader.read() != 32)
      return false;
    this.u = localByteArrayReader.readBigInteger();
    this.secret = this.u.modPow(this.x, this.o);
    this.hostKey = this.pubkey.getEncoded();
    calculateExchangeHash();
    this.signature = this.prvkey.sign(this.exchangeHash);
    this.transport.postMessage(new SshMessage()
    {
      public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
      {
        try
        {
          paramByteBuffer.put(33);
          paramByteBuffer.putInt(DiffieHellmanGroupExchangeSha1JCE.this.hostKey.length);
          paramByteBuffer.put(DiffieHellmanGroupExchangeSha1JCE.this.hostKey);
          byte[] arrayOfByte = DiffieHellmanGroupExchangeSha1JCE.this.s.toByteArray();
          paramByteBuffer.putInt(arrayOfByte.length);
          paramByteBuffer.put(arrayOfByte);
          ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
          localByteArrayWriter.writeString(DiffieHellmanGroupExchangeSha1JCE.this.pubkey.getAlgorithm());
          localByteArrayWriter.writeBinaryString(DiffieHellmanGroupExchangeSha1JCE.this.signature);
          arrayOfByte = localByteArrayWriter.toByteArray();
          paramByteBuffer.putInt(arrayOfByte.length);
          paramByteBuffer.put(arrayOfByte);
        }
        catch (IOException localIOException)
        {
          DiffieHellmanGroupExchangeSha1JCE.this.transport.disconnect(3, "Could not read host key");
        }
        return true;
      }

      public void messageSent()
      {
      }
    }
    , true);
    this.transport.sendNewKeys();
    return true;
  }

  protected void calculateExchangeHash()
    throws SshException
  {
    Digest localDigest = (Digest)ServerComponentManager.getInstance().supportedDigests().getInstance("SHA-1");
    localDigest.putString(this.clientId);
    localDigest.putString(this.serverId);
    localDigest.putInt(this.clientKexInit.length);
    localDigest.putBytes(this.clientKexInit);
    localDigest.putInt(this.serverKexInit.length);
    localDigest.putBytes(this.serverKexInit);
    localDigest.putInt(this.hostKey.length);
    localDigest.putBytes(this.hostKey);
    if (this.q != null)
      localDigest.putInt(this.q.intValue());
    localDigest.putInt(this.p.intValue());
    if (this.w != null)
      localDigest.putInt(this.w.intValue());
    localDigest.putBigInteger(this.o);
    localDigest.putBigInteger(r);
    localDigest.putBigInteger(this.u);
    localDigest.putBigInteger(this.s);
    localDigest.putBigInteger(this.secret);
    this.exchangeHash = localDigest.doFinal();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.jce.DiffieHellmanGroupExchangeSha1JCE
 * JD-Core Version:    0.6.0
 */