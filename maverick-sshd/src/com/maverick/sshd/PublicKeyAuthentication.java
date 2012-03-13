package com.maverick.sshd;

import com.maverick.nio.EventLog;
import com.maverick.nio.SocketConnection;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import com.sshtools.publickey.SshPublicKeyFileFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class PublicKeyAuthentication
  implements AuthenticationMechanism
{
  public static final int SSH_MSG_USERAUTH_PK_OK = 60;
  TransportProtocol H;
  AuthenticationProtocol E;
  byte[] F;
  AuthenticationProvider G;
  PublicKeyStore D;

  public void init(TransportProtocol paramTransportProtocol, AuthenticationProtocol paramAuthenticationProtocol, byte[] paramArrayOfByte)
    throws IOException
  {
    this.H = paramTransportProtocol;
    this.E = paramAuthenticationProtocol;
    this.F = paramArrayOfByte;
    this.G = paramTransportProtocol.getSshContext().getAuthenticationProvider();
    this.D = paramTransportProtocol.getSshContext().getPublicKeyStore();
  }

  public String getMethod()
  {
    return "publickey";
  }

  public boolean startRequest(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    int i = localByteArrayReader.read() == 0 ? 0 : 1;
    String str = localByteArrayReader.readString();
    if (!this.H.getContext().supportedPublicKeys().contains(str))
    {
      this.E.failedAuthentication();
      return true;
    }
    byte[] arrayOfByte1 = localByteArrayReader.readBinaryString();
    byte[] arrayOfByte2 = null;
    Object localObject;
    if (i != 0)
    {
      arrayOfByte2 = localByteArrayReader.readBinaryString();
      localObject = A(str, arrayOfByte1, paramString, this.H.getRemoteAddress());
      if (localObject != null)
      {
        ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
        localByteArrayWriter.writeBinaryString(this.F);
        localByteArrayWriter.write(50);
        localByteArrayWriter.writeString(paramString);
        localByteArrayWriter.writeString("ssh-connection");
        localByteArrayWriter.writeString("publickey");
        localByteArrayWriter.write(1);
        localByteArrayWriter.writeString(str);
        localByteArrayWriter.writeBinaryString(arrayOfByte1);
        byte[] arrayOfByte3 = localByteArrayWriter.toByteArray();
        try
        {
          if ((((SshPublicKey)localObject).verifySignature(arrayOfByte2, arrayOfByte3)) && (this.G.logon(this.F, paramString, this.H.getSocketConnection().getRemoteAddress(), (SshPublicKey)localObject, false)))
            this.E.completedAuthentication();
          else
            this.E.failedAuthentication();
        }
        catch (SshException localSshException)
        {
          throw new IOException();
        }
      }
      else
      {
        this.E.failedAuthentication();
      }
    }
    else
    {
      localObject = (Integer)this.E.getParameter("publickey.max.verify");
      if (localObject == null)
        localObject = new Integer(1);
      else
        localObject = new Integer(((Integer)localObject).intValue() + 1);
      this.E.setParameter("publickey.max.verify", localObject);
      if (((Integer)localObject).intValue() > this.H.getSshContext().getMaximumPublicKeyVerificationAttempts())
      {
        this.H.disconnect(14, "Too many publickey verification attempts were made.");
        return true;
      }
      if (A(str, arrayOfByte1, paramString, this.H.getRemoteAddress()) != null)
      {
        this.H.postMessage(new SshMessage(str, arrayOfByte1)
        {
          public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
          {
            paramByteBuffer.put(60);
            paramByteBuffer.putInt(this.val$algorithm.length());
            paramByteBuffer.put(this.val$algorithm.getBytes());
            paramByteBuffer.putInt(this.val$keyblob.length);
            paramByteBuffer.put(this.val$keyblob);
            return true;
          }

          public void messageSent()
          {
          }
        });
        this.E.discardAuthentication();
      }
      else
      {
        this.E.failedAuthentication(false, true);
      }
    }
    return true;
  }

  private SshPublicKey A(String paramString1, byte[] paramArrayOfByte, String paramString2, SocketAddress paramSocketAddress)
  {
    try
    {
      SshPublicKey localSshPublicKey = SshPublicKeyFileFactory.decodeSSH2PublicKey(paramString1, paramArrayOfByte);
      if (this.D.isAuthorizedKey(localSshPublicKey, this.F, paramSocketAddress, this.G))
        return localSshPublicKey;
      return null;
    }
    catch (IOException localIOException)
    {
      EventLog.LogEvent(this, "Client provided unreadable key for authentication");
    }
    return null;
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    return false;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.PublicKeyAuthentication
 * JD-Core Version:    0.6.0
 */