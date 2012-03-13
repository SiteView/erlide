package com.maverick.ssh1;

import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.SshTunnel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class d extends e
  implements SshTunnel
{
  String ie;
  int be;
  String de;
  int fe;
  String ee;
  int ge;
  int he;
  SshTransport ce;
  SshContext ae;

  d(SshContext paramSshContext, String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, int paramInt3, int paramInt4, SshTransport paramSshTransport)
    throws SshException
  {
    this.ae = paramSshContext;
    this.ie = paramString1;
    this.be = paramInt1;
    this.de = paramString2;
    this.fe = paramInt2;
    this.ee = paramString3;
    this.ge = paramInt3;
    this.he = paramInt4;
    if (isX11())
      this.ce = new _b(paramSshTransport);
    else
      this.ce = paramSshTransport;
  }

  public String getHost()
  {
    return this.ie;
  }

  public int getPort()
  {
    return this.be;
  }

  public String getListeningAddress()
  {
    return this.de;
  }

  public int getListeningPort()
  {
    return this.fe;
  }

  public String getOriginatingHost()
  {
    return this.ee;
  }

  public int getOriginatingPort()
  {
    return this.ge;
  }

  public boolean isLocal()
  {
    return this.he == 1;
  }

  public boolean isX11()
  {
    return this.he == 3;
  }

  public SshTransport getTransport()
  {
    return this.ce;
  }

  public SshTransport duplicate()
    throws IOException
  {
    throw new SshIOException(new SshException("SSH tunnels cannot be duplicated!", 4));
  }

  class _c extends OutputStream
  {
    byte[] g = new byte[1024];
    boolean e = false;
    int d = 0;
    int c = 12;
    int i;
    int h;
    OutputStream f;

    _c(OutputStream arg2)
    {
      Object localObject;
      this.f = localObject;
    }

    public void write(int paramInt)
      throws IOException
    {
      write(new byte[] { (byte)paramInt }, 0, 1);
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      try
      {
        if ((d.this.isX11()) && (!this.e))
        {
          int j;
          if (this.d < 12)
          {
            j = b(paramArrayOfByte, paramInt1, paramInt2);
            paramInt2 -= j;
            paramInt1 += j;
            if (this.c == 0)
            {
              if (this.g[0] == 66)
              {
                this.i = ((this.g[6] & 0xFF) << 8 | this.g[7] & 0xFF);
                this.h = ((this.g[8] & 0xFF) << 8 | this.g[9] & 0xFF);
              }
              else if (this.g[0] == 108)
              {
                this.i = ((this.g[7] & 0xFF) << 8 | this.g[6] & 0xFF);
                this.h = ((this.g[9] & 0xFF) << 8 | this.g[8] & 0xFF);
              }
              else
              {
                throw new SshIOException(new SshException("Corrupt X11 authentication packet", 3));
              }
              this.c = (this.i + 3 & 0xFFFFFFFC);
              this.c += (this.h + 3 & 0xFFFFFFFC);
              if (this.c + this.d > this.g.length)
                throw new SshIOException(new SshException("Corrupt X11 authentication packet", 3));
              if (this.c == 0)
                throw new SshIOException(new SshException("X11 authentication cookie not found", 3));
            }
          }
          if (paramInt2 > 0)
          {
            j = b(paramArrayOfByte, paramInt1, paramInt2);
            paramInt2 -= j;
            paramInt1 += j;
            if (this.c == 0)
            {
              byte[] arrayOfByte1 = d.this.ae.getX11AuthenticationCookie();
              String str = new String(this.g, 12, this.i);
              byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
              this.i = (this.i + 3 & 0xFFFFFFFC);
              System.arraycopy(this.g, 12 + this.i, arrayOfByte2, 0, arrayOfByte1.length);
              if ((!"MIT-MAGIC-COOKIE-1".equals(str)) || (!b(arrayOfByte1, arrayOfByte2, arrayOfByte1.length)))
                throw new SshIOException(new SshException("Incorrect X11 cookie", 3));
              byte[] arrayOfByte3 = d.this.ae.getX11RealCookie();
              if (arrayOfByte3.length != this.h)
                throw new SshIOException(new SshException("Invalid X11 cookie", 3));
              System.arraycopy(arrayOfByte3, 0, this.g, 12 + this.i, arrayOfByte3.length);
              this.e = true;
              this.f.write(this.g, 0, this.d);
              this.g = null;
            }
          }
          if ((!this.e) || (paramInt2 == 0))
            return;
        }
        this.f.write(paramArrayOfByte, paramInt1, paramInt2);
      }
      catch (SshException localSshException)
      {
        throw new SshIOException(localSshException);
      }
    }

    private boolean b(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
    {
      for (int j = 0; (j < paramInt) && (paramArrayOfByte1[j] == paramArrayOfByte2[j]); j++);
      return j == paramInt;
    }

    private int b(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      if (paramInt2 > this.c)
      {
        System.arraycopy(paramArrayOfByte, paramInt1, this.g, this.d, this.c);
        this.d += this.c;
        paramInt2 = this.c;
        this.c = 0;
      }
      else
      {
        System.arraycopy(paramArrayOfByte, paramInt1, this.g, this.d, paramInt2);
        this.d += paramInt2;
        this.c -= paramInt2;
      }
      return paramInt2;
    }
  }

  class _b
    implements SshTransport
  {
    SshTransport od;
    d._c nd;

    _b(SshTransport arg2)
      throws SshException
    {
      try
      {
        Object localObject;
        this.od = localObject;
        this.nd = new d._c(d.this, localObject.getOutputStream());
      }
      catch (IOException localIOException)
      {
        throw new SshException(localIOException);
      }
    }

    public InputStream getInputStream()
      throws IOException
    {
      return this.od.getInputStream();
    }

    public OutputStream getOutputStream()
    {
      return this.nd;
    }

    public String getHost()
    {
      return this.od.getHost();
    }

    public int getPort()
    {
      return this.od.getPort();
    }

    public SshTransport duplicate()
      throws IOException
    {
      return this.od.duplicate();
    }

    public void close()
      throws IOException
    {
      this.od.close();
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.d
 * JD-Core Version:    0.6.0
 */