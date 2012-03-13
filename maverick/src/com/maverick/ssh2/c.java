package com.maverick.ssh2;

import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.SshTunnel;
import com.maverick.ssh.message.SshChannelMessage;
import java.io.IOException;

class c extends Ssh2Channel
  implements SshTunnel
{
  SshTransport le;
  String ve;
  int ke;
  String ne;
  int qe;
  String pe;
  int se;
  byte[] je = new byte[1024];
  boolean te = false;
  int ue = 0;
  int me = 12;
  int oe;
  int re;

  public c(String paramString1, int paramInt1, int paramInt2, String paramString2, int paramInt3, String paramString3, int paramInt4, String paramString4, int paramInt5, SshTransport paramSshTransport)
  {
    super(paramString1, paramInt1, paramInt2, 0L);
    this.le = paramSshTransport;
    this.ve = paramString2;
    this.ke = paramInt3;
    this.ne = paramString3;
    this.qe = paramInt4;
    this.pe = paramString4;
    this.se = paramInt5;
  }

  public String getHost()
  {
    return this.ve;
  }

  public int getPort()
  {
    return this.ke;
  }

  public String getOriginatingHost()
  {
    return this.pe;
  }

  public int getOriginatingPort()
  {
    return this.se;
  }

  public String getListeningAddress()
  {
    return this.ne;
  }

  public int getListeningPort()
  {
    return this.qe;
  }

  public boolean isLocal()
  {
    return getName().equals("direct-tcpip");
  }

  public boolean isX11()
  {
    return getName().equals("x11");
  }

  public SshTransport getTransport()
  {
    return this.le;
  }

  public SshTransport duplicate()
    throws IOException
  {
    throw new SshIOException(new SshException("SSH tunnels cannot be duplicated!", 4));
  }

  public void close()
  {
    super.close();
  }

  protected void processStandardData(int paramInt, SshChannelMessage paramSshChannelMessage)
    throws SshException
  {
    if ((getName().equals("x11")) && (!this.te))
    {
      int i;
      if (this.ue < 12)
      {
        i = b(paramSshChannelMessage);
        paramInt -= i;
        if (this.me == 0)
        {
          if (this.je[0] == 66)
          {
            this.oe = ((this.je[6] & 0xFF) << 8 | this.je[7] & 0xFF);
            this.re = ((this.je[8] & 0xFF) << 8 | this.je[9] & 0xFF);
          }
          else if (this.je[0] == 108)
          {
            this.oe = ((this.je[7] & 0xFF) << 8 | this.je[6] & 0xFF);
            this.re = ((this.je[9] & 0xFF) << 8 | this.je[8] & 0xFF);
          }
          else
          {
            close();
            throw new SshException("Corrupt X11 authentication packet", 6);
          }
          this.me = (this.oe + 3 & 0xFFFFFFFC);
          this.me += (this.re + 3 & 0xFFFFFFFC);
          if (this.me + this.ue > this.je.length)
          {
            close();
            throw new SshException("Corrupt X11 authentication packet", 6);
          }
          if (this.me == 0)
          {
            close();
            throw new SshException("X11 authentication cookie not found", 6);
          }
        }
      }
      if (paramInt > 0)
      {
        i = b(paramSshChannelMessage);
        paramInt -= i;
        if (this.me == 0)
        {
          byte[] arrayOfByte1 = this.i.e().getX11AuthenticationCookie();
          String str = new String(this.je, 12, this.oe);
          byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
          this.oe = (this.oe + 3 & 0xFFFFFFFC);
          System.arraycopy(this.je, 12 + this.oe, arrayOfByte2, 0, arrayOfByte1.length);
          if ((!"MIT-MAGIC-COOKIE-1".equals(str)) || (!b(arrayOfByte1, arrayOfByte2, arrayOfByte1.length)))
          {
            close();
            throw new SshException("Incorrect X11 cookie", 6);
          }
          byte[] arrayOfByte3 = this.i.e().getX11RealCookie();
          if (arrayOfByte3.length != this.re)
            throw new SshException("Invalid X11 cookie", 6);
          System.arraycopy(arrayOfByte3, 0, this.je, 12 + this.oe, arrayOfByte3.length);
          this.te = true;
          super.processStandardData(paramInt, paramSshChannelMessage);
          this.je = null;
        }
      }
      if ((!this.te) || (paramInt == 0))
        return;
    }
    super.processStandardData(paramInt, paramSshChannelMessage);
  }

  private boolean b(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    for (int i = 0; (i < paramInt) && (paramArrayOfByte1[i] == paramArrayOfByte2[i]); i++);
    return i == paramInt;
  }

  private int b(SshChannelMessage paramSshChannelMessage)
  {
    int i = paramSshChannelMessage.available();
    if (i > this.me)
    {
      paramSshChannelMessage.read(this.je, this.ue, this.me);
      this.ue += this.me;
      i = this.me;
      this.me = 0;
    }
    else
    {
      paramSshChannelMessage.read(this.je, this.ue, i);
      this.ue += i;
      this.me -= i;
    }
    return i;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.c
 * JD-Core Version:    0.6.0
 */