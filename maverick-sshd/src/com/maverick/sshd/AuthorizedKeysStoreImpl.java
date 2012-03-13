package com.maverick.sshd;

import com.maverick.ssh.components.SshPublicKey;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.sshd.platform.NativeFileSystemProvider;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import com.sshtools.publickey.SshPublicKeyFile;
import com.sshtools.publickey.SshPublicKeyFileFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.SocketAddress;

public class AuthorizedKeysStoreImpl
  implements PublicKeyStore
{
  protected String authorizedKeysFile = ".ssh/authorized_keys";

  public AuthorizedKeysStoreImpl()
  {
  }

  public AuthorizedKeysStoreImpl(String paramString)
  {
    this.authorizedKeysFile = paramString;
  }

  public boolean isAuthorizedKey(SshPublicKey paramSshPublicKey, byte[] paramArrayOfByte, SocketAddress paramSocketAddress, AuthenticationProvider paramAuthenticationProvider)
  {
    try
    {
      NativeFileSystemProvider localNativeFileSystemProvider = (NativeFileSystemProvider)paramAuthenticationProvider.getContext().getFileSystemProvider_KeyStore().newInstance();
      localNativeFileSystemProvider.init(paramArrayOfByte, null, paramAuthenticationProvider.getContext(), "authorized_keys");
      byte[] arrayOfByte1 = localNativeFileSystemProvider.openFile(this.authorizedKeysFile, new UnsignedInteger32(1L), localNativeFileSystemProvider.getFileAttributes(this.authorizedKeysFile));
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      byte[] arrayOfByte2 = new byte[4096];
      UnsignedInteger64 localUnsignedInteger64 = new UnsignedInteger64(0L);
      try
      {
        int i;
        while ((i = localNativeFileSystemProvider.readFile(arrayOfByte1, localUnsignedInteger64, arrayOfByte2, 0, arrayOfByte2.length)) > 0)
        {
          localByteArrayOutputStream.write(arrayOfByte2, 0, i);
          localUnsignedInteger64 = UnsignedInteger64.add(localUnsignedInteger64, i);
        }
      }
      catch (EOFException localEOFException)
      {
      }
      localNativeFileSystemProvider.closeFile(arrayOfByte1);
      localNativeFileSystemProvider.closeFilesystem();
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(localByteArrayOutputStream.toByteArray())));
      String str;
      while ((str = localBufferedReader.readLine()) != null)
      {
        SshPublicKeyFile localSshPublicKeyFile = SshPublicKeyFileFactory.parse(str.getBytes("US-ASCII"));
        if (localSshPublicKeyFile.toPublicKey().equals(paramSshPublicKey))
          return true;
      }
      return false;
    }
    catch (Throwable localThrowable)
    {
    }
    return false;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.AuthorizedKeysStoreImpl
 * JD-Core Version:    0.6.0
 */