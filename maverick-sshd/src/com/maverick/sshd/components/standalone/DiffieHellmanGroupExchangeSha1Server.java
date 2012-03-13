package com.maverick.sshd.components.standalone;

import com.maverick.crypto.security.SecureRandom;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.DiffieHellmanGroups;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshPrivateKey;
import com.maverick.ssh.components.SshPublicKey;
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

public class DiffieHellmanGroupExchangeSha1Server extends SshKeyExchangeServer
{
  static final BigInteger ¢ = BigInteger.valueOf(1L);
  static final BigInteger º = BigInteger.valueOf(2L);
  static final BigInteger ª = º;
  BigInteger £ = null;
  BigInteger À = null;
  BigInteger µ = null;
  BigInteger Ã = null;
  BigInteger Â = null;
  UnsignedInteger32 ¥ = null;
  UnsignedInteger32 ¤ = null;
  UnsignedInteger32 Á = null;
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
      this.¤ = localByteArrayReader.readUINT32();
      this.£ = DiffieHellmanGroups.getSafePrime(this.¤);
      break;
    case 34:
      this.¥ = localByteArrayReader.readUINT32();
      this.¤ = localByteArrayReader.readUINT32();
      this.Á = localByteArrayReader.readUINT32();
      this.£ = DiffieHellmanGroups.getSafePrime(this.Á);
      break;
    default:
      return false;
    }
    this.transport.postMessage(new SshMessage()
    {
      public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
      {
        paramByteBuffer.put(31);
        byte[] arrayOfByte = DiffieHellmanGroupExchangeSha1Server.this.£.toByteArray();
        paramByteBuffer.putInt(arrayOfByte.length);
        paramByteBuffer.put(arrayOfByte);
        arrayOfByte = DiffieHellmanGroupExchangeSha1Server.ª.toByteArray();
        paramByteBuffer.putInt(arrayOfByte.length);
        paramByteBuffer.put(arrayOfByte);
        return true;
      }

      public void messageSent()
      {
      }
    }
    , true);
    BigInteger localBigInteger = this.£.subtract(¢).divide(º);
    do
      this.Â = new BigInteger(localBigInteger.bitLength(), SecureRandom.getInstance());
    while ((this.Â.compareTo(BigInteger.ONE) < 0) || (this.Â.compareTo(localBigInteger) > 0));
    this.µ = ª.modPow(this.Â, this.£);
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
    this.À = localByteArrayReader.readBigInteger();
    this.secret = this.À.modPow(this.Â, this.£);
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
          paramByteBuffer.putInt(DiffieHellmanGroupExchangeSha1Server.this.hostKey.length);
          paramByteBuffer.put(DiffieHellmanGroupExchangeSha1Server.this.hostKey);
          byte[] arrayOfByte = DiffieHellmanGroupExchangeSha1Server.this.µ.toByteArray();
          paramByteBuffer.putInt(arrayOfByte.length);
          paramByteBuffer.put(arrayOfByte);
          ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
          localByteArrayWriter.writeString(DiffieHellmanGroupExchangeSha1Server.this.pubkey.getAlgorithm());
          localByteArrayWriter.writeBinaryString(DiffieHellmanGroupExchangeSha1Server.this.signature);
          arrayOfByte = localByteArrayWriter.toByteArray();
          paramByteBuffer.putInt(arrayOfByte.length);
          paramByteBuffer.put(arrayOfByte);
        }
        catch (IOException localIOException)
        {
          DiffieHellmanGroupExchangeSha1Server.this.transport.disconnect(3, "Could not read host key");
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
    if (this.¥ != null)
      localDigest.putInt(this.¥.intValue());
    localDigest.putInt(this.¤.intValue());
    if (this.Á != null)
      localDigest.putInt(this.Á.intValue());
    localDigest.putBigInteger(this.£);
    localDigest.putBigInteger(ª);
    localDigest.putBigInteger(this.À);
    localDigest.putBigInteger(this.µ);
    localDigest.putBigInteger(this.secret);
    this.exchangeHash = localDigest.doFinal();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.standalone.DiffieHellmanGroupExchangeSha1Server
 * JD-Core Version:    0.6.0
 */