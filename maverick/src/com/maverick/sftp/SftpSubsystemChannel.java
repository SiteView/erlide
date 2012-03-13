package com.maverick.sftp;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import com.maverick.ssh.Packet;
import com.maverick.ssh.SshChannel;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.SubsystemChannel;
import com.maverick.ssh.message.Message;
import com.maverick.ssh.message.MessageHolder;
import com.maverick.ssh.message.SshMessageRouter;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SftpSubsystemChannel extends SubsystemChannel
{
  private String k = "ISO-8859-1";
  public static final int OPEN_READ = 1;
  public static final int OPEN_WRITE = 2;
  public static final int OPEN_APPEND = 4;
  public static final int OPEN_CREATE = 8;
  public static final int OPEN_TRUNCATE = 16;
  public static final int OPEN_EXCLUSIVE = 32;
  public static final int OPEN_TEXT = 64;
  public static int MAX_VERSION = 4;
  int o = 4;
  int h = -1;
  int n = -1;
  Vector g = new Vector();
  UnsignedInteger32 m = new UnsignedInteger32(0L);
  Hashtable l = new Hashtable();
  _b j = new _b();
  Hashtable p = new Hashtable();
  static Log i = LogFactory.getLog(SftpSubsystemChannel.class);

  public SftpSubsystemChannel(SshSession paramSshSession)
    throws SshException
  {
    super(paramSshSession);
    this.o = MAX_VERSION;
  }

  public SftpSubsystemChannel(SshSession paramSshSession, int paramInt)
    throws SshException
  {
    super(paramSshSession);
    setThisMaxSftpVersion(paramInt);
  }

  public static void setMaxSftpVersion(int paramInt)
  {
    MAX_VERSION = paramInt;
  }

  public void setThisMaxSftpVersion(int paramInt)
  {
    this.o = paramInt;
  }

  public int getVersion()
  {
    return this.h;
  }

  public String getCanonicalNewline()
    throws SftpStatusException
  {
    if (this.h <= 3)
      throw new SftpStatusException(8, "Newline setting not available for SFTP versions <= 3");
    if (!this.p.containsKey("newline"))
      return "\r\n";
    return (String)this.p.get("newline");
  }

  public void initialize()
    throws SshException, UnsupportedEncodingException
  {
    try
    {
      this.channel.getMessageRouter().addShutdownHook(new Runnable()
      {
        public void run()
        {
          SftpSubsystemChannel.this.l.clear();
          SftpSubsystemChannel.this.g.clear();
        }
      });
      Packet localPacket = createPacket();
      localPacket.write(1);
      localPacket.writeInt(this.o);
      sendMessage(localPacket);
      byte[] arrayOfByte = nextMessage();
      if (arrayOfByte[0] != 2)
      {
        close();
        throw new SshException("Unexpected response from SFTP subsystem.", 6);
      }
      ByteArrayReader localByteArrayReader = new ByteArrayReader(arrayOfByte);
      localByteArrayReader.skip(1L);
      this.n = (int)localByteArrayReader.readInt();
      this.h = Math.min(this.n, MAX_VERSION);
      try
      {
        while (localByteArrayReader.available() > 0)
        {
          String str1 = localByteArrayReader.readString();
          String str2 = localByteArrayReader.readString();
          this.p.put(str1, str2);
        }
      }
      catch (Throwable localThrowable2)
      {
      }
      if (this.h <= 3)
        setCharsetEncoding("ISO-8859-1");
      else
        setCharsetEncoding("UTF8");
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(6, localIOException);
    }
    catch (Throwable localThrowable1)
    {
      throw new SshException(6, localThrowable1);
    }
  }

  public void setCharsetEncoding(String paramString)
    throws SshException, UnsupportedEncodingException
  {
    if (this.h == -1)
      throw new SshException("SFTP Channel must be initialized before setting character set encoding", 4);
    String str = "123456890";
    str.getBytes(paramString);
    this.k = paramString;
  }

  public int getServerVersion()
  {
    return this.n;
  }

  public String getCharsetEncoding()
  {
    return this.k;
  }

  public boolean supportsExtension(String paramString)
  {
    return this.p.containsKey(paramString);
  }

  public String getExtension(String paramString)
  {
    return (String)this.p.get(paramString);
  }

  public SftpMessage sendExtensionMessage(String paramString, byte[] paramArrayOfByte)
    throws SshException, SftpStatusException
  {
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(200);
      localPacket.writeUINT32(localUnsignedInteger32);
      localPacket.writeString(paramString);
      sendMessage(localPacket);
      return c(localUnsignedInteger32);
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(5, localIOException);
  }

  public void changePermissions(SftpFile paramSftpFile, int paramInt)
    throws SftpStatusException, SshException
  {
    SftpFileAttributes localSftpFileAttributes = new SftpFileAttributes(this, 5);
    localSftpFileAttributes.setPermissions(new UnsignedInteger32(paramInt));
    setAttributes(paramSftpFile, localSftpFileAttributes);
  }

  public void changePermissions(String paramString, int paramInt)
    throws SftpStatusException, SshException
  {
    SftpFileAttributes localSftpFileAttributes = new SftpFileAttributes(this, 5);
    localSftpFileAttributes.setPermissions(new UnsignedInteger32(paramInt));
    setAttributes(paramString, localSftpFileAttributes);
  }

  public void changePermissions(String paramString1, String paramString2)
    throws SftpStatusException, SshException
  {
    SftpFileAttributes localSftpFileAttributes = new SftpFileAttributes(this, 5);
    localSftpFileAttributes.setPermissions(paramString2);
    setAttributes(paramString1, localSftpFileAttributes);
  }

  public void setAttributes(String paramString, SftpFileAttributes paramSftpFileAttributes)
    throws SftpStatusException, SshException
  {
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(9);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      localPacket.write(paramSftpFileAttributes.toByteArray());
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public void setAttributes(SftpFile paramSftpFile, SftpFileAttributes paramSftpFileAttributes)
    throws SftpStatusException, SshException
  {
    if (!c(paramSftpFile.getHandle()))
      throw new SftpStatusException(100, "The handle is not an open file handle!");
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(10);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeBinaryString(paramSftpFile.getHandle());
      localPacket.write(paramSftpFileAttributes.toByteArray());
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  public UnsignedInteger32 postWriteRequest(byte[] paramArrayOfByte1, long paramLong, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
    throws SftpStatusException, SshException
  {
    if (!this.g.contains(new String(paramArrayOfByte1)))
      throw new SftpStatusException(100, "The handle is not valid!");
    if (paramArrayOfByte2.length - paramInt1 < paramInt2)
      throw new IndexOutOfBoundsException("Incorrect data array size!");
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(6);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeBinaryString(paramArrayOfByte1);
      localPacket.writeUINT64(paramLong);
      localPacket.writeBinaryString(paramArrayOfByte2, paramInt1, paramInt2);
      sendMessage(localPacket);
      return localUnsignedInteger32;
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public void writeFile(byte[] paramArrayOfByte1, UnsignedInteger64 paramUnsignedInteger64, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
    throws SftpStatusException, SshException
  {
    getOKRequestStatus(postWriteRequest(paramArrayOfByte1, paramUnsignedInteger64.longValue(), paramArrayOfByte2, paramInt1, paramInt2));
  }

  public void performOptimizedWrite(byte[] paramArrayOfByte, int paramInt1, int paramInt2, InputStream paramInputStream, int paramInt3, FileTransferProgress paramFileTransferProgress)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    performOptimizedWrite(paramArrayOfByte, paramInt1, paramInt2, paramInputStream, paramInt3, paramFileTransferProgress, 0L);
  }

  public void performOptimizedWrite(byte[] paramArrayOfByte, int paramInt1, int paramInt2, InputStream paramInputStream, int paramInt3, FileTransferProgress paramFileTransferProgress, long paramLong)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    try
    {
      if (!this.g.contains(new String(paramArrayOfByte)))
        throw new SftpStatusException(100, "The file handle is invalid!");
      if (paramInt1 < 4096)
        throw new SshException("Block size cannot be less than 4096", 4);
      if (paramLong < 0L)
        throw new SshException("Position value must be greater than zero!", 4);
      if ((paramLong > 0L) && (paramFileTransferProgress != null))
        paramFileTransferProgress.progressed(paramLong);
      if (paramInt3 <= 0)
        paramInt3 = paramInt1;
      byte[] arrayOfByte = new byte[paramInt1];
      long l1 = paramLong;
      int i1 = 0;
      Vector localVector = new Vector();
      while (true)
      {
        i1 = paramInputStream.read(arrayOfByte);
        if (i1 == -1)
          break;
        localVector.addElement(postWriteRequest(paramArrayOfByte, l1, arrayOfByte, 0, i1));
        l1 += i1;
        if (paramFileTransferProgress != null)
        {
          if (paramFileTransferProgress.isCancelled())
            throw new TransferCancelledException();
          paramFileTransferProgress.progressed(l1);
        }
        if (localVector.size() <= paramInt2)
          continue;
        UnsignedInteger32 localUnsignedInteger32 = (UnsignedInteger32)localVector.elementAt(0);
        localVector.removeElementAt(0);
        getOKRequestStatus(localUnsignedInteger32);
      }
      Enumeration localEnumeration = localVector.elements();
      while (localEnumeration.hasMoreElements())
        getOKRequestStatus((UnsignedInteger32)localEnumeration.nextElement());
      localVector.removeAllElements();
    }
    catch (SshIOException localSshIOException1)
    {
      throw localSshIOException1.getRealException();
    }
    catch (EOFException localEOFException)
    {
      try
      {
        close();
      }
      catch (SshIOException localSshIOException2)
      {
        throw localSshIOException2.getRealException();
      }
      catch (IOException localIOException2)
      {
        throw new SshException(localIOException2.getMessage(), 6);
      }
      throw new SftpStatusException(7, "The SFTP channel terminated unexpectedly");
    }
    catch (IOException localIOException1)
    {
      throw new SshException(localIOException1);
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      throw new SshException("Resource Shortage: try reducing the local file buffer size", 4);
    }
  }

  public void performOptimizedRead(byte[] paramArrayOfByte, long paramLong, int paramInt1, OutputStream paramOutputStream, int paramInt2, FileTransferProgress paramFileTransferProgress)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    performOptimizedRead(paramArrayOfByte, paramLong, paramInt1, paramOutputStream, paramInt2, paramFileTransferProgress, 0L);
  }

  public void performOptimizedRead(byte[] paramArrayOfByte, long paramLong1, int paramInt1, OutputStream paramOutputStream, int paramInt2, FileTransferProgress paramFileTransferProgress, long paramLong2)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    if (i.isDebugEnabled())
      i.debug("Performing optimized read length=" + paramLong1 + " postion=" + paramLong2 + " blocksize=" + paramInt1 + " outstandingRequests=" + paramInt2);
    if (paramLong1 <= 0L)
      paramLong1 = 9223372036854775807L;
    try
    {
      if (!this.g.contains(new String(paramArrayOfByte)))
        throw new SftpStatusException(100, "The file handle is invalid!");
      if ((paramInt1 < 1) || (paramInt1 > 32768))
      {
        if (i.isDebugEnabled())
          i.debug("Blocksize to large for some SFTP servers, reseting to 32K");
        paramInt1 = 32768;
      }
      if (paramLong2 < 0L)
        throw new SshException("Position value must be greater than zero!", 4);
      byte[] arrayOfByte = new byte[paramInt1];
      int i1 = readFile(paramArrayOfByte, new UnsignedInteger64(0L), arrayOfByte, 0, arrayOfByte.length);
      if (i1 == -1)
        return;
      if (i1 > paramLong2)
      {
        paramOutputStream.write(arrayOfByte, (int)paramLong2, (int)(i1 - paramLong2));
        paramLong1 -= i1 - paramLong2;
        paramLong2 = i1;
      }
      if (paramLong2 + paramLong1 <= i1)
        return;
      if ((i1 < paramInt1) && (paramLong1 > i1))
        paramInt1 = i1;
      arrayOfByte = null;
      long l1 = paramLong1 / paramInt1;
      long l2 = paramInt2;
      if ((paramLong2 > 0L) && (paramFileTransferProgress != null))
        paramFileTransferProgress.progressed(paramLong2);
      Vector localVector = new Vector(paramInt2);
      long l3 = paramLong2;
      if (l1 < l2)
        l2 = l1 + 1L;
      if (l2 <= 0L)
      {
        if (i.isDebugEnabled())
          i.debug("We calculated zero outstanding requests! numBlocks=" + l1 + " outstandingRequests=" + paramInt2 + " blocksize=" + paramInt1 + " length=" + paramLong1 + " position=" + paramLong2);
        l2 = 1L;
      }
      long l4 = l1 + 2L;
      int i2 = 0;
      long l5 = paramLong2;
      for (i1 = 0; i1 < l2; i1++)
      {
        if (i.isDebugEnabled())
          i.debug("Posting request for file offset " + l3);
        localVector.addElement(postReadRequest(paramArrayOfByte, l3, paramInt1));
        l3 += paramInt1;
        if ((paramFileTransferProgress == null) || (!paramFileTransferProgress.isCancelled()))
          continue;
        throw new TransferCancelledException();
      }
      while (true)
      {
        UnsignedInteger32 localUnsignedInteger32 = (UnsignedInteger32)localVector.elementAt(0);
        localVector.removeElementAt(0);
        SftpMessage localSftpMessage = c(localUnsignedInteger32);
        if (localSftpMessage.getType() == 103)
        {
          arrayOfByte = localSftpMessage.readBinaryString();
          if (i.isDebugEnabled())
            i.debug("Get " + arrayOfByte.length + " bytes of data");
          paramOutputStream.write(arrayOfByte);
          i2++;
          if (paramFileTransferProgress != null)
            paramFileTransferProgress.progressed(l5 += arrayOfByte.length);
        }
        else
        {
          if (localSftpMessage.getType() == 101)
          {
            int i3 = (int)localSftpMessage.readInt();
            if (i3 == 1)
            {
              if (i.isDebugEnabled())
                i.debug("Received file EOF");
              return;
            }
            if (this.h >= 3)
            {
              String str = localSftpMessage.readString().trim();
              if (i.isDebugEnabled())
                i.debug("Received status " + str);
              throw new SftpStatusException(i3, str);
            }
            if (i.isDebugEnabled())
              i.debug("Received status " + i3);
            throw new SftpStatusException(i3);
          }
          close();
          throw new SshException("The server responded with an unexpected message", 6);
        }
        if ((localVector.isEmpty()) || (i2 + localVector.size() < l4))
        {
          if (i.isDebugEnabled())
            i.debug("Posting request for file offset " + l3);
          localVector.addElement(postReadRequest(paramArrayOfByte, l3, paramInt1));
          l3 += paramInt1;
        }
        if ((paramFileTransferProgress != null) && (paramFileTransferProgress.isCancelled()))
          throw new TransferCancelledException();
      }
    }
    catch (SshIOException localSshIOException1)
    {
      throw localSshIOException1.getRealException();
    }
    catch (EOFException localEOFException)
    {
      if (i.isDebugEnabled())
        i.debug("Channel has reached EOF", localEOFException);
      try
      {
        close();
      }
      catch (SshIOException localSshIOException2)
      {
        throw localSshIOException2.getRealException();
      }
      catch (IOException localIOException2)
      {
        throw new SshException(localIOException2.getMessage(), 6);
      }
      throw new SftpStatusException(7, "The SFTP channel terminated unexpectedly");
    }
    catch (IOException localIOException1)
    {
    }
    throw new SshException(localIOException1);
  }

  public void performSynchronousRead(byte[] paramArrayOfByte, int paramInt, OutputStream paramOutputStream, FileTransferProgress paramFileTransferProgress, long paramLong)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    if (i.isDebugEnabled())
      i.debug("Performing synchronous read postion=" + paramLong + " blocksize=" + paramInt);
    if (!this.g.contains(new String(paramArrayOfByte)))
      throw new SftpStatusException(100, "The file handle is invalid!");
    if ((paramInt < 1) || (paramInt > 32768))
    {
      if (i.isDebugEnabled())
        i.debug("Blocksize to large for some SFTP servers, reseting to 32K");
      paramInt = 32768;
    }
    if (paramLong < 0L)
      throw new SshException("Position value must be greater than zero!", 4);
    byte[] arrayOfByte = new byte[paramInt];
    UnsignedInteger64 localUnsignedInteger64 = new UnsignedInteger64(paramLong);
    if ((paramLong > 0L) && (paramFileTransferProgress != null))
      paramFileTransferProgress.progressed(paramLong);
    try
    {
      int i1;
      while ((i1 = readFile(paramArrayOfByte, localUnsignedInteger64, arrayOfByte, 0, arrayOfByte.length)) > -1)
      {
        if ((paramFileTransferProgress != null) && (paramFileTransferProgress.isCancelled()))
          throw new TransferCancelledException();
        paramOutputStream.write(arrayOfByte, 0, i1);
        localUnsignedInteger64 = UnsignedInteger64.add(localUnsignedInteger64, i1);
        if (paramFileTransferProgress == null)
          continue;
        paramFileTransferProgress.progressed(localUnsignedInteger64.longValue());
      }
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  public UnsignedInteger32 postReadRequest(byte[] paramArrayOfByte, long paramLong, int paramInt)
    throws SftpStatusException, SshException
  {
    try
    {
      if (!this.g.contains(new String(paramArrayOfByte)))
        throw new SftpStatusException(100, "The file handle is invalid!");
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(5);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeBinaryString(paramArrayOfByte);
      localPacket.writeUINT64(paramLong);
      localPacket.writeInt(paramInt);
      sendMessage(localPacket);
      return localUnsignedInteger32;
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public int readFile(byte[] paramArrayOfByte1, UnsignedInteger64 paramUnsignedInteger64, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
    throws SftpStatusException, SshException
  {
    try
    {
      if (!this.g.contains(new String(paramArrayOfByte1)))
        throw new SftpStatusException(100, "The file handle is invalid!");
      if (paramArrayOfByte2.length - paramInt1 < paramInt2)
        throw new IndexOutOfBoundsException("Output array size is smaller than read length!");
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(5);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeBinaryString(paramArrayOfByte1);
      localPacket.write(paramUnsignedInteger64.toByteArray());
      localPacket.writeInt(paramInt2);
      sendMessage(localPacket);
      SftpMessage localSftpMessage = c(localUnsignedInteger32);
      if (localSftpMessage.getType() == 103)
      {
        byte[] arrayOfByte = localSftpMessage.readBinaryString();
        System.arraycopy(arrayOfByte, 0, paramArrayOfByte2, paramInt1, arrayOfByte.length);
        return arrayOfByte.length;
      }
      if (localSftpMessage.getType() == 101)
      {
        int i1 = (int)localSftpMessage.readInt();
        if (i1 == 1)
          return -1;
        if (this.h >= 3)
        {
          String str = localSftpMessage.readString().trim();
          throw new SftpStatusException(i1, str);
        }
        throw new SftpStatusException(i1);
      }
      close();
      throw new SshException("The server responded with an unexpected message", 6);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public SftpFile getFile(String paramString)
    throws SftpStatusException, SshException
  {
    String str = getAbsolutePath(paramString);
    SftpFile localSftpFile = new SftpFile(str, getAttributes(str));
    localSftpFile.e = this;
    return localSftpFile;
  }

  public String getAbsolutePath(SftpFile paramSftpFile)
    throws SftpStatusException, SshException
  {
    return getAbsolutePath(paramSftpFile.getFilename());
  }

  public void createSymbolicLink(String paramString1, String paramString2)
    throws SftpStatusException, SshException
  {
    if (this.h < 3)
      throw new SftpStatusException(8, "Symbolic links are not supported by the server SFTP version " + String.valueOf(this.h));
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(20);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString2, this.k);
      localPacket.writeString(paramString1, this.k);
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  public String getSymbolicLinkTarget(String paramString)
    throws SftpStatusException, SshException
  {
    if (this.h < 3)
      throw new SftpStatusException(8, "Symbolic links are not supported by the server SFTP version " + String.valueOf(this.h));
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(19);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      sendMessage(localPacket);
      SftpFile[] arrayOfSftpFile = b(c(localUnsignedInteger32), null);
      return arrayOfSftpFile[0].getAbsolutePath();
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public String getDefaultDirectory()
    throws SftpStatusException, SshException
  {
    return getAbsolutePath("");
  }

  public String getAbsolutePath(String paramString)
    throws SftpStatusException, SshException
  {
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(16);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      sendMessage(localPacket);
      SftpMessage localSftpMessage = c(localUnsignedInteger32);
      if (localSftpMessage.getType() == 104)
      {
        SftpFile[] arrayOfSftpFile = b(localSftpMessage, null);
        if (arrayOfSftpFile.length != 1)
        {
          close();
          throw new SshException("Server responded to SSH_FXP_REALPATH with too many files!", 6);
        }
        return arrayOfSftpFile[0].getAbsolutePath();
      }
      if (localSftpMessage.getType() == 101)
      {
        int i1 = (int)localSftpMessage.readInt();
        if (this.h >= 3)
        {
          String str = localSftpMessage.readString().trim();
          throw new SftpStatusException(i1, str);
        }
        throw new SftpStatusException(i1);
      }
      close();
      throw new SshException("The server responded with an unexpected message", 6);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public int listChildren(SftpFile paramSftpFile, Vector paramVector)
    throws SftpStatusException, SshException
  {
    if (paramSftpFile.isDirectory())
    {
      if (!c(paramSftpFile.getHandle()))
      {
        paramSftpFile = openDirectory(paramSftpFile.getAbsolutePath());
        if (!c(paramSftpFile.getHandle()))
          throw new SftpStatusException(4, "Failed to open directory");
      }
    }
    else
      throw new SshException("Cannot list children for this file object", 4);
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(12);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeBinaryString(paramSftpFile.getHandle());
      sendMessage(localPacket);
      SftpMessage localSftpMessage = c(localUnsignedInteger32);
      if (localSftpMessage.getType() == 104)
      {
        SftpFile[] arrayOfSftpFile = b(localSftpMessage, paramSftpFile.getAbsolutePath());
        for (int i2 = 0; i2 < arrayOfSftpFile.length; i2++)
          paramVector.addElement(arrayOfSftpFile[i2]);
        return arrayOfSftpFile.length;
      }
      if (localSftpMessage.getType() == 101)
      {
        int i1 = (int)localSftpMessage.readInt();
        if (i1 == 1)
          return -1;
        if (this.h >= 3)
        {
          String str = localSftpMessage.readString().trim();
          throw new SftpStatusException(i1, str);
        }
        throw new SftpStatusException(i1);
      }
      close();
      throw new SshException("The server responded with an unexpected message", 6);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  SftpFile[] b(SftpMessage paramSftpMessage, String paramString)
    throws SshException
  {
    try
    {
      if ((paramString != null) && (!paramString.endsWith("/")))
        paramString = paramString + "/";
      int i1 = (int)paramSftpMessage.readInt();
      SftpFile[] arrayOfSftpFile = new SftpFile[i1];
      String str2 = null;
      for (int i2 = 0; i2 < arrayOfSftpFile.length; i2++)
      {
        String str1 = paramSftpMessage.readString(this.k);
        if (this.h <= 3)
          str2 = paramSftpMessage.readString(this.k);
        arrayOfSftpFile[i2] = new SftpFile(paramString != null ? paramString + str1 : str1, new SftpFileAttributes(this, paramSftpMessage));
        arrayOfSftpFile[i2].f = str2;
        if ((str2 != null) && (this.h <= 3))
          try
          {
            StringTokenizer localStringTokenizer = new StringTokenizer(str2);
            localStringTokenizer.nextToken();
            localStringTokenizer.nextToken();
            String str3 = localStringTokenizer.nextToken();
            String str4 = localStringTokenizer.nextToken();
            arrayOfSftpFile[i2].getAttributes().b(str3);
            arrayOfSftpFile[i2].getAttributes().c(str4);
          }
          catch (Exception localException)
          {
          }
        arrayOfSftpFile[i2].b(this);
      }
      return arrayOfSftpFile;
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public void recurseMakeDirectory(String paramString)
    throws SftpStatusException, SshException
  {
    if (paramString.trim().length() > 0)
      try
      {
        localSftpFile = openDirectory(paramString);
        localSftpFile.close();
      }
      catch (SshException localSshException1)
      {
        SftpFile localSftpFile;
        int i1 = 0;
        do
        {
          i1 = paramString.indexOf('/', i1);
          String str = i1 > -1 ? paramString.substring(0, i1 + 1) : paramString;
          try
          {
            localSftpFile = openDirectory(str);
            localSftpFile.close();
          }
          catch (SshException localSshException2)
          {
            makeDirectory(str);
          }
        }
        while (i1 > -1);
      }
  }

  public SftpFile openFile(String paramString, int paramInt)
    throws SftpStatusException, SshException
  {
    return openFile(paramString, paramInt, new SftpFileAttributes(this, 5));
  }

  public SftpFile openFile(String paramString, int paramInt, SftpFileAttributes paramSftpFileAttributes)
    throws SftpStatusException, SshException
  {
    if (paramSftpFileAttributes == null)
      paramSftpFileAttributes = new SftpFileAttributes(this, 5);
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(3);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      localPacket.writeInt(paramInt);
      localPacket.write(paramSftpFileAttributes.toByteArray());
      sendMessage(localPacket);
      byte[] arrayOfByte = b(localUnsignedInteger32);
      this.g.addElement(new String(arrayOfByte));
      SftpFile localSftpFile = new SftpFile(paramString, null);
      localSftpFile.b(arrayOfByte);
      localSftpFile.b(this);
      EventServiceImplementation.getInstance().fireEvent(new Event(this, 26, true).addAttribute("FILE_NAME", localSftpFile.getAbsolutePath()));
      return localSftpFile;
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public SftpFile openDirectory(String paramString)
    throws SftpStatusException, SshException
  {
    String str = getAbsolutePath(paramString);
    SftpFileAttributes localSftpFileAttributes = getAttributes(str);
    if (!localSftpFileAttributes.isDirectory())
      throw new SftpStatusException(4, paramString + " is not a directory");
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(11);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      sendMessage(localPacket);
      byte[] arrayOfByte = b(localUnsignedInteger32);
      this.g.addElement(new String(arrayOfByte));
      SftpFile localSftpFile = new SftpFile(str, localSftpFileAttributes);
      localSftpFile.b(arrayOfByte);
      localSftpFile.b(this);
      return localSftpFile;
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  void b(byte[] paramArrayOfByte)
    throws SftpStatusException, SshException
  {
    if (!c(paramArrayOfByte))
      throw new SftpStatusException(100, "The handle is invalid!");
    try
    {
      this.g.removeElement(new String(paramArrayOfByte));
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(4);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeBinaryString(paramArrayOfByte);
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  public void closeFile(SftpFile paramSftpFile)
    throws SftpStatusException, SshException
  {
    if (paramSftpFile.getHandle() != null)
    {
      b(paramSftpFile.getHandle());
      EventServiceImplementation.getInstance().fireEvent(new Event(this, 25, true).addAttribute("FILE_NAME", paramSftpFile.getAbsolutePath()));
      paramSftpFile.b(null);
    }
  }

  boolean c(byte[] paramArrayOfByte)
  {
    return this.g.contains(new String(paramArrayOfByte));
  }

  public void removeDirectory(String paramString)
    throws SftpStatusException, SshException
  {
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(15);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
    EventServiceImplementation.getInstance().fireEvent(new Event(this, 29, true).addAttribute("DIRECTORY_PATH", paramString));
  }

  public void removeFile(String paramString)
    throws SftpStatusException, SshException
  {
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(13);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
    EventServiceImplementation.getInstance().fireEvent(new Event(this, 28, true).addAttribute("FILE_NAME", paramString));
  }

  public void renameFile(String paramString1, String paramString2)
    throws SftpStatusException, SshException
  {
    if (this.h < 2)
      throw new SftpStatusException(8, "Renaming files is not supported by the server SFTP version " + String.valueOf(this.h));
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(18);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString1, this.k);
      localPacket.writeString(paramString2, this.k);
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
    EventServiceImplementation.getInstance().fireEvent(new Event(this, 27, true).addAttribute("FILE_NAME", paramString1).addAttribute("FILE_NEW_NAME", paramString2));
  }

  public SftpFileAttributes getAttributes(String paramString)
    throws SftpStatusException, SshException
  {
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(17);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      if (this.h > 3)
        localPacket.writeInt(-2147483139L);
      sendMessage(localPacket);
      SftpMessage localSftpMessage = c(localUnsignedInteger32);
      return b(localSftpMessage);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  SftpFileAttributes b(SftpMessage paramSftpMessage)
    throws SftpStatusException, SshException
  {
    try
    {
      if (paramSftpMessage.getType() == 105)
        return new SftpFileAttributes(this, paramSftpMessage);
      if (paramSftpMessage.getType() == 101)
      {
        int i1 = (int)paramSftpMessage.readInt();
        if (this.h >= 3)
        {
          String str = paramSftpMessage.readString().trim();
          throw new SftpStatusException(i1, str);
        }
        throw new SftpStatusException(i1);
      }
      close();
      throw new SshException("The server responded with an unexpected message.", 6);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public SftpFileAttributes getAttributes(SftpFile paramSftpFile)
    throws SftpStatusException, SshException
  {
    try
    {
      if (!c(paramSftpFile.getHandle()))
        return getAttributes(paramSftpFile.getAbsolutePath());
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(8);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeBinaryString(paramSftpFile.getHandle());
      if (this.h > 3)
        localPacket.writeInt(-2147483139L);
      sendMessage(localPacket);
      return b(c(localUnsignedInteger32));
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public void makeDirectory(String paramString)
    throws SftpStatusException, SshException
  {
    makeDirectory(paramString, new SftpFileAttributes(this, 2));
  }

  public void makeDirectory(String paramString, SftpFileAttributes paramSftpFileAttributes)
    throws SftpStatusException, SshException
  {
    try
    {
      UnsignedInteger32 localUnsignedInteger32 = b();
      Packet localPacket = createPacket();
      localPacket.write(14);
      localPacket.writeInt(localUnsignedInteger32.longValue());
      localPacket.writeString(paramString, this.k);
      localPacket.write(paramSftpFileAttributes.toByteArray());
      sendMessage(localPacket);
      getOKRequestStatus(localUnsignedInteger32);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  byte[] b(UnsignedInteger32 paramUnsignedInteger32)
    throws SftpStatusException, SshException
  {
    try
    {
      SftpMessage localSftpMessage = c(paramUnsignedInteger32);
      if (localSftpMessage.getType() == 102)
        return localSftpMessage.readBinaryString();
      if (localSftpMessage.getType() == 101)
      {
        int i1 = (int)localSftpMessage.readInt();
        if (this.h >= 3)
        {
          String str = localSftpMessage.readString().trim();
          throw new SftpStatusException(i1, str);
        }
        throw new SftpStatusException(i1);
      }
      close();
      throw new SshException("The server responded with an unexpected message!", 6);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public void getOKRequestStatus(UnsignedInteger32 paramUnsignedInteger32)
    throws SftpStatusException, SshException
  {
    try
    {
      SftpMessage localSftpMessage = c(paramUnsignedInteger32);
      if (localSftpMessage.getType() == 101)
      {
        int i1 = (int)localSftpMessage.readInt();
        if (i1 == 0)
          return;
        if (this.h >= 3)
        {
          String str = localSftpMessage.readString().trim();
          throw new SftpStatusException(i1, str);
        }
        throw new SftpStatusException(i1);
      }
      close();
      throw new SshException("The server responded with an unexpected message!", 6);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  SftpMessage c(UnsignedInteger32 paramUnsignedInteger32)
    throws SshException
  {
    MessageHolder localMessageHolder = new MessageHolder();
    while (localMessageHolder.msg == null)
      try
      {
        if (this.j.b(paramUnsignedInteger32, localMessageHolder))
        {
          SftpMessage localSftpMessage = new SftpMessage(nextMessage());
          this.l.put(new UnsignedInteger32(localSftpMessage.getMessageId()), localSftpMessage);
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        try
        {
          close();
        }
        catch (SshIOException localSshIOException)
        {
          throw localSshIOException.getRealException();
        }
        catch (IOException localIOException2)
        {
          throw new SshException(localIOException2.getMessage(), 6);
        }
        throw new SshException("The thread was interrupted", 6);
      }
      catch (IOException localIOException1)
      {
        throw new SshException(5, localIOException1);
      }
      finally
      {
        this.j.b();
      }
    return (SftpMessage)this.l.remove(paramUnsignedInteger32);
  }

  UnsignedInteger32 b()
  {
    this.m = UnsignedInteger32.add(this.m, 1);
    return this.m;
  }

  protected void finalize()
    throws Throwable
  {
    close();
    this.l.clear();
    super.finalize();
  }

  class _b
  {
    boolean c = false;

    _b()
    {
    }

    public synchronized boolean b(UnsignedInteger32 paramUnsignedInteger32, MessageHolder paramMessageHolder)
      throws InterruptedException
    {
      int i = !this.c ? 1 : 0;
      if (SftpSubsystemChannel.this.l.containsKey(paramUnsignedInteger32))
      {
        paramMessageHolder.msg = ((Message)SftpSubsystemChannel.this.l.get(paramUnsignedInteger32));
        return false;
      }
      if (i != 0)
        this.c = true;
      else
        wait();
      return i;
    }

    public synchronized void b()
    {
      this.c = false;
      notifyAll();
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.sftp.SftpSubsystemChannel
 * JD-Core Version:    0.6.0
 */