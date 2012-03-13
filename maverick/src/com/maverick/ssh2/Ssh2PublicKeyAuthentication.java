package com.maverick.ssh2;

import com.maverick.ssh.PublicKeyAuthentication;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshPrivateKey;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;

public class Ssh2PublicKeyAuthentication extends PublicKeyAuthentication
  implements AuthenticationClient
{
  SignatureGenerator w;

  public void authenticate(AuthenticationProtocol paramAuthenticationProtocol, String paramString)
    throws SshException, AuthenticationResult
  {
    try
    {
      if (getPublicKey() == null)
        throw new SshException("Public key not set!", 4);
      if ((getPrivateKey() == null) && (this.w == null) && (isAuthenticating()))
        throw new SshException("Private key or signature generator not set!", 4);
      if (getUsername() == null)
        throw new SshException("Username not set!", 4);
      ByteArrayWriter localByteArrayWriter1 = new ByteArrayWriter();
      localByteArrayWriter1.writeBinaryString(paramAuthenticationProtocol.getSessionIdentifier());
      localByteArrayWriter1.write(50);
      localByteArrayWriter1.writeString(getUsername());
      localByteArrayWriter1.writeString(paramString);
      localByteArrayWriter1.writeString("publickey");
      localByteArrayWriter1.writeBoolean(isAuthenticating());
      byte[] arrayOfByte;
      try
      {
        if (((getPublicKey() instanceof SshRsaPublicKey)) && (((SshRsaPublicKey)getPublicKey()).getVersion() == 1))
        {
          SshRsaPublicKey localSshRsaPublicKey = (SshRsaPublicKey)getPublicKey();
          localByteArrayWriter1.writeString("ssh-rsa");
          localObject = new ByteArrayWriter();
          ((ByteArrayWriter)localObject).writeString("ssh-rsa");
          ((ByteArrayWriter)localObject).writeBigInteger(localSshRsaPublicKey.getPublicExponent());
          ((ByteArrayWriter)localObject).writeBigInteger(localSshRsaPublicKey.getModulus());
          localByteArrayWriter1.writeBinaryString(arrayOfByte = ((ByteArrayWriter)localObject).toByteArray());
        }
        else
        {
          localByteArrayWriter1.writeString(getPublicKey().getAlgorithm());
          localByteArrayWriter1.writeBinaryString(arrayOfByte = getPublicKey().getEncoded());
        }
      }
      catch (Throwable localThrowable)
      {
        throw new SshException("Unsupported public key type " + getPublicKey().getAlgorithm(), 4);
      }
      ByteArrayWriter localByteArrayWriter2 = new ByteArrayWriter();
      localByteArrayWriter2.writeBoolean(isAuthenticating());
      localByteArrayWriter2.writeString(getPublicKey().getAlgorithm());
      localByteArrayWriter2.writeBinaryString(arrayOfByte);
      if (isAuthenticating())
      {
        if (this.w != null)
          localObject = this.w.sign(getPublicKey(), localByteArrayWriter1.toByteArray());
        else
          localObject = getPrivateKey().sign(localByteArrayWriter1.toByteArray());
        ByteArrayWriter localByteArrayWriter3 = new ByteArrayWriter();
        localByteArrayWriter3.writeString(getPublicKey().getAlgorithm());
        localByteArrayWriter3.writeBinaryString(localObject);
        localByteArrayWriter2.writeBinaryString(localByteArrayWriter3.toByteArray());
      }
      paramAuthenticationProtocol.sendRequest(getUsername(), paramString, "publickey", localByteArrayWriter2.toByteArray());
      Object localObject = paramAuthenticationProtocol.readMessage();
      if (localObject[0] == 60)
        throw new AuthenticationResult(5);
      paramAuthenticationProtocol.d.disconnect(2, "Unexpected message " + localObject[0] + " received");
      throw new SshException("Unexpected message " + localObject[0] + " received", 3);
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public void setSignatureGenerator(SignatureGenerator paramSignatureGenerator)
  {
    this.w = paramSignatureGenerator;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.Ssh2PublicKeyAuthentication
 * JD-Core Version:    0.6.0
 */