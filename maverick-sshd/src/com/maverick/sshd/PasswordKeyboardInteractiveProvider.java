package com.maverick.sshd;

import com.maverick.ssh2.KBIPrompt;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.sshd.platform.KeyboardInteractiveProvider;
import com.maverick.sshd.platform.PasswordChangeException;

public class PasswordKeyboardInteractiveProvider
  implements KeyboardInteractiveProvider
{
  String H;
  String D;
  KeyboardInteractiveAuthentication E;
  boolean G = false;
  String C = "password";
  String A = "";
  int F = 1;
  int B = 2;

  public boolean hasAuthenticated()
  {
    return this.G;
  }

  public KBIPrompt[] setResponse(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length == 0)
      throw new RuntimeException("Not enough answers!");
    Object localObject;
    switch (this.F)
    {
    case 1:
      this.D = paramArrayOfString[0];
      try
      {
        this.G = this.E.getProvider().logon(this.E.getSessionid(), this.H, this.D, this.E.getTransport().getRemoteAddress());
        this.F = 2;
        return null;
      }
      catch (PasswordChangeException localPasswordChangeException1)
      {
        this.F = 2;
        localObject = new KBIPrompt[2];
        localObject[0] = new KBIPrompt("New Password:", false);
        localObject[1] = new KBIPrompt("Confirm Password:", false);
        if (localPasswordChangeException1.getMessage() == null)
          this.A = ("Enter new password for " + this.H);
        else
          this.A = localPasswordChangeException1.getMessage();
        return localObject;
      }
    case 2:
      if (paramArrayOfString.length < 2)
        throw new RuntimeException("Not enough answers!");
      if (this.B <= 0)
      {
        this.F = 2;
        return null;
      }
      String str = paramArrayOfString[0];
      localObject = paramArrayOfString[1];
      if (str.equals(localObject))
        try
        {
          this.G = this.E.getProvider().logon(this.E.getSessionid(), this.H, this.D, str, this.E.getTransport().getRemoteAddress());
          this.F = 2;
          return null;
        }
        catch (PasswordChangeException localPasswordChangeException2)
        {
          this.F = 2;
          KBIPrompt[] arrayOfKBIPrompt2 = new KBIPrompt[2];
          arrayOfKBIPrompt2[0] = new KBIPrompt("New Password:", false);
          arrayOfKBIPrompt2[1] = new KBIPrompt("Confirm Password:", false);
          if (localPasswordChangeException2.getMessage() == null)
            this.A = ("Password change failed! Enter new password for " + this.H);
          else
            this.A = localPasswordChangeException2.getMessage();
          this.B -= 1;
          return arrayOfKBIPrompt2;
        }
      KBIPrompt[] arrayOfKBIPrompt1 = new KBIPrompt[2];
      this.A = ("Passwords do not match! Enter new password for " + this.H);
      arrayOfKBIPrompt1[0] = new KBIPrompt("New Password:", false);
      arrayOfKBIPrompt1[1] = new KBIPrompt("Confirm Password:", false);
      this.B -= 1;
      return arrayOfKBIPrompt1;
    }
    throw new RuntimeException("We shouldn't be here");
  }

  public KBIPrompt[] init(String paramString, KeyboardInteractiveAuthentication paramKeyboardInteractiveAuthentication)
  {
    this.H = paramString;
    this.E = paramKeyboardInteractiveAuthentication;
    KBIPrompt[] arrayOfKBIPrompt = new KBIPrompt[1];
    arrayOfKBIPrompt[0] = new KBIPrompt("Password:", false);
    this.A = ("Enter password for " + paramString);
    return arrayOfKBIPrompt;
  }

  public String getInstruction()
  {
    return this.A;
  }

  public String getName()
  {
    return this.C;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.PasswordKeyboardInteractiveProvider
 * JD-Core Version:    0.6.0
 */