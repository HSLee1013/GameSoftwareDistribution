import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import materials.DBUtil;
import materials.Function;
import materials.PictureDAO;
import materials.User;


// 회원가입 버튼을 눌렀을 때 나오는 다이얼로그 창 화면
class AccessionMember extends JDialog implements ActionListener{
	private JTextField idField;
	private JTextField pwField;
	private JTextField nickNameField;
	private JTextField birthField;
	private JLabel warningLbl;
	private Function function;
	private Connection conn;
	

	public AccessionMember(PictureDAO pictureDAO, Function function, Connection conn) {
		
		this.function = function;
		this.conn = conn;
		JPanel pnlCenter = new JPanel();
		JPanel pnlSouth = new JPanel();
		JPanel pnlNorth = new JPanel();
		
		idField = new JTextField(10);
		idField.setBounds(280, 43, 116, 21);
		pwField = new JTextField(10);
		pwField.setBounds(280, 80, 116, 21);
		nickNameField = new JTextField(10);
		nickNameField.setBounds(280, 115, 116, 21);
		birthField = new JTextField(10);
		birthField.setBounds(280, 148, 116, 21);
		
		JLabel idLbl = new JLabel("아이디 : ");
		idLbl.setBounds(179, 46, 60, 15);
		JLabel pwLbl = new JLabel("비밀번호 : ");
		pwLbl.setBounds(179, 83, 77, 15);
		JLabel nickNameLbl = new JLabel("닉네임 : ");
		nickNameLbl.setBounds(179, 118, 67, 15);
		JLabel birthLbl = new JLabel("생년월일 : ");
		birthLbl.setBounds(179, 151, 77, 15);
		
		JLabel image1 = new JLabel();
		byte[] bytes = pictureDAO.getData(1);
		ImageIcon icon = new ImageIcon(bytes);
		image1.setIcon(icon);
		
		JLabel image2 = new JLabel();
		byte[] bytes2 = pictureDAO.getData(3);
		ImageIcon icon2 = new ImageIcon(bytes2);
		image2.setIcon(icon2);
		
		warningLbl = new JLabel("");
		warningLbl.setHorizontalAlignment(SwingConstants.CENTER);
		warningLbl.setBounds(115, 220, 343, 21);
		pnlCenter.add(warningLbl);
		
		pnlCenter.setLayout(null);
		
		pnlCenter.add(idLbl);
		pnlCenter.add(idField);
		pnlCenter.add(pwLbl);
		pnlCenter.add(pwField);
		pnlCenter.add(nickNameLbl);
		pnlCenter.add(nickNameField);
		pnlCenter.add(birthLbl);
		pnlCenter.add(birthField);
		pnlNorth.add(image1);
		pnlSouth.add(image2);
		getContentPane().add(pnlCenter, "Center");
		
		JButton registBtn = new JButton("등록하기");
		registBtn.setBounds(236, 187, 97, 23);
		pnlCenter.add(registBtn);
		registBtn.addActionListener(this);
		
		getContentPane().add(pnlSouth, "South");
		getContentPane().add(pnlNorth, "North");
		
		setModal(true);
		setSize(600, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public JTextField getIdField() {
		return idField;
	}

	public void setIdField(JTextField idField) {
		this.idField = idField;
	}

	public JTextField getNickNameField() {
		return nickNameField;
	}

	public void setNickNameField(JTextField nickNameField) {
		this.nickNameField = nickNameField;
	}

	public JLabel getWarningLbl() {
		return warningLbl;
	}

	public void setWarningLbl(JLabel warningLbl) {
		this.warningLbl = warningLbl;
	}
	
	// 회원가입 버튼을 눌렀을 때의 액션리스너
	@Override
	public void actionPerformed(ActionEvent e) {
		String id = idField.getText();
		String pw = pwField.getText();
		String nickName = nickNameField.getText();
		String birth = birthField.getText();
		
		if (id.equals("") || pw.equals("") || nickName.equals("") || birth.equals("")) {
			warningLbl.setText("모든 칸에 입력하라고 ㅡㅡ");
		} else {
			String result = function.checkUser(id, nickName, birth, conn);
			if (result == null) {
				this.setVisible(false);
				function.insertUser(conn, id, pw, nickName, birth);
				JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
			} else if (result.equals("닉네임")) {
				warningLbl.setText("이미 존재하는 닉네임 입니다.");
			} else if (result.equals("아이디")) {
				warningLbl.setText("이미 존재하는 아이디 입니다.");
			} else if (result.equals("생년월일")) {
				warningLbl.setText("생년월일의 양식을 맞춰주세요 ex : 2012-08-04");
			}
		}
	}
}

// 로그인 화면
public class Login extends JFrame implements ActionListener {
	PictureDAO pictureDAO;
	private JTextField idField;
	private JTextField pwField;
	private Function function;
	Connection conn;

	public Login() {
		super("로그인");

		conn = null;
		try {
			conn = DBUtil.getConnection("jojosoft");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JPanel centerPnl = new JPanel();
		JPanel southPnl = new JPanel();
		JPanel northPnl = new JPanel();
		JLabel jojoImage = new JLabel();
		JLabel adImage = new JLabel();
		byte[] bytes1 = getPictureBytes(1);
		byte[] bytes2 = getPictureBytes(2);
		ImageIcon icon1 = new ImageIcon(bytes1);
		ImageIcon icon2 = new ImageIcon(bytes2);
		jojoImage.setIcon(icon1);
		adImage.setIcon(icon2);

		JLabel idLbl = new JLabel("아이디 : ");
		idLbl.setBounds(189, 62, 60, 15);
		JLabel pwLbl = new JLabel("비밀번호 : ");
		pwLbl.setBounds(189, 103, 80, 15);

		idField = new JTextField(10);
		idField.setBounds(287, 59, 116, 21);
		pwField = new JTextField(10);
		pwField.setBounds(287, 100, 116, 21);

		JButton loginBtn = new JButton("로그인");
		loginBtn.setBounds(189, 183, 97, 23);
		loginBtn.addActionListener(this);

		JButton joinMemberBtn = new JButton("회원 가입");
		joinMemberBtn.setBounds(306, 183, 97, 23);
		joinMemberBtn.addActionListener(this);

		northPnl.add(jojoImage);
		centerPnl.setLayout(null);
		centerPnl.add(idLbl);
		centerPnl.add(idField);
		centerPnl.add(pwLbl);
		centerPnl.add(pwField);
		centerPnl.add(loginBtn);
		centerPnl.add(joinMemberBtn);
		southPnl.add(adImage);

		getContentPane().add(centerPnl, "Center");
		getContentPane().add(southPnl, "South");
		getContentPane().add(northPnl, "North");

		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		function = new Function();
		AccessionMember accessionMember = new AccessionMember(pictureDAO, function, conn);
		String command = e.getActionCommand();
		if (command.equals("로그인")) {
			
			User user = function.findMember(idField.getText(), pwField.getText(), conn);
			
			if (user == null) {
				JOptionPane.showMessageDialog(this, "없는 회원입니다.");
			} else {
				JOptionPane.showMessageDialog(this, "환영합니다.");
			}
			
		} else if (command.equals("회원 가입")) {
			accessionMember.setVisible(true);
		} 
	}

	public byte[] getPictureBytes(int key) {
		pictureDAO = new PictureDAO();
		return pictureDAO.getData(key);
	}

	public static void main(String[] args) {
		new Login().setVisible(true);
	}
}
