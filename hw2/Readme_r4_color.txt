
        Add Color to the Renderer


This renderer adds a List of Color objects to each Model object.
The list of colors is used to associate a color with each Vertex
of a LineSegment.

Recall that each LineSegment object in a Model contains two integer
indices into the Model's list of Vertex objects. This gives the
LineSegment two endpoints. In this renderer, each LineSegment object
will also contain two more integer indices, this time into the Model's
list of Color objects. This will give a color to each of the LineSegment's
two endpoints.

The color from each end of a line segment will be linearly interpolated
by the rasterizer to the rendered pixels of the line segment.

When we clip a line segment, the color from each end of the line segment
will be linearly interpolated by the clipper to the line segment's new
vertex.

https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/Color.html


Changes
=======

The updated files in the renderer are
   Model.java,
   LineSegment.java,
   Clip.java,
   RasterizeAntialias.java.
In addition, all the client programs are updated to use colors.

There is a new file in the scene package,
   ModelShading.java
which is a library of static methods that assign
colors to a model in various interesting ways.


Scene Tree Data Structure
=========================

Here is a sketch of what a Scene data structure looks
like for this renderer. Notice how a LineSegment object
refers to two List<Integer> objects. The first list holds
two indices into the Model's Vertex list, and the second
list holds two indices into the Model's Color list.

            Scene
           /     \
          /       \
    Camera        List<Position>
                       /  |  \
                      /   |   \
                          |
                       Position
                      /        \
                     /          \
                    /            \
               Vector             Model
               /  |  \       /---/  |  \---\
              /   |   \     /       |       \
             x    y   z    /        |        \
                          /         |         \
                List<Vertex>   List<Color>     List<Primitive>
                    /  |  \       /  |  \            /  |  \
                   /   |   \     /   |   \          /   |   \
                       |             |                  |
                    Vertex         Color             LineSegment
                    /  |  \        / | \            /           \
                   /   |   \      /  |  \          /             \
                  x    y    z    r   g   b    List<Integer>   List<Integer>


When you instantiate a LineSegment object, you can give the
LineSegment constructor four integer values.

   Primitive ls = new LineSegment(2, 3, 1, 0);

The first two integer parameters are indices into the Model's Vertex list.
The last two integer parameters are indices into the Model's Color list.

You do not have to give the LineSegment constructor exactly four integer
values. There are other, overloaded, ways to call the LineSegment constructor.
See the Javadocs page for the LineSegment class.



Pipeline
========

Our pipeline still has the same four stages.

       v_0 ... v_n     A Model's list of Vertex objects
          \   /
           \ /
            |
            | model coordinates (of v_0 ... v_n)
            |
        +-------+
        |       |
        |   P1  |    Model-to-camera transformation (of the vertices)
        |       |
        +-------+
            |
            | camera coordinates (of v_0 ... v_n)
            |
        +-------+
        |       |
        |   P2  |    Projection transformation (of the vertices)
        |       |
        +-------+
            |
            | image plane coordinates (of v_0 ... v_n)
            |
        +-------+
        |       |
        |   P3  |    Viewport transformation (of the vertices)
        |       |
        +-------+
            |
            | pixel-plane coordinates (of v_0 ... v_n)
            |
           / \
          /   \
         /     \
        |   P4  |    Rasterization, clipping & anti-aliasing (of each line segment)
         \     /
          \   /
           \ /
            |
            |  shaded pixels (for each clipped, anti-aliased line segment)
            |
           \|/
    FrameBuffer.ViewPort



Vertex, Color, and Allocating LineSegments
==========================================

Giving color to vertices forces us to think about how we model geometry
using Vertex, Color, and LineSegment objects. Below are several examples.

Suppose that we have two line segments that share an endpoint, labeled p1
in this picture.

       p0 +---------------+ p1
                           \
                            \
                             \
                              \
                               \
                                + p2

Consider the following situations.

Suppose we want the horizontal line segment to have color c0 and the
vertical line segment to have color c1, where c0 and c1 can be set and
changed independently of each other. Here is one way to use Vertex,
Color, and LineSegment objects to model this situation. Here, Vertex v0
represents point p0, Vertex v1 represents p1, and Vertex v2 represents p2.

  List<Vertex>      List<Color>      List<LineSegment>
    +------+         +------+        +---------------+
  0 |  v0  |       0 |  c0  |      0 | [0, 1] [0, 0] |
    +------+         +------+        +---------------+
  1 |  v1  |       1 |  c1  |      1 | [1, 2] [1, 1] |
    +------+         +------+        +---------------+
  2 |  v2  |
    +------+

Notice how, if we change the entries in the List<Color>, each of the
two line segments will change its color.

You could also model this situation with the following allocation of
Vertex, Color, and LineSgement objects. Here, point p1 is represented by
both Vertex v1 and Vertex v2 (so v1.equals(v2) is true). Also c0.equals(c1)
and c2.equals(c3) must also be true. This is the model that OpenGL requires,
because in OpenGL the Vertex list and the Color list must have the same
length. Notice how we need to change two colors in the color list if we
want to change the color of one of the line segments. Also notice that if
we want to move the point p1, then we must change both vertices v1 and v2.

  List<Vertex>      List<Color>      List<LineSegment>
    +------+         +------+        +---------------+
  0 |  v0  |       0 |  c0  |      0 | [0, 1] [0, 1] |
    +------+         +------+        +---------------+
  1 |  v1  |       1 |  c1  |      1 | [2, 3] [2, 3] |
    +------+         +------+        +---------------+
  2 |  v2  |       2 |  c2  |
    +------+         +------+
  3 |  v3  |       3 |  c3  |
    +------+         +------+


Suppose we want the point p0 to have Color c0, the point p1 to have Color c1,
and the point p2 to have color c2. Suppose that the line segment from p0 to p1
should be shaded from c0 to c1 and the line segment from p1 to p2 should be
shaded from c1 to c2. And suppose we want the colors c0, c1, and c2 to be set
and changed independently of each other. Here is one way to allocate Vertex,
Color, and LineSegment objects to model this. Notice how, if we change color
c1 to color c3, then the shading of both line segments gets changed.

  List<Vertex>      List<Color>      List<LineSegment>
    +------+         +------+        +---------------+
  0 |  v0  |       0 |  c0  |      0 | [0, 1] [0, 1] |
    +------+         +------+        +---------------+
  1 |  v1  |       1 |  c1  |      1 | [1, 2] [1, 2] |
    +------+         +------+        +---------------+
  2 |  v2  |       2 |  c2  |
    +------+         +------+


Suppose we want the horizontal line segment to have solid color c0 and
the vertical line segment to be shaded from c0 to c1, where c0 and c1
can be changed independently of each other. Here is one way to model
this (be sure to compare this with the first model above).

  List<Vertex>      List<Color>      List<LineSegment>
    +------+         +------+        +---------------+
  0 |  v0  |       0 |  c0  |      0 | [0, 1] [0, 0] |
    +------+         +------+        +---------------+
  1 |  v1  |       1 |  c1  |      1 | [1, 2] [0, 1] |
    +------+         +------+        +---------------+
  2 |  v2  |
    +------+

If we change color c0 to c2, then the horizontal line segment
changes its solid color and the vertical line segment changes
its shading.


Here is a more complex situation. Suppose we want the two line
segments to be able to move away from each other, but the color
at (what was) the common point p1 will always be the same in
each line segment.

  List<Vertex>      List<Color>      List<LineSegment>
    +------+         +------+        +---------------+
  0 |  v0  |       0 |  c0  |      0 | [0, 1] [0, 1] |
    +------+         +------+        +---------------+
  1 |  v1  |       1 |  c1  |      1 | [2, 3] [1, 2] |
    +------+         +------+        +---------------+
  2 |  v2  |       2 |  c2  |
    +------+         +------+
  3 |  v3  |
    +------+

Initially, v1.equals(v2) will be true, but when the two line segments
separate, v1 and v2 will no longer be equal. But the Color with index
1 is always shared by both line segments, so even if the two line
segments move apart, and even if Color c1 is changed, the two line
segments will always have the same color at what was their common
endpoint.

You should create small Java programs that implement each of
these situations.



Color Interpolation in the Rasterizer
=====================================

This picture represents a line segment projected into the camera's view
rectangle. Each end of the line segment has a Color associated to it.

          x = -1               x = +1
            |                    |
        ----+--------------------+---- y = +1
            |                    |
            |         v1,c1      |
            |          /         |
            |         /          |
            |        /           |
            |       /            |
            |      /             |
            |     /              |
            |  v0,c0             |
            |                    |
            |                    |
        ----+--------------------+---- y = -1
            |                    |

We want to describe how the rasterizer uses the colors from the two
endpoints of the line segment to shade the pixels that represent the
line segment.

If c0 and c1 are the same Color, then the rasterizer should just give
that color to every pixel in the line segment. So the interesting case
is when the two colors are not the same. In that case, we want the
rasterizer to shift the color from co to c1 as the rasterizer moves
across the line segment.

We have two ways of writing an equation for the line segment. The line
segment can be described by the two-point equation for a line,

   y(x) = y0 + (y1-y0)/(x1-x0)*(x - x0)  with  x0 <= x <= x1,

or by the vector parametric lerp equation,

   p(t) = (1-t)*v0 + t*v1  with  0 <= t <= 1.

We can use either equation to shade pixels on the line segment.

Let (r0, g0, b0) be the Color c0 at v0 and
let (r1, g1, b1) be the Color c1 at v1.

Given a value for x with x0 <= x <= x1, then the following three
linear equations linearly interpolate the three components of a
color to the pixel at (x, y(x)).

   r(x) = r0 + (r1-r0)/(x1-x0)*(x - x0)
   g(x) = g0 + (g1-g0)/(x1-x0)*(x - x0)
   b(x) = b0 + (b1-b0)/(x1-x0)*(x - x0)

Given a value for t with 0 <= t <= 1, then the following three
lerp equations linearly interpolate the three components of a
color to the pixel at (t, p(t)).

   r(t) = (1-t)*r0 + t*r1
   g(t) = (1-t)*g0 + t*g1
   b(t) = (1-t)*b0 + t*b1

Notice that the lerp versions of the equations are easier to
read and understand. But the rasterizer is written around the
two-point equations, so it uses those. We will see below that
the clipping algorithm uses the lerp equations.



Color Interpolation in the Clipper
===================================

Suppose we have a line segment that extends out of the camera's view
rectangle. Here we have a line segment with vertex v1, with color c1,
on the "right" side of the clipping line x = 1 and vertex v0, with
color c0, on the "wrong" side of the clipping line.

                        x=1
                         |
                  v1,c1  |
                     \   |
                      \  |
                       \ |
                        \|
                         \
                         |\
                         | \
                         |  \
                         |   \
                         |  v0,c0

Vertex v0 needs to be clipped off and replaced with a new vertex at the
line x=1. We also need to give the new vertex a new color. Since color
along the line segment will be linearly interpolated by the rasterizer,
the clipping stage should give to the new vertex the same color that the
rasterizer would interpolate to the line segment's pixel at x=1.

                        x=1
                         |
                  v1,c1  |
                     \   |
                      \  |
                       \ |
                        \|
                         \ v2 = (1-t0) * v0 + t0 * v1,
                         | c2 = (1-t0) * c0 + t0 * c1
                         |
                         |
                         |
                         |

Once the clipping algorithm has solved for the value t0 when the x-coordinate
of p(t) = 1, then the clipping algorithm can use the following three lerp
equations to calculate the new color, c(t0), for the new vertex p(t0).

   r(t0) = (1-t0) * r0 + t0 * r1
   g(t0) = (1-t0) * g0 + t0 * g1
   b(t0) = (1-t0) * b0 + t0 * b1
