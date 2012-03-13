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
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class DiffieHellmanGroup1Sha1Server extends SshKeyExchangeServer
{
  public static final String DIFFIE_HELLMAN_GROUP1_SHA1 = "diffie-hellman-group1-sha1";
  static final BigInteger L = BigInteger.valueOf(1L);
  static final BigInteger Q = BigInteger.valueOf(2L);
  static final BigInteger O = Q;
  static final BigInteger N = DiffieHellmanGroups.group1;
  static BigInteger M = N.subtract(L).divide(Q);
  BigInteger R = null;
  BigInteger P = null;
  BigInteger T = null;
  BigInteger S = null;

  public String getAlgorithm()
  {
    return "diffie-hellman-group1-sha1";
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
    do
      this.S = new BigInteger(M.bitLength(), SecureRandom.getInstance());
    while ((this.S.compareTo(BigInteger.ONE) < 0) || (this.S.compareTo(M) > 0));
    this.P = O.modPow(this.S, N);
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
      this.R = localByteArrayReader.readBigInteger();
      this.secret = this.R.modPow(this.S, N);
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
            paramByteBuffer.putInt(DiffieHellmanGroup1Sha1Server.this.hostKey.length);
            paramByteBuffer.put(DiffieHellmanGroup1Sha1Server.this.hostKey);
            byte[] arrayOfByte = DiffieHellmanGroup1Sha1Server.this.P.toByteArray();
            paramByteBuffer.putInt(arrayOfByte.length);
            paramByteBuffer.put(arrayOfByte);
            ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
            localByteArrayWriter.writeString(DiffieHellmanGroup1Sha1Server.this.pubkey.getAlgorithm());
            localByteArrayWriter.writeBinaryString(DiffieHellmanGroup1Sha1Server.this.signature);
            arrayOfByte = localByteArrayWriter.toByteArray();
            paramByteBuffer.putInt(arrayOfByte.length);
            paramByteBuffer.put(arrayOfByte);
          }
          catch (IOException localIOException)
          {
            DiffieHellmanGroup1Sha1Server.this.transport.disconnect(3, "Could not read host key");
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
    localDigest.putBigInteger(this.R);
    localDigest.putBigInteger(this.P);
    localDigest.putBigInteger(this.secret);
    this.exchangeHash = localDigest.doFinal();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.standalone.DiffieHellmanGroup1Sha1Server
 * JD-Core Version:    0.6.0
 */