/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2016, 2018 by the contributors of the JetUML project.
 *
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ca.mcgill.cs.jetuml.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import ca.mcgill.cs.jetuml.diagram.builder.ClassDiagramBuilder;
import ca.mcgill.cs.jetuml.diagram.edges.AggregationEdge;
import ca.mcgill.cs.jetuml.diagram.edges.AssociationEdge;
import ca.mcgill.cs.jetuml.diagram.edges.DependencyEdge;
import ca.mcgill.cs.jetuml.diagram.edges.GeneralizationEdge;
import ca.mcgill.cs.jetuml.diagram.edges.NoteEdge;
import ca.mcgill.cs.jetuml.diagram.nodes.ClassNode;
import ca.mcgill.cs.jetuml.diagram.nodes.InterfaceNode;
import ca.mcgill.cs.jetuml.diagram.nodes.PackageNode;
import ca.mcgill.cs.jetuml.diagram.nodes.PointNode;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;

public class TestUsageScenariosClassDiagram extends AbstractTestUsageScenarios
{
	private ClassNode aClassNode1;
	private ClassNode aClassNode2;
	private InterfaceNode aInterfaceNode;
	private PackageNode aPackageNode;
	private AggregationEdge aAggregationEdge;
	private AssociationEdge aAssociationEdge;
	private DependencyEdge aDependencyEdge;
	private GeneralizationEdge aGeneralizationEdge;
	
	@Before
	public void setup()
	{
		super.setup();
		aDiagram = new ClassDiagram();
		aBuilder = new ClassDiagramBuilder(aDiagram);
		aClassNode1 = new ClassNode();
		aClassNode2 = new ClassNode();
		aInterfaceNode = new InterfaceNode();
		aPackageNode = new PackageNode();
		aAggregationEdge = new AggregationEdge();
		aAssociationEdge = new AssociationEdge();
		aDependencyEdge = new DependencyEdge();
		aGeneralizationEdge = new GeneralizationEdge();
	}
	
	@Test
	public void testClassDiagramCopyClassNodesAndEdgesInsidePackageNode()
	{
		addNode(aPackageNode, new Point(20, 20));
		addNode(aClassNode1, new Point(25, 25));
		addNode(aClassNode2, new Point(30, 30));
		moveNode(aClassNode2, 100, 0);

		addEdge(aDependencyEdge, new Point(31, 31), new Point(131, 31));
		assertEquals(1, numberOfRootNodes());
		assertEquals(2, aPackageNode.getChildren().size());
		assertSame(aClassNode1, aDependencyEdge.getStart());
		assertSame(aClassNode2, aDependencyEdge.getEnd());
		
		select(aClassNode1, aClassNode2, aDependencyEdge);
		copy();
		paste();

		assertEquals(3, numberOfRootNodes());
		assertEquals(2, numberOfEdges());
	}
	
	@Test
	public void testClassDiagramCopyPackageNodesAndEdgesInsidePackageNode()
	{
		addNode(aPackageNode, new Point(20, 20));
		addNode(aClassNode1, new Point(30, 30));
		addNode(aClassNode2, new Point(30, 30));
		moveNode(aClassNode2, 100, 0);
		addEdge(aDependencyEdge, new Point(31, 31), new Point(135, 30));
		
		assertEquals(1, numberOfRootNodes());
		assertEquals(2, aPackageNode.getChildren().size());
		assertEquals(1, numberOfEdges());
		assertSame(aClassNode1, aDependencyEdge.getStart());
		assertSame(aClassNode2, aDependencyEdge.getEnd());

		// Does nothing
		select(aDependencyEdge);
		copy();
		paste(); 
		assertEquals(1, numberOfRootNodes());
		assertEquals(1, numberOfEdges());
	}
	
	@Test
	public void testBasicNode()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addNode(aPackageNode, new Point(87, 87));
		addNode(aNoteNode, new Point(134, 132));

		assertEquals(4, numberOfRootNodes());
		
		setProperty(aClassNode1.properties().get("name"), "truck");
		setProperty(aClassNode1.properties().get("methods"), "setDriver()");
		setProperty(aInterfaceNode.properties().get("name"), "vehicle");
		setProperty(aInterfaceNode.properties().get("methods"), "getPrice()");
		setProperty(aPackageNode.properties().get("name"), "object");
		setProperty(aPackageNode.properties().get("contents"), "some stuff");
		setProperty(aNoteNode.properties().get("name"), "some text...");

		assertEquals(aClassNode1.getName(), "truck");
		assertEquals(aClassNode1.getMethods(), "setDriver()");
		assertEquals(aInterfaceNode.getMethods(), "getPrice()");
		assertEquals(aPackageNode.getName(), "object");
		assertEquals(aPackageNode.getContents(), "some stuff");
		assertEquals(aNoteNode.getName(), "some text...");
	}
	
	@Test
	public void testEdgeCreation()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addNode(aPackageNode, new Point(87, 87));
		addNode(aNoteNode, new Point(134, 132));
		
		// both start and end points are invalid
		assertFalse(aBuilder.canAdd(aAggregationEdge, new Point(70, 70), new Point(170, 170)));
		// one point is invalid
		assertFalse(aBuilder.canAdd(aAggregationEdge, new Point(6, 7), new Point(170, 170)));
		
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		addEdge(aAssociationEdge, new Point(47, 49), new Point(10, 17));
		addEdge(aDependencyEdge, new Point(90, 93), new Point(44, 49));
		assertEquals(3, numberOfEdges());
		
		addEdge(new AssociationEdge(), new Point(47, 49), new Point(50, 49));
		assertEquals(4, numberOfEdges());
		
		// not every edge is a valid self-edge
		assertFalse(aBuilder.canAdd(aGeneralizationEdge, new Point(47, 49), new Point(50, 49)));
	}
	
	@Test 
	public void testSelfEdgeCreation()
	{
		addNode(aInterfaceNode, new Point(44, 44));
		addEdge(aAssociationEdge, new Point(47, 49), new Point(50, 49));
		assertEquals(1, numberOfEdges());
		assertSame(aInterfaceNode, aAssociationEdge.getStart());
		assertSame(aInterfaceNode, aAssociationEdge.getEnd());
		
		// not every edge is a valid self-edge
		assertFalse(aBuilder.canAdd(aGeneralizationEdge, new Point(47, 49), new Point(50, 49)));
	}
	
	@Test
	public void testNoteEdgeCreation()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addNode(aPackageNode, new Point(87, 87));
		addNode(aNoteNode, new Point(134, 132));
		
		NoteEdge noteEdge1 = new NoteEdge();
		NoteEdge noteEdge2 = new NoteEdge();
		
		// if begin with a non-NoteNode type, both point needs to be valid
		assertFalse(aBuilder.canAdd(noteEdge1, new Point(9, 9), new Point(209,162)));
		addEdge(noteEdge1, new Point(10, 10), new Point(139,142));
		assertEquals(1, numberOfEdges());
		assertEquals(noteEdge1.getStart(), aClassNode1);
		assertEquals(noteEdge1.getEnd(), aNoteNode);
		
		// if begin with a NoteNode, the end point can be anywhere
		addEdge(noteEdge2, new Point(138, 140), new Point(9,9));
		assertEquals(noteEdge2.getStart(), aNoteNode);
		assertEquals(noteEdge2.getEnd().getClass(), PointNode.class);
		assertEquals(2, numberOfEdges());
	}
	
	@Test
	public void testSingleNodeMovement()
	{
		
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addNode(aPackageNode, new Point(87, 87));
		addNode(aNoteNode, new Point(134, 132));
		
		moveNode(aClassNode1, 5, 5);
		moveNode(aInterfaceNode, 11, 19);
		moveNode(aPackageNode, 32, -42);
		moveNode(aNoteNode, -5, 19);
		
		assertEquals(new Point(10, 10), aClassNode1.position());
		assertEquals(new Point(55, 63), aInterfaceNode.position());
		assertEquals(new Point(119, 45), aPackageNode.position());
		assertEquals(new Point(129, 151), aNoteNode.position());
	}
	
	@Test
	public void testSelectionNodeAndEdges()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addNode(aPackageNode, new Point(87, 87));
		addNode(aNoteNode, new Point(134, 132));
		
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		addEdge(aAssociationEdge, new Point(47, 49), new Point(10, 17));
		addEdge(aDependencyEdge, new Point(90, 93), new Point(44, 49));
		
		select(aClassNode1, aAggregationEdge, aInterfaceNode);
		moveSelection(10, 10);
		assertEquals(new Point(15, 15), aClassNode1.position());
		assertEquals(new Point(54, 54), aInterfaceNode.position());
		assertSame(aClassNode1, aAggregationEdge.getStart());
		assertSame(aInterfaceNode, aAggregationEdge.getEnd());
	}
	
	@Test
	public void testMoveNodeWithSelfEdge()
	{
		addNode(aClassNode1, new Point(5, 7));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(12, 10));
		moveNode(aClassNode1, 10, 10);
		
		assertEquals(new Point(15, 17), aClassNode1.position());
		assertSame(aClassNode1, aAggregationEdge.getStart());
		assertSame(aClassNode1, aAggregationEdge.getEnd());
	}
	
	@Test
	public void testMoveNodeConnectWithAnotherNode()
	{
	
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addNode(aPackageNode, new Point(87, 87));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		addEdge(aAssociationEdge, new Point(47, 49), new Point(10, 17));
		addEdge(aDependencyEdge, new Point(90, 93), new Point(44, 49));
		
		moveNode(aClassNode1, 20, 20);
		assertSame(aClassNode1, aAggregationEdge.getStart());
		assertSame(aInterfaceNode, aAggregationEdge.getEnd());

		moveNode(aInterfaceNode, -19, 45);
		assertSame(aPackageNode, aDependencyEdge.getStart());
		assertSame(aInterfaceNode, aDependencyEdge.getEnd());
	}
	
	@Test
	public void testDeleteSingleNode()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
				
		select(aClassNode1);
		deleteSelected();
		assertEquals(1, numberOfRootNodes());
		
		select(aInterfaceNode);
		deleteSelected();
		assertEquals(0, numberOfRootNodes());
		
		undo();
		assertEquals(1, numberOfRootNodes());
		undo();
		assertEquals(2, numberOfRootNodes());
	}
	
	@Test
	public void testDeleteSingleEdge()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		
		select(aAggregationEdge);
		deleteSelected();
		assertEquals(0, numberOfEdges());
		
		undo();
		assertEquals(1, numberOfEdges());
	}
	
	@Test
	public void testMultipleEdgesSameTypeCanNotExist()
	{		
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		addEdge(aAssociationEdge, new Point(10, 10), new Point(45, 48));
		addEdge(aDependencyEdge, new Point(10, 10), new Point(45, 48)); 
		addEdge(aGeneralizationEdge, new Point(10, 10), new Point(45, 48));
		assertEquals(4, numberOfEdges());
		
		assertFalse(aBuilder.canAdd(aAggregationEdge, new Point(9, 11), new Point(46, 49)));
		assertFalse(aBuilder.canAdd(aAssociationEdge, new Point(9, 11), new Point(46, 49)));
		assertFalse(aBuilder.canAdd(aDependencyEdge, new Point(9, 11), new Point(46, 49))); 
		assertFalse(aBuilder.canAdd(aGeneralizationEdge, new Point(9, 11), new Point(46, 49)));
		assertEquals(4, numberOfEdges());

	}
	
	@Test
	public void testNewEdgesSameTypeDoNotReplaceOriginalEdges()
	{
		AggregationEdge aSecondAggregationEdge = new AggregationEdge();
		AssociationEdge aSecondAssociationEdge = new AssociationEdge();
		DependencyEdge aSecondDependencyEdge = new DependencyEdge();
		GeneralizationEdge aSecondGeneralizationEdge = new GeneralizationEdge();
		
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		addEdge(aAssociationEdge, new Point(10, 10), new Point(45, 48));
		addEdge(aDependencyEdge, new Point(10, 10), new Point(45, 48)); 
		addEdge(aGeneralizationEdge, new Point(10, 10), new Point(45, 48));
		assertEquals(4, numberOfEdges());
		
		// new edges should not replace the current edges in the diagram
		assertFalse(aBuilder.canAdd(aSecondAggregationEdge, new Point(9, 111), new Point(46, 49)));
		assertFalse(aBuilder.canAdd(aSecondAssociationEdge, new Point(9, 111), new Point(46, 49)));
		assertFalse(aBuilder.canAdd(aSecondDependencyEdge, new Point(9, 111), new Point(46, 49)));
		assertFalse(aBuilder.canAdd(aSecondGeneralizationEdge, new Point(9, 111), new Point(46, 49)));
		assertEquals(4, numberOfEdges());
		assertFalse(aDiagram.contains(aSecondAggregationEdge));
		assertFalse(aDiagram.contains(aSecondAssociationEdge));
		assertFalse(aDiagram.contains(aSecondDependencyEdge));
		assertFalse(aDiagram.contains(aSecondGeneralizationEdge));
	}
	
	@Test 
	public void testDeleteNodeEdgeCombination1()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		Rectangle classNodeBounds = aClassNode1.view().getBounds();
		
		select(aClassNode1);
		deleteSelected();
		
		assertEquals(1, numberOfRootNodes());
		assertEquals(0, numberOfEdges());

		undo();
		assertEquals(1, numberOfEdges());
		assertEquals(2, numberOfRootNodes());
		assertEquals(classNodeBounds, getRootNode(1).view().getBounds());
	}
	
	@Test 
	public void testDeleteNodeEdgeCombination2()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(44, 44));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		Rectangle classNodeBounds = aClassNode1.view().getBounds();
		Rectangle interfaceNodeBounds = aInterfaceNode.view().getBounds();

		selectAll();
		deleteSelected();
		assertEquals(0, numberOfRootNodes());
		assertEquals(0, numberOfEdges());
		
		undo();
		assertEquals(1, numberOfEdges());
		assertEquals(2, numberOfRootNodes());
		for(Node node: aDiagram.rootNodes())
		{
			if(node instanceof ClassNode)
			{
				assertEquals(classNodeBounds,node.view().getBounds());
			}
			else
			{
				assertEquals(interfaceNodeBounds,node.view().getBounds());
			}
		}
	}
	
	@Test
	public void testDeleteNodeInsidePackageNode()
	{
		addNode(aPackageNode, new Point(5, 5));
		addNode(aClassNode1, new Point(6, 8));
		addNode(aClassNode2, new Point(11, 12));
		
		select(aClassNode2);
		deleteSelected();

		assertEquals(1, aPackageNode.getChildren().size());
		undo();
		assertEquals(2, aPackageNode.getChildren().size());
	}

	@Test
	public void testDeleteNodeInsideNestedPackageNode()
	{
		PackageNode innerNode = new PackageNode();
		addNode(aPackageNode, new Point(5, 5));
		addNode(innerNode, new Point(10, 10));
		addNode(aClassNode1, new Point(10, 13));
		addNode(aClassNode2, new Point(11, 12));

		select(aClassNode2);
		deleteSelected();
		assertEquals(1, aPackageNode.getChildren().size());
		assertEquals(1, innerNode.getChildren().size());
		undo();
		assertEquals(1, aPackageNode.getChildren().size());
		assertEquals(2, innerNode.getChildren().size());
	}
	
	@Test
	public void testCopyPasteSingleNode()
	{
		addNode(aClassNode1, new Point(5, 5));
		
		select(aClassNode1);
		copy();
		paste();
		
		assertEquals(2, numberOfRootNodes());
		assertEquals(new Point(0, 0), getRootNode(1).position());
	}
	
	@Test
	public void testCutPasteSingleNode()
	{
		addNode(aClassNode1, new Point(5, 5));
		select(aClassNode1);
		cut();
		assertEquals(0, numberOfRootNodes());
		paste();
		assertEquals(1, numberOfRootNodes());
		assertEquals(new Point(0, 0), getRootNode(0).position());
	}
	
	@Test
	public void testCopyPasteCombinationNodeAndEdge()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(45, 45));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		
		selectAll();
		copy();
		paste();
		assertEquals(4, numberOfRootNodes());
		assertEquals(2, numberOfEdges());
		
		undo();
		assertEquals(2, numberOfRootNodes());
		assertEquals(1, numberOfEdges());
	}
	
	@Test
	public void testCutPasteCombinationNodeAndEdge()
	{
		addNode(aClassNode1, new Point(5, 5));
		addNode(aInterfaceNode, new Point(45, 45));
		addEdge(aAggregationEdge, new Point(10, 10), new Point(45, 48));
		
		selectAll();
		cut();
		paste();
		
		assertEquals(2, numberOfRootNodes());
		assertEquals(1, numberOfEdges());
		
		undo();
		assertEquals(0, numberOfRootNodes());
		assertEquals(0, numberOfEdges());
		
		undo();
		assertEquals(2, numberOfRootNodes());
		assertEquals(1, numberOfEdges());
	}
	
	@Test
	public void testAddNodesToPackageNode()
	{
		addNode(aPackageNode, new Point(20, 20));
		addNode(aClassNode1, new Point(25, 25));
		addNode(aClassNode2, new Point(30, 30));
		addNode(new PackageNode(), new Point(35, 45));
		
		assertEquals(1, numberOfRootNodes());
		assertEquals(3, aPackageNode.getChildren().size());
	}
	
	@Test
	public void testAddNodeToInnerPackageNode()
	{
		PackageNode innerNode = new PackageNode();
		addNode(aPackageNode, new Point(20, 20));
		addNode(innerNode, new Point(25, 25));
		addNode(aClassNode1, new Point(26, 29));
		addNode(aClassNode2, new Point(30, 31));
	
		assertEquals(1, numberOfRootNodes());
		assertEquals(1, aPackageNode.getChildren().size());
		assertEquals(2, innerNode.getChildren().size());
	}
	
	@Test
	public void testConnectInnerNodeWithEdges()
	{
		addNode(aPackageNode, new Point(20, 20));
		addNode(aClassNode1, new Point(25, 25));
		addNode(aClassNode2, new Point(30, 30));
		moveNode(aClassNode2, 30, 30);
		addEdge(aDependencyEdge, new Point(26, 26), new Point(65, 31));
	
		assertEquals(1, numberOfRootNodes());
		assertEquals(1, numberOfEdges());
	}
	
	@Test
	public void testCutNodesAndEdgesInsidePackageNode()
	{
		addNode(aPackageNode, new Point(20, 20));
		addNode(aClassNode1, new Point(25, 25));
		addNode(aClassNode2, new Point(30, 30));
		moveNode(aClassNode2, 100, 0);
		addEdge(aDependencyEdge, new Point(26, 26), new Point(131, 31));
		
		selectAll();
		cut(); 
		assertEquals(0, numberOfRootNodes());
		assertEquals(0, numberOfEdges());
		
		paste();
		assertEquals(1, numberOfRootNodes());
		assertEquals(1, numberOfEdges());
	}
	
	@Test
	public void testCopyNodesAndEdgesInsidePackageNode()
	{
		addNode(aPackageNode, new Point(20, 20));
		addNode(aClassNode1, new Point(25, 25));
		addNode(aClassNode2, new Point(130, 30));
		addEdge(aDependencyEdge, new Point(26, 26), new Point(131, 31));
		
		selectAll();
		copy();
		paste();
		assertEquals(2, numberOfRootNodes());
		assertEquals(2, numberOfEdges());
	}	
}
