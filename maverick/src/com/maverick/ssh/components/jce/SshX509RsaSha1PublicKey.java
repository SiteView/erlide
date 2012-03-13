package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

public class SshX509RsaSha1PublicKey extends Ssh2RsaPublicKey
{
  public static final String X509V3_SIGN_RSA_SHA1 = "x509v3-sign-rsa-sha1";
  X509Certificate d;

  public SshX509RsaSha1PublicKey()
  {
  }

  public SshX509RsaSha1PublicKey(X509Certificate paramX509Certificate)
  {
    super((RSAPublicKey)paramX509Certificate.getPublicKey());
    this.d = paramX509Certificate;
  }

  public String getAlgorithm()
  {
    return "x509v3-sign-rsa-sha1";
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(getAlgorithm());
      localByteArrayWriter.writeBinaryString(this.d.getEncoded());
      return localByteArrayWriter.toByteArray();
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
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte, paramInt1, paramInt2);
      String str = localByteArrayReader.readString();
      if (!str.equals("x509v3-sign-rsa-sha1"))
        throw new SshException("The encoded key is not X509 RSA", 5);
      byte[] arrayOfByte = localByteArrayReader.readBinaryString();
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      CertificateFactory localCertificateFactory = JCEProvider.getProviderForAlgorithm("X.509") == null ? CertificateFactory.getInstance("X.509") : CertificateFactory.getInstance("X.509", JCEProvider.getProviderForAlgorithm("X.509"));
      this.d = ((X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream));
      if (!(this.d.getPublicKey() instanceof RSAPublicKey))
        throw new SshException("Certificate public key is not an RSA public key!", 4);
      this.c = ((RSAPublicKey)this.d.getPublicKey());
    }
    catch (Throwable localThrowable)
    {
      throw new SshException(localThrowable.getMessage(), 16, localThrowable);
    }
  }

  public X509Certificate getCertificate()
  {
    return this.d;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.SshX509RsaSha1PublicKey
 * JD-Core Version:    0.6.0
 */