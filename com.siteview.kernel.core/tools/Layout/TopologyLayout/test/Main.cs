using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace Layout
{
    public partial class Main : Form
    {
        public Main()
        {
            InitializeComponent();
        }

        private Layout layout = new Layout();
        private static readonly SizeF SIZE = new SizeF(32.0F, 32.0F);
        private static readonly PointF POSITION = new PointF(0.0F, 0.0F);

        private void openToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (this.folderBrowserDialog.ShowDialog() == DialogResult.OK)
            {
                layout.Clear();

                string dir = this.folderBrowserDialog.SelectedPath;
                StreamReader sr = null;
                string line = null;

                sr = new StreamReader(dir + "\\point.txt");
                while ((line = sr.ReadLine()) != null)
                {
                    if (line[0] == '[')
                    {
                        line = line.Substring(1);
                    }
                    else if (line.EndsWith("]"))
                    {
                        line = line.Remove(line.Length - 1);
                    }
                    else
                    {
                        //line = line;
                    }

                    string[] tokens = line.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                    foreach (string item in tokens)
                    {
                        Device device = new Device();
                        device.ID = item;
                        device.Size = SIZE;
                        device.Coordinate = POSITION;
                        layout.Devices.Add(device);
                    }
                }

                sr.Close();

                int index1 = 0;
                int index2 = 0;

                sr = new StreamReader(dir + "\\line.txt");
                while ((line = sr.ReadLine()) != null)
                {

                    index1 = line.IndexOf('{');
                    index2 = line.IndexOf('}');

                    line = line.Substring(index1 + 1, index2 - index1 - 1);
                    string[] tokens = line.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);

                    Link link = new Link();
                    link.Source = tokens[0];
                    link.Target = tokens[1];
                    link.Polyline = new List<PointF>();

                    this.layout.Links.Add(link);

                }

                sr.Close();


            }
        }

        private void Clear(Graphics graphics)
        {

        }



        private void verticalToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.layout.LayoutType = LayoutType.vertical;
            this.layout.DoLayout();
            this.canvas.Refresh();

        }

        private void organicToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.layout.LayoutType = LayoutType.organic;
            this.layout.DoLayout();
            this.canvas.Refresh();
           
        }

        private void circularToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.layout.LayoutType = LayoutType.circular;
            this.layout.DoLayout();
            this.canvas.Refresh();
            
        }

        private void rectangleToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.layout.LayoutType = LayoutType.rectangular;
            this.layout.DoLayout();
            this.canvas.Refresh();
           
        }

        private void container_Paint(object sender, PaintEventArgs e)
        {
            this.canvas.Location = new Point(0, 0);
            this.canvas.Width = this.container.Width;
            this.canvas.Height = this.container.Height;
        }

        private void canvas_Paint(object sender, PaintEventArgs e)
        {

            Graphics graphics = e.Graphics;
            foreach (Link link in this.layout.Links)
            {
                if (link.Polyline.Count > 0)
                {
                    graphics.DrawLines(Pens.Blue, link.Polyline.ToArray());
                }
            }

            foreach (Device device in this.layout.Devices)
            {
                if (this.canvas.Width < device.Size.Width + device.Coordinate.X)
                {
                    this.canvas.Width = (int)(device.Size.Width + device.Coordinate.X);
                }

                if (this.canvas.Height < device.Size.Height + device.Coordinate.Y)
                {
                    this.canvas.Height = (int)(device.Size.Height + device.Coordinate.Y);
                }

                graphics.FillRectangle(Brushes.Orange, (float)device.Coordinate.X + 1.5f, (float)device.Coordinate.Y + 1.5f, (float)device.Size.Width - 2.5f, (float)device.Size.Height - 2.5f);
                graphics.DrawRectangle(Pens.Pink, device.Coordinate.X, device.Coordinate.Y, device.Size.Width, device.Size.Height);
            }

        }
    }
}