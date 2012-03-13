package com.maverick.crypto.asn1.x509;

public class X509NameTokenizer
{
  private String d;
  private int c;
  private StringBuffer b = new StringBuffer();

  public X509NameTokenizer(String paramString)
  {
    this.d = paramString;
    this.c = -1;
  }

  public boolean hasMoreTokens()
  {
    return this.c != this.d.length();
  }

  public String nextToken()
  {
    if (this.c == this.d.length())
      return null;
    int i = this.c + 1;
    int j = 0;
    int k = 0;
    this.b.setLength(0);
    while (i != this.d.length())
    {
      char c1 = this.d.charAt(i);
      if (c1 == '"')
      {
        if (k == 0)
          j = j == 0 ? 1 : 0;
        else
          this.b.append(c1);
        k = 0;
      }
      else if ((k != 0) || (j != 0))
      {
        this.b.append(c1);
        k = 0;
      }
      else if (c1 == '\\')
      {
        k = 1;
      }
      else
      {
        if (c1 == ',')
          break;
        this.b.append(c1);
      }
      i++;
    }
    this.c = i;
    return this.b.toString().trim();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509NameTokenizer
 * JD-Core Version:    0.6.0
 */