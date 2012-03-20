/****************************************************************************
 **
 ** This file is part of yFiles.NET 4.0.
 ** 
 ** yWorks proprietary/confidential. Use is subject to license terms.
 **
 ** Redistribution of this file or of an unauthorized byte-code version
 ** of this file is strictly forbidden.
 **
 ** Copyright (c) 2003-2011 by yWorks GmbH, Vor dem Kreuzberg 28, 
 ** 72070 Tuebingen, Germany. All rights reserved.
 **
 ***************************************************************************/
using System;
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using yWorks.yFiles.Algorithms;
using yWorks.yFiles.Algorithms.Geometry;
using yWorks.yFiles.Layout;
using yWorks.yFiles.Layout.Grouping;


namespace Layout
{
  public class LayoutGraphPanel : Panel
  {
    private LayoutGraph layoutGraph;

    private Pen edgePen;
    private Pen nodeBorderPen;
    private Pen labelBorderPen;
    private GraphGrouping grouping;
    private SolidBrush nodeFillBrush;
    private Font labelFont;
    private Brush labelBrush;

    private Rectangle worldRect = new Rectangle(0, 0, 500, 500);
    private int insets = 20;
    private float zoom = 1.0f;
    private Color nodeFillColor = Color.Yellow;

    public Rectangle WorldRect {
      get { return this.worldRect; }

      set {
        this.worldRect = value;
        this.AutoScrollMinSize = new Size((int) (value.Width*zoom), (int) (value.Height*zoom));
        this.Invalidate();
      }
    }

    public float Zoom {
      get { return this.zoom; }

      set {
        float newValue = Math.Min(1000f, Math.Max(0.01f, value));
        if (newValue != this.Zoom) {
          this.zoom = newValue;
          this.AutoScrollMinSize = new Size((int) (this.worldRect.Width*zoom), (int) (this.worldRect.Height*zoom));
          this.Invalidate();
        }
      }
    }

    public LayoutGraphPanel(LayoutGraph graph) {
      if (!GraphGrouping.IsFlat(graph)) {
        grouping = new GraphGrouping(graph);
      }

      this.BackColor = Color.White;
      this.BorderStyle = BorderStyle.Fixed3D;
      this.AutoScroll = true;
      global::yWorks.yFiles.Util.Geom.Rectangle rect = graph.GetBoundingBox();
      this.HScroll = true;
      this.VScroll = true;
      this.layoutGraph = graph;
      this.edgePen = new Pen(Brushes.Black, 1);
      this.nodeBorderPen = new Pen(Brushes.DarkGray, 1);
      this.labelBorderPen = new Pen(Brushes.Red, 1);
      this.nodeFillBrush = new SolidBrush(nodeFillColor);
      this.labelBrush = Brushes.Black;
      this.labelFont = new Font("sansserif", 6);
      this.MouseDown += new MouseEventHandler(LayoutGraphPanel_MouseDown);
      SetWorldRect(rect.X - insets, rect.Y - insets, rect.Width + insets*2, rect.Height + insets*2);
    }

    public void SetWorldRect(double x, double y, double w, double h) {
      this.worldRect.X = (int) x;
      this.worldRect.Y = (int) y;
      this.worldRect.Width = (int) w;
      this.worldRect.Height = (int) h;
      this.AutoScrollMinSize = new Size((int) (w*zoom), (int) (h*zoom));
    }

    protected override void OnPaint(PaintEventArgs e) {
      base.OnPaint(e);
      Graphics g = e.Graphics;
      g.SmoothingMode = SmoothingMode.AntiAlias;
      g.TranslateTransform(this.AutoScrollPosition.X/zoom - this.worldRect.X,
                           this.AutoScrollPosition.Y/zoom - this.worldRect.Y, MatrixOrder.Append);
      g.ScaleTransform(this.zoom, this.zoom, MatrixOrder.Append);
      PaintGraph(g);
    }

    public void PaintGraph(Graphics g) {
      if (this.grouping != null) {
        for (ListCell cell = this.grouping.GetChildren(this.grouping.Root).FirstCell; cell != null; cell = cell.Succ()) {
          Node node = (Node) cell.Info;
          PaintNodeAndChildren(g, layoutGraph, node);
        }
      } else {
        foreach (Node node in layoutGraph.Nodes) {
          PaintNode(g, layoutGraph, node);
          INodeLabelLayout[] labels = layoutGraph.GetNodeLabelLayout(node);
          if (labels != null && labels.Length > 0) {
            foreach (INodeLabelLayout label in labels) {
              PaintNodeLabel(g, layoutGraph, node, label);
            }
          }
        }
      }

      foreach (Edge edge in layoutGraph.Edges) {
        PaintEdge(g, layoutGraph, edge);
        IEdgeLabelLayout[] labels = layoutGraph.GetEdgeLabelLayout(edge);
        if (labels != null && labels.Length > 0) {
          foreach (IEdgeLabelLayout label in labels) {
            PaintEdgeLabel(g, layoutGraph, edge, label);
          }
        }
      }
    }

    private void PaintNodeAndChildren(Graphics g, LayoutGraph graph, Node node) {
      PaintNode(g, graph, node);
      INodeLabelLayout[] labels = layoutGraph.GetNodeLabelLayout(node);
      if (labels != null && labels.Length > 0) {
        foreach (INodeLabelLayout label in labels) {
          PaintNodeLabel(g, layoutGraph, node, label);
        }
      }
      if (grouping.IsGroupNode(node)) {
        SolidBrush oldBrush = this.nodeFillBrush;
        Color color = Color.FromArgb(Math.Min(255, (int) (oldBrush.Color.R*0.9f)),
                                     Math.Min(255, (int) (oldBrush.Color.G*0.9f)),
                                     Math.Min(255, (int) (oldBrush.Color.B*0.9f)));
        this.nodeFillBrush = new SolidBrush(color);
        for (ListCell cell = this.grouping.GetChildren(node).FirstCell; cell != null; cell = cell.Succ()) {
          Node child = (Node) cell.Info;
          PaintNodeAndChildren(g, layoutGraph, child);
        }
        this.nodeFillBrush.Dispose();
        this.nodeFillBrush = oldBrush;
      }
    }

    protected void PaintNodeLabel(Graphics g, LayoutGraph graph, Node node, INodeLabelLayout label) {
      YRectangle box = label.Box;
      YPoint pos = GetNodeLabelLocation(graph, node, label);
      points[0] = new PointF((float) pos.X, (float) pos.Y);
      points[1] = new PointF((float) (pos.X + box.Width), (float) pos.Y);
      points[2] = new PointF((float) (pos.X + box.Width), (float) (pos.Y + box.Height));
      points[3] = new PointF((float) pos.X, (float) (pos.Y + box.Height));
      g.DrawPolygon(this.labelBorderPen, points);
    }

    protected void PaintEdgeLabel(Graphics g, LayoutGraph graph, Edge edge, IEdgeLabelLayout label) {
      YRectangle box = label.Box;
      YPoint pos = GetEdgeLabelLocation(graph, edge, label);
      points[0] = new PointF((float) pos.X, (float) pos.Y);
      points[1] = new PointF((float) (pos.X + box.Width), (float) pos.Y);
      points[2] = new PointF((float) (pos.X + box.Width), (float) (pos.Y + box.Height));
      points[3] = new PointF((float) pos.X, (float) (pos.Y + box.Height));
      g.DrawPolygon(this.labelBorderPen, points);
    }

      protected void PaintEdge(Graphics g, LayoutGraph graph, Edge e)
      {
      IEdgeLayout el = graph.GetEdgeLayout(e);
      YPoint sp = graph.GetSourcePointAbs(e);
      YPoint tp = graph.GetTargetPointAbs(e);
      PointF[] points = new PointF[el.PointCount() + 2];
      points[0] = new PointF((float) sp.X, (float) sp.Y);
      points[el.PointCount() + 1] = new PointF((float) tp.X, (float) tp.Y);
      for (int i = 0; i < el.PointCount(); i++) {
        YPoint p = el.GetPoint(i);
        points[i + 1] = new PointF((float) p.X, (float) p.Y);
      }
      g.DrawLines(edgePen, points);
    }

    private PointF[] points = new PointF[4];

    protected void PaintNode(Graphics g, LayoutGraph graph, Node node) {
      INodeLayout nl = graph.GetNodeLayout(node);
      RectangleF rect = RectangleF.FromLTRB((float) nl.X, (float) nl.Y, (float) (nl.X + nl.Width),
                                            (float) (nl.Y + nl.Height));
      Region r = new Region(rect);
      g.FillRegion(nodeFillBrush, r);
      points[0] = new PointF(rect.X, rect.Y);
      points[1] = new PointF(rect.X + rect.Width, rect.Y);
      points[2] = new PointF(rect.X + rect.Width, rect.Y + rect.Height);
      points[3] = new PointF(rect.X, rect.Y + rect.Height);
      g.DrawPolygon(nodeBorderPen, points);
      string text = node.Index.ToString();
      SizeF size = g.MeasureString(text, labelFont);
      g.DrawString(text, labelFont, labelBrush, rect.X + rect.Width*0.5f - size.Width*0.5f,
                   rect.Y + rect.Height*0.5f - size.Height*0.5f);
    }

    public void ExportToEmf(string emfFile) {
      // Create a Bitmap
      Bitmap bitmap = new Bitmap(1, 1, PixelFormat.Format24bppRgb);
      // Wrap a Graphics around the Bitmap
      Graphics gRef = Graphics.FromImage(bitmap);
      // Get an HDC from the Graphics
      IntPtr hDC = gRef.GetHdc();
      // Create a recordable Metafile
      Metafile mf = new Metafile(emfFile, hDC);
      // Release the HDC
      gRef.ReleaseHdc(hDC);
      // Wrap a Graphics around the Metafile so we can draw on it
      Graphics g = Graphics.FromImage(mf);
      // Draw on our Metafile
      PaintGraph(g);
      g.Flush();
      mf.Dispose();
      g.Dispose();
      gRef.Dispose();
      bitmap.Dispose();
    }

    private static YPoint GetEdgeLabelLocation(LayoutGraph graph, Edge e, IEdgeLabelLayout ell) {
      YPoint ellp = ell.LabelModel.GetLabelPlacement(
        ell.Box,
        graph.GetEdgeLayout(e),
        graph.GetNodeLayout(e.Source),
        graph.GetNodeLayout(e.Target),
        ell.ModelParameter);
      return ellp;
    }

    private static YPoint GetNodeLabelLocation(LayoutGraph graph, Node n, INodeLabelLayout nll) {
      return nll.LabelModel.GetLabelPlacement(
        nll.Box,
        graph.GetNodeLayout(n),
        nll.ModelParameter);
    }

    protected override void OnMouseWheel(MouseEventArgs e) {
      int numberOfTextLinesToMove = e.Delta*SystemInformation.MouseWheelScrollLines/120;
      if (numberOfTextLinesToMove != 0) {
        if (numberOfTextLinesToMove > 0) {
          for (int i = 0; i < numberOfTextLinesToMove; i++) {
            this.Zoom *= 1.2F;
          }
        } else {
          for (int i = 0; i > numberOfTextLinesToMove; i--) {
            this.Zoom /= 1.2F;
          }
        }
      }
    }

    private void LayoutGraphPanel_MouseDown(object sender, MouseEventArgs e) {
      this.Focus();
    }
  }
}