package org.apache.oro.text.regex;

public interface PatternMatcher
{
  public abstract boolean matches(String paramString, Pattern paramPattern);

  public abstract boolean matches(char[] paramArrayOfChar, Pattern paramPattern);

  public abstract boolean matches(PatternMatcherInput paramPatternMatcherInput, Pattern paramPattern);

  public abstract boolean matchesPrefix(char[] paramArrayOfChar, Pattern paramPattern, int paramInt);

  public abstract boolean matchesPrefix(String paramString, Pattern paramPattern);

  public abstract boolean matchesPrefix(char[] paramArrayOfChar, Pattern paramPattern);

  public abstract boolean matchesPrefix(PatternMatcherInput paramPatternMatcherInput, Pattern paramPattern);

  public abstract boolean contains(String paramString, Pattern paramPattern);

  public abstract boolean contains(char[] paramArrayOfChar, Pattern paramPattern);

  public abstract boolean contains(PatternMatcherInput paramPatternMatcherInput, Pattern paramPattern);

  public abstract MatchResult getMatch();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.PatternMatcher
 * JD-Core Version:    0.6.0
 */