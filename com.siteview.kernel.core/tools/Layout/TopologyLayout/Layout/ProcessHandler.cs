using System;
using System.Collections.Generic;
using System.Text;

using Erlang.NET;
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

    class GlobalAtom
    {
        public readonly static OtpErlangAtom GEN_CALL = new OtpErlangAtom("$gen_call");

        public readonly static OtpErlangAtom IS_AUTH = new OtpErlangAtom("is_auth");
        public readonly static OtpErlangAtom YES = new OtpErlangAtom("yes");

        public readonly static OtpErlangAtom OK = new OtpErlangAtom("ok");
        public readonly static OtpErlangAtom ERROR = new OtpErlangAtom("error");

        public readonly static OtpErlangAtom CALL = new OtpErlangAtom("call");
        public readonly static OtpErlangAtom LAYOUT = new OtpErlangAtom("layout");
        public readonly static OtpErlangAtom DO = new OtpErlangAtom("do");
    }

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
                    if (!id2link.ContainsKey(link.ToString()))
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

    class ProcessHandler
    {
        private readonly Thread thread;
        private OtpConnection client = null;


        public ProcessHandler(OtpConnection client)
        {
            this.client = client;
            this.thread = new Thread(new ThreadStart(run));
            this.thread.IsBackground = true;
            this.thread.Name = client.Name;
            this.thread.Start();
        }

        //Layout(DeviceList, LinkList, LayoutType, NodeSize, Options) -> LayoutResult.

        //DeviceList:[Id1,Id2,Id3…]
        //DeviceList = ['1300:166480:547003','1300:166479:516007','1300:166478:250001',,,,]

        //LinkList:[{LeftId1,RightId1},{LeftId2,RightId2},{LeftId3,RightId3},…]
        //LinkList = [{'1300:166480:547003','1300:166479:516007'},{'1300:166478:250001','1300:166479:516007'},,,]
        //LeftId:线段起始端点Id，值为Device中的Id
        //RightId:线段终止端点Id，值为Device中的Id                                

        //LayoutType为:organic | circular | rectangular | vertical
        //       organic:普遍排版(默认)
        //       circular:环形
        //       rectangular:直角
        //       vertical:垂直

        //NodeSize ->{w,h}
        //w:设备的宽，默认值是32
        //h:设备的高，默认值是32

        //Options->[{key1, value1},{key2,value2}......]
        //为附加参数，所有为key:value的键值对，key为原子，而value为任意值，根据排版类型的不同而不同

        //LayoutResult->[{devices,[{Id1, {X1, Y1}},{ID2, {X2, Y2}}...]}, {links, [{{Id1, Id2}, [{X1, Y1},{X2, Y2}....]}, ]}]

        private OtpErlangTuple LayoutResult(Layout layout)
        {

            OtpErlangObject[] devices = new OtpErlangObject[layout.Devices.Count];
            for (int i = 0; i < layout.Devices.Count; i++)
            {
                Device device = layout.Devices[i];
                OtpErlangAtom ID = new OtpErlangAtom(device.ID);
                OtpErlangTuple coordinate = new OtpErlangTuple(new OtpErlangObject[]{new OtpErlangFloat(device.Coordinate.X), new OtpErlangFloat(device.Coordinate.Y)});
                devices[i] = new OtpErlangTuple(new OtpErlangObject[] { ID, coordinate});
            }


            OtpErlangObject[] links = new OtpErlangObject[layout.Links.Count];
            for (int i = 0; i < layout.Links.Count; i++)
            {
                Link link = layout.Links[i];

                OtpErlangTuple soure2target = new OtpErlangTuple(new OtpErlangObject[] { new OtpErlangAtom(link.Source), new OtpErlangAtom(link.Target) });
                
                OtpErlangObject[] points = new OtpErlangObject[link.Polyline.Count];
                for (int j = 0; j < link.Polyline.Count; j++)
                {
                    points[j] = new OtpErlangTuple(new OtpErlangObject[] { new OtpErlangFloat(link.Polyline[j].X), new OtpErlangFloat(link.Polyline[j].Y)});
                }
                links[i] = new OtpErlangTuple(new OtpErlangObject[] { soure2target, new OtpErlangList(points)});

            }

            OtpErlangObject[] list = new OtpErlangObject[]{
                                                            new OtpErlangTuple(new OtpErlangObject[]{new OtpErlangAtom("devices"), new OtpErlangList(devices)}),
                                                            new OtpErlangTuple(new OtpErlangObject[]{new OtpErlangAtom("links"), new OtpErlangList(links)})
                                                            };

            return new OtpErlangTuple(new OtpErlangObject[] { GlobalAtom.OK,  new OtpErlangList(list)});

        }

        private OtpErlangTuple CallHandle(OtpErlangTuple request)
        {
            OtpErlangAtom module = (OtpErlangAtom)request.elementAt(1);
            OtpErlangAtom function = (OtpErlangAtom)request.elementAt(2);
            OtpErlangList paramemters = (OtpErlangList)request.elementAt(3);

            if (!module.Equals(GlobalAtom.LAYOUT))
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("module name error!") });
            }

            if (!function.Equals(GlobalAtom.DO))
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("function name error!") });
            }

            if (paramemters.arity() != 5)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("parameter count error!") });
            }

            //[['1300:166480:547003','1300:166479:516007'],[{'1300:166480:547003','1300:166479:516007'}],circular,{32.0, 32.0},[]]

            OtpErlangTuple deviceSize = paramemters.elementAt(3) as OtpErlangTuple;

            if (deviceSize == null)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("size parameter error!") });
            }

            if (deviceSize.arity() != 2)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("size format error!") });
            }

            OtpErlangDouble width = deviceSize.elementAt(0) as OtpErlangDouble;
            if (width == null)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("width format error!") });
            }

            OtpErlangDouble height = deviceSize.elementAt(1) as OtpErlangDouble;
            if (height == null)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("heigth format error!") });
            }

            Layout layout = new Layout();

            SizeF size = new SizeF((float)width.doubleValue(), (float)height.doubleValue());

            PointF position = new PointF(0.0F, 0.0F);

            OtpErlangList deviceList = paramemters.elementAt(0) as OtpErlangList;

            if (deviceList == null)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("devices parameter error!") });
            }

            for (int i = 0; i < deviceList.arity(); i++)
            {
                Device device = new Device();
                device.ID = ((OtpErlangAtom)deviceList.elementAt(i)).atomValue();
                device.Size = size;
                device.Coordinate = position;
                layout.Devices.Add(device);
            }


            OtpErlangList linkList = paramemters.elementAt(1) as OtpErlangList;

            if (linkList == null)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("links parameter error!") });
            }

            for (int i = 0; i < linkList.arity(); i++)
            {
                OtpErlangTuple deviceLink = linkList.elementAt(i) as OtpErlangTuple;
                Link link = new Link();
                link.Source = ((OtpErlangAtom)deviceLink.elementAt(0)).atomValue();
                link.Target = ((OtpErlangAtom)deviceLink.elementAt(1)).atomValue();
                link.Polyline = new List<PointF>();
                layout.Links.Add(link);
            }

            OtpErlangAtom deviceLayout = paramemters.elementAt(2) as OtpErlangAtom;

            if (deviceLayout == null)
            {
                return new OtpErlangTuple(new OtpErlangObject[2] { GlobalAtom.ERROR, new OtpErlangList("layout parameter error!") });
            }

            switch (deviceLayout.atomValue())
            {
                case "organic":
                    layout.LayoutType = LayoutType.organic;
                    break;
                case "circular":
                    layout.LayoutType = LayoutType.circular;
                    break;
                case "rectangular":
                    layout.LayoutType = LayoutType.rectangular;
                    break;
                case "vertical":
                    layout.LayoutType = LayoutType.vertical;
                    break;
                default:
                    layout.LayoutType = LayoutType.organic;
                    break;
            }

            Console.WriteLine("---->LayoutType:" + layout.LayoutType.ToString());

            layout.DoLayout();

            return LayoutResult(layout);

        }


        public void run()
        {
            try
            {
                while (true)
                {

                    OtpErlangTuple message = this.client.receive() as OtpErlangTuple;

                    Console.WriteLine(this.thread.Name + "-->Receive:" + message.ToString());

                    // node
                    OtpErlangTuple node = (OtpErlangTuple)message.elementAt(1);

                    // pid
                    OtpErlangPid npid = (OtpErlangPid)node.elementAt(0);
                    // #Ref
                    OtpErlangRef nref = (OtpErlangRef)node.elementAt(1);

                    // request
                    OtpErlangTuple request = (OtpErlangTuple)message.elementAt(2);

                    OtpErlangAtom method = (OtpErlangAtom)request.elementAt(0);

                    //{'$gen_call',{#Pid<client@developer.36.0>,#Ref<client@developer.44.0.0>},{call,layout,do,[['1300:166480:547003','1300:166479:516007'],[{'1300:166480:547003','1300:166479:516007'}],circular,{32.0, 32.0},[]],#Pid<client@developer.29.0>}}

                    if (method.Equals(GlobalAtom.CALL))
                    {
                        OtpErlangObject[] result = new OtpErlangObject[2] { nref, CallHandle(request) };
                        this.client.send(npid, new OtpErlangTuple(result));
                                   
                    }
                    //{'$gen_call',{#Pid<client@developer.36.0>,#Ref<client@developer.120.0.0>},{is_auth,client@developer}}
                    else if (method.Equals(GlobalAtom.IS_AUTH))
                    {
                        OtpErlangObject[] pong = new OtpErlangObject[2] { nref, GlobalAtom.YES };
                        this.client.send(npid, new OtpErlangTuple(pong));
                    }
                    else
                    {
                        OtpErlangObject[] result = new OtpErlangObject[2] { nref, new OtpErlangTuple(new OtpErlangObject[2]{GlobalAtom.ERROR, new OtpErlangList("unkown message error!")})};
                        this.client.send(npid, new OtpErlangTuple(result));
                        continue;
                    }

                }

            }
            catch (Exception e)
            {
                if (e.Message.Equals("'Remote has closed connection'"))
                {
                    Console.WriteLine(this.client.Name + " shut down!");
                }
                else
                {
                    Console.WriteLine(this.thread.Name + "-->unkown error:" + e.Message);
                }
            }
            finally
            {
                if (this.client.isConnected())
                {
                    this.client.close();
                }
            }
        }
    }
}
