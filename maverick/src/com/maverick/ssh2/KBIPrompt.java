package com.maverick.ssh2;

public class KBIPrompt
{
  private String b;
  private String d;
  private boolean c;

  public KBIPrompt(String paramString, boolean paramBoolean)
  {
    this.b = paramString;
    this.c = paramBoolean;
  }

  public String getPrompt()
  {
    return this.b;
  }

  public boolean echo()
  {
    return this.c;
  }

  public void setResponse(String paramString)
  {
    this.d = paramString;
  }

  public String getResponse()
  {
    return this.d;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.KBIPrompt
 * JD-Core Version:    0.6.0
 */