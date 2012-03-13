package com.sshtools.sftp;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import com.maverick.sftp.FileTransferProgress;
import com.maverick.sftp.SftpFile;
import com.maverick.sftp.SftpFileAttributes;
import com.maverick.sftp.SftpFileInputStream;
import com.maverick.sftp.SftpFileOutputStream;
import com.maverick.sftp.SftpStatusException;
import com.maverick.sftp.SftpSubsystemChannel;
import com.maverick.sftp.TransferCancelledException;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.Client;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh1.Ssh1Client;
import com.maverick.ssh2.Ssh2Client;
import com.maverick.ssh2.Ssh2Session;
import com.maverick.util.EOLProcessor;
import com.maverick.util.IOUtil;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SftpClient
  implements Client
{
  SftpSubsystemChannel tb;
  String ob;
  String pb;
  private int nb = 32768;
  private int ub = 100;
  private int mb = -1;
  int wb = 18;
  public static final int MODE_BINARY = 1;
  public static final int MODE_TEXT = 2;
  public static final int EOL_CRLF = 1;
  public static final int EOL_LF = 2;
  public static final int EOL_CR = 3;
  private int rb = 1;
  private int sb = 1;
  static Log qb = LogFactory.getLog(SftpClient.class);
  private Vector xb = new Vector();
  public static final int NoSyntax = 0;
  public static final int GlobSyntax = 1;
  public static final int Perl5Syntax = 2;
  private int vb = 1;

  public SftpClient(SshClient paramSshClient)
    throws SftpStatusException, SshException, ChannelOpenException
  {
    this(paramSshClient, SftpSubsystemChannel.MAX_VERSION);
  }

  public SftpClient(SshSession paramSshSession)
    throws SftpStatusException, SshException
  {
    this(paramSshSession, SftpSubsystemChannel.MAX_VERSION);
  }

  public SftpClient(SshSession paramSshSession, int paramInt)
    throws SftpStatusException, SshException
  {
    b(paramSshSession, paramInt);
  }

  public SftpClient(SshClient paramSshClient, int paramInt)
    throws SftpStatusException, SshException, ChannelOpenException
  {
    Object localObject1 = null;
    if ((paramSshClient instanceof Ssh1Client))
    {
      if (qb.isDebugEnabled())
        qb.debug("SFTP may not be supported on SSH1, searching for sftp-server binary");
      try
      {
        localObject1 = paramSshClient.openSessionChannel();
        if (((SshSession)localObject1).executeCommand("find / -name 'sftp-server'"))
        {
          BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(((SshSession)localObject1).getInputStream()));
          String str;
          while ((str = localBufferedReader.readLine()) != null)
          {
            if ((!str.startsWith("/")) || (!str.endsWith("sftp-server")))
              continue;
            paramSshClient.getContext().setSFTPProvider(str);
            if (!qb.isDebugEnabled())
              continue;
            qb.debug("Found SFTP provider " + str);
          }
          localBufferedReader.close();
        }
      }
      catch (Exception localException)
      {
      }
      finally
      {
        if (localObject1 != null)
          ((SshSession)localObject1).close();
        localObject1 = paramSshClient.openSessionChannel();
      }
    }
    else
    {
      if (qb.isDebugEnabled())
        qb.debug("SFTP will be performed over an SSH2 connection");
      localObject1 = ((Ssh2Client)paramSshClient).openSessionChannel(131072, 32768, null, 0L);
    }
    if ((localObject1 instanceof Ssh2Session))
    {
      Ssh2Session localSsh2Session = (Ssh2Session)localObject1;
      if (!localSsh2Session.startSubsystem("sftp"))
      {
        if (qb.isDebugEnabled())
          qb.debug("The SFTP subsystem failed to start, attempting to execute provider " + paramSshClient.getContext().getSFTPProvider());
        if (!localSsh2Session.executeCommand(paramSshClient.getContext().getSFTPProvider()))
        {
          localSsh2Session.close();
          throw new SshException("Failed to start SFTP subsystem or SFTP provider " + paramSshClient.getContext().getSFTPProvider(), 6);
        }
      }
    }
    else if (!((SshSession)localObject1).executeCommand(paramSshClient.getContext().getSFTPProvider()))
    {
      ((SshSession)localObject1).close();
      throw new SshException("Failed to launch SFTP provider " + paramSshClient.getContext().getSFTPProvider(), 6);
    }
    b((SshSession)localObject1, paramInt);
  }

  private void b(SshSession paramSshSession, int paramInt)
    throws SftpStatusException, SshException
  {
    this.tb = new SftpSubsystemChannel(paramSshSession, paramInt);
    try
    {
      this.tb.initialize();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    this.ob = this.tb.getDefaultDirectory();
    String str = "";
    try
    {
      str = System.getProperty("user.home");
    }
    catch (SecurityException localSecurityException)
    {
    }
    this.pb = str;
    EventServiceImplementation.getInstance().fireEvent(new Event(this, 22, true));
  }

  public void setBlockSize(int paramInt)
  {
    if (paramInt < 512)
      throw new IllegalArgumentException("Block size must be greater than 512");
    this.nb = paramInt;
  }

  public SftpSubsystemChannel getSubsystemChannel()
  {
    return this.tb;
  }

  public void setTransferMode(int paramInt)
  {
    if ((paramInt != 1) && (paramInt != 2))
      throw new IllegalArgumentException("Mode can only be either binary or text");
    this.sb = paramInt;
    if (qb.isDebugEnabled())
      qb.debug("Transfer mode set to " + (paramInt == 1 ? "binary" : "text"));
  }

  public void setRemoteEOL(int paramInt)
  {
    this.rb = paramInt;
    if (qb.isDebugEnabled())
      qb.debug("Remote EOL set to " + (paramInt == 3 ? "CR" : paramInt == 1 ? "CRLF" : "LF"));
  }

  public int getTransferMode()
  {
    return this.sb;
  }

  public void setBufferSize(int paramInt)
  {
    this.mb = paramInt;
    if (qb.isDebugEnabled())
      qb.debug("Buffer size set to " + paramInt);
  }

  public void setMaxAsyncRequests(int paramInt)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("Maximum asynchronous requests must be greater or equal to 1");
    this.ub = paramInt;
    if (qb.isDebugEnabled())
      qb.debug("Max async requests set to " + paramInt);
  }

  public int umask(int paramInt)
  {
    int i = this.wb;
    this.wb = paramInt;
    if (qb.isDebugEnabled())
      qb.debug("umask " + paramInt);
    return i;
  }

  public SftpFile openFile(String paramString)
    throws SftpStatusException, SshException
  {
    if ((this.sb == 2) && (this.tb.getVersion() > 3))
      return this.tb.openFile(b(paramString), 65);
    return this.tb.openFile(b(paramString), 1);
  }

  public void cd(String paramString)
    throws SftpStatusException, SshException
  {
    String str;
    if ((paramString == null) || (paramString.equals("")))
    {
      str = this.tb.getDefaultDirectory();
    }
    else
    {
      str = b(paramString);
      str = this.tb.getAbsolutePath(str);
    }
    if (!str.equals(""))
    {
      SftpFileAttributes localSftpFileAttributes = this.tb.getAttributes(str);
      if (!localSftpFileAttributes.isDirectory())
        throw new SftpStatusException(4, paramString + " is not a directory");
    }
    if (qb.isDebugEnabled())
      qb.debug("Changing dir from " + this.ob + " to " + (str.equals("") ? "user default dir" : str));
    this.ob = str;
  }

  protected void finalize()
    throws Throwable
  {
    if (this.tb != null)
      this.tb.close();
    this.tb = null;
    super.finalize();
  }

  public String getDefaultDirectory()
    throws SftpStatusException, SshException
  {
    return this.tb.getDefaultDirectory();
  }

  public void cdup()
    throws SftpStatusException, SshException
  {
    SftpFile localSftpFile1 = this.tb.getFile(this.ob);
    SftpFile localSftpFile2 = localSftpFile1.getParent();
    if (localSftpFile2 != null)
      this.ob = localSftpFile2.getAbsolutePath();
  }

  private File d(String paramString)
  {
    File localFile = new File(paramString);
    if (!localFile.isAbsolute())
      localFile = new File(this.pb, paramString);
    return localFile;
  }

  private boolean h(String paramString)
  {
    paramString = paramString.trim();
    return (paramString.length() > 2) && (((paramString.charAt(0) >= 'a') && (paramString.charAt(0) <= 'z')) || (((paramString.charAt(0) >= 'A') && (paramString.charAt(0) <= 'Z') && (paramString.charAt(1) == ':') && (paramString.charAt(2) == '/')) || (paramString.charAt(2) == '\\')));
  }

  public void addCustomRoot(String paramString)
  {
    this.xb.addElement(paramString);
  }

  public void removeCustomRoot(String paramString)
  {
    this.xb.removeElement(paramString);
  }

  private boolean g(String paramString)
  {
    Enumeration localEnumeration = this.xb.elements();
    while ((localEnumeration != null) && (localEnumeration.hasMoreElements()))
      if (paramString.startsWith((String)localEnumeration.nextElement()))
        return true;
    return false;
  }

  private String b(String paramString)
    throws SftpStatusException
  {
    b();
    String str;
    if ((!paramString.startsWith("/")) && (!paramString.startsWith(this.ob)) && (!h(paramString)) && (!g(paramString)))
      str = this.ob + (this.ob.endsWith("/") ? "" : "/") + paramString;
    else
      str = paramString;
    return str;
  }

  private void b()
    throws SftpStatusException
  {
    if (this.tb.isClosed())
      throw new SftpStatusException(7, "The SFTP connection has been closed");
  }

  public void mkdir(String paramString)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    if (qb.isDebugEnabled())
      qb.debug("Creating dir " + paramString);
    SftpFileAttributes localSftpFileAttributes1 = null;
    try
    {
      localSftpFileAttributes1 = this.tb.getAttributes(str);
    }
    catch (SftpStatusException localSftpStatusException)
    {
      SftpFileAttributes localSftpFileAttributes2 = new SftpFileAttributes(this.tb, 2);
      localSftpFileAttributes2.setPermissions(new UnsignedInteger32(0x1FF ^ this.wb));
      this.tb.makeDirectory(str, localSftpFileAttributes2);
      return;
    }
    if (!localSftpFileAttributes1.isDirectory())
    {
      if (qb.isDebugEnabled())
        qb.debug("File with name " + paramString + " already exists!");
      throw new SftpStatusException(4, "File already exists named " + paramString);
    }
    if (qb.isDebugEnabled())
      qb.debug("Directory " + paramString + " already exists!");
  }

  public void mkdirs(String paramString)
    throws SftpStatusException, SshException
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "/");
    for (String str = paramString.startsWith("/") ? "/" : ""; localStringTokenizer.hasMoreElements(); str = str + "/")
    {
      str = str + (String)localStringTokenizer.nextElement();
      try
      {
        stat(str);
      }
      catch (SftpStatusException localSftpStatusException1)
      {
        try
        {
          mkdir(str);
        }
        catch (SftpStatusException localSftpStatusException2)
        {
          if (localSftpStatusException2.getStatus() != 3)
            continue;
          throw localSftpStatusException2;
        }
      }
    }
  }

  public boolean isDirectoryOrLinkedDirectory(SftpFile paramSftpFile)
    throws SftpStatusException, SshException
  {
    return (paramSftpFile.isDirectory()) || ((paramSftpFile.isLink()) && (stat(paramSftpFile.getAbsolutePath()).isDirectory()));
  }

  public String pwd()
  {
    return this.ob;
  }

  public SftpFile[] ls()
    throws SftpStatusException, SshException
  {
    return ls(this.ob);
  }

  public SftpFile[] ls(String paramString)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    if (qb.isDebugEnabled())
      qb.debug("Listing files for " + str);
    SftpFile localSftpFile = this.tb.openDirectory(str);
    Vector localVector = new Vector();
    while (this.tb.listChildren(localSftpFile, localVector) > -1);
    localSftpFile.close();
    SftpFile[] arrayOfSftpFile = new SftpFile[localVector.size()];
    int i = 0;
    Enumeration localEnumeration = localVector.elements();
    while (localEnumeration.hasMoreElements())
      arrayOfSftpFile[(i++)] = ((SftpFile)localEnumeration.nextElement());
    return arrayOfSftpFile;
  }

  public void lcd(String paramString)
    throws SftpStatusException
  {
    File localFile;
    if (!f(paramString))
      localFile = new File(this.pb, paramString);
    else
      localFile = new File(paramString);
    if (!localFile.isDirectory())
      throw new SftpStatusException(4, paramString + " is not a directory");
    try
    {
      this.pb = localFile.getCanonicalPath();
    }
    catch (IOException localIOException)
    {
      throw new SftpStatusException(4, "Failed to canonicalize path " + paramString);
    }
  }

  private static boolean f(String paramString)
  {
    return new File(paramString).isAbsolute();
  }

  public String lpwd()
  {
    return this.pb;
  }

  public SftpFileAttributes get(String paramString, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString, paramFileTransferProgress, false);
  }

  public SftpFileAttributes get(String paramString, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    String str;
    if (paramString.lastIndexOf("/") > -1)
      str = paramString.substring(paramString.lastIndexOf("/") + 1);
    else
      str = paramString;
    return get(paramString, str, paramFileTransferProgress, paramBoolean);
  }

  public SftpFileAttributes get(String paramString, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString, (FileTransferProgress)null, paramBoolean);
  }

  public SftpFileAttributes get(String paramString)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString, (FileTransferProgress)null);
  }

  public SftpFileAttributes get(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString1, paramString2, paramFileTransferProgress, false);
  }

  public SftpFileAttributes get(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    Object localObject1 = null;
    SftpFileAttributes localSftpFileAttributes1 = null;
    File localFile1 = d(paramString2);
    if (!localFile1.exists())
    {
      File localFile2 = new File(localFile1.getParent());
      localFile2.mkdirs();
    }
    if (localFile1.isDirectory())
    {
      int i;
      if ((i = paramString1.lastIndexOf('/')) > -1)
        localFile1 = new File(localFile1, paramString1.substring(i));
      else
        localFile1 = new File(localFile1, paramString1);
    }
    stat(paramString1);
    long l = 0L;
    try
    {
      if ((paramBoolean) && (localFile1.exists()))
      {
        l = localFile1.length();
        RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile1, "rw");
        localRandomAccessFile.seek(l);
        localObject1 = new _b(localRandomAccessFile);
      }
      else
      {
        localObject1 = new FileOutputStream(localFile1);
      }
      if (this.sb == 2)
      {
        int j = 4;
        int k = 0;
        if (this.tb.getVersion() > 3)
        {
          byte[] arrayOfByte = this.tb.getCanonicalNewline().getBytes();
          switch (arrayOfByte.length)
          {
          case 1:
            if (arrayOfByte[0] == 13)
              j = 3;
            else if (arrayOfByte[0] == 10)
              j = 2;
            else
              throw new SftpStatusException(100, "Unsupported text mode: invalid newline character");
          case 2:
            if ((arrayOfByte[0] == 13) && (arrayOfByte[1] == 10))
              j = 1;
            else
              throw new SftpStatusException(100, "Unsupported text mode: invalid newline characters");
          default:
            throw new SftpStatusException(100, "Unsupported text mode: newline length > 2");
          }
        }
        localObject1 = EOLProcessor.createOutputStream(j, k, (OutputStream)localObject1);
      }
      localSftpFileAttributes1 = get(paramString1, (OutputStream)localObject1, paramFileTransferProgress, l);
      SftpFileAttributes localSftpFileAttributes2 = localSftpFileAttributes1;
      Method localMethod1;
      return localSftpFileAttributes2;
    }
    catch (IOException localIOException)
    {
      throw new SftpStatusException(4, "Failed to open outputstream to " + paramString2);
    }
    finally
    {
      try
      {
        if (localObject1 != null)
          ((OutputStream)localObject1).close();
        if (localSftpFileAttributes1 != null)
        {
          Method localMethod2 = localFile1.getClass().getMethod("setLastModified", new Class[] { Long.TYPE });
          localMethod2.invoke(localFile1, new Object[] { new Long(localSftpFileAttributes1.getModifiedTime().longValue() * 1000L) });
        }
      }
      catch (Throwable localThrowable2)
      {
      }
    }
    throw localObject2;
  }

  public SftpFileAttributes get(String paramString1, String paramString2, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString1, paramString2, null, paramBoolean);
  }

  public SftpFileAttributes get(String paramString1, String paramString2)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString1, paramString2, false);
  }

  public SftpFileAttributes get(String paramString, OutputStream paramOutputStream, FileTransferProgress paramFileTransferProgress)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString, paramOutputStream, paramFileTransferProgress, 0L);
  }

  public void setRegularExpressionSyntax(int paramInt)
  {
    this.vb = paramInt;
  }

  private SftpFile[] c(String paramString)
    throws SftpStatusException, SshException
  {
    int i;
    String str1;
    String str2;
    if ((i = paramString.lastIndexOf("/")) > -1)
    {
      str1 = paramString.substring(0, i);
      str2 = paramString.length() > i + 1 ? paramString.substring(i + 1) : "";
    }
    else
    {
      str1 = this.ob;
      str2 = paramString;
    }
    Object localObject;
    SftpFile[] arrayOfSftpFile;
    switch (this.vb)
    {
    case 1:
      localObject = new GlobRegExpMatching();
      arrayOfSftpFile = ls(str1);
      break;
    case 2:
      localObject = new Perl5RegExpMatching();
      arrayOfSftpFile = ls(str1);
      break;
    default:
      localObject = new NoRegExpMatching();
      arrayOfSftpFile = new SftpFile[1];
      String str3 = b(paramString);
      arrayOfSftpFile[0] = getSubsystemChannel().getFile(str3);
    }
    return (SftpFile)((RegularExpressionMatching)localObject).matchFilesWithPattern(arrayOfSftpFile, str2);
  }

  private SftpFile[] b(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    SftpFile[] arrayOfSftpFile1 = c(paramString1);
    Vector localVector = new Vector();
    for (int i = 0; i < arrayOfSftpFile1.length; i++)
    {
      get(arrayOfSftpFile1[i].getAbsolutePath(), paramString2, paramFileTransferProgress, paramBoolean);
      localVector.addElement(arrayOfSftpFile1[i]);
    }
    SftpFile[] arrayOfSftpFile2 = new SftpFile[localVector.size()];
    localVector.copyInto(arrayOfSftpFile2);
    return arrayOfSftpFile2;
  }

  private String[] e(String paramString)
    throws SftpStatusException, SshException
  {
    int i;
    String str1;
    String str2;
    if (((i = paramString.lastIndexOf(System.getProperty("file.separator"))) > -1) || ((i = paramString.lastIndexOf('/')) > -1))
    {
      str1 = d(paramString.substring(0, i)).getAbsolutePath();
      str2 = i < paramString.length() - 1 ? paramString.substring(i + 1) : "";
    }
    else
    {
      str1 = this.pb;
      str2 = paramString;
    }
    File localFile;
    Object localObject;
    File[] arrayOfFile;
    switch (this.vb)
    {
    case 1:
      localFile = new File(str1);
      localObject = new GlobRegExpMatching();
      arrayOfFile = b(localFile);
      break;
    case 2:
      localFile = new File(str1);
      localObject = new Perl5RegExpMatching();
      arrayOfFile = b(localFile);
      break;
    default:
      localObject = new NoRegExpMatching();
      arrayOfFile = new File[1];
      arrayOfFile[0] = new File(paramString);
    }
    return (String)((RegularExpressionMatching)localObject).matchFileNamesWithPattern(arrayOfFile, str2);
  }

  private File[] b(File paramFile)
  {
    String str = paramFile.getAbsolutePath();
    String[] arrayOfString = paramFile.list();
    File[] arrayOfFile = new File[arrayOfString.length];
    for (int i = 0; i < arrayOfString.length; i++)
      arrayOfFile[i] = new File(str, arrayOfString[i]);
    return arrayOfFile;
  }

  private void c(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    String str = b(paramString2);
    SftpFileAttributes localSftpFileAttributes = null;
    try
    {
      localSftpFileAttributes = stat(str);
    }
    catch (SftpStatusException localSftpStatusException1)
    {
      throw new SftpStatusException(localSftpStatusException1.getStatus(), "Remote path '" + paramString2 + "' does not exist. It must be a valid directory and must already exist!");
    }
    if (!localSftpFileAttributes.isDirectory())
      throw new SftpStatusException(10, "Remote path '" + paramString2 + "' is not a directory!");
    String[] arrayOfString = e(paramString1);
    for (int i = 0; i < arrayOfString.length; i++)
      try
      {
        put(arrayOfString[i], str, paramFileTransferProgress, paramBoolean);
      }
      catch (SftpStatusException localSftpStatusException2)
      {
        throw new SftpStatusException(localSftpStatusException2.getStatus(), "Failed to put " + arrayOfString[i] + " to " + paramString2 + " [" + localSftpStatusException2.getMessage() + "]");
      }
  }

  public SftpFileAttributes get(String paramString, OutputStream paramOutputStream, FileTransferProgress paramFileTransferProgress, long paramLong)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    String str = b(paramString);
    SftpFileAttributes localSftpFileAttributes = this.tb.getAttributes(str);
    if (paramLong > localSftpFileAttributes.getSize().longValue())
      throw new SftpStatusException(101, "The local file size is greater than the remote file");
    if (paramFileTransferProgress != null)
      paramFileTransferProgress.started(localSftpFileAttributes.getSize().longValue(), str);
    SftpFile localSftpFile;
    if ((this.sb == 2) && (this.tb.getVersion() > 3))
      localSftpFile = this.tb.openFile(str, 65);
    else
      localSftpFile = this.tb.openFile(str, 1);
    try
    {
      this.tb.performOptimizedRead(localSftpFile.getHandle(), localSftpFileAttributes.getSize().longValue(), this.nb, paramOutputStream, this.ub, paramFileTransferProgress, paramLong);
    }
    catch (TransferCancelledException localTransferCancelledException)
    {
      throw localTransferCancelledException;
    }
    finally
    {
      try
      {
        this.tb.closeFile(localSftpFile);
      }
      catch (SftpStatusException localSftpStatusException2)
      {
      }
      try
      {
        paramOutputStream.close();
      }
      catch (Throwable localThrowable2)
      {
      }
    }
    if (paramFileTransferProgress != null)
      paramFileTransferProgress.completed();
    return localSftpFileAttributes;
  }

  public InputStream getInputStream(String paramString, long paramLong)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    this.tb.getAttributes(str);
    return new SftpFileInputStream(this.tb.openFile(str, 1), paramLong);
  }

  public InputStream getInputStream(String paramString)
    throws SftpStatusException, SshException
  {
    return getInputStream(paramString, 0L);
  }

  public SftpFileAttributes get(String paramString, OutputStream paramOutputStream, long paramLong)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString, paramOutputStream, null, paramLong);
  }

  public SftpFileAttributes get(String paramString, OutputStream paramOutputStream)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    return get(paramString, paramOutputStream, null, 0L);
  }

  public boolean isClosed()
  {
    return this.tb.isClosed();
  }

  public void put(String paramString, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    File localFile = new File(paramString);
    put(paramString, localFile.getName(), paramFileTransferProgress, paramBoolean);
  }

  public void put(String paramString, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    put(paramString, paramFileTransferProgress, false);
  }

  public void put(String paramString)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    put(paramString, false);
  }

  public void put(String paramString, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    put(paramString, (FileTransferProgress)null, paramBoolean);
  }

  public void put(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    put(paramString1, paramString2, paramFileTransferProgress, false);
  }

  public void put(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    File localFile = d(paramString1);
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    long l = 0L;
    try
    {
      SftpFileAttributes localSftpFileAttributes = stat(paramString2);
      if (localSftpFileAttributes.isDirectory())
      {
        paramString2 = paramString2 + (paramString2.endsWith("/") ? "" : "/") + localFile.getName();
        localSftpFileAttributes = stat(paramString2);
      }
      if (paramBoolean)
      {
        if (localFile.length() <= localSftpFileAttributes.getSize().longValue())
          throw new SftpStatusException(101, "The remote file size is greater than the local file");
        try
        {
          l = localSftpFileAttributes.getSize().longValue();
          localFileInputStream.skip(l);
        }
        catch (IOException localIOException)
        {
          throw new SftpStatusException(2, localIOException.getMessage());
        }
      }
    }
    catch (SftpStatusException localSftpStatusException)
    {
    }
    put(localFileInputStream, paramString2, paramFileTransferProgress, l);
  }

  public void put(String paramString1, String paramString2, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    put(paramString1, paramString2, null, paramBoolean);
  }

  public void put(String paramString1, String paramString2)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    put(paramString1, paramString2, null, false);
  }

  public void put(InputStream paramInputStream, String paramString, FileTransferProgress paramFileTransferProgress)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    put(paramInputStream, paramString, paramFileTransferProgress, 0L);
  }

  public void put(InputStream paramInputStream, String paramString, FileTransferProgress paramFileTransferProgress, long paramLong)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    String str = b(paramString);
    SftpFileAttributes localSftpFileAttributes = null;
    if (this.sb == 2)
    {
      int i = 4;
      int j = this.rb;
      byte[] arrayOfByte = null;
      if ((this.tb.getVersion() <= 3) && (this.tb.getExtension("newline@vandyke.com") != null))
        arrayOfByte = this.tb.getExtension("newline@vandyke.com").getBytes();
      else if (this.tb.getVersion() > 3)
        arrayOfByte = this.tb.getCanonicalNewline().getBytes();
      if (arrayOfByte != null)
        switch (arrayOfByte.length)
        {
        case 1:
          if (arrayOfByte[0] == 13)
            j = 3;
          else if (arrayOfByte[0] == 10)
            j = 2;
          else
            throw new SftpStatusException(100, "Unsupported text mode: invalid newline character");
        case 2:
          if ((arrayOfByte[0] == 13) && (arrayOfByte[1] == 10))
            j = 1;
          else
            throw new SftpStatusException(100, "Unsupported text mode: invalid newline characters");
        default:
          throw new SftpStatusException(100, "Unsupported text mode: newline length > 2");
        }
      try
      {
        paramInputStream = EOLProcessor.createInputStream(i, j, paramInputStream);
      }
      catch (IOException localIOException2)
      {
        throw new SshException("Failed to create EOL processing stream", 5);
      }
    }
    SftpFile localSftpFile;
    if (paramLong > 0L)
    {
      if ((this.sb == 2) && (this.tb.getVersion() > 3))
        throw new SftpStatusException(8, "Resume on text mode files is not supported");
      localSftpFile = this.tb.openFile(str, 6, localSftpFileAttributes);
    }
    else if ((this.sb == 2) && (this.tb.getVersion() > 3))
    {
      localSftpFile = this.tb.openFile(str, 90, localSftpFileAttributes);
    }
    else
    {
      localSftpFile = this.tb.openFile(str, 26, localSftpFileAttributes);
    }
    if (paramFileTransferProgress != null)
      try
      {
        paramFileTransferProgress.started(paramInputStream.available(), str);
      }
      catch (IOException localIOException1)
      {
        throw new SshException("Failed to determine local file size", 5);
      }
    try
    {
      this.tb.performOptimizedWrite(localSftpFile.getHandle(), this.nb, this.ub, paramInputStream, this.mb, paramFileTransferProgress, paramLong);
    }
    finally
    {
      try
      {
        this.tb.closeFile(localSftpFile);
      }
      catch (SftpStatusException localSftpStatusException2)
      {
      }
      try
      {
        paramInputStream.close();
      }
      catch (Throwable localThrowable2)
      {
      }
    }
    if (paramFileTransferProgress != null)
      paramFileTransferProgress.completed();
  }

  public OutputStream getOutputStream(String paramString)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    return new SftpFileOutputStream(this.tb.openFile(str, 26));
  }

  public void put(InputStream paramInputStream, String paramString, long paramLong)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    put(paramInputStream, paramString, null, paramLong);
  }

  public void put(InputStream paramInputStream, String paramString)
    throws SftpStatusException, SshException, TransferCancelledException
  {
    put(paramInputStream, paramString, null, 0L);
  }

  public void chown(String paramString1, String paramString2)
    throws SftpStatusException, SshException
  {
    String str = b(paramString2);
    SftpFileAttributes localSftpFileAttributes = this.tb.getAttributes(str);
    localSftpFileAttributes.setUID(paramString1);
    this.tb.setAttributes(str, localSftpFileAttributes);
  }

  public void chgrp(String paramString1, String paramString2)
    throws SftpStatusException, SshException
  {
    String str = b(paramString2);
    SftpFileAttributes localSftpFileAttributes = this.tb.getAttributes(str);
    localSftpFileAttributes.setGID(paramString1);
    this.tb.setAttributes(str, localSftpFileAttributes);
  }

  public void chmod(int paramInt, String paramString)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    this.tb.changePermissions(str, paramInt);
  }

  public void umask(String paramString)
    throws SshException
  {
    try
    {
      this.wb = Integer.parseInt(paramString, 8);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new SshException("umask must be 4 digit octal number e.g. 0022", 4);
    }
  }

  public void rename(String paramString1, String paramString2)
    throws SftpStatusException, SshException
  {
    String str1 = b(paramString1);
    String str2 = b(paramString2);
    SftpFileAttributes localSftpFileAttributes = null;
    try
    {
      localSftpFileAttributes = this.tb.getAttributes(str2);
    }
    catch (SftpStatusException localSftpStatusException)
    {
      this.tb.renameFile(str1, str2);
      return;
    }
    if ((localSftpFileAttributes != null) && (localSftpFileAttributes.isDirectory()))
      this.tb.renameFile(str1, str2);
    else
      throw new SftpStatusException(11, paramString2 + " already exists on the remote filesystem");
  }

  public void rm(String paramString)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    SftpFileAttributes localSftpFileAttributes = this.tb.getAttributes(str);
    if (localSftpFileAttributes.isDirectory())
      this.tb.removeDirectory(str);
    else
      this.tb.removeFile(str);
  }

  public void rm(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    SftpFileAttributes localSftpFileAttributes = null;
    localSftpFileAttributes = this.tb.getAttributes(str);
    if (localSftpFileAttributes.isDirectory())
    {
      SftpFile[] arrayOfSftpFile = ls(paramString);
      if ((!paramBoolean1) && (arrayOfSftpFile.length > 0))
        throw new SftpStatusException(4, "You cannot delete non-empty directory, use force=true to overide");
      for (int i = 0; i < arrayOfSftpFile.length; i++)
      {
        SftpFile localSftpFile = arrayOfSftpFile[i];
        if ((localSftpFile.isDirectory()) && (!localSftpFile.getFilename().equals(".")) && (!localSftpFile.getFilename().equals("..")))
        {
          if (paramBoolean2)
            rm(localSftpFile.getAbsolutePath(), paramBoolean1, paramBoolean2);
          else
            throw new SftpStatusException(4, "Directory has contents, cannot delete without recurse=true");
        }
        else
        {
          if (!localSftpFile.isFile())
            continue;
          this.tb.removeFile(localSftpFile.getAbsolutePath());
        }
      }
      this.tb.removeDirectory(str);
    }
    else
    {
      this.tb.removeFile(str);
    }
  }

  public void symlink(String paramString1, String paramString2)
    throws SftpStatusException, SshException
  {
    String str1 = b(paramString1);
    String str2 = b(paramString2);
    this.tb.createSymbolicLink(str1, str2);
  }

  public SftpFileAttributes stat(String paramString)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    return this.tb.getAttributes(str);
  }

  public String getAbsolutePath(String paramString)
    throws SftpStatusException, SshException
  {
    String str = b(paramString);
    return this.tb.getAbsolutePath(str);
  }

  public void quit()
    throws SshException
  {
    try
    {
      this.tb.close();
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException.getMessage(), 6);
    }
  }

  public void exit()
    throws SshException
  {
    try
    {
      this.tb.close();
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException.getMessage(), 6);
    }
  }

  public DirectoryOperation copyLocalDirectory(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    DirectoryOperation localDirectoryOperation = new DirectoryOperation();
    File localFile1 = d(paramString1);
    paramString2 = b(paramString2);
    paramString2 = paramString2 + (paramString2.endsWith("/") ? "" : "/");
    if (paramBoolean3)
      try
      {
        this.tb.getAttributes(paramString2);
      }
      catch (SftpStatusException localSftpStatusException1)
      {
        mkdirs(paramString2);
      }
    String[] arrayOfString = localFile1.list();
    if (arrayOfString != null)
      for (int i = 0; i < arrayOfString.length; i++)
      {
        File localFile2 = new File(localFile1, arrayOfString[i]);
        if ((localFile2.isDirectory()) && (!localFile2.getName().equals(".")) && (!localFile2.getName().equals("..")))
        {
          if (!paramBoolean1)
            continue;
          localDirectoryOperation.addDirectoryOperation(copyLocalDirectory(localFile2.getAbsolutePath(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, paramFileTransferProgress), localFile2);
        }
        else
        {
          if (!localFile2.isFile())
            continue;
          int j = 0;
          int k = 0;
          try
          {
            SftpFileAttributes localSftpFileAttributes1 = this.tb.getAttributes(paramString2 + localFile2.getName());
            k = (localFile2.length() == localSftpFileAttributes1.getSize().longValue()) && (localFile2.lastModified() / 1000L == localSftpFileAttributes1.getModifiedTime().longValue()) ? 1 : 0;
          }
          catch (SftpStatusException localSftpStatusException3)
          {
            j = 1;
          }
          try
          {
            if ((paramBoolean3) && (k == 0))
            {
              put(localFile2.getAbsolutePath(), paramString2 + localFile2.getName(), paramFileTransferProgress);
              SftpFileAttributes localSftpFileAttributes2 = this.tb.getAttributes(paramString2 + localFile2.getName());
              localSftpFileAttributes2.setTimes(new UnsignedInteger64(localFile2.lastModified() / 1000L), new UnsignedInteger64(localFile2.lastModified() / 1000L));
              this.tb.setAttributes(paramString2 + localFile2.getName(), localSftpFileAttributes2);
            }
            if (k != 0)
              localDirectoryOperation.c(localFile2);
            else if (j == 0)
              localDirectoryOperation.d(localFile2);
            else
              localDirectoryOperation.b(localFile2);
          }
          catch (SftpStatusException localSftpStatusException4)
          {
            localDirectoryOperation.b(localFile2, localSftpStatusException4);
          }
        }
      }
    if (paramBoolean2)
      try
      {
        SftpFile[] arrayOfSftpFile = ls(paramString2);
        for (int m = 0; m < arrayOfSftpFile.length; m++)
        {
          SftpFile localSftpFile = arrayOfSftpFile[m];
          File localFile3 = new File(localFile1, localSftpFile.getFilename());
          if ((localDirectoryOperation.containsFile(localFile3)) || (localSftpFile.getFilename().equals(".")) || (localSftpFile.getFilename().equals("..")))
            continue;
          localDirectoryOperation.c(localSftpFile);
          if (!paramBoolean3)
            continue;
          if (localSftpFile.isDirectory())
          {
            b(localSftpFile, localDirectoryOperation);
            if (!paramBoolean3)
              continue;
            rm(localSftpFile.getAbsolutePath(), true, true);
          }
          else
          {
            if (!localSftpFile.isFile())
              continue;
            rm(localSftpFile.getAbsolutePath());
          }
        }
      }
      catch (SftpStatusException localSftpStatusException2)
      {
      }
    return localDirectoryOperation;
  }

  private void b(SftpFile paramSftpFile, DirectoryOperation paramDirectoryOperation)
    throws SftpStatusException, SshException
  {
    SftpFile[] arrayOfSftpFile = ls(paramSftpFile.getAbsolutePath());
    paramDirectoryOperation.c(paramSftpFile);
    for (int i = 0; i < arrayOfSftpFile.length; i++)
    {
      paramSftpFile = arrayOfSftpFile[i];
      if ((paramSftpFile.isDirectory()) && (!paramSftpFile.getFilename().equals(".")) && (!paramSftpFile.getFilename().equals("..")))
      {
        b(paramSftpFile, paramDirectoryOperation);
      }
      else
      {
        if (!paramSftpFile.isFile())
          continue;
        paramDirectoryOperation.c(paramSftpFile);
      }
    }
  }

  private void b(File paramFile, DirectoryOperation paramDirectoryOperation)
    throws SftpStatusException, SshException
  {
    String[] arrayOfString = paramFile.list();
    paramDirectoryOperation.e(paramFile);
    if (arrayOfString != null)
      for (int i = 0; i < arrayOfString.length; i++)
      {
        paramFile = new File(arrayOfString[i]);
        if ((paramFile.isDirectory()) && (!paramFile.getName().equals(".")) && (!paramFile.getName().equals("..")))
        {
          b(paramFile, paramDirectoryOperation);
        }
        else
        {
          if (!paramFile.isFile())
            continue;
          paramDirectoryOperation.e(paramFile);
        }
      }
  }

  public static String formatLongname(SftpFile paramSftpFile)
    throws SftpStatusException, SshException
  {
    return formatLongname(paramSftpFile.getAttributes(), paramSftpFile.getFilename());
  }

  public static String formatLongname(SftpFileAttributes paramSftpFileAttributes, String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(b(10 - paramSftpFileAttributes.getPermissionsString().length()) + paramSftpFileAttributes.getPermissionsString());
    localStringBuffer.append("    1 ");
    localStringBuffer.append(paramSftpFileAttributes.getUID() + b(8 - paramSftpFileAttributes.getUID().length()));
    localStringBuffer.append(" ");
    localStringBuffer.append(paramSftpFileAttributes.getGID() + b(8 - paramSftpFileAttributes.getGID().length()));
    localStringBuffer.append(" ");
    localStringBuffer.append(b(8 - paramSftpFileAttributes.getSize().toString().length()) + paramSftpFileAttributes.getSize().toString());
    localStringBuffer.append(" ");
    localStringBuffer.append(b(12 - b(paramSftpFileAttributes.getModifiedTime()).length()) + b(paramSftpFileAttributes.getModifiedTime()));
    localStringBuffer.append(" ");
    localStringBuffer.append(paramString);
    return localStringBuffer.toString();
  }

  private static String b(UnsignedInteger64 paramUnsignedInteger64)
  {
    if (paramUnsignedInteger64 == null)
      return "";
    long l1 = paramUnsignedInteger64.longValue() * 1000L;
    long l2 = System.currentTimeMillis();
    SimpleDateFormat localSimpleDateFormat;
    if (l2 - l1 > 15552000000L)
      localSimpleDateFormat = new SimpleDateFormat("MMM dd  yyyy");
    else
      localSimpleDateFormat = new SimpleDateFormat("MMM dd hh:mm");
    return localSimpleDateFormat.format(new Date(l1));
  }

  private static String b(int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer("");
    if (paramInt > 0)
      for (int i = 0; i < paramInt; i++)
        localStringBuffer.append(" ");
    return localStringBuffer.toString();
  }

  public DirectoryOperation copyRemoteDirectory(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    DirectoryOperation localDirectoryOperation = new DirectoryOperation();
    String str1 = pwd();
    cd(paramString1);
    String str2 = paramString1;
    if (str2.endsWith("/"))
      str2 = str2.substring(0, str2.length() - 1);
    int i = str2.lastIndexOf('/');
    if (i != -1)
      str2 = str2.substring(i + 1);
    File localFile1 = new File(paramString2, str2);
    if (!localFile1.isAbsolute())
      localFile1 = new File(lpwd(), paramString2);
    if ((!localFile1.exists()) && (paramBoolean3))
      localFile1.mkdir();
    SftpFile[] arrayOfSftpFile = ls();
    for (int j = 0; j < arrayOfSftpFile.length; j++)
    {
      SftpFile localSftpFile = arrayOfSftpFile[j];
      File localFile2;
      if ((localSftpFile.isDirectory()) && (!localSftpFile.getFilename().equals(".")) && (!localSftpFile.getFilename().equals("..")))
      {
        if (!paramBoolean1)
          continue;
        localFile2 = new File(localFile1, localSftpFile.getFilename());
        localDirectoryOperation.addDirectoryOperation(copyRemoteDirectory(localSftpFile.getFilename(), localFile1.getAbsolutePath(), paramBoolean1, paramBoolean2, paramBoolean3, paramFileTransferProgress), localFile2);
      }
      else
      {
        if (!localSftpFile.isFile())
          continue;
        localFile2 = new File(localFile1, localSftpFile.getFilename());
        if ((localFile2.exists()) && (localFile2.length() == localSftpFile.getAttributes().getSize().longValue()) && (localFile2.lastModified() / 1000L == localSftpFile.getAttributes().getModifiedTime().longValue()))
        {
          if (paramBoolean3)
            localDirectoryOperation.c(localFile2);
          else
            localDirectoryOperation.d(localSftpFile);
        }
        else
          try
          {
            if (localFile2.exists())
            {
              if (paramBoolean3)
                localDirectoryOperation.d(localFile2);
              else
                localDirectoryOperation.e(localSftpFile);
            }
            else if (paramBoolean3)
              localDirectoryOperation.b(localFile2);
            else
              localDirectoryOperation.b(localSftpFile);
            if (paramBoolean3)
              get(localSftpFile.getFilename(), localFile2.getAbsolutePath(), paramFileTransferProgress);
          }
          catch (SftpStatusException localSftpStatusException)
          {
            localDirectoryOperation.b(localFile2, localSftpStatusException);
          }
      }
    }
    if (paramBoolean2)
    {
      String[] arrayOfString = localFile1.list();
      if (arrayOfString != null)
        for (int k = 0; k < arrayOfString.length; k++)
        {
          File localFile3 = new File(localFile1, arrayOfString[k]);
          if (localDirectoryOperation.containsFile(localFile3))
            continue;
          localDirectoryOperation.e(localFile3);
          if ((localFile3.isDirectory()) && (!localFile3.getName().equals(".")) && (!localFile3.getName().equals("..")))
          {
            b(localFile3, localDirectoryOperation);
            if (!paramBoolean3)
              continue;
            IOUtil.recurseDeleteDirectory(localFile3);
          }
          else
          {
            if (!paramBoolean3)
              continue;
            localFile3.delete();
          }
        }
    }
    cd(str1);
    return localDirectoryOperation;
  }

  public SftpFile[] getFiles(String paramString)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return getFiles(paramString, (FileTransferProgress)null);
  }

  public SftpFile[] getFiles(String paramString, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return getFiles(paramString, (FileTransferProgress)null, paramBoolean);
  }

  public SftpFile[] getFiles(String paramString, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return getFiles(paramString, paramFileTransferProgress, false);
  }

  public SftpFile[] getFiles(String paramString, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return getFiles(paramString, this.pb, paramFileTransferProgress, paramBoolean);
  }

  public SftpFile[] getFiles(String paramString1, String paramString2)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return getFiles(paramString1, paramString2, false);
  }

  public SftpFile[] getFiles(String paramString1, String paramString2, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return getFiles(paramString1, paramString2, null, paramBoolean);
  }

  public SftpFile[] getFiles(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    return b(paramString1, paramString2, paramFileTransferProgress, paramBoolean);
  }

  public void putFiles(String paramString)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    putFiles(paramString, false);
  }

  public void putFiles(String paramString, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    putFiles(paramString, (FileTransferProgress)null, paramBoolean);
  }

  public void putFiles(String paramString, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    putFiles(paramString, paramFileTransferProgress, false);
  }

  public void putFiles(String paramString, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    putFiles(paramString, pwd(), paramFileTransferProgress, paramBoolean);
  }

  public void putFiles(String paramString1, String paramString2)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    putFiles(paramString1, paramString2, null, false);
  }

  public void putFiles(String paramString1, String paramString2, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    putFiles(paramString1, paramString2, null, paramBoolean);
  }

  public void putFiles(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    putFiles(paramString1, paramString2, paramFileTransferProgress, false);
  }

  public void putFiles(String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress, boolean paramBoolean)
    throws FileNotFoundException, SftpStatusException, SshException, TransferCancelledException
  {
    c(paramString1, paramString2, paramFileTransferProgress, paramBoolean);
  }

  static class _b extends OutputStream
  {
    RandomAccessFile b;

    _b(RandomAccessFile paramRandomAccessFile)
    {
      this.b = paramRandomAccessFile;
    }

    public void write(int paramInt)
      throws IOException
    {
      this.b.write(paramInt);
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      this.b.write(paramArrayOfByte, paramInt1, paramInt2);
    }

    public void close()
      throws IOException
    {
      this.b.close();
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.sftp.SftpClient
 * JD-Core Version:    0.6.0
 */