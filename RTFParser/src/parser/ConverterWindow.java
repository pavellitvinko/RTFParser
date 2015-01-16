package parser;

import java.io.File;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ConverterWindow extends ApplicationWindow {
	private Text txtInput;
	private Text txtOutput;
	private Button btnPlainText;
	private Button btnHtmlDocument;
	private Button btnConvert;
	private Label lblResult;

	/**
	 * Create the application window.
	 */
	public ConverterWindow() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		txtInput = new Text(container, SWT.BORDER);
		txtInput.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				lblResult.setVisible(false);
				if (new File(txtInput.getText()).isFile()) {
					txtOutput.setText(txtInput.getText().concat(".txt"));
					btnConvert.setEnabled(true);
				} else
					btnConvert.setEnabled(false);
			}
		});
		txtInput.setBounds(125, 12, 265, 21);

		txtOutput = new Text(container, SWT.BORDER);
		txtOutput.setBounds(125, 44, 265, 22);

		Label lblInputFile = new Label(container, SWT.NONE);
		lblInputFile.setBounds(31, 15, 88, 21);
		lblInputFile.setText("Input RTF file:");

		Label lblOutputFile = new Label(container, SWT.NONE);
		lblOutputFile.setText("Output File:");
		lblOutputFile.setBounds(31, 47, 88, 19);

		btnPlainText = new Button(container, SWT.RADIO);
		btnPlainText.setSelection(true);
		btnPlainText.setBounds(145, 105, 167, 18);
		btnPlainText.setText("Plain text");

		btnHtmlDocument = new Button(container, SWT.RADIO);
		btnHtmlDocument.setBounds(145, 134, 167, 18);
		btnHtmlDocument.setText("HTML Document");

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Lucida Grande", 11,
				SWT.BOLD));
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(145, 82, 155, 19);
		lblNewLabel.setText("Choose output format:");

		btnConvert = new Button(container, SWT.NONE);
		btnConvert.setEnabled(false);
		btnConvert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnPlainText.getSelection()) {
					Parser.convertRTF(txtInput.getText(), txtOutput.getText(),
							new TextConverter());
				} else if (btnHtmlDocument.getSelection()) {
					Parser.convertRTF(txtInput.getText(), txtOutput.getText(),
							new HTMLConverter());
				}
				lblResult.setText("Done!");
				lblResult.setVisible(true);

			}
		});
		btnConvert.setBounds(168, 167, 94, 28);
		btnConvert.setText("Convert!");

		lblResult = new Label(container, SWT.NONE);
		lblResult.setVisible(false);
		lblResult.setBounds(268, 174, 59, 14);
		lblResult.setText("Result");

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * 
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * 
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			ConverterWindow window = new ConverterWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("RTF Simple Converter");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 272);
	}
}
