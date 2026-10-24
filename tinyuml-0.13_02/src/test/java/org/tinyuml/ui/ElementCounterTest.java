package org.tinyuml.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.structure.Association;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.ComponentElement;
import org.tinyuml.umldraw.structure.Dependency;
import org.tinyuml.umldraw.structure.PackageElement;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * Pruebas del RF-001 (conteo de elementos por categoria).
 */
public class ElementCounterTest {

  private ElementCounter counter;
  private StructureDiagram diagram;

  @Before
  public void setUp() {
    counter = new ElementCounter();
    diagram = new StructureDiagram(null);
  }

  private static PackageElement newPackage() {
    return (PackageElement) PackageElement.getPrototype().clone();
  }

  private static ClassElement newClass() {
    return (ClassElement) ClassElement.getPrototype().clone();
  }

  private static ComponentElement newComponent() {
    return (ComponentElement) ComponentElement.getPrototype().clone();
  }

  private static NoteElement newNote() {
    return (NoteElement) NoteElement.getPrototype().clone();
  }

  private void assertCounts(int packages, int classes, int components,
                            int notes) {
    assertEquals(packages, counter.getPackages());
    assertEquals(classes, counter.getClasses());
    assertEquals(components, counter.getComponents());
    assertEquals(notes, counter.getNotes());
    assertEquals(packages + classes + components + notes, counter.getTotal());
  }

  @Test
  public void nullDiagramCountsZero() {
    counter.count(null);
    assertCounts(0, 0, 0, 0);
    assertEquals("Total Items: 00 | Package: 00 | Class: 00 | "
      + "Component: 00 | Note: 00", counter.getSummary());
  }

  @Test
  public void newCounterStartsAtZero() {
    assertCounts(0, 0, 0, 0);
  }

  @Test
  public void emptyDiagramCountsZero() {
    counter.count(diagram);
    assertCounts(0, 0, 0, 0);
    assertEquals("Total Items: 00 | Package: 00 | Class: 00 | "
      + "Component: 00 | Note: 00", counter.getSummary());
  }

  @Test
  public void twoOfEachCategory() {
    for (int i = 0; i < 2; i++) {
      diagram.addChild(newPackage());
      diagram.addChild(newClass());
      diagram.addChild(newComponent());
      diagram.addChild(newNote());
    }
    counter.count(diagram);
    assertCounts(2, 2, 2, 2);
    assertEquals("Total Items: 08 | Package: 02 | Class: 02 | "
      + "Component: 02 | Note: 02", counter.getSummary());
  }

  @Test
  public void summaryMatchesRequirementExample() {
    for (int i = 0; i < 2; i++) {
      diagram.addChild(newPackage());
      diagram.addChild(newClass());
      diagram.addChild(newComponent());
    }
    counter.count(diagram);
    assertEquals("Total Items: 06 | Package: 02 | Class: 02 | "
      + "Component: 02 | Note: 00", counter.getSummary());
  }

  @Test
  public void nestedClassInsidePackageIsCounted() {
    PackageElement pkg = newPackage();
    pkg.addChild(newClass());
    diagram.addChild(pkg);
    counter.count(diagram);
    assertCounts(1, 1, 0, 0);
  }

  @Test
  public void deeplyNestedElementsAreCounted() {
    PackageElement outer = newPackage();
    PackageElement inner = newPackage();
    inner.addChild(newClass());
    inner.addChild(newNote());
    outer.addChild(inner);
    outer.addChild(newComponent());
    diagram.addChild(outer);
    counter.count(diagram);
    assertCounts(2, 1, 1, 1);
  }

  @Test
  public void connectionsAreNotCounted() {
    diagram.addChild(newClass());
    diagram.addChild(newClass());
    diagram.addChild((DiagramElement) Association.getPrototype().clone());
    diagram.addChild((DiagramElement) Dependency.getPrototype().clone());
    counter.count(diagram);
    assertCounts(0, 2, 0, 0);
  }

  @Test
  public void recountResetsPreviousValues() {
    diagram.addChild(newClass());
    counter.count(diagram);
    assertCounts(0, 1, 0, 0);
    counter.count(new StructureDiagram(null));
    assertCounts(0, 0, 0, 0);
    counter.count(null);
    assertCounts(0, 0, 0, 0);
  }

  @Test
  public void countReflectsRemovedElement() {
    ClassElement removed = newClass();
    diagram.addChild(newClass());
    diagram.addChild(removed);
    counter.count(diagram);
    assertCounts(0, 2, 0, 0);
    diagram.removeChild(removed);
    counter.count(diagram);
    assertCounts(0, 1, 0, 0);
  }
}
