package com.maverick.ssh1;

import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.ssh.compression.SshCompression;
import com.maverick.ssh.message.SshMessage;
import com.maverick.ssh.message.SshMessageReader;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.math.BigInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class f
  implements SshMessageReader
{
  DataInputStream h;
  DataOutputStream p;
  SshTransport r;
  SshCipher t;
  SshCipher k;
  SshCompression o;
  SshCompression n;
  SshRsaPublicKey v;
  SshRsaPublicKey i;
  int l;
  int s = 2;
  int f;
  int m;
  byte[] c;
  byte[] q;
  byte[] u;
  long j = 0L;
  Ssh1Context b;
  int d = 1;
  boolean e = false;
  static Log g = LogFactory.getLog(f.class);

  f(SshTransport paramSshTransport, SshContext paramSshContext)
    throws SshException
  {
    try
    {
      if (!(paramSshContext instanceof Ssh1Context))
        throw new SshException("Invalid SshContext!", 4);
      this.h = new DataInputStream(paramSshTransport.getInputStream());
      this.p = new DataOutputStream(paramSshTransport.getOutputStream());
      this.r = paramSshTransport;
      this.b = ((Ssh1Context)paramSshContext);
    }
    catch (IOException localIOException)
    {
      c();
      throw new SshException(localIOException, 10);
    }
  }

  public boolean isConnected()
  {
    return this.d == 2;
  }

  public byte[] nextMessage()
    throws SshException
  {
    byte[] arrayOfByte;
    do
      arrayOfByte = h();
    while ((b(arrayOfByte)) && (this.d == 2));
    if (this.d == 3)
      throw new SshException("The remote host disconnected", 2);
    return arrayOfByte;
  }

  void c()
  {
    try
    {
      this.r.close();
    }
    catch (IOException localIOException)
    {
      if (g.isDebugEnabled())
        g.debug("RECIEVED IOException IN Ssh1Protocol.close:" + localIOException.getMessage());
    }
    this.d = 3;
  }

  void c(String paramString)
  {
    try
    {
      this.e = true;
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(paramString.length() + 5);
      localByteArrayWriter.write(1);
      localByteArrayWriter.writeString(paramString);
      if (g.isDebugEnabled())
        g.debug("Sending SSH_MSG_DISCONNECT");
      d(localByteArrayWriter.toByteArray());
    }
    catch (Throwable localThrowable)
    {
      if (g.isDebugEnabled())
        g.debug("RECIEVED THROWABLE EXCEPTION IN Ssh1Protocol.disconnect:" + localThrowable.getMessage());
    }
    finally
    {
      c();
    }
  }

  int e()
  {
    return this.d;
  }

  boolean b(byte[] paramArrayOfByte)
  {
    switch (paramArrayOfByte[0])
    {
    case 1:
      if (g.isDebugEnabled())
        g.debug("Received SSH_MSG_DISCONNECT");
      c();
      return true;
    case 32:
      if (g.isDebugEnabled())
        g.debug("Received SSH_MSG_IGNORE");
      return true;
    case 36:
      if (g.isDebugEnabled())
        g.debug("Received SSH_MSG_DEBUG");
      return true;
    }
    return false;
  }

  boolean b(String paramString)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(4);
      localByteArrayWriter.writeString(paramString);
      if (g.isDebugEnabled())
        g.debug("Sending SSH_CMSG_USER");
      d(localByteArrayWriter.toByteArray());
      boolean bool = f();
      this.d = 2;
      return bool;
    }
    catch (IOException localIOException)
    {
      c();
    }
    throw new SshException(localIOException, 1);
  }

  void g()
    throws SshException
  {
    try
    {
      int i1 = this.b.getCipherType(this.f);
      if (g.isDebugEnabled())
        g.debug("Selected cipher 0x" + Integer.toHexString(i1));
      SshCipher localSshCipher1 = this.b.createCipher(i1);
      SshCipher localSshCipher2 = this.b.createCipher(i1);
      d();
      this.c = new byte[32];
      ComponentManager.getInstance().getRND().nextBytes(this.c);
      b(i1);
      byte[] arrayOfByte = new byte[localSshCipher1.getBlockSize()];
      for (int i2 = 0; i2 < arrayOfByte.length; i2++)
        arrayOfByte[i2] = 0;
      localSshCipher1.init(1, arrayOfByte, this.c);
      arrayOfByte = new byte[localSshCipher2.getBlockSize()];
      for (i2 = 0; i2 < arrayOfByte.length; i2++)
        arrayOfByte[i2] = 0;
      localSshCipher2.init(0, arrayOfByte, this.c);
      this.t = localSshCipher1;
      this.k = localSshCipher2;
      if (!f())
      {
        try
        {
          this.r.close();
        }
        catch (IOException localIOException2)
        {
          if (g.isDebugEnabled())
            g.debug("RECIEVED IOException IN Ssh1Protocol.setupSession:" + localIOException2.getMessage());
        }
        throw new SshException("The session failed to initialize!", 9);
      }
    }
    catch (IOException localIOException1)
    {
      c();
      throw new SshException(localIOException1, 9);
    }
  }

  boolean f()
    throws SshException
  {
    try
    {
      SshMessage localSshMessage;
      while (true)
      {
        localSshMessage = new SshMessage(nextMessage());
        switch (localSshMessage.getMessageId())
        {
        case 14:
          if (g.isDebugEnabled())
            g.debug("Received SSH_SMSG_SUCCESS");
          return true;
        case 15:
          if (g.isDebugEnabled())
            g.debug("Received SSH_SMSG_FAILURE");
          return false;
        case 1:
          if (g.isDebugEnabled())
            g.debug("Received SSH_MSG_DISCONNECT");
          throw new SshException("The server disconnected! " + localSshMessage.readString(), 2);
        case 36:
          if (!g.isDebugEnabled())
            continue;
          g.debug("Received SSH_MSG_DEBUG");
          break;
        case 32:
          if (!g.isDebugEnabled())
            continue;
          g.debug("Received SSH_MSG_IGNORE");
        }
      }
      throw new SshException("Invalid message type " + String.valueOf(localSshMessage.getMessageId()) + " received", 3);
    }
    catch (IOException localIOException)
    {
      c();
    }
    throw new SshException(localIOException, 1);
  }

  private String c(byte[] paramArrayOfByte)
  {
    String str = "";
    for (int i1 = 0; i1 < paramArrayOfByte.length; i1++)
      str = str + " " + Integer.toHexString(paramArrayOfByte[i1] & 0xFF);
    return str.trim();
  }

  void b(int paramInt)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      byte[] arrayOfByte = new byte[this.c.length + 1];
      arrayOfByte[0] = 0;
      System.arraycopy(this.c, 0, arrayOfByte, 1, this.c.length);
      for (int i1 = 0; i1 < this.q.length; i1++)
      {
        int tmp55_54 = (i1 + 1);
        byte[] tmp55_50 = arrayOfByte;
        tmp55_50[tmp55_54] = (byte)(tmp55_50[tmp55_54] ^ this.q[i1]);
      }
      BigInteger localBigInteger = new BigInteger(arrayOfByte);
      if ((this.i.getModulus().bitLength() <= this.v.getModulus().bitLength() - 16) || (this.v.getModulus().bitLength() <= this.i.getModulus().bitLength() - 16))
      {
        int i2 = (this.i.getModulus().bitLength() + 7) / 8;
        int i3 = (this.v.getModulus().bitLength() + 7) / 8;
        if (g.isDebugEnabled())
        {
          g.debug("Encoding session key with server host key slen=" + i2 + " hlen=" + i3);
          g.debug("Host key is    : " + c(this.v.getEncoded()));
          g.debug("Server key is  : " + c(this.i.getEncoded()));
          g.debug("Cookie is      : " + c(this.u));
          g.debug("Session key is : " + c(this.c));
          g.debug("XORd key is    : " + c(arrayOfByte));
        }
        if (i3 < i2)
        {
          localBigInteger = this.v.doPublic(localBigInteger);
          localBigInteger = this.i.doPublic(localBigInteger);
        }
        else
        {
          localBigInteger = this.i.doPublic(localBigInteger);
          localBigInteger = this.v.doPublic(localBigInteger);
        }
        if (g.isDebugEnabled())
          g.debug("Encoded key is : " + c(localBigInteger.toByteArray()));
        localByteArrayWriter.write(3);
        localByteArrayWriter.write(paramInt);
        localByteArrayWriter.write(this.u);
        localByteArrayWriter.writeMPINT(localBigInteger);
        localByteArrayWriter.writeInt(this.s);
        if (g.isDebugEnabled())
          g.debug("Sending SSH_CMSG_SESSION_KEY");
        d(localByteArrayWriter.toByteArray());
      }
      else
      {
        throw new SshException("SSH 1.5 protocol violation: Server key and host key lengths do not match protocol requirements. serverbits=" + String.valueOf(this.i.getModulus().bitLength()) + " hostbits=" + String.valueOf(this.v.getModulus().bitLength()), 3);
      }
    }
    catch (IOException localIOException)
    {
      c();
      throw new SshException("", 1);
    }
  }

  void d()
    throws SshException
  {
    byte[] arrayOfByte2 = this.v.getModulus().toByteArray();
    byte[] arrayOfByte3 = this.i.getModulus().toByteArray();
    int i1 = arrayOfByte2.length + arrayOfByte3.length + this.u.length;
    if (arrayOfByte2[0] == 0)
      i1--;
    if (arrayOfByte3[0] == 0)
      i1--;
    byte[] arrayOfByte1 = new byte[i1];
    if (arrayOfByte2[0] == 0)
    {
      System.arraycopy(arrayOfByte2, arrayOfByte2[0] == 0 ? 1 : 0, arrayOfByte1, 0, arrayOfByte2.length - 1);
      i1 = arrayOfByte2.length - 1;
    }
    else
    {
      System.arraycopy(arrayOfByte2, arrayOfByte2[0] == 0 ? 1 : 0, arrayOfByte1, 0, arrayOfByte2.length);
      i1 = arrayOfByte2.length;
    }
    if (arrayOfByte3[0] == 0)
    {
      System.arraycopy(arrayOfByte3, arrayOfByte3[0] == 0 ? 1 : 0, arrayOfByte1, i1, arrayOfByte3.length - 1);
      i1 += arrayOfByte3.length - 1;
    }
    else
    {
      System.arraycopy(arrayOfByte3, arrayOfByte3[0] == 0 ? 1 : 0, arrayOfByte1, i1, arrayOfByte3.length);
      i1 += arrayOfByte3.length;
    }
    System.arraycopy(this.u, 0, arrayOfByte1, i1, this.u.length);
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("MD5");
    localDigest.putBytes(arrayOfByte1);
    this.q = localDigest.doFinal();
  }

  void b()
    throws SshException
  {
    try
    {
      SshMessage localSshMessage = new SshMessage(nextMessage());
      if (localSshMessage.getMessageId() != 2)
        throw new SshException("SSH_SMSG_PUBLIC_KEY message expected but received type " + String.valueOf(localSshMessage.getMessageId()) + " instead!", 3);
      if (g.isDebugEnabled())
        g.debug("Received SSH_SMSG_PUBLIC_KEY");
      this.u = new byte[8];
      localSshMessage.read(this.u);
      int i1 = (int)localSshMessage.readInt();
      BigInteger localBigInteger1 = localSshMessage.readMPINT();
      BigInteger localBigInteger2 = localSshMessage.readMPINT();
      this.i = ComponentManager.getInstance().createRsaPublicKey(localBigInteger2, localBigInteger1, 1);
      i1 = (int)localSshMessage.readInt();
      localBigInteger1 = localSshMessage.readMPINT();
      localBigInteger2 = localSshMessage.readMPINT();
      this.v = ComponentManager.getInstance().createRsaPublicKey(localBigInteger2, localBigInteger1, 1);
      this.l = (int)localSshMessage.readInt();
      this.f = (int)localSshMessage.readInt();
      this.m = (int)localSshMessage.readInt();
      if (g.isDebugEnabled())
      {
        g.debug("Server Protocol Flags   : 0x" + Integer.toHexString(this.l));
        g.debug("Supported Ciphers       : 0x" + Integer.toHexString(this.f));
        g.debug("Supported Authentication: 0x" + Integer.toHexString(this.m));
      }
      if ((this.b.getHostKeyVerification() != null) && (!this.b.getHostKeyVerification().verifyHost(this.r.getHost(), this.v)))
        throw new SshException("The host key was not accepted.", 9);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  byte[] h()
    throws SshException
  {
    try
    {
      synchronized (this.h)
      {
        int i1 = this.h.readInt();
        int i2 = i1 + 8 & 0xFFFFFFF8;
        byte[] arrayOfByte1 = new byte[i2];
        this.h.readFully(arrayOfByte1);
        if (this.t != null)
          this.t.transform(arrayOfByte1);
        int i3 = (int)ByteArrayReader.readInt(arrayOfByte1, arrayOfByte1.length - 4);
        int i4 = (int)c.b(arrayOfByte1, 0, i2 - 4);
        if (i3 != i4)
          throw new SshException("Invalid checksum detected! Received:" + i3 + " Expected:" + i4, 3);
        if (this.o != null)
          throw new SshException("Compression not yet supported", 4);
        byte[] arrayOfByte2 = new byte[i1 - 4];
        System.arraycopy(arrayOfByte1, 8 - i1 % 8, arrayOfByte2, 0, arrayOfByte2.length);
        return arrayOfByte2;
      }
    }
    catch (InterruptedIOException localInterruptedIOException)
    {
      throw new SshException("Interrupted IO; possible socket timeout detected?", 19);
    }
    catch (IOException localIOException)
    {
      c();
    }
    throw new SshException(localIOException, 1);
  }

  void d(byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      synchronized (this.p)
      {
        if (this.n != null)
          throw new SshException("Compression not supported yet!", 4);
        int i1 = 8 - (paramArrayOfByte.length + 4) % 8;
        byte[] arrayOfByte1 = new byte[i1];
        ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(paramArrayOfByte.length + 4 + i1);
        if (this.k != null)
          ComponentManager.getInstance().getRND().nextBytes(arrayOfByte1);
        localByteArrayWriter.write(arrayOfByte1);
        localByteArrayWriter.write(paramArrayOfByte);
        byte[] arrayOfByte2 = localByteArrayWriter.toByteArray();
        localByteArrayWriter.writeInt((int)c.b(arrayOfByte2, 0, arrayOfByte2.length));
        paramArrayOfByte = localByteArrayWriter.toByteArray();
        if (this.k != null)
          this.k.transform(paramArrayOfByte);
        localByteArrayWriter.reset();
        localByteArrayWriter.writeInt(paramArrayOfByte.length - i1);
        localByteArrayWriter.write(paramArrayOfByte);
        this.p.write(localByteArrayWriter.toByteArray());
      }
    }
    catch (IOException localIOException)
    {
      c();
      throw new SshException(localIOException, 1);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.f
 * JD-Core Version:    0.6.0
 */