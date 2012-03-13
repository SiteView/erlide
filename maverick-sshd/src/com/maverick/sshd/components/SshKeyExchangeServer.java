package com.maverick.sshd.components;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshPrivateKey;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.sshd.TransportProtocol;
import java.io.IOException;
import java.math.BigInteger;

public abstract class SshKeyExchangeServer
{
  protected BigInteger secret;
  protected byte[] exchangeHash;
  protected byte[] hostKey;
  protected byte[] signature;
  protected String clientId;
  protected String serverId;
  protected byte[] clientKexInit;
  protected byte[] serverKexInit;
  protected SshPrivateKey prvkey;
  protected SshPublicKey pubkey;
  protected boolean firstPacketFollows;
  protected boolean useFirstPacket;
  boolean A = false;
  boolean B = false;
  protected TransportProtocol transport;

  public void setReceivedNewKeys(boolean paramBoolean)
  {
    this.B = paramBoolean;
  }

  public void setSentNewKeys(boolean paramBoolean)
  {
    this.A = paramBoolean;
  }

  public boolean hasSentNewKeys()
  {
    return this.A;
  }

  public boolean hasReceivedNewKeys()
  {
    return this.B;
  }

  public abstract String getAlgorithm();

  public byte[] getExchangeHash()
  {
    return this.exchangeHash;
  }

  public byte[] getHostKey()
  {
    return this.hostKey;
  }

  public BigInteger getSecret()
  {
    return this.secret;
  }

  public byte[] getSignature()
  {
    return this.signature;
  }

  public abstract void init(TransportProtocol paramTransportProtocol, String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, SshPrivateKey paramSshPrivateKey, SshPublicKey paramSshPublicKey, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException;

  public abstract boolean processMessage(byte[] paramArrayOfByte)
    throws SshException, IOException;

  public void reset()
  {
    this.exchangeHash = null;
    this.hostKey = null;
    this.signature = null;
    this.secret = null;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.SshKeyExchangeServer
 * JD-Core Version:    0.6.0
 */