package org.apache.oro.text;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;

public final class GlobCompiler
  implements PatternCompiler
{
  public static final int DEFAULT_MASK = 0;
  public static final int CASE_INSENSITIVE_MASK = 1;
  public static final int STAR_CANNOT_MATCH_NULL_MASK = 2;
  public static final int QUESTION_MATCHES_ZERO_OR_ONE_MASK = 4;
  public static final int READ_ONLY_MASK = 8;
  private Perl5Compiler b = new Perl5Compiler();

  public Pattern compile(String paramString)
    throws MalformedPatternException
  {
    return compile(paramString.toCharArray(), 0);
  }

  public Pattern compile(String paramString, int paramInt)
    throws MalformedPatternException
  {
    return compile(paramString.toCharArray(), paramInt);
  }

  public Pattern compile(char[] paramArrayOfChar)
    throws MalformedPatternException
  {
    return compile(paramArrayOfChar, 0);
  }

  public Pattern compile(char[] paramArrayOfChar, int paramInt)
    throws MalformedPatternException
  {
    int i = 0;
    if ((paramInt & 0x1) != 0)
      i |= 1;
    if ((paramInt & 0x8) != 0)
      i |= 32768;
    return this.b.compile(globToPerl5(paramArrayOfChar, paramInt), i);
  }

  public static String globToPerl5(char[] paramArrayOfChar, int paramInt)
  {
    int j = 0;
    StringBuffer localStringBuffer = new StringBuffer(2 * paramArrayOfChar.length);
    int i = 0;
    int k = (paramInt & 0x4) == 0 ? 0 : 1;
    j = (paramInt & 0x2) == 0 ? 0 : 1;
    for (int m = 0; m < paramArrayOfChar.length; m++)
      switch (paramArrayOfChar[m])
      {
      case '*':
        if (i != 0)
          localStringBuffer.append('*');
        else if (j != 0)
          localStringBuffer.append(".+");
        else
          localStringBuffer.append(".*");
        break;
      case '?':
        if (i != 0)
          localStringBuffer.append('?');
        else if (k != 0)
          localStringBuffer.append(".?");
        else
          localStringBuffer.append('.');
        break;
      case '[':
        i = 1;
        localStringBuffer.append(paramArrayOfChar[m]);
        if (m + 1 < paramArrayOfChar.length)
          switch (paramArrayOfChar[(m + 1)])
          {
          case '!':
          case '^':
            localStringBuffer.append('^');
            m++;
            break;
          case ']':
            localStringBuffer.append(']');
            m++;
          }
        break;
      case ']':
        i = 0;
        localStringBuffer.append(paramArrayOfChar[m]);
        break;
      case '\\':
        localStringBuffer.append('\\');
        if (m == paramArrayOfChar.length - 1)
        {
          localStringBuffer.append('\\');
        }
        else if (b(paramArrayOfChar[(m + 1)]))
        {
          m++;
          localStringBuffer.append(paramArrayOfChar[m]);
        }
        else
        {
          localStringBuffer.append('\\');
        }
        break;
      default:
        if ((i == 0) && (c(paramArrayOfChar[m])))
          localStringBuffer.append('\\');
        localStringBuffer.append(paramArrayOfChar[m]);
      }
    return localStringBuffer.toString();
  }

  private static boolean c(char paramChar)
  {
    return (paramChar == '*') || (paramChar == '?') || (paramChar == '+') || (paramChar == '[') || (paramChar == ']') || (paramChar == '(') || (paramChar == ')') || (paramChar == '|') || (paramChar == '^') || (paramChar == '$') || (paramChar == '.') || (paramChar == '{') || (paramChar == '}') || (paramChar == '\\');
  }

  private static boolean b(char paramChar)
  {
    return (paramChar == '*') || (paramChar == '?') || (paramChar == '[') || (paramChar == ']');
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.GlobCompiler
 * JD-Core Version:    0.6.0
 */