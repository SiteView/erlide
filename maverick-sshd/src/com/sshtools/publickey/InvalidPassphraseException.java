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

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.InvalidPassphraseException
 * JD-Core Version:    0.6.0
 */