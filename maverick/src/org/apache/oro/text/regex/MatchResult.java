package org.apache.oro.text.regex;

public interface MatchResult
{
  public abstract String group(int paramInt);

  public abstract int begin(int paramInt);

  public abstract int end(int paramInt);

  public abstract int endOffset(int paramInt);

  public abstract String toString();

  public abstract int groups();

  public abstract int beginOffset(int paramInt);

  public abstract int length();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.MatchResult
 * JD-Core Version:    0.6.0
 */