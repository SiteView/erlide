package com.maverick.ssh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URL;

class c
{
  String k;
  String d;
  String e;
  String j;
  long b;
  String i;
  int c = 8;
  int f;
  BigInteger h = new BigInteger(new byte[] { 0, -114, 50, 88, -53, 37, -110, -74, -67, 34, 105, 25, 91, 43, -35, 116, -13, 86, 51, -124, 59, 6, -117, 107, 89, 115, 77, -127, -117, 8, -54, -35, -70, -32, -99, -84, -51, 63, 56, 77, -18, -7, 82, -66, -62, 68, -49, 29, 15, 106, 39, 107, 68, 66, 22, -101, 99, 44, -20, -77, 75, -103, -12, 101, -100, -127, 36, 43, 99, -34, -33, -60, -104, -64, 58, 120, -72, 46, 94, -34, 23, -76, 73, 41, -121, 3, 105, -49, -4, -104, 56, 53, 48, 87, -7, -78, -102, -85, -93, -87, 15, 112, 19, -75, 61, -31, -51, -71, -10, -127, 106, -20, 50, -103, 95, -126, -2, -12, 43, 85, 109, -7, -70, -75, -25, 122, 108, -22, -111 });
  BigInteger g = new BigInteger(new byte[] { 1, 0, 1 });

  c()
  {
    e();
  }

  void e()
  {
    b(getClass().getClassLoader().getResource("maverick-license.txt"));
    b(getClass().getClassLoader().getResource("META-INF/maverick-license.txt"));
    try
    {
      b(new File(System.getProperty("maverick.license.directory", System.getProperty("user.dir")) + File.separator + System.getProperty("maverick.license.filename", ".maverick-license.txt")));
    }
    catch (Exception localException)
    {
    }
  }

  private void b(URL paramURL)
  {
    if (paramURL != null)
      try
      {
        InputStream localInputStream = paramURL.openStream();
        try
        {
          b(localInputStream);
        }
        finally
        {
          localInputStream.close();
        }
      }
      catch (Exception localException)
      {
        System.err.println("WARNING: Failed to read Maverick license resource " + paramURL + ". " + localException.getMessage());
      }
  }

  private void b(File paramFile)
  {
    if (paramFile.exists())
      try
      {
        FileInputStream localFileInputStream = new FileInputStream(paramFile);
        try
        {
          b(localFileInputStream);
        }
        finally
        {
          localFileInputStream.close();
        }
      }
      catch (Exception localException)
      {
        System.err.println("WARNING: Failed to read Maverick license file " + paramFile + ". " + localException.getMessage());
      }
  }

  void b(InputStream paramInputStream)
    throws IOException
  {
    for (String str = c(paramInputStream); !str.startsWith("\"----BEGIN 3SP LICENSE"); str = str.substring(1));
    while (!str.endsWith("----END 3SP LICENSE----\\r\\n"))
      str = str.substring(0, str.length() - 1);
    StringBuffer localStringBuffer = new StringBuffer();
    int m = 0;
    int n = 0;
    for (int i1 = 0; i1 < str.length(); i1++)
    {
      char c1 = str.charAt(i1);
      if ((c1 == '"') && (m == 0) && (n == 0))
      {
        m = 1;
      }
      else if ((c1 == '"') && (m != 0) && (n == 0))
      {
        m = 0;
      }
      else
      {
        if (m == 0)
          continue;
        if ((n == 0) && (c1 == '\\'))
        {
          n = 1;
        }
        else
        {
          if (m == 0)
            continue;
          if (n != 0)
          {
            if (c1 == 'r')
              c1 = '\r';
            else if (c1 == 'n')
              c1 = '\n';
            n = 0;
          }
          localStringBuffer.append(c1);
        }
      }
    }
    this.d = localStringBuffer.toString();
  }

  final void b(String paramString)
  {
    this.d = paramString;
  }

  final String c(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      int m;
      while ((m = paramInputStream.read()) > -1)
        localByteArrayOutputStream.write(m);
      String str = new String(localByteArrayOutputStream.toByteArray(), "UTF8");
      return str;
    }
    finally
    {
      try
      {
        paramInputStream.close();
      }
      catch (IOException localIOException3)
      {
      }
      try
      {
        localByteArrayOutputStream.close();
      }
      catch (IOException localIOException4)
      {
      }
    }
    throw localObject;
  }

  final int d()
  {
    try
    {
      long l1 = 1222033367024L;
      long l2 = 31708800000L;
      long l3 = 1209600000L;
      if ((this.d == null) || (this.d.equals("")))
      {
        if ((System.currentTimeMillis() >= l1) && (System.currentTimeMillis() <= l1 + l3))
          return this.c = 4;
        return this.c = 8;
      }
      _e local_e = new _e(this.h, this.g);
      _c local_c = new _c(new ByteArrayInputStream(this.d.getBytes()));
      String str1 = local_c.b();
      if ((!str1.equals("----BEGIN 3SP LICENSE----")) && (!str1.equals("----BEGIN SSHTOOLS LICENSE----")))
        return this.c = 2;
      String str2 = "";
      String str3 = "";
      String str4 = null;
      String str5 = null;
      String str7 = "";
      StringBuffer localStringBuffer = new StringBuffer("");
      while (((str1 = local_c.b()) != null) && (!str1.equals("----END 3SP LICENSE----")) && (!str1.equals("----END SSHTOOLS LICENSE----")))
      {
        int m = str1.indexOf("Licensee: ");
        if (m > -1)
        {
          str2 = str1.substring(m + 10);
          continue;
        }
        m = str1.indexOf("Comments: ");
        if (m > -1)
        {
          str3 = str1.substring(m + 10);
          continue;
        }
        m = str1.indexOf("Created : ");
        if (m > -1)
        {
          str4 = str1.substring(m + 10);
          continue;
        }
        m = str1.indexOf("Type    : ");
        if (m > -1)
        {
          this.j = str1.substring(m + 10);
          continue;
        }
        m = str1.indexOf("Product : ");
        if (m > -1)
        {
          str7 = str1.substring(m + 10);
          continue;
        }
        m = str1.indexOf("Expires : ");
        if (m > -1)
        {
          str5 = str1.substring(m + 10);
          continue;
        }
        localStringBuffer.append(str1);
      }
      String str6 = localStringBuffer.toString();
      ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
      int n = 0;
      while (n < str6.length())
      {
        if ((str6.charAt(n) == '\r') || (str6.charAt(n) == '\n'))
        {
          n++;
          continue;
        }
        localByteArrayOutputStream1.write(Integer.parseInt(str6.substring(n, n + 2), 16));
        n += 2;
      }
      DataInputStream localDataInputStream1 = new DataInputStream(new ByteArrayInputStream(localByteArrayOutputStream1.toByteArray()));
      byte[] arrayOfByte1 = new byte[16];
      localDataInputStream1.readFully(arrayOfByte1);
      byte[] arrayOfByte2 = { 55, -121, 33, 9, 68, 73, 11, -37, -39, -1, 12, 48, 99, 49, 11, 55 };
      for (int i1 = 0; i1 < 16; i1++)
      {
        int tmp636_634 = i1;
        byte[] tmp636_632 = arrayOfByte1;
        tmp636_632[tmp636_634] = (byte)(tmp636_632[tmp636_634] ^ arrayOfByte2[i1]);
      }
      DataInputStream localDataInputStream2 = new DataInputStream(new ByteArrayInputStream(arrayOfByte1));
      long l4 = localDataInputStream2.readLong();
      long l5 = localDataInputStream2.readLong();
      byte[] arrayOfByte3 = new byte[localDataInputStream1.available()];
      localDataInputStream1.readFully(arrayOfByte3);
      this.b = l5;
      for (int i2 = 0; i2 < 23; i2++)
      {
        ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
        _g local_g = new _g(localByteArrayOutputStream2);
        local_g.b(str7);
        local_g.b("3SP Ltd");
        local_g.b(str3);
        local_g.b(str2);
        this.f = (256 << i2);
        local_g.writeInt(this.f);
        local_g.b(this.j);
        local_g.writeLong(l4);
        local_g.writeLong(l5);
        if (l5 > System.currentTimeMillis())
        {
          this.e = str3;
          this.k = str2;
          this.i = str7;
          if (!local_e.b(arrayOfByte3, localByteArrayOutputStream2.toByteArray()))
            continue;
          if (l1 > l4 + l2)
            return this.c = 0x10 | this.f;
          return this.c = 0x4 | this.f;
        }
        return this.c = 0x1 | this.f;
      }
      return this.c = 2;
    }
    catch (Throwable localThrowable)
    {
    }
    return this.c = 2;
  }

  String g()
  {
    return this.e;
  }

  String c()
  {
    return this.k;
  }

  int f()
  {
    return this.c;
  }

  int b()
  {
    return this.f;
  }

  class _f extends c._b
  {
    private final int j = 20;
    private int n;
    private int l;
    private int i;
    private int h;
    private int g;
    private int[] f = new int[80];
    private int q;
    private final int p = 1518500249;
    private final int o = 1859775393;
    private final int m = -1894007588;
    private final int k = -899497514;

    public _f()
    {
      super();
      b();
    }

    public int e()
    {
      return 20;
    }

    protected void b(byte[] paramArrayOfByte, int paramInt)
    {
      this.f[(this.q++)] = ((paramArrayOfByte[paramInt] & 0xFF) << 24 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 3)] & 0xFF);
      if (this.q == 16)
        d();
    }

    private void b(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
    {
      paramArrayOfByte[paramInt2] = (byte)(paramInt1 >>> 24);
      paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >>> 16);
      paramArrayOfByte[(paramInt2 + 2)] = (byte)(paramInt1 >>> 8);
      paramArrayOfByte[(paramInt2 + 3)] = (byte)paramInt1;
    }

    protected void b(long paramLong)
    {
      if (this.q > 14)
        d();
      this.f[14] = (int)(paramLong >>> 32);
      this.f[15] = (int)(paramLong & 0xFFFFFFFF);
    }

    public int c(byte[] paramArrayOfByte, int paramInt)
    {
      c();
      b(this.n, paramArrayOfByte, paramInt);
      b(this.l, paramArrayOfByte, paramInt + 4);
      b(this.i, paramArrayOfByte, paramInt + 8);
      b(this.h, paramArrayOfByte, paramInt + 12);
      b(this.g, paramArrayOfByte, paramInt + 16);
      b();
      return 20;
    }

    public void b()
    {
      super.b();
      this.n = 1732584193;
      this.l = -271733879;
      this.i = -1732584194;
      this.h = 271733878;
      this.g = -1009589776;
      this.q = 0;
      for (int i1 = 0; i1 != this.f.length; i1++)
        this.f[i1] = 0;
    }

    private int c(int paramInt1, int paramInt2, int paramInt3)
    {
      return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
    }

    private int d(int paramInt1, int paramInt2, int paramInt3)
    {
      return paramInt1 ^ paramInt2 ^ paramInt3;
    }

    private int b(int paramInt1, int paramInt2, int paramInt3)
    {
      return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
    }

    private int b(int paramInt1, int paramInt2)
    {
      return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
    }

    protected void d()
    {
      for (int i1 = 16; i1 <= 79; i1++)
        this.f[i1] = b(this.f[(i1 - 3)] ^ this.f[(i1 - 8)] ^ this.f[(i1 - 14)] ^ this.f[(i1 - 16)], 1);
      i1 = this.n;
      int i2 = this.l;
      int i3 = this.i;
      int i4 = this.h;
      int i5 = this.g;
      int i7;
      for (int i6 = 0; i6 <= 19; i6++)
      {
        i7 = b(i1, 5) + c(i2, i3, i4) + i5 + this.f[i6] + 1518500249;
        i5 = i4;
        i4 = i3;
        i3 = b(i2, 30);
        i2 = i1;
        i1 = i7;
      }
      for (i6 = 20; i6 <= 39; i6++)
      {
        i7 = b(i1, 5) + d(i2, i3, i4) + i5 + this.f[i6] + 1859775393;
        i5 = i4;
        i4 = i3;
        i3 = b(i2, 30);
        i2 = i1;
        i1 = i7;
      }
      for (int i6 = 40; i6 <= 59; i6++)
      {
        i7 = b(i1, 5) + b(i2, i3, i4) + i5 + this.f[i6] + -1894007588;
        i5 = i4;
        i4 = i3;
        i3 = b(i2, 30);
        i2 = i1;
        i1 = i7;
      }
      for (int i6 = 60; i6 <= 79; i6++)
      {
        i7 = b(i1, 5) + d(i2, i3, i4) + i5 + this.f[i6] + -899497514;
        i5 = i4;
        i4 = i3;
        i3 = b(i2, 30);
        i2 = i1;
        i1 = i7;
      }
      this.n += i1;
      this.l += i2;
      this.i += i3;
      this.h += i4;
      this.g += i5;
      this.q = 0;
      for (int i6 = 0; i6 != this.f.length; i6++)
        this.f[i6] = 0;
    }
  }

  abstract class _b
  {
    private byte[] c = new byte[4];
    private int d = 0;
    private long e;

    protected _b()
    {
    }

    public void b(byte paramByte)
    {
      this.c[(this.d++)] = paramByte;
      if (this.d == this.c.length)
      {
        b(this.c, 0);
        this.d = 0;
      }
      this.e += 1L;
    }

    public void b(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      while ((this.d != 0) && (paramInt2 > 0))
      {
        b(paramArrayOfByte[paramInt1]);
        paramInt1++;
        paramInt2--;
      }
      while (paramInt2 > this.c.length)
      {
        b(paramArrayOfByte, paramInt1);
        paramInt1 += this.c.length;
        paramInt2 -= this.c.length;
        this.e += this.c.length;
      }
      while (paramInt2 > 0)
      {
        b(paramArrayOfByte[paramInt1]);
        paramInt1++;
        paramInt2--;
      }
    }

    public void c()
    {
      long l = this.e << 3;
      b(-128);
      while (this.d != 0)
        b(0);
      b(l);
      d();
    }

    public void b()
    {
      this.e = 0L;
      this.d = 0;
      for (int i = 0; i < this.c.length; i++)
        this.c[i] = 0;
    }

    protected abstract void b(byte[] paramArrayOfByte, int paramInt);

    protected abstract void b(long paramLong);

    protected abstract void d();
  }

  class _d
  {
    BigInteger e;
    BigInteger c;
    final byte[] d = { 48, 33, 48, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0, 4, 20 };

    public _d(BigInteger paramBigInteger1, BigInteger arg3)
    {
      this.c = paramBigInteger1;
      Object localObject;
      this.e = localObject;
    }

    public BigInteger b(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
    {
      return paramBigInteger1.modPow(paramBigInteger3, paramBigInteger2);
    }

    public BigInteger b(BigInteger paramBigInteger, int paramInt)
      throws IOException
    {
      byte[] arrayOfByte1 = paramBigInteger.toByteArray();
      if (arrayOfByte1[0] != paramInt)
        throw new IOException("PKCS1 padding type " + paramInt + " is not valid");
      for (int i = 1; (i < arrayOfByte1.length) && (arrayOfByte1[i] != 0); i++)
      {
        if ((paramInt != 1) || (arrayOfByte1[i] == -1))
          continue;
        throw new IOException("Corrupt data found in expected PKSC1 padding");
      }
      if (i == arrayOfByte1.length)
        throw new IOException("Corrupt data found in expected PKSC1 padding");
      byte[] arrayOfByte2 = new byte[arrayOfByte1.length - i];
      System.arraycopy(arrayOfByte1, i, arrayOfByte2, 0, arrayOfByte2.length);
      return new BigInteger(1, arrayOfByte2);
    }

    public boolean b(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      try
      {
        BigInteger localBigInteger = new BigInteger(1, paramArrayOfByte1);
        localBigInteger = b(localBigInteger, this.c, this.e);
        localBigInteger = b(localBigInteger, 1);
        paramArrayOfByte1 = localBigInteger.toByteArray();
        c._f local_f = new c._f(c.this);
        local_f.b(paramArrayOfByte2, 0, paramArrayOfByte2.length);
        byte[] arrayOfByte1 = new byte[local_f.e()];
        local_f.c(arrayOfByte1, 0);
        if (arrayOfByte1.length != paramArrayOfByte1.length - this.d.length)
          return false;
        byte[] arrayOfByte2 = this.d;
        int i = 0;
        for (int j = 0; i < paramArrayOfByte1.length; j++)
        {
          if (i == this.d.length)
          {
            arrayOfByte2 = arrayOfByte1;
            j = 0;
          }
          if (paramArrayOfByte1[i] != arrayOfByte2[j])
            return false;
          i++;
        }
        return true;
      }
      catch (IOException localIOException)
      {
      }
      return false;
    }
  }

  static class _c
  {
    InputStream b;

    _c(InputStream paramInputStream)
    {
      this.b = paramInputStream;
    }

    String b()
      throws IOException
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int i;
      while (((i = this.b.read()) > -1) && (i != 10))
        localStringBuffer.append((char)i);
      if ((i == -1) && (localStringBuffer.length() == 0))
        return null;
      return new String(localStringBuffer.toString().getBytes("UTF8"), "UTF8").trim();
    }
  }

  static class _g extends DataOutputStream
  {
    _g(OutputStream paramOutputStream)
    {
      super();
    }

    void b(String paramString)
      throws IOException
    {
      byte[] arrayOfByte = paramString.getBytes("UTF8");
      writeInt(arrayOfByte.length);
      write(arrayOfByte);
    }
  }

  class _e extends c._d
  {
    _e(BigInteger paramBigInteger1, BigInteger arg3)
    {
      super(paramBigInteger1, localBigInteger);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.c
 * JD-Core Version:    0.6.0
 */