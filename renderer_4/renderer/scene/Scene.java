/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene;

import java.util.List;
import java.util.ArrayList;

/**
   A {@code Scene} data structure is a {@link List} of {@link Position}
   data structures and a {@link Camera} data structure.
<p>
   Each {@link Position} object represents a {@link Model} object in a
   distinct position in three-dimensional camera space.
<p>
   Each {@link Model} object represents a distinct geometric object
   in the scene.
<p>
   The {@link Camera} object determines a "view volume", which
   determines how much of the scene is actually visible (to the
   camera) and gets rendered into the framebuffer.
*/
public final class Scene
{
   public final String name;
   public final Camera camera;
   public final List<Position> positionList;

   public boolean debug;

   /**
      Construct a {@code Scene} with a default perspective
      {@link Camera} object.
   */
   public Scene()
   {
      this("",                 // name
           Camera.projPerspective(),
           new ArrayList<>(),  // positionList
           false);             // debug
   }


   /**
      Construct a {@code Scene} with a default perspective
      {@link Camera} object and the given {@link String} name.

      @param name  {@link String} name for this {@code Scene}
      @throws NullPointerException if {@code name} is {@code null}
   */
   public Scene(final String name)
   {
      this(name,
           Camera.projPerspective(),
           new ArrayList<>(), // positionList
           false);            // debug
   }


   /**
      Construct a {@code Scene} with the given {@link Camera} object.

      @param camera  {@link Camera} object for this {@code Scene}
      @throws NullPointerException if {@code camera} is {@code null}
   */
   public Scene(final Camera camera)
   {
      this("",                // name
           camera,
           new ArrayList<>(), // positionList
           false);            // debug
   }


   /**
      Construct a {@code Scene} with the given {@link Camera} object
      and the given {@link String} name.

      @param name    {@link String} name for this {@code Scene}
      @param camera  {@link Camera} object for this {@code Scene}
      @throws NullPointerException if {@code name} is {@code null}
      @throws NullPointerException if {@code camera} is {@code null}
   */
   public Scene(final String name,
                final Camera camera)
   {
      this(name,
           camera,
           new ArrayList<>(), // positionList
           false);            // debug
   }


   /**
      Construct a {@code Scene} object with all the given data.

      @param name          {@link String} name for this {@code Scene}
      @param camera        {@link Camera} object for this {@code Scene}
      @param positionList  {@link List} of {@link Position} objects
      @param debug         debug status for this {@code Scene}
      @throws NullPointerException if {@code camera} is {@code null}
      @throws NullPointerException if {@code positionList} is {@code null}
      @throws NullPointerException if {@code name} is {@code null}
   */
   public Scene(final String name,
                final Camera camera,
                final List<Position> positionList,
                final boolean debug)
   {
      if (null == name)
         throw new NullPointerException("name must not be null");
      if (null == camera)
         throw new NullPointerException("camera must not be null");
      if (null == positionList)
         throw new NullPointerException("positionList must not be null");

      this.name = name;
      this.camera = camera;
      this.positionList = positionList;
      this.debug = debug;
   }


   /**
      Create a new {@code Scene} that is essentially the same as this
      {@code Scene} but holding a refernece to the given {@link Camera}
      object.

      @param camera  {@link Camera} object for the new {@code Scene}
      @return a new {@code Scene} object holding the given {@link Camera} object
      @throws NullPointerException if {@code camera} is {@code null}
   */
   public Scene changeCamera(final Camera camera)
   {
      return new Scene(this.name,
                       camera,
                       this.positionList,
                       this.debug);
   }


   /**
      Get a reference to the {@link Position} object at the given index in this {@code Scene}'s
      {@link List} of {@link Position}s.

      @param index  index of the {@link Position} to return
      @return {@link Position} at the specified index in the {@link List} of {@link Position}s
      @throws IndexOutOfBoundsException if the index is out of range
              {@code (index < 0 || index >= size())}
   */
   public Position getPosition(final int index)
   {
      return positionList.get(index);
   }


   /**
      Set a reference to the given {@link Position} object at the given index in this {@code Scene}'s
      {@link List} of {@link Position}s.

      @param index     index of the {@link Position} to set
      @param position  {@link Position} object to place at the specified index in the {@link List} of {@link Position}s
      @throws NullPointerException if {@link Position} is {@code null}
      @throws IndexOutOfBoundsException if the index is out of range
              {@code (index < 0 || index >= size())}
   */
   public void setPosition(final int index, final Position position)
   {
      if (null == position)
         throw new NullPointerException("Position must not be null");

      positionList.set(index, position);
   }


   /**
      Add a {@link Position} (or Positions) to this {@code Scene}.

      @param pArray  array of {@link Position}s to add to this {@code Scene}
      @throws NullPointerException if any {@link Position} is {@code null}
   */
   public void addPosition(final Position... pArray)
   {
      for (final Position position : pArray)
      {
         if (null == position)
            throw new NullPointerException("Position must not be null");

         positionList.add(position);
      }
   }


   /**
      Get a reference to the first {@link Model} object with the given name from
      this {@code Scene}'s {@link List} of {@link Position}s.

      @param name  {@link String} name of the {@link Model} to search for
      @return a {@link Model} with the give name from the {@link List} of {@link Position}s
   */
   public Model getModelByName(final String name)
   {
      Model result = null;
      for (final Position position : positionList)
      {
         if ( name.equals(position.getModel().name) )
         {
            result = position.getModel();
            break;
         }
      }
      return result;
   }


   /**
      Get a reference to the first {@link Position} object that holds a {@link Model}
      with the given name from this {@code Scene}'s {@link List} of {@link Position}s.

      @param name  {@link String} name of the {@link Model} to search for
      @return a {@link Model} with the give name from the {@link List} of {@link Position}s
   */
   public Position getPositionByModelName(final String name)
   {
      Position result = null;
      for (final Position position : positionList)
      {
         if ( name.equals(position.getModel().name) )
         {
            result = position;
            break;
         }
      }
      return result;
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Scene} object
   */
   @Override
   public String toString()
   {
      String result = "";
      result += "Scene: " + name + "\n";
      result += camera.toString() + "\n";
      result += "This Scene has " + positionList.size() + " positions\n";
      int i = 0;
      for (final Position p : positionList)
      {
         result += "Position " + (i++) + "\n";
         result += p.toString();
      }
      return result;
   }
}
