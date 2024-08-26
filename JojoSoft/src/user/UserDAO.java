package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import materials.DBUtil;
import materials.Function;
import materials.IResultMapper;

public class UserDAO {
	public static final IResultMapper<User> userMapper = new UserMapper();

	// 로그인 할 때 아이디와 패스워드를 받아와 디비에 존재하는 유저인지 확인하는 메소드
	public User findMember(String userId, String userPw) {
		String sql = "select * from user where user_id = ? and user_pw = ?";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection("jojosoft");
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			String changePw = Function.changePW(userPw);
			stmt.setString(2, changePw);

			rs = stmt.executeQuery();

			if (rs.next()) {
				return userMapper.resultMapping(rs);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "예외 발생, UserDAO 클래스 find 검토 요망");
		} finally {
			DBUtil.closeAll(rs, stmt, conn);
		}

		return null;
	}

	// 회원가입 시 중복된 아이디 또는 중복된 닉네임이 있는지 체크하는 메소드
	public String checkUser(String userId, String userNickName, String birth, String phoneNum) {
		String sqlId = "select * from user where user_id = ?";
		String sqlNick = "select * from user where user_nickname = ?";
		String sqlPhone = "select * from user where user_phonenumber = ?";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement stmt3 = null;
		ResultSet rs3 = null;
		try {
			conn = DBUtil.getConnection("jojosoft");
			stmt = conn.prepareStatement(sqlId);
			stmt.setString(1, userId);

			rs = stmt.executeQuery();
			if (rs.next()) {
				return "아이디";
			}

			stmt2 = conn.prepareStatement(sqlNick);
			stmt2.setString(1, userNickName);
			rs2 = stmt2.executeQuery();
			if (rs2.next()) {
				return "닉네임";
			}

			stmt3 = conn.prepareStatement(sqlPhone);
			stmt3.setString(1, phoneNum);
			rs3 = stmt3.executeQuery();
			if (rs3.next()) {
				return "전화번호";
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "예외 발생, UserDAO 클래스 check 검토 요망");
		} finally {
			DBUtil.closeAll(rs, stmt, null);
			DBUtil.closeAll(rs2, stmt2, null);
			DBUtil.closeAll(rs3, stmt3, conn);
		}

		String exp = "^(19[1-9][0-9]|200[0-9]|201[0-9]|2020)-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
		String exp2 = "^(010-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9])$";
		// 정규표현식으로 문자열의 동등함을 비교
		Pattern p = Pattern.compile(exp);
		Matcher m = p.matcher(birth);
		Pattern p2 = Pattern.compile(exp2);
		Matcher m2 = p2.matcher(phoneNum);

		if (!m2.matches()) {
			return "전화번호양식";
		} else if (!m.matches()) {
			return "생년월일";
		} else {
			return null;
		}
	}

	// 유저를 데이터 베이스에 저장하는 메소드
	public int insertUser(String userId, String userPw, String userNickName, String userBirth, String userPhoneNumber) {
		String sql = "insert into user(user_id, user_pw, user_nickname, user_birth, user_phonenumber) values (?, ?, ?, ?, ?);";
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBUtil.getConnection("jojosoft");
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			String changePw = Function.changePW(userPw);
			stmt.setString(2, changePw);
			stmt.setString(3, userNickName);
			stmt.setString(4, userBirth);
			stmt.setString(5, userPhoneNumber);

			result = stmt.executeUpdate();
			if (result == 1) {
				return result;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "예외 발생, UserDAO 클래스 insert 검토 요망");
		} finally {
			DBUtil.closeAll(null, stmt, conn);
		}

		return result;
	}

	// 전화번호를 이용하여 유저 아이디를 찾는 메소드
	public int findIdToUsingPhoneNumber(String phoneNumber) {

		String exp = "^(010-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9])$";
		// 정규표현식으로 문자열의 동등함을 비교
		Pattern p = Pattern.compile(exp);
		Matcher m = p.matcher(phoneNumber);

		if (!m.matches()) {
			JOptionPane.showMessageDialog(null, "전화번호 양식이 올바르지 않습니다. ex : 010-1234-1234");
		} else {
			String sql = "select user_id from user where user_phonenumber = ?";

			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.getConnection("jojosoft");
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, phoneNumber);

				rs = stmt.executeQuery();
				if (rs.next()) {
					JOptionPane.showMessageDialog(null, "아이디 : " + rs.getString("user_id"));
					return 1;
				} else {
					JOptionPane.showMessageDialog(null, "해당 전화번호로 등록된 아이디가 없습니다.");
					return 0;
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "예외 발생, function 클래스 검토 요망");
			} finally {
				DBUtil.closeAll(rs, stmt, conn);
			}
		}
		return 0;
	}

	// 아이디를 이용하여 유저를 찾거나 비밀번호를 변경할 수 있는 메소드
	public int findIdToUsingId(String select, String userId, String userPw) {

		String sql = null;
		if (select.equals("아이디로 찾기")) {
			sql = "select user_id from user where user_id= ?";
		} else if (select.equals("비밀번호 변경")) {
			sql = "update user set user_pw = ? where user_id = ?";
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection("jojosoft");
			stmt = conn.prepareStatement(sql);

			if (select.equals("아이디로 찾기") && userId != null) {
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				if (rs.next()) {
					JOptionPane.showMessageDialog(null, "유저 확인 완료");
					return 1;
				} else {
					JOptionPane.showMessageDialog(null, "해당 아이디로 등록된 기록이 없습니다.");
					return 0;
				}

			} else if (select.equals("비밀번호 변경")) {
				String changePw = Function.changePW(userPw);

				stmt.setString(1, changePw);
				stmt.setString(2, userId);

				int result = stmt.executeUpdate();
				if (result == 1) {
					JOptionPane.showMessageDialog(null, "비밀번호 변경이 완료되었습니다.");
				}
				return result;
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "예외 발생, function 클래스 검토 요망");
		} finally {
			DBUtil.closeAll(rs, stmt, conn);
		}

		return 0;
	}

}
