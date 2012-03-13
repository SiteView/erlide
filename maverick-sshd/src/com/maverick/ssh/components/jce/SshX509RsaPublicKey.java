package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

public class SshX509RsaPublicKey extends Ssh2RsaPublicKey
{
  public static final String X509V3_SIGN_RSA = "x509v3-sign-rsa";
  X509Certificate D;

  public SshX509RsaPublicKey()
  {
  }

  public SshX509RsaPublicKey(X509Certificate paramX509Certificate)
  {
    super((RSAPublicKey)paramX509Certificate.getPublicKey());
    this.D = paramX509Certificate;
  }

  public String getAlgorithm()
  {
    return "x509v3-sign-rsa";
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      return this.D.getEncoded();
    }
    catch (Throwable localThrowable)
    {
    }
    throw new SshException("Failed to encoded key data", 5, localThrowable);
  }

  public void init(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException
  {
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2);
      CertificateFactory localCertificateFactory = JCEProvider.getProviderForAlgorithm("X.509") == null ? CertificateFactory.getInstance("X.509") : CertificateFactory.getInstance("X.509", JCEProvider.getProviderForAlgorithm("X.509"));
      this.D = ((X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream));
      if (!(this.D.getPublicKey() instanceof RSAPublicKey))
        throw new SshException("Certificate public key is not an RSA public key!", 4);
      this.B = ((RSAPublicKey)this.D.getPublicKey());
    }
    catch (Throwable localThrowable)
    {
      throw new SshException(localThrowable.getMessage(), 16, localThrowable);
    }
  }

  public X509Certificate getCertificate()
  {
    return this.D;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.SshX509RsaPublicKey
 * JD-Core Version:    0.6.0
 */