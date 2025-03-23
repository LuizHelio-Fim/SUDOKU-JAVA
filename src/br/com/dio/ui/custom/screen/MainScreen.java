package br.com.dio.ui.custom.screen;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.EventEnum;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.button.buttonCheckGameStatusButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

public class MainScreen {

	private final static Dimension dimension = new Dimension(600, 600);
	
	private final BoardService boardService;
	private final NotifierService notifierService;
	
	private JButton finishGameButton;
	private JButton checkGameStatusButton;
	private JButton resetButton;
	
	public MainScreen(final Map<String, String> gameConfig) {
		this.boardService = new BoardService(gameConfig);
		this.notifierService = new NotifierService();
	}
	
	public void buildMainScreen() {
		JPanel mainPanel = new MainPanel(dimension);
		JFrame mainFrame = new MainFrame(dimension, mainPanel);
		for (int r = 0; r < 9; r+=3) {
			var endRow = r + 2;
			for (int c = 0; c < 9; c+=3) {
				var endCol = c + 2;
				var spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
				JPanel sector = generateSection(spaces);
				mainPanel.add(sector);
			}
		}
		addResetButton(mainPanel);
		addCheckGameStatusButton(mainPanel);
		addFinishGameButton(mainPanel);
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	private List<Space> getSpacesFromSector(List<List<Space>> spaces, final int initCol, final int endCol, final int initRow, final int endRow) {
		List<Space> spaceSector = new ArrayList<>();
		for (int r = initRow; r <= endRow; r++) {
			for (int c = initCol; c <= endCol; c++) {
				spaceSector.add(spaces.get(c).get(r));
			}
		}
		return spaceSector;
	}
	
	private JPanel generateSection(final List<Space> spaces) {
		List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
		fields.forEach(t -> notifierService.subscribe(EventEnum.CLEAR_SPACE, t));
		return new SudokuSector(fields);
	}

	private void addResetButton(JPanel mainPanel) {
		resetButton = new ResetButton(e -> {
			var dialogueResult = JOptionPane.showConfirmDialog(
					null,
					"Deseja realmente reiniciar o jogo?",
					"Limpar o jogo",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE
				);
				if (dialogueResult == 0) {
					boardService.reset();
					notifierService.notify(EventEnum.CLEAR_SPACE);
				}
		});
		mainPanel.add(resetButton);
		
	}

	private void addCheckGameStatusButton(JPanel mainPanel) {
		 checkGameStatusButton = new buttonCheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErros();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus){
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está imcompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(MainScreen.this.checkGameStatusButton);
		
	}

	private void addFinishGameButton(JPanel mainPanel) {
		 finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameIsFinished()){
                JOptionPane.showMessageDialog(null, "Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                var message = "Seu jogo tem alguma inconsistência, ajuste e tente novamente";
                JOptionPane.showMessageDialog(null, message);
            }
        });
        mainPanel.add(finishGameButton);
	}
}
