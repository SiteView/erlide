package com.sshtools.scp;

import com.maverick.scp.ScpClientIO;
import com.maverick.scp.ScpClientIO.ScpEngineIO;
import com.maverick.sftp.FileTransferProgress;
import com.maverick.sftp.SftpStatusException;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.Client;
import com.maverick.ssh.ShellTimeoutException;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshSession;
import com.sshtools.sftp.GlobRegExpMatching;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ScpClient extends ScpClientIO
  implements Client
{
  File m;

  public ScpClient(SshClient paramSshClient)
  {
    this(null, paramSshClient);
  }

  public ScpClient(File paramFile, SshClient paramSshClient)
  {
    super(paramSshClient);
    String str = "";
    if (paramFile == null)
    {
      try
      {
        str = System.getProperty("user.home");
      }
      catch (SecurityException localSecurityException)
      {
      }
      paramFile = new File(str);
    }
    this.m = paramFile;
  }

  public void put(String paramString1, String paramString2, boolean paramBoolean)
    throws SshException, ChannelOpenException, SftpStatusException
  {
    put(paramString1, paramString2, paramBoolean, null);
  }

  public void putFile(String paramString1, String paramString2, boolean paramBoolean1, FileTransferProgress paramFileTransferProgress, boolean paramBoolean2)
    throws SshException, ChannelOpenException
  {
    File localFile = new File(paramString1);
    if (!localFile.isAbsolute())
      localFile = new File(this.m, paramString1);
    if (!localFile.exists())
      throw new SshException(paramString1 + " does not exist", 6);
    if ((!localFile.isFile()) && (!localFile.isDirectory()))
      throw new SshException(paramString1 + " is not a regular file or directory", 6);
    if ((localFile.isDirectory()) && (!paramBoolean1))
      throw new SshException(paramString1 + " is a directory, use recursive mode", 6);
    if ((paramString2 == null) || (paramString2.equals("")))
      paramString2 = ".";
    ScpEngine localScpEngine = new ScpEngine("scp " + ((localFile.isDirectory() | paramBoolean2) ? "-d " : "") + "-t " + (paramBoolean1 ? "-r " : "") + paramString2, this.ssh.openSessionChannel());
    try
    {
      localScpEngine.waitForResponse();
      ScpEngine.b(localScpEngine, localFile, paramBoolean1, paramFileTransferProgress);
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException("localfile=" + paramString1 + " remotefile=" + paramString2, 6, localIOException);
    }
    finally
    {
      try
      {
        localScpEngine.close();
      }
      catch (Throwable localThrowable2)
      {
      }
    }
  }

  public void put(String paramString1, String paramString2, boolean paramBoolean, FileTransferProgress paramFileTransferProgress)
    throws SshException, ChannelOpenException
  {
    GlobRegExpMatching localGlobRegExpMatching = new GlobRegExpMatching();
    Object localObject = this.m.getAbsolutePath();
    String str = "";
    int i;
    if (((i = paramString1.lastIndexOf(System.getProperty("file.separator"))) > -1) || ((i = paramString1.lastIndexOf('/')) > -1))
    {
      str = paramString1.substring(0, i + 1);
      localFile = new File(str);
      if (localFile.isAbsolute())
        localObject = str;
      else
        localObject = (String)localObject + System.getProperty("file.separator") + str;
    }
    File localFile = new File((String)localObject);
    String[] arrayOfString1 = localFile.list();
    File[] arrayOfFile = new File[arrayOfString1.length];
    for (int j = 0; j < arrayOfString1.length; j++)
      arrayOfFile[j] = new File((String)localObject + File.separator + arrayOfString1[j]);
    String[] arrayOfString2 = localGlobRegExpMatching.matchFileNamesWithPattern(arrayOfFile, paramString1.substring(i + 1));
    if (arrayOfString2.length == 0)
      throw new SshException(paramString1 + "No file matches/File does not exist", 6);
    if (arrayOfString2.length > 1)
      put(arrayOfString2, paramString2, paramBoolean, paramFileTransferProgress);
    else
      putFile(arrayOfString2[0], paramString2, paramBoolean, paramFileTransferProgress, false);
  }

  public void put(String[] paramArrayOfString, String paramString, boolean paramBoolean)
    throws SshException, ChannelOpenException
  {
    put(paramArrayOfString, paramString, paramBoolean, null);
  }

  public void put(String[] paramArrayOfString, String paramString, boolean paramBoolean, FileTransferProgress paramFileTransferProgress)
    throws SshException, ChannelOpenException
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
      putFile(paramArrayOfString[i], paramString, paramBoolean, paramFileTransferProgress, true);
  }

  public void get(String paramString, String[] paramArrayOfString, boolean paramBoolean)
    throws SshException, ChannelOpenException
  {
    get(paramString, paramArrayOfString, paramBoolean, null);
  }

  public void get(String paramString, String[] paramArrayOfString, boolean paramBoolean, FileTransferProgress paramFileTransferProgress)
    throws SshException, ChannelOpenException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      localStringBuffer.append("\"");
      localStringBuffer.append(paramArrayOfString[i]);
      localStringBuffer.append("\" ");
    }
    String str = localStringBuffer.toString();
    str = str.trim();
    get(paramString, str, paramBoolean, paramFileTransferProgress);
  }

  public void get(String paramString1, String paramString2, boolean paramBoolean)
    throws SshException, ChannelOpenException
  {
    get(paramString1, paramString2, paramBoolean, null);
  }

  public void get(String paramString1, String paramString2, boolean paramBoolean, FileTransferProgress paramFileTransferProgress)
    throws SshException, ChannelOpenException
  {
    if ((paramString1 == null) || (paramString1.equals("")))
      paramString1 = ".";
    File localFile = new File(paramString1);
    if (!localFile.isAbsolute())
      localFile = new File(this.m, paramString1);
    if ((localFile.exists()) && (!localFile.isFile()) && (!localFile.isDirectory()))
      throw new SshException(paramString1 + " is not a regular file or directory", 6);
    ScpEngine localScpEngine = new ScpEngine("scp -f " + (paramBoolean ? "-r " : "") + paramString2, this.ssh.openSessionChannel());
    ScpEngine.b(localScpEngine, localFile, paramFileTransferProgress, false);
    try
    {
      localScpEngine.close();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void exit()
    throws SshException, ShellTimeoutException, IOException
  {
  }

  protected class ScpEngine extends ScpClientIO.ScpEngineIO
  {
    protected ScpEngine(String paramSshSession, SshSession arg3)
      throws SshException
    {
      super(paramSshSession, localSshSession);
    }

    private boolean c(File paramFile, boolean paramBoolean, FileTransferProgress paramFileTransferProgress)
      throws SshException
    {
      try
      {
        if (!paramBoolean)
        {
          writeError("File " + paramFile.getName() + " is a directory, use recursive mode");
          return false;
        }
        String str = "D0755 0 " + paramFile.getName() + "\n";
        this.out.write(str.getBytes());
        waitForResponse();
        String[] arrayOfString = paramFile.list();
        for (int i = 0; i < arrayOfString.length; i++)
        {
          File localFile = new File(paramFile, arrayOfString[i]);
          b(localFile, paramBoolean, paramFileTransferProgress);
        }
        this.out.write("E\n".getBytes());
        return true;
      }
      catch (IOException localIOException)
      {
        close();
      }
      throw new SshException(localIOException, 6);
    }

    private void b(File paramFile, boolean paramBoolean, FileTransferProgress paramFileTransferProgress)
      throws SshException
    {
      try
      {
        if (paramFile.isDirectory())
        {
          if (!c(paramFile, paramBoolean, paramFileTransferProgress))
            return;
        }
        else if (paramFile.isFile())
        {
          String str = "C0644 " + paramFile.length() + " " + paramFile.getName() + "\n";
          this.out.write(str.getBytes());
          if (paramFileTransferProgress != null)
            paramFileTransferProgress.started(paramFile.length(), paramFile.getName());
          waitForResponse();
          FileInputStream localFileInputStream = new FileInputStream(paramFile);
          writeCompleteFile(localFileInputStream, paramFile.length(), paramFileTransferProgress);
          if (paramFileTransferProgress != null)
            paramFileTransferProgress.completed();
          writeOk();
        }
        else
        {
          throw new SshException(paramFile.getName() + " not valid for SCP", 6);
        }
        waitForResponse();
      }
      catch (SshIOException localSshIOException)
      {
        throw localSshIOException.getRealException();
      }
      catch (IOException localIOException)
      {
        close();
        throw new SshException(localIOException, 6);
      }
    }

    private void b(File paramFile, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
      throws SshException
    {
      try
      {
        String[] arrayOfString = new String[3];
        writeOk();
        while (true)
        {
          String str1;
          try
          {
            str1 = readString();
          }
          catch (EOFException localEOFException)
          {
            return;
          }
          catch (SshIOException localSshIOException2)
          {
            return;
          }
          int i = str1.charAt(0);
          switch (i)
          {
          case 69:
            writeOk();
            return;
          case 84:
            break;
          case 67:
          case 68:
            String str2 = paramFile.getAbsolutePath();
            parseCommand(str1, arrayOfString);
            if (paramFile.isDirectory())
              str2 = str2 + File.separator + arrayOfString[2];
            File localFile = new File(str2);
            if (i == 68)
            {
              if (localFile.exists())
              {
                if (!localFile.isDirectory())
                {
                  localObject = "Invalid target " + localFile.getName() + ", must be a directory";
                  writeError((String)localObject);
                  throw new IOException((String)localObject);
                }
              }
              else if (!localFile.mkdir())
              {
                localObject = "Could not create directory: " + localFile.getName();
                writeError((String)localObject);
                throw new IOException((String)localObject);
              }
              b(localFile, paramFileTransferProgress, true);
              continue;
            }
            Object localObject = new FileOutputStream(localFile);
            writeOk();
            long l = Long.parseLong(arrayOfString[1]);
            if (paramFileTransferProgress != null)
              paramFileTransferProgress.started(l, str2);
            readCompleteFile((OutputStream)localObject, l, paramFileTransferProgress);
            if (paramFileTransferProgress != null)
              paramFileTransferProgress.completed();
            try
            {
              waitForResponse();
              writeOk();
            }
            catch (SshIOException localSshIOException3)
            {
              if ((localSshIOException3.getRealException().getReason() == 1) && (!paramBoolean))
                return;
              throw localSshIOException3;
            }
          default:
            writeError("Unexpected cmd: " + str1);
            throw new IOException("SCP unexpected cmd: " + str1);
          }
        }
      }
      catch (SshIOException localSshIOException1)
      {
        throw localSshIOException1.getRealException();
      }
      catch (IOException localIOException)
      {
        close();
      }
      throw new SshException(localIOException, 6);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.scp.ScpClient
 * JD-Core Version:    0.6.0
 */