package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;

public class SshX509DsaPublicKey extends Ssh2DsaPublicKey
{
  public static final String X509V3_SIGN_DSA = "x509v3-sign-dss";
  X509Certificate b;

  public SshX509DsaPublicKey()
  {
  }

  public SshX509DsaPublicKey(X509Certificate paramX509Certificate)
  {
    super((DSAPublicKey)paramX509Certificate.getPublicKey());
    this.b = paramX509Certificate;
  }

  public String getAlgorithm()
  {
    return "x509v3-sign-dss";
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      return this.b.getEncoded();
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
      this.b = ((X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream));
      if (!(this.b.getPublicKey() instanceof RSAPublicKey))
        throw new SshException("Certificate public key is not an RSA public key!", 4);
      this.pubkey = ((DSAPublicKey)this.b.getPublicKey());
    }
    catch (Throwable localThrowable)
    {
      throw new SshException(localThrowable.getMessage(), 16, localThrowable);
    }
  }

  public X509Certificate getCertificate()
  {
    return this.b;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.SshX509DsaPublicKey
 * JD-Core Version:    0.6.0
 */