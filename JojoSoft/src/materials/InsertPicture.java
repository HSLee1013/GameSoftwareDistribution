package materials;

/* 해당 클래스는 이미지 파일 데이터를 문자열로 변환하여
 * 데이터 베이스에 등록할 때만 사용하는 클래스입니다.
 * path에 자신의 컴퓨터에 저장되어 있는 이미지 경로와 이미지 이름을 작성
 * pictureDAO.insert메소드에 저장할 이름을 작성하여 실행시키면
 * 자동으로 데이터 베이스에 등록됩니다.
 */
public class InsertPicture {
//	static PictureDAO pictureDAO;
//	
//	public static void main(String[] args) {
//		pictureDAO = new PictureDAO();
//		
//<<<<<<< HEAD
//<<<<<<< HEAD
//		Path path = Paths.get("C:\\Users\\GGG\\Desktop\\자유주제 프로젝트", "소닉.jpg"); // 경로와 이미지파일 이름 작성
//=======
//		Path path = Paths.get("C:\\Users\\GGG\\Desktop\\춘식\\팀프", "naughtydog.jpg"); // 경로와 이미지파일 이름 작성
//>>>>>>> branch 'Stupefyee' of https://github.com/HSLee1013/GameSoftwareDistribution.git
//		try {
//			byte[] bytes = Files.readAllBytes(path);
//			
//<<<<<<< HEAD
//			pictureDAO.insert("제조사:SEGA", bytes); // 데이터 베이스에 저장할 이름을 작성
//=======
//		int row = pictureDAO.insert("제조사: "+ path.getFileName().toString(), bytes); // 데이터 베이스에 저장할 이름을 작성
//			System.out.println("삽입된 행 개수: " + row);
//>>>>>>> branch 'Stupefyee' of https://github.com/HSLee1013/GameSoftwareDistribution.git
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Path path = Paths.get("C:\\Users\\GGG\\Desktop\\춘식\\팀프", "eddy.jpg"); // 경로와 이미지파일 이름 작성
//		try {
//			byte[] bytes = Files.readAllBytes(path);
//			
//		int row = pictureDAO.insert("DLC: "+ path.getFileName().toString(), bytes); // 데이터 베이스에 저장할 이름을 작성
//			System.out.println("삽입된 행 개수: " + row);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
