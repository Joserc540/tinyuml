package org.tinyuml.ui;

import org.tinyuml.draw.AbstractCompositeNode;
import org.tinyuml.draw.Connection;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.ComponentElement;
import org.tinyuml.umldraw.structure.PackageElement;
import org.tinyuml.umldraw.structure.StructureDiagram;


public class ElementCounter {

  private int packages;
  private int classes;
  private int components;

  /**
   * Recorre el diagrama y cuenta los elementos por categoria.
   * @param diagram el diagrama a contar (puede ser null)
   */
  public void count(StructureDiagram diagram) {
    packages = 0;
    classes = 0;
    components = 0;
    if (diagram != null) {
      countChildren(diagram);
    }
  }

  /**
   * Recorrido recursivo: tambien cuenta elementos anidados (ej. dentro
   * de un Package). Las conexiones (asociaciones, herencias) no se cuentan.
   * @param parent nodo contenedor
   */
  private void countChildren(AbstractCompositeNode parent) {
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
      }
      if (element instanceof AbstractCompositeNode) {
        countChildren((AbstractCompositeNode) element);
      }
    }
  }

  public int getPackages() { return packages; }
  public int getClasses() { return classes; }
  public int getComponents() { return components; }
  public int getTotal() { return packages + classes + components; }

  /**
   * Texto con el formato pedido:
   * "Total Items : 06; Package:02; Class:02; Component: 02".
   * @return texto para la barra de estado
   */
  public String getSummary() {
    return String.format("Total Items : %02d; Package:%02d; Class:%02d; "
      + "Component: %02d", getTotal(), packages, classes, components);
  }
}