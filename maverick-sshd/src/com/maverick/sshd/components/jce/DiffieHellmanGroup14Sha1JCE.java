package com.maverick.sshd.components.jce;

import com.maverick.ssh.SshException;
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
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class DiffieHellmanGroup14Sha1JCE extends SshKeyExchangeServer
  implements AbstractKeyExchange
{
  public static final String DIFFIE_HELLMAN_GROUP14_SHA1 = "diffie-hellman-group14-sha1";
  static final BigInteger d = BigInteger.valueOf(1L);
  static final BigInteger h = BigInteger.valueOf(2L);
  static final BigInteger f = h;
  static final BigInteger e = DiffieHellmanGroups.group14;
  BigInteger i = null;
  BigInteger g = null;
  BigInteger l = null;
  BigInteger k = null;
  KeyPairGenerator m;
  KeyAgreement j;

  public String getAlgorithm()
  {
    return "diffie-hellman-group14-sha1";
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
      this.m = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyPairGenerator.getInstance("DH") : KeyPairGenerator.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
      this.j = (JCEProvider.getProviderForAlgorithm("DH") == null ? KeyAgreement.getInstance("DH") : KeyAgreement.getInstance("DH", JCEProvider.getProviderForAlgorithm("DH")));
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IOException("JCE does not support Diffie Hellman key exchange");
    }
    try
    {
      DHParameterSpec localDHParameterSpec = new DHParameterSpec(e, f);
      this.m.initialize(localDHParameterSpec, JCEProvider.getSecureRandom());
      KeyPair localKeyPair = this.m.generateKeyPair();
      this.j.init(localKeyPair.getPrivate());
      this.k = ((DHPrivateKey)localKeyPair.getPrivate()).getX();
      this.g = ((DHPublicKey)localKeyPair.getPublic()).getY();
    }
    catch (Exception localException)
    {
      throw new IOException("Failed to generate DH value: " + localException.getMessage());
    }
  }

  public String getProvider()
  {
    if (this.j != null)
      return this.j.getProvider().getName();
    return "";
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws SshException, IOException
  {
    switch (paramArrayOfByte[0])
    {
    case 30:
      if ((this.firstPacketFollows) && (!this.useFirstPacket))
      {
        this.firstPacketFollows = false;
        return true;
      }
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
      localByteArrayReader.skip(1L);
      this.i = localByteArrayReader.readBigInteger();
      this.secret = this.i.modPow(this.k, e);
      this.hostKey = this.pubkey.getEncoded();
      calculateExchangeHash();
      this.signature = this.prvkey.sign(this.exchangeHash);
      this.transport.postMessage(new SshMessage()
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          try
          {
            paramByteBuffer.put(31);
            paramByteBuffer.putInt(DiffieHellmanGroup14Sha1JCE.this.hostKey.length);
            paramByteBuffer.put(DiffieHellmanGroup14Sha1JCE.this.hostKey);
            byte[] arrayOfByte = DiffieHellmanGroup14Sha1JCE.this.g.toByteArray();
            paramByteBuffer.putInt(arrayOfByte.length);
            paramByteBuffer.put(arrayOfByte);
            ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
            localByteArrayWriter.writeString(DiffieHellmanGroup14Sha1JCE.this.pubkey.getAlgorithm());
            localByteArrayWriter.writeBinaryString(DiffieHellmanGroup14Sha1JCE.this.signature);
            arrayOfByte = localByteArrayWriter.toByteArray();
            paramByteBuffer.putInt(arrayOfByte.length);
            paramByteBuffer.put(arrayOfByte);
          }
          catch (IOException localIOException)
          {
            DiffieHellmanGroup14Sha1JCE.this.transport.disconnect(3, "Could not read host key");
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
    return false;
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
    localDigest.putBigInteger(this.i);
    localDigest.putBigInteger(this.g);
    localDigest.putBigInteger(this.secret);
    this.exchangeHash = localDigest.doFinal();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.jce.DiffieHellmanGroup14Sha1JCE
 * JD-Core Version:    0.6.0
 */