package com.maverick.sshd.scp;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.nio.EventLog;
import com.maverick.sshd.Channel;
import com.maverick.sshd.ChannelEventAdapter;
import com.maverick.sshd.SessionChannel;
import com.maverick.sshd.SftpFile;
import com.maverick.sshd.SftpFileAttributes;
import com.maverick.sshd.SshContext;
import com.maverick.sshd.events.EventServiceImplementation;
import com.maverick.sshd.events.SSHDEvent;
import com.maverick.sshd.platform.ExecutableCommand;
import com.maverick.sshd.platform.InvalidHandleException;
import com.maverick.sshd.platform.NativeFileSystemProvider;
import com.maverick.sshd.platform.PermissionDeniedException;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class ScpCommand extends ExecutableCommand
  implements Runnable
{
  private static int H = 16384;
  private String L;
  private Thread J;
  private int G = 0;
  private int K = -2147483648;
  private boolean I;
  private boolean D;
  private boolean O;
  private boolean N;
  private NativeFileSystemProvider F;
  private byte[] E = new byte[H];
  private boolean P;
  private boolean M = true;

  public ScpCommand()
  {
    super(65535);
  }

  public boolean createProcess(String paramString, Map paramMap)
  {
    try
    {
      this.F = ((NativeFileSystemProvider)this.session.getContext().getFileSystemProvider().newInstance());
      this.F.init(this.session.getSessionIdentifier(), this.session, this.session.getContext(), "scp");
      this.session.addEventListener(new ChannelEventAdapter()
      {
        public void onChannelClose(Channel paramChannel)
        {
          ScpCommand.this.kill();
        }
      });
      C(paramString.substring(4));
      return true;
    }
    catch (IOException localIOException)
    {
      EventLog.LogEvent(this, "Failed to start command: " + paramString, localIOException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      EventLog.LogEvent(this, "Failed to create native file system", localIllegalAccessException);
    }
    catch (InstantiationException localInstantiationException)
    {
      EventLog.LogEvent(this, "Failed to create native file system", localInstantiationException);
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "SCP command could not be processed: " + paramString, localThrowable);
    }
    return false;
  }

  public int getExitCode()
  {
    return this.K;
  }

  public void kill()
  {
    try
    {
      this.stdin.close();
    }
    catch (IOException localIOException1)
    {
    }
    try
    {
      this.stdout.close();
    }
    catch (IOException localIOException2)
    {
    }
    this.F.closeFilesystem();
    if (!this.session.isClosing())
      this.session.close();
  }

  public void onStart()
  {
    this.J = new Thread(this, "SCP");
    this.J.start();
  }

  private void C(String paramString)
    throws IOException
  {
    String[] arrayOfString = A.B().A(paramString, " ");
    this.L = null;
    this.I = false;
    this.O = false;
    this.N = false;
    this.D = false;
    this.G = 0;
    int i = 0;
    for (int j = 0; j < arrayOfString.length; j++)
      if (arrayOfString[j].startsWith("-"))
      {
        String str = arrayOfString[j].substring(1);
        for (int k = 0; k < str.length(); k++)
        {
          char c = str.charAt(k);
          switch (c)
          {
          case 't':
            this.N = true;
            break;
          case 'd':
            this.I = true;
            break;
          case 'f':
            this.O = true;
            break;
          case 'r':
            this.D = true;
            break;
          case 'v':
            this.G += 1;
            break;
          case 'p':
            this.P = true;
            break;
          case 'e':
          case 'g':
          case 'h':
          case 'i':
          case 'j':
          case 'k':
          case 'l':
          case 'm':
          case 'n':
          case 'o':
          case 'q':
          case 's':
          case 'u':
          default:
            EventLog.LogEvent(this, "Unsupported SCP argument " + c);
          }
        }
      }
      else if (this.L == null)
      {
        this.L = arrayOfString[j];
      }
      else if (this.L.endsWith("\\"))
      {
        this.L = this.L.substring(0, this.L.length() - 1);
        this.L = (this.L + " " + arrayOfString[j]);
      }
      else
      {
        throw new IOException("More than one destination supplied " + arrayOfString[j]);
      }
    if ((!this.N) && (!this.O))
      throw new IOException("Must supply either -t or -f.");
    if (this.L == null)
      throw new IOException("Destination not supplied.");
  }

  private void A()
    throws IOException
  {
    this.stdout.write(0);
  }

  private void E(String paramString)
    throws IOException
  {
    this.stdout.write(paramString.getBytes());
    if (!paramString.endsWith("\n"))
      this.stdout.write("\n".getBytes());
  }

  private void F(String paramString)
    throws IOException
  {
    A(paramString, false);
  }

  private void A(String paramString, boolean paramBoolean)
    throws IOException
  {
    this.K = 1;
    if (!this.session.isClosed())
    {
      this.stdout.write(paramBoolean ? 2 : 1);
      this.stdout.write(paramString.getBytes());
      if (!paramString.endsWith("\n"))
        this.stdout.write("\n".getBytes());
    }
  }

  protected FilenamePattern createFilenamePattern(String paramString)
  {
    return new FilenamePattern(paramString, System.getProperty("os.name") != null ? System.getProperty("os.name").startsWith("Windows") : false);
  }

  public void run()
  {
    try
    {
      if (this.O)
        try
        {
          C();
          String str1 = this.L;
          String str2 = ".";
          int i = str1.lastIndexOf('/');
          if (i != -1)
          {
            if (i > 0)
              str2 = str1.substring(0, i);
            str1 = str1.substring(i + 1);
          }
          FilenamePattern localFilenamePattern = createFilenamePattern(str1);
          byte[] arrayOfByte = null;
          try
          {
            arrayOfByte = this.F.openDirectory(str2);
            SftpFile[] arrayOfSftpFile = this.F.readDirectory(arrayOfByte);
            for (int j = 0; j < arrayOfSftpFile.length; j++)
            {
              if ((arrayOfSftpFile[j].getFilename().equals(".")) || (arrayOfSftpFile[j].getFilename().equals("..")) || (!localFilenamePattern.matches(arrayOfSftpFile[j].getFilename())))
                continue;
              D(str2 + "/" + arrayOfSftpFile[j].getFilename());
            }
          }
          finally
          {
            if (arrayOfByte != null)
              try
              {
                this.F.closeFile(arrayOfByte);
              }
              catch (InvalidHandleException localInvalidHandleException2)
              {
              }
          }
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          A(localFileNotFoundException.getMessage(), true);
        }
        catch (PermissionDeniedException localPermissionDeniedException)
        {
          A(localPermissionDeniedException.getMessage(), true);
        }
        catch (InvalidHandleException localInvalidHandleException1)
        {
          A(localInvalidHandleException1.getMessage(), true);
        }
        catch (IOException localIOException)
        {
          EventLog.LogEvent(this, "", localIOException);
          A(localIOException.getMessage(), true);
        }
      else
        A(this.L);
      this.K = 0;
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "SCP thread failed", localThrowable);
      this.K = 1;
    }
    if (!this.session.isClosed())
      this.session.close();
  }

  private boolean B(String paramString)
    throws IOException
  {
    byte[] arrayOfByte = null;
    try
    {
      SftpFileAttributes localSftpFileAttributes = this.F.getFileAttributes(paramString);
      int i;
      if ((localSftpFileAttributes.isDirectory()) && (!this.D))
      {
        F("File " + paramString + " is a directory, use recursive mode");
        i = 0;
        jsr 247;
      }
      String str = paramString;
      int j = paramString.lastIndexOf('/');
      if (j != -1)
        str = paramString.substring(j + 1);
      E("D" + localSftpFileAttributes.getMaskString() + " 0 " + str + "\n");
      C();
      arrayOfByte = this.F.openDirectory(paramString);
      try
      {
        SftpFile[] arrayOfSftpFile;
        do
        {
          arrayOfSftpFile = this.F.readDirectory(arrayOfByte);
          for (int k = 0; k < arrayOfSftpFile.length; k++)
          {
            if ((arrayOfSftpFile[k].getFilename().equals(".")) || (arrayOfSftpFile[k].getFilename().equals("..")))
              continue;
            D(paramString + "/" + arrayOfSftpFile[k].getFilename());
          }
        }
        while (arrayOfSftpFile.length > 0);
      }
      catch (EOFException localEOFException)
      {
      }
      E("E");
    }
    catch (InvalidHandleException localInvalidHandleException)
    {
      throw new IOException(localInvalidHandleException.getMessage());
    }
    catch (PermissionDeniedException localPermissionDeniedException)
    {
      throw new IOException(localPermissionDeniedException.getMessage());
    }
    finally
    {
      if (arrayOfByte != null)
        try
        {
          this.F.closeFile(arrayOfByte);
        }
        catch (Exception localException)
        {
        }
    }
    return true;
  }

  private void D(String paramString)
    throws IOException, PermissionDeniedException, InvalidHandleException
  {
    SftpFileAttributes localSftpFileAttributes = this.F.getFileAttributes(paramString);
    if (localSftpFileAttributes.isDirectory())
    {
      if (!B(paramString))
        return;
    }
    else if (localSftpFileAttributes.isFile())
    {
      String str = paramString;
      int i = str.lastIndexOf('/');
      if (i != -1)
        str = paramString.substring(i + 1);
      E("C" + localSftpFileAttributes.getMaskString() + " " + localSftpFileAttributes.getSize() + " " + str + "\n");
      C();
      byte[] arrayOfByte1 = null;
      try
      {
        arrayOfByte1 = this.F.openFile(paramString, new UnsignedInteger32(1L), localSftpFileAttributes);
        long l = 0L;
        UnsignedInteger64 localUnsignedInteger64 = new UnsignedInteger64(l);
        while (l < localSftpFileAttributes.getSize().longValue())
        {
          byte[] arrayOfByte2 = new byte[H];
          try
          {
            int j = this.F.readFile(arrayOfByte1, localUnsignedInteger64, arrayOfByte2, 0, arrayOfByte2.length);
            if (j < 0)
              break;
            localUnsignedInteger64 = UnsignedInteger64.add(localUnsignedInteger64, j);
            l += j;
            this.stdout.write(arrayOfByte2, 0, j);
          }
          catch (EOFException localEOFException)
          {
            break;
          }
        }
        if (l < localSftpFileAttributes.getSize().longValue())
          throw new IOException("File transfer terminated abnormally.");
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 34, true).addAttribute("FILE_NAME", paramString).addAttribute("BYTES_TRANSFERED", new Long(l)).addAttribute("NFS", this.F).addAttribute("SESSION_ID", this.session.getSessionIdentifier()));
        A();
      }
      finally
      {
        if (arrayOfByte1 != null)
          try
          {
            this.F.closeFile(arrayOfByte1);
          }
          catch (Exception localException)
          {
          }
      }
    }
    else
    {
      throw new IOException(paramString + " not valid for SCP.");
    }
    C();
    this.K = 0;
  }

  private void C()
    throws IOException
  {
    int i = this.stdin.read();
    if (i == 0)
      return;
    if (i == -1)
      throw new EOFException("SCP returned unexpected EOF");
    String str = B();
    if (i == 2)
      throw new IOException(str);
    throw new IOException("SCP returned an unexpected error: " + str);
  }

  private void A(String paramString)
    throws IOException
  {
    String[] arrayOfString = new String[3];
    A();
    label980: 
    while ((!this.session.isClosed()) && (!this.session.isEOF()))
    {
      String str1;
      try
      {
        str1 = B();
        this.K = -2147483648;
      }
      catch (EOFException localEOFException)
      {
        return;
      }
      int i = str1.charAt(0);
      switch (i)
      {
      case 69:
        A();
        return;
      case 84:
        A();
        break;
      case 67:
      case 68:
        A(str1, arrayOfString);
        String str2 = arrayOfString[2];
        SftpFileAttributes localSftpFileAttributes = null;
        try
        {
          localSftpFileAttributes = this.F.getFileAttributes(paramString);
        }
        catch (FileNotFoundException localFileNotFoundException1)
        {
        }
        catch (PermissionDeniedException localPermissionDeniedException1)
        {
        }
        String str3;
        if (i == 68)
        {
          if (paramString.equals("."))
            str3 = str2;
          else if ((localSftpFileAttributes == null) && (this.M))
            str3 = paramString;
          else
            str3 = paramString + (paramString.endsWith("/") ? "" : "/") + str2;
          this.M = false;
          try
          {
            localSftpFileAttributes = this.F.getFileAttributes(str3);
          }
          catch (FileNotFoundException localFileNotFoundException2)
          {
            localSftpFileAttributes = null;
          }
          catch (PermissionDeniedException localPermissionDeniedException2)
          {
            localSftpFileAttributes = null;
          }
          String str4;
          if (localSftpFileAttributes != null)
          {
            if (!localSftpFileAttributes.isDirectory())
            {
              str4 = "Invalid target " + str2 + ", must be a directory";
              F(str4);
              throw new IOException(str4);
            }
          }
          else
            try
            {
              if (!this.F.makeDirectory(str3))
              {
                str4 = "Could not create directory: " + str2;
                F(str4);
                throw new IOException(str4);
              }
              localSftpFileAttributes = this.F.getFileAttributes(str3);
              localSftpFileAttributes.setPermissionsFromMaskString(arrayOfString[0]);
            }
            catch (FileNotFoundException localFileNotFoundException3)
            {
              F("File not found");
              throw new IOException("File not found");
            }
            catch (PermissionDeniedException localPermissionDeniedException3)
            {
              F("Permission denied");
              throw new IOException("Permission denied");
            }
          A(str3);
          this.K = 0;
          continue;
        }
        if ((localSftpFileAttributes == null) || (!localSftpFileAttributes.isDirectory()))
          str3 = paramString;
        else
          str3 = paramString + (paramString.endsWith("/") ? "" : "/") + str2;
        if (localSftpFileAttributes == null)
          localSftpFileAttributes = new SftpFileAttributes();
        localSftpFileAttributes.setSize(new UnsignedInteger64(arrayOfString[1]));
        byte[] arrayOfByte = null;
        try
        {
          arrayOfByte = this.F.openFile(str3, new UnsignedInteger32(26L), localSftpFileAttributes);
          A();
          long l1 = 0L;
          long l2 = Long.parseLong(arrayOfString[1]);
          UnsignedInteger64 localUnsignedInteger64 = new UnsignedInteger64(0L);
          while (l1 < l2)
          {
            int j = this.stdin.read(this.E, 0, (int)this.E.length);
            if (j == -1)
              throw new EOFException("ScpServer received an unexpected EOF during file transfer");
            this.F.writeFile(arrayOfByte, localUnsignedInteger64, this.E, 0, j);
            localUnsignedInteger64 = UnsignedInteger64.add(localUnsignedInteger64, j);
            l1 += j;
          }
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 33, true).addAttribute("FILE_NAME", str3).addAttribute("BYTES_TRANSFERED", new Long(l2)).addAttribute("NFS", this.F).addAttribute("SESSION_ID", this.session.getSessionIdentifier()));
        }
        catch (InvalidHandleException localInvalidHandleException)
        {
          F("Invalid handle.");
          throw new IOException("Invalid handle.");
        }
        catch (FileNotFoundException localFileNotFoundException4)
        {
          F("File not found");
          throw new IOException("File not found");
        }
        catch (PermissionDeniedException localPermissionDeniedException4)
        {
          F("Permission denied");
          throw new IOException("Permission denied");
        }
        finally
        {
          if (arrayOfByte != null)
            try
            {
              this.F.closeFile(arrayOfByte);
            }
            catch (Exception localException2)
            {
            }
        }
        C();
        if (this.P)
        {
          localSftpFileAttributes.setPermissionsFromMaskString(arrayOfString[0]);
          try
          {
            this.F.setFileAttributes(str3, localSftpFileAttributes);
          }
          catch (Exception localException1)
          {
            F("Failed to set file permissions.");
            break label980;
          }
        }
        A();
        this.K = 0;
        break;
      default:
        F("Unexpected cmd: " + str1);
        throw new IOException("SCP unexpected cmd: " + str1);
      }
    }
  }

  private void A(String paramString, String[] paramArrayOfString)
    throws IOException
  {
    int i = paramString.indexOf(' ');
    int j = paramString.indexOf(' ', i + 1);
    if ((i == -1) || (j == -1))
    {
      F("Syntax error in cmd");
      throw new IOException("Syntax error in cmd");
    }
    paramArrayOfString[0] = paramString.substring(1, i);
    paramArrayOfString[1] = paramString.substring(i + 1, j);
    paramArrayOfString[2] = paramString.substring(j + 1);
  }

  private String B()
    throws IOException
  {
    int j = 0;
    int i;
    while (((i = this.stdin.read()) != 10) && (i >= 0))
      this.E[(j++)] = (byte)i;
    if (i == -1)
      throw new EOFException("SCP returned unexpected EOF");
    if (this.E[0] == 10)
      throw new IOException("Unexpected <NL>");
    if ((this.E[0] == 2) || (this.E[0] == 1))
    {
      String str = new String(this.E, 1, j - 1);
      if (this.E[0] == 2)
        throw new IOException(str);
      throw new IOException("SCP returned an unexpected error: " + str);
    }
    return new String(this.E, 0, j);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.scp.ScpCommand
 * JD-Core Version:    0.6.0
 */