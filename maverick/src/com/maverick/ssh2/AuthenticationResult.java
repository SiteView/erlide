package com.maverick.ssh2;

public class AuthenticationResult extends Throwable
{
  int b;
  String c;

  public AuthenticationResult(int paramInt)
  {
    this.b = paramInt;
  }

  public AuthenticationResult(int paramInt, String paramString)
  {
    this.b = paramInt;
    this.c = paramString;
  }

  public int getResult()
  {
    return this.b;
  }

  public String getAuthenticationMethods()
  {
    return this.c;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.AuthenticationResult
 * JD-Core Version:    0.6.0
 */