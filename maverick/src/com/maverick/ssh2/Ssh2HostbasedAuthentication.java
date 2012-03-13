package com.maverick.ssh2;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshPrivateKey;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;

public class Ssh2HostbasedAuthentication
  implements AuthenticationClient
{
  String p;
  String o;
  String n;
  SshPrivateKey l;
  SshPublicKey m;

  public void authenticate(AuthenticationProtocol paramAuthenticationProtocol, String paramString)
    throws SshException, AuthenticationResult
  {
    if (this.o == null)
      throw new SshException("Username not set!", 4);
    if (this.p == null)
      throw new SshException("Client hostname not set!", 4);
    if (this.n == null)
      this.n = this.o;
    if ((this.l == null) || (this.m == null))
      throw new SshException("Client host keys not set!", 4);
    if ((!(this.m instanceof SshRsaPublicKey)) && (!(this.m instanceof SshDsaPublicKey)))
      throw new SshException("Invalid public key type for SSH2 authentication!", 4);
    try
    {
      ByteArrayWriter localByteArrayWriter1 = new ByteArrayWriter();
      localByteArrayWriter1.writeString(this.m.getAlgorithm());
      localByteArrayWriter1.writeBinaryString(this.m.getEncoded());
      localByteArrayWriter1.writeString(this.p);
      localByteArrayWriter1.writeString(this.n);
      ByteArrayWriter localByteArrayWriter2 = new ByteArrayWriter();
      localByteArrayWriter2.writeBinaryString(paramAuthenticationProtocol.getSessionIdentifier());
      localByteArrayWriter2.write(50);
      localByteArrayWriter2.writeString(this.o);
      localByteArrayWriter2.writeString(paramString);
      localByteArrayWriter2.writeString("hostbased");
      localByteArrayWriter2.writeString(this.m.getAlgorithm());
      localByteArrayWriter2.writeBinaryString(this.m.getEncoded());
      localByteArrayWriter2.writeString(this.p);
      localByteArrayWriter2.writeString(this.n);
      ByteArrayWriter localByteArrayWriter3 = new ByteArrayWriter();
      localByteArrayWriter3.writeString(this.m.getAlgorithm());
      localByteArrayWriter3.writeBinaryString(this.l.sign(localByteArrayWriter2.toByteArray()));
      localByteArrayWriter1.writeBinaryString(localByteArrayWriter3.toByteArray());
      paramAuthenticationProtocol.sendRequest(getUsername(), paramString, "hostbased", localByteArrayWriter1.toByteArray());
      byte[] arrayOfByte = paramAuthenticationProtocol.readMessage();
      throw new SshException("Unexpected message returned from authentication protocol: " + arrayOfByte[0], 3);
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public String getMethod()
  {
    return "hostbased";
  }

  public void setClientHostname(String paramString)
  {
    this.p = paramString;
  }

  public void setUsername(String paramString)
  {
    this.o = paramString;
  }

  public String getUsername()
  {
    return this.o;
  }

  public void setPublicKey(SshPublicKey paramSshPublicKey)
  {
    this.m = paramSshPublicKey;
  }

  public void setPrivateKey(SshPrivateKey paramSshPrivateKey)
  {
    this.l = paramSshPrivateKey;
  }

  public void setClientUsername(String paramString)
  {
    this.n = paramString;
  }

  public String getClientUsername()
  {
    return this.n;
  }

  public SshPrivateKey getPrivateKey()
  {
    return this.l;
  }

  public SshPublicKey getPublicKey()
  {
    return this.m;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.Ssh2HostbasedAuthentication
 * JD-Core Version:    0.6.0
 */