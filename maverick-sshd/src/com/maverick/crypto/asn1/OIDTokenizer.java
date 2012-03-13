package com.maverick.crypto.asn1;

public class OIDTokenizer
{
  private String B;
  private int A;

  public OIDTokenizer(String paramString)
  {
    this.B = paramString;
    this.A = 0;
  }

  public boolean hasMoreTokens()
  {
    return this.A != -1;
  }

  public String nextToken()
  {
    if (this.A == -1)
      return null;
    int i = this.B.indexOf('.', this.A);
    if (i == -1)
    {
      str = this.B.substring(this.A);
      this.A = -1;
      return str;
    }
    String str = this.B.substring(this.A, i);
    this.A = (i + 1);
    return str;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.OIDTokenizer
 * JD-Core Version:    0.6.0
 */