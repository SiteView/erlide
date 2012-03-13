package com.maverick.ssh.components.jce;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Hashtable;

public class JCEProvider
  implements JCEAlgorithms
{
  static Provider s = null;
  static Hashtable p = new Hashtable();
  static String r = "SHA1PRNG";
  static SecureRandom q;

  public static void initializeDefaultProvider(Provider paramProvider)
  {
    s = paramProvider;
  }

  public static void initializeProviderForAlgorithm(String paramString, Provider paramProvider)
  {
    p.put(paramString, paramProvider);
  }

  public static String getSecureRandomAlgorithm()
  {
    return r;
  }

  public static void setSecureRandomAlgorithm(String paramString)
  {
    r = paramString;
  }

  public static Provider getProviderForAlgorithm(String paramString)
  {
    if (p.containsKey(paramString))
      return (Provider)p.get(paramString);
    return s;
  }

  public static SecureRandom getSecureRandom()
    throws NoSuchAlgorithmException
  {
    if (q == null)
      try
      {
        return JCEProvider.q = getProviderForAlgorithm(getSecureRandomAlgorithm()) == null ? SecureRandom.getInstance(getSecureRandomAlgorithm()) : SecureRandom.getInstance(getSecureRandomAlgorithm(), getProviderForAlgorithm(getSecureRandomAlgorithm()));
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        return JCEProvider.q = SecureRandom.getInstance(getSecureRandomAlgorithm());
      }
    return q;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.JCEProvider
 * JD-Core Version:    0.6.0
 */