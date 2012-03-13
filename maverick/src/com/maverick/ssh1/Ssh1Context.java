package com.maverick.ssh1;

import com.maverick.ssh.ForwardingRequestListener;
import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshSecureRandomGenerator;

public final class Ssh1Context
  implements SshContext
{
  public static final int CIPHER_DES = 2;
  public static final int CIPHER_3DES = 3;
  String d = "/usr/libexec/sftp-server";
  int c = 10;
  HostKeyVerification i;
  String g = null;
  byte[] b = null;
  byte[] e = null;
  ForwardingRequestListener j = null;
  int h = 3;
  int f = 60000;

  public void setChannelLimit(int paramInt)
  {
    this.c = paramInt;
  }

  public int getChannelLimit()
  {
    return this.c;
  }

  public void setSFTPProvider(String paramString)
  {
    this.d = paramString;
  }

  public String getSFTPProvider()
  {
    return this.d;
  }

  public void setX11Display(String paramString)
  {
    this.g = paramString;
  }

  public String getX11Display()
  {
    return this.g;
  }

  public byte[] getX11AuthenticationCookie()
    throws SshException
  {
    if (this.b == null)
    {
      this.b = new byte[16];
      ComponentManager.getInstance().getRND().nextBytes(this.b);
    }
    return this.b;
  }

  public void setX11RealCookie(byte[] paramArrayOfByte)
  {
    this.e = paramArrayOfByte;
  }

  public byte[] getX11RealCookie()
    throws SshException
  {
    if (this.e == null)
      this.e = getX11AuthenticationCookie();
    return this.e;
  }

  public void setX11RequestListener(ForwardingRequestListener paramForwardingRequestListener)
  {
    this.j = paramForwardingRequestListener;
  }

  public ForwardingRequestListener getX11RequestListener()
  {
    return this.j;
  }

  public int getCipherType(int paramInt)
    throws SshException
  {
    int k = 1 << this.h;
    if ((paramInt & k) != 0)
      return this.h;
    k = 8;
    if ((paramInt & k) != 0)
      return 3;
    k = 4;
    if ((paramInt & k) != 0)
      return 2;
    throw new SshException("Cipher could not be agreed!", 9);
  }

  public void setCipherType(int paramInt)
  {
    this.h = paramInt;
  }

  public SshCipher createCipher(int paramInt)
    throws SshException
  {
    return (SshCipher)ComponentManager.getInstance().supportedSsh1CiphersCS().getInstance(String.valueOf(paramInt));
  }

  public void setHostKeyVerification(HostKeyVerification paramHostKeyVerification)
  {
    this.i = paramHostKeyVerification;
  }

  public HostKeyVerification getHostKeyVerification()
  {
    return this.i;
  }

  public void setMessageTimeout(int paramInt)
  {
    this.f = paramInt;
  }

  public int getMessageTimeout()
  {
    return this.f;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.Ssh1Context
 * JD-Core Version:    0.6.0
 */