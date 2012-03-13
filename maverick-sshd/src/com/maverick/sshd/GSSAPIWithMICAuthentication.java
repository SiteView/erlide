package com.maverick.sshd;

import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import com.maverick.util.UnsignedInteger32;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

public class GSSAPIWithMICAuthentication
  implements AuthenticationMechanism
{
  static final Log S = LogFactory.getLog(GSSAPIWithMICAuthentication.class);
  private TransportProtocol Q;
  private AuthenticationProtocol V;
  private Subject W;
  private Oid X;
  private LoginContext R;
  private String U;
  private GSSContext P;
  private byte[] T;

  public String getMethod()
  {
    return "gssapi-with-mic";
  }

  public void init(TransportProtocol paramTransportProtocol, AuthenticationProtocol paramAuthenticationProtocol, byte[] paramArrayOfByte)
    throws IOException
  {
    this.Q = paramTransportProtocol;
    this.V = paramAuthenticationProtocol;
    this.T = paramArrayOfByte;
    try
    {
      SshContext localSshContext = paramTransportProtocol.getSshContext();
      this.X = new Oid("1.2.840.113554.1.2.2");
      localObject = new CallbackHandler(localSshContext)
      {
        public void handle(Callback[] paramArrayOfCallback)
          throws IOException, UnsupportedCallbackException
        {
          for (int i = 0; i < paramArrayOfCallback.length; i++)
          {
            Object localObject;
            if ((paramArrayOfCallback[i] instanceof NameCallback))
            {
              localObject = GSSAPIWithMICAuthentication.this.getServicePrinicipal(this.val$context);
              if ((localObject != null) && (!((String)localObject).equals("")))
                ((NameCallback)paramArrayOfCallback[i]).setName((String)localObject);
            }
            else
            {
              if (!(paramArrayOfCallback[i] instanceof PasswordCallback))
                continue;
              localObject = this.val$context.getKerberosServicePassword();
              if (localObject == null)
                continue;
              ((PasswordCallback)paramArrayOfCallback[i]).setPassword(localObject);
            }
          }
        }
      };
      this.R = null;
      if (localSshContext.getKerberosConfiguration() != null)
        this.R = new LoginContext("com.maverick.sshd.gssapi", null, (CallbackHandler)localObject, localSshContext.getKerberosConfiguration());
      else
        this.R = new LoginContext("com.maverick.sshd.gssapi", null, (CallbackHandler)localObject, createDefaultConfiguration(localSshContext));
      this.R.login();
      if (S.isDebugEnabled())
        S.debug("Logged into GSSAPI context");
      this.W = this.R.getSubject();
    }
    catch (GSSException localGSSException)
    {
      localObject = new IOException("Failed to initialise GSS");
      ((IOException)localObject).initCause(localGSSException);
      throw ((Throwable)localObject);
    }
    catch (LoginException localLoginException)
    {
      Object localObject = new IOException("Failed to login via GSS to Kerbero server");
      ((IOException)localObject).initCause(localLoginException);
      throw ((Throwable)localObject);
    }
  }

  protected Configuration createDefaultConfiguration(SshContext paramSshContext)
  {
    return new Configuration(paramSshContext)
    {
      public AppConfigurationEntry[] getAppConfigurationEntry(String paramString)
      {
        HashMap localHashMap = new HashMap();
        String str = GSSAPIWithMICAuthentication.this.getServicePrinicipal(this.val$context);
        if (str != null)
          localHashMap.put("principal", str);
        localHashMap.put("debug", String.valueOf(GSSAPIWithMICAuthentication.S.isDebugEnabled()));
        localHashMap.put("isInitiator", "false");
        localHashMap.put("useKeyTab", "false");
        localHashMap.put("storeKey", "true");
        localHashMap.put("useTicketCache", "false");
        localHashMap.put("useSubjectCredsOnly", "true");
        localHashMap.put("doNotPrompt", String.valueOf((this.val$context.getKerberosServicePassword() == null) && ((this.val$context.getKerberosServicePrincipal() == null) || (this.val$context.getKerberosServicePrincipal().equals("")))));
        return new AppConfigurationEntry[] { new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, localHashMap) };
      }
    };
  }

  protected String getServicePrinicipal(SshContext paramSshContext)
  {
    if (paramSshContext.getKerberosServicePrincipal() != null)
      return paramSshContext.getKerberosServicePrincipal();
    try
    {
      return "host/" + InetAddress.getLocalHost().getCanonicalHostName();
    }
    catch (UnknownHostException localUnknownHostException)
    {
      S.error("Failed to get canonical hostname. You should set the Kerberos service principal manually.", localUnknownHostException);
    }
    return null;
  }

  public boolean startRequest(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    this.U = paramString;
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    UnsignedInteger32 localUnsignedInteger32 = localByteArrayReader.readUINT32();
    Object localObject;
    try
    {
      byte[] arrayOfByte1 = this.X.getDER();
      for (int i = 0; i < localUnsignedInteger32.intValue(); i++)
      {
        byte[] arrayOfByte2 = localByteArrayReader.readBinaryString();
        if (Arrays.equals(arrayOfByte2, arrayOfByte1))
          continue;
        S.error("Client requested an Oid that is not supported.");
        this.V.failedAuthentication();
        return false;
      }
      this.P = A();
      if (this.P == null)
      {
        S.error("No GSS context, rejecting authentication.");
        this.V.failedAuthentication();
        return false;
      }
      localObject = new ByteArrayWriter();
      ((ByteArrayWriter)localObject).write(60);
      ((ByteArrayWriter)localObject).writeBinaryString(arrayOfByte1);
      this.Q.postMessage(new SshMessage((ByteArrayWriter)localObject)
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(this.val$baw.toByteArray());
          return true;
        }

        public void messageSent()
        {
          if (GSSAPIWithMICAuthentication.S.isDebugEnabled())
            GSSAPIWithMICAuthentication.S.debug("Sent SSH_MSG_USERAUTH_GSSAPI_RESPONSE");
        }
      });
      return true;
    }
    catch (GSSException localGSSException)
    {
      localObject = new IOException("Failed to decide Oid");
      ((IOException)localObject).initCause(localGSSException);
    }
    throw ((Throwable)localObject);
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    int i = localByteArrayReader.read();
    if (this.P.isEstablished())
    {
      if (S.isDebugEnabled())
        S.debug("GSS established");
      if (i != 66)
      {
        S.warn("Expected GSSAPI_MIC because context was established. Rejecting authentication.");
        this.V.failedAuthentication(false, false);
        return true;
      }
      A(localByteArrayReader);
    }
    else
    {
      if (S.isDebugEnabled())
        S.debug("GSS not established");
      if (i != 61)
      {
        S.error("Expected SSH_MSG_USERAUTH_GSSAPI_TOKEN");
        this.V.failedAuthentication();
      }
      else
      {
        byte[] arrayOfByte = localByteArrayReader.readBinaryString();
        try
        {
          A(arrayOfByte);
        }
        catch (GSSException localGSSException)
        {
          S.error("GSS failed", localGSSException);
          this.V.failedAuthentication(false, false);
        }
      }
    }
    return true;
  }

  private void A(byte[] paramArrayOfByte)
    throws GSSException, IOException
  {
    byte[] arrayOfByte = this.P.acceptSecContext(paramArrayOfByte, 0, paramArrayOfByte.length);
    if (S.isDebugEnabled())
      S.debug("Security context accept: " + arrayOfByte);
    boolean bool = this.P.isEstablished();
    if (arrayOfByte != null)
    {
      this.Q.postMessage(new SshMessage(arrayOfByte)
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(61);
          ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
          try
          {
            localByteArrayWriter.writeBinaryString(this.val$returnVal);
          }
          catch (IOException localIOException)
          {
            throw new Error(localIOException);
          }
          paramByteBuffer.put(localByteArrayWriter.toByteArray());
          return true;
        }

        public void messageSent()
        {
          if (GSSAPIWithMICAuthentication.S.isDebugEnabled())
            GSSAPIWithMICAuthentication.S.debug("Sent SSH_MSG_USERAUTH_GSSAPI_TOKEN");
        }
      });
    }
    else
    {
      if (S.isDebugEnabled())
        S.debug("No token returned, sending result of " + bool);
      if (bool)
        this.V.completedAuthentication();
      else
        this.V.failedAuthentication(false, false);
    }
  }

  private void A(ByteArrayReader paramByteArrayReader)
    throws IOException
  {
    byte[] arrayOfByte1 = paramByteArrayReader.readBinaryString();
    ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
    localByteArrayWriter.writeBinaryString(this.T);
    localByteArrayWriter.write(50);
    localByteArrayWriter.writeString(this.U);
    localByteArrayWriter.writeString("ssh-connection");
    localByteArrayWriter.writeString(getMethod());
    byte[] arrayOfByte2 = localByteArrayWriter.toByteArray();
    try
    {
      this.P.verifyMIC(arrayOfByte1, 0, arrayOfByte1.length, arrayOfByte2, 0, arrayOfByte2.length, new MessageProp(false));
      if (S.isDebugEnabled())
        S.debug("MIC OK");
      this.V.completedAuthentication();
    }
    catch (GSSException localGSSException)
    {
      if (S.isDebugEnabled())
        S.debug("GSS verify failed. ", localGSSException);
      this.V.failedAuthentication();
    }
  }

  private GSSContext A()
    throws GSSException
  {
    GSSManager localGSSManager = GSSManager.getInstance();
    GSSCredential localGSSCredential = (GSSCredential)Subject.doAs(this.W, new PrivilegedAction(localGSSManager)
    {
      public Object run()
      {
        try
        {
          return this.val$manager.createCredential(null, 2147483647, GSSAPIWithMICAuthentication.this.X, 2);
        }
        catch (Exception localException)
        {
          GSSAPIWithMICAuthentication.S.error("Failed to create GSS credential.", localException);
        }
        return null;
      }
    });
    if (localGSSCredential == null)
      return null;
    return localGSSManager.createContext(localGSSCredential);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.GSSAPIWithMICAuthentication
 * JD-Core Version:    0.6.0
 */