package admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import game.Game;
import game.GameService;
import materials.DataManager;
import picture.Picture;

public class PnlAddGame extends JPanel implements ActionListener {
	private JTextField tfName;
	private JTextField tfPrice;
	private JTextField tfDiscount;
	private JTextField tfAge;
	private JTextField tfGenre;
	private JTextField tfProduction;
	private JTextArea taInfo;
	private JTextField tfRelease;
	private JLabel lblThumnailFile;
	private JLabel lblInGameFile;
	private JComboBox<String> cbCategory;
	private JFileChooser fileChooser;
	private File fileThumnail;
	private File fileInGame;
	private GameService gameService;

	public PnlAddGame() {
		setLayout(new BorderLayout());
		gameService = new GameService();

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("이미지 파일", "jpg", "png", "gif", "bmp"));

		JPanel pnlCenter = new JPanel(new BorderLayout());
		JPanel pnlFiled = new JPanel(new GridLayout(0, 1));
		JPanel pnlValue = new JPanel(new GridLayout(0, 1));
		JLabel lblName = createLineLabel("게임 이름", pnlFiled);

		lblName.setBorder(new LineBorder(Color.BLACK));
		tfName = new JTextField();
		pnlValue.add(tfName);

		JLabel lblPrice = createLineLabel("가격", pnlFiled);
		tfPrice = new JTextField();
		pnlValue.add(tfPrice);

		JLabel lblDiscount = createLineLabel("할인율", pnlFiled);
		tfDiscount = new JTextField();
		pnlValue.add(tfDiscount);

		JLabel lblAge = createLineLabel("연령제한", pnlFiled);
		tfAge = new JTextField();
		pnlValue.add(tfAge);

		JLabel lblGenre = createLineLabel("장르", pnlFiled);
		tfGenre = new JTextField();
		pnlValue.add(tfGenre);

		JLabel lblProduction = createLineLabel("제작사", pnlFiled);
		tfProduction = new JTextField();
		pnlValue.add(tfProduction);

		JLabel lblInfo = createLineLabel("정보", pnlFiled);
		taInfo = new JTextArea();
		taInfo.setLineWrap(true);

		JScrollPane js = new JScrollPane(taInfo);
		js.getVerticalScrollBar().setUnitIncrement(10);
		js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pnlValue.add(js);

		JLabel lblRelease = createLineLabel("출시일", pnlFiled);
		tfRelease = new JTextField();
		pnlValue.add(tfRelease);

		JLabel lblCategory = createLineLabel("카테고리", pnlFiled);
		String[] categories = { "기본게임", "DLC", "번들" };
		cbCategory = new JComboBox<>(categories);
		pnlValue.add(cbCategory);

		JLabel lblThumnail = createLineLabel("썸네일 이미지", pnlFiled);
		JPanel pnlThumnail = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlValue.add(pnlThumnail);
		lblThumnailFile = new JLabel();
		pnlThumnail.add(lblThumnailFile);
		JButton btnThumnail = new JButton("썸네일 이미지 가져오기");
		btnThumnail.addActionListener(this);
		pnlThumnail.add(btnThumnail);

		JLabel lblInGame = createLineLabel("인게임 이미지", pnlFiled);
		JPanel pnlInGame = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlValue.add(pnlInGame);
		lblInGameFile = new JLabel();
		pnlInGame.add(lblInGameFile);
		JButton btnInGame = new JButton("인게임 이미지 가져오기");
		btnInGame.addActionListener(this);
		pnlInGame.add(btnInGame);

		JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnAdd = new JButton("추가");
		btnAdd.addActionListener(this);
		pnlSouth.add(btnAdd);
		JButton btnReset = new JButton("초기화");
		btnReset.addActionListener(this);
		pnlSouth.add(btnReset);

		pnlCenter.add(pnlFiled, "West");
		pnlCenter.add(pnlValue);
		add(pnlCenter);
		add(pnlSouth, "South");
	}

	public JLabel createLineLabel(String text, JPanel pnl) {
		JLabel lbl = new JLabel(text);
		lbl.setBorder(new LineBorder(Color.BLACK));
		pnl.add(lbl);
		return lbl;
	}

	public void update() {
		tfName.setText("");
		tfPrice.setText("");
		tfDiscount.setText("");
		tfAge.setText("");
		tfGenre.setText("");
		tfProduction.setText("");
		taInfo.setText("");
		tfRelease.setText("");
		cbCategory.setSelectedItem("");
		lblThumnailFile.setText("이미지 없음");
		lblInGameFile.setText("이미지 없음");
		fileThumnail = null;
		fileInGame = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("썸네일 이미지 가져오기")) {
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				fileThumnail = fileChooser.getSelectedFile();
				lblThumnailFile.setText(fileThumnail.getAbsolutePath());
			}
		} else if (command.equals("인게임 이미지 가져오기")) {
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				fileInGame = fileChooser.getSelectedFile();
				lblInGameFile.setText(fileInGame.getAbsolutePath());
			}
		} else if (command.equals("추가")) {
			if (tfName.getText().length() == 0 || tfPrice.getText().length() == 0 || tfDiscount.getText().length() == 0
					|| tfAge.getText().length() == 0 || tfGenre.getText().length() == 0 || tfProduction.getText().length() == 0
					|| taInfo.getText().length() == 0 || tfRelease.getText().length() == 0 || fileThumnail == null
					|| fileInGame == null) {
				JOptionPane.showMessageDialog(this, "정보를 모두 입력해주세요!");
				return;
			}
			try {
				String game_name = tfName.getText();
				int game_price = Integer.parseInt(tfPrice.getText());
				int game_discount = Integer.parseInt(tfDiscount.getText());
				int age_limit = Integer.parseInt(tfAge.getText());
				String game_genre = tfGenre.getText();
				String game_production = tfProduction.getText();
				String game_info = taInfo.getText();
				Date game_release = Date.valueOf(tfRelease.getText());
				String game_category = (String) cbCategory.getSelectedItem();
				Game g = new Game(-1, game_name, game_price, game_discount, age_limit, game_genre, game_production, game_info,
						game_release, -1, game_category);
				Picture picThumnail = new Picture("썸네일: " + fileThumnail.getName(), Files.readAllBytes(fileThumnail.toPath()));
				Picture picInGame = new Picture("인게임: " + fileInGame.getName(), Files.readAllBytes(fileInGame.toPath()));
				gameService.insertGame(g, picThumnail, picInGame);

				JOptionPane.showMessageDialog(this, "게임 추가 완료!");
				((PnlBasicAdmin) DataManager.getData("pnlBasic")).changeScreenToMain();
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "숫자 입력 오류!");
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "날짜 입력 오류!");
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		} else if (command.equals("초기화")) {
			update();
		}
	}

}
