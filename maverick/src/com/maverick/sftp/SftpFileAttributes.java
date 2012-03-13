package com.maverick.sftp;

import B;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class SftpFileAttributes
{
  public static final int SSH_FILEXFER_TYPE_REGULAR = 1;
  public static final int SSH_FILEXFER_TYPE_DIRECTORY = 2;
  public static final int SSH_FILEXFER_TYPE_SYMLINK = 3;
  public static final int SSH_FILEXFER_TYPE_SPECIAL = 4;
  public static final int SSH_FILEXFER_TYPE_UNKNOWN = 5;
  private Vector q = new Vector();
  private Hashtable m = new Hashtable();
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
  int r = 3;
  long j = 0L;
  int t;
  UnsignedInteger64 d = null;
  String c = null;
  String o = null;
  UnsignedInteger32 e = null;
  UnsignedInteger64 f = null;
  UnsignedInteger32 b = null;
  UnsignedInteger64 h = null;
  UnsignedInteger32 g = null;
  UnsignedInteger64 s = null;
  UnsignedInteger32 l = null;
  String n;
  String k;
  char[] p = { 'p', 'c', 'd', 'b', '-', 'l', 's' };
  SftpSubsystemChannel i;

  public SftpFileAttributes(SftpSubsystemChannel paramSftpSubsystemChannel, int paramInt)
  {
    this.i = paramSftpSubsystemChannel;
    this.r = paramSftpSubsystemChannel.getVersion();
    this.t = paramInt;
  }

  public int getType()
  {
    return this.t;
  }

  public SftpFileAttributes(SftpSubsystemChannel paramSftpSubsystemChannel, ByteArrayReader paramByteArrayReader)
    throws IOException
  {
    this.i = paramSftpSubsystemChannel;
    this.r = paramSftpSubsystemChannel.getVersion();
    if (paramByteArrayReader.available() >= 4)
      this.j = paramByteArrayReader.readInt();
    if ((this.r > 3) && (paramByteArrayReader.available() > 0))
      this.t = paramByteArrayReader.read();
    if ((isFlagSet(1L)) && (paramByteArrayReader.available() >= 8))
    {
      byte[] arrayOfByte = new byte[8];
      paramByteArrayReader.read(arrayOfByte);
      this.d = new UnsignedInteger64(arrayOfByte);
    }
    if ((this.r <= 3) && (isFlagSet(2L)) && (paramByteArrayReader.available() >= 8))
    {
      this.c = String.valueOf(paramByteArrayReader.readInt());
      this.o = String.valueOf(paramByteArrayReader.readInt());
    }
    else if ((this.r > 3) && (isFlagSet(128L)) && (paramByteArrayReader.available() > 0))
    {
      this.c = paramByteArrayReader.readString(paramSftpSubsystemChannel.getCharsetEncoding());
      this.o = paramByteArrayReader.readString(paramSftpSubsystemChannel.getCharsetEncoding());
    }
    if ((isFlagSet(4L)) && (paramByteArrayReader.available() >= 4))
      this.e = new UnsignedInteger32(paramByteArrayReader.readInt());
    if ((this.r <= 3) && (isFlagSet(8L)) && (paramByteArrayReader.available() >= 8))
    {
      this.f = new UnsignedInteger64(paramByteArrayReader.readInt());
      this.s = new UnsignedInteger64(paramByteArrayReader.readInt());
    }
    else if ((this.r > 3) && (paramByteArrayReader.available() > 0))
    {
      if ((isFlagSet(8L)) && (paramByteArrayReader.available() >= 8))
        this.f = paramByteArrayReader.readUINT64();
      if ((isFlagSet(256L)) && (paramByteArrayReader.available() >= 4))
        this.b = paramByteArrayReader.readUINT32();
    }
    if ((this.r > 3) && (paramByteArrayReader.available() > 0))
    {
      if ((isFlagSet(16L)) && (paramByteArrayReader.available() >= 8))
        this.h = paramByteArrayReader.readUINT64();
      if ((isFlagSet(256L)) && (paramByteArrayReader.available() >= 4))
        this.g = paramByteArrayReader.readUINT32();
    }
    if ((this.r > 3) && (paramByteArrayReader.available() > 0))
    {
      if ((isFlagSet(32L)) && (paramByteArrayReader.available() >= 8))
        this.s = paramByteArrayReader.readUINT64();
      if ((isFlagSet(256L)) && (paramByteArrayReader.available() >= 4))
        this.l = paramByteArrayReader.readUINT32();
    }
    int i1;
    int i2;
    if ((this.r > 3) && (isFlagSet(64L)) && (paramByteArrayReader.available() > 0))
    {
      i1 = (int)paramByteArrayReader.readInt();
      i2 = (int)paramByteArrayReader.readInt();
      for (int i3 = 0; i3 < i2; i3++)
        this.q.addElement(new ACL((int)paramByteArrayReader.readInt(), (int)paramByteArrayReader.readInt(), (int)paramByteArrayReader.readInt(), paramByteArrayReader.readString()));
    }
    if ((this.r >= 3) && (isFlagSet(-2147483648L)) && (paramByteArrayReader.available() > 0))
    {
      i1 = (int)paramByteArrayReader.readInt();
      for (i2 = 0; i2 < i1; i2++)
        this.m.put(paramByteArrayReader.readString(), paramByteArrayReader.readBinaryString());
    }
  }

  public String getUID()
  {
    if (this.n != null)
      return this.n;
    if (this.c != null)
      return this.c;
    return "";
  }

  public void setUID(String paramString)
  {
    if (this.r > 3)
      this.j |= 128L;
    else
      this.j |= 2L;
    this.c = paramString;
  }

  public void setGID(String paramString)
  {
    if (this.r > 3)
      this.j |= 128L;
    else
      this.j |= 2L;
    this.o = paramString;
  }

  public String getGID()
  {
    if (this.k != null)
      return this.k;
    if (this.o != null)
      return this.o;
    return "";
  }

  public boolean hasUID()
  {
    return this.c != null;
  }

  public boolean hasGID()
  {
    return this.o != null;
  }

  public void setSize(UnsignedInteger64 paramUnsignedInteger64)
  {
    this.d = paramUnsignedInteger64;
    if (paramUnsignedInteger64 != null)
      this.j |= 1L;
    else
      this.j ^= 1L;
  }

  public UnsignedInteger64 getSize()
  {
    if (this.d != null)
      return this.d;
    return new UnsignedInteger64("0");
  }

  public boolean hasSize()
  {
    return this.d != null;
  }

  public void setPermissions(UnsignedInteger32 paramUnsignedInteger32)
  {
    this.e = paramUnsignedInteger32;
    if (paramUnsignedInteger32 != null)
      this.j |= 4L;
    else
      this.j ^= 4L;
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
    int i1 = 0;
    if (this.e != null)
    {
      i1 |= ((this.e.longValue() & 0xF000) == 61440L ? 61440 : 0);
      i1 |= ((this.e.longValue() & 0xC000) == 49152L ? 49152 : 0);
      i1 |= ((this.e.longValue() & 0xA000) == 40960L ? 40960 : 0);
      i1 |= ((this.e.longValue() & 0x8000) == 32768L ? 32768 : 0);
      i1 |= ((this.e.longValue() & 0x6000) == 24576L ? 24576 : 0);
      i1 |= ((this.e.longValue() & 0x4000) == 16384L ? 16384 : 0);
      i1 |= ((this.e.longValue() & 0x2000) == 8192L ? 8192 : 0);
      i1 |= ((this.e.longValue() & 0x1000) == 4096L ? 4096 : 0);
      i1 |= ((this.e.longValue() & 0x800) == 2048L ? 2048 : 0);
      i1 |= ((this.e.longValue() & 0x400) == 1024L ? 1024 : 0);
    }
    int i2 = paramString.length();
    if (i2 >= 1)
      i1 |= (paramString.charAt(0) == 'r' ? 256 : 0);
    if (i2 >= 2)
      i1 |= (paramString.charAt(1) == 'w' ? 128 : 0);
    if (i2 >= 3)
      i1 |= (paramString.charAt(2) == 'x' ? 64 : 0);
    if (i2 >= 4)
      i1 |= (paramString.charAt(3) == 'r' ? 32 : 0);
    if (i2 >= 5)
      i1 |= (paramString.charAt(4) == 'w' ? 16 : 0);
    if (i2 >= 6)
      i1 |= (paramString.charAt(5) == 'x' ? 8 : 0);
    if (i2 >= 7)
      i1 |= (paramString.charAt(6) == 'r' ? 4 : 0);
    if (i2 >= 8)
      i1 |= (paramString.charAt(7) == 'w' ? 2 : 0);
    if (i2 >= 9)
      i1 |= (paramString.charAt(8) == 'x' ? 1 : 0);
    setPermissions(new UnsignedInteger32(i1));
  }

  public UnsignedInteger32 getPermissions()
  {
    if (this.e != null)
      return this.e;
    return new UnsignedInteger32(0L);
  }

  public void setTimes(UnsignedInteger64 paramUnsignedInteger641, UnsignedInteger64 paramUnsignedInteger642)
  {
    this.f = paramUnsignedInteger641;
    this.s = paramUnsignedInteger642;
    if (paramUnsignedInteger641 != null)
      this.j |= 8L;
    else
      this.j ^= 8L;
  }

  public UnsignedInteger64 getAccessedTime()
  {
    return this.f;
  }

  public UnsignedInteger64 getModifiedTime()
  {
    if (this.s != null)
      return this.s;
    return new UnsignedInteger64(0L);
  }

  public Date getModifiedDateTime()
  {
    long l1 = 0L;
    if (this.s != null)
      l1 = this.s.longValue() * 1000L;
    if (this.l != null)
      l1 += this.l.longValue() / 1000000L;
    return new Date(l1);
  }

  public Date getCreationDateTime()
  {
    long l1 = 0L;
    if (this.h != null)
      l1 = this.h.longValue() * 1000L;
    if (this.g != null)
      l1 += this.g.longValue() / 1000000L;
    return new Date(l1);
  }

  public Date getAccessedDateTime()
  {
    long l1 = 0L;
    if (this.f != null)
      l1 = this.f.longValue() * 1000L;
    if (this.f != null)
      l1 += this.b.longValue() / 1000000L;
    return new Date(l1);
  }

  public UnsignedInteger64 getCreationTime()
  {
    if (this.h != null)
      return this.h;
    return new UnsignedInteger64(0L);
  }

  public boolean isFlagSet(long paramLong)
  {
    return (this.j & (paramLong & 0xFFFFFFFF)) == (paramLong & 0xFFFFFFFF);
  }

  public byte[] toByteArray()
    throws IOException
  {
    ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
    localByteArrayWriter.writeInt(this.j);
    if (this.r > 3)
      localByteArrayWriter.write(this.t);
    if (isFlagSet(1L))
      localByteArrayWriter.write(this.d.toByteArray());
    if ((this.r <= 3) && (isFlagSet(2L)))
    {
      if (this.c != null)
        try
        {
          localByteArrayWriter.writeInt(Long.parseLong(this.c));
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          localByteArrayWriter.writeInt(0);
        }
      else
        localByteArrayWriter.writeInt(0);
      if (this.o != null)
        try
        {
          localByteArrayWriter.writeInt(Long.parseLong(this.o));
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          localByteArrayWriter.writeInt(0);
        }
      else
        localByteArrayWriter.writeInt(0);
    }
    else if ((this.r > 3) && (isFlagSet(128L)))
    {
      if (this.c != null)
        localByteArrayWriter.writeString(this.c, this.i.getCharsetEncoding());
      else
        localByteArrayWriter.writeString("");
      if (this.o != null)
        localByteArrayWriter.writeString(this.o, this.i.getCharsetEncoding());
      else
        localByteArrayWriter.writeString("");
    }
    if (isFlagSet(4L))
      localByteArrayWriter.writeInt(this.e.longValue());
    if ((this.r <= 3) && (isFlagSet(8L)))
    {
      localByteArrayWriter.writeInt(this.f.longValue());
      localByteArrayWriter.writeInt(this.s.longValue());
    }
    else if (this.r > 3)
    {
      if (isFlagSet(8L))
        localByteArrayWriter.writeUINT64(this.f);
      if (isFlagSet(256L))
        localByteArrayWriter.writeUINT32(this.b);
      if (isFlagSet(16L))
        localByteArrayWriter.writeUINT64(this.h);
      if (isFlagSet(256L))
        localByteArrayWriter.writeUINT32(this.g);
      if (isFlagSet(32L))
        localByteArrayWriter.writeUINT64(this.s);
      if (isFlagSet(256L))
        localByteArrayWriter.writeUINT32(this.l);
    }
    Object localObject1;
    Object localObject2;
    if (isFlagSet(64L))
    {
      localObject1 = new ByteArrayWriter();
      localObject2 = this.q.elements();
      ((ByteArrayWriter)localObject1).writeInt(this.q.size());
      while (((Enumeration)localObject2).hasMoreElements())
      {
        ACL localACL = (ACL)((Enumeration)localObject2).nextElement();
        ((ByteArrayWriter)localObject1).writeInt(localACL.getType());
        ((ByteArrayWriter)localObject1).writeInt(localACL.getFlags());
        ((ByteArrayWriter)localObject1).writeInt(localACL.getMask());
        ((ByteArrayWriter)localObject1).writeString(localACL.getWho());
      }
      localByteArrayWriter.writeBinaryString(((ByteArrayWriter)localObject1).toByteArray());
    }
    if (isFlagSet(-2147483648L))
    {
      localByteArrayWriter.writeInt(this.m.size());
      localObject1 = this.m.keys();
      while (((Enumeration)localObject1).hasMoreElements())
      {
        localObject2 = (String)((Enumeration)localObject1).nextElement();
        localByteArrayWriter.writeString((String)localObject2);
        localByteArrayWriter.writeBinaryString((byte[])(byte[])this.m.get(localObject2));
      }
    }
    return (B)(B)localByteArrayWriter.toByteArray();
  }

  private int c(int paramInt1, int paramInt2)
  {
    paramInt1 >>>= paramInt2;
    return ((paramInt1 & 0x4) != 0 ? 4 : 0) + ((paramInt1 & 0x2) != 0 ? 2 : 0) + ((paramInt1 & 0x1) != 0 ? 1 : 0);
  }

  private String b(int paramInt1, int paramInt2)
  {
    paramInt1 >>>= paramInt2;
    String str = ((paramInt1 & 0x4) != 0 ? "r" : "-") + ((paramInt1 & 0x2) != 0 ? "w" : "-");
    if (((paramInt2 == 6) && ((this.e.longValue() & 0x800) == 2048L)) || ((paramInt2 == 3) && ((this.e.longValue() & 0x400) == 1024L)))
      str = str + ((paramInt1 & 0x1) != 0 ? "s" : "S");
    else
      str = str + ((paramInt1 & 0x1) != 0 ? "x" : "-");
    return str;
  }

  public String getPermissionsString()
  {
    if (this.e != null)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int i1 = ((int)this.e.longValue() & 0xF000) > 0 ? 1 : 0;
      if (i1 != 0)
        localStringBuffer.append(this.p[((int)(this.e.longValue() & 0xF000) >>> 13)]);
      else
        localStringBuffer.append('-');
      localStringBuffer.append(b((int)this.e.longValue(), 6));
      localStringBuffer.append(b((int)this.e.longValue(), 3));
      localStringBuffer.append(b((int)this.e.longValue(), 0));
      return localStringBuffer.toString();
    }
    return "";
  }

  public String getMaskString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (this.e != null)
    {
      int i1 = (int)this.e.longValue();
      localStringBuffer.append('0');
      localStringBuffer.append(c(i1, 6));
      localStringBuffer.append(c(i1, 3));
      localStringBuffer.append(c(i1, 0));
    }
    else
    {
      localStringBuffer.append("----");
    }
    return localStringBuffer.toString();
  }

  public boolean isDirectory()
  {
    if (this.i.getVersion() > 3)
      return this.t == 2;
    return (this.e != null) && ((this.e.longValue() & 0x4000) == 16384L);
  }

  public boolean isFile()
  {
    if (this.i.getVersion() > 3)
      return this.t == 1;
    return (this.e != null) && ((this.e.longValue() & 0x8000) == 32768L);
  }

  public boolean isLink()
  {
    if (this.i.getVersion() > 3)
      return this.t == 3;
    return (this.e != null) && ((this.e.longValue() & 0xA000) == 40960L);
  }

  public boolean isFifo()
  {
    return (this.e != null) && ((this.e.longValue() & 0x1000) == 4096L);
  }

  public boolean isBlock()
  {
    return (this.e != null) && ((this.e.longValue() & 0x6000) == 24576L);
  }

  public boolean isCharacter()
  {
    return (this.e != null) && ((this.e.longValue() & 0x2000) == 8192L);
  }

  public boolean isSocket()
  {
    return (this.e != null) && ((this.e.longValue() & 0xC000) == 49152L);
  }

  void b(String paramString)
  {
    this.n = paramString;
  }

  void c(String paramString)
  {
    this.k = paramString;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.sftp.SftpFileAttributes
 * JD-Core Version:    0.6.0
 */