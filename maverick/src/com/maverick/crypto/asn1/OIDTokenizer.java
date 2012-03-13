package com.maverick.crypto.asn1;

public class OIDTokenizer
{
  private String c;
  private int b;

  public OIDTokenizer(String paramString)
  {
    this.c = paramString;
    this.b = 0;
  }

  public boolean hasMoreTokens()
  {
    return this.b != -1;
  }

  public String nextToken()
  {
    if (this.b == -1)
      return null;
    int i = this.c.indexOf('.', this.b);
    if (i == -1)
    {
      str = this.c.substring(this.b);
      this.b = -1;
      return str;
    }
    String str = this.c.substring(this.b, i);
    this.b = (i + 1);
    return str;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.OIDTokenizer
 * JD-Core Version:    0.6.0
 */