package com.maverick.ssh.components.jce;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Hashtable;

public class JCEProvider
  implements JCEAlgorithms
{
  static Provider a = null;
  static Hashtable Y = new Hashtable();
  static String _ = "SHA1PRNG";
  static SecureRandom Z;

  public static void initializeDefaultProvider(Provider paramProvider)
  {
    a = paramProvider;
  }

  public static void initializeProviderForAlgorithm(String paramString, Provider paramProvider)
  {
    Y.put(paramString, paramProvider);
  }

  public static String getSecureRandomAlgorithm()
  {
    return _;
  }

  public static void setSecureRandomAlgorithm(String paramString)
  {
    _ = paramString;
  }

  public static Provider getProviderForAlgorithm(String paramString)
  {
    if (Y.containsKey(paramString))
      return (Provider)Y.get(paramString);
    return a;
  }

  public static SecureRandom getSecureRandom()
    throws NoSuchAlgorithmException
  {
    if (Z == null)
      try
      {
        return JCEProvider.Z = getProviderForAlgorithm(getSecureRandomAlgorithm()) == null ? SecureRandom.getInstance(getSecureRandomAlgorithm()) : SecureRandom.getInstance(getSecureRandomAlgorithm(), getProviderForAlgorithm(getSecureRandomAlgorithm()));
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        return JCEProvider.Z = SecureRandom.getInstance(getSecureRandomAlgorithm());
      }
    return Z;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.JCEProvider
 * JD-Core Version:    0.6.0
 */