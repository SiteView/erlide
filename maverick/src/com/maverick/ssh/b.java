package com.maverick.ssh;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class b extends InputStream
{
  private String j;
  private byte[] k;
  private byte[] h;
  private int l;
  private StringBuffer o;
  private String b;
  private StringBuffer d = new StringBuffer();
  private boolean f = true;
  private int n = -2147483648;
  private Shell i;
  private BufferedInputStream p;
  private boolean c = true;
  private boolean m;
  private static Log e = LogFactory.getLog(b.class);
  private static boolean g = Boolean.getBoolean("maverick.shell.verbose");

  b(Shell paramShell, String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4)
  {
    this.j = paramString1;
    this.k = paramString2.getBytes();
    this.m = paramBoolean;
    this.h = paramString4.getBytes();
    this.i = paramShell;
    this.b = paramString3;
    this.p = paramShell.j;
  }

  public int c()
    throws IllegalStateException
  {
    return this.n;
  }

  public boolean f()
  {
    return this.n == 0;
  }

  public String g()
  {
    return this.d.toString().trim();
  }

  private String d()
    throws IOException
  {
    this.p.mark(-1);
    StringBuffer localStringBuffer = new StringBuffer();
    int i1;
    do
    {
      i1 = this.p.read();
      if (i1 <= -1)
        continue;
      localStringBuffer.append((char)i1);
    }
    while ((i1 != 10) && (i1 != -1));
    if ((i1 == -1) && (localStringBuffer.toString().trim().length() == 0))
      return null;
    return localStringBuffer.toString().trim();
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i1 = read();
    if (i1 > -1)
    {
      paramArrayOfByte[paramInt1] = (byte)i1;
      return 1;
    }
    return -1;
  }

  public int read()
    throws IOException
  {
    if (this.f)
    {
      if (e.isDebugEnabled())
        e.debug(this.b + ": Expecting begin marker");
      String str;
      do
        str = d();
      while ((str != null) && (!str.endsWith(this.j)));
      if (str == null)
      {
        if (e.isDebugEnabled())
          e.debug(this.b + ": Failed to read from shell whilst waiting for begin marker");
        this.i.c();
        return -1;
      }
      this.o = new StringBuffer();
      this.f = false;
      if (e.isDebugEnabled())
        e.debug(this.b + ": Found begin marker");
    }
    int i2 = Math.max(this.k.length, this.h.length);
    this.p.mark(i2);
    int i3 = 0;
    int i4 = 0;
    boolean bool = true;
    byte[] arrayOfByte = null;
    StringBuffer localStringBuffer = new StringBuffer();
    do
    {
      int i1 = this.p.read();
      if ((i3 == 0) && (i4 == 0))
      {
        if (this.k[this.l] == i1)
        {
          i3 = 1;
          i2 = this.k.length;
          arrayOfByte = this.k;
        }
        else
        {
          if ((!this.m) || (this.h[this.l] != i1))
            break;
          i4 = 1;
          i2 = this.h.length;
          arrayOfByte = this.h;
          bool = false;
        }
      }
      else if (i3 != 0)
      {
        if (this.k[this.l] != i1)
        {
          i3 = 0;
          break;
        }
      }
      else if ((i4 != 0) && (this.h[this.l] != i1))
      {
        i4 = 0;
        break;
      }
      localStringBuffer.append((char)i1);
    }
    while ((this.l++ < i2 - 1) && ((i3 != 0) || (i4 != 0)));
    if ((arrayOfByte != null) && (this.l == arrayOfByte.length))
    {
      if (e.isDebugEnabled())
        e.debug(this.b + ": " + localStringBuffer.toString());
      b(bool, bool ? "end" : "prompt");
      return -1;
    }
    this.p.reset();
    int i1 = this.p.read();
    if (i1 == -1)
    {
      b(false, "EOF");
      return -1;
    }
    this.l = 0;
    this.o.append((char)i1);
    this.d.append((char)i1);
    if (i1 == 10)
      this.o = new StringBuffer();
    if ((g) && (e.isDebugEnabled()))
      e.debug(this.b + ": Current Line [" + this.o.toString() + "]");
    this.p.mark(-1);
    return i1;
  }

  void b(boolean paramBoolean, String paramString)
    throws IOException
  {
    if (e.isDebugEnabled())
      e.debug(this.b + ": Found " + paramString + " marker");
    if (paramBoolean)
      this.n = e();
    else
      this.n = -2147483647;
    this.i.b = 1;
    this.c = false;
  }

  boolean b()
  {
    return this.c;
  }

  int e()
    throws IOException
  {
    if (e.isDebugEnabled())
      e.debug(this.b + ": Looking for exit code");
    StringBuffer localStringBuffer = new StringBuffer();
    int i1 = -1;
    char c1;
    do
    {
      c1 = (char)this.p.read();
      localStringBuffer.append(c1);
    }
    while (c1 != '\n');
    try
    {
      i1 = Integer.parseInt(localStringBuffer.toString().trim());
      if (e.isDebugEnabled())
        e.debug(this.b + ": Exit code is " + i1);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      if (e.isDebugEnabled())
        e.debug(this.b + ": Failed to get exit code: " + localStringBuffer.toString().trim());
      i1 = -2147483647;
    }
    return i1;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.b
 * JD-Core Version:    0.6.0
 */