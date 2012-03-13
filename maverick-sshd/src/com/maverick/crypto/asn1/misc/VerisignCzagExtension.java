package com.maverick.crypto.asn1.misc;

import com.maverick.crypto.asn1.DERIA5String;

public class VerisignCzagExtension extends DERIA5String
{
  public VerisignCzagExtension(DERIA5String paramDERIA5String)
  {
    super(paramDERIA5String.getString());
  }

  public String toString()
  {
    return "VerisignCzagExtension: " + getString();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.misc.VerisignCzagExtension
 * JD-Core Version:    0.6.0
 */