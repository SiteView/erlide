package com.maverick.sshd.vfs;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.sshd.SessionChannel;
import com.maverick.sshd.SftpFile;
import com.maverick.sshd.SftpFileAttributes;
import com.maverick.sshd.SshContext;
import com.maverick.sshd.components.ServerComponentManager;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.sshd.platform.InvalidHandleException;
import com.maverick.sshd.platform.NativeFileSystemProvider;
import com.maverick.sshd.platform.PermissionDeniedException;
import com.maverick.sshd.platform.UnsupportedFileOperationException;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

public class VirtualFileSystem
  implements NativeFileSystemProvider
{
  private Map D = Collections.synchronizedMap(new HashMap());
  static String I = "/home/";
  Map L;
  A C;
  VFSPermissionHandler K = null;
  byte[] E;
  SshContext B;
  AuthenticationProvider H;
  String G;
  String A;
  String F;
  String J;

  public void init(byte[] paramArrayOfByte, SessionChannel paramSessionChannel, SshContext paramSshContext)
    throws IOException
  {
    this.L = Collections.synchronizedMap(new HashMap());
    this.E = paramArrayOfByte;
    for (int i = 0; ; i++)
      if (System.getProperties().containsKey("com.maverick.sshd.vfs.VFSMount." + String.valueOf(i)))
      {
        String str1 = System.getProperty("com.maverick.sshd.vfs.VFSMount." + String.valueOf(i));
        int j = str1.indexOf("=");
        if (j > -1)
        {
          String str3 = str1.substring(0, j);
          String str4 = str1.substring(j + 1);
          if (str3.startsWith("/"))
          {
            File localFile2 = new File(str4);
            if ((localFile2.exists()) && (localFile2.isDirectory()))
              this.L.put(str3, new A(str3, str4));
          }
        }
      }
      else
      {
        if (i >= 1)
          break;
      }
    String str2 = null;
    try
    {
      str2 = System.getProperty("com.maverick.sshd.vfs.VFSRoot");
    }
    catch (SecurityException localSecurityException)
    {
    }
    if ((str2 != null) && (str2.length() > 0))
    {
      this.C = new A("/", str2);
      this.C.A(true);
      File localFile1 = new File(this.C.A());
      if ((!localFile1.exists()) || (!localFile1.isDirectory()));
    }
    if (System.getProperty("com.maverick.sshd.vfs.VFSPermissionHandler") != null)
      try
      {
        this.K = ((VFSPermissionHandler)Class.forName(System.getProperty("com.maverick.sshd.vfs.VFSPermissionHandler")).newInstance());
      }
      catch (Throwable localThrowable)
      {
      }
    if (this.K == null)
      this.K = new _C();
    this.E = paramArrayOfByte;
    this.B = paramSshContext;
    this.H = paramSshContext.getAuthenticationProvider();
    this.F = AuthenticationProvider.getUsername(paramArrayOfByte);
    this.J = this.H.getGroup(paramArrayOfByte);
    this.G = this.H.getHomeDirectory(paramArrayOfByte).replace('\\', '/');
    this.G = translateCanonicalPath(this.G, this.G);
    this.A = A(this.F);
  }

  protected void addMount(String paramString1, String paramString2)
    throws IOException
  {
    this.L.put(paramString1, new A(paramString1, paramString2));
  }

  public void closeFilesystem()
  {
    Iterator localIterator = this.D.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      try
      {
        if (closeFile(((String)localObject).getBytes(), false))
          localIterator.remove();
      }
      catch (Exception localException)
      {
      }
    }
  }

  public void init(SessionChannel paramSessionChannel, SshContext paramSshContext)
    throws IOException
  {
    init(paramSessionChannel.getSessionIdentifier(), paramSessionChannel, paramSshContext);
  }

  private String A(String paramString)
    throws FileNotFoundException
  {
    if (this.K != null)
      return this.K.getVFSHomeDirectory(paramString);
    return I + paramString + "/";
  }

  public String translateNFSPath(String paramString)
    throws IOException, FileNotFoundException
  {
    paramString = paramString.replace('\\', '/');
    if (paramString.startsWith("./"))
      paramString = paramString.substring(2);
    String str2;
    try
    {
      paramString = translateCanonicalPath(paramString, this.G);
      return this.A + paramString.substring(this.G.length());
    }
    catch (FileNotFoundException localIterator)
    {
      Iterator localIterator = this.L.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str1 = (String)localEntry.getKey();
        A localA = (A)localEntry.getValue();
        str2 = localA.A();
        try
        {
          paramString = translateCanonicalPath(paramString, str2);
          int i = paramString.indexOf(str2);
          String str3 = paramString.substring(i + str2.length());
          return str1 + (str3.startsWith("/") ? str3 : new StringBuffer().append("/").append(str3).toString());
        }
        catch (FileNotFoundException localFileNotFoundException2)
        {
        }
      }
      paramString = translateCanonicalPath(paramString, this.C.A());
      str2 = paramString.substring(this.C.A().length());
      if (str2.startsWith("/"))
        tmpTernaryOp = str2;
    }
    return "/" + str2;
  }

  public String translateVFSPath(String paramString)
    throws IOException, FileNotFoundException
  {
    paramString = paramString.replace('\\', '/').trim();
    if (paramString.equals(""))
      return this.G;
    Object localObject;
    if (paramString.startsWith("/"))
    {
      try
      {
        if (paramString.startsWith(this.A))
        {
          if (paramString.length() > this.A.length())
          {
            String str1 = paramString.substring(this.A.length());
            if (str1.startsWith("/"))
              str1 = str1.substring(1);
            return translateCanonicalPath(this.G + (this.G.endsWith("/") ? "" : "/") + str1, this.G);
          }
          return translateCanonicalPath(this.G, this.G);
        }
      }
      catch (FileNotFoundException localFileNotFoundException1)
      {
      }
      localObject = this.L.entrySet().iterator();
      String str3;
      while (((Iterator)localObject).hasNext())
      {
        Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
        String str2 = (String)localEntry.getKey();
        A localA = (A)localEntry.getValue();
        str3 = localA.A();
        if (!paramString.startsWith(str2))
          continue;
        String str4 = str3 + paramString.substring(str2.length());
        return translateCanonicalPath(str4, str3);
      }
      if (this.C != null)
      {
        str3 = this.C.A() + (this.C.A().endsWith("/") ? paramString.substring(1) : paramString);
        return translateCanonicalPath(str3, this.C.A());
      }
      throw new FileNotFoundException("The file could not be found");
    }
    try
    {
      localObject = this.G + (this.G.endsWith("/") ? "" : "/") + paramString;
      return translateCanonicalPath((String)localObject, this.G);
    }
    catch (FileNotFoundException localFileNotFoundException2)
    {
    }
    throw new FileNotFoundException("Only fully qualified VFS paths can be translated outside of a user context");
  }

  protected String translateCanonicalPath(String paramString1, String paramString2)
    throws FileNotFoundException, IOException
  {
    try
    {
      boolean bool = Boolean.getBoolean("maverick.alternativeCanonicalize");
      int i = paramString1.indexOf("..") > -1 ? 1 : 0;
      File localFile1 = new File(paramString1);
      String str1 = (i != 0) || (!bool) ? localFile1.getCanonicalPath().replace('\\', '/') : localFile1.getAbsolutePath().replace('\\', '/');
      File localFile2 = new File(paramString2);
      String str2 = localFile2.getCanonicalPath().replace('\\', '/');
      if (!str2.endsWith("/"))
        str2 = str2 + "/";
      if (!str1.endsWith("/"))
        str1 = str1 + "/";
      if (str1.startsWith(str2))
        return localFile1.getCanonicalPath().replace('\\', '/');
      throw new FileNotFoundException("Path could not be found");
    }
    catch (IOException localIOException)
    {
    }
    throw new FileNotFoundException("Path could not be found");
  }

  public boolean makeDirectory(String paramString)
    throws PermissionDeniedException, FileNotFoundException, IOException
  {
    paramString = translateVFSPath(paramString);
    File localFile = new File(paramString);
    if (this.K.canWrite(this.F, this.J, paramString))
    {
      localFile.mkdirs();
      return localFile.exists();
    }
    throw new PermissionDeniedException("User does not have permission to create directory here.");
  }

  public SftpFileAttributes getFileAttributes(byte[] paramArrayOfByte)
    throws IOException, InvalidHandleException
  {
    String str = A(paramArrayOfByte);
    if (this.D.containsKey(str))
    {
      Object localObject = this.D.get(str);
      File localFile;
      if ((localObject instanceof _B))
        localFile = ((_B)localObject).C();
      else if ((localObject instanceof _A))
        localFile = ((_A)localObject).B();
      else
        throw new IOException("Unexpected open file handle");
      SftpFileAttributes localSftpFileAttributes = this.K.getPermissions(this.F, localFile.getAbsolutePath());
      localSftpFileAttributes.setSize(new UnsignedInteger64(String.valueOf(localFile.length())));
      long l = localFile.lastModified() / 1000L;
      if (l < 0L)
        l = 0L;
      else if (l > 4294967295L)
        l = 4294967295L;
      localSftpFileAttributes.setTimes(new UnsignedInteger32(l), new UnsignedInteger32(l));
      int i = 1;
      try
      {
        if (System.getSecurityManager() != null)
          System.getSecurityManager().checkExec(localFile.getCanonicalPath());
      }
      catch (SecurityException localSecurityException)
      {
        i = 0;
      }
      return localSftpFileAttributes;
    }
    throw new InvalidHandleException("The handle is invalid");
  }

  public SftpFileAttributes getFileAttributes(String paramString)
    throws IOException, FileNotFoundException
  {
    paramString = translateVFSPath(paramString);
    if (!this.K.canRead(this.F, this.J, paramString))
      throw new FileNotFoundException("File not found");
    File localFile = new File(paramString);
    if (!localFile.exists())
      throw new FileNotFoundException(translateNFSPath(paramString) + " doesn't exist");
    SftpFileAttributes localSftpFileAttributes = this.K.getPermissions(this.F, localFile.getAbsolutePath());
    localSftpFileAttributes.setSize(new UnsignedInteger64(String.valueOf(localFile.length())));
    long l = localFile.lastModified() / 1000L;
    if (l < 0L)
      l = 0L;
    else if (l > 4294967295L)
      l = 4294967295L;
    localSftpFileAttributes.setTimes(new UnsignedInteger32(l), new UnsignedInteger32(l));
    int i = 1;
    try
    {
      if (System.getSecurityManager() != null)
        System.getSecurityManager().checkExec(localFile.getCanonicalPath());
    }
    catch (SecurityException localSecurityException)
    {
      i = 0;
    }
    return localSftpFileAttributes;
  }

  public byte[] openDirectory(String paramString)
    throws PermissionDeniedException, FileNotFoundException, IOException
  {
    String str = paramString;
    paramString = translateVFSPath(paramString);
    File localFile = new File(paramString);
    if (!this.K.canRead(this.F, this.J, paramString))
      throw new PermissionDeniedException("The user does not have permission to read this directory");
    if (localFile.exists())
    {
      if (localFile.isDirectory())
      {
        byte[] arrayOfByte = A(localFile);
        this.D.put(A(arrayOfByte), new _A(str, paramString, localFile));
        return arrayOfByte;
      }
      throw new IOException(translateNFSPath(paramString) + " is not a directory");
    }
    throw new FileNotFoundException(translateNFSPath(paramString) + " does not exist");
  }

  private byte[] A(Object paramObject)
    throws UnsupportedEncodingException, IOException, SshIOException
  {
    try
    {
      return (paramObject.hashCode() + "-" + ServerComponentManager.getInstance().getRND().nextInt()).getBytes("US-ASCII");
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  private String A(byte[] paramArrayOfByte)
    throws UnsupportedEncodingException
  {
    return new String(paramArrayOfByte, "US-ASCII");
  }

  public SftpFile[] readDirectory(byte[] paramArrayOfByte)
    throws InvalidHandleException, EOFException, IOException
  {
    String str1 = A(paramArrayOfByte);
    if (this.D.containsKey(str1))
    {
      Object localObject = this.D.get(str1);
      if ((localObject instanceof _A))
      {
        _A local_A = (_A)localObject;
        int i = local_A.C();
        File[] arrayOfFile = local_A.A();
        if (arrayOfFile == null)
          throw new IOException("Permission denined.");
        int j = arrayOfFile.length - i < 100 ? arrayOfFile.length - i : 100;
        if (j > 0)
        {
          Vector localVector = new Vector();
          for (int k = 0; k < j; k++)
          {
            File localFile = arrayOfFile[(i + k)];
            if ((!arrayOfFile[(i + k)].isFile()) && (!arrayOfFile[(i + k)].isDirectory()))
              continue;
            String str2 = local_A.C + (local_A.C.endsWith("/") ? "" : "/") + localFile.getName();
            SftpFile localSftpFile = new SftpFile(localFile.getName(), getFileAttributes(str2));
            localVector.add(localSftpFile);
          }
          local_A.E = (i + localVector.size());
          SftpFile[] arrayOfSftpFile = new SftpFile[localVector.size()];
          localVector.copyInto(arrayOfSftpFile);
          return arrayOfSftpFile;
        }
        throw new EOFException("There are no more files");
      }
      throw new InvalidHandleException("Handle is not an open directory");
    }
    throw new InvalidHandleException("The handle is invalid");
  }

  public byte[] openFile(String paramString, UnsignedInteger32 paramUnsignedInteger32, SftpFileAttributes paramSftpFileAttributes)
    throws PermissionDeniedException, FileNotFoundException, IOException
  {
    paramString = translateVFSPath(paramString);
    File localFile = new File(paramString);
    if ((!this.K.canRead(this.F, this.J, paramString)) && ((paramUnsignedInteger32.intValue() & 0x1) != 0))
      throw new PermissionDeniedException("The user does not have permission to read.");
    if ((!this.K.canWrite(this.F, this.J, paramString)) && ((paramUnsignedInteger32.intValue() & 0x2) != 0))
      throw new PermissionDeniedException("The user does not have permission to write.");
    if (!localFile.exists())
    {
      if ((paramUnsignedInteger32.longValue() & 0x8) == 8L)
      {
        if (!localFile.createNewFile())
          throw new IOException(translateNFSPath(paramString) + " could not be created");
      }
      else
        throw new FileNotFoundException(translateNFSPath(paramString) + " does not exist");
    }
    else if (((paramUnsignedInteger32.longValue() & 0x8) == 8L) && ((paramUnsignedInteger32.longValue() & 0x20) == 32L))
      throw new IOException(translateNFSPath(paramString) + " already exists");
    String str = "r" + ((paramUnsignedInteger32.longValue() & 0x2) == 2L ? "w" : "");
    RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile, str);
    if (((paramUnsignedInteger32.longValue() & 0x8) == 8L) && ((paramUnsignedInteger32.longValue() & 0x10) == 16L))
      localRandomAccessFile.setLength(0L);
    byte[] arrayOfByte = A(localRandomAccessFile.toString());
    this.D.put(A(arrayOfByte), new _B(localFile, localRandomAccessFile, paramUnsignedInteger32));
    return arrayOfByte;
  }

  public int readFile(byte[] paramArrayOfByte1, UnsignedInteger64 paramUnsignedInteger64, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
    throws InvalidHandleException, EOFException, IOException
  {
    String str = A(paramArrayOfByte1);
    if (this.D.containsKey(str))
    {
      Object localObject = this.D.get(str);
      if ((localObject instanceof _B))
      {
        _B local_B = (_B)localObject;
        if ((local_B.A().longValue() & 1L) == 1L)
        {
          if (local_B.B().getFilePointer() != paramUnsignedInteger64.longValue())
            local_B.B().seek(paramUnsignedInteger64.longValue());
          int i = local_B.B().read(paramArrayOfByte2, paramInt1, paramInt2);
          if (i >= 0)
            return i;
          return -1;
        }
        throw new InvalidHandleException("The file handle was not opened for reading");
      }
      throw new InvalidHandleException("Handle is not an open file");
    }
    throw new InvalidHandleException("The handle is invalid");
  }

  public void writeFile(byte[] paramArrayOfByte1, UnsignedInteger64 paramUnsignedInteger64, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
    throws InvalidHandleException, IOException
  {
    String str = A(paramArrayOfByte1);
    if (this.D.containsKey(str))
    {
      Object localObject = this.D.get(str);
      if ((localObject instanceof _B))
      {
        _B local_B = (_B)localObject;
        if ((local_B.A().longValue() & 0x2) == 2L)
        {
          if ((local_B.A().longValue() & 0x4) == 4L)
            local_B.B().seek(local_B.B().length());
          else if (local_B.B().getFilePointer() != paramUnsignedInteger64.longValue())
            local_B.B().seek(paramUnsignedInteger64.longValue());
          local_B.B().write(paramArrayOfByte2, paramInt1, paramInt2);
        }
        else
        {
          throw new InvalidHandleException("The file was not opened for writing");
        }
      }
      else
      {
        throw new InvalidHandleException("Handle is not an open file");
      }
    }
    else
    {
      throw new InvalidHandleException("The handle is invalid");
    }
  }

  public void closeFile(byte[] paramArrayOfByte)
    throws InvalidHandleException, IOException
  {
    closeFile(paramArrayOfByte, true);
  }

  public boolean closeFile(byte[] paramArrayOfByte, boolean paramBoolean)
    throws InvalidHandleException, IOException
  {
    String str = A(paramArrayOfByte);
    if (this.D.containsKey(str))
    {
      Object localObject = this.D.get(str);
      if ((localObject instanceof _A))
      {
        if (paramBoolean)
          this.D.remove(str);
        else
          return true;
      }
      else if ((localObject instanceof _B))
      {
        try
        {
          if (((_B)localObject).B().getChannel() != null)
            ((_B)localObject).B().getChannel().force(true);
        }
        catch (Exception localException)
        {
        }
        ((_B)localObject).B().close();
        if (paramBoolean)
          this.D.remove(str);
        else
          return true;
      }
      else
      {
        throw new InvalidHandleException("Internal server error");
      }
    }
    else
    {
      throw new InvalidHandleException("The handle is invalid");
    }
    return false;
  }

  public void removeFile(String paramString)
    throws PermissionDeniedException, IOException, FileNotFoundException
  {
    paramString = translateVFSPath(paramString);
    if (!this.K.canWrite(this.F, this.J, paramString))
      throw new PermissionDeniedException("User does not have the permission to delete.");
    File localFile = new File(paramString);
    if (localFile.exists())
      try
      {
        if (localFile.isFile())
        {
          if (!localFile.delete())
            throw new IOException("Failed to delete " + translateNFSPath(paramString));
        }
        else
          throw new IOException(translateNFSPath(paramString) + " is a directory, use remove directory command to remove");
      }
      catch (SecurityException localSecurityException)
      {
        throw new PermissionDeniedException("Permission denied");
      }
    else
      throw new FileNotFoundException(translateNFSPath(paramString) + " does not exist");
  }

  public void renameFile(String paramString1, String paramString2)
    throws PermissionDeniedException, FileNotFoundException, IOException
  {
    paramString1 = translateVFSPath(paramString1);
    paramString2 = translateVFSPath(paramString2);
    if (!this.K.canWrite(this.F, this.J, paramString1))
      throw new PermissionDeniedException("User does not have permission to write.");
    if (!this.K.canWrite(this.F, this.J, paramString2))
      throw new PermissionDeniedException("User does not have permission to write.");
    File localFile1 = new File(paramString1);
    if (localFile1.exists())
    {
      File localFile2 = new File(paramString2);
      if (!localFile2.exists())
      {
        if (!localFile1.renameTo(localFile2))
          throw new IOException("Failed to rename file " + translateNFSPath(paramString1));
      }
      else
        throw new IOException(translateNFSPath(paramString2) + " already exists");
    }
    else
    {
      throw new FileNotFoundException(translateNFSPath(paramString1) + " does not exist");
    }
  }

  public String getDefaultPath()
  {
    return this.A;
  }

  public void removeDirectory(String paramString)
    throws PermissionDeniedException, FileNotFoundException, IOException
  {
    paramString = translateVFSPath(paramString);
    if (!this.K.canWrite(this.F, this.J, paramString))
      throw new PermissionDeniedException("User does not have the permission to write.");
    File localFile = new File(paramString);
    if (localFile.isDirectory())
    {
      if (localFile.exists())
      {
        if (localFile.listFiles().length == 0)
        {
          if (!localFile.delete())
            throw new IOException("Failed to remove directory " + translateNFSPath(paramString));
        }
        else
          throw new IOException(translateNFSPath(paramString) + " is not an empty directory");
      }
      else
        throw new FileNotFoundException(translateNFSPath(paramString) + " does not exist");
    }
    else
      throw new IOException(translateNFSPath(paramString) + " is not a directory");
  }

  public void setFileAttributes(String paramString, SftpFileAttributes paramSftpFileAttributes)
    throws PermissionDeniedException, IOException, FileNotFoundException
  {
  }

  public void setFileAttributes(byte[] paramArrayOfByte, SftpFileAttributes paramSftpFileAttributes)
    throws PermissionDeniedException, IOException, InvalidHandleException
  {
  }

  public SftpFile readSymbolicLink(String paramString)
    throws UnsupportedFileOperationException, FileNotFoundException, IOException, PermissionDeniedException
  {
    throw new UnsupportedFileOperationException("Symbolic links are not supported by the Virtual File System");
  }

  public void createSymbolicLink(String paramString1, String paramString2)
    throws UnsupportedFileOperationException, FileNotFoundException, IOException, PermissionDeniedException
  {
    throw new UnsupportedFileOperationException("Symbolic links are not supported by the Virtual File System");
  }

  public boolean fileExists(String paramString)
    throws IOException
  {
    File localFile = new File(translateVFSPath(paramString));
    return localFile.exists();
  }

  public String getRealPath(String paramString)
    throws IOException, FileNotFoundException
  {
    paramString = translateVFSPath(paramString);
    paramString = translateNFSPath(paramString);
    return paramString;
  }

  public void init(byte[] paramArrayOfByte, SessionChannel paramSessionChannel, SshContext paramSshContext, String paramString)
    throws IOException
  {
    init(paramArrayOfByte, paramSessionChannel, paramSshContext);
  }

  class _A
  {
    File B;
    File[] A;
    int E = 0;
    String D;
    String C;

    public _A(String paramString1, String paramFile, File arg4)
    {
      this.D = paramFile;
      this.C = paramString1;
      Object localObject;
      this.B = localObject;
      this.A = localObject.listFiles();
    }

    public File B()
    {
      return this.B;
    }

    public File[] A()
    {
      return this.A;
    }

    public int C()
    {
      return this.E;
    }
  }

  class _C
    implements VFSPermissionHandler
  {
    _C()
    {
    }

    public boolean canRead(String paramString1, String paramString2, String paramString3)
    {
      return true;
    }

    public boolean canWrite(String paramString1, String paramString2, String paramString3)
    {
      return true;
    }

    public boolean canExecute(String paramString1, String paramString2, String paramString3)
    {
      return true;
    }

    public SftpFileAttributes getPermissions(String paramString1, String paramString2)
      throws FileNotFoundException
    {
      File localFile = new File(paramString2);
      SftpFileAttributes localSftpFileAttributes = new SftpFileAttributes();
      localSftpFileAttributes.setPermissions(new UnsignedInteger32(0x1FF | (localFile.isDirectory() ? 16384 : 32768)));
      return localSftpFileAttributes;
    }

    public String getVFSHomeDirectory(String paramString)
      throws FileNotFoundException
    {
      return VirtualFileSystem.I + paramString;
    }
  }

  class _B
  {
    File B;
    RandomAccessFile C;
    UnsignedInteger32 A;

    public _B(File paramRandomAccessFile, RandomAccessFile paramUnsignedInteger32, UnsignedInteger32 arg4)
    {
      this.B = paramRandomAccessFile;
      this.C = paramUnsignedInteger32;
      Object localObject;
      this.A = localObject;
    }

    public File C()
    {
      return this.B;
    }

    public RandomAccessFile B()
    {
      return this.C;
    }

    public UnsignedInteger32 A()
    {
      return this.A;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.vfs.VirtualFileSystem
 * JD-Core Version:    0.6.0
 */