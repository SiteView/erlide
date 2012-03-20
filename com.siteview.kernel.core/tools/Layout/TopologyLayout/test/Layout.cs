using System;
using System.Collections.Generic;
using System.Text;

using System.Threading;
using System.Drawing;

using yWorks.yFiles.Algorithms;
using yWorks.yFiles.Algorithms.Geometry;
using yWorks.yFiles.Algorithms.Util;
using yWorks.yFiles.Layout;
using yWorks.yFiles.Layout.Hierarchic;
using yWorks.yFiles.Layout.Circular;
using yWorks.yFiles.Layout.Orthogonal;
using LayoutStyle = yWorks.yFiles.Layout.Hierarchic.LayoutStyle;
using yWorks.yFiles.Layout.Organic;

namespace Layout
{

    public enum LayoutType
    {
        organic,
        circular,
        rectangular,
        vertical
    }

    struct Device
    {
        public String ID;
        public SizeF Size;
        public PointF Coordinate;

        public Device Clone()
        {
            Device device = new Device();
            device.ID = this.ID;
            device.Size = this.Size;
            device.Coordinate = this.Coordinate;

            return device;
        }
    }

    struct Link 
    {
        public String Source;
        public String Target;
        public List<PointF> Polyline;
        public override string ToString()
        {
            return Source + "," + Target;
        }
    }

    class Layout
    {
        private List<Device> devices;
        private List<Link> links;
        private LayoutType layoutType;
        private Dictionary<string, Object> parameters;

        public Layout()
        {
            this.devices = new List<Device>();
            this.links = new List<Link>();
            this.layoutType = LayoutType.organic;
            this.parameters = new Dictionary<string, object>();
        }

        public void Clear()
        {
            this.devices.Clear();
            this.links.Clear();
            this.parameters.Clear();
        }

        public List<Device> Devices
        {
            get
            {
                return this.devices;
            }
            set
            {
                this.devices = value;
            }
        }

        public List<Link> Links
        {
            get
            {
                return this.links;
            }
            set
            {
                this.links = value;
            }
        }

        public LayoutType LayoutType
        {
            get
            {
                return this.layoutType;
            }
            set
            {
                this.layoutType = value;
            }
        }

        public Dictionary<string, Object> Parameters
        {
            get
            {
                return this.parameters;
            }
            set
            {
                this.parameters = value;
            }
        }

        public void DoLayout()
        {
            DefaultLayoutGraph graph = new DefaultLayoutGraph();
            Dictionary<string, Node> id2node = new Dictionary<string, Node>();
            Dictionary<int, string> index2id = new Dictionary<int, string>();
            Dictionary<string, Link> id2link = new Dictionary<string,Link>();

            foreach (Device device in this.devices)
            {
                Node node = graph.CreateNode();
                graph.SetSize(node, device.Size.Width, device.Size.Height);
                id2node.Add(device.ID, node);
                index2id.Add(node.Index, device.ID);
            }

            foreach (Link link in this.links)
            {
                Node source = null;
                Node target = null;

                link.Polyline.Clear();

                id2node.TryGetValue(link.Source, out source);
                id2node.TryGetValue(link.Target, out target);
                if (source != null && target != null)
                {
                    if(!id2link.ContainsKey(link.ToString()))
                    {
                        graph.CreateEdge(source, target);
                        id2link.Add(link.ToString(), link);
                    }
                }
            }

            switch (this.layoutType)
            {
                case LayoutType.circular:
                    CircularLayouter circularLayouter = new CircularLayouter();
                    circularLayouter.BalloonLayouter.MinimalEdgeLength = 20;
                    circularLayouter.BalloonLayouter.CompactnessFactor = 0.1;
                    new BufferedLayouter(circularLayouter).DoLayout(graph);
                    LayoutTool.ClipEdgesOnBB(graph);
                    break;
                case LayoutType.rectangular:
                    OrthogonalLayouter rectangularLayouter = new OrthogonalLayouter();
                    rectangularLayouter.Grid = 15;
                    rectangularLayouter.OptimizePerceivedBends = true;
                    new BufferedLayouter(rectangularLayouter).DoLayout(graph);
                    break;
                case LayoutType.vertical:
                    HierarchicLayouter verticalLayouter = new HierarchicLayouter();
                    verticalLayouter.LayoutStyle = LayoutStyle.MedianSimplex;
                    new BufferedLayouter(verticalLayouter).DoLayout(graph);
                    break;
                default:
                    SmartOrganicLayouter organicLayouter = new SmartOrganicLayouter();
                    organicLayouter.QualityTimeRatio = 1.0;
                    organicLayouter.NodeOverlapsAllowed = true;
                    organicLayouter.MinimalNodeDistance = 10;
                    organicLayouter.PreferredEdgeLength = 50;
                    new BufferedLayouter(organicLayouter).DoLayout(graph);
                    LayoutTool.ClipEdgesOnBB(graph);
                    break;
            }

            for(int i = 0; i < this.devices.Count; i++)
            {
                Device device = this.devices[i].Clone();
                Node node = id2node[device.ID];
                device.Coordinate = new PointF((float)graph.GetX(node),(float)graph.GetY(node));
                this.devices[i] = device;
                foreach(Edge edge in node.Edges)
                {
                    string source = index2id[edge.Source.Index];
                    string target = index2id[edge.Target.Index];
                    if (edge.Source.Equals(node))
                    {
                        Link link = id2link[source + "," + target];
                        foreach (YPoint point in graph.GetPath(edge).ToArray())
                        {
                            link.Polyline.Add(new PointF((float)point.X, (float)point.Y));
                        }
                    }
                }
            }

            index2id.Clear();
            id2link.Clear();
            id2node.Clear();

            graph.Clear();
        }
    }
    
}
