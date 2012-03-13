package com.sshtools.publickey;

public class InvalidPassphraseException extends Exception
{
  public InvalidPassphraseException()
  {
    super("The passphrase supplied was invalid!");
  }

  public InvalidPassphraseException(Exception paramException)
  {
    super(paramException.getMessage());
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.InvalidPassphraseException
 * JD-Core Version:    0.6.0
 */