package com.maverick.sshd.platform;

import com.maverick.ssh2.KBIPrompt;
import com.maverick.sshd.KeyboardInteractiveAuthentication;

public abstract interface KeyboardInteractiveProvider
{
  public abstract KBIPrompt[] init(String paramString, KeyboardInteractiveAuthentication paramKeyboardInteractiveAuthentication);

  public abstract KBIPrompt[] setResponse(String[] paramArrayOfString);

  public abstract String getName();

  public abstract String getInstruction();

  public abstract boolean hasAuthenticated();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.platform.KeyboardInteractiveProvider
 * JD-Core Version:    0.6.0
 */