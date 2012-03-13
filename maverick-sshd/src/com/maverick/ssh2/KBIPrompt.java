package com.maverick.ssh2;

public class KBIPrompt
{
  private String A;
  private String C;
  private boolean B;

  public KBIPrompt(String paramString, boolean paramBoolean)
  {
    this.A = paramString;
    this.B = paramBoolean;
  }

  public String getPrompt()
  {
    return this.A;
  }

  public boolean echo()
  {
    return this.B;
  }

  public void setResponse(String paramString)
  {
    this.C = paramString;
  }

  public String getResponse()
  {
    return this.C;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh2.KBIPrompt
 * JD-Core Version:    0.6.0
 */