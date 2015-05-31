package cg.trab2;

import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.jtrace.geometry.Sphere;

public class ObjectEditor extends Panel {
	private static enum Field { X, Y, Z, R }
	
	private static final int TEXT_FIELD_COLUMNS = 6;

	private static final long serialVersionUID = 8930844466550309792L;
	
	private Sphere object;
	
	private Label[] labels;
	private TextField[] textFields;
	
	public ObjectEditor(Sphere object, String objectName) {
		this.object = object;
		
		labels = new Label[Field.values().length];
		textFields = new TextField[Field.values().length];
		
		for (Field f : Field.values()) {
			int i = f.ordinal();
			
			labels[i] = new Label(objectName + "." + f.toString());
			add(labels[i]);
			
			textFields[i] = new TextField(TEXT_FIELD_COLUMNS);
			textFields[i].addFocusListener(new ObjectFieldEventListener(f));
			add(textFields[i]);
		}
		
		update();
	}
	
	public void update() {
		for (Field f : Field.values()) {
			update(f);
		}
	}
	
	public void update(Field field) {
		int i = field.ordinal();
		try {
			double value = Double.parseDouble(textFields[i].getText());
			setValue(field, value);
		} catch (NumberFormatException e) {
			textFields[i].setText(Double.toString(getValue(field)));
		}
	}
	
	private void setValue(Field field, double value) {
		switch (field) {
		case X:
			object.getCenter().setX(value);
			break;
			
		case Y:
			object.getCenter().setY(value);
			break;
			
		case Z:
			object.getCenter().setZ(value);
			break;
			
		case R:
			object.setRadius((float) value);
			break;

		default:
			break;
		}
	}
	
	private double getValue(Field field) {
		switch (field) {
		case X:
			return object.getCenter().getX();
		
		case Y:
			return object.getCenter().getY();
		
		case Z:
			return object.getCenter().getZ();

		case R:
			return object.getRadius();

		default:
			return 0;
		}
	}
	
	private class ObjectFieldEventListener implements FocusListener {
		private Field field;
		
		public ObjectFieldEventListener(Field field) {
			this.field = field;
		}
		
		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			update(field);
		}
		
	}
}
