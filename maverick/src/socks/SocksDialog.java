package socks;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocksDialog extends Dialog
  implements WindowListener, ItemListener, ActionListener, Runnable
{
  TextField bb;
  TextField m;
  TextField w;
  TextField z;
  TextField db;
  Button f;
  Button b;
  Button v;
  Button h;
  Button e;
  List l;
  Checkbox g;
  Checkbox j;
  Checkbox i;
  Checkbox t;
  Checkbox p;
  Dialog r;
  Label ab;
  String s;
  String cb;
  String d;
  int n;
  Thread c = null;
  CheckboxGroup q = new CheckboxGroup();
  Proxy u;
  InetRange k;
  int o;
  public static boolean useThreads = true;

  public SocksDialog(Frame paramFrame)
  {
    this(paramFrame, null);
  }

  public SocksDialog(Frame paramFrame, Proxy paramProxy)
  {
    super(paramFrame, "Proxy Configuration", true);
    this.r = new Dialog(paramFrame, "Warning", true);
    e();
    setResizable(false);
    addWindowListener(this);
    Component[] arrayOfComponent = getComponents();
    for (int i1 = 0; i1 < arrayOfComponent.length; i1++)
      if ((arrayOfComponent[i1] instanceof Button))
      {
        ((Button)arrayOfComponent[i1]).addActionListener(this);
      }
      else if ((arrayOfComponent[i1] instanceof TextField))
      {
        ((TextField)arrayOfComponent[i1]).addActionListener(this);
      }
      else
      {
        if (!(arrayOfComponent[i1] instanceof Checkbox))
          continue;
        ((Checkbox)arrayOfComponent[i1]).addItemListener(this);
      }
    this.u = paramProxy;
    if (this.u != null)
      b(this.u);
    else
      this.k = new InetRange();
    this.e.addActionListener(this);
    this.r.addWindowListener(this);
  }

  public Proxy getProxy()
  {
    this.o = 0;
    pack();
    show();
    return this.u;
  }

  public Proxy getProxy(Proxy paramProxy)
  {
    if (paramProxy != null)
      b(paramProxy);
    this.o = 0;
    pack();
    show();
    return this.u;
  }

  public void windowActivated(WindowEvent paramWindowEvent)
  {
  }

  public void windowDeactivated(WindowEvent paramWindowEvent)
  {
  }

  public void windowOpened(WindowEvent paramWindowEvent)
  {
  }

  public void windowClosing(WindowEvent paramWindowEvent)
  {
    Window localWindow = paramWindowEvent.getWindow();
    if (localWindow == this)
      h();
    else if (localWindow == this.r)
      g();
  }

  public void windowClosed(WindowEvent paramWindowEvent)
  {
  }

  public void windowIconified(WindowEvent paramWindowEvent)
  {
  }

  public void windowDeiconified(WindowEvent paramWindowEvent)
  {
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    Object localObject = paramActionEvent.getSource();
    if (localObject == this.v)
      h();
    else if ((localObject == this.f) || (localObject == this.db))
      j();
    else if (localObject == this.b)
      d();
    else if (localObject == this.e)
      g();
    else if ((localObject == this.h) || ((localObject instanceof TextField)))
      f();
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    Object localObject = paramItemEvent.getSource();
    if ((localObject == this.j) || (localObject == this.g))
      i();
    else if (localObject == this.t)
      b();
  }

  public void run()
  {
    if (!c())
    {
      if (this.o != 1)
        return;
      if (this.c != Thread.currentThread())
        return;
      this.o = 0;
      this.ab.setText("Look up failed.");
      this.ab.invalidate();
      return;
    }
    while (!this.r.isShowing());
    this.r.dispose();
  }

  private void f()
  {
    this.s = this.bb.getText().trim();
    this.cb = this.w.getText();
    this.d = this.z.getText();
    if (this.s.length() == 0)
    {
      b("Proxy host is not set!");
      return;
    }
    if (this.q.getSelectedCheckbox() == this.g)
    {
      if (this.cb.length() == 0)
      {
        b("User name is not set");
        return;
      }
    }
    else if (this.t.getState())
    {
      if (this.cb.length() == 0)
      {
        b("User name is not set.");
        return;
      }
      if (this.d.length() == 0)
      {
        b("Password is not set.");
        return;
      }
    }
    else if (!this.i.getState())
    {
      b("Please select at least one Authentication Method.");
      return;
    }
    try
    {
      this.n = Integer.parseInt(this.m.getText());
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b("Proxy port is invalid!");
      return;
    }
    this.o = 1;
    if (useThreads)
    {
      this.c = new Thread(this);
      this.c.start();
      c("Looking up host: " + this.s);
    }
    else if (!c())
    {
      b("Proxy host is invalid.");
      this.o = 0;
    }
    if (this.o == 1)
      dispose();
  }

  private void h()
  {
    this.u = null;
    dispose();
  }

  private void j()
  {
    String str = this.db.getText();
    str.trim();
    if (str.length() == 0)
      return;
    String[] arrayOfString = this.l.getItems();
    for (int i1 = 0; i1 < arrayOfString.length; i1++)
      if (arrayOfString[i1].equals(str))
        return;
    this.l.add(str);
    this.k.add(str);
  }

  private void d()
  {
    int i1 = this.l.getSelectedIndex();
    if (i1 < 0)
      return;
    this.k.remove(this.l.getItem(i1));
    this.l.remove(i1);
    this.l.select(i1);
  }

  private void i()
  {
    Checkbox localCheckbox = this.q.getSelectedCheckbox();
    if (localCheckbox == this.g)
    {
      this.w.setEnabled(true);
      this.z.setEnabled(false);
      this.i.setEnabled(false);
      this.t.setEnabled(false);
    }
    else
    {
      if (this.t.getState())
      {
        this.w.setEnabled(true);
        this.z.setEnabled(true);
      }
      else
      {
        this.w.setEnabled(false);
        this.z.setEnabled(false);
      }
      this.i.setEnabled(true);
      this.t.setEnabled(true);
    }
  }

  private void b()
  {
    if (this.t.getState())
    {
      this.w.setEnabled(true);
      this.z.setEnabled(true);
    }
    else
    {
      this.w.setEnabled(false);
      this.z.setEnabled(false);
    }
  }

  private void g()
  {
    this.r.dispose();
    if (this.o == 1)
    {
      this.o = 0;
      if (this.c != null)
        this.c.interrupt();
    }
  }

  private void b(Proxy paramProxy)
  {
    if (paramProxy.version == 5)
    {
      this.q.setSelectedCheckbox(this.j);
      i();
      if (((Socks5Proxy)paramProxy).getAuthenticationMethod(0) != null)
        this.i.setState(true);
      localObject = (UserPasswordAuthentication)((Socks5Proxy)paramProxy).getAuthenticationMethod(2);
      if (localObject != null)
      {
        this.w.setText(((UserPasswordAuthentication)localObject).getUser());
        this.z.setText(((UserPasswordAuthentication)localObject).getPassword());
        this.t.setState(true);
        b();
      }
    }
    else
    {
      this.q.setSelectedCheckbox(this.g);
      i();
      this.w.setText(((Socks4Proxy)paramProxy).b);
    }
    this.k = ((InetRange)(InetRange)paramProxy.directHosts.clone());
    Object localObject = this.k.getAll();
    this.l.removeAll();
    for (int i1 = 0; i1 < localObject.length; i1++)
      this.l.add(localObject[i1]);
    this.bb.setText(paramProxy.proxyIP.getHostName());
    this.m.setText("" + paramProxy.proxyPort);
  }

  private boolean c()
  {
    try
    {
      if (this.q.getSelectedCheckbox() == this.j)
      {
        this.u = new Socks5Proxy(this.s, this.n);
        if (this.t.getState())
          ((Socks5Proxy)this.u).setAuthenticationMethod(2, new UserPasswordAuthentication(this.cb, this.d));
        if (!this.i.getState())
          ((Socks5Proxy)this.u).setAuthenticationMethod(0, null);
      }
      else
      {
        this.u = new Socks4Proxy(this.s, this.n, this.cb);
      }
    }
    catch (UnknownHostException localUnknownHostException)
    {
      return false;
    }
    this.u.directHosts = this.k;
    return true;
  }

  private void c(String paramString)
  {
    b("Info", paramString);
  }

  private void b(String paramString)
  {
    b("Warning", paramString);
  }

  private void b(String paramString1, String paramString2)
  {
    this.ab.setText(paramString2);
    this.ab.invalidate();
    this.r.setTitle(paramString1);
    this.r.pack();
    this.r.show();
  }

  void e()
  {
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    Font localFont = new Font("Dialog", 0, 12);
    SocksDialog localSocksDialog = this;
    localSocksDialog.setLayout(new GridBagLayout());
    localSocksDialog.setFont(localFont);
    localSocksDialog.setBackground(SystemColor.menu);
    localGridBagConstraints.insets = new Insets(3, 3, 3, 3);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 12;
    Label localLabel = new Label("Host:");
    localSocksDialog.add(localLabel, localGridBagConstraints);
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.bb = new TextField("socks-proxy", 15);
    localSocksDialog.add(this.bb, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 12;
    localLabel = new Label("Port:");
    localSocksDialog.add(localLabel, localGridBagConstraints);
    localGridBagConstraints.gridx = 4;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.m = new TextField("1080", 5);
    localSocksDialog.add(this.m, localGridBagConstraints);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.gridwidth = 3;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 11;
    this.g = new Checkbox("Socks4", this.q, false);
    this.g.setFont(new Font(localFont.getName(), 1, 14));
    localSocksDialog.add(this.g, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 11;
    this.j = new Checkbox("Socks5", this.q, true);
    this.j.setFont(new Font(localFont.getName(), 1, 14));
    localSocksDialog.add(this.j, localGridBagConstraints);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 13;
    localLabel = new Label("User Id:");
    localSocksDialog.add(localLabel, localGridBagConstraints);
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.w = new TextField("", 15);
    this.w.setEnabled(false);
    localSocksDialog.add(this.w, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 11;
    localLabel = new Label("Authentication");
    localLabel.setFont(new Font(localFont.getName(), 1, 14));
    localSocksDialog.add(localLabel, localGridBagConstraints);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 13;
    localLabel = new Label("Password:");
    localSocksDialog.add(localLabel, localGridBagConstraints);
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.z = new TextField("", 15);
    this.z.setEchoChar('*');
    this.z.setEnabled(false);
    localSocksDialog.add(this.z, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.i = new Checkbox("None", true);
    localSocksDialog.add(this.i, localGridBagConstraints);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.gridwidth = 3;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 11;
    localLabel = new Label("Direct Hosts");
    localLabel.setFont(new Font(localFont.getName(), 1, 14));
    localSocksDialog.add(localLabel, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.t = new Checkbox("User/Password", false);
    localSocksDialog.add(this.t, localGridBagConstraints);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 5;
    localGridBagConstraints.gridwidth = 3;
    localGridBagConstraints.gridheight = 2;
    localGridBagConstraints.anchor = 18;
    localGridBagConstraints.fill = 1;
    this.l = new List(3);
    localSocksDialog.add(this.l, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 5;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.p = new Checkbox("GSSAPI", false);
    this.p.setEnabled(false);
    localSocksDialog.add(this.p, localGridBagConstraints);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 7;
    localGridBagConstraints.gridwidth = 3;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.db = new TextField("", 25);
    localSocksDialog.add(this.db, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 7;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 11;
    this.f = new Button("Add");
    localSocksDialog.add(this.f, localGridBagConstraints);
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 6;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 11;
    this.b = new Button("Remove");
    localSocksDialog.add(this.b, localGridBagConstraints);
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 8;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 11;
    this.v = new Button("Cancel");
    localSocksDialog.add(this.v, localGridBagConstraints);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 8;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.anchor = 18;
    this.h = new Button("OK");
    localSocksDialog.add(this.h, localGridBagConstraints);
    this.e = new Button("Dismiss");
    this.ab = new Label("", 1);
    this.ab.setFont(new Font("Dialog", 1, 15));
    Panel localPanel = new Panel();
    localPanel.add(this.e);
    this.r.add(localPanel, "South");
    this.r.add(this.ab, "Center");
    this.r.setResizable(false);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.SocksDialog
 * JD-Core Version:    0.6.0
 */