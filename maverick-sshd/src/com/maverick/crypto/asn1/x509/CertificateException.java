package com.maverick.crypto.asn1.x509;

public class CertificateException extends Exception
{
  public static final int CERTIFICATE_EXPIRED = 1;
  public static final int CERTIFICATE_NOT_YET_VALID = 2;
  public static final int CERTIFICATE_ENCODING_ERROR = 3;
  public static final int CERTIFICATE_GENERAL_ERROR = 4;
  public static final int CERTIFICATE_UNSUPPORTED_ALGORITHM = 5;
  int A;

  public CertificateException(int paramInt, String paramString)
  {
    super(paramString);
    this.A = paramInt;
  }

  public int getStatus()
  {
    return this.A;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.CertificateException
 * JD-Core Version:    0.6.0
 */