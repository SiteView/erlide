package com.maverick.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

class C
{
  String I;
  String C;
  String D;
  String H;
  long A;
  String G;
  int B = 8;
  BigInteger F = new BigInteger(new byte[] { 0, -80, 95, -109, -57, 84, 20, 126, -63, 121, -113, 32, -105, 104, -105, 9, -53, -24, -73, 48, -114, 97, 111, 5, -85, -84, 64, -6, 0, 2, -103, -64, 124, -63, 94, 46, -104, -82, 35, 30, 8, -104, -40, 94, -86, -93, -53, -45, 93, -29, -20, 25, -14, 96, -125, 9, -9, -109, -33, 32, -5, 38, 65, 99, 31, 41, -108, -26, -111, -35, -39, -1, 43, 9, 112, 75, -61, -95, -88, 66, -96, 92, 122, -73, -45, 114, -41, 106, -123, 35, -55, -92, -44, 27, -13, 96, 24, 118, 90, 112, -80, -72, -109, -7, -33, -53, -111, 20, -26, -38, 41, 11, -37, 109, 58, 4, -105, 87, 101, -22, 9, 78, -37, -88, 107, -113, 84, 88, 105 });
  BigInteger E = new BigInteger(new byte[] { 1, 0, 1 });

  final void A(String paramString)
  {
    this.C = paramString;
  }

  final int A()
  {
    try
    {
      long l1 = 1316539815622L;
      long l2 = 31708800000L;
      long l3 = 1209600000L;
      if ((this.C == null) || (this.C.equals("")))
      {
        if ((System.currentTimeMillis() >= l1) && (System.currentTimeMillis() <= l1 + l3))
          return this.B = 4;
        return this.B = 8;
      }
      _D local_D = new _D(this.F, this.E);
      _B local_B = new _B(new ByteArrayInputStream(this.C.getBytes()));
      String str1 = local_B.A();
      if ((!str1.equals("----BEGIN 3SP LICENSE----")) && (!str1.equals("----BEGIN SSHTOOLS LICENSE----")))
        return this.B = 2;
      String str2 = "";
      String str3 = "";
      String str4 = null;
      String str5 = null;
      String str6 = "";
      String str7 = "";
      while (((str1 = local_B.A()) != null) && (!str1.equals("----END 3SP LICENSE----")) && (!str1.equals("----END SSHTOOLS LICENSE----")))
      {
        int i = str1.indexOf("Licensee: ");
        if (i > -1)
        {
          str2 = str1.substring(i + 10);
          continue;
        }
        i = str1.indexOf("Comments: ");
        if (i > -1)
        {
          str3 = str1.substring(i + 10);
          continue;
        }
        i = str1.indexOf("Created : ");
        if (i > -1)
        {
          str4 = str1.substring(i + 10);
          continue;
        }
        i = str1.indexOf("Type    : ");
        if (i > -1)
        {
          this.H = str1.substring(i + 10);
          continue;
        }
        i = str1.indexOf("Product : ");
        if (i > -1)
        {
          str7 = str1.substring(i + 10);
          continue;
        }
        i = str1.indexOf("Expires : ");
        if (i > -1)
        {
          str5 = str1.substring(i + 10);
          continue;
        }
        str6 = str6 + str1;
      }
      ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
      int j = 0;
      while (j < str6.length())
      {
        if ((str6.charAt(j) == '\r') || (str6.charAt(j) == '\n'))
        {
          j++;
          continue;
        }
        localByteArrayOutputStream1.write(Integer.parseInt(str6.substring(j, j + 2), 16));
        j += 2;
      }
      DataInputStream localDataInputStream1 = new DataInputStream(new ByteArrayInputStream(localByteArrayOutputStream1.toByteArray()));
      byte[] arrayOfByte1 = new byte[16];
      localDataInputStream1.readFully(arrayOfByte1);
      byte[] arrayOfByte2 = { 55, -121, 33, 9, 68, 73, 11, -37, -39, -1, 12, 48, 99, 49, 11, 55 };
      for (int k = 0; k < 16; k++)
      {
        int tmp627_625 = k;
        byte[] tmp627_623 = arrayOfByte1;
        tmp627_623[tmp627_625] = (byte)(tmp627_623[tmp627_625] ^ arrayOfByte2[k]);
      }
      DataInputStream localDataInputStream2 = new DataInputStream(new ByteArrayInputStream(arrayOfByte1));
      long l4 = localDataInputStream2.readLong();
      long l5 = localDataInputStream2.readLong();
      byte[] arrayOfByte3 = new byte[localDataInputStream1.available()];
      localDataInputStream1.readFully(arrayOfByte3);
      this.A = l5;
      for (int m = 0; m < 8; m++)
      {
        ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
        _F local_F = new _F(localByteArrayOutputStream2);
        local_F.A(str7);
        local_F.A("3SP Ltd");
        local_F.A(str3);
        local_F.A(str2);
        int n = 256 << m;
        local_F.writeInt(n);
        local_F.A(this.H);
        local_F.writeLong(l4);
        local_F.writeLong(l5);
        if (l5 > System.currentTimeMillis())
        {
          this.D = str3;
          this.I = str2;
          this.G = str7;
          if (!local_D.A(arrayOfByte3, localByteArrayOutputStream2.toByteArray()))
            continue;
          if (l1 > l4 + l2)
            return this.B = 0x10 | n;
          return this.B = 0x4 | n;
        }
        return this.B = 0x1 | n;
      }
      return this.B = 2;
    }
    catch (Throwable localThrowable)
    {
    }
    return this.B = 2;
  }

  class _E extends C._A
  {
    private final int H = 20;
    private int L;
    private int J;
    private int G;
    private int F;
    private int E;
    private int[] D = new int[80];
    private int O;
    private final int N = 1518500249;
    private final int M = 1859775393;
    private final int K = -1894007588;
    private final int I = -899497514;

    public _E()
    {
      super();
      A();
    }

    public int D()
    {
      return 20;
    }

    protected void A(byte[] paramArrayOfByte, int paramInt)
    {
      this.D[(this.O++)] = ((paramArrayOfByte[paramInt] & 0xFF) << 24 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 3)] & 0xFF);
      if (this.O == 16)
        C();
    }

    private void A(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
    {
      paramArrayOfByte[paramInt2] = (byte)(paramInt1 >>> 24);
      paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >>> 16);
      paramArrayOfByte[(paramInt2 + 2)] = (byte)(paramInt1 >>> 8);
      paramArrayOfByte[(paramInt2 + 3)] = (byte)paramInt1;
    }

    protected void A(long paramLong)
    {
      if (this.O > 14)
        C();
      this.D[14] = (int)(paramLong >>> 32);
      this.D[15] = (int)(paramLong & 0xFFFFFFFF);
    }

    public int B(byte[] paramArrayOfByte, int paramInt)
    {
      B();
      A(this.L, paramArrayOfByte, paramInt);
      A(this.J, paramArrayOfByte, paramInt + 4);
      A(this.G, paramArrayOfByte, paramInt + 8);
      A(this.F, paramArrayOfByte, paramInt + 12);
      A(this.E, paramArrayOfByte, paramInt + 16);
      A();
      return 20;
    }

    public void A()
    {
      super.A();
      this.L = 1732584193;
      this.J = -271733879;
      this.G = -1732584194;
      this.F = 271733878;
      this.E = -1009589776;
      this.O = 0;
      for (int i = 0; i != this.D.length; i++)
        this.D[i] = 0;
    }

    private int B(int paramInt1, int paramInt2, int paramInt3)
    {
      return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
    }

    private int C(int paramInt1, int paramInt2, int paramInt3)
    {
      return paramInt1 ^ paramInt2 ^ paramInt3;
    }

    private int A(int paramInt1, int paramInt2, int paramInt3)
    {
      return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
    }

    private int A(int paramInt1, int paramInt2)
    {
      return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
    }

    protected void C()
    {
      for (int i = 16; i <= 79; i++)
        this.D[i] = A(this.D[(i - 3)] ^ this.D[(i - 8)] ^ this.D[(i - 14)] ^ this.D[(i - 16)], 1);
      i = this.L;
      int j = this.J;
      int k = this.G;
      int m = this.F;
      int n = this.E;
      int i2;
      for (int i1 = 0; i1 <= 19; i1++)
      {
        i2 = A(i, 5) + B(j, k, m) + n + this.D[i1] + 1518500249;
        n = m;
        m = k;
        k = A(j, 30);
        j = i;
        i = i2;
      }
      for (i1 = 20; i1 <= 39; i1++)
      {
        i2 = A(i, 5) + C(j, k, m) + n + this.D[i1] + 1859775393;
        n = m;
        m = k;
        k = A(j, 30);
        j = i;
        i = i2;
      }
      for (i1 = 40; i1 <= 59; i1++)
      {
        i2 = A(i, 5) + A(j, k, m) + n + this.D[i1] + -1894007588;
        n = m;
        m = k;
        k = A(j, 30);
        j = i;
        i = i2;
      }
      for (i1 = 60; i1 <= 79; i1++)
      {
        i2 = A(i, 5) + C(j, k, m) + n + this.D[i1] + -899497514;
        n = m;
        m = k;
        k = A(j, 30);
        j = i;
        i = i2;
      }
      this.L += i;
      this.J += j;
      this.G += k;
      this.F += m;
      this.E += n;
      this.O = 0;
      for (i1 = 0; i1 != this.D.length; i1++)
        this.D[i1] = 0;
    }
  }

  abstract class _A
  {
    private byte[] A = new byte[4];
    private int B = 0;
    private long C;

    protected _A()
    {
    }

    public void A(byte paramByte)
    {
      this.A[(this.B++)] = paramByte;
      if (this.B == this.A.length)
      {
        A(this.A, 0);
        this.B = 0;
      }
      this.C += 1L;
    }

    public void A(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      while ((this.B != 0) && (paramInt2 > 0))
      {
        A(paramArrayOfByte[paramInt1]);
        paramInt1++;
        paramInt2--;
      }
      while (paramInt2 > this.A.length)
      {
        A(paramArrayOfByte, paramInt1);
        paramInt1 += this.A.length;
        paramInt2 -= this.A.length;
        this.C += this.A.length;
      }
      while (paramInt2 > 0)
      {
        A(paramArrayOfByte[paramInt1]);
        paramInt1++;
        paramInt2--;
      }
    }

    public void B()
    {
      long l = this.C << 3;
      A(-128);
      while (this.B != 0)
        A(0);
      A(l);
      C();
    }

    public void A()
    {
      this.C = 0L;
      this.B = 0;
      for (int i = 0; i < this.A.length; i++)
        this.A[i] = 0;
    }

    protected abstract void A(byte[] paramArrayOfByte, int paramInt);

    protected abstract void A(long paramLong);

    protected abstract void C();
  }

  class _C
  {
    BigInteger C;
    BigInteger A;
    final byte[] B = { 48, 33, 48, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0, 4, 20 };

    public _C(BigInteger paramBigInteger1, BigInteger arg3)
    {
      this.A = paramBigInteger1;
      Object localObject;
      this.C = localObject;
    }

    public BigInteger A(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
    {
      return paramBigInteger1.modPow(paramBigInteger3, paramBigInteger2);
    }

    public BigInteger A(BigInteger paramBigInteger, int paramInt)
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

    public boolean A(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      try
      {
        BigInteger localBigInteger = new BigInteger(1, paramArrayOfByte1);
        localBigInteger = A(localBigInteger, this.A, this.C);
        localBigInteger = A(localBigInteger, 1);
        paramArrayOfByte1 = localBigInteger.toByteArray();
        C._E local_E = new C._E(C.this);
        local_E.A(paramArrayOfByte2, 0, paramArrayOfByte2.length);
        byte[] arrayOfByte1 = new byte[local_E.D()];
        local_E.B(arrayOfByte1, 0);
        if (arrayOfByte1.length != paramArrayOfByte1.length - this.B.length)
          return false;
        byte[] arrayOfByte2 = this.B;
        int i = 0;
        for (int j = 0; i < paramArrayOfByte1.length; j++)
        {
          if (i == this.B.length)
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

  class _B
  {
    InputStream A;

    _B(InputStream arg2)
    {
      Object localObject;
      this.A = localObject;
    }

    String A()
      throws IOException
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int i;
      while (((i = this.A.read()) > -1) && (i != 10))
        localStringBuffer.append((char)i);
      if ((i == -1) && (localStringBuffer.length() == 0))
        return null;
      return new String(localStringBuffer.toString().getBytes("UTF8"), "UTF8").trim();
    }
  }

  class _F extends DataOutputStream
  {
    _F(OutputStream arg2)
    {
      super();
    }

    void A(String paramString)
      throws IOException
    {
      byte[] arrayOfByte = paramString.getBytes("UTF8");
      writeInt(arrayOfByte.length);
      write(arrayOfByte);
    }
  }

  class _D extends C._C
  {
    _D(BigInteger paramBigInteger1, BigInteger arg3)
    {
      super(paramBigInteger1, localBigInteger);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.C
 * JD-Core Version:    0.6.0
 */