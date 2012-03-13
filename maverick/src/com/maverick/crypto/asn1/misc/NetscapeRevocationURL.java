package com.maverick.crypto.asn1.misc;

import com.maverick.crypto.asn1.DERIA5String;

public class NetscapeRevocationURL extends DERIA5String
{
  public NetscapeRevocationURL(DERIA5String paramDERIA5String)
  {
    super(paramDERIA5String.getString());
  }

  public String toString()
  {
    return "NetscapeRevocationURL: " + getString();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.misc.NetscapeRevocationURL
 * JD-Core Version:    0.6.0
 */