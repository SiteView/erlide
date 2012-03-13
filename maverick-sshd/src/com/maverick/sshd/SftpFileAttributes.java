package com.maverick.sshd;

import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.IOException;

public class SftpFileAttributes
{
  public static final int S_IFMT = 61440;
  public static final int S_IFSOCK = 49152;
  public static final int S_IFLNK = 40960;
  public static final int S_IFREG = 32768;
  public static final int S_IFBLK = 24576;
  public static final int S_IFDIR = 16384;
  public static final int S_IFCHR = 8192;
  public static final int S_IFIFO = 4096;
  public static final int S_ISUID = 2048;
  public static final int S_ISGID = 1024;
  public static final int S_IRUSR = 256;
  public static final int S_IWUSR = 128;
  public static final int S_IXUSR = 64;
  public static final int S_IRGRP = 32;
  public static final int S_IWGRP = 16;
  public static final int S_IXGRP = 8;
  public static final int S_IROTH = 4;
  public static final int S_IWOTH = 2;
  public static final int S_IXOTH = 1;
  int C = 3;
  long D = 0L;
  UnsignedInteger64 L = null;
  UnsignedInteger32 E = null;
  UnsignedInteger32 K = null;
  UnsignedInteger32 H = null;
  UnsignedInteger32 A = null;
  UnsignedInteger32 G = null;
  UnsignedInteger32 J = null;
  String B;
  String F;
  char[] I = { 'p', 'c', 'd', 'b', '-', 'l', 's' };

  public SftpFileAttributes()
  {
  }

  public SftpFileAttributes(ByteArrayReader paramByteArrayReader)
    throws IOException
  {
    this.D = paramByteArrayReader.readInt();
    if (isFlagSet(1))
    {
      byte[] arrayOfByte = new byte[8];
      paramByteArrayReader.read(arrayOfByte);
      this.L = new UnsignedInteger64(arrayOfByte);
    }
    if (isFlagSet(2))
    {
      this.E = new UnsignedInteger32(paramByteArrayReader.readInt());
      this.K = new UnsignedInteger32(paramByteArrayReader.readInt());
    }
    if (isFlagSet(4))
      this.H = new UnsignedInteger32(paramByteArrayReader.readInt());
    if (isFlagSet(8))
    {
      this.A = new UnsignedInteger32(paramByteArrayReader.readInt());
      this.J = new UnsignedInteger32(paramByteArrayReader.readInt());
    }
  }

  public UnsignedInteger32 getUID()
  {
    if (this.E != null)
      return this.E;
    return new UnsignedInteger32(0L);
  }

  public boolean hasUID()
  {
    return this.E != null;
  }

  public boolean hasGID()
  {
    return this.K != null;
  }

  public void setUID(UnsignedInteger32 paramUnsignedInteger32)
  {
    this.D |= 2L;
    this.E = paramUnsignedInteger32;
  }

  public void setUID(UnsignedInteger32 paramUnsignedInteger32, String paramString)
  {
    this.D |= 2L;
    this.E = paramUnsignedInteger32;
    this.B = paramString;
  }

  public String getUsername()
  {
    if (this.B == null)
      return getUID().toString();
    return this.B;
  }

  public void setGID(UnsignedInteger32 paramUnsignedInteger32)
  {
    this.D |= 2L;
    this.K = paramUnsignedInteger32;
  }

  public void setGID(UnsignedInteger32 paramUnsignedInteger32, String paramString)
  {
    this.D |= 2L;
    this.K = paramUnsignedInteger32;
    this.F = paramString;
  }

  public String getGroup()
  {
    if (this.F == null)
      return getGID().toString();
    return this.F;
  }

  public UnsignedInteger32 getGID()
  {
    if (this.K != null)
      return this.K;
    return new UnsignedInteger32(0L);
  }

  public void setSize(UnsignedInteger64 paramUnsignedInteger64)
  {
    this.L = paramUnsignedInteger64;
    if (paramUnsignedInteger64 != null)
      this.D |= 1L;
    else
      this.D ^= 1L;
  }

  public UnsignedInteger64 getSize()
  {
    if (this.L != null)
      return this.L;
    return new UnsignedInteger64("0");
  }

  public boolean hasSize()
  {
    return this.L != null;
  }

  public void setPermissions(UnsignedInteger32 paramUnsignedInteger32)
  {
    this.H = paramUnsignedInteger32;
    if (paramUnsignedInteger32 != null)
      this.D |= 4L;
    else
      this.D ^= 4L;
  }

  public void setPermissionsFromMaskString(String paramString)
  {
    if (paramString.length() != 4)
      throw new IllegalArgumentException("Mask length must be 4");
    try
    {
      setPermissions(new UnsignedInteger32(String.valueOf(Integer.parseInt(paramString, 8))));
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new IllegalArgumentException("Mask must be 4 digit octal number.");
    }
  }

  public void setPermissionsFromUmaskString(String paramString)
  {
    if (paramString.length() != 4)
      throw new IllegalArgumentException("umask length must be 4");
    try
    {
      setPermissions(new UnsignedInteger32(String.valueOf(Integer.parseInt(paramString, 8) ^ 0x1FF)));
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new IllegalArgumentException("umask must be 4 digit octal number");
    }
  }

  public void setPermissions(String paramString)
  {
    int i = 0;
    if (this.H != null)
    {
      i |= ((this.H.longValue() & 0xF000) == 61440L ? 61440 : 0);
      i |= ((this.H.longValue() & 0xC000) == 49152L ? 49152 : 0);
      i |= ((this.H.longValue() & 0xA000) == 40960L ? 40960 : 0);
      i |= ((this.H.longValue() & 0x8000) == 32768L ? 32768 : 0);
      i |= ((this.H.longValue() & 0x6000) == 24576L ? 24576 : 0);
      i |= ((this.H.longValue() & 0x4000) == 16384L ? 16384 : 0);
      i |= ((this.H.longValue() & 0x2000) == 8192L ? 8192 : 0);
      i |= ((this.H.longValue() & 0x1000) == 4096L ? 4096 : 0);
      i |= ((this.H.longValue() & 0x800) == 2048L ? 2048 : 0);
      i |= ((this.H.longValue() & 0x400) == 1024L ? 1024 : 0);
    }
    int j = paramString.length();
    if (j >= 1)
      i |= (paramString.charAt(0) == 'r' ? 256 : 0);
    if (j >= 2)
      i |= (paramString.charAt(1) == 'w' ? 128 : 0);
    if (j >= 3)
      i |= (paramString.charAt(2) == 'x' ? 64 : 0);
    if (j >= 4)
      i |= (paramString.charAt(3) == 'r' ? 32 : 0);
    if (j >= 5)
      i |= (paramString.charAt(4) == 'w' ? 16 : 0);
    if (j >= 6)
      i |= (paramString.charAt(5) == 'x' ? 8 : 0);
    if (j >= 7)
      i |= (paramString.charAt(6) == 'r' ? 4 : 0);
    if (j >= 8)
      i |= (paramString.charAt(7) == 'w' ? 2 : 0);
    if (j >= 9)
      i |= (paramString.charAt(8) == 'x' ? 1 : 0);
    setPermissions(new UnsignedInteger32(i));
  }

  public UnsignedInteger32 getPermissions()
  {
    return this.H;
  }

  public void setTimes(UnsignedInteger32 paramUnsignedInteger321, UnsignedInteger32 paramUnsignedInteger322)
  {
    this.A = paramUnsignedInteger321;
    this.J = paramUnsignedInteger322;
    if (paramUnsignedInteger321 != null)
      this.D |= 8L;
    else
      this.D ^= 8L;
  }

  public UnsignedInteger32 getAccessedTime()
  {
    return this.A;
  }

  public UnsignedInteger32 getModifiedTime()
  {
    if (this.J != null)
      return this.J;
    return new UnsignedInteger32(0L);
  }

  public boolean isFlagSet(int paramInt)
  {
    return (this.D & paramInt) == paramInt;
  }

  public byte[] toByteArray()
    throws IOException
  {
    ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
    localByteArrayWriter.writeInt(this.D);
    if (isFlagSet(1))
      localByteArrayWriter.write(this.L.toByteArray());
    if (isFlagSet(2))
    {
      if (this.E != null)
        localByteArrayWriter.writeInt(this.E.longValue());
      else
        localByteArrayWriter.writeInt(0);
      if (this.K != null)
        localByteArrayWriter.writeInt(this.K.longValue());
      else
        localByteArrayWriter.writeInt(0);
    }
    if (isFlagSet(4))
      localByteArrayWriter.writeInt(this.H.longValue());
    if (isFlagSet(8))
    {
      localByteArrayWriter.writeInt(this.A.longValue());
      localByteArrayWriter.writeInt(this.J.longValue());
    }
    return localByteArrayWriter.toByteArray();
  }

  private int A(int paramInt1, int paramInt2)
  {
    paramInt1 >>>= paramInt2;
    return ((paramInt1 & 0x4) != 0 ? 4 : 0) + ((paramInt1 & 0x2) != 0 ? 2 : 0) + ((paramInt1 & 0x1) != 0 ? 1 : 0);
  }

  private String B(int paramInt1, int paramInt2)
  {
    paramInt1 >>>= paramInt2;
    String str = ((paramInt1 & 0x4) != 0 ? "r" : "-") + ((paramInt1 & 0x2) != 0 ? "w" : "-");
    if (((paramInt2 == 6) && ((this.H.longValue() & 0x800) == 2048L)) || ((paramInt2 == 3) && ((this.H.longValue() & 0x400) == 1024L)))
      str = str + ((paramInt1 & 0x1) != 0 ? "s" : "S");
    else
      str = str + ((paramInt1 & 0x1) != 0 ? "x" : "-");
    return str;
  }

  public String getPermissionsString()
  {
    if (this.H != null)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append(this.I[(((int)this.H.longValue() & 0xF000) >>> 13)]);
      localStringBuffer.append(B((int)this.H.longValue(), 6));
      localStringBuffer.append(B((int)this.H.longValue(), 3));
      localStringBuffer.append(B((int)this.H.longValue(), 0));
      return localStringBuffer.toString();
    }
    return "";
  }

  public String getMaskString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append('0');
    int i = (int)this.H.longValue();
    localStringBuffer.append(A(i, 6));
    localStringBuffer.append(A(i, 3));
    localStringBuffer.append(A(i, 0));
    return localStringBuffer.toString();
  }

  public boolean isDirectory()
  {
    return (this.H != null) && ((this.H.longValue() & 0x4000) == 16384L);
  }

  public boolean isFile()
  {
    return (this.H != null) && ((this.H.longValue() & 0x8000) == 32768L);
  }

  public boolean isLink()
  {
    return (this.H != null) && ((this.H.longValue() & 0xA000) == 40960L);
  }

  public boolean isFifo()
  {
    return (this.H != null) && ((this.H.longValue() & 0x1000) == 4096L);
  }

  public boolean isBlock()
  {
    return (this.H != null) && ((this.H.longValue() & 0x6000) == 24576L);
  }

  public boolean isCharacter()
  {
    return (this.H != null) && ((this.H.longValue() & 0x2000) == 8192L);
  }

  public boolean isSocket()
  {
    return (this.H != null) && ((this.H.longValue() & 0xC000) == 49152L);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.SftpFileAttributes
 * JD-Core Version:    0.6.0
 */