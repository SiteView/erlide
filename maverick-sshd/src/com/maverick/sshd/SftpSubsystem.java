package com.maverick.sshd;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.nio.EventLog;
import com.maverick.ssh.Packet;
import com.maverick.sshd.events.EventServiceImplementation;
import com.maverick.sshd.events.SSHDEvent;
import com.maverick.sshd.platform.InvalidHandleException;
import com.maverick.sshd.platform.NativeFileSystemProvider;
import com.maverick.sshd.platform.PermissionDeniedException;
import com.maverick.sshd.platform.UnsupportedFileOperationException;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class SftpSubsystem extends Subsystem
{
  private NativeFileSystemProvider E;
  int F;
  _T G;
  private String H = "ISO-8859-1";
  private Map I = Collections.synchronizedMap(new HashMap());

  public SftpSubsystem()
  {
    super("sftp");
  }

  protected void init(SessionChannel paramSessionChannel, SshContext paramSshContext)
    throws IOException
  {
    super.init(paramSessionChannel, paramSshContext);
    try
    {
      "1234567890".getBytes(paramSshContext.getSFTPCharsetEncoding());
      this.H = paramSshContext.getSFTPCharsetEncoding();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      EventLog.LogEvent(this, paramSshContext.getSFTPCharsetEncoding() + " is not a supported character set encoding. Defaulting to ISO-8859-1");
      this.H = "ISO-8859-1";
    }
    this.G = new _T(paramSshContext.isFileSystemAsynchronous());
    this.G.A(new _B(paramSessionChannel.getSessionIdentifier(), paramSshContext));
    paramSessionChannel.addEventListener(new ChannelEventAdapter()
    {
      public void onChannelEOF(Channel paramChannel)
      {
        SftpSubsystem.this.session.close();
      }

      public void onChannelClosing(Channel paramChannel)
      {
        SftpSubsystem.this.session.sendExitStatus(0);
      }

      public void onChannelClose(Channel paramChannel)
      {
        synchronized (SftpSubsystem.this)
        {
          if (SftpSubsystem.this.E != null)
          {
            SftpSubsystem.this.B();
            SftpSubsystem.this.E.closeFilesystem();
            SftpSubsystem.access$002(SftpSubsystem.this, null);
            EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 31, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()));
          }
        }
      }
    });
    EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 22, true).addAttribute("SESSION_ID", paramSessionChannel.getSessionIdentifier()));
  }

  protected void onSubsystemFree()
  {
    if (this.G != null)
    {
      this.G.B();
      this.G = null;
    }
    synchronized (this)
    {
      if (this.E != null)
      {
        B();
        this.E.closeFilesystem();
        this.E = null;
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 31, true).addAttribute("SESSION_ID", this.session.getSessionIdentifier()));
      }
    }
  }

  protected void onMessageReceived(byte[] paramArrayOfByte)
    throws IOException
  {
    switch (paramArrayOfByte[0] & 0xFF)
    {
    case 1:
      A(paramArrayOfByte);
      break;
    case 14:
      this.G.A(new _W(paramArrayOfByte));
      break;
    case 16:
      this.G.A(new _H(paramArrayOfByte));
      break;
    case 11:
      this.G.A(new _C(paramArrayOfByte));
      break;
    case 3:
      this.G.A(new _M(paramArrayOfByte));
      break;
    case 5:
      this.G.A(new _Q(paramArrayOfByte));
      break;
    case 6:
      this.G.A(new _A(paramArrayOfByte));
      break;
    case 12:
      this.G.A(new _V(paramArrayOfByte));
      break;
    case 7:
      this.G.A(new _O(paramArrayOfByte));
      break;
    case 17:
      this.G.A(new _J(paramArrayOfByte));
      break;
    case 8:
      this.G.A(new _N(paramArrayOfByte));
      break;
    case 4:
      this.G.A(new _K(paramArrayOfByte));
      break;
    case 13:
      this.G.A(new _I(paramArrayOfByte));
      break;
    case 18:
      this.G.A(new _E(paramArrayOfByte));
      break;
    case 15:
      this.G.A(new _G(paramArrayOfByte));
      break;
    case 9:
      this.G.A(new _S(paramArrayOfByte));
      break;
    case 10:
      this.G.A(new _D(paramArrayOfByte));
      break;
    case 19:
      this.G.A(new _F(paramArrayOfByte));
      break;
    case 20:
      this.G.A(new _R(paramArrayOfByte));
      break;
    case 200:
      this.G.A(new _P(paramArrayOfByte));
    default:
      EventLog.LogEvent(this, "Unsupported SFTP message received [id=" + (paramArrayOfByte[0] & 0xFF) + "]");
    }
  }

  private void A(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    Packet localPacket = new Packet(paramArrayOfByte.length + 9);
    localPacket.write(102);
    localPacket.writeInt(paramInt);
    localPacket.writeBinaryString(paramArrayOfByte);
    sendMessage(localPacket);
  }

  void A(int paramInt, SftpFileAttributes paramSftpFileAttributes)
    throws IOException
  {
    byte[] arrayOfByte = paramSftpFileAttributes.toByteArray();
    Packet localPacket = new Packet(5 + arrayOfByte.length);
    localPacket.write(105);
    localPacket.writeInt(paramInt);
    localPacket.write(arrayOfByte);
    sendMessage(localPacket);
  }

  void A(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      Packet localPacket = new Packet(1024);
      localPacket.write(101);
      localPacket.writeInt(paramInt1);
      localPacket.writeInt(paramInt2);
      if (this.F > 2)
      {
        localPacket.writeString(paramString, this.H);
        localPacket.writeString("");
      }
      sendMessage(localPacket);
    }
    catch (IOException localIOException)
    {
      this.session.close();
    }
  }

  public static String formatLongname(SftpFile paramSftpFile)
  {
    return formatLongname(paramSftpFile.getAttributes(), paramSftpFile.getFilename());
  }

  public static String formatLongname(SftpFileAttributes paramSftpFileAttributes, String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(A(10 - paramSftpFileAttributes.getPermissionsString().length()) + paramSftpFileAttributes.getPermissionsString());
    localStringBuffer.append("   1 ");
    localStringBuffer.append(paramSftpFileAttributes.getUsername() + A(8 - paramSftpFileAttributes.getUsername().length()));
    localStringBuffer.append(" ");
    localStringBuffer.append(paramSftpFileAttributes.getGroup() + A(8 - paramSftpFileAttributes.getGroup().toString().length()));
    localStringBuffer.append(" ");
    localStringBuffer.append(A(8 - paramSftpFileAttributes.getSize().toString().length()) + paramSftpFileAttributes.getSize().toString());
    localStringBuffer.append(" ");
    localStringBuffer.append(A(12 - A(paramSftpFileAttributes.getModifiedTime()).length()) + A(paramSftpFileAttributes.getModifiedTime()));
    localStringBuffer.append(" ");
    localStringBuffer.append(paramString);
    return localStringBuffer.toString();
  }

  private static String A(UnsignedInteger32 paramUnsignedInteger32)
  {
    if (paramUnsignedInteger32 == null)
      return "";
    long l1 = paramUnsignedInteger32.longValue() * 1000L;
    long l2 = System.currentTimeMillis();
    SimpleDateFormat localSimpleDateFormat;
    if (l2 - l1 > 15552000000L)
      localSimpleDateFormat = new SimpleDateFormat("MMM dd  yyyy");
    else
      localSimpleDateFormat = new SimpleDateFormat("MMM dd HH:mm");
    return localSimpleDateFormat.format(new Date(l1));
  }

  private static String A(int paramInt)
  {
    String str = "";
    if (paramInt > 0)
      for (int i = 0; i < paramInt; i++)
        str = str + " ";
    return str;
  }

  void A(int paramInt, SftpFile[] paramArrayOfSftpFile, boolean paramBoolean)
    throws IOException
  {
    Packet localPacket = new Packet(4096);
    localPacket.write(104);
    localPacket.writeInt(paramInt);
    localPacket.writeInt(paramArrayOfSftpFile.length);
    for (int i = 0; i < paramArrayOfSftpFile.length; i++)
    {
      localPacket.writeString(paramArrayOfSftpFile[i].getAbsolutePath(), this.H);
      localPacket.writeString(paramBoolean ? paramArrayOfSftpFile[i].getAbsolutePath() : formatLongname(paramArrayOfSftpFile[i]), this.H);
      localPacket.write(paramArrayOfSftpFile[i].getAttributes().toByteArray());
    }
    sendMessage(localPacket);
  }

  private String A(String paramString)
    throws IOException
  {
    paramString = paramString.trim();
    if (paramString.equals(""))
      return this.E.getDefaultPath();
    return paramString;
  }

  private void A(byte[] paramArrayOfByte)
    throws IOException
  {
    this.F = Math.min((int)ByteArrayReader.readInt(paramArrayOfByte, 1), 3);
    Packet localPacket = new Packet(5);
    localPacket.write(2);
    localPacket.writeInt(this.F);
    sendMessage(localPacket);
  }

  private void B()
  {
    Iterator localIterator = this.I.values().iterator();
    while (localIterator.hasNext())
    {
      _L local_L = (_L)localIterator.next();
      if (local_L != null)
        if ((local_L.B > 0L) && (local_L.C <= 0L))
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 201, false).addAttribute("SESSION_ID", this.session.getSessionIdentifier()).addAttribute("BYTES_TRANSFERED", new Long(local_L.B)).addAttribute("NFS", this.E).addAttribute("FILE_NAME", local_L.D));
        else if ((local_L.C > 0L) && (local_L.B <= 0L))
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 202, false).addAttribute("SESSION_ID", this.session.getSessionIdentifier()).addAttribute("BYTES_TRANSFERED", new Long(local_L.C)).addAttribute("NFS", this.E).addAttribute("FILE_NAME", local_L.D));
        else if ((local_L.C <= 0L) && (local_L.B <= 0L))
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 203, false).addAttribute("SESSION_ID", this.session.getSessionIdentifier()).addAttribute("NFS", this.E).addAttribute("FILE_NAME", local_L.D));
        else
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 204, false).addAttribute("SESSION_ID", this.session.getSessionIdentifier()).addAttribute("BYTES_READ", new Long(local_L.C)).addAttribute("BYTES_WRITTEN", new Long(local_L.B)).addAttribute("NFS", this.E).addAttribute("FILE_NAME", local_L.D));
    }
    this.I.clear();
  }

  class _T
    implements Runnable
  {
    boolean B;
    boolean A = false;
    Thread D;
    LinkedList C = new LinkedList();

    _T(boolean arg2)
    {
      boolean bool;
      this.B = bool;
      if (bool)
      {
        this.D = new Thread(this);
        this.D.start();
      }
    }

    public void run()
    {
      this.A = true;
      while (this.A)
      {
        if (this.C.size() > 0)
        {
          synchronized (this.C)
          {
            localRunnable = (Runnable)this.C.removeFirst();
          }
          if (localRunnable != null)
            localRunnable.run();
          Runnable localRunnable = null;
          continue;
        }
        A();
      }
      this.C.clear();
      this.C = null;
      this.D = null;
    }

    synchronized void A()
    {
      try
      {
        wait(500L);
      }
      catch (InterruptedException localInterruptedException)
      {
      }
      this.A = ((SftpSubsystem.this.session != null) && (!SftpSubsystem.this.session.isClosed()));
    }

    synchronized void B()
    {
      this.A = false;
      if (this.B)
        notifyAll();
    }

    synchronized void A(Runnable paramRunnable)
    {
      if (this.B)
        synchronized (this.C)
        {
          this.C.addLast(paramRunnable);
          notifyAll();
        }
      else
        paramRunnable.run();
    }
  }

  abstract class _U
    implements Runnable
  {
    protected byte[] A;

    _U(byte[] arg2)
    {
      Object localObject;
      this.A = localObject;
    }
  }

  class _W extends SftpSubsystem._U
  {
    _W(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str = SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H));
        if (SftpSubsystem.this.E.makeDirectory(str))
        {
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 200, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("FILE_NAME", str));
          SftpSubsystem.this.A(i, 0, "The operation completed sucessfully");
        }
        else
        {
          SftpSubsystem.this.A(i, 4, "The operation failed");
        }
        return;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _H extends SftpSubsystem._U
  {
    _H(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      try
      {
        ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
        localByteArrayReader.skip(1L);
        int i = (int)localByteArrayReader.readInt();
        String str1 = localByteArrayReader.readString(SftpSubsystem.this.H);
        try
        {
          String str2 = SftpSubsystem.this.E.getRealPath(SftpSubsystem.this.A(str1));
          if (str1 != null)
          {
            SftpFile localSftpFile = new SftpFile(str2, SftpSubsystem.this.E.getFileAttributes(str2));
            SftpSubsystem.this.A(i, new SftpFile[] { localSftpFile }, true);
          }
          else
          {
            SftpSubsystem.this.A(i, 4, "Failed to determine real path of " + str1);
          }
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          SftpSubsystem.this.A(i, 2, "File not found");
        }
        catch (PermissionDeniedException localPermissionDeniedException)
        {
          SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
        }
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.session.close();
      }
    }
  }

  class _C extends SftpSubsystem._U
  {
    _C(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        byte[] arrayOfByte = SftpSubsystem.this.E.openDirectory(SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H)));
        SftpSubsystem.this.A(i, arrayOfByte);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
    }
  }

  class _V extends SftpSubsystem._U
  {
    _V(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        SftpSubsystem.this.A(i, SftpSubsystem.this.E.readDirectory(localByteArrayReader.readBinaryString()), false);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (InvalidHandleException localInvalidHandleException)
      {
        SftpSubsystem.this.A(i, 4, localInvalidHandleException.getMessage());
      }
      catch (EOFException localEOFException)
      {
        SftpSubsystem.this.A(i, 1, localEOFException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _O extends SftpSubsystem._U
  {
    _O(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str = SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H));
        if (SftpSubsystem.this.E.fileExists(str))
          SftpSubsystem.this.A(i, SftpSubsystem.this.E.getFileAttributes(str));
        else
          SftpSubsystem.this.A(i, 2, str + " is not a valid file path");
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _J extends SftpSubsystem._U
  {
    _J(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str = SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H));
        if (SftpSubsystem.this.E.fileExists(str))
          SftpSubsystem.this.A(i, SftpSubsystem.this.E.getFileAttributes(str));
        else
          SftpSubsystem.this.A(i, 2, str + " is not a valid file path");
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _N extends SftpSubsystem._U
  {
    _N(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        SftpSubsystem.this.A(i, SftpSubsystem.this.E.getFileAttributes(localByteArrayReader.readBinaryString()));
      }
      catch (InvalidHandleException localInvalidHandleException)
      {
        SftpSubsystem.this.A(i, 4, localInvalidHandleException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _K extends SftpSubsystem._U
  {
    _K(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        byte[] arrayOfByte = localByteArrayReader.readBinaryString();
        String str = new String(arrayOfByte);
        SftpSubsystem.this.E.closeFile(arrayOfByte);
        SftpSubsystem._L local_L = (SftpSubsystem._L)SftpSubsystem.this.I.remove(str);
        if (local_L != null)
          if ((local_L.B > 0L) && (local_L.C <= 0L))
            EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 201, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("BYTES_TRANSFERED", new Long(local_L.B)).addAttribute("NFS", SftpSubsystem.this.E).addAttribute("FILE_NAME", local_L.D));
          else if ((local_L.C > 0L) && (local_L.B <= 0L))
            EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 202, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("BYTES_TRANSFERED", new Long(local_L.C)).addAttribute("NFS", SftpSubsystem.this.E).addAttribute("FILE_NAME", local_L.D));
          else if ((local_L.C <= 0L) && (local_L.B <= 0L))
            EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 203, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("NFS", SftpSubsystem.this.E).addAttribute("FILE_NAME", local_L.D));
          else
            EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 204, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("BYTES_READ", new Long(local_L.C)).addAttribute("BYTES_WRITTEN", new Long(local_L.B)).addAttribute("NFS", SftpSubsystem.this.E).addAttribute("FILE_NAME", local_L.D));
        SftpSubsystem.this.A(i, 0, "The operation completed");
      }
      catch (InvalidHandleException localInvalidHandleException)
      {
        SftpSubsystem.this.A(i, 4, localInvalidHandleException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _A extends SftpSubsystem._U
  {
    _A(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        byte[] arrayOfByte = localByteArrayReader.readBinaryString();
        String str = new String(arrayOfByte);
        SftpSubsystem._L local_L = (SftpSubsystem._L)SftpSubsystem.this.I.get(str);
        UnsignedInteger64 localUnsignedInteger64 = localByteArrayReader.readUINT64();
        int j = (int)localByteArrayReader.readInt();
        SftpSubsystem.this.E.writeFile(arrayOfByte, localUnsignedInteger64, localByteArrayReader.array(), localByteArrayReader.getPosition(), j);
        local_L.B += j;
        SftpSubsystem.this.A(i, 0, "The write completed successfully");
      }
      catch (InvalidHandleException localInvalidHandleException)
      {
        SftpSubsystem.this.A(i, 4, localInvalidHandleException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _Q extends SftpSubsystem._U
  {
    _Q(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        byte[] arrayOfByte = localByteArrayReader.readBinaryString();
        String str = new String(arrayOfByte);
        SftpSubsystem._L local_L = (SftpSubsystem._L)SftpSubsystem.this.I.get(str);
        UnsignedInteger64 localUnsignedInteger64 = localByteArrayReader.readUINT64();
        int j = (int)localByteArrayReader.readInt();
        Packet localPacket = new Packet(j + 13);
        localPacket.write(103);
        localPacket.writeInt(i);
        int k = localPacket.position();
        localPacket.writeInt(0);
        j = SftpSubsystem.this.E.readFile(arrayOfByte, localUnsignedInteger64, localPacket.array(), localPacket.position(), j);
        if (j == -1)
        {
          SftpSubsystem.this.A(i, 1, "File is EOF");
        }
        else
        {
          local_L.C += j;
          k = localPacket.setPosition(k);
          localPacket.writeInt(j);
          localPacket.setPosition(k + j);
          SftpSubsystem.this.sendMessage(localPacket);
        }
      }
      catch (EOFException localEOFException)
      {
        SftpSubsystem.this.A(i, 1, localEOFException.getMessage());
      }
      catch (InvalidHandleException localInvalidHandleException)
      {
        SftpSubsystem.this.A(i, 4, localInvalidHandleException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _L
  {
    String D;
    NativeFileSystemProvider A;
    long C = 0L;
    long B = 0L;

    _L()
    {
    }
  }

  class _M extends SftpSubsystem._U
  {
    _M(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str = SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H));
        UnsignedInteger32 localUnsignedInteger32 = new UnsignedInteger32(localByteArrayReader.readInt());
        SftpFileAttributes localSftpFileAttributes = new SftpFileAttributes(localByteArrayReader);
        byte[] arrayOfByte = SftpSubsystem.this.E.openFile(str, localUnsignedInteger32, localSftpFileAttributes);
        SftpSubsystem._L local_L = new SftpSubsystem._L(SftpSubsystem.this);
        local_L.D = str;
        local_L.A = SftpSubsystem.this.E;
        SftpSubsystem.this.I.put(new String(arrayOfByte), local_L);
        SftpSubsystem.this.A(i, arrayOfByte);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
    }
  }

  class _I extends SftpSubsystem._U
  {
    _I(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str = SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H));
        SftpSubsystem.this.E.removeFile(str);
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 28, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("FILE_NAME", str));
        SftpSubsystem.this.A(i, 0, "The file was removed");
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
    }
  }

  class _E extends SftpSubsystem._U
  {
    _E(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str1 = localByteArrayReader.readString(SftpSubsystem.this.H);
        String str2 = localByteArrayReader.readString(SftpSubsystem.this.H);
        SftpSubsystem.this.E.renameFile(SftpSubsystem.this.A(str1), SftpSubsystem.this.A(str2));
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 27, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("FILE_NAME", str1).addAttribute("FILE_NEW_NAME", str2));
        SftpSubsystem.this.A(i, 0, "The file was renamed");
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
    }
  }

  class _G extends SftpSubsystem._U
  {
    _G(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str = SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H));
        SftpSubsystem.this.E.removeDirectory(str);
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 29, true).addAttribute("SESSION_ID", SftpSubsystem.this.session.getSessionIdentifier()).addAttribute("FILE_NAME", str));
        SftpSubsystem.this.A(i, 0, "The directory was removed");
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
    }
  }

  class _R extends SftpSubsystem._U
  {
    _R(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str1 = localByteArrayReader.readString(SftpSubsystem.this.H);
        String str2 = localByteArrayReader.readString(SftpSubsystem.this.H);
        SftpSubsystem.this.E.createSymbolicLink(SftpSubsystem.this.A(str1), SftpSubsystem.this.A(str2));
        SftpSubsystem.this.A(i, 0, "The symbolic link was created");
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
      catch (UnsupportedFileOperationException localUnsupportedFileOperationException)
      {
        SftpSubsystem.this.A(i, 8, localUnsupportedFileOperationException.getMessage());
      }
    }
  }

  class _F extends SftpSubsystem._U
  {
    _F(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        SftpFile[] arrayOfSftpFile = new SftpFile[1];
        arrayOfSftpFile[0] = SftpSubsystem.access$000(SftpSubsystem.this).readSymbolicLink(SftpSubsystem.access$300(SftpSubsystem.this, localByteArrayReader.readString(SftpSubsystem.access$200(SftpSubsystem.this))));
        SftpSubsystem.this.A(i, arrayOfSftpFile, false);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (UnsupportedFileOperationException localUnsupportedFileOperationException)
      {
        SftpSubsystem.this.A(i, 8, localUnsupportedFileOperationException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _D extends SftpSubsystem._U
  {
    _D(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        byte[] arrayOfByte = localByteArrayReader.readBinaryString();
        SftpFileAttributes localSftpFileAttributes = new SftpFileAttributes(localByteArrayReader);
        SftpSubsystem.this.E.setFileAttributes(arrayOfByte, localSftpFileAttributes);
        SftpSubsystem.this.A(i, 0, "The attributes were set");
      }
      catch (InvalidHandleException localInvalidHandleException)
      {
        SftpSubsystem.this.A(i, 4, localInvalidHandleException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _S extends SftpSubsystem._U
  {
    _S(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      int i = -1;
      try
      {
        i = (int)localByteArrayReader.readInt();
        String str = SftpSubsystem.this.A(localByteArrayReader.readString(SftpSubsystem.this.H));
        SftpFileAttributes localSftpFileAttributes = new SftpFileAttributes(localByteArrayReader);
        SftpSubsystem.this.E.setFileAttributes(str, localSftpFileAttributes);
        SftpSubsystem.this.A(i, 0, "The attributes were set");
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        SftpSubsystem.this.A(i, 2, localFileNotFoundException.getMessage());
      }
      catch (PermissionDeniedException localPermissionDeniedException)
      {
        SftpSubsystem.this.A(i, 3, localPermissionDeniedException.getMessage());
      }
      catch (IOException localIOException)
      {
        SftpSubsystem.this.A(i, 4, localIOException.getMessage());
      }
    }
  }

  class _P extends SftpSubsystem._U
  {
    _P(byte[] arg2)
    {
      super(arrayOfByte);
    }

    public void run()
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.A);
      localByteArrayReader.skip(1L);
      try
      {
        SftpSubsystem.this.A((int)localByteArrayReader.readInt(), 8, "Extensions not currently supported");
      }
      catch (IOException localIOException)
      {
      }
    }
  }

  class _B extends SftpSubsystem._U
  {
    SshContext B;

    _B(byte[] paramSshContext, SshContext arg3)
    {
      super(paramSshContext);
      Object localObject;
      this.B = localObject;
    }

    public void run()
    {
      try
      {
        SftpSubsystem.access$002(SftpSubsystem.this, (NativeFileSystemProvider)this.B.getFileSystemProvider().newInstance());
        SftpSubsystem.this.E.init(SftpSubsystem.this.session.getSessionIdentifier(), SftpSubsystem.this.session, this.B, "sftp");
      }
      catch (Throwable localThrowable1)
      {
        try
        {
          EventLog.LogEvent(this, "An SFTP initialization error occurred", localThrowable1);
          SftpSubsystem.this.session.close();
        }
        catch (Throwable localThrowable2)
        {
        }
      }
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.SftpSubsystem
 * JD-Core Version:    0.6.0
 */