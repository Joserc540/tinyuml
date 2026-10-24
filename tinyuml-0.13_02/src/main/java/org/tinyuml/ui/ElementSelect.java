package org.tinyuml.ui;

import java.util.List;

import org.tinyuml.draw.Connection;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.model.UmlModelElement;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.shared.UmlDiagramElement;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.ComponentElement;
import org.tinyuml.umldraw.structure.PackageElement;


public class ElementSelect {

  static final String NO_SELECTION = "Sin selecci\u00f3n";
  private static final int MAX_NAME_LENGTH = 30;

  /**
   * Describe la seleccion actual.
   * @param selected elementos seleccionados (puede ser null o vacio)
   * @return "Categoria: nombre", o "Sin selección" si no hay seleccion
   */
  public String describe(List<DiagramElement> selected) {
    if (selected == null || selected.isEmpty()) {
      return NO_SELECTION;
    }
    DiagramElement first = selected.get(0);
    String text = "Selecci\u00f3n: " + categoryOf(first) + " - " + nameOf(first); 
    if (selected.size() > 1) {
      text += " (+" + (selected.size() - 1) + " m\u00e1s)";
    }
    return text;
  }

  private String categoryOf(DiagramElement element) {
    if (element instanceof PackageElement) {
      return "Package";
    } else if (element instanceof ClassElement) {
      return "Class";
    } else if (element instanceof ComponentElement) {
      return "Component";
    } else if (element instanceof NoteElement) {
      return "Note";
    } else if (element instanceof Connection) {
      return "Relation";
    }
    return "Element";
  }

  private String nameOf(DiagramElement element) {
    String name = null;
    if (element instanceof NoteElement) {
      name = ((NoteElement) element).getLabelText();
    } else if (element instanceof UmlDiagramElement) {
      UmlModelElement model = ((UmlDiagramElement) element).getModelElement();
      name = model == null ? null : model.getName();
    }
    if (name == null || name.trim().length() == 0) {
      return "(sin nombre)";
    }
    name = name.replaceAll("\\s+", " ").trim();
    if (name.length() > MAX_NAME_LENGTH) {
      name = name.substring(0, MAX_NAME_LENGTH) + "...";
    }
    return name;
  }
}
