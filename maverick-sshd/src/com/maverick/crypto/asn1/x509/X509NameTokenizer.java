package com.maverick.crypto.asn1.x509;

public class X509NameTokenizer
{
  private String C;
  private int B;
  private StringBuffer A = new StringBuffer();

  public X509NameTokenizer(String paramString)
  {
    this.C = paramString;
    this.B = -1;
  }

  public boolean hasMoreTokens()
  {
    return this.B != this.C.length();
  }

  public String nextToken()
  {
    if (this.B == this.C.length())
      return null;
    int i = this.B + 1;
    int j = 0;
    int k = 0;
    this.A.setLength(0);
    while (i != this.C.length())
    {
      char c = this.C.charAt(i);
      if (c == '"')
      {
        if (k == 0)
          j = j == 0 ? 1 : 0;
        else
          this.A.append(c);
        k = 0;
      }
      else if ((k != 0) || (j != 0))
      {
        this.A.append(c);
        k = 0;
      }
      else if (c == '\\')
      {
        k = 1;
      }
      else
      {
        if (c == ',')
          break;
        this.A.append(c);
      }
      i++;
    }
    this.B = i;
    return this.A.toString().trim();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509NameTokenizer
 * JD-Core Version:    0.6.0
 */