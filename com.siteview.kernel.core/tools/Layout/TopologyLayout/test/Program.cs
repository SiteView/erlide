using System;
using System.Collections.Generic;
using System.Windows.Forms;

using yWorks.yFiles.Algorithms;
using yWorks.yFiles.Algorithms.Geometry;
using yWorks.yFiles.Algorithms.Util;
using yWorks.yFiles.Layout;
using yWorks.yFiles.Layout.Hierarchic;
using yWorks.yFiles.Layout.Hierarchic.Incremental;


namespace Layout
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            //DefaultLayoutGraph graph = new DefaultLayoutGraph();

            ////construct graph. assign sizes to nodes
            //Node v1 = graph.CreateNode();
            //graph.SetSize(v1, 30, 30);
            //Node v2 = graph.CreateNode();
            //graph.SetSize(v2, 30, 30);
            //Node v3 = graph.CreateNode();
            //graph.SetSize(v3, 30, 30);

            //Edge e1 = graph.CreateEdge(v1, v2);
            //Edge e2 = graph.CreateEdge(v1, v3);

            ////optionally setup some port constraints for IncrementalHierarchicLayouter
            //IEdgeMap spc = graph.CreateEdgeMap();
            //IEdgeMap tpc = graph.CreateEdgeMap();
            ////e1 shall leave and enter the node on the right side
            //spc.Set(e1, PortConstraint.Create(PortSide.East));
            ////additionally set a strong port constraint on the target side. 
            //tpc.Set(e1, PortConstraint.Create(PortSide.East, true));
            ////ports with strong port constraints will not be reset by the 
            ////layouter.  So we specify the target port right now to connect 
            ////to the upper left corner of the node 
            //graph.SetTargetPointRel(e1, new YPoint(15, -15));

            ////e2 shall leave and enter the node on the top side
            //spc.Set(e2, PortConstraint.Create(PortSide.North));
            //tpc.Set(e2, PortConstraint.Create(PortSide.North));

            //graph.AddDataProvider(PortConstraintKeys.SourcePortConstraintDpKey, spc);
            //graph.AddDataProvider(PortConstraintKeys.TargetPortConstraintDpKey, tpc);

            //IncrementalHierarchicLayouter layouter = new IncrementalHierarchicLayouter();
            //layouter.LayoutMode = LayoutMode.FromScratch;

            //new BufferedLayouter(layouter).DoLayout(graph);

            //Console.WriteLine("\n\nGRAPH LAID OUT HIERARCHICALLY FROM SCRATCH");
            //Console.WriteLine("v1 center position = " + graph.GetCenter(v1));
            //Console.WriteLine("v2 center position = " + graph.GetCenter(v2));
            //Console.WriteLine("v3 center position = " + graph.GetCenter(v3));
            //Console.WriteLine("e1 path = " + graph.GetPath(e1));
            //Console.WriteLine("e2 path = " + graph.GetPath(e2));

            ////display the graph in a simple viewer
            //GraphViewer gv = new GraphViewer();
            //gv.AddLayoutGraph(new CopiedLayoutGraph(graph), "Before Addition");

            //// now add a node and two edges incrementally...
            //Node v4 = graph.CreateNode();
            //graph.SetSize(v4, 30, 30);

            //Edge e4 = graph.CreateEdge(v4, v2);
            //Edge e3 = graph.CreateEdge(v1, v4);

            ////mark elements as newly added so that the layout algorithm can place 
            ////them nicely.
            //IIncrementalHintsFactory ihf = layouter.CreateIncrementalHintsFactory();
            //IDataMap map = Maps.CreateHashedDataMap();
            //map.Set(v4, ihf.CreateLayerIncrementallyHint(v4));
            //map.Set(e3, ihf.CreateSequenceIncrementallyHint(e3));
            //map.Set(e4, ihf.CreateSequenceIncrementallyHint(e4));
            //graph.AddDataProvider(IncrementalHierarchicLayouter.IncrementalHintsDpKey, map);
            //layouter.LayoutMode = LayoutMode.Incremental;

            //new BufferedLayouter(layouter).DoLayout(graph);

            //Console.WriteLine("\n\nGRAPH AFTER ELEMENTS HAVE BEEN ADDED INCREMENTALLY");
            //Console.WriteLine("v1 center position = " + graph.GetCenter(v1));
            //Console.WriteLine("v2 center position = " + graph.GetCenter(v2));
            //Console.WriteLine("v3 center position = " + graph.GetCenter(v3));
            //Console.WriteLine("v4 center position = " + graph.GetCenter(v4));
            //Console.WriteLine("e1 path = " + graph.GetPath(e1));
            //Console.WriteLine("e2 path = " + graph.GetPath(e2));
            //Console.WriteLine("e3 path = " + graph.GetPath(e3));
            //Console.WriteLine("e4 path = " + graph.GetPath(e4));

            ////clean up data maps
            //graph.RemoveDataProvider(IncrementalHierarchicLayouter.IncrementalHintsDpKey);

            ////display the graph in a simple viewer
            //gv.AddLayoutGraph(new CopiedLayoutGraph(graph), "After Addition");
            //Application.Run(gv);

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Main());
        }
    }
}