import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.cellstructure.ShowVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertEquals;

/**
 * A class to tests the functionality of the cache.
 */
public class CacheTest {
  private Cache c;
  private HashMap<Coord, Cache> h;

  @Before
  public void setup() {
    h = new HashMap<>();
    c = new Cache(new CellDouble(1), new Coord(1, 1), new HashSet<>(), h);
    h.put(new Coord(1, 1), c);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorAllNull() {
    c = new Cache(null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullValue() {
    c = new Cache(null, new Coord(1, 1), new HashSet<>(), new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullCoord() {
    c = new Cache(new CellDouble(3), null, new HashSet<>(), new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullDependency() {
    new Cache(new CellDouble(3), new Coord(1, 1), null, new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullMap() {
    new Cache(new CellDouble(3), new Coord(1, 1), new HashSet<>(), null);
  }

  @Test
  public void accessCacheWithBasicValuePresentString() {
    assertEquals("1.000000",
            h.get(new Coord(1, 1)).accessCache().accept(new ShowVisitor()));
  }

  @Test
  public void accessCacheWithBasicValuePresent() {
    assertEquals(new CellDouble(1),
            h.get(new Coord(1, 1)).accessCache());
  }

  @Test
  public void accessCacheWithNewlyAddedValue() {
    Cache n = new Cache(new CellDouble(3), new Coord(1, 2), new HashSet<>(), h);
    h.put(new Coord(1, 2), n);
    assertEquals(new CellDouble(3),
            h.get(new Coord(1, 2)).accessCache());
  }


  @Test
  public void accessCacheWithNewlyAddedString() {
    Cache n = new Cache(new CellString("blue"), new Coord(1, 2), new HashSet<>(), h);
    h.put(new Coord(1, 2), n);
    assertEquals(new CellString("blue"),
            h.get(new Coord(1, 2)).accessCache());
  }

  @Test
  public void accessDependency() {
    HashSet<Coord> dep = new HashSet<>();
    dep.add(new Coord(1, 4));
    Cache n = new Cache(new CellString("blue"), new Coord(1, 2), dep, h);
    h.put(new Coord(1, 2), n);
    assertEquals(dep,
            h.get(new Coord(1, 2)).accessDependency());
  }

  @Test
  public void accessDependencyWithNone() {
    Cache n = new Cache(new CellString("blue"), new Coord(1, 2), new HashSet<>(), h);
    h.put(new Coord(1, 2), n);
    assertEquals(new HashSet<Coord>(),
            h.get(new Coord(1, 2)).accessDependency());
  }

  @Test
  public void accessDependencyMultiple() {
    HashSet<Coord> dep = new HashSet<>();
    dep.add(new Coord(1, 4));
    dep.add(new Coord(1, 5));
    dep.add(new Coord(4, 7));
    Cache n = new Cache(new CellString("testing"), new Coord(1, 2), dep, h);
    h.put(new Coord(1, 2), n);
    assertEquals(dep,
            h.get(new Coord(1, 2)).accessDependency());
  }
}