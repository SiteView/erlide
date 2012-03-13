package com.maverick.ssh.components;

import com.maverick.ssh.SshException;
import com.maverick.ssh2.TransportProtocol;
import java.math.BigInteger;

public abstract class SshKeyExchangeClient
  implements SshKeyExchange
{
  protected BigInteger secret;
  protected byte[] exchangeHash;
  protected byte[] hostKey;
  protected byte[] signature;
  protected TransportProtocol transport;

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

  public void init(TransportProtocol paramTransportProtocol, boolean paramBoolean)
  {
    this.transport = paramTransportProtocol;
  }

  public abstract void performClientExchange(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SshException;

  public abstract boolean isKeyExchangeMessage(int paramInt);

  public void reset()
  {
    this.exchangeHash = null;
    this.hostKey = null;
    this.signature = null;
    this.secret = null;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.SshKeyExchangeClient
 * JD-Core Version:    0.6.0
 */