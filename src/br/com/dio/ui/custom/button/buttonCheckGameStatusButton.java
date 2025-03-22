package br.com.dio.ui.custom.button;

import java.awt.event.ActionListener;
import javax.swing.JButton;

public class buttonCheckGameStatusButton extends JButton {
	
	public buttonCheckGameStatusButton(final ActionListener actionListener) {
		this.setText("Verificar jogo");
		this.addActionListener(actionListener);
	}
	
}
