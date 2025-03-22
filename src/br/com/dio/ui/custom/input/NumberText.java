package br.com.dio.ui.custom.input;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.com.dio.model.Space;
import static java.awt.Font.PLAIN;

public class NumberText extends JTextField{

	private final Space space;
	
	public NumberText(final Space space) {
		this.space = space;
		var dimension = new Dimension(50,50);
		this.setSize(dimension);
		this.setPreferredSize(dimension);
		this.setVisible(true);
		this.setFont(new Font("Arial", PLAIN, 20));
		this.setHorizontalAlignment(CENTER);
		this.setDocument(new NumberTextLimit());
		this.setEnabled(!space.isFixed());
		if(space.isFixed()) {
			this.setText(space.getActual().toString());
		}
		this.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				changeSpace();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changeSpace();				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				changeSpace();				
			}
			
			private void changeSpace() {
				if(getText().isBlank()) {
					space.clearSpace();
					return;
				}
				space.setActual(Integer.parseInt(getText()));
			}
		});
	}
}
