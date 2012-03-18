using System;
using System.Collections.Generic;
using System.Windows.Forms;

using log4net;
using log4net.Config;

using Erlang.NET;
using System.Reflection;

namespace Layout
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {

            XmlConfigurator.Configure();

            if (args.Length != 2)
            {
                string app = AppDomain.CurrentDomain.FriendlyName;
                Console.WriteLine("Usage: " + app + " sname cookie\r\nfor example:" + app + " layout 3ren");
                return;
            }

            string host = System.Net.Dns.GetHostName();
            string name = args[0];
            string cookie = args[1];

            OtpServer self = new OtpServer(name, cookie);
            self.publishPort();

            Console.WriteLine("Layout Node:" + name + "@" + host + " started.................");

            OtpConnection client = null;
            ProcessHandler handler = null;

            while (true)
            {
                client = self.accept();
                handler = new ProcessHandler(client);
            }

        }
    }
}