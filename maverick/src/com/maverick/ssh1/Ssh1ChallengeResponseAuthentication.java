package com.maverick.ssh1;

import com.maverick.ssh.SshAuthentication;

public class Ssh1ChallengeResponseAuthentication
  implements SshAuthentication
{
  String c;
  Prompt b;

  public String getMethod()
  {
    return "challenge";
  }

  public String getUsername()
  {
    return this.c;
  }

  public void setUsername(String paramString)
  {
    this.c = paramString;
  }

  public void setPrompt(Prompt paramPrompt)
  {
    this.b = paramPrompt;
  }

  public Prompt getPrompt()
  {
    return this.b;
  }

  public static abstract interface Prompt
  {
    public abstract String getResponse(String paramString);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.Ssh1ChallengeResponseAuthentication
 * JD-Core Version:    0.6.0
 */