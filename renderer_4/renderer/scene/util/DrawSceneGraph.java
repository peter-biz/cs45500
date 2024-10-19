/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.util;

import renderer.scene.*;
import renderer.scene.primitives.Primitive;

import java.io.File;
import java.io.PrintWriter;
import java.lang.Runtime;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

/**
   This program converts a {@link Scene} data structure into
   a DOT description of the scene. The DOT description is
   written to a file and that file is processed by the
   dot.exe program to produce a png file graphical image
   of the scene data structure.
<p>
   A <code>scene.png</code> image file is created from
   a <code>scene.dot</code> file with the following
   command-line.
   <pre>{@code
   > dot.exe -Tpng -O scene.dot
   }</pre>
<p>
   See
<br><a href="https://www.graphviz.org/Documentation.php" target="_top">
             https://www.graphviz.org/Documentation.php</a>
<p>
   This class has four static boolean variables that can be used
   to control the complexity of the scene graph drawing.
*/
public class DrawSceneGraph
{
   // These four variables allow us to turn on and off the
   // the drawing of many details in the scene graph.
   // This helps control the complexity of the scene graph.
   /**
      Control the details shown in a {@link Camera} node.
   */
   public static boolean drawCameraDetails = true;
   /**
      Control the display of {@link Vector} nodes.
   */
   public static boolean drawVector = true;
   /**
      Control the details shown in a {@link Vector} node.
   */
   public static boolean drawVectorDetails = true;
   /**
      Control the details shown below a {@link Model} node.
   */
   public static boolean drawVertexList = false;

   // These three variables are used to detect those
   // nodes in the DAG that have multiple parents.
   private static List<Object> visitedNodes = null;
   private static List<String> visitedNodeNames = null;
   private static int nodeNumber;

   /**
      This method converts a {@link Scene} data structure into a dot
      language description. The dot code for the scene is written into
      a dot file. Then the dot.exe program is called to convert the dot
      file into a png image of the scene data structure.

      @param scene     {@link Scene} that needs to be converted to a dot description
      @param fileName  base name for the dot and png files
   */
   public static void draw(final Scene scene, final String fileName)
   {
      // Convert the scene data structure into a dot language description.
      final String dotDescription = scene2dot(scene);

      // Write the dot language description stored in dotDescription
      // into a dot file. Then use the dot.exe program to convert the
      // dot file into a png file.
      try
      {
         // Create the (empty) dot file.
         final String baseName = fileName;
         java.io.PrintWriter out = new PrintWriter(
                                      new File(baseName + ".dot"));

         // Write the dot commands into the dot file.
         out.print( dotDescription );
         out.close();

         // Create a command-line for running the dot.exe program.
         final String dotExecutable = "C:\\Graphviz\\bin\\dot.exe";
         final String[] cmd = {dotExecutable,
                               "-Tpng",
                               baseName + ".dot",
                               "-o",
                               baseName + ".png"};

         final File dot = new File(dotExecutable);
         if(dot.exists() && !dot.isDirectory())
         {
            // Execute the command-line to create the png file.
            Runtime.getRuntime().exec(cmd);
         }
         else
         {
            System.out.println("\nPlease consider installing GraphViz:");
            System.out.println("  https://graphviz.org/download/");
            System.out.println("or upload the contents of " + baseName + ".dot to Graphviz Visual Editor:");
            System.out.println("  http://magjac.com/graphviz-visual-editor/");
         }
      }
      catch (Exception e)
      {
         System.out.println( e );
      }
   }


   /**
      This method generates a dot language description of the
      DAG rooted at a {@link Scene} node.
      <p>
      This method generates the dot code for the forest of top-level
      positions just below the scene node. Each position node just
      below the scene node is the root of a DAG. This method calls
      the <code>position2dot()</code> method to traverse the DAG of
      each top-level position.

      @param  scene  {@link Scene} that needs to be converted to a dot description
      @return a {@link String} containing the dot language description of the scene
   */
   public static String scene2dot(final Scene scene)
   {
      // https://stackoverflow.com/questions/48266439/getting-graphviz-to-eliminate-identical-duplicate-edges
      String result = "strict digraph {\n";

      // https://graphviz.org/docs/attrs/ordering/
      result += "graph [ordering=\"out\"];\n";

      // https://stackoverflow.com/questions/10879115/graphviz-change-font-for-the-whole-graph
      // https://graphviz.org/docs/attrs/fontname/
      result += "graph [fontname=\"helvetica\"];\n";
      result += "node  [fontname=\"helvetica\"];\n";
      result += "edge  [fontname=\"helvetica\"];\n";

      // Scene node.
      result += "scene [label=\"Scene: " + scene.name + "\"];\n";

      // Camera and List<Position> nodes under the Scene node.

      // Camera node and label.
      final String cameraNodeName = "Camera";
      result += cameraNodeName + " ";
      if (drawCameraDetails)
      {
         result += "[label=\"" + scene.camera + "\"];\n";
      }
      else
      {
         result += "[label=\"Camera\"];\n";
      }
      // Camera edge.
      result += "scene -> " + cameraNodeName + ";\n";

      // List<Position> node and label.
      final String pListNodeName = "positionList";
      result += pListNodeName + " ";
      result += "[label=\"List<Position>\"];\n";
      // List<Position> edge.
      result += "scene -> " + pListNodeName + ";\n";

      visitedNodes = new ArrayList<>();
      visitedNodeNames = new ArrayList<>();
      nodeNumber = -1;

      // For each top-level Position, create a node with two edges,
      // its model and its translation vector.
      for (int i = 0; i < scene.positionList.size(); ++i)
      {
         // Position node name.
         ++nodeNumber;
         final String pNodeName = "_p" + nodeNumber;

         // Position node and label.
         final Position positionReference = scene.getPosition(i);
         result += pNodeName + " ";
         result += "[label=\"Position: " + positionReference.name + "\"];\n";

         // Position edge.
         result += pListNodeName + " -> " + pNodeName + ";\n";

         // This Position's translation vector and model.
         result += position2dot(positionReference, pNodeName);
      }

      result += "}\n";

      return result;
   }


   /**
      This method generates a dot language description of the
      DAG rooted at a {@link Position} node.
      <p>
      {@code positionName} is the id that has been assigned to
      the dot node representing the given {@link Position} node.
      <p>
      Every {@link Position} node has attached to it a translation {@link Vector}
      and a {@link Model}.

      @param position      {@link Position} that needs to be converted to a dot description
      @param positionName  the {@link String} name that has been assigned to {@code position}
      @return a {@link String} containing the dot language description of {@code position}
   */
   public static String position2dot(final Position position,
                                     final String positionName)
   {
      String result = "";

      if (drawVector || drawVectorDetails)
      {
         // Vector node name.
         final String tNodeName = positionName + "_Matrix";

         // Vector node and label.
         result += tNodeName + " ";

         if (drawVectorDetails)
         {
            result += "[label=\"Vector:\n" + position.getTranslation() + "\"];\n";
         }
         else
         {
            result += "[label=\"Vector\"];\n";
         }

         // Vector edge.
         result += positionName + " -> " + tNodeName + ";\n";
       //result += positionName + " -> " + tNodeName + " [constraint=false];\n";
      }

      // The position's model.
      final Model modelReference = position.getModel();

      // Check if the Model is being reused.
      final boolean modelVisited = visitedNodes.contains(modelReference);

      if ( ! modelVisited )
      {
         // Model node name.
         ++nodeNumber;
         final String mNodeName = "_m" + nodeNumber;
         // Mark this model as visited.
         visitedNodes.add(modelReference);
         visitedNodeNames.add(mNodeName);

         // Model node and label.
         result += mNodeName + " ";
         result += "[label=\"Model: " + modelReference.name + "\"];\n";

         // Model edge.
         result += positionName + " -> " + mNodeName + ";\n";

         // The model's vertex, color, and primitive lists.
         result += model2dot(modelReference, mNodeName);
      }
      else // this Model has already been visited
      {
         final int index = visitedNodes.indexOf(modelReference);
         // Model node name.
         final String mNodeName = visitedNodeNames.get(index);
         // Model edge (to a previously visited Model node).
         result += positionName + " -> " + mNodeName + ";\n";
      }

      return result;
   }


   /**
      This method generates a dot language description of the
      tree rooted at a {@link Model} node.
      <p>
      {@code nodeName} is the id that has been assigned to the
      dot node representing the given {@link Model} node.
      <p>
      Every {@link Model} node has attached to it a {@link List}
      of vertices, a {@link List} of colors, and a {@link List}
      of primitives.

      @param model  {@link Model} that needs to be converted to a dot description
      @param nodeName  the {@link String} name that has been assigned to {@code model}
      @return a {@link String} containing the dot language description of the model
   */
   public static String model2dot(final Model model,
                                  final String nodeName)
   {
      String result = "";

      if (drawVertexList)
      {
         // List<Vertex> node and label.
         final String vertexListNodeName = nodeName + "_vertexList";
         result += vertexListNodeName + " ";
         result += "[label=\"List<Vertex>\"];\n";
         // List<Vertex> edge.
         result += nodeName + " -> " + vertexListNodeName + ";\n";
         // List<Vertex> children.
         int vertexCounter = 0;
         String lastVertexNodeName = vertexListNodeName;
         for (Vertex v : model.vertexList)
         {
            // Vertex node name.
            final String vertexNodeName = nodeName + "_v" + vertexCounter;

            // Vertex node and label.
            result += vertexNodeName + " ";
            result += "[label=\"Vertex: " + v + "\"];\n";

            // Vertex edge.
            result += lastVertexNodeName + " -> " + vertexNodeName + ";\n";

            lastVertexNodeName = vertexNodeName;
            ++vertexCounter;
         }


         // List<Color> node and label.
         final String colorListNodeName = nodeName + "_colorList";
         result += colorListNodeName + " ";
         result += "[label=\"List<Color>\"];\n";
         // List<Color> edge.
         result += nodeName + " -> " + colorListNodeName + ";\n";
         // List<Color> children.
         int colorCounter = 0;
         String lastColorNodeName = colorListNodeName;
         for (Color c : model.colorList)
         {
            // Color node name.
            final String colorNodeName = nodeName + "_c" + colorCounter;

            // Color node and label.
            result += colorNodeName + " ";
            result += "[label=\"" + c + "\"];\n";

            // Color edge.
            result += lastColorNodeName + " -> " + colorNodeName + ";\n";

            lastColorNodeName = colorNodeName;
            ++colorCounter;
         }


         // List<Primitive> node and label.
         final String primitiveListNodeName = nodeName + "_primitiveList";
         result += primitiveListNodeName + " ";
         result += "[label=\"List<Primitive>\"];\n";
         // List<Primitive> edge.
         result += nodeName + " -> " + primitiveListNodeName + ";\n";
         // List<Primitive> children.
         int primitiveCounter = 0;
         String lastPrimitiveNodeName = primitiveListNodeName;
         for (Primitive p : model.primitiveList)
         {
            // Primitive node name.
            final String primitiveNodeName = nodeName + "_p" + primitiveCounter;

            // Primitive node and label.
            result += primitiveNodeName + " ";
            result += "[label=\"" + p + "\"];\n";

            // Primitive edge.
            result += lastPrimitiveNodeName + " -> " + primitiveNodeName + ";\n";

            lastPrimitiveNodeName = primitiveNodeName;
            ++primitiveCounter;
         }
      }

      return result;
   }


   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private DrawSceneGraph() {
      throw new AssertionError();
   }
}
