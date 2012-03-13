package org.apache.oro.text.regex;

public interface PatternCompiler
{
  public abstract Pattern compile(String paramString)
    throws MalformedPatternException;

  public abstract Pattern compile(String paramString, int paramInt)
    throws MalformedPatternException;

  public abstract Pattern compile(char[] paramArrayOfChar)
    throws MalformedPatternException;

  public abstract Pattern compile(char[] paramArrayOfChar, int paramInt)
    throws MalformedPatternException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.PatternCompiler
 * JD-Core Version:    0.6.0
 */