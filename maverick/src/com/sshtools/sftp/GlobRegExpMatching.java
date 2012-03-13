package com.sshtools.sftp;

import com.maverick.sftp.SftpFile;
import com.maverick.sftp.SftpStatusException;
import com.maverick.ssh.SshException;
import java.io.File;
import java.util.Vector;
import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Matcher;

public class GlobRegExpMatching
  implements RegularExpressionMatching
{
  public String[] matchFileNamesWithPattern(File[] paramArrayOfFile, String paramString)
    throws SshException
  {
    Pattern localPattern = null;
    GlobCompiler localGlobCompiler = new GlobCompiler();
    Perl5Matcher localPerl5Matcher = new Perl5Matcher();
    try
    {
      localPattern = localGlobCompiler.compile(paramString);
    }
    catch (MalformedPatternException localMalformedPatternException)
    {
      throw new SshException("Invalid regular expression:" + localMalformedPatternException.getMessage(), 4);
    }
    Vector localVector = new Vector();
    for (int i = 0; i < paramArrayOfFile.length; i++)
    {
      if ((paramArrayOfFile[i].getName().equals(".")) || (paramArrayOfFile[i].getName().equals("..")) || (paramArrayOfFile[i].isDirectory()) || (!localPerl5Matcher.matches(paramArrayOfFile[i].getName(), localPattern)))
        continue;
      localVector.addElement(paramArrayOfFile[i].getAbsolutePath());
    }
    String[] arrayOfString = new String[localVector.size()];
    localVector.copyInto(arrayOfString);
    return arrayOfString;
  }

  public SftpFile[] matchFilesWithPattern(SftpFile[] paramArrayOfSftpFile, String paramString)
    throws SftpStatusException, SshException
  {
    Pattern localPattern = null;
    GlobCompiler localGlobCompiler = new GlobCompiler();
    Perl5Matcher localPerl5Matcher = new Perl5Matcher();
    try
    {
      localPattern = localGlobCompiler.compile(paramString);
    }
    catch (MalformedPatternException localMalformedPatternException)
    {
      throw new SshException("Invalid regular expression:" + localMalformedPatternException.getMessage(), 4);
    }
    Vector localVector = new Vector();
    for (int i = 0; i < paramArrayOfSftpFile.length; i++)
    {
      if ((paramArrayOfSftpFile[i].getFilename().equals(".")) || (paramArrayOfSftpFile[i].getFilename().equals("..")) || (paramArrayOfSftpFile[i].isDirectory()) || (!localPerl5Matcher.matches(paramArrayOfSftpFile[i].getFilename(), localPattern)))
        continue;
      localVector.addElement(paramArrayOfSftpFile[i]);
    }
    SftpFile[] arrayOfSftpFile = new SftpFile[localVector.size()];
    localVector.copyInto(arrayOfSftpFile);
    return arrayOfSftpFile;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.sftp.GlobRegExpMatching
 * JD-Core Version:    0.6.0
 */