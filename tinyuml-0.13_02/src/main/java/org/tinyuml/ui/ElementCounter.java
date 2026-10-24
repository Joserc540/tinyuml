package org.tinyuml.ui;

import java.util.ArrayDeque;
import java.util.Deque;

import org.tinyuml.draw.AbstractCompositeNode;
import org.tinyuml.draw.Connection;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.ComponentElement;
import org.tinyuml.umldraw.structure.PackageElement;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * Implementa el RF-001: cuenta los elementos de un diagrama por categoria
 * (Package, Class, Component y Note) y arma el texto de la barra de estado.
 * Las relaciones entre elementos no se cuentan.
 */
public class ElementCounter {

  private int packages;
  private int classes;
  private int components;
  private int notes;

  /**
   * Recorre el diagrama y cuenta los elementos por categoria.
   * @param diagram el diagrama a contar (puede ser null)
   */
  public void count(StructureDiagram diagram) {
    packages = 0;
    classes = 0;
    components = 0;
    notes = 0;
    if (diagram != null) {
      countChildren(diagram);
    }
  }


  private void countChildren(AbstractCompositeNode root) {
    Deque<AbstractCompositeNode> pending =
      new ArrayDeque<AbstractCompositeNode>();
    pending.push(root);
    while (!pending.isEmpty()) {
      AbstractCompositeNode parent = pending.pop();
      for (DiagramElement element : parent.getChildren()) {
        if (element instanceof Connection) {
          continue;
        }
        if (element instanceof PackageElement) {
          packages++;
        } else if (element instanceof ClassElement) {
          classes++;
        } else if (element instanceof ComponentElement) {
          components++;
        } else if (element instanceof NoteElement) {
          notes++;
        }
        if (element instanceof AbstractCompositeNode) {
          pending.push((AbstractCompositeNode) element);
        }
      }
    }
  }

  public int getPackages() { return packages; }
  public int getClasses() { return classes; }
  public int getComponents() { return components; }
  public int getNotes() { return notes; }
  public int getTotal() { return packages + classes + components + notes; }

  /**
   * Texto con el formato pedido:
   * "Total Items: 06 | Package: 02 | Class: 02 | Component: 02 | Note: 00".
   * @return texto para la barra de estado
   */
  public String getSummary() {
    return String.format("Total Items: %02d | Package: %02d | Class: %02d | "
      + "Component: %02d | Note: %02d", getTotal(), packages, classes,
      components, notes);
  }
}